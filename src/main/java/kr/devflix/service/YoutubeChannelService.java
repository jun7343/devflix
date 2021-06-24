package kr.devflix.service;

import kr.devflix.posts.Status;
import kr.devflix.entity.YoutubeChannel;
import kr.devflix.repository.YoutubeChennelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class YoutubeChannelService {

    private final YoutubeChennelRepository youtubeChennelRepository;

    @Transactional
    public YoutubeChannel createYoutubeChannel(final YoutubeChannel youtubeChannel) {
        return youtubeChennelRepository.save(youtubeChannel);
    }

    @Transactional
    public List<YoutubeChannel> findAllOrderByCrawlingAtAsc() {
        return youtubeChennelRepository.findAll(Sort.by(Sort.Order.asc("crawlingAt")));
    }

    @Transactional
    public void updateYoutubeChannel(final YoutubeChannel channel) {
        youtubeChennelRepository.save(channel);
    }

    @Transactional
    public List<YoutubeChannel> findAllByStatusOrderByCreateAtDesc(Status status) {
        return youtubeChennelRepository.findAllByStatusOrderByCreateAtDesc(status);
    }

    @Transactional
    public List<YoutubeChannel> findAll() {
        return youtubeChennelRepository.findAll(Sort.by(Sort.Order.desc("createAt")));
    }

    @Transactional
    public Optional<YoutubeChannel> findOneById(long id) {
        return youtubeChennelRepository.findById(id);
    }
}
