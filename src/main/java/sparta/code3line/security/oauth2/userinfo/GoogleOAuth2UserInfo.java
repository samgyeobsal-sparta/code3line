package sparta.code3line.security.oauth2.userinfo;

import java.util.Map;

public class GoogleOAuth2UserInfo implements OAuth2UserInfo {

    @Override
    public boolean supports(String socialId) {
        return "google".equals(socialId);
    }

    @Override
    public String getEmailFromAttributes(Map<String, Object> attributes) {
        return (String) attributes.get("email");
    }

    @Override
    public String getNameFromAttributes(Map<String, Object> attributes) {
        String name = (String) attributes.get("name");
        if (name == null) {
            return "Unknown";
        }
        return name;
    }
}
