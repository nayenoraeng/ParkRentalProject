package com.project.parkrental.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<?> registerNewUser (User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("이미 동일한 이름의 사용자가 있습니다. 로그인 해 주세요");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("이미 동일한 이메일의 사용자가 존재합니다. 로그인 해 주세요.");
        }
        if (userRepository.existsByPhoneNum(user.getPhoneNum())) {
            throw new RuntimeException("이미 동일한 전화번호의 사용자가 존재합니다. 로그인 해 주세요.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAuthority("ROLE_USER");
        System.out.println("save method called");
        userRepository.save(user);
        return ResponseEntity.ok("성공적으로 회원가입 되셨습니다.");
    }

    public boolean isUsernameTaken (String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }

    public  boolean isPhoneNumTaken(String phoneNum) {
        return userRepository.existsByPhoneNum(phoneNum);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));
    }
}
