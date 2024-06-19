package sparta.code3line.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sparta.code3line.domain.user.dto.LoginRequestDto;
import sparta.code3line.domain.user.dto.LoginResponseDto;
import sparta.code3line.domain.user.entity.Token;
import sparta.code3line.domain.user.entity.User;
import sparta.code3line.domain.user.repository.TokenRepository;
import sparta.code3line.domain.user.repository.UserRepository;
import sparta.code3line.jwt.JwtService;
import sparta.code3line.security.UserPrincipal;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public LoginResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("아이디를 다시 확인해주세요")
        );

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);


        /* jwtService.createToken */
        String accessJwt = "access_token";
        String refreshJwt = "refresh_token";

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
                ()->new UsernameNotFoundException("Valid Token not found")
        );
    }
}
