package com.example.navire.services;

import com.example.navire.dto.LoginDTO;
import com.example.navire.exception.UserNotFoundException;
import com.example.navire.mapper.UserMapper;
import com.example.navire.model.User;
import com.example.navire.repository.UserRepository;
import com.example.navire.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toDTO(user);
    }

    @Transactional
    public LoginDTO register(String mail, String password) {
        if (userRepository.existsByMail(mail)) {
            throw new IllegalArgumentException("Mail already exists");
        }
        User user = new User();
        user.setMail(mail);
        user.setPassword(passwordEncoder.encode(password));
        return userMapper.toDTO(userRepository.save(user));
    }

    public String login(String mail, String password) {
        Optional<User> userOpt = userRepository.findByMail(mail);
        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return jwtUtil.generateToken(mail);
    }
}
