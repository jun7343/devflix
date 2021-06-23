package kr.devflix.admin.controller;

import kr.devflix.constant.RoleType;
import kr.devflix.entity.ContactMe;
import kr.devflix.service.ContactMeService;
import kr.devflix.utils.JavaMailUtils;
import lombok.RequiredArgsConstructor;
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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@Secured(RoleType.MANAGER)
@RequiredArgsConstructor
public class AdminContactMeController {

    private final ContactMeService contactMeService;
    private final JavaMailUtils javaMailUtils;
    private final int DEFAULT_PER_PAGE_SIZE = 20;

    @RequestMapping(path = "/dfa/contact-me", method = RequestMethod.GET)
    public String contactMeList(@RequestParam(name = "page", required = false, defaultValue = "0")final int page,
                                @RequestParam(name = "title", required = false)final String title,
                                @RequestParam(name = "email", required = false)final String email,
                                @RequestParam(name = "answer", required = false)final String answer,
                                Model model) {
        Page<ContactMe> findList = null;

        if (StringUtils.isBlank(title) && StringUtils.isBlank(email) && StringUtils.isBlank(answer)) {
            findList = contactMeService.findAll(page, DEFAULT_PER_PAGE_SIZE);
        } else {
            findList = contactMeService.findAllBySearch(title, email, answer, page, DEFAULT_PER_PAGE_SIZE);
        }

        model.addAttribute("search-title", title);
        model.addAttribute("search-email", email);
        model.addAttribute("search-answer", answer);

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

        return "/admin/contact-me/list";
    }

    @RequestMapping(path = "/dfa/contact-me/{id}", method = RequestMethod.GET)
    public String contactMeDetailForm(@PathVariable(name = "id")final long id, Model model) {
        Optional<ContactMe> findOne = contactMeService.findOneById(id);

        if (findOne.isPresent()) {
            model.addAttribute("item", findOne.get());

            return "/admin/contact-me/detail";
        } else {
            return "redirect:/dfa/contact-me";
        }
    }

    @RequestMapping(path = "/dfa/contact-me/{id}", method = RequestMethod.POST)
    public String contactMeDetailAction(@PathVariable(name = "id")final long id,
                                        @RequestParam(name = "answer", required = false)final String answer,
                                        RedirectAttributes attrs, HttpServletRequest request) {
        Optional<ContactMe> findOne = contactMeService.findOneById(id);

        if (findOne.isPresent()) {
            final ContactMe item = findOne.get();

            javaMailUtils.contactMeSendMail(item, answer, request);
            contactMeService.updateContactMe(ContactMe.builder()
                    .id(item.getId())
                    .title(item.getTitle())
                    .email(item.getEmail())
                    .content(item.getContent())
                    .confirm(true)
                    .createAt(item.getCreateAt())
                    .updateAt(new Date())
                    .build());

            attrs.addFlashAttribute("successMessage", "문의 메일 답변 정상적으로 전송 하였습니다.");
        } else {
            attrs.addFlashAttribute("errorMessage", "답변 저장중 에러가 발생 하였습니다.");
        }

        return "redirect:/dfa/contact-me/" + id;
    }
}
