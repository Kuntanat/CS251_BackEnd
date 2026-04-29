package com.cs251.backend.service;

import com.cs251.backend.dto.request.LoginRequest;
import com.cs251.backend.dto.request.RegisterRequest;
import com.cs251.backend.dto.response.AuthResponse;
import com.cs251.backend.entity.Account;
import com.cs251.backend.repository.AccountRepository;
import com.cs251.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    /** ไม่รองรับ — ใช้ /api/donors/register หรือ /api/employees/register แทน */
    public AuthResponse register(RegisterRequest request) {
        throw new IllegalArgumentException(
                "กรุณาใช้ /api/donors/register หรือ /api/employees/register แทน");
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        Account account = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(account.getUsername());
        String token = jwtTokenProvider.generateToken(userDetails);

        log.info("User logged in: {}", account.getUsername());
        return buildAuthResponse(token, account);
    }

    public AuthResponse.UserInfo getMe(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return buildUserInfo(account);
    }

    private AuthResponse buildAuthResponse(String token, Account account) {
        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getExpirationTime())
                .user(buildUserInfo(account))
                .build();
    }

    private AuthResponse.UserInfo buildUserInfo(Account account) {
        return AuthResponse.UserInfo.builder()
                .id((long) account.getReferenceId())
                .username(account.getUsername())
                .email(null)
                .fullName(null)
                .role(account.getUserType())
                .build();
    }
}
