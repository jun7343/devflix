package com.devflix.service;

import com.devflix.dto.PostCommentDto;
import com.devflix.entity.PostComment;
import com.devflix.repository.PostCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;

    @Transactional
    public PostComment createComment(final PostCommentDto dto) {
        return postCommentRepository.save(PostComment.builder()
                .writer(dto.getWriter())
                .post(dto.getPost())
                .comment(dto.getComment())
                .createAt(new Date())
                .updateAt(new Date())
                .build());
    }
}
