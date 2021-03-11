package kr.devflix;

import kr.devflix.clawler.YoutubeCrawler;
import kr.devflix.job.CrawlingScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SpringRunner implements ApplicationRunner {

    @Autowired
    private CrawlingScheduler scheduler;

    @Autowired
    private YoutubeCrawler youtubeCrawler;

    @Override
    public void run(ApplicationArguments args) throws Exception {
    }
}
