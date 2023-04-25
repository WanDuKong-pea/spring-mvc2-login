package hello.login.web.argumentresolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) //파라미터에서만 사용 가능
@Retention(RetentionPolicy.RUNTIME) //reflection 등을 활용할 수 있도록 런타임까지 애노테이션 정보 남아있음
public @interface Login {
}
