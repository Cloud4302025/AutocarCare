package com.spring.jwt.jwt.impl;

import com.spring.jwt.entity.Employee;
import com.spring.jwt.exception.BaseException;
import com.spring.jwt.jwt.JwtConfig;
import com.spring.jwt.jwt.JwtService;
import com.spring.jwt.repository.EmployeeRepository;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.service.security.UserDetailsCustom;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    private final UserRepository userRepository;

    private final EmployeeRepository employeeRepository;

    private final JwtConfig jwtConfig;

    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtServiceImpl(@Lazy JwtConfig jwtConfig, UserDetailsService userDetailsService,
                          UserRepository userRepository, EmployeeRepository employeeRepository) {
        this.jwtConfig = jwtConfig;
        this.userDetailsService = userDetailsService;

        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }


    @Override
    public Claims extractClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public Key getKey() {
        byte[] key = Decoders.BASE64.decode(jwtConfig.getSecret());
        return Keys.hmacShaKeyFor(key);
    }


    @Override
    public String generateToken(UserDetailsCustom userDetailsCustom) {
        Instant now = Instant.now();

        List<String> roles = userDetailsCustom.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        log.info("Roles: {}", roles);

        Integer userId   = userDetailsCustom.getUserId();
        String  firstName = userDetailsCustom.getFirstName();

        if (roles.contains("USER")) {
            userId = userDetailsCustom.getUserId();

        }
        if (roles.contains("ADMIN")) {
            userId = userDetailsCustom.getUserId();
        }
        List<String> componentNames = null;
        if (roles.contains("EMPLOYEE")) {
            Employee employee = employeeRepository.findByUser_Id(userId)
                    .orElseThrow(() -> new BaseException(
                            String.valueOf(HttpStatus.NOT_FOUND.value()),
                            "Employee not found for userId: "
                    ));
            componentNames = employee.getComponentNames();
        }
        log.info("firstName: {}", firstName);
        log.info("userId: {}", userId);

        return Jwts.builder()
                .setSubject(userDetailsCustom.getUsername())
                .claim("firstname", firstName)
                .claim("userId", userId)
                .claim("componentNames", componentNames)
                .claim("authorities", roles)
                .claim("roles", roles)
                .claim("isEnable", userDetailsCustom.isEnabled())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(jwtConfig.getExpiration())))
                .signWith(getKey(), SignatureAlgorithm.HS256)

                .compact();
    }
    @Override
    public boolean isValidToken(String token) {
        final String username = extractUsername(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return !ObjectUtils.isEmpty(userDetails);
    }

    private String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    private Claims extractAllClaims(String token){
        Claims claims = null;

        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (ExpiredJwtException e){
            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()), "Token expiration");
        }catch (UnsupportedJwtException e){
            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()), "Token's not supported");
        }catch (MalformedJwtException e){
            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()), "Invalid format 3 part of token");
        }catch (SignatureException e){
            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()), "Invalid format token");
        }catch (Exception e){
            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()), e.getLocalizedMessage());
        }

        return claims;
    }

}


