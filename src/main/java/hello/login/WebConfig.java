package hello.login;

import hello.login.web.argumentresolver.LoginMemberArgumentResolver;
import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

//@Configuration: 수동으로 스프링 컨테이너에 빈을 등록하는 설정 클래스 애노테이션 + @Bean
//@Component : 빈 자동 등록 애노테이션

@Configuration
public class WebConfig implements WebMvcConfigurer { //implements WebMvcConfigurer: 인터셉터 등록을 위함

    //@Bean //로그 필터 등록 빈
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

    //@Bean //로그인 필터 등록 빈
    public FilterRegistrationBean loginCheckFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;

    }

    @Override //WebMvcConfigurer 의 인터셉터 등록 메서드
    public void addInterceptors(InterceptorRegistry registry){

        //Log 인터셉터 등록
        registry.addInterceptor(new LogInterceptor()) //인터셉터 등록
                .order(1) //인터셉터 호출 순서. 낮을 수록 먼저 호출
                .addPathPatterns("/**") //인터셉터를 적용할 URL 패턴 지정
                .excludePathPatterns("/css/**","/*.ico","/error"); //인터셉터에서 제외할 패턴 지정
                //filter 와 urlPattern 다르게 사용.
                //filter "/*" -> interceptor "/**"

        //로그인 인증 체크 인테셉터 등록
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**","/*.ico","/error",
                        "/","/login","/logout","/members/add");

        //필터와 비교해보면 인터셉터는 addPathPatterns, excludePathPatterns로
        //매우 정밀하게 URL 패턴을 지정 가능
        //웬만해서 특별한 문제가 없다면 filter 보다 interceptor 사용 권장 (더 편리)
    }

    //직접 만든 LoginMemberArgumentResolver 등록
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers){
        resolvers.add(new LoginMemberArgumentResolver());
    }
}
