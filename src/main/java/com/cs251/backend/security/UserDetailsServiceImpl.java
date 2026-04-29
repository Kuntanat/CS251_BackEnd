package com.cs251.backend.security;

import com.cs251.backend.entity.Account;
import com.cs251.backend.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("[AUTH] Username not found in Account table: '{}'", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });

        // Account.Status convention: 1 = active (ปกติ), 0 = suspended (ระงับ)
        // Spring's disabled(true) blocks login; we disable when Status == 0.
        boolean isDisabled = account.getStatus() == 0;
        String role = "ROLE_" + account.getUserType().toUpperCase();

        log.debug("[AUTH] Loaded account: username='{}', userType='{}', status={}, disabled={}, role={}",
                account.getUsername(), account.getUserType(), account.getStatus(), isDisabled, role);

        return org.springframework.security.core.userdetails.User.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(role)))
                .disabled(isDisabled)
                .build();
    }
}
