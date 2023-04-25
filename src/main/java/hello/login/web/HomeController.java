package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentresolver.Login;
import hello.login.web.login.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    //@GetMapping("/")
    public String home() {
        return "home";
    }

    /*//쿠키만 사용한 로그인 홈
    @GetMapping("/")
    public String homeLogin(
            @CookieValue(name="memberId", required = false) Long memberId,
            //@CookieValue(name="쿠키 저장 key", required=t/f)
            //쿠키가 없을 경우도 포함하기 때문에 required=false
            Model model){
            //로그인 홈 화면에서 사용자 정보 보여줘야하기 때문에 Model 사용

        if(memberId == null){
            //쿠키에 memberId가 없다면 home으로 보냄
            return "home";
        }

        //로그인
        Member loginMember = memberRepository.findById(memberId);
        if(loginMember==null){
            //쿠키에 memberId는 존재하나, db에서 찾지 못하는 경우도 존재
            //ex) 회원 탈퇴
            return "home";
        }

        //위의 조건을 모두 빠져나왔다면 모델에 값을 담고
        //가입자 홈 화면으로 return
        model.addAttribute("member",loginMember);
        return "loginHome";
    }*/

    /*//직접 만든 세션으로 처리하는 홈
    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model){
        //세션 관리자에 저장된 회원 정보를 조회
        Member member = (Member)sessionManager.getSession(request);

        //로그인 안된 사용자
        if(member == null){
            return "home";
        }

        //로그인 된 사용자
        model.addAttribute("member",member);
        return "loginHome";
    }*/

    /*//HttpSession을 이용한 홈
    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model){
        //세션 관리자에 저장된 회원 정보를 조회
        HttpSession session = request.getSession(false);

        //세션이 없으면 home
        if(session == null){
            return "home";
        }

        //세션이 있다면 로그인한 멤버 찾아 담기
        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);

        //세션에 회원 데이터가 없으면 home
        //로그인하지 않은 사용자라도 페이지에 방문할 떄 HttpSession이 생성되어버림
        //getSession<-기본값 true
        if(loginMember == null){
            return "home";
        }

        //로그인 된 사용자(세션 유지되어 있으면)
        model.addAttribute("member",loginMember);
        return "loginHome";
    }*/

   /* //@SessionAttribute 사용
    @GetMapping("/")
    public String homeLoginV3Spring(
            @SessionAttribute(name=SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            Model model){

        //@SessionAttribute
        //sessionConst.LOGIN_MEMBER로 세션 아이디를 찾아 저장된 객체를 찾음 -> Member에 담음
        //@SessionAttribute는 기본적으로 getSession(false). 세션을 생성하지 않음

        //세션이 없으면 home (Member가 null)
        if(loginMember == null){
            return "home";
        }

        //로그인 된 사용자(세션 유지되어 있으면)
        model.addAttribute("member",loginMember);
        return "loginHome";
    }*/

    //직접 만든 ArgumentResolver 사용
    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(@Login Member loginMember, Model model){
        //세션에 회원 데이터가 없으면 home
        if(loginMember == null){
            return "home";
        }
        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member",loginMember);
        return "loginHome";
    }
}