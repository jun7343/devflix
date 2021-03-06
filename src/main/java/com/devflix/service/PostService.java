package com.devflix.service;

import com.devflix.dto.PostDto;
import com.devflix.entity.Post;
import com.devflix.repository.DevPostRepository;
import com.devflix.repository.PostRepository;
import lombok.RequiredArgsConstructor;
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
    public Post get(long id) {
        return postRepository.getOne(id);
    }

    @Transactional
    public Post createPost(PostDto dto) {
        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .pathBase(dto.getPathBase())
                .devPostUrl(dto.getDevPostUrl())
                .images(dto.getImages())
                .createDate(new Date())
                .updateDate(new Date())
                .build();

        return postRepository.save(post);
    }
}
