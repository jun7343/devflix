package kr.devflix.entity;

import kr.devflix.constant.Status;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "youtube_channel")
public class YoutubeChannel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_youtube_channel_id")
    @SequenceGenerator(name = "seq_youtube_channel_id", sequenceName = "youtube_channel_id_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private String category;

    @Column(name = "channel_id", unique = true, updatable = false)
    private String channelId;

    @Column(name = "channel_title")
    private String channelTitle;

    @Column
    private String etag;

    @Column
    private String description;

    @Column
    private String thumbnail;

    @Column(name = "publish_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishAt;

    @Column(name = "crawling_at")
    private Long crawlingAt;

    @Column(name = "create_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    protected YoutubeChannel() {
    }

    private YoutubeChannel(Long id, Status status, String channelId, String channelTitle, String category, String etag,
                           String description, String thumbnail, Date publishAt, Long crawlingAt, Date createAt, Date updateAt) {
        this.id = id;
        this.status = status;
        this.category = category;
        this.channelId = channelId;
        this.channelTitle = channelTitle;
        this.etag = etag;
        this.description = description;
        this.thumbnail = thumbnail;
        this.publishAt = publishAt;
        this.crawlingAt = crawlingAt;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public static YoutubeChannelBuilder builder() {
        return new YoutubeChannelBuilder();
    }

    public static class YoutubeChannelBuilder {
        private Long id;
        private Status status;
        private String channelId;
        private String channelTitle;
        private String category;
        private String etag;
        private String description;
        private String thumbnail;
        private Date publishAt;
        private Long crawlingAt;
        private Date createAt;
        private Date updateAt;

        YoutubeChannelBuilder() {
        }

        public YoutubeChannelBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public YoutubeChannelBuilder status(Status status) {
            this.status = status;
            return this;
        }

        public YoutubeChannelBuilder channelId(String channelId) {
            this.channelId = channelId;
            return this;
        }

        public YoutubeChannelBuilder channelTitle(String channelTitle) {
            this.channelTitle = channelTitle;
            return this;
        }

        public YoutubeChannelBuilder category(String category) {
            this.category = category;
            return this;
        }

        public YoutubeChannelBuilder etag(String etag) {
            this.etag = etag;
            return this;
        }

        public YoutubeChannelBuilder description(String description) {
            this.description = description;
            return this;
        }

        public YoutubeChannelBuilder thumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        public YoutubeChannelBuilder publishAt(Date publishAt) {
            this.publishAt = publishAt;
            return this;
        }

        public YoutubeChannelBuilder crawlingAt(Long crawlingAt) {
            this.crawlingAt = crawlingAt;
            return this;
        }

        public YoutubeChannelBuilder createAt(Date createAt) {
            this.createAt = createAt;
            return this;
        }

        public YoutubeChannelBuilder updateAt(Date updateAt) {
            this.updateAt = updateAt;
            return this;
        }

        public YoutubeChannel build() {
            return new YoutubeChannel(id, status, channelId, channelTitle, category, etag, description, thumbnail, publishAt,
                    crawlingAt, createAt, updateAt);
        }
    }

    @Override
    public String toString() {
        return "YoutubeChannel{" +
                "id=" + id +
                ", status=" + status +
                ", category='" + category + '\'' +
                ", channelId='" + channelId + '\'' +
                ", channelTitle='" + channelTitle + '\'' +
                ", etag='" + etag + '\'' +
                ", description='" + description + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", publishAt=" + publishAt +
                ", crawlingAt=" + crawlingAt +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }

    public Long getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public String getCategory() {
        return category;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public String getEtag() {
        return etag;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Date getPublishAt() {
        return publishAt;
    }

    public Long getCrawlingAt() {
        return crawlingAt;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }
}
