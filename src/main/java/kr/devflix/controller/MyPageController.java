package kr.devflix.controller;

import kr.devflix.constant.RoleType;
import kr.devflix.entity.Member;
import kr.devflix.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Secured(RoleType.USER)
    @RequestMapping(path = "/my-page", method = RequestMethod.GET)
    public String myPageForm(@AuthenticationPrincipal Member user, Model model) {

        model.addAttribute("item", user);

        System.out.println(user.toString());

        return "/my-page/index";
    }

    @Secured(RoleType.USER)
    @RequestMapping(path = "/my-page", method = RequestMethod.POST)
    public String myPageAction(@RequestParam(name = "id", required = false)final long id,
                               @RequestParam(name = "username", required = false)final String username,
                               @RequestParam(name = "description", required = false)final String description,
                               @RequestParam(name = "path-base", required = false)final String pathBase,
                               @RequestParam(name = "image", required = false)final String image,
                               @RequestParam(name = "github", required = false)final String github,
                               @RequestParam(name = "facebook", required = false)final String facebook,
                               @RequestParam(name = "twitter", required = false)final String twitter,
                               @RequestParam(name = "instagram", required = false)final String instagram,
                               @RequestParam(name = "linked-in", required = false)final String linkedIn,
                               @AuthenticationPrincipal Member user, RedirectAttributes attrs) {
        if (user.getId().equals(id)) {
            if (StringUtils.isBlank(username)) {
                attrs.addFlashAttribute("errorMessage", "유저 이름 기입해 주세요.");

                return "redirect:/my-page";
            }

            memberService.updateMemberInfo(Member.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .password(user.getPassword())
                            .username(username)
                            .status(user.getStatus())
                            .description(description)
                            .github(github)
                            .facebook(facebook)
                            .twiter(twitter)
                            .instagram(instagram)
                            .linkedIn(linkedIn)
                            .pathBase(pathBase)
                            .imagePath(image)
                            .authority(user.getAuthority())
                            .createAt(user.getCreateAt())
                            .updateAt(new Date())
                            .build());

            // Security Context에 업데이트한 유저 정보 반영
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            final Member updateMember = memberService.loadUserByUsername(user.getEmail());
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(updateMember, authentication.getCredentials(), updateMember.getAuthorities());
            token.setDetails(authentication.getDetails());

            SecurityContextHolder.getContext().setAuthentication(token);
        } else {
            attrs.addFlashAttribute("errorMessage", "오류가 발생 하였습니다.");
        }

        return "redirect:/my-page";
    }

    @Secured(RoleType.USER)
    @RequestMapping(path = "/my-page/password", method = RequestMethod.GET)
    public String myPagePasswordForm() {
        return "/my-page/password";
    }

    @Secured(RoleType.USER)
    @RequestMapping(path = "/my-page/password", method = RequestMethod.POST)
    public String myPagePasswordAction(@RequestParam(name = "current-password", required = false)final String currentPassword,
                                       @RequestParam(name = "new-password", required = false)final String newPassword,
                                       @AuthenticationPrincipal Member user, RedirectAttributes attrs) {
        if (StringUtils.isBlank(currentPassword)) {
            attrs.addFlashAttribute("errorMessage", "현재 비밀번호 기입해 주세요.");

            return "redirect:/my-page/password";
        } else if (StringUtils.isBlank(newPassword)) {
            attrs.addFlashAttribute("errorMessage", "새로운 비밀번호 기입해 주세요.");

            return "redirect:/my-page/password";
        } else if (! passwordEncoder.matches(currentPassword, user.getPassword())) {
            attrs.addFlashAttribute("errorMessage", "현재 비밀번호와 맞지 않습니다.");

            return "redirect:/my-page/password";
        }

        memberService.updateMemberInfo(Member.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .password(passwordEncoder.encode(newPassword))
                        .username(user.getUsername())
                        .status(user.getStatus())
                        .description(user.getDescription())
                        .github(user.getGithub())
                        .facebook(user.getFacebook())
                        .twiter(user.getTwiter())
                        .instagram(user.getInstagram())
                        .linkedIn(user.getLinkedIn())
                        .pathBase(user.getPathBase())
                        .imagePath(user.getImagePath())
                        .authority(user.getAuthority())
                        .createAt(user.getCreateAt())
                        .updateAt(new Date())
                        .build());

        return "redirect:/logout";
    }
}
