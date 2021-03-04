package com.devflix.job;

import com.devflix.clawler.KakaoDevPostCrawler;
import com.devflix.clawler.LineDevPostCrawler;
import com.devflix.clawler.NaverDevPostCrawler;
import com.devflix.clawler.WoowaDevPostCrawler;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrawlingScheduler {

    private final KakaoDevPostCrawler kakaoDevPostCrawler;
    private final LineDevPostCrawler lineDevPostCrawler;
    private final NaverDevPostCrawler naverDevPostCrawler;
    private final WoowaDevPostCrawler woowaDevPostCrawler;

    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
    public void crawlingAll() {
        kakaoDevPostCrawler.crawling();
        lineDevPostCrawler.crawling();
        naverDevPostCrawler.crawling();
        woowaDevPostCrawler.crawling();
    }
}
