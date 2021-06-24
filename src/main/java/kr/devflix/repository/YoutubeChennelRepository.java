package kr.devflix.repository;

import kr.devflix.posts.Status;
import kr.devflix.entity.YoutubeChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YoutubeChennelRepository extends JpaRepository<YoutubeChannel, Long> {
    List<YoutubeChannel> findAllByStatusOrderByCreateAtDesc(Status status);
}
