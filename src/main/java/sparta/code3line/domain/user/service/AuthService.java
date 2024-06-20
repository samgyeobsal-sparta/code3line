package sparta.code3line.domain.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sparta.code3line.common.exception.CustomException;
import sparta.code3line.common.exception.ErrorCode;
import sparta.code3line.domain.user.dto.LoginRequestDto;
import sparta.code3line.domain.user.dto.LoginResponseDto;
import sparta.code3line.domain.user.entity.Token;
import sparta.code3line.domain.user.entity.User;
import sparta.code3line.domain.user.repository.TokenRepository;
import sparta.code3line.domain.user.repository.UserRepository;
import sparta.code3line.jwt.JwtService;
import sparta.code3line.security.UserPrincipal;

import java.io.IOException;

@Slf4j(topic = "Login Process")
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public LoginResponseDto login(LoginRequestDto requestDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getUsername(),
                        requestDto.getPassword(),
                        null
                )
        );

        User user = ((UserPrincipal)authentication.getPrincipal()).getUser();

        /* jwtService.createToken */
        String accessJwt = jwtService.generateAccessToken(user.getUsername());
        String refreshJwt = jwtService.generateRefreshToken(user.getUsername());

        tokenRepository.save(createToken(user, refreshJwt, "Refresh"));

        return new LoginResponseDto(accessJwt, refreshJwt);
    }

    public Void logout(UserPrincipal principal) {
        Token token = getToken(principal.getUser());
        token.updateToken(null);
        tokenRepository.save(token);
        return null;
    }

    private Token createToken(User user, String token, String type) {
        return Token.builder()
                .user(user)
                .token(token)
                .tokenType(type)
                .build();
    }

    private Token getToken(User user) {
        return tokenRepository.findByUserId(user.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND)
        );
    }
}
