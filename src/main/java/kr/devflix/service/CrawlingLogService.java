package kr.devflix.service;

import kr.devflix.entity.CrawlingLog;
import kr.devflix.repository.CrawlingLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Order;


@Service
@RequiredArgsConstructor
public class CrawlingLogService {

    private final CrawlingLogRepository crawlingLogRepository;

    @Transactional
    public void createCrawlingSchedulerLog(final CrawlingLog crawlingSchedulerLog) {
        crawlingLogRepository.save(crawlingSchedulerLog);
    }

    @Transactional
    public Page<CrawlingLog> findAll(final int page, final int size) {
        return crawlingLogRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Order.desc("createAt"))));
    }

    @Transactional
    public Page<CrawlingLog> findAllBySearch(final String jobName, final int page, int size) {
        return crawlingLogRepository.findAll((root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(root.get("jobName"), "%" + jobName + "%");
        }, PageRequest.of(page, size, Sort.by(Sort.Order.desc("createAt"))));
    }
}
