package com.sitebase.service;

import com.sitebase.command.PostCommand;
import com.sitebase.entity.Member;
import com.sitebase.entity.Post;
import com.sitebase.repository.PostRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;


public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public void writePost(PostCommand postCommand, Member writer) {
        Post post = Post.builder()
                .title(postCommand.getTitle())
                .content(postCommand.getContent())
                .createdDate(new Date())
                .updatedDate(new Date())
                .build();
    }
}
