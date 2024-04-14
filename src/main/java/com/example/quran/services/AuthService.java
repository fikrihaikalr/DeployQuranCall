package com.example.quran.services;

import com.example.quran.dto.LoginRequest;
import com.example.quran.dto.SignupRequest;
import com.example.quran.response.AuthenticationResponse;
import com.example.quran.jwt.JwtUtils;
import com.example.quran.model.ERole;
import com.example.quran.model.Role;
import com.example.quran.model.Users;
import com.example.quran.repository.RoleRepository;
import com.example.quran.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsersRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

//    public AuthenticationResponse loginUser(LoginRequest loginRequest) {
//
//            Authentication authentication = authenticationManager
//                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//
//            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
//
//            List<String> roles = userDetails.getAuthorities().stream()
//                    .map(item -> item.getAuthority())
//                    .collect(Collectors.toList());
//
//            return new AuthenticationResponse(userDetails.getId(),
//                    userDetails.getName(),
//                    userDetails.getEmail(),
//                    roles,
//                    jwtCookie.getValue());
//    }
public AuthenticationResponse loginUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new AuthenticationResponse(userDetails.getId(),
                userDetails.getName(),
                userDetails.getEmail(),
                roles,
                jwtCookie.getValue());

}

    public void registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException("Error: Email is already in use!");
        }

        Users user = new Users(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getNumberPhone(),
                signUpRequest.getStatus());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_STUDENTS)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "teacher":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_TEACHER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_STUDENTS)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
    }
}
