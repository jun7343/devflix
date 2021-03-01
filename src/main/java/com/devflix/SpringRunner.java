package com.devflix;

import com.devflix.clawler.LineDevPostCrawler;
import com.devflix.clawler.NaverDevPostCrawler;
import com.devflix.clawler.WoowaDevPostCrawler;
import com.devflix.clawler.YoutubeCrawler;
import com.devflix.service.DevPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SpringRunner implements ApplicationRunner {

    @Autowired
    private DevPostService devPostService;

    @Autowired
    private WoowaDevPostCrawler woowaDevPostCrawler;

    @Autowired
    private NaverDevPostCrawler naverDevPostCrawler;

    @Autowired
    private LineDevPostCrawler lineDevPostCrawler;

    @Autowired
    private YoutubeCrawler youtubeCrawler;

    @Override
    public void run(ApplicationArguments args) throws Exception {
    }
}
