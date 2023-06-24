package com.poolaeem.poolaeem.security.oauth2.service;

import com.poolaeem.poolaeem.common.exception.sign.UserNotSignedUpException;
import com.poolaeem.poolaeem.security.oauth2.model.GoogleUser;
import com.poolaeem.poolaeem.security.oauth2.model.ProviderUser;
import com.poolaeem.poolaeem.user.application.SignService;
import com.poolaeem.poolaeem.user.domain.entity.User;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public abstract class AbstractOAuth2UserService {

//    private final SignService signService;
//
//    protected AbstractOAuth2UserService(SignService signService) {
//        this.signService = signService;
//    }
//
//    @Transactional
//    public void isUserSignedUpByOAuth2(ProviderUser providerUser) {
//        Optional<User> optional = signService.findOauth2SignedUpUser(providerUser);
//        if (optional.isEmpty()) {
////            throw new UserNotSignedUpException();
//        }
//    }

    protected ProviderUser providerUser(ClientRegistration clientRegistration, OAuth2User oAuth2User) {
        String registrationId = clientRegistration.getRegistrationId();

        if (registrationId.equals("google")) {
            return new GoogleUser(oAuth2User, clientRegistration);
        }

        return null;
    }
}
