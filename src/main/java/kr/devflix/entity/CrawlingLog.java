package kr.devflix.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "crawling_log")
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrawlingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Builder
    public CrawlingLog(final Long id, final String jobName, final Long jobStartAt,
                       final Long jobEndAt, final Integer totalCrawling, final Boolean success,
                       final String message, final Date createAt, final Date updateAt) {
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
}
