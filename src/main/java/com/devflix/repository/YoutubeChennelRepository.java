package com.devflix.repository;

import com.devflix.entity.YoutubeChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YoutubeChennelRepository extends JpaRepository<YoutubeChannel, Long> {
}
