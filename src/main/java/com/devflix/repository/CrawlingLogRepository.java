package com.devflix.repository;

import com.devflix.entity.CrawlingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawlingLogRepository extends JpaRepository<CrawlingLog, Long> {
}
