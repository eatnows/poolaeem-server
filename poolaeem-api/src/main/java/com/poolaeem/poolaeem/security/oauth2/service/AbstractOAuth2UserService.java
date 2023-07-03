package com.poolaeem.poolaeem.security.oauth2.service;

import com.poolaeem.poolaeem.security.oauth2.model.GoogleUser;
import com.poolaeem.poolaeem.security.oauth2.model.ProviderUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public abstract class AbstractOAuth2UserService {

    protected ProviderUser providerUser(ClientRegistration clientRegistration, OAuth2User oAuth2User) {
        String registrationId = clientRegistration.getRegistrationId();

        if (registrationId.equals("google")) {
            return new GoogleUser(oAuth2User, clientRegistration);
        }

        return null;
    }
}
