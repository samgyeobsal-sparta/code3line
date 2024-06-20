package sparta.code3line.security.oauth2.userinfo;

import java.util.Map;

public interface OAuth2UserInfo {
    boolean supports(String providerId);
    String getEmailFromAttributes(Map<String, Object> attributes);
    String getNameFromAttributes(Map<String, Object> attributes);
}
