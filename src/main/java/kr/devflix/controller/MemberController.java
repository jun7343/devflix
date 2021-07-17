package kr.devflix.controller;

import com.google.common.collect.ImmutableMap;
import kr.devflix.constant.MemberConfirmType;
import kr.devflix.constant.MemberStatus;
import kr.devflix.entity.Member;
import kr.devflix.entity.MemberConfirm;
import kr.devflix.service.MemberService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

@Controller
public class MemberController {

    private final MemberService memberService;
    private final String RESULT = "result";
    private final String MESSAGE = "msg";
    private final Pattern emailPattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginForm() {
        return "/login/index";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String loginAction(HttpServletRequest request, @RequestParam(name = "email", required = false)String email,
                              @RequestParam(name = "password", required = false)String password, RedirectAttributes attrs) {
        if (StringUtils.isBlank(email)) {
            attrs.addFlashAttribute("errorMessage", "이메일 기입해 주세요.");

            return "redirect:/login?error";
        } else if (! emailPattern.matcher(email).matches()) {
            attrs.addFlashAttribute("errorMessage", "이메일 형식이 올바르지 않습니다.");

            return "redirect:/login?error";
        } else if (StringUtils.isBlank(password)) {
            attrs.addFlashAttribute("errorMessage", "패스워드 기입해 주세요.");

            return "redirect:/login?error";
        }

        AuthenticationException exception = (AuthenticationException) request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        if (exception != null) {
            attrs.addFlashAttribute("errorMessage", exception.getMessage());

            return "redirect:/login?error";
        }

        return "home";
    }

    @RequestMapping(path = "/login/join-us", method = RequestMethod.GET)
    public String joinUsForm() {
        return "/login/join-us";
    }

    @RequestMapping(path = "/login/join-us", method = RequestMethod.POST)
    public String joinUsAction(@RequestParam(name = "email", required = false)final String email, @RequestParam(name = "username", required = false)final String username,
            @RequestParam(name = "password", required = false)final String password, @RequestParam(name = "code", required = false)final String code, RedirectAttributes attrs) {
        String message = "";
        boolean success = false;

        if (StringUtils.isBlank(email)) {
            message = "이메일 기입해 주세요.";
        } else if (! emailPattern.matcher(email).matches()) {
            message = "이메일 형식이 올바르지 않습니다.";
        } else if (StringUtils.isBlank(username)) {
            message = "유저 이름 기입해 주세요.";
        } else if (StringUtils.isBlank(password)) {
            message = "패스워드 기입해 주세요.";
        } else if (StringUtils.isBlank(code)) {
            message = "인증코드가 기입해 주세요.";
        } else {
            success = true;
        }

        if (! success) {
            attrs.addFlashAttribute("errorMessage", message);

            return "redirect:/login/join-us";
        }

        final MemberConfirm confirm = memberService.findMemberConfirmByEmail(email);

        if (confirm == null) {
            message = "인증 확인해 주세요.";
        } else if (! StringUtils.equals(confirm.getUuid(), code)) {
            message = "인증코드가 다릅니다. 다시 확인해 주세요.";
        } else {
            final Member newMember = memberService.createMemberAndDeleteMemberConfirm(email, password, username);

            if (newMember == null) {
                success = false;
                message = "아이디 생성중 에러가 발생하였습니다. 관리자에게 문의해 주세요.";
            }
        }

        if (! success) {
            attrs.addFlashAttribute("errorMessage", message);

            return "redirect:/login/join-us";
        }

        return "redirect:/login";
    }

    @RequestMapping(path = "/login/join-us/email-authentication", method = RequestMethod.POST)
    @ResponseBody
    public ImmutableMap<String, Object> emailAuthentication(@RequestParam(value = "email", required = false)final String email,
                                                            HttpServletRequest request) {
        if (StringUtils.isBlank(email)) {
            return ImmutableMap.of(RESULT, false, MESSAGE, "이메일을 기입해 주세요.");
        } else if (! emailPattern.matcher(email).matches()) {
            return ImmutableMap.of(RESULT, false, MESSAGE, "이메일 형식이 올바르지 않습니다.");
        }

        final Member findUser = memberService.findUserByEmail(email);

        if (findUser != null) {
            return ImmutableMap.of(RESULT, false, MESSAGE, "이미 가입한 회원 입니다.");
        }

        final MemberConfirm findConfirm = memberService.findMemberConfirmByEmail(email);

        if (findConfirm != null && findConfirm.getConfirmCount() > 5) {
            return ImmutableMap.of(RESULT, false, MESSAGE, "요청 메일 횟수를 넘어섰습니다.");
        }

        final MemberConfirm confirm = memberService.createOrUpdateMemberConfirmByEmail(email, MemberConfirmType.EMAIL_AUTHENTICATION, request);

        if (confirm == null) {
            return ImmutableMap.of(RESULT, false, MESSAGE, "인증 과정중 에러가 발생하였습니다. 관리자에게 문의해 주세요.");
        }

        return ImmutableMap.of(RESULT, true, MESSAGE, "인증 메일 성공적으로 요청 하였습니다.");
    }

    @RequestMapping(path = "/login/join-us/code-authentication", method = RequestMethod.POST)
    @ResponseBody
    public ImmutableMap<String, Object> codeAuthentication(@RequestParam(name = "email", required = false)final String email,
                                                           @RequestParam(name = "code", required = false)final String code) {
        if (StringUtils.isBlank(email)) {
            return ImmutableMap.of(RESULT, false, MESSAGE, "이메일 기입해 주세요.");
        } else if (! emailPattern.matcher(email).matches()) {
            return ImmutableMap.of(RESULT, false, MESSAGE, "이메일 형식이 올바르지 않습니다.");
        } else if (StringUtils.isBlank(code)) {
            return ImmutableMap.of(RESULT, false, MESSAGE, "인증코드 기입해 주세요.");
        }

        final MemberConfirm confirm = memberService.findMemberConfirmByEmail(email);

        if (confirm == null) {
            return ImmutableMap.of(RESULT, false, MESSAGE, "이메일 인증 다시 확인해 주세요.");
        } else if (! StringUtils.equals(confirm.getUuid(), code)) {
            return ImmutableMap.of(RESULT, false, MESSAGE, "인증코드가 맞지 않습니다. 다시 확인해 주세요.");
        }

        return ImmutableMap.of(RESULT, true, MESSAGE, "인증 확인 되었습니다.");
    }

    @RequestMapping(path = "/login/find-password", method = RequestMethod.GET)
    public String findPasswordForm() {
        return "/login/find-password";
    }

    @RequestMapping(path = "/login/find-password", method = RequestMethod.POST)
    public String findPasswordAction(@RequestParam(name = "email", required = false)final String email,
                                     HttpServletRequest request, RedirectAttributes attrs) {
        if (StringUtils.isBlank(email)) {
            attrs.addFlashAttribute("errorMessage", "이메일을 기입해 주세요.");

            return "redirect:/login/find-password";
        } else if (! emailPattern.matcher(email).matches()) {
            attrs.addFlashAttribute("errorMessage", "이메일을 형식이 올바르지 않습니다.");

            return "redirect:/login/find-password";
        }

        final Member findUser = memberService.findUserByEmail(email);

        if (findUser == null) {
            attrs.addFlashAttribute("errorMessage", "회원가입이 필요 합니다.");

            return "redirect:/login/find-password";
        } else if (findUser.getStatus() == MemberStatus.LOCK) {
            attrs.addFlashAttribute("errorMessage", "잠금된 회원 입니다. 관리자에게 문의 하세요.");

            return "redirect:/login/find-password";
        }

        final MemberConfirm findConfirm = memberService.findMemberConfirmByEmail(email);

        if (findConfirm != null && findConfirm.getConfirmCount() > 5) {
            attrs.addFlashAttribute("errorMessage", "요청 횟수를 초과 하였습니다.");

            return "redirect:/login/find-password";
        }

        memberService.createOrUpdateMemberConfirmByEmail(email, MemberConfirmType.PASSWORD, request);

        return "redirect:/login";
    }

    @RequestMapping(path = "/login/new-password/{uuid}", method = RequestMethod.GET)
    public String newPasswordForm(@PathVariable(name = "uuid")final String uuid, Model model,
                               HttpServletResponse response) {
        final MemberConfirm findConfirm = memberService.findMemberConfirmByUuid(uuid);

        if (findConfirm == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);

            return null;
        } else if (findConfirm.getType() != MemberConfirmType.PASSWORD) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);

            return null;
        }

        model.addAttribute("uuid", uuid);

        return "/login/new-password";
    }

    @RequestMapping(path = "/login/new-password/{uuid}", method = RequestMethod.POST)
    public String newPasswordAction(@PathVariable(name = "uuid")final String uuid,
                                    @RequestParam(name = "password")final String password,
                                    HttpServletResponse response,
                                    RedirectAttributes attrs) {
        if (StringUtils.isBlank(password)) {
            attrs.addFlashAttribute("errorMessage", "비밀번호 기입해 주세요.");

            return "redirect:/login/new-password/" + uuid;
        }

        final MemberConfirm findConfirm = memberService.findMemberConfirmByUuid(uuid);

        if (findConfirm == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);

            return null;
        } else if (findConfirm.getType() != MemberConfirmType.PASSWORD) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);

            return null;
        }

        final Member updateUser = memberService.updateMemberPasswordAndDeleteMemberConfirm(password, findConfirm);

        if (updateUser == null) {
            attrs.addFlashAttribute("errorMessage", "에러가 발생하였습니다. 관리자에게 문의해 주세요.");

            return "/login/new-password/" + uuid;
        }

        return "redirect:/login";
    }
}
