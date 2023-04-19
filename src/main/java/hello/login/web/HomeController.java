package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    //@GetMapping("/")
    public String home() {
        return "home";
    }

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
    }
}