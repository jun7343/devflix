package com.devflix.service;

import com.devflix.constant.PostStatus;
import com.devflix.dto.PostDto;
import com.devflix.entity.Member;
import com.devflix.entity.Post;
import com.devflix.repository.PostRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public Post write(PostDto postDto, Member writer) {
        Post post = Post.builder()
                .postStatus(PostStatus.POST)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .pathBase(postDto.getPathBase())
                .images(postDto.getImages())
                .writer(writer)
                .createDate(new Date())
                .updateDate(new Date())
                .build();

        return postRepository.save(post);
    }
}
