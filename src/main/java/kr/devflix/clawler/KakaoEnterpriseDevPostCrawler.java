package kr.devflix.clawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.javascript.DefaultJavaScriptErrorListener;
import kr.devflix.service.CrawlingLogService;
import kr.devflix.service.DevPostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
@RequiredArgsConstructor
public class KakaoEnterpriseDevPostCrawler implements Crawler {

    private final DevPostService devPostService;
    private final CrawlingLogService crawlingLogService;
    private final String KAKAO_ENTERPRISE_BLOG_URL = "https://tech.kakaoenterprise.com/category/Tech%20Log";
    private final String DEFAULT_KAKAO_ENTERPRISE_THUMBNAIL = "https://blog.kakaocdn.net/dn/bUOyDZ/btqD2voBAVh/VNqPMkzrqEdfkVfyeaond0/img.png";
    private final SimpleDateFormat kakaoEnterpriseDateFormat = new SimpleDateFormat();
    private final Logger logger = LoggerFactory.getLogger(KakaoEnterpriseDevPostCrawler.class);
    private final int DEFAULT_CRAWLING_MAX_SIZE = 3;

    @Override
    public void crawling() {


        try (WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
            webClient.setJavaScriptErrorListener(new DefaultJavaScriptErrorListener());
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.waitForBackgroundJavaScript(3000);


        } catch (Exception e) {

        }
    }
}
