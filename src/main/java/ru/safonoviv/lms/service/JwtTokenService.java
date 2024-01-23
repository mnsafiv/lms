package ru.safonoviv.lms.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.safonoviv.lms.entities.Token;
import ru.safonoviv.lms.entities.User;
import ru.safonoviv.lms.repository.TokenRepository;

import java.security.Key;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtTokenService {
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Duration expiration;

    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    @Lazy
    private TokenRepository tokenRepo;

    public String getOrGenerateToken(UserDetails userDetails) {
        Optional<User> user = userService.findByUsername(userDetails.getUsername());
        if (user.isPresent()) {
            Optional<Token> token = tokenRepo.findByUser(user.get());
            if (token.isPresent()) {
                Token tokenSave = token.get();
                Claims allClaimsFromToken;
                try {
                    allClaimsFromToken = getAllClaimsFromToken(tokenSave.getToken());
                } catch (ExpiredJwtException e) {
                    tokenSave.setToken(getToken(userDetails, user.get()));
                    tokenSave.setExpired(false);
                    tokenRepo.save(tokenSave);
                }

                return tokenSave.getToken();

            } else {
                Map<String, List<String>> claims = new HashMap<>();
                List<String> rolesList = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());
                claims.put("roles", rolesList);
                user.ifPresent(value -> claims.put("user_id", Collections.singletonList(String.valueOf(value.getId()))));

                Date issuedDate = new Date();
                Date expiredDate = new Date(issuedDate.getTime() + expiration.toMillis());
                String strToken = Jwts.builder()
                        .setClaims(claims)
                        .setSubject(userDetails.getUsername())
                        .setIssuedAt(issuedDate)
                        .setExpiration(expiredDate)
                        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                        .compact();
                Token tokenSave = Token.builder()
                        .token(strToken)
                        .user(user.get())
                        .expired(false)
                        .revoked(true)
                        .build();
                tokenRepo.save(tokenSave);

                return tokenSave.getToken();
            }
        }
        return "error";

    }

    private String getToken(UserDetails userDetails, User user) {
        Map<String, List<String>> claims = new HashMap<>();
        List<String> rolesList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", rolesList);
        claims.put("user_id", Collections.singletonList(String.valueOf(user.getId())));

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + expiration.toMillis());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserName(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public User getUser(String token) {
        ArrayList<String> strIds = (ArrayList<String>)getAllClaimsFromToken(token).computeIfAbsent("user_id", s -> Collections.EMPTY_LIST);
        if(!strIds.isEmpty()){
            return new User(Long.valueOf(strIds.get(0)));
        }
        return new User();
    }


    public List<String> getRoles(String token) {
        return (List<String>) getAllClaimsFromToken(token).computeIfAbsent("roles", s -> Collections.EMPTY_LIST);
    }

    private Claims getAllClaimsFromToken(String token) throws ExpiredJwtException {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
