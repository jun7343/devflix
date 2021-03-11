package kr.devflix.service;

import kr.devflix.entity.YoutubeChannel;
import kr.devflix.repository.YoutubeChennelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
    public YoutubeChannel updateYoutubeChannel(final YoutubeChannel channel) {
        return youtubeChennelRepository.save(channel);
    }
}
