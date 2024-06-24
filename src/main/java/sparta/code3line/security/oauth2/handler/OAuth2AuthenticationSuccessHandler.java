package sparta.code3line.security.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import sparta.code3line.domain.user.dto.LoginResponseDto;
import sparta.code3line.domain.user.entity.User;
import sparta.code3line.jwt.JwtService;
import sparta.code3line.security.UserPrincipal;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();

        String accessJwt = jwtService.generateAccessToken(user.getUsername());
        String refreshJwt = jwtService.generateRefreshToken(user.getUsername());

        LoginResponseDto loginResponseDto = new LoginResponseDto(accessJwt, refreshJwt);

        response.setHeader("Authorization", accessJwt);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(loginResponseDto));

    }

}
