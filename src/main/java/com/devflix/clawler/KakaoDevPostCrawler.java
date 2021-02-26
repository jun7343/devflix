package com.devflix.clawler;

import com.devflix.constant.DevPostCategory;
import com.devflix.entity.DevPost;
import com.devflix.service.DevPostService;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
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
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@RequiredArgsConstructor
public class KakaoDevPostCrawler implements Crawler {

    private final DevPostService devPostService;
    private final String KAKAO_BLOG_URL = "https://tech.kakao.com/blog/page/";
    private final String DEFAULT_KAKAO_THUMBNAIL = "https://tech.kakao.com/wp-content/uploads/2020/07/2020tech_main-2.jpg";
    private final SimpleDateFormat kakaoDateFormat = new SimpleDateFormat("yyyy.MM.dd");
    private final Logger logger = LoggerFactory.getLogger(KakaoDevPostCrawler.class);

    @Override
    public void crawling() {
        final DevPost recentlyDevPost = devPostService.findRecentlyDevPost(DevPostCategory.KAKAO);
        boolean check = false;
        int totalCrawling = 0;

        if (recentlyDevPost != null) {
            logger.info("recently Kakao dev post = " + recentlyDevPost.toString());
        }

        logger.info("Kakao dev blog crawling start ....");
        try (WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
            webClient.setJavaScriptErrorListener(new DefaultJavaScriptErrorListener());
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.waitForBackgroundJavaScript(3000);

            // 크롤링 최대 맥시멈 10 page
            for (int page = 1; page <= 10; page++) {
                WebResponse response = webClient.getPage(new URL(KAKAO_BLOG_URL + page)).getWebResponse();

                if (response.getStatusCode() == HttpStatus.SC_OK) {
                    final String content = response.getContentAsString(StandardCharsets.UTF_8);

                    Element parse = Jsoup.parse(content).body();
                    Elements elements;

                    try {
                        elements = parse.getElementsByClass("list_post").get(0).children();
                    } catch (Exception e) {
                        logger.error("Kakao blog list crawling error !! " + e.getMessage());
                        break;
                    }

                    if (elements.size() == 0) {
                        logger.warn("Kakao Blog dev post size zero!!");
                        break;
                    }

                    for (int i = 0; i < elements.size(); i++) {
                        Map<String, String> map = new HashMap<>();

                        try {
                            map.put("url", elements.get(i).getElementsByClass("link_post").get(0).attr("href"));
                        } catch (Exception e) {
                            map.put("url", KAKAO_BLOG_URL);
                            logger.error("Kakao blog URL crawling error !! " + e.getMessage());
                        }

                        try {
                            map.put("title", elements.get(i).getElementsByClass("link_post").get(0).getElementsByClass("tit_post").get(0).text());
                        } catch (Exception e) {
                            map.put("title", "카카오 기술 블로그");
                            logger.error("kakao blog title crawling error !! " + e.getMessage());
                        }

                        try {
                            map.put("desc", elements.get(i).getElementsByClass("link_post").get(0).getElementsByClass("desc_post").get(0).text());
                        } catch (Exception e) {
                            map.put("desc", "");
                            logger.error("Kakao blog desc crawling error !! " + e.getMessage());
                        }

                        try {
                            map.put("uploadDate", elements.get(i).getElementsByClass("link_post").get(0).getElementsByClass("txt_date").get(0).text());
                        } catch (Exception e) {
                            map.put("uploadDate", "");
                            logger.error("Kakao blog upload crawling error !! " + e.getMessage());
                        }

                        Date uploadAt = new Date();

                        try {
                            uploadAt = kakaoDateFormat.parse(map.get("uploadDate"));
                        } catch (ParseException e) {
                            logger.error("Upload date parsing error !! " + e.getMessage());
                        }

                        try {
                            map.put("thumbnail", elements.get(i).getElementsByClass("link_post").get(0).getElementsByClass("thumb_img").get(0).child(0).attr("src"));
                        } catch (Exception e) {
                            map.put("thumbnail", DEFAULT_KAKAO_THUMBNAIL);
                            logger.error("Kakao blog thumbnail crawling error !! " + e.getMessage());
                        }

                        try {
                            map.put("writer", elements.get(i).getElementsByClass("info_writer").get(0).getElementsByClass("area_txt").get(0).getElementsByClass("txt_name").text());
                        } catch (Exception e) {
                            map.put("writer", "kakao tech");
                            logger.error("Kakao blog writer crawling error !! " + e.getMessage());
                        }

                        List<String> tagList = new LinkedList<>();

                        try {
                            Elements area_tag = elements.get(i).getElementsByClass("area_tag").get(0).children();

                            for (int j = 0; j < area_tag.size(); j++) {
                                tagList.add(area_tag.get(j).text());
                            }
                        } catch (Exception e) {
                            logger.error("Kakao blog tags crawling error !! " + e.getMessage());
                        }

                        // 크롤링된 포스트와 DB 저장된 최근 포스트 일치 여부
                        if (recentlyDevPost != null) {
                            StringTokenizer st = new StringTokenizer(recentlyDevPost.getTitle());
                            StringTokenizer st2 = new StringTokenizer(map.get("title"));
                            int titleTokenSize = st.countTokens();
                            int cnt = 0;

                            while (st.hasMoreTokens()) {
                                if (st2.hasMoreTokens()) {
                                    if (st.nextToken().equals(st2.nextToken())) {
                                        cnt++;
                                    }
                                } else {
                                    break;
                                }
                            }

                            // title text가 단어 별로 6할 이상 맞으면 최근 게시물로 인정
                            if ((double) cnt / titleTokenSize >= 0.6) {
                                logger.info("Kakao dev blog crawling done !! total crawling count = " + totalCrawling);
                                check = true;
                                break;
                            }
                        }

                        DevPost post = DevPost.builder()
                                .title(map.get("title"))
                                .url(map.get("url"))
                                .category(DevPostCategory.KAKAO)
                                .description(map.get("desc"))
                                .thumbnail(map.get("thumbnail"))
                                .tag(tagList)
                                .writer(map.get("writer"))
                                .uploadAt(uploadAt)
                                .createAt(new Date())
                                .updateAt(new Date())
                                .build();

                        devPostService.createDevPost(post);
                        logger.info("Kakao post crawling success !! URL = " + (KAKAO_BLOG_URL + page) + " post = " + post.toString());
                        totalCrawling++;
                    }

                    // 크롤링 최근 게시물까지 도달 했으면 break;
                    if (check) {
                        break;
                    }
                } else {
                    logger.error("Kakao dev blog get error !! status code = " + response.getStatusCode());
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("webclient error = " + e.getMessage());
        }

        logger.info("Kakao dev blog crawling end ....");
    }
}
