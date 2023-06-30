package com.poolaeem.poolaeem.user.presentation;

import com.poolaeem.poolaeem.security.oauth2.handler.LoginSuccessToken;
import com.poolaeem.poolaeem.security.oauth2.model.GenerateTokenUser;
import com.poolaeem.poolaeem.user.application.SignService;
import com.poolaeem.poolaeem.user.domain.entity.User;
import com.poolaeem.poolaeem.user.presentation.dto.auth.SignRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SignController {

    private final SignService signService;
    private final LoginSuccessToken loginSuccessToken;

    public SignController(SignService signService, LoginSuccessToken loginSuccessToken) {
        this.signService = signService;
        this.loginSuccessToken = loginSuccessToken;
    }

    @PostMapping("/signup/terms")
    public void signUp(HttpServletResponse response,
                       @Valid @RequestBody SignRequest.SignUpTermsDto dto) {

        User user = signService.signUpOAuth2User(dto.getOauthProvider(), dto.getOauthId(), dto.getEmail());
        loginSuccessToken.addTokenInResponse(
                response,
                new GenerateTokenUser(user.getId(), user.getEmail(), user.getName(), null)
        );
    }
}
