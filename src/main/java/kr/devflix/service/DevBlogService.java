package kr.devflix.service;

import kr.devflix.constant.Status;
import kr.devflix.entity.DevBlog;
import kr.devflix.repository.DevBlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DevBlogService {

    private final DevBlogRepository devBlogRepository;

    @Transactional
    public List<DevBlog> findAllDevBlogByStatus(Status status) {
        return devBlogRepository.findAllByStatusOrderByCreateAtDesc(status);
    }

    @Transactional
    public DevBlog createDevBlog(final DevBlog devBlog) {
        return devBlogRepository.save(devBlog);
    }
}
