package kr.devflix.service;

import kr.devflix.constant.PostType;
import kr.devflix.constant.Status;
import kr.devflix.entity.DevPost;
import kr.devflix.repository.DevPostRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DevPostService {

    private final DevPostRepository devPostRepository;

    @Transactional
    public DevPost createDevPost(final DevPost devPost) {
        return devPostRepository.save(devPost);
    }

    @Transactional
    public DevPost findRecentlyDevPost(final String category, PostType postType) {
        return devPostRepository.findTopOneByCategoryAndPostTypeOrderByUploadAtDesc(category, postType);
    }

    @Transactional
    public Page<DevPost> findAllByCategoryAndStatusOrderByUploadAt(final String category, Status status, final int page, final int size) {
        return devPostRepository.findAll((root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(criteriaBuilder.equal(root.get("category"), category), criteriaBuilder.equal(root.get("status"), status));
        }, PageRequest.of(page, size, Sort.by(Sort.Order.desc("uploadAt"))));
    }

    @Transactional
    public Page<DevPost> findAllByTagAndStatusOrderByUploadAt(final String tag, Status status, final int page, final int size) {
        return devPostRepository.findAllByTagIn(tag, status.name(), PageRequest.of(page, size));
    }

    @Transactional
    public DevPost updateViewCount(final String url) {
        final DevPost findPost = devPostRepository.findTopOneByUrl(url);

        if (findPost != null) {
            DevPost post = DevPost.builder()
                    .id(findPost.getId())
                    .title(findPost.getTitle())
                    .description(findPost.getDescription())
                    .url(findPost.getUrl())
                    .category(findPost.getCategory())
                    .postType(findPost.getPostType())
                    .uploadAt(findPost.getUploadAt())
                    .updateAt(new Date())
                    .createAt(findPost.getCreateAt())
                    .view(findPost.getView() + 1)
                    .writer(findPost.getWriter())
                    .thumbnail(findPost.getThumbnail())
                    .tag(findPost.getTag())
                    .status(findPost.getStatus())
                    .build();

            return devPostRepository.save(post);
        } else {
            return null;
        }
    }

    @Transactional
    public List<DevPost> findAllBySearchContentAndStatus(final String content, Status status) {
        return devPostRepository.findAll((root, query, criteriaBuilder) -> {
            Order uploadAt = criteriaBuilder.desc(root.get("uploadAt"));
            Predicate result = criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), status), criteriaBuilder.like(root.get("title"), "%" + content + "%"));

            return query.where(result).orderBy(uploadAt).getRestriction();
        });
    }

    @Transactional
    public Page<DevPost> findAllByStatusAndPageRequest(Status post, int page, int size) {
        return devPostRepository.findAll((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("status"), post);
        }, PageRequest.of(page, size, Sort.by(Sort.Order.desc("uploadAt"))));
    }

    @Transactional
    public DevPost findRandomOneByStatus(Status status) {
        return devPostRepository.findOneByStatusOrderByRandom(status.name());
    }

    @Transactional
    public Optional<DevPost> findOneByUrl(final String url) {
        return devPostRepository.findOne((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("url"), url);
        });
    }

    @Transactional
    public Optional<DevPost> findOneByUrlAndStatus(final String url, Status status) {
        return devPostRepository.findOne((root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(criteriaBuilder.equal(root.get("url"), url), criteriaBuilder.equal(root.get("status"), status));
        });
    }

    @Transactional
    public Page<DevPost> findAll(final int page, final int size) {
        return devPostRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Order.desc("uploadAt"))));
    }

    @Transactional
    public Page<DevPost> findAllBySearch(final String title, final String category, PostType type,
                                         Status status, final int page, final int size) {
        return devPostRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> list = new LinkedList<>();

            if (! StringUtils.isBlank(title)) {
                list.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
            }

            if (! StringUtils.isBlank(category)) {
                list.add(criteriaBuilder.equal(root.get("category"), category));
            }

            if (type != null) {
                list.add(criteriaBuilder.equal(root.get("postType"), type));
            }

            if (status != null) {
                list.add(criteriaBuilder.equal(root.get("status"), status));
            }

            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        }, PageRequest.of(page, size, Sort.by(Sort.Order.desc("uploadAt"))));
    }

    @Transactional
    public void updateStatusByIdList(Status status, List<Long> idList) {
        devPostRepository.updateStatusByIdList(status, idList);
    }
}
