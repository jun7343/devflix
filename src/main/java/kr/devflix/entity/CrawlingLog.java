package kr.devflix.entity;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "crawling_log")
public class CrawlingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_crawling_log_id")
    @SequenceGenerator(name = "seq_crawling_log_id", sequenceName = "crawling_log_id_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "job_start_at")
    private Long jobStartAt;

    @Column(name = "job_end_at")
    private Long jobEndAt;

    @Column(name = "total_crawling")
    private Integer totalCrawling;

    @Column
    private Boolean success;

    @Column
    private String message;

    @Column(name = "create_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    protected CrawlingLog() {
    }

    private CrawlingLog(Long id, String jobName, Long jobStartAt, Long jobEndAt, Integer totalCrawling,
                        Boolean success, String message, Date createAt, Date updateAt) {
        this.id = id;
        this.jobName = jobName;
        this.jobStartAt = jobStartAt;
        this.jobEndAt = jobEndAt;
        this.totalCrawling = totalCrawling;
        this.success = success;
        this.message = message;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public static CrawlingLogBuilder builder() {
        return new CrawlingLogBuilder();
    }

    public static class CrawlingLogBuilder {
        private Long id;
        private String jobName;
        private Long jobStartAt;
        private Long jobEndAt;
        private Integer totalCrawling;
        private Boolean success;
        private String message;
        private Date createAt;
        private Date updateAt;

        CrawlingLogBuilder() {
        }

        public CrawlingLogBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CrawlingLogBuilder jobName(String jobName) {
            this.jobName = jobName;
            return this;
        }

        public CrawlingLogBuilder jobStartAt(Long jobStartAt) {
            this.jobStartAt = jobStartAt;
            return this;
        }
        public CrawlingLogBuilder jobEndAt(Long jobEndAt) {
            this.jobEndAt = jobEndAt;
            return this;
        }

        public CrawlingLogBuilder totalCrawling(Integer totalCrawling) {
            this.totalCrawling = totalCrawling;
            return this;
        }

        public CrawlingLogBuilder success(Boolean success) {
            this.success = success;
            return this;
        }

        public CrawlingLogBuilder message(String message) {
            this.message = message;
            return this;
        }

        public CrawlingLogBuilder createAt(Date createAt) {
            this.createAt = createAt;
            return this;
        }

        public CrawlingLogBuilder updateAt(Date updateAt) {
            this.updateAt = updateAt;
            return this;
        }

        public CrawlingLog build() {
            return new CrawlingLog(id, jobName, jobStartAt, jobEndAt, totalCrawling, success, message, createAt, updateAt);
        }
    }

    @Override
    public String toString() {
        return "CrawlingLog{" +
                "id=" + id +
                ", jobName='" + jobName + '\'' +
                ", jobStartAt=" + jobStartAt +
                ", jobEndAt=" + jobEndAt +
                ", totalCrawling=" + totalCrawling +
                ", success=" + success +
                ", message='" + message + '\'' +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getJobName() {
        return jobName;
    }

    public Long getJobStartAt() {
        return jobStartAt;
    }

    public Long getJobEndAt() {
        return jobEndAt;
    }

    public Integer getTotalCrawling() {
        return totalCrawling;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }
}
