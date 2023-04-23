package hello.login.web.session;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {
    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null){
            return "세션이 없습니다.";
        }

        //세션 데이터 출력(세션 아이디의 키인 세션 네임과 그에 해당하는 값(객체))
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name={}, value={}",name,session.getAttribute(name)));

        //JSESSIONID 값
        log.info("sessionId={}",session.getId());

        //세션 유효 시간 second
        log.info("maxInactiveInterval={}",session.getMaxInactiveInterval());

        //세션 생성 일시
        log.info("creationTime={}", new Date(session.getCreationTime()));

        //사용자가 최근에 서버에 접근한 시간
        log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime()));

        //새로 생성된 세션인지, 이미 과거에 만들어져서 클라이언트에서 서버로 요청해서 조회된 세션인지
        log.info("isNew={}", session.isNew());

        return "세션 출력";
    }
}
