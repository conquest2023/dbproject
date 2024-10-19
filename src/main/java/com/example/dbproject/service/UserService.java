package com.example.dbproject.service;

import com.example.dbproject.dto.SignUpUser;
import com.example.dbproject.entity.User;
import com.example.dbproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private  final UserRepository userRepository;

    private  final PasswordEncoder passwordEncoder;

  //  private  final BCryptPasswordEncoder passwordEncoder;


    public User createUser(SignUpUser signUpUser) {
        User user = new User();
        user.setUsername(signUpUser.getUsername());
        user.setPassword(passwordEncoder.encode(signUpUser.getPassword()));
        user.setEmail(signUpUser.getEmail());
        user.setLastLogin(LocalDateTime.now());
        return userRepository.save(user);
    }


    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public List<User> getUsers(){

        return userRepository.findAll();
    }

}
