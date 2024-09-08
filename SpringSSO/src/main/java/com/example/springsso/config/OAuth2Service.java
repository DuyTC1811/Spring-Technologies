package com.example.springsso.config;

import com.example.springsso.entitys.UserEntity;
import com.example.springsso.repositorys.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2Service extends DefaultOAuth2UserService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if ("FACEBOOK".equalsIgnoreCase(registrationId)) {
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(registrationId);
            userEntity.setEmail(oAuth2User.getAttribute("email"));
            userEntity.setPassword(oAuth2User.getAttribute("password"));
            userRepository.saveAndFlush(userEntity);
        }

        System.out.println(oAuth2User);
        return oAuth2User;
    }
}
