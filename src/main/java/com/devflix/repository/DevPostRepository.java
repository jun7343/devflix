package com.devflix.repository;

import com.devflix.constant.DevPostCategory;
import com.devflix.entity.DevPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevPostRepository extends JpaRepository<DevPost, Long> {
    DevPost findTopOneByCategoryOrderByIdDesc(DevPostCategory category);
}
