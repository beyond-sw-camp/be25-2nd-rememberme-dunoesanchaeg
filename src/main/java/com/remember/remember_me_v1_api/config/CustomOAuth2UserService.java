package com.remember.remember_me_v1_api.config;

import com.remember.remember_me_v1_api.dto.MemberDto;
import com.remember.remember_me_v1_api.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberMapper memberMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getName();

        // 💡 카카오 이메일 파싱 전략: JSON 계층 구조(kakao_account -> email) 접근
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        String email = null;
        if (kakaoAccount != null && kakaoAccount.containsKey("email")) {
            email = (String) kakaoAccount.get("email");
        }

        log.info("로그인 시도 - Provider: {}, ProviderId: {}, Email: {}", provider, providerId, email);

        // DB 조회 및 신규 사용자 INSERT
        final String finalEmail = email;
        memberMapper.findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> {
                    log.info("신규 사용자 가입 진행. 이메일: {}", finalEmail);
                    MemberDto newMember = MemberDto.builder()
                            .provider(provider)
                            .providerId(providerId)
                            .email(finalEmail)
                            .build();
                    memberMapper.insertMember(newMember);
                    return newMember;
                });

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName()
        );
    }
}
