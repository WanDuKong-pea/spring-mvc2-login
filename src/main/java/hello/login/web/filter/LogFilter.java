package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter { //필터를 사용하려면 필터 인터페이스 구현해야 함

    @Override //필터 초기화 메서드. 서블릿 컨테이너가 생성될 때 호출
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override //고객의 요청이 올 때 마다 해당 메서드가 호출. 필터의 로직을 구현하는 부분
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        //ServletRequest request는 HTTP 요청이 아닌 경우까지 고려
        //(HttpServletRequest)로 다운 케스팅 (ServletRequest 기능이 부족).
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        //HTTP 요청을 구분하기 위해 요청당 임의의 uuid를 생성해둠
        String uuid = UUID.randomUUID().toString();

        try {
            //uuid와 requestURI를 함께 출력
            log.info("REQUEST [{}][{}]", uuid, requestURI);
            //다음 필터가 있으면 필터를 호출하고 필터가 없으면 서블릿 호출
            //이 로직을 호출하지 않으면 다음 단계로 진행되지 않음
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e;
        } finally {
            //request.getDispatcherType() 예외 처리 페이지 요청시 WAS에서 ERROR 코드를 담아줌
            log.info("RESPONSE [{}][{}][{}]", uuid, request.getDispatcherType(), requestURI);
        }
    }

    @Override //필터 종료 메서드, 서블릿 컨테이너가 종료될 때 호출.
    public void destroy() {
        log.info("log filter destroy");
    }
}
