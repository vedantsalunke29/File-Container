package com.banking.authservice.service.implementation;

import com.banking.authservice.dto.AuthResponseDto;
import com.banking.authservice.dto.LoginDto;
import com.banking.authservice.dto.RegisterDto;
import com.banking.authservice.exception.AuthenticationException;
import com.banking.authservice.exception.RegistrationException;
import com.banking.authservice.exception.UserAlreadyExistsException;
import com.banking.authservice.model.User;
import com.banking.authservice.repository.UserRepository;
import com.banking.authservice.service.AuthService;
import com.banking.authservice.utils.JwtGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImplementation implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;

    @Override
    @Transactional
    public AuthResponseDto login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);

            return new AuthResponseDto(token);
        } catch (Exception e) {
            log.error("Error during login for user {}: {}", loginDto.getUsername(), e.getMessage());
            throw new AuthenticationException("Invalid username or password");
        }
    }

    @Override
    @Transactional
    public void register(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new UserAlreadyExistsException("Username is already taken");
        }

        try {
            User user = new User();
            user.setUsername(registerDto.getUsername());
            user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

            userRepository.save(user);

            log.info("Successfully registered new user: {}", registerDto.getUsername());
        } catch (Exception e) {
            log.error("Error during registration for user {}: {}", registerDto.getUsername(), e.getMessage());
            throw new RegistrationException("Error occurred during registration");
        }
    }
}
