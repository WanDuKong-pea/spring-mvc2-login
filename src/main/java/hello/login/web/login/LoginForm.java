package hello.login.web.login;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * loginForm 에서 넘어오는 모델을 위해 사용될 객체
 */
@Data
public class LoginForm {
    @NotEmpty
    private String loginId;

    @NotEmpty
    private  String password;
}
