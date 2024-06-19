package sparta.code3line.security.oauth2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import sparta.code3line.domain.user.repository.UserRepository;
import sparta.code3line.domain.user.service.UserService;
import sparta.code3line.security.oauth2.userinfo.OAuth2UserInfo;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final List<OAuth2UserInfo> oAuth2UserInfoList;
    private final BCryptPasswordEncoder encoder;
    private final UserService userService;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    }
}