package kr.devflix.service;

import kr.devflix.constant.MemberStatus;
import kr.devflix.constant.Status;
import kr.devflix.dto.PostDto;
import kr.devflix.entity.Member;
import kr.devflix.entity.Post;
import kr.devflix.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Post write(final Post post) {
        return postRepository.save(post);
    }

    @Transactional
    public Post createPost(final PostDto dto) {
        Post post = Post.builder()
                .status(dto.getStatus())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .pathBase(dto.getPathBase())
                .devPostUrl(dto.getDevPostUrl())
                .images(dto.getImages())
                .view(dto.getView())
                .createAt(new Date())
                .updateAt(new Date())
                .build();

        return postRepository.save(post);
    }

    @Transactional
    public void updatePost(final PostDto dto) {
        Post post = Post.builder()
                .id(dto.getId())
                .status(dto.getStatus())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .pathBase(dto.getPathBase())
                .devPostUrl(dto.getDevPostUrl())
                .images(dto.getImages())
                .view(dto.getView())
                .createAt(dto.getCreateAt())
                .updateAt(dto.getUpdateAt())
                .build();

        postRepository.save(post);
    }

    @Transactional
    public Optional<Post> findOneById(final long id) {
        return postRepository.findOne((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("id"), id);
        });
    }

    @Transactional
    public void updateAllStatusByWriter(Status status, final Member writer) {
        postRepository.updateAllStatusByWriter(status, writer);
    }

    @Transactional
    public Page<Post> findAllByStatusAndWriterStatusAndPageRequest(Status postStatus, MemberStatus writerStatus, final int page, final int size) {
        return postRepository.findAll((root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), postStatus), criteriaBuilder.equal(root.join("writer").get("status"), writerStatus));
        }, PageRequest.of(page, size, Sort.by(Sort.Order.desc("createAt"))));
    }

    @Transactional
    public Page<Post> findAllByWriterAndStatusAndPageRequest(final Member user, Status status, final int page, final int size) {
        return postRepository.findAll((root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(criteriaBuilder.equal(root.get("writer"), user), criteriaBuilder.equal(root.get("status"), status));
        }, PageRequest.of(page, size, Sort.by(Sort.Order.desc("createAt"))));
    }

    @Transactional
    public Page<Post> findAllBySearchAndStatusAndWrtierStatusAndPageRequest(final String search, Status postStatus, final MemberStatus writerStatus,
                                                                            final int page, final int size) {
        return postRepository.findAll((root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(criteriaBuilder.like(root.get("title"), "%" + search + "%"), criteriaBuilder.equal(root.get("status"), postStatus),
                    criteriaBuilder.equal(root.join("writer").get("status"), writerStatus));
        }, PageRequest.of(page, size, Sort.by(Sort.Order.desc("createAt"))));
    }

    @Transactional
    public Page<Post> findAll(final int page, final int size) {
        return postRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Order.desc("createAt"))));
    }

    @Transactional
    public void updateStatusByIdList(Status status, List<Long> idList) {
        postRepository.updateStatusByIdList(status, idList);
    }

    @Transactional
    public Page<Post> findAllBySearch(final String title, final String writer, final Status status,
                                      final int page, final int size) {
        return postRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();

            if (! StringUtils.isBlank(title)) {
                list.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
            }

            if (! StringUtils.isBlank(writer)) {
                list.add(criteriaBuilder.equal(root.join("writer").get("username"), writer));
            }

            if (status != null) {
                list.add(criteriaBuilder.equal(root.get("status"), status));
            }

            Predicate[] predicates = list.toArray(new Predicate[0]);

            return criteriaBuilder.and(predicates);
        }, PageRequest.of(page, size, Sort.by(Sort.Order.desc("createAt"))));
    }
}
