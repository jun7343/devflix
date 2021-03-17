package kr.devflix.repository;

import kr.devflix.entity.CrawlingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawlingLogRepository extends PagingAndSortingRepository<CrawlingLog, Long>, JpaSpecificationExecutor<CrawlingLog>{
}
