package kr.devflix.service;

import kr.devflix.constant.Status;
import kr.devflix.dto.PostDto;
import kr.devflix.entity.Member;
import kr.devflix.entity.Post;
import kr.devflix.entity.PostImage;
import kr.devflix.repository.PostImageRepository;
import kr.devflix.repository.PostRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

    public PostService(PostRepository postRepository, PostImageRepository postImageRepository) {
        this.postRepository = postRepository;
        this.postImageRepository = postImageRepository;
    }

    @Transactional(readOnly = true)
    public Page<PostDto> getAllByStatusOrSearch(Status status, String search, int page, int perPage) {
        return postRepository.findAllByStatusOrTitle(status, search, PageRequest.of(page, perPage, Sort.by(Sort.Order.desc("createAt"))))
                .map(PostDto::new);
    }

    @Transactional
    public Post createPost(final PostDto dto, Status status, final Member member) {
        checkNotNull(dto.getTitle(), "title must be porvided");

        Post save = postRepository.save(Post.builder()
                .status(status)
                .writer(member)
                .title(dto.getTitle())
                .content(dto.getContent())
                .devPostUrl(dto.getDevPostUrl())
                .view(0)
                .pathBase(dto.getPathBase())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build());

        if (StringUtils.isNoneBlank(dto.getPathBase()) && dto.getImages() != null) {
            List<PostImage> images = dto.getImages()
                    .stream()
                    .map(s -> {
                        return PostImage.builder()
                                .pathBase(dto.getPathBase())
                                .imageName(s)
                                .post(save)
                                .createAt(LocalDateTime.now())
                                .build();
                    })
                    .collect(Collectors.toList());

            postImageRepository.saveAll(images);
        }

        return save;
    }

    @Transactional
    public void updatePost(final PostDto dto) {

    }

    @Transactional
    public Optional<Post> findOneById(final long id) {
        return postRepository.findOne((root, query, criteriaBuilder) -> {
            root.fetch("writer", JoinType.LEFT);
            return criteriaBuilder.equal(root.get("id"), id);
        });
    }

    @Transactional
    public void updateAllStatusByWriter(Status status, final Member writer) {
        postRepository.updateAllStatusByWriter(status, writer);
    }

    @Transactional
    public Page<Post> findAllByWriterAndStatusAndPageRequest(final Member user, Status status, final int page, final int size) {
        return postRepository.findAll((root, query, criteriaBuilder) -> {
            root.fetch("writer", JoinType.LEFT);
            return criteriaBuilder.and(criteriaBuilder.equal(root.get("writer"), user), criteriaBuilder.equal(root.get("status"), status));
        }, PageRequest.of(page, size, Sort.by(Sort.Order.desc("createAt"))));
    }


    @Transactional
    public Page<Post> findAll(final int page, final int size) {
        return postRepository.findAll((root, query, criteriaBuilder) -> {
            root.fetch("writer", JoinType.LEFT);
            return query.getRestriction();
        },PageRequest.of(page, size, Sort.by(Sort.Order.desc("createAt"))));
    }

    @Transactional
    public void updateStatusByIdList(Status status, List<Long> idList) {
        postRepository.updateStatusByIdList(status, idList);
    }

    @Transactional
    public Page<Post> findAllBySearch(final String title, final String writer, final Status status,
                                      final int page, final int size) {
        return postRepository.findAll((root, query, criteriaBuilder) -> {
            root.fetch("writer", JoinType.LEFT);

            List<Predicate> list = new LinkedList<>();

            if (! StringUtils.isBlank(title)) {
                list.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
            }

            if (! StringUtils.isBlank(writer)) {
                list.add(criteriaBuilder.equal(root.join("writer").get("username"), writer));
            }

            if (status != null) {
                list.add(criteriaBuilder.equal(root.get("status"), status));
            }

            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        }, PageRequest.of(page, size, Sort.by(Sort.Order.desc("createAt"))));
    }
}
