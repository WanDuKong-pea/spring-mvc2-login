package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 세션 관리
 */
@Component
public class SessionManager {
    //상수 만드는 단축키 Ctrl+Alt+C
    public static final String SESSION_COOKIE_NAME="mySessionId";

    private Map<String,Object> sessionStore = new ConcurrentHashMap<>();

    /**
     * 1.세션 생성
     */
    public void createSession(Object value, HttpServletResponse response){
        //세션 id를 생성하고, 값을 세션에 저장
        String sessionId = UUID.randomUUID().toString();
                        //UUID 자바에서 제공하는 랜덤 값
        sessionStore.put(sessionId,value);

        //쿠키 생성 (쿠키에는 "mySessionId"와 생성된 UUID 랜덤 값 저장)
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME,sessionId);
        response.addCookie(mySessionCookie);
    }

    /**
     * 2.세션 조회
     */
    public Object getSession(HttpServletRequest request){
        //"request 에서 mySessionId에 해당하는 쿠키 찾음"
        Cookie sessionCookie = findCookie(request,SESSION_COOKIE_NAME);

        //쿠키가 null이라면 return null
        if(sessionCookie == null){
            return null;
        }

        //쿠키에 값이 있다면 seesionStore에서
        //cookie의 Value인 UUID 랜덤 값(key)에 해당하는 value(객체) 가져옴
        return sessionStore.get(sessionCookie.getValue());
    }

    /**
     * 세션 만료
     */
    public void expire(HttpServletRequest request){
        //요청에서 "mySessionId"에 해당하는 쿠키를 찾음
        Cookie sessionCookie = findCookie(request,SESSION_COOKIE_NAME);
        if(sessionCookie != null){
            //요청에 쿠키가 null이 아니라면
            //sessionStore에서 sessionCookie의 값(UUID)에 해당하는 데이터 지움
            sessionStore.remove(sessionCookie.getValue());
        }
    }

    /**
     * sessionId에 해당하는 쿠키 찾아 반환
     */
    private Cookie findCookie(HttpServletRequest request, String cookieName){
        if(request.getCookies()==null){
            return null;
        }
        //request.getCookies() -> Cookie[]배열 반환함
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny() //뭐라도 찾으면 보내고
                .orElse(null); //못찾으면 null 보냄
    }
}
