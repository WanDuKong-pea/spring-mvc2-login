package hello.login;

import hello.login.web.filter.LogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

//@Configuration: 수동으로 스프링 컨테이너에 빈을 등록하는 설정 클래스 애노테이션 + @Bean
//@Component : 빈 자동 등록 애노테이션

@Configuration
public class WebConfig {

    @Bean //메소드 명으로 빈 이름 결정
    public FilterRegistrationBean logFilter(){

        //스프링 부트를 이용시 FilterRegistrationBean 을 사용하여 필터를 등록하면 됨
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

        //setFilter(new LogFilter()): 등록할 필터 지정
        filterRegistrationBean.setFilter(new LogFilter());
        //필터 체인의 순서 낮을 수록 먼저 동작
        filterRegistrationBean.setOrder(1);
        //필터를 적용할 URL패턴을 지정. 한번에 여러개 가능
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}
