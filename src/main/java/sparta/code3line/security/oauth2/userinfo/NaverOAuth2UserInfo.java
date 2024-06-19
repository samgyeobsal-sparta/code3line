package sparta.code3line.security.oauth2.userinfo;

import java.util.Map;

public class NaverOAuth2UserInfo implements OAuth2UserInfo {

    @Override
    public boolean supports(String socialId) {
        return "naver".equals(socialId);
    }

    @Override
    public String getEmailFromAttributes(Map<String, Object> attributes) {
        return (String) attributes.get("email");
    }

    @Override
    public String getNameFromAttributes(Map<String, Object> attributes) {

        return (String) attributes.get("name");
    }
}