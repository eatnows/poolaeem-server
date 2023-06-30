package com.poolaeem.poolaeem.user.domain.service.auth;

import com.poolaeem.poolaeem.common.exception.request.BadRequestDataException;
import com.poolaeem.poolaeem.common.exception.auth.DuplicateSignUpException;
import com.poolaeem.poolaeem.common.exception.auth.UserNotSignedUpException;
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

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
public class SignServiceImpl implements SignService {

    private final UserRepository userRepository;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final SignUpOAuth2UserService signUpOAuth2UserService;

    public SignServiceImpl(UserRepository userRepository, OAuth2AuthorizedClientService oAuth2AuthorizedClientService, SignUpOAuth2UserService signUpOAuth2UserService) {
        this.userRepository = userRepository;
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
        this.signUpOAuth2UserService = signUpOAuth2UserService;
    }

    @Transactional
    @Override
    public User signUpOAuth2User(OauthProvider oauthProvider, String oauthId, String email) {
        OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(oauthProvider.getId(), oauthId);
        OAuth2AccessToken accessToken = client.getAccessToken();

        if (isExpired(Objects.requireNonNull(accessToken.getExpiresAt()))) {
            throw new UserNotSignedUpException();
        }

        OAuth2User oAuth2User = signUpOAuth2UserService.loadUser(new OAuth2UserRequest(client.getClientRegistration(), accessToken));
        ProviderUser providerUser = getProviderUser(oAuth2User, client.getClientRegistration());
        matchRequestEmailAndUserEmail(email, providerUser.getEmail());

        return saveUser(providerUser);
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
            throw new DuplicateSignUpException();
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
