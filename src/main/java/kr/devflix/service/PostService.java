package kr.devflix.service;

import kr.devflix.constant.MemberStatus;
import kr.devflix.constant.Status;
import kr.devflix.dto.PostDto;
import kr.devflix.entity.Member;
import kr.devflix.entity.Post;
import kr.devflix.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
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
    public Page<Post> findAllByStatusAndPageRequest(Status status, int page, int size) {
        return postRepository.findAll((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("status"), status);
        }, PageRequest.of(page, size, Sort.by(Sort.Order.desc("createAt"))));
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

    public Page<Post> findAllByStatusAndWriterStatusAndPageRequest(Status postStatus, MemberStatus writerStatus, final int page, final int size) {
        return postRepository.findAll((root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), postStatus), criteriaBuilder.equal(root.join("writer").get("status"), writerStatus));
        }, PageRequest.of(page, size, Sort.by(Sort.Order.desc("createAt"))));
    }
}
