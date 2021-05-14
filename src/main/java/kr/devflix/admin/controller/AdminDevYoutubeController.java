package kr.devflix.admin.controller;

import kr.devflix.clawler.YoutubeCrawler;
import kr.devflix.constant.RoleType;
import kr.devflix.constant.Status;
import kr.devflix.entity.YoutubeChannel;
import kr.devflix.service.YoutubeChannelService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@Secured(RoleType.MANAGER)
@RequiredArgsConstructor
public class AdminDevYoutubeController {

    private final YoutubeChannelService youtubeChannelService;
    private final YoutubeCrawler youtubeCrawler;
    private final String YOUTUBE_WEB_URL = "https://www.youtube.com/";

    @RequestMapping(path = "/dfa/dev-post/youtube-list", method = RequestMethod.GET)
    public String devYotubeList(Model model) {
        List<YoutubeChannel> findAll = youtubeChannelService.findAll();

        model.addAttribute("list", findAll);

        return "/admin/dev-post/youtube-list";
    }

    @RequestMapping(path = "/dfa/dev-post/youtube-list/save", method = RequestMethod.POST)
    public String devYoutubeChannelSave(@RequestParam(name = "url", required = false)final String URL,
                                        @RequestParam(name = "category", required = false)String category,
                                        RedirectAttributes attrs) {
        if (StringUtils.isBlank(URL)) {
            attrs.addFlashAttribute("errorMessage", "URL 기입해 주세요.");

            return "redirect:/dfa/dev-post/youtube-list";
        }

        if (StringUtils.isBlank(category)) {
            category = "YOUTUBE";
        }

        String[] urlDepth = StringUtils.substringAfter(URL, YOUTUBE_WEB_URL).split("/");

        if (urlDepth.length > 1) {
            if (StringUtils.equals(urlDepth[0], "user") || StringUtils.equals(urlDepth[0], "c")) {
                youtubeCrawler.targetCrawling(
                        youtubeCrawler.saveChannelInfoByChannelUsername(StringUtils.trim(urlDepth[1]), category), 50);

                attrs.addFlashAttribute("successMessage", "성공적으로 등록 하였습니다.");
            } else {
                youtubeCrawler.targetCrawling(youtubeCrawler.saveChannelInfoByChannelId(urlDepth[1], category.toUpperCase(Locale.ROOT)), 50);

                attrs.addFlashAttribute("succesMessage", "성공적으로 등록 하였습니다.");
            }
        } else {
            attrs.addFlashAttribute("errorMessage", "정확한 Youtube Channel URL 기입해 주세요.");
        }

        return "redirect:/dfa/dev-post/youtube-list";
    }

    @RequestMapping(path = "/dfa/dev-post/youtube-list/{id}", method = RequestMethod.GET)
    public String devYoutubeDetailForm(@PathVariable(name = "id")final long id, Model model) {
        Optional<YoutubeChannel> findOne = youtubeChannelService.findOneById(id);

        if (findOne.isPresent()) {
            model.addAttribute("item", findOne.get());

            return "/admin/dev-post/youtube-detail";
        } else {
            return "redirect:/dfa/dev-post/youtube-list";
        }
    }

    @RequestMapping(path = "/dfa/dev-post/youtube-list/{id}", method = RequestMethod.POST)
    public String devYoutubeDetailAction(@PathVariable(name = "id")final long id,
                                         @RequestParam(name = "channel-title", required = false)final String channelTitle,
                                         @RequestParam(name = "thumbnail", required = false)final String thumbnail,
                                         @RequestParam(name = "category", required = false)final String category,
                                         @RequestParam(name = "description", required = false)final String description,
                                         @RequestParam(name = "status", required = false)Status status, RedirectAttributes attrs) {
        Optional<YoutubeChannel> findOne = youtubeChannelService.findOneById(id);

        if (findOne.isPresent()) {
            if (StringUtils.isBlank(channelTitle)) {
                attrs.addFlashAttribute("errorMessage", "채널 제목 기입해 주세요.");

                return "redirect:/dfa/dev-post/youtube-list/" + id;
            } else if (StringUtils.isBlank(thumbnail)) {
                attrs.addFlashAttribute("errorMessage", "섬네일 주소 기입해 주세요.");

                return "redirect:/dfa/dev-post/youtube-list/" + id;
            } else if (StringUtils.isBlank(category)) {
                attrs.addFlashAttribute("errorMessage", "카테고리 기입해 주세요.");

                return "redirect:/dfa/dev-post/youtube-list/" + id;
            } else if (status == null) {
                attrs.addFlashAttribute("errorMessage", "상태 선택해 주세요.");

                return "redirect:/dfa/dev-post/youtube-list/" + id;
            }

            YoutubeChannel channel = findOne.get();

            youtubeChannelService.updateYoutubeChannel(YoutubeChannel.builder()
                    .id(id)
                    .channelId(channel.getChannelId())
                    .channelTitle(channelTitle)
                    .etag(channel.getEtag())
                    .thumbnail(thumbnail)
                    .category(category.toUpperCase(Locale.ROOT))
                    .description(description)
                    .crawlingAt(channel.getCrawlingAt())
                    .status(status)
                    .publishAt(channel.getPublishAt())
                    .createAt(channel.getCreateAt())
                    .updateAt(new Date())
                    .build());

            attrs.addFlashAttribute("successMessage", "성공적으로 수정 하였습니다.");

            return "redirect:/dfa/dev-post/youtube-list/" + id;
        } else {
            return "redirect:/dfa/dev-post/youtube-list";
        }
    }
}
