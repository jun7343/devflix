package kr.devflix.controller;

import kr.devflix.entity.ContactMe;
import kr.devflix.entity.Member;
import kr.devflix.service.ContactMeSearvice;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
@RequiredArgsConstructor
public class ContactMeController {

    private final ContactMeSearvice contactMeSearvice;

    @RequestMapping(path = "/contact-me", method = RequestMethod.GET)
    public String contactMeForm(@AuthenticationPrincipal Member user, Model model) {
        if (user != null) {
            model.addAttribute("email", user.getEmail());
        }

        return "contact-me";
    }

    @RequestMapping(path = "/contact-me", method = RequestMethod.POST)
    public String contactMeAction(@RequestParam(name = "title", required = false)final String title,
                                  @RequestParam(name = "email", required = false)final String email,
                                  @RequestParam(name = "content", required = false)final String content,
                                  RedirectAttributes attrs) {
        if (StringUtils.isBlank(title)) {
            attrs.addFlashAttribute("errorMessage", "제목 기입해 주세요.");

            return "redirect:/contact-me";
        } else if (StringUtils.isBlank(email)) {
            attrs.addFlashAttribute("errorMessage", "이메일 기입해 주세요.");

            return "redirect:/contact-me";
        }

        final ContactMe save = contactMeSearvice.createContactMe(ContactMe.builder()
                .title(title)
                .email(email)
                .content(content)
                .confirm(false)
                .createAt(new Date())
                .updateAt(new Date())
                .build());


        if (save != null) {
            attrs.addFlashAttribute("successMessage", "성공적으로 문의 내용이 작성 되었습니다.");
        } else {
            attrs.addFlashAttribute("errorMessage", "문의 내용 저장중 에러가 발생 하였습니다.");
        }

        return "redirect:/contact-me";
    }
}
