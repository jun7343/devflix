package kr.devflix.clawler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import kr.devflix.constant.PostType;
import kr.devflix.constant.Status;
import kr.devflix.entity.CrawlingLog;
import kr.devflix.entity.DevPost;
import kr.devflix.entity.YoutubeChannel;
import kr.devflix.service.CrawlingLogService;
import kr.devflix.service.DevPostService;
import kr.devflix.service.YoutubeChannelService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/*
    reference - https://developers.google.com/youtube/v3/docs/search
                https://developers.google.com/youtube/v3/docs/channels
 */
@Component
public class YoutubeCrawler implements Crawler {

    private static final String API_YOUTUBE_SEARCH_URL = "https://www.googleapis.com/youtube/v3/search";
    private static final String API_YOUTUBE_CHANNEL_URL = "https://www.googleapis.com/youtube/v3/channels";
    private static final String YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v=";
    // youtube channel id for search, channel parameter
    private static final String ID = "id";
    // youtube channel name for search, channer parameter
    private static final String FOR_URSE_NAME = "forUsername";
    // next page or previous page token for search, channel parameter
    private static final String PAGE_TOKEN = "pageToken";
    // result size(default 5) for search, channel parameter
    private static final String MAX_RESULTS = "maxResults";
    private static final Integer DEFAULT_MAX_RESULT_SIZE = 20;
    // search type(channel, playlist, video) for search parmeter
    private static final String TYPE = "type";
    // search result ordering(date, rating, relevance, title, videoCount, viewCount) for search parmeter
    private static final String ORDER = "order";
    // channel id for search parameter
    private static final String CHANNEL_ID = "channelId";
    private static final String KEY = "key";
    private static final String PART = "part";
    // youtube date format ISO8601
    private static final StdDateFormat youtubeDateFormat = new StdDateFormat();
    private static final Logger logger = LoggerFactory.getLogger(YoutubeCrawler.class);
    private final String YOUTUBE_API_KEY;
    private final YoutubeChannelService youtubeChannelService;
    private final DevPostService devPostService;
    private final CrawlingLogService crawlingLogService;

    public YoutubeCrawler(final YoutubeChannelService youtubeChannelService, final DevPostService devPostService,
                          final CrawlingLogService crawlingLogService, Environment environment) {
        this.YOUTUBE_API_KEY = environment.getProperty("youtube.data.api-key");
        this.youtubeChannelService = youtubeChannelService;
        this.devPostService = devPostService;
        this.crawlingLogService = crawlingLogService;
    }

    @Override
    public void crawling() {
        List<YoutubeChannel> findAll = youtubeChannelService.findAllOrderByCrawlingAtAsc();

        for (int channelNum = 0; channelNum < findAll.size(); channelNum++) {
            final YoutubeChannel channel = findAll.get(channelNum);
            Integer totalCrawling = 0;
            StringBuilder message = new StringBuilder();
            Boolean success = false;

            UriComponents build = UriComponentsBuilder.fromHttpUrl(API_YOUTUBE_SEARCH_URL)
                    .queryParam(KEY, YOUTUBE_API_KEY)
                    .queryParam(PART, "id", "snippet")
                    .queryParam(MAX_RESULTS, DEFAULT_MAX_RESULT_SIZE)
                    .queryParam(ORDER, "date")
                    .queryParam(TYPE, "video")
                    .queryParam(CHANNEL_ID, channel.getChannelId())
                    .build();

            logger.info("Youtube " + channel.getChannelTitle() + " video crawling start ...");
            Long startAt = System.currentTimeMillis();
            try {
                URL url = build.toUri().toURL();

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                ObjectMapper mapper = new ObjectMapper();

                JsonNode result = mapper.readTree(connection.getInputStream());
                JsonNode items = result.get("items");

                for (int i = 0; i < items.size(); i++) {
                    JsonNode id = items.get(i).get("id");
                    JsonNode videoId = id.get("videoId");
                    JsonNode snippet = items.get(i).get("snippet");
                    JsonNode channelTitle = snippet.get("channelTitle");
                    JsonNode title = snippet.get("title");
                    JsonNode description = snippet.get("description");
                    JsonNode publishedAt = snippet.get("publishedAt");
                    JsonNode thumbnails = snippet.get("thumbnails");
                    JsonNode thumbnailshigh = thumbnails.get("high");
                    JsonNode thumbnailshighUrl = thumbnailshigh.get("url");

                    Date publishDate = new Date();

                    try {
                        publishDate = youtubeDateFormat.parse(publishedAt.asText());
                    } catch (ParseException e) {
                        logger.error("Youtube video publish date parsing error !! " + e.getMessage());
                    }

                    Optional<DevPost> findItem = devPostService.findOneByUrl(YOUTUBE_VIDEO_URL + videoId.asText());

                    if (findItem.isPresent()) {
                        success = true;
                        break;
                    }

                    DevPost post = DevPost.builder()
                            .category(channel.getCategory())
                            .postType(PostType.YOUTUBE)
                            .title(title.asText())
                            .description(description.asText().replaceAll("\\R", ""))
                            .writer(channelTitle.asText())
                            .url(YOUTUBE_VIDEO_URL + videoId.asText())
                            .tag(null)
                            .thumbnail(thumbnailshighUrl.asText())
                            .status(Status.POST)
                            .view(0)
                            .uploadAt(publishDate)
                            .tag(new ArrayList<>())
                            .createAt(new Date())
                            .updateAt(new Date())
                            .build();

                    DevPost createPost = devPostService.createDevPost(post);
                    logger.info("Youtube " + channel.getChannelTitle() +" video crawling success !! post = " + createPost.toString());
                    totalCrawling++;
                }

                if (result.has("nextPageToken") && ! success) {
                    result = null;

                    JsonNode pageInfo = result.get("pageInfo");
                    JsonNode totalResults = pageInfo.get("totalResults");
                    JsonNode resultsPerPage = pageInfo.get("resultsPerPage");
                    JsonNode nextPageToken = result.get("nextPageToken");

                    int start = 1;
                    int end = totalResults.asInt() / resultsPerPage.asInt();

                    if (totalResults.asInt() % resultsPerPage.asInt() != 0) {
                        end++;
                    }

                    for (; start < end; start++) {
                        UriComponents build1 = UriComponentsBuilder.fromHttpUrl(API_YOUTUBE_SEARCH_URL)
                                .queryParam(KEY, YOUTUBE_API_KEY)
                                .queryParam(PART, "id", "snippet")
                                .queryParam(MAX_RESULTS, DEFAULT_MAX_RESULT_SIZE)
                                .queryParam(ORDER, "date")
                                .queryParam(TYPE, "video")
                                .queryParam(CHANNEL_ID, channel.getChannelId())
                                .queryParam(PAGE_TOKEN, nextPageToken.asText())
                                .build();


                        URL url1 = build1.toUri().toURL();
                        HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                        ObjectMapper mapper1 = new ObjectMapper();

                        JsonNode result1 = mapper1.readTree(connection1.getInputStream());
                        JsonNode items1 = result1.get("items");

                        for (int i = 0; i < items1.size(); i++) {
                            JsonNode id = items1.get(i).get("id");
                            JsonNode videoId = id.get("videoId");
                            JsonNode snippet = items1.get(i).get("snippet");
                            JsonNode channelTitle = snippet.get("channelTitle");
                            JsonNode title = snippet.get("title");
                            JsonNode description = snippet.get("description");
                            JsonNode publishedAt = snippet.get("publishedAt");
                            JsonNode thumbnails = snippet.get("thumbnails");
                            JsonNode thumbnailshigh = thumbnails.get("high");
                            JsonNode thumbnailshighUrl = thumbnailshigh.get("url");

                            Date publishDate = new Date();

                            try {
                                publishDate = youtubeDateFormat.parse(publishedAt.asText());
                            } catch (ParseException e) {
                                logger.error("Youtube " + channel.getChannelTitle() +" video publish date parsing error !! " + e.getMessage());
                            }

                            Optional<DevPost> findItem = devPostService.findOneByUrl(YOUTUBE_VIDEO_URL + videoId.asText());

                            if (findItem.isPresent()) {
                                success = true;
                                break;
                            }

                            DevPost post = DevPost.builder()
                                    .category(channel.getCategory())
                                    .postType(PostType.YOUTUBE)
                                    .title(title.asText())
                                    .description(description.asText().replaceAll("\\R", ""))
                                    .writer(channelTitle.asText())
                                    .url(YOUTUBE_VIDEO_URL + videoId.asText())
                                    .tag(null)
                                    .thumbnail(thumbnailshighUrl.asText())
                                    .status(Status.POST)
                                    .view(0)
                                    .uploadAt(publishDate)
                                    .tag(new ArrayList<>())
                                    .createAt(new Date())
                                    .updateAt(new Date())
                                    .build();

                            DevPost createPost = devPostService.createDevPost(post);
                            logger.info("Youtube " + channel.getChannelTitle() + " video crawling success !! post = " + createPost.toString());
                            totalCrawling++;
                        }

                        if (result1.has("nextPageToken") && ! success && result1.get("pageInfo").get("resultsPerPage").asInt() == DEFAULT_MAX_RESULT_SIZE) {
                            nextPageToken = result1.get("nextPageToken");
                        } else {
                            logger.info("Youtube " + channel.getChannelTitle()  +" video crawling done !! total video crawling count = " + totalCrawling);
                            message.append("Youtube video crawling done!!");
                            success = true;
                            result1 = null;
                            break;
                        }
                    }
                } else {
                    logger.info("Youtube " + channel.getChannelTitle() + " video crawling done !! total video crawling count = " + totalCrawling);
                    message.append("Youtube video crawling done!!");
                    success = true;
                    result = null;
                }
            } catch (MalformedURLException e) {
                logger.error("Youtube " + channel.getChannelTitle() +" URL connetion error !! " + e.getMessage());
                message.append("Youtube URL connection error !!");
                success = false;
            } catch (IOException e) {
                logger.error("Youtube " + channel.getChannelTitle() +" video IO Exception error !! " + e.getMessage());
                message.append("Youtube video IO Exception erorr !!");
                success = false;
            }

            logger.info("Youtube " + channel.getChannelTitle() + " video crawling end ...");
            Long endAt = System.currentTimeMillis();

            CrawlingLog log = CrawlingLog.builder()
                    .jobName("Youtube " + channel.getChannelTitle() + " video crawling")
                    .jobStartAt(startAt)
                    .jobEndAt(endAt)
                    .message(message.toString())
                    .success(success)
                    .totalCrawling(totalCrawling)
                    .createAt(new Date())
                    .updateAt(new Date())
                    .build();

            crawlingLogService.createCrawlingSchedulerLog(log);

            YoutubeChannel updateChannel = YoutubeChannel.builder()
                    .id(channel.getId())
                    .category(channel.getCategory())
                    .channelId(channel.getChannelId())
                    .channelTitle(channel.getChannelTitle())
                    .etag(channel.getEtag())
                    .status(Status.POST)
                    .description(channel.getDescription())
                    .thumbnail(channel.getThumbnail())
                    .publishAt(channel.getPublishAt())
                    .crawlingAt(System.currentTimeMillis())
                    .createAt(channel.getCreateAt())
                    .updateAt(new Date())
                    .build();

            youtubeChannelService.updateYoutubeChannel(updateChannel);
        }
    }

    public void targetCrawling(final YoutubeChannel channel) {
        targetCrawling(channel, DEFAULT_MAX_RESULT_SIZE);
    }

    public void targetCrawling(final YoutubeChannel channel, final int resultSize) {
        if (channel == null) {
            logger.error("Youtube channel object is null!!");
            return;
        }

        Integer totalCrawling = 0;
        Boolean success = false;
        StringBuilder message = new StringBuilder();

        UriComponents build = UriComponentsBuilder.fromHttpUrl(API_YOUTUBE_SEARCH_URL)
                .queryParam(KEY, YOUTUBE_API_KEY)
                .queryParam(PART, "id", "snippet")
                .queryParam(MAX_RESULTS, resultSize)
                .queryParam(ORDER, "date")
                .queryParam(TYPE, "video")
                .queryParam(CHANNEL_ID, channel.getChannelId())
                .build();

        logger.info("Youtube " + channel.getChannelTitle() + " video crawling start ...");
        Long startAt = System.currentTimeMillis();
        try {
            URL url = build.toUri().toURL();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            ObjectMapper mapper = new ObjectMapper();

            JsonNode result = mapper.readTree(connection.getInputStream());
            JsonNode items = result.get("items");

            for (int i = 0; i < items.size(); i++) {
                JsonNode id = items.get(i).get("id");
                JsonNode videoId = id.get("videoId");
                JsonNode snippet = items.get(i).get("snippet");
                JsonNode channelTitle = snippet.get("channelTitle");
                JsonNode title = snippet.get("title");
                JsonNode description = snippet.get("description");
                JsonNode publishedAt = snippet.get("publishedAt");
                JsonNode thumbnails = snippet.get("thumbnails");
                JsonNode thumbnailshigh = thumbnails.get("high");
                JsonNode thumbnailshighUrl = thumbnailshigh.get("url");

                Date publishDate = new Date();

                try {
                    publishDate = youtubeDateFormat.parse(publishedAt.asText());
                } catch (ParseException e) {
                    logger.error("Youtube " + channel.getChannelTitle() + " video publish date parsing error !! " + e.getMessage());
                }

                Optional<DevPost> findItem = devPostService.findOneByUrl(YOUTUBE_VIDEO_URL + videoId.asText());

                if (findItem.isPresent()) {
                    success = true;
                    break;
                }

                DevPost post = DevPost.builder()
                        .category(channel.getCategory())
                        .postType(PostType.YOUTUBE)
                        .title(title.asText())
                        .description(description.asText().replaceAll("\\R", ""))
                        .writer(channelTitle.asText())
                        .url(YOUTUBE_VIDEO_URL + videoId.asText())
                        .tag(null)
                        .thumbnail(thumbnailshighUrl.asText())
                        .status(Status.POST)
                        .view(0)
                        .uploadAt(publishDate)
                        .tag(new ArrayList<>())
                        .createAt(new Date())
                        .updateAt(new Date())
                        .build();

                DevPost createPost = devPostService.createDevPost(post);
                logger.info("Youtube " + channel.getChannelTitle() + " video crawling success !! post = " + createPost.toString());
                totalCrawling++;
            }

            if (result.has("nextPageToken") && ! success) {
                result = null;

                JsonNode pageInfo = result.get("pageInfo");
                JsonNode totalResults = pageInfo.get("totalResults");
                JsonNode resultsPerPage = pageInfo.get("resultsPerPage");
                JsonNode nextPageToken = result.get("nextPageToken");

                int start = 1;
                int end = totalResults.asInt() / resultsPerPage.asInt();

                if (totalResults.asInt() % resultsPerPage.asInt() != 0) {
                    end++;
                }

                for (; start < end; start++) {
                    UriComponents build1 = UriComponentsBuilder.fromHttpUrl(API_YOUTUBE_SEARCH_URL)
                            .queryParam(KEY, YOUTUBE_API_KEY)
                            .queryParam(PART, "id", "snippet")
                            .queryParam(MAX_RESULTS, resultSize)
                            .queryParam(ORDER, "date")
                            .queryParam(TYPE, "video")
                            .queryParam(CHANNEL_ID, channel.getChannelId())
                            .queryParam(PAGE_TOKEN, nextPageToken.asText())
                            .build();


                    URL url1 = build1.toUri().toURL();
                    HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                    ObjectMapper mapper1 = new ObjectMapper();

                    JsonNode result1 = mapper1.readTree(connection1.getInputStream());
                    JsonNode items1 = result1.get("items");

                    for (int i = 0; i < items1.size(); i++) {
                        JsonNode id = items1.get(i).get("id");
                        JsonNode videoId = id.get("videoId");
                        JsonNode snippet = items1.get(i).get("snippet");
                        JsonNode channelTitle = snippet.get("channelTitle");
                        JsonNode title = snippet.get("title");
                        JsonNode description = snippet.get("description");
                        JsonNode publishedAt = snippet.get("publishedAt");
                        JsonNode thumbnails = snippet.get("thumbnails");
                        JsonNode thumbnailshigh = thumbnails.get("high");
                        JsonNode thumbnailshighUrl = thumbnailshigh.get("url");

                        Date publishDate = new Date();

                        try {
                            publishDate = youtubeDateFormat.parse(publishedAt.asText());
                        } catch (ParseException e) {
                            logger.error("Youtube " + channel.getChannelTitle() + "  video publish date parsing error !! " + e.getMessage());
                        }

                        Optional<DevPost> findItem = devPostService.findOneByUrl(YOUTUBE_VIDEO_URL + videoId.asText());

                        if (findItem.isPresent()) {
                            success = true;
                            break;
                        }

                        DevPost post = DevPost.builder()
                                .category(channel.getCategory())
                                .postType(PostType.YOUTUBE)
                                .title(title.asText())
                                .description(description.asText().replaceAll("\\R", ""))
                                .writer(channelTitle.asText())
                                .url(YOUTUBE_VIDEO_URL + videoId.asText())
                                .tag(null)
                                .thumbnail(thumbnailshighUrl.asText())
                                .status(Status.POST)
                                .view(0)
                                .uploadAt(publishDate)
                                .tag(new ArrayList<>())
                                .createAt(new Date())
                                .updateAt(new Date())
                                .build();

                        DevPost createPost = devPostService.createDevPost(post);
                        logger.info("Youtube " + channel.getChannelTitle() + " video crawling success !! post = " + createPost.toString());
                        totalCrawling++;
                    }

                    if (result1.has("nextPageToken") && ! success && result1.get("pageInfo").get("resultsPerPage").asInt() == DEFAULT_MAX_RESULT_SIZE) {
                        nextPageToken = result1.get("nextPageToken");
                    } else {
                        logger.info("Youtube " + channel.getChannelTitle() + " video crawling done !! total video crawling count = " + totalCrawling);
                        message.append("Youtube ").append(channel.getChannelTitle()).append(" video crawling done !!");
                        success = true;
                        result1 = null;
                        break;
                    }
                }
            } else {
                logger.info("Youtube " + channel.getChannelTitle() + " video crawling done !! total video crawling count = " + totalCrawling);
                message.append("Youtube ").append(channel.getChannelTitle()).append(" video crawling done !!");
                success = true;
                result = null;
            }
        } catch (MalformedURLException e) {
            logger.error("Youtube " + channel.getChannelTitle() + " video URL connetion error !! " + e.getMessage());
            message.append("Youtube ").append(channel.getChannelTitle()).append(" video URL connection error !!");
            success = false;
        } catch (IOException e) {
            logger.error("Youtube " + channel.getChannelTitle() + " video IO Exception error !! " + e.getMessage());
            message.append("Youtube ").append(channel.getChannelTitle()).append(" video IO Exception error !!");
            success = false;
        }

        logger.info("Youtube " + channel.getChannelTitle() + " video crawling end ...");
        Long endAt = System.currentTimeMillis();

        CrawlingLog log = CrawlingLog.builder()
                .jobName("Youtube " + channel.getChannelTitle() + " video crawling")
                .jobStartAt(startAt)
                .jobEndAt(endAt)
                .success(success)
                .message(message.toString())
                .totalCrawling(totalCrawling)
                .createAt(new Date())
                .updateAt(new Date())
                .build();

        crawlingLogService.createCrawlingSchedulerLog(log);

        YoutubeChannel updateChannel = YoutubeChannel.builder()
                .id(channel.getId())
                .status(Status.POST)
                .category(channel.getCategory())
                .channelId(channel.getChannelId())
                .channelTitle(channel.getChannelTitle())
                .etag(channel.getEtag())
                .description(channel.getDescription())
                .thumbnail(channel.getThumbnail())
                .publishAt(channel.getPublishAt())
                .crawlingAt(System.currentTimeMillis())
                .createAt(channel.getCreateAt())
                .updateAt(new Date())
                .build();

        youtubeChannelService.updateYoutubeChannel(updateChannel);
    }

    public YoutubeChannel saveChannelInfoByChannelId(final String channelId, final String category) {
        YoutubeChannel saveChannel = null;

        if (StringUtils.isBlank(YOUTUBE_API_KEY)) {
            logger.error("Youtube channel save error !! youtube API key is null");
            return saveChannel;
        }

        StringBuilder message = new StringBuilder();
        Boolean success = false;

        UriComponents components = UriComponentsBuilder.fromHttpUrl(API_YOUTUBE_CHANNEL_URL)
                .queryParam(KEY, YOUTUBE_API_KEY)
                .queryParam(PART, "id", "snippet")
                .queryParam(ID, channelId)
                .build();

        logger.info("Youtube " + category + " channel save start .... channel id = " + channelId);
        Long startAt = System.currentTimeMillis();
        try {
            URL url = components.toUri().toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode result = mapper.readTree(connection.getInputStream());

                if (result.has("items")) {
                    JsonNode items = result.get("items").get(0);

                    JsonNode id = items.get("id");
                    JsonNode etag = items.get("etag");
                    JsonNode snippet = items.get("snippet");
                    JsonNode title = snippet.get("title");
                    JsonNode description = snippet.get("description");
                    JsonNode publishedAt = snippet.get("publishedAt");
                    JsonNode thumbnails = snippet.get("thumbnails");
                    JsonNode thumbnailsHigh = thumbnails.get("high");
                    JsonNode thumbnailsHighUrl = thumbnailsHigh.get("url");

                    Date publishDate = new Date();

                    try {
                        publishDate = youtubeDateFormat.parse(publishedAt.asText());
                    } catch (ParseException e) {
                        logger.error("Youtube channel publish date parsing error !! " + e.getMessage());
                    }

                    YoutubeChannel channel = YoutubeChannel.builder()
                            .status(Status.POST)
                            .category(category)
                            .channelId(id.asText())
                            .etag(etag.asText())
                            .channelTitle(title.asText())
                            .description(description.asText().replaceAll("\\R", ""))
                            .thumbnail(thumbnailsHighUrl.asText())
                            .publishAt(publishDate)
                            .crawlingAt(0)
                            .createAt(new Date())
                            .updateAt(new Date())
                            .build();

                    result = null;

                    try {
                        saveChannel = youtubeChannelService.createYoutubeChannel(channel);

                        logger.info("Youtube " + category + " channel save success !! channel info = " + saveChannel.toString());
                        message.append("Youtube ").append(category).append(" channel save success !!");
                        success = true;
                    } catch (Exception e) {
                        logger.error("Youtube " + category + " channel save error !! ", e.getMessage());
                        message.append("Youtube ").append(category).append(" channel save error !!");
                        success = false;
                    }
                } else {
                    logger.error("Youtbue " + category + " channel save error !! no items");
                    message.append("Youtube ").append(category).append(" channel save error !! no items");
                    success = false;
                }
            } else {
                logger.error("Youtube " + category + " channel URL connection error !! status code = " + connection.getResponseCode());
                message.append("Youtube ").append(category).append(" channel URL connection error !!");
                success = false;
            }
        } catch (MalformedURLException e) {
            logger.error("Youtube " + category + " channel URL Exception error !! " + e.getMessage());
            message.append("Youtube ").append(category).append("channel URL Exception error !!");
            success = false;
        } catch (IOException e) {
            logger.error("Yuotube " + category + " channel IO Exception error !! " + e.getMessage());
            message.append("Youtube ").append(category).append(" channel IO Exception error !!");
            success = false;
        }

        logger.info("Youtube channel save end ....");
        Long endAt = System.currentTimeMillis();

        CrawlingLog log = CrawlingLog.builder()
                .jobName("Youtube " + category + " channel info crawling")
                .jobStartAt(startAt)
                .jobEndAt(endAt)
                .success(success)
                .message(message.toString())
                .totalCrawling(success == Boolean.TRUE? 1 : 0)
                .createAt(new Date())
                .updateAt(new Date())
                .build();

        crawlingLogService.createCrawlingSchedulerLog(log);

        return saveChannel;
    }

    public YoutubeChannel saveChannelInfoByChannelUsername(final String username, final String category) {
        UriComponents components = UriComponentsBuilder.fromHttpUrl(API_YOUTUBE_CHANNEL_URL)
                .queryParam(KEY, YOUTUBE_API_KEY)
                .queryParam(PART, "id", "snippet")
                .queryParam(FOR_URSE_NAME, username)
                .build();

        try {
            URL url = components.toUri().toURL();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                ObjectMapper mapper = new ObjectMapper();

                final JsonNode result = mapper.readTree(connection.getInputStream());

                if (result.has("items")) {
                    JsonNode items = result.get("items");
                    JsonNode item = items.get(0);

                    return saveChannelInfoByChannelId(item.get("id").asText(), category);
                } else {
                    logger.error("Find Youtube Channel items is null !!");
                }
            } else {
                logger.error("Find Youtube Channel Connection error !! status code = " + connection.getResponseCode());
            }
        } catch (Exception e) {
            logger.error("Find Youtube Channel Id error !! " + e.getMessage());
        }

        return null;
    }
}
