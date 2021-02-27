package com.devflix.clawler;

import com.devflix.constant.DevPostCategory;
import com.devflix.entity.DevPost;
import com.devflix.service.DevPostService;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.DefaultJavaScriptErrorListener;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

@Component
@RequiredArgsConstructor
public class NaverDevPostCrawler implements Crawler {

    private final DevPostService devPostService;
    private final SimpleDateFormat naverDateFormat = new SimpleDateFormat("yyyy.MM.dd");
    private final String NAVER_BLOG_URL = "https://d2.naver.com";
    private final String DEFAULT_NAVER_THUMBNAIL = "https://d2.naver.com/static/img/app/common/sns_share_big_img1.png";
    private final Logger logger = LoggerFactory.getLogger(NaverDevPostCrawler.class);

    @Override
    public void crawling() {
        final DevPost recentlyDevPost = devPostService.findRecentlyDevPost(DevPostCategory.NAVER);
        int totalCrawling = 0;
        boolean check = false;

        try (WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
            webClient.setJavaScriptErrorListener(new DefaultJavaScriptErrorListener());
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.waitForBackgroundJavaScript(30000);

            logger.info("Naver dev blog crawling start ....");
            for (int page = 0; page < 10; page++) {
                HtmlPage htmlPage = webClient.getPage(new URL(NAVER_BLOG_URL + "/helloworld?page=" + page));
                WebResponse response = htmlPage.getWebResponse();

                if (response.getStatusCode() == HttpStatus.SC_OK) {
                    String content = htmlPage.getBody().asXml();
                    Element body = Jsoup.parse(content).body();
                    Elements contents = body.getElementsByClass("contents");

                    if (contents.size() > 0) {
                        Elements elements = contents.get(0).children();

                        if (elements.size() > 0) {
                            for (int i = 0; i < elements.size(); i++) {
                                if (elements.get(i).hasClass("post_article")) {
                                    Map<String, String> map = new HashMap<>();

                                    try {
                                        map.put("title", elements.get(i).getElementsByTag("h2").get(0).text());
                                    } catch (Exception e) {
                                        map.put("title", "네이버 기술 블로그");
                                        logger.error("Naver title crawling error !! " + e.getMessage());
                                    }

                                    try {
                                        map.put("desc", elements.get(i).getElementsByClass("post_txt").get(0).text());
                                    } catch (Exception e) {
                                        map.put("desc", "");
                                        logger.error("Naver description crawling error !! " + e.getMessage());
                                    }

                                    try {
                                        map.put("url", NAVER_BLOG_URL + elements.get(i).getElementsByTag("h2").get(0).getElementsByTag("a").get(0).attr("href"));
                                    } catch (Exception e) {
                                        map.put("url", NAVER_BLOG_URL);
                                        map.put("writer", "네이버");

                                        logger.error("Naver writer crawling error !!" + e.getMessage());
                                        logger.error("Naver tag crawling error !!" + e.getMessage());
                                        logger.error("Naver URL crawling error !! " + e.getMessage());
                                    }

                                    try {
                                        map.put("uploadDate", elements.get(0).getElementsByTag("dl").get(0).getElementsByTag("dd").get(0).text());
                                    } catch (Exception e) {
                                        map.put("uploadDate", naverDateFormat.format(new Date()));
                                        logger.error("Naver upload date crawling error !! " + e.getMessage());
                                    }

                                    try {
                                        map.put("thumbnail", NAVER_BLOG_URL + elements.get(0).getElementsByClass("cont_img").get(0).getElementsByTag("img").get(0).attr("src"));
                                    } catch (Exception e) {
                                        map.put("thumbnail", DEFAULT_NAVER_THUMBNAIL);
                                        logger.error("Naevr thumbnail crawling error !! " + e.getMessage());
                                    }

                                    Date date = new Date();

                                    try {
                                        date = naverDateFormat.parse(map.get("uploadDate"));
                                    } catch (ParseException e) {
                                        logger.error("Naver upload date parsing error !! " + e.getMessage());
                                    }

                                    map.put("writer", "네이버");

                                    if (recentlyDevPost != null) {
                                        StringTokenizer st = new StringTokenizer(recentlyDevPost.getTitle());
                                        StringTokenizer st1 = new StringTokenizer(map.get("title"));

                                        int titleTokenSize = st.countTokens();
                                        int cnt = 0;

                                        while (st.hasMoreTokens()) {
                                            if (st1.hasMoreTokens()) {
                                                if (st.nextToken().equals(st1.nextToken())) {
                                                    cnt++;
                                                }
                                            } else {
                                                break;
                                            }
                                        }

                                        StringTokenizer st2 = new StringTokenizer(recentlyDevPost.getDescription());
                                        StringTokenizer st3 = new StringTokenizer(map.get("desc"));

                                        int descTokenSize = st2.countTokens();
                                        int cnt1 = 0;

                                        while (st2.hasMoreTokens()) {
                                            if (st3.hasMoreTokens()) {
                                                if (st2.nextToken().equals(st3.nextToken())) {
                                                    cnt1++;
                                                } else {
                                                    break;
                                                }
                                            }
                                        }

                                        if ((double) cnt / titleTokenSize >= 0.6 && (double) cnt1 / descTokenSize >= 0.6) {
                                            logger.info("Naver dev blog crawling done !! total crawling count = " + totalCrawling);
                                            check = true;
                                            break;
                                        }
                                    }

                                    DevPost post = DevPost.builder()
                                            .category(DevPostCategory.NAVER)
                                            .title(map.get("title"))
                                            .description(map.get("desc"))
                                            .url(map.get("url"))
                                            .thumbnail(map.get("thumbnail"))
                                            .writer(map.get("writer"))
                                            .uploadAt(date)
                                            .createAt(new Date())
                                            .updateAt(new Date())
                                            .build();

                                    devPostService.createDevPost(post);
                                    logger.info("Naver post crawling success !! URL = " + NAVER_BLOG_URL + "/helloworld?page=" + page + " post = " + post.toString());
                                    totalCrawling++;
                                }
                            }

                            if (check) {
                                break;
                            }
                        } else {
                            logger.error("Naver post item size zero !!");
                            break;
                        }
                    } else {
                        logger.error("Naver post list document not found !! ");
                        break;
                    }
                } else {
                    logger.error("Naver dev blog get error !! status code = " + response.getStatusCode());
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("Naver dev blog Webclient error !! " + e.getMessage());
        }

        logger.info("Naver dev blog crawling end ....");
    }
}
