package sparta.code3line.security.oauth2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import sparta.code3line.config.PasswordEncorderConfig;
import sparta.code3line.domain.user.entity.User;
import sparta.code3line.domain.user.repository.UserRepository;
import sparta.code3line.security.UserPrincipal;
import sparta.code3line.security.oauth2.userinfo.GoogleOAuth2UserInfo;
import sparta.code3line.security.oauth2.userinfo.KakaoOAuth2UserInfo;
import sparta.code3line.security.oauth2.userinfo.NaverOAuth2UserInfo;
import sparta.code3line.security.oauth2.userinfo.OAuth2UserInfo;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class Oauth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncorderConfig encoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}", oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo userInfo;

        if ("google".equals(provider)) {
            userInfo = new GoogleOAuth2UserInfo();
        } else if ("naver".equals(provider)) {
            userInfo = new NaverOAuth2UserInfo();
        } else if ("kakao".equals(provider)) {
            userInfo = new KakaoOAuth2UserInfo();
        } else {
            throw new OAuth2AuthenticationException("Unsupported provider: " + provider);
        }

        String socialId = oAuth2User.getAttribute("sub");
        Optional<User> optionalUser = userRepository.findBySocialId(socialId);
        User user;

        if (optionalUser.isEmpty()) {
            user = User.builder()
                    .username(provider + "_" + socialId)
                    .password(encoder.passwordEncoder().encode("default_password"))
                    .email(userInfo.getEmailFromAttributes(oAuth2User.getAttributes()))
                    .nickname(userInfo.getNameFromAttributes(oAuth2User.getAttributes()))
                    .socialId(socialId)
                    .role(User.Role.NORMAL)
                    .status(User.Status.ACTIVE)
                    .build();
            userRepository.save(user);
        } else {
            user = optionalUser.get();
        }

        return new UserPrincipal(user, oAuth2User.getAttributes());
    }
}
