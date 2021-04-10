package kr.devflix.clawler;

import kr.devflix.constant.Status;
import kr.devflix.constant.PostType;
import kr.devflix.entity.CrawlingLog;
import kr.devflix.entity.DevPost;
import kr.devflix.service.CrawlingLogService;
import kr.devflix.service.DevPostService;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.DefaultJavaScriptErrorListener;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@RequiredArgsConstructor
public class LineDevPostCrawler implements Crawler {

    private final DevPostService devPostService;
    private final CrawlingLogService crawlingLogService;
    private static final SimpleDateFormat lineDateFormat = new SimpleDateFormat("yyyy.MM.dd");
    private static final String LINE_BLOG_URL = "https://engineering.linecorp.com/ko/blog/page/";
    private static final String DEFAULT_LINE_THUMBNAIL = "https://engineering.linecorp.com/wp-content/uploads/2018/11/linedev_logo.jpg";
    private static final Logger logger = LoggerFactory.getLogger(LineDevPostCrawler.class);
    private static final Integer DEFAULT_CRAWLING_MAX_PAGE = 3;

    @Override
    public void crawling() {
        final DevPost recentlyDevPost = devPostService.findRecentlyDevPost("LINE", PostType.BLOG);
        Integer totalCrawling = 0;
        Boolean success = false;
        StringBuilder message = new StringBuilder();

        logger.info("Line dev blog crawling start ....");
        Long startAt = System.currentTimeMillis();
        try (WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
            webClient.setJavaScriptErrorListener(new DefaultJavaScriptErrorListener());
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.waitForBackgroundJavaScript(3000);

            for (int page = 1; page <= DEFAULT_CRAWLING_MAX_PAGE; page++) {
                HtmlPage htmlPage = webClient.getPage(new URL(LINE_BLOG_URL + page));
                WebResponse webResponse = htmlPage.getWebResponse();

                if (webResponse.getStatusCode() == HttpStatus.SC_OK) {
                    String content = htmlPage.getBody().asXml();
                    Document parse = Jsoup.parse(content);

                    Elements articleList = parse.getElementById("main").getElementsByTag("article");

                    if (articleList.size() > 0) {
                        for (int i = 0; i < articleList.size(); i++) {
                            Map<String, String> map = new HashMap<>();
                            List<String> tagList = new LinkedList<>();

                            try {
                                map.put("title", articleList.get(i).getElementsByTag("header").get(0).getElementsByClass("entry-title").get(0).text());
                            } catch (Exception e) {
                                map.put("title", "라인 기술 블로그");
                                logger.error("Line title crawling error !! " + e.getMessage());
                            }

                            try {
                                map.put("writer", articleList.get(i).getElementsByTag("header").get(0).getElementsByClass("entry-meta").get(0).getElementsByClass("author-name").get(0).text());
                            } catch (Exception e) {
                                map.put("writer", "라인");
                                logger.error("Line writer crawling error !! " + e.getMessage());
                            }

                            try {
                                map.put("uploadDate", articleList.get(i).getElementsByTag("header").get(0).getElementsByClass("posted-on").get(0).getElementsByClass("published").get(0).text());
                            } catch (Exception e) {
                                map.put("uploadDate", lineDateFormat.format(new Date()));
                                logger.error("Line upload date crawling error !! " + e.getMessage());
                            }

                            try {
                                map.put("url", articleList.get(i).getElementsByTag("header").get(0).getElementsByClass("entry-title").get(0).getElementsByTag("a").get(0).attr("href"));
                            } catch (Exception e) {
                                map.put("url", LINE_BLOG_URL);
                                logger.error("Line URL crawling error !! " + e.getMessage());
                            }

                            try {
                                Elements pTag = articleList.get(i).getElementsByClass("entry-content").get(0).children();
                                StringBuilder builder = new StringBuilder();

                                for (org.jsoup.nodes.Element element : pTag) {
                                    if (!element.hasClass("post-tags-list")) {
                                        builder.append(element.text());
                                    }
                                }

                                map.put("desc", builder.toString());
                            } catch (Exception e) {
                                map.put("desc", "");
                                logger.error("Line description crawling error !! " + e.getMessage());
                            }

                            try {
                                Elements tagElements = articleList.get(i).getElementsByClass("entry-content").get(0).getElementsByClass("post-tags-list").get(0).children();

                                for (org.jsoup.nodes.Element tagElement : tagElements) {
                                    tagList.add(tagElement.text().substring(1));
                                }
                            } catch (Exception e) {
                                logger.error("Line tag crawling error !! " + e.getMessage());
                            }

                            Date date = new Date();

                            try {
                                date = lineDateFormat.parse(map.get("uploadDate"));
                            } catch (ParseException e) {
                                logger.error("Line date parsing error !! " + e.getMessage());
                            }

                            // 크롤링된 포스트와 DB 저장된 최근 포스트 일치 여부
                            if (recentlyDevPost != null) {
                                StringTokenizer st = new StringTokenizer(recentlyDevPost.getTitle());
                                StringTokenizer st1 = new StringTokenizer(map.get("title"));

                                int titleTokensize = st.countTokens();
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
                                        }
                                    } else {
                                        break;
                                    }
                                }

                                // title, description text가 단어 별로 6할 이상 맞으면 최근 게시물로 인정
                                if ((double) cnt / titleTokensize >= 0.6 && (double) cnt1 / descTokenSize >= 0.6) {
                                    logger.info("Line dev blog crawling done !! total crawling count = " + totalCrawling);
                                    success = true;
                                    message.append("Line dev blog crawling done !!");
                                    break;
                                }
                            }

                            DevPost post = DevPost.builder()
                                    .category("LINE")
                                    .postType(PostType.BLOG)
                                    .status(Status.POST)
                                    .title(map.get("title"))
                                    .url(map.get("url"))
                                    .description(map.get("desc"))
                                    .writer(map.get("writer"))
                                    .thumbnail(DEFAULT_LINE_THUMBNAIL)
                                    .tag(tagList)
                                    .view(0)
                                    .uploadAt(date)
                                    .createAt(new Date())
                                    .updateAt(new Date())
                                    .build();

                            devPostService.createDevPost(post);

                            map.clear();
                            tagList.clear();

                            logger.info("Line post crawling success !! URL = " + (LINE_BLOG_URL + page) + " post = " + post.toString());
                            totalCrawling++;
                        }

                        if (success) {
                            break;
                        }
                    } else {
                        logger.error("Naver post size zero !!");
                        message.append("Naver post size zero !!");
                        success = false;
                        break;
                    }

                    webResponse.cleanUp();
                    webClient.close();
                } else {
                    logger.error("Naver dev blog page get error !! status code = " + webResponse.getStatusCode());
                    message.append("Naver dev blog page get error !! status code = ").append(webResponse.getStatusCode());
                    success = false;
                    break;
                }

                if (page == DEFAULT_CRAWLING_MAX_PAGE) {
                    success = true;
                    message.append("Line dev blog crawling done !!");
                }
            }
        } catch (Exception e) {
            logger.error("Naver dev blog Webclient error !! " + e.getMessage());
            message.append("Naver dev blog Webclient error !! ").append(e.getMessage());
            success = false;
        }

        logger.info("Line dev blog crawling end ....");
        Long endAt = System.currentTimeMillis();

        CrawlingLog log = CrawlingLog.builder()
                .jobName("Line dev blog crawling")
                .jobStartAt(startAt)
                .jobEndAt(endAt)
                .success(success)
                .totalCrawling(totalCrawling)
                .message(message.toString())
                .createAt(new Date())
                .updateAt(new Date())
                .build();

        crawlingLogService.createCrawlingSchedulerLog(log);
    }
}
