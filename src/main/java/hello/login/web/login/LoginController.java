package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form){
        return "login/loginForm";
    }

    /* //쿠키만 사용한 로그인 처리
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form,
                        BindingResult bindingResult, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        log.info("login? {}",loginMember);

        if(loginMember == null){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리
        //세션 쿠키 만들기
        Cookie idCookie = new Cookie("memberId",String.valueOf(loginMember.getId()));
                        //Member 의 id를 String 으로 변환하여 담음
        response.addCookie(idCookie);
        return "redirect:/";
    }
    */

    /* //직접 만든 세션으로 로그인 처리
    @PostMapping("/login")
    public String loginV2(@Valid @ModelAttribute LoginForm form,
                          BindingResult bindingResult, HttpServletResponse response){
        //입력값(필드)에 타입이나 검증 오류가 있는지
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }

        //로그인 처리
        Member loginMember = loginService.login(form.getLoginId(),form.getPassword());
        log.info("login? {}",loginMember);

        //로그인 실패 시
        if(loginMember == null){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 시
        //세션 관리자를 통해 세션을 생성하고, 회원 데이터를 보관
        sessionManager.createSession(loginMember,response);
        return "redirect:/";
    }*/

    //HttpSession을 사용한 로그인 처리
    @PostMapping("/login")
    public String loginV3(@Valid @ModelAttribute LoginForm form,
                          BindingResult bindingResult, HttpServletRequest request){
        //필드 타입, 검증 에러
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }

        //로그인 처리
        Member loginMember = loginService.login(form.getLoginId(),form.getPassword());
        log.info("login? {}",loginMember);

        //로그인 실패
        if(loginMember == null){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성 (기본 true 옵션)
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);
        return "redirect:/";
    }

    /* //쿠키만 사용한 로그아웃 로직
    @PostMapping("/logout")
    public String logout(HttpServletResponse response){
        expireCookie(response, "memberId");
        return "redirect:/";
    }*/

    /*//직접 만든 세션으로 로그아웃 로직
    @PostMapping("/logout")
    public String logOutV2(HttpServletRequest request){
        //쿠키는 쿠키를 새로 MaxAge(0)으로 생성해서 만들기 때문에 response를,
        //세션은 요청에서 세션 아이디를 찾아 지우기 때문에 request를 param으로 받음
        sessionManager.expire(request);
        return "redirect:/";
    }*/

    //HttpSession을 이용한 로그아웃 처리
    @PostMapping("/logout")
    public String logOutV3(HttpServletRequest request){
        //세션을 만들어 반환하지 않도록 false 옵션 사용
        HttpSession session = request.getSession(false);

        //세션 삭제
        if(session != null){
            session.invalidate();
        }

        return "redirect:/";
    }



    /**
     * cookie.setMaxAge(0) <- 해당 쿠키 즉시 종료
     */
    private void expireCookie(HttpServletResponse response, String cookieName){
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
