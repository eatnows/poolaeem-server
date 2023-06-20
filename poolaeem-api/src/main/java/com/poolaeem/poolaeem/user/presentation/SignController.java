package com.poolaeem.poolaeem.user.presentation;

import com.poolaeem.poolaeem.security.oauth2.handler.LoginSuccessHandler;
import com.poolaeem.poolaeem.security.oauth2.handler.LoginSuccessRedirect;
import com.poolaeem.poolaeem.security.oauth2.model.GenerateTokenUser;
import com.poolaeem.poolaeem.user.application.SignService;
import com.poolaeem.poolaeem.user.domain.entity.User;
import com.poolaeem.poolaeem.user.presentation.dto.SignRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class SignController {

    private final SignService signService;

    private final LoginSuccessRedirect loginSuccessRedirect;

    public SignController(SignService signService, LoginSuccessRedirect loginSuccessRedirect) {
        this.signService = signService;
        this.loginSuccessRedirect = loginSuccessRedirect;
    }

    @PostMapping("/signup/terms")
    public void signUp(HttpServletRequest request,
                       HttpServletResponse response,
                       @Valid @RequestBody SignRequest.SignUpTermsDto dto) throws IOException {

        User user = signService.signUpOAuth2User(dto.getOauthProvider(), dto.getOauthId());
        loginSuccessRedirect.redirectSignedIn(request,
                response,
                new GenerateTokenUser(user.getId(), user.getEmail(), user.getName(), null),
                LoginSuccessHandler.DEFAULT_REDIRECT_URL);
    }
}
