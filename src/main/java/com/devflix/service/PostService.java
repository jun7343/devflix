package com.devflix.service;

import com.devflix.constant.PostStatus;
import com.devflix.dto.PostDto;
import com.devflix.entity.Post;
import com.devflix.repository.DevPostRepository;
import com.devflix.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final DevPostRepository devPostRepository;

    @Transactional
    public Post write(Post post) {

        return postRepository.save(post);
    }

    @Transactional
    public Post createPost(PostDto dto) {
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

    public Page<Post> findAllByStatusAndPageRequest(PostStatus status, int page, int size) {
        return postRepository.findAll((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("status"), status);
        }, PageRequest.of(page, size, Sort.by(Sort.Order.desc("createAt"))));
    }
}
