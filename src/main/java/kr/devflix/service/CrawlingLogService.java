package kr.devflix.service;

import kr.devflix.entity.CrawlingLog;
import kr.devflix.repository.CrawlingLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CrawlingLogService {

    private final CrawlingLogRepository crawlingLogRepository;

    @Transactional
    public CrawlingLog createCrawlingSchedulerLog(final CrawlingLog crawlingSchedulerLog) {
        return crawlingLogRepository.save(crawlingSchedulerLog);
    }
}
