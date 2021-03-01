package com.devflix.repository;

import com.devflix.entity.CrawlingSchedulerLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawlingSchedulerLogRepository extends JpaRepository<CrawlingSchedulerLog, Long> {
}
