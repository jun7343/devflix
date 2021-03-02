package com.devflix.service;

import com.devflix.constant.PostType;
import com.devflix.entity.DevPost;
import com.devflix.repository.DevPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public DevPost findRecentlyDevPost(final String category, PostType postType, final String writer) {
        return devPostRepository.findTopOneByCategoryAndPostTypeAndWriterOrderByUploadAtDesc(category, postType, writer);
    }

    @Transactional
    public Page<DevPost> findAllByCategoryOrderByUploadAt(final String category, final int page, final int size) {
        return devPostRepository.findAllByCategoryOrderByUploadAtDesc(category, PageRequest.of(page, size, Sort.by(Sort.Order.desc("uploadAt"))));
    }

    @Transactional
    public Page<DevPost> findAllByTagOrderByUploadAt(final String tag, final int page, final int size) {
        return devPostRepository.findAllByTagIn(tag, PageRequest.of(page, size));
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
    public Page<DevPost> findAllByPageRequest(int page, int size) {
        return devPostRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Order.desc("uploadAt"))));
    }
}
