package com.devflix.service;

import com.devflix.entity.CrawlingSchedulerLog;
import com.devflix.repository.CrawlingSchedulerLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CrawlingScheudlerLogService {

    private final CrawlingSchedulerLogRepository crawlingSchedulerLogRepository;

    @Transactional
    public CrawlingSchedulerLog createCrawlingSchedulerLog(final CrawlingSchedulerLog crawlingSchedulerLog) {
        return crawlingSchedulerLogRepository.save(crawlingSchedulerLog);
    }
}
