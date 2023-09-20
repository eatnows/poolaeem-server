package com.poolaeem.poolaeem.user.presentation;

import com.poolaeem.poolaeem.common.annotation.LoggedInUser;
import com.poolaeem.poolaeem.common.annotation.LoggedInUserOnly;
import com.poolaeem.poolaeem.common.jwt.HeaderTokenExtractor;
import com.poolaeem.poolaeem.common.response.ApiResponseDto;
import com.poolaeem.poolaeem.security.oauth2.handler.LoginSuccessToken;
import com.poolaeem.poolaeem.security.oauth2.model.GenerateTokenUser;
import com.poolaeem.poolaeem.user.application.SignService;
import com.poolaeem.poolaeem.user.domain.entity.User;
import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import com.poolaeem.poolaeem.user.presentation.dto.auth.SignRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
public class SignController {

    private final SignService signService;
    private final LoginSuccessToken loginSuccessToken;

    public SignController(SignService signService, LoginSuccessToken loginSuccessToken) {
        this.signService = signService;
        this.loginSuccessToken = loginSuccessToken;
    }

    @PostMapping("/api/signup/terms")
    public void signUp(HttpServletRequest request,
                       HttpServletResponse response,
                       @Valid @RequestBody SignRequest.SignUpTermsDto dto) {
        User user = signService.signUpOAuth2User(request, dto.getOauthProvider(), dto.getOauthId(), dto.getEmail());
        loginSuccessToken.addTokenInResponse(response, new GenerateTokenUser(user.getId()));
    }

    @PostMapping("/api/access-token/refresh")
    public ApiResponseDto<?> generateAccessTokenByRefreshToken(HttpServletResponse response,
                                                               @RequestHeader HttpHeaders headers) {
        String bearerRefreshToken = headers.getOrEmpty("Refresh")
                .stream().findFirst().orElse("");

        String refreshToken = HeaderTokenExtractor.extract(bearerRefreshToken);

        String accessToken = signService.generateAccessTokenByRefreshToken(refreshToken);
        loginSuccessToken.addOnlyAccessTokenInResponse(response, accessToken);

        return ApiResponseDto.OK();
    }

    @LoggedInUserOnly
    @DeleteMapping("/api/users/{userId}")
    public ApiResponseDto<?> deleteUser(@LoggedInUser UserVo user,
                                        @PathVariable String userId) {
        signService.deleteUser(user.getId(), userId);
        return ApiResponseDto.OK();
    }

    @LoggedInUserOnly
    @PostMapping("/api/sign-out")
    public ApiResponseDto<?> signOut(@LoggedInUser UserVo user) {
        signService.signOut(user.getId());
        return ApiResponseDto.OK();
    }
}
