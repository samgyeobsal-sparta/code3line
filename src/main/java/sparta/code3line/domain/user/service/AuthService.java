package sparta.code3line.domain.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import sparta.code3line.common.exception.CustomException;
import sparta.code3line.common.exception.ErrorCode;
import sparta.code3line.domain.user.dto.LoginRequestDto;
import sparta.code3line.domain.user.dto.LoginResponseDto;
import sparta.code3line.domain.user.entity.Token;
import sparta.code3line.domain.user.entity.User;
import sparta.code3line.domain.user.repository.TokenRepository;
import sparta.code3line.jwt.JwtService;
import sparta.code3line.security.UserPrincipal;

@Slf4j(topic = "Login Process")
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    @Transactional
    public LoginResponseDto login(LoginRequestDto requestDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getUsername(),
                        requestDto.getPassword(),
                        null
                )
        );

        User user = ((UserPrincipal)authentication.getPrincipal()).getUser();

        String accessJwt = jwtService.generateAccessToken(user.getUsername());
        String refreshJwt = jwtService.generateRefreshToken(user.getUsername());

        if (hasToken(user)) {
            Token token = getToken(user);
            token.updateToken(refreshJwt);
        } else {
            tokenRepository.save(createToken(user, refreshJwt, "Refresh"));
        }

        return new LoginResponseDto(accessJwt, refreshJwt);
    }

    @Transactional
    public Void logout(User user) {
        Token token = getToken(user);
        token.updateToken(null);
        return null;
    }

    private Token createToken(User user, String token, String type) {
        return Token.builder()
                .user(user)
                .token(token)
                .tokenType(type)
                .build();
    }

    private boolean hasToken(User user) {
        return tokenRepository.findByUserId(user.getId()).isPresent();
    }

    private Token getToken(User user) {
        return tokenRepository.findByUserId(user.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_TOKEN)
        );
    }
}
