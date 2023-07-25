package com.poolaeem.poolaeem.user.domain.service.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.poolaeem.poolaeem.common.event.FileEventsPublisher;
import com.poolaeem.poolaeem.common.event.obj.EventsPublisherFileEvent;
import com.poolaeem.poolaeem.common.exception.request.BadRequestDataException;
import com.poolaeem.poolaeem.common.exception.auth.DuplicateSignUpException;
import com.poolaeem.poolaeem.common.exception.auth.UserNotSignedUpException;
import com.poolaeem.poolaeem.common.exception.request.ForbiddenRequestException;
import com.poolaeem.poolaeem.common.exception.user.UserNotFoundException;
import com.poolaeem.poolaeem.common.file.FilePath;
import com.poolaeem.poolaeem.common.jwt.JwtTokenUtil;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;
import com.poolaeem.poolaeem.security.oauth2.model.GenerateTokenUser;
import com.poolaeem.poolaeem.security.oauth2.model.GoogleUser;
import com.poolaeem.poolaeem.security.oauth2.model.ProviderUser;
import com.poolaeem.poolaeem.user.application.SignService;
import com.poolaeem.poolaeem.user.domain.entity.OauthProvider;
import com.poolaeem.poolaeem.user.domain.entity.TermsVersion;
import com.poolaeem.poolaeem.user.domain.entity.User;
import com.poolaeem.poolaeem.user.infra.repository.UserRepository;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
public class SignServiceImpl implements SignService {

    private final UserRepository userRepository;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final SignUpOAuth2UserService signUpOAuth2UserService;
    private final JwtTokenUtil jwtTokenUtil;
    private final FileEventsPublisher fileEventsPublisher;

    public SignServiceImpl(UserRepository userRepository, OAuth2AuthorizedClientService oAuth2AuthorizedClientService, SignUpOAuth2UserService signUpOAuth2UserService, JwtTokenUtil jwtTokenUtil, FileEventsPublisher fileEventsPublisher) {
        this.userRepository = userRepository;
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
        this.signUpOAuth2UserService = signUpOAuth2UserService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.fileEventsPublisher = fileEventsPublisher;
    }

    @Transactional
    @Override
    public User signUpOAuth2User(OauthProvider oauthProvider, String oauthId, String email) {
        OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(oauthProvider.getId(), oauthId);
        OAuth2AccessToken accessToken = client.getAccessToken();

        if (isExpired(Objects.requireNonNull(accessToken.getExpiresAt()))) {
            throw new UserNotSignedUpException(ApiResponseCode.USER_NOT_SIGNED_UP.getMessage());
        }

        OAuth2User oAuth2User = signUpOAuth2UserService.loadUser(new OAuth2UserRequest(client.getClientRegistration(), accessToken));
        ProviderUser providerUser = getProviderUser(oAuth2User, client.getClientRegistration());
        matchRequestEmailAndUserEmail(email, providerUser.getEmail());

        return saveUser(providerUser);
    }

    @Override
    public String generateAccessTokenByRefreshToken(String refreshToken) {
        DecodedJWT decodedJWT = jwtTokenUtil.validRefreshToken(refreshToken);
        String userId = decodedJWT.getClaim("code").asString();
        GenerateTokenUser generateTokenUser = new GenerateTokenUser(userId);
        return jwtTokenUtil.generateAccessToken(generateTokenUser);
    }

    @Transactional
    @Override
    public void deleteUser(String tokenUserId, String pathUserId) {
        validUserId(tokenUserId, pathUserId);

        User user = userRepository.findByIdAndIsDeletedFalse(tokenUserId)
                .orElseThrow(UserNotFoundException::new);
        user.delete();
        deleteUserProfileImage(user.getProfileImage());
    }

    private void deleteUserProfileImage(String profileImage) {
        if (!StringUtils.hasText(profileImage)) return;

        fileEventsPublisher.publish(new EventsPublisherFileEvent.FileDeleteEvent(profileImage, FilePath.PROFILE_IMAGE));
    }

    private void validUserId(String tokenUserId, String pathUserId) {
        if (!tokenUserId.equals(pathUserId)) {
            throw new ForbiddenRequestException();
        }
    }

    private void matchRequestEmailAndUserEmail(String requestEmail, String userEmail) {
        if (!requestEmail.equals(userEmail)) {
            throw new BadRequestDataException();
        }
    }
    
    private boolean isExpired(Instant tokenExpiredAt) {
        return tokenExpiredAt.isBefore(Instant.now());
    }

    private User saveUser(ProviderUser providerUser) {
        Optional<User> optional = userRepository.findByOauthProviderAndOauthIdAndIsDeletedFalse(
                OauthProvider.valueOf(providerUser.getProvider().toUpperCase()),
                providerUser.getId());

        if (optional.isPresent()) {
            throw new DuplicateSignUpException(ApiResponseCode.DUPLICATE_SIGNED_UP.getMessage());
        }

        return userRepository.save(new User(providerUser.getEmail(),
                providerUser.getUsername(),
                OauthProvider.valueOf(providerUser.getProvider().toUpperCase()),
                providerUser.getId(),
                null,
                TermsVersion.V1));
    }

    private ProviderUser getProviderUser(OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        OauthProvider provider = OauthProvider.valueOf(clientRegistration.getRegistrationId().toUpperCase());

        if (provider == OauthProvider.GOOGLE) {
            return new GoogleUser(oAuth2User, clientRegistration);
        }

        return new GoogleUser(oAuth2User, clientRegistration);
    }
}
