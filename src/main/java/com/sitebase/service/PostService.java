package com.sitebase.service;

import com.sitebase.constant.PostStatus;
import com.sitebase.dto.PostDto;
import com.sitebase.entity.Member;
import com.sitebase.entity.Post;
import com.sitebase.repository.PostRepository;
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
