package sparta.code3line.security.oauth2.userinfo;

import java.util.Map;

public class KakaoOAuth2UserInfo implements OAuth2UserInfo{

    @Override
    public boolean supports(String socialId) {
        return "kakao".equals(socialId);
    }

    @Override
    public String getEmailFromAttributes(Map<String, Object> attributes) {
        return (String)((Map) attributes.get("kakao_account")).get("email");
    }

    @Override
    public String getNameFromAttributes(Map<String, Object> attributes) {
        return (String)((Map) attributes.get("properties")).get("nickname");
    }
}