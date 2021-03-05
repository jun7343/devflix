package com.devflix.entity;

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
    private long jobStartAt;

    @Column(name = "job_end_at")
    private long jobEndAt;

    @Column(name = "total_crawling")
    private int totalCrawling;

    @Column
    private boolean success;

    @Column
    private String message;

    @Column(name = "create_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    @Builder
    public CrawlingLog(final Long id, final String jobName, final long jobStartAt,
                       final long jobEndAt, final int totalCrawling, final boolean success,
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
