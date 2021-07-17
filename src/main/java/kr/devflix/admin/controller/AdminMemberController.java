package kr.devflix.admin.controller;

import kr.devflix.constant.MemberStatus;
import kr.devflix.constant.RoleType;
import kr.devflix.entity.Member;
import kr.devflix.service.MemberService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@Secured(RoleType.ADMIN)
public class AdminMemberController {

    private final MemberService memberService;
    private final int DEFAULT_PER_PAGE_SIZE = 20;

    public AdminMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @RequestMapping(path = "/dfa/member", method = RequestMethod.GET)
    public String memberList(@RequestParam(name = "page", required = false, defaultValue = "0")final int page,
                             @RequestParam(name = "email", required = false)final String email,
                             @RequestParam(name = "status", required = false)MemberStatus status, Model model) {
        Page<Member> findList = null;

        if (StringUtils.isBlank(email) && status == null) {
            findList = memberService.findAll(page, DEFAULT_PER_PAGE_SIZE);
        } else {
            findList = memberService.findAllBySearch(email, status, page, DEFAULT_PER_PAGE_SIZE);
        }

        model.addAttribute("search-email", email);
        model.addAttribute("search-status", status);

        List<Integer> pageNumList = new ArrayList<>();

        model.addAttribute("list", findList);
        model.addAttribute("page", page);

        if (findList.getNumber() / 5 != 0 && ((findList.getNumber() / 5) * 5 - 1) > 0) {
            model.addAttribute("previousPageNum", (findList.getNumber() / 5) * 5 - 1);
        }

        if ((findList.getNumber() / 5) * 5 + 6 <= findList.getTotalPages()) {
            model.addAttribute("nextPageNum", (findList.getNumber() / 5 + 1) * 5);
        }

        int start = (findList.getNumber() / 5) * 5 + 1;
        int end = Math.min((findList.getNumber() / 5 + 1) * 5, findList.getTotalPages());

        for (int i = start; i <= end; i++) {
            pageNumList.add(i);
        }

        model.addAttribute("pageNumList", pageNumList);
        model.addAttribute("currentPageNum", findList.getNumber() + 1);
        model.addAttribute("pagination", findList.getTotalPages() > 1);

        return "/admin/member/list";
    }

    @RequestMapping(path = "/dfa/member/{id}", method = RequestMethod.GET)
    public String memberDetailForm(@PathVariable(name = "id")final long id,
                                   Model model) {
        Optional<Member> findOne = memberService.findOneById(id);

        if (findOne.isPresent()) {
            model.addAttribute("item", findOne.get());

            return "/admin/member/detail";
        } else {
            return "redirect:/dfa/member";
        }
    }

    @RequestMapping(path = "/dfa/member/{id}", method = RequestMethod.POST)
    public String memberDetailAction(@PathVariable(name = "id")final long id,
                                   @RequestParam(name = "authority", required = false)List<String> authority,
                                   @RequestParam(name = "status", required = false)MemberStatus status,
                                   RedirectAttributes attrs) {
        Optional<Member> findOne = memberService.findOneById(id);

        if (findOne.isPresent()) {
            final Member user = findOne.get();

            if (authority != null && status != null) {
                memberService.updateMemberInfo(Member.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .username(user.getUsername())
                        .status(status)
                        .description(user.getDescription())
                        .github(user.getGithub())
                        .facebook(user.getFacebook())
                        .twiter(user.getTwiter())
                        .instagram(user.getInstagram())
                        .linkedIn(user.getLinkedIn())
                        .pathBase(user.getPathBase())
                        .imagePath(user.getImagePath())
                        .authority(authority)
                        .createAt(user.getCreateAt())
                        .updateAt(new Date())
                        .build());

                attrs.addFlashAttribute("successMessage", "수정 완료 하였습니다.");
            } else {
                attrs.addFlashAttribute("errorMessage", "수정 도중 에러가 발생 하였습니다.");
            }
        } else {
            attrs.addFlashAttribute("errorMessage", "수정 도중 에러가 발생 하였습니다.");
        }

        return "redirect:/dfa/member/" + id;
    }
}
