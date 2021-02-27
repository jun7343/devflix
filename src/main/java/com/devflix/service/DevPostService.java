package com.devflix.service;

import com.devflix.constant.DevPostCategory;
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
    public DevPost findRecentlyDevPost(final DevPostCategory category) {
        return devPostRepository.findTopOneByCategoryOrderByIdDesc(category);
    }

    @Transactional
    public Page<DevPost> findAllByCategoryPageRequest(DevPostCategory category, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("uploadAt")));

        if (category == DevPostCategory.ALL) {
            return devPostRepository.findAll(pageRequest);
        } else {
            return devPostRepository.findAllByCategory(category, pageRequest);
        }
    }
}
