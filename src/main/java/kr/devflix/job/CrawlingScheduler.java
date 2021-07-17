package kr.devflix.job;

import kr.devflix.clawler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CrawlingScheduler {

    private final KakaoDevPostCrawler kakaoDevPostCrawler;
    private final LineDevPostCrawler lineDevPostCrawler;
    private final NaverDevPostCrawler naverDevPostCrawler;
    private final WoowaDevPostCrawler woowaDevPostCrawler;
    private final YoutubeCrawler youtubeCrawler;
    private final Logger logger = LoggerFactory.getLogger(CrawlingScheduler.class);

    public CrawlingScheduler(KakaoDevPostCrawler kakaoDevPostCrawler, LineDevPostCrawler lineDevPostCrawler,
                             NaverDevPostCrawler naverDevPostCrawler, WoowaDevPostCrawler woowaDevPostCrawler,
                             YoutubeCrawler youtubeCrawler) {
        this.kakaoDevPostCrawler = kakaoDevPostCrawler;
        this.lineDevPostCrawler = lineDevPostCrawler;
        this.naverDevPostCrawler = naverDevPostCrawler;
        this.woowaDevPostCrawler = woowaDevPostCrawler;
        this.youtubeCrawler = youtubeCrawler;
    }

    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
    public void crawlingAll() {
        try {
            youtubeCrawler.crawling();
        } catch (Exception e) {
            logger.error("Youtube video crawling error !! " + e.getMessage());
        }

        try {
            kakaoDevPostCrawler.crawling();
        } catch (Exception e) {
            logger.error("Kakao dev blog crawling error !! " + e.getMessage());
        }
        try {
            lineDevPostCrawler.crawling();
        } catch (Exception e) {
            logger.error("Line dev blog crawling error !! " + e.getMessage());
        }

        try {
            naverDevPostCrawler.crawling();
        } catch (Exception e) {
            logger.error("Naver dev blog crawling error !! " + e.getMessage());
        }

        try {
            woowaDevPostCrawler.crawling();
        } catch (Exception e) {
            logger.error("Woowa dev blog crawling error !! " + e.getMessage());
        }
    }
}
