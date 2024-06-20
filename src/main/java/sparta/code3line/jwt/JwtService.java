package sparta.code3line.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {

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

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // 엑세스 토큰
    public String generateAccessToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, accessExpireTime);
    }

    // 리프레쉬 토큰
    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, refreshExpireTime);
    }

    // 토큰 생성
    private String createToken(Map<String, Object> claims, String subject, long expirationTime) {
        return BEARER_PREFIX +
                Jwts.builder()
                        .setClaims(claims)
                        .setSubject(subject)
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                        .signWith(SignatureAlgorithm.HS256, secret)
                        .compact();
    }

    // 토큰 유효성 검사.
    public boolean isValidToken(String token) {
        if(!StringUtils.hasText(token)) {
            return false;
        }
        // 토큰에서 사용자 이름 추출
        String username = extractUsername(token);
        // 토큰이 만료되지 않았다면 유효한 토큰임
        return (username != null && !isTokenExpired(token));
    }

    // 토큰 만료 확인
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 토큰 만료시간 추출
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 토큰에서 사용자 주체를 추출하는 메서드
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 토큰에서 특정 클레임을 추출하는 메서드
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 토큰에서 모든 클레임 추출
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims getClaims(String token) {
        return extractAllClaims(token);
    }
}