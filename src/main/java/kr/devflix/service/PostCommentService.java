package kr.devflix.service;

import kr.devflix.constant.Status;
import kr.devflix.entity.Member;
import kr.devflix.entity.PostComment;
import kr.devflix.repository.PostCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostService postService;

    @Transactional
    public PostComment createComment(final long postId, final String comment, final Member writer) {
        return postCommentRepository.save(PostComment.builder()
                .status(Status.POST)
                .post(postService.findOneById(postId).orElse(null))
                .writer(writer)
                .comment(comment)
                .createAt(new Date())
                .updateAt(new Date())
                .build());
    }

    @Transactional
    public Page<PostComment> findAllByPostIdAndStatusAndPageRequest(final long id, Status status, final int page, final int size) {
        return postCommentRepository.findAll((root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(criteriaBuilder.equal(root.get("post").get("id"), id), criteriaBuilder.equal(root.get("status"), status));
        }, PageRequest.of(page, size, Sort.by(Sort.Order.asc("createAt"))));
    }

    @Transactional
    public long getCountAllByPostIdAndStatus(final long id, Status status) {
        return postCommentRepository.count((root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(criteriaBuilder.equal(root.get("post").get("id"), id), criteriaBuilder.equal(root.get("status"), status));
        });
    }

    @Transactional
    public Optional<PostComment> findOneById(final long id) {
        return postCommentRepository.findOne((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("id"), id);
        });
    }

    @Transactional
    public void updatePostComment(final PostComment comment) {
        if (comment.getId() != null) {
            postCommentRepository.save(comment);
        }
    }
}
