package com.poolaeem.poolaeem.user.domain.service;

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
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SignServiceImpl implements SignService {

    private final UserRepository userRepository;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    public SignServiceImpl(UserRepository userRepository, OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        this.userRepository = userRepository;
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
    }

    @Transactional
    @Override
    public User signUpOAuth2User(String oauthProvider, String oauthId) {
        OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(oauthProvider, oauthId);
        OAuth2AccessToken accessToken = client.getAccessToken();
        DefaultOAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(new OAuth2UserRequest(client.getClientRegistration(), accessToken));

        ProviderUser providerUser = getProviderUser(oAuth2User, client.getClientRegistration());

        return saveUser(providerUser);
    }

    private User saveUser(ProviderUser providerUser) {
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
