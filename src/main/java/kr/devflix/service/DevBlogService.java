package kr.devflix.service;

import kr.devflix.constant.Status;
import kr.devflix.entity.DevBlog;
import kr.devflix.repository.DevBlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DevBlogService {

    private final DevBlogRepository devBlogRepository;

    @Transactional
    public List<DevBlog> findAllDevBlogByStatus(Status status) {
        return devBlogRepository.findAllByStatusOrderByCreateAtDesc(status);
    }

    @Transactional
    public List<DevBlog> findAll() {
        return devBlogRepository.findAll(Sort.by(Sort.Order.desc("createAt")));
    }

    @Transactional
    public Optional<DevBlog> findOneById(long id) {
        return devBlogRepository.findById(id);
    }

    @Transactional
    public void updateDevBlog(final DevBlog blog) {
        devBlogRepository.save(blog);
    }
}
