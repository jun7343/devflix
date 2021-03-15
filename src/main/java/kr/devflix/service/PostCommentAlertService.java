package kr.devflix.service;

import kr.devflix.entity.Member;
import kr.devflix.entity.Post;
import kr.devflix.entity.PostCommentAlert;
import kr.devflix.repository.PostCommentAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCommentAlertService {

    private final PostCommentAlertRepository postCommentAlertRepository;

    @Transactional
    public List<PostCommentAlert> findAllByConfirmAndUser(final Member user) {
        return postCommentAlertRepository.findTop5ByConfirmAndUserOrderByCreateAt(false, user);
    }

    @Transactional
    public void updateAllConfirmByPostAndUser(final Post post, final Member user) {
        postCommentAlertRepository.updateAllConfirmIsTrueByPostAndUser(post, user);
    }

    @Transactional
    public Page<PostCommentAlert> findAllByUserOrderByCreateAtDesc(final Member user, final int page, final int size) {
        return postCommentAlertRepository.findAll((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("user"), user);
        }, PageRequest.of(page,size, Sort.by(Sort.Order.desc("createAt"))));
    }
}
