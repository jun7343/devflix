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

@Service
@RequiredArgsConstructor
public class DevPostService {

    private final DevPostRepository devPostRepository;

    @Transactional
    public DevPost createDevPost(final DevPost devPost) {
        return devPostRepository.save(devPost);
    }

    @Transactional
    public DevPost findRecentlyDevPost(final String category) {
        return devPostRepository.findTopOneByCategoryOrderByIdDesc(category);
    }

    @Transactional
    public DevPost findRecentlyDevPost(final String category, PostType postType, final String writer) {
        return devPostRepository.findTopOneByCategoryAndPostTypeAndWriterOrderByUploadAtDesc(category, postType, writer);
    }

    @Transactional
    public Page<DevPost> findAllByCategoryPageRequest(final String category, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("uploadAt")));

        return devPostRepository.findAllByCategory(category, pageRequest);
    }

    @Transactional
    public Page<DevPost> findAllByPageRequest(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("uploadAt")));

        return devPostRepository.findAll(pageRequest);
    }
}
