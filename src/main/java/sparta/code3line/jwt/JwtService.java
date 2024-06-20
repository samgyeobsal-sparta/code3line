package sparta.code3line.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import sparta.code3line.common.exception.CustomException;
import sparta.code3line.common.exception.ErrorCode;
import sparta.code3line.domain.user.entity.User;
import sparta.code3line.domain.user.repository.UserRepository;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j(topic = "JwtService")
@Component
public class JwtService {

    private UserRepository userRepository;

    // 환경변수에서 JWT 시크릿키 가져오기~
    @Value("${jwt.key}")
    private String secret;

    // 엑세스 토큰 만료시간
    @Value("${jwt.access-expire-time}")
    private long accessExpireTime;

    // 리프레쉬 토큰 만료시간
    @Value("${jwt.refresh-expire-time}")
    private long refreshExpireTime;

    private SecretKey secretKey;

    public static final String BEARER_PREFIX = "Bearer ";

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 엑세스 토큰 만료시 토큰 재발급
    public String regenerateAccessToken(String refreshToken) {
        log.info("regenerateAccessToken 메서드 실행");

        if (isValidToken(refreshToken)) {
            log.error("리프레쉬 토큰 유효하지 않음.");
            return null;
        }

        if (!isTokenExpired(refreshToken)) {
            log.error("리프레쉬 토큰 만료되었음.");
            return null;
        }

        String username = extractUsername(refreshToken);
        return generateAccessToken(username);
    }

    // 토큰 디코딩
    @PostConstruct
    public void init() {
        log.info("토큰 디코딩 실행");
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // 엑세스 토큰
    public String generateAccessToken(String username) {
        log.info("generateAccessToken 메서드 실행");
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, accessExpireTime);
    }

    // 리프레쉬 토큰
    public String generateRefreshToken(String username) {
        log.info("generateRefreshToken 메서드 실행");
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, refreshExpireTime);
    }

    // 토큰 생성
    private String createToken(Map<String, Object> claims, String subject, long expirationTime) {
        log.info("createToken 메서드 실행");
        return BEARER_PREFIX +
                Jwts.builder()
                        .setClaims(claims)
                        .setSubject(subject)
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                        .signWith(secretKey, signatureAlgorithm)
                        .compact();
    }

    // 토큰 유효성 검사.
    public boolean isValidToken(String token) {
        log.info("isValidToken 메서드 실행");
        if (!StringUtils.hasText(token)) {
            log.info("토큰 없음 : 회원가입이나 로그인 기능은 무시해도 좋음.");
            return false;
        }
        try {
            // 토큰에서 bearer 제거
            token = getToken(token);
            // 토큰에서 사용자 이름 추출
            String username = extractUsername(token);
            // 토큰이 만료되지 않았다면 유효한 토큰임
            return (username != null && !isTokenExpired(token));
        } catch (Exception e) {
            log.error("토큰 유효성 검사 실패 : TokenExpired");
            return false;
        }
    }

    // 토큰 만료 확인
    public Boolean isTokenExpired(String token) {
        log.info("isTokenExpired 메서드 실행");
        return extractExpiration(token).before(new Date());
    }

    // 토큰 만료시간 추출
    public Date extractExpiration(String token) {
        log.info("extractExpiration 메서드 실행");
        return extractClaim(token, Claims::getExpiration);
    }

    // 토큰에서 사용자 주체를 추출하는 메서드
    public String extractUsername(String token) {
        log.info("extractUsername 메서드 실행");
        return extractClaim(token, Claims::getSubject);
    }

    // 토큰에서 특정 클레임을 추출하는 메서드
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        log.info("extractClaim 메서드 실행");
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 토큰에서 모든 클레임 추출
    private Claims extractAllClaims(String token) {
        log.info("extractAllClaims 메서드 실행");
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims getClaims(String token) {
        log.info("getClaims 메서드 실행");
        return extractAllClaims(token);
    }

    // 헤더에서 Jwt 가져오기.
    public String getToken(String token) {
        log.info("getToken 메서드 실행");
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            log.info("헤더에서 토큰 추출 성공");
            return token.substring(7);
        }
        log.error("헤더에서 토큰 추출 실패");
        throw new CustomException(ErrorCode.TOKEN_INVALID);
    }

    // 헤더에 담긴 토큰에서 유저정보 빼오는 메서드.
    public User getUserFromRequest(HttpServletRequest servletRequest) {
        log.info("getUserFromRequest 메서드 실행.");
        // 헤더에서 토큰 빼오기
        String token = servletRequest.getHeader(JwtService.AUTHORIZATION_HEADER);
        if (token == null) {
            log.error("토큰 없음");
            throw new CustomException(ErrorCode.TOKEN_INVALID);
        }

        // 토큰에서 Bearer 제거하고 남은 토큰값만 token 에 저장.
        token = getToken(token);

        // 빼온 토큰에서 username 빼오기
        String username = extractUsername(token);

        // userRepository 에서 username 으로 해당 유저 찾기.
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USERNAME_NOT_FOUND));
    }
}
