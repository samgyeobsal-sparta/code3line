package sparta.code3line.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import sparta.code3line.domain.user.repository.UserRepository;

import java.security.Key;
import java.util.Date;
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

    private Key key;

    public static final String BEARER_PREFIX = "Bearer ";

    // 권한 부여를 위한 AUTHORIZATION_KEY 설정
    public static final String AUTHORIZATION_KEY = "role";

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 엑세스 토큰 만료시 토큰 재발급
//    public String regenerateAccessToken(String refreshToken) {
//        log.info("regenerateAccessToken 메서드 실행");
//
//        if (isValidToken(refreshToken)) {
//            log.error("리프레쉬 토큰 유효하지 않음.");
//            return null;
//        }
//
//        String username = extractUsername(refreshToken);
//        return generateAccessToken(username);
//    }

    // 토큰 디코딩
    @PostConstruct
    public void init() {
        log.info("토큰 디코딩 실행");
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 엑세스 토큰
    // TODO : 유저 권한 기능 추가시 role 추가하여 권한도 토큰에 담기
    public String generateAccessToken(String username) {
        log.info("generateAccessToken 메서드 실행");
        return createToken(username, accessExpireTime);
    }

    // 리프레쉬 토큰
    // TODO : 유저 권한 기능 추가시 role 추가하여 권한도 토큰에 담기
    public String generateRefreshToken(String username) {
        log.info("generateRefreshToken 메서드 실행");
        return createToken(username, refreshExpireTime);
    }

    // 토큰 생성
    private String createToken(String username, Long expirationTime) {
        log.info("createToken 메서드 실행");
        Date now = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
//                        .claim(AUTHORIZATION_KEY, role)  // 유저 권한 추가시 주석제거 후 사용
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + expirationTime))
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    // 토큰 검증
    public boolean isValidToken(String token) {
        log.info("isValidToken 메서드 실행");
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 에러 메시지 가져오기
    public String getErrorMessage(String token) {
        String okToken = getToken(token);
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(okToken);
            return "정상 JWT token 입니다.";
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            return "Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.";
        } catch (ExpiredJwtException e) {
            return "Expired JWT token, 만료된 JWT token 입니다.";
        } catch (UnsupportedJwtException e) {
            return "Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.";
        } catch (IllegalArgumentException e) {
            return "JWT claims is empty, 잘못된 JWT 토큰 입니다.";
        }
    }

    // 토큰 만료 확인
    // TODO : 없어도 됨
//    public Boolean isTokenExpired(String token) {
//        log.info("isTokenExpired 메서드 실행");
//        return extractExpiration(token).before(new Date());
//    }

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
        final Claims claims = getClaims(token);
        return claimsResolver.apply(claims);
    }

    // 토큰에서 모든 클레임 추출
    public Claims getClaims(String token) {
        log.info("extractAllClaims 메서드 실행");
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(getToken(token))
                .getBody();
    }

//    public Claims getClaims(String token) {
//        log.info("getClaims 메서드 실행");
//        return extractAllClaims(token);
//    }

    // 토큰 가져오기
    public String getToken(String token) {
        log.info("getToken 메서드 실행");
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            log.info("헤더에서 토큰 추출 성공");
            return token.replace(BEARER_PREFIX, "");
        }
        log.error("헤더에서 토큰 추출 실패");
        return null;
    }
}
