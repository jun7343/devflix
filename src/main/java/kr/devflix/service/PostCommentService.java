package kr.devflix.service;

import kr.devflix.constant.Status;
import kr.devflix.entity.Member;
import kr.devflix.entity.Post;
import kr.devflix.entity.PostComment;
import kr.devflix.entity.PostCommentAlert;
import kr.devflix.repository.PostCommentAlertRepository;
import kr.devflix.repository.PostCommentRepository;
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
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostCommentAlertRepository postCommentAlertRepository;
    private final PostService postService;

    @Transactional
    public PostComment createComment(final long postId, final String comment, final Member writer) {
        final Post post = postService.findOneById(postId).orElse(null);

        final PostComment save = postCommentRepository.save(PostComment.builder()
                .status(Status.POST)
                .post(post)
                .writer(writer)
                .comment(comment)
                .createAt(new Date())
                .updateAt(new Date())
                .build());

        if (post != null && ! post.getWriter().getId().equals(writer.getId())) {
            postCommentAlertRepository.save(PostCommentAlert.builder()
                    .post(post)
                    .comment(save)
                    .user(post.getWriter())
                    .confirm(false)
                    .createAt(new Date())
                    .updateAt(new Date())
                    .build());
        }

        return save;
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

    @Transactional
    public Page<PostComment> findAllByWriterAndStatusAndPageRequest(final Member user, Status status, final int page, final int size) {
        return postCommentRepository.findAll((root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(criteriaBuilder.equal(root.get("writer"), user), criteriaBuilder.equal(root.get("status"), status));
        }, PageRequest.of(page, size, Sort.by(Sort.Order.desc("createAt"))));
    }

    @Transactional
    public long getTotalCountByPost(final Post post) {
        return postCommentRepository.count((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("post"), post);
        });
    }
}
