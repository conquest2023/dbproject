package com.example.dbproject.controller;

import com.example.dbproject.dto.SignUpUser;
import com.example.dbproject.entity.User;
import com.example.dbproject.jwt.JwtUtil;
import com.example.dbproject.service.CustomUserDetailsService;
import com.example.dbproject.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.AuthenticationException;
import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserDetailsService userDetailsService;

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    @GetMapping("/login")
    public ResponseEntity<List<User>> createUser() {

       return  ResponseEntity.ok(userService.getUsers());
    }


    @PostMapping("/signup")
    public ResponseEntity<User> createUser(@RequestBody SignUpUser signUpUser) {

    User user=   userService.createUser(signUpUser);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
            userService.deleteUser(userId);

            return ResponseEntity.noContent().build();
    }
    @PostMapping("/login")
    public  String login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) throws AuthenticationException {

                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));

                UserDetails userDetails =userDetailsService.loadUserByUsername(username);

                String token= jwtUtil.generateToken(userDetails.getUsername());

                Cookie cookie=new Cookie("token",token);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                cookie.setMaxAge(3600);

                response.addCookie(cookie);

                return token;
    }
    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        Cookie cookie=new Cookie("token",null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @PostMapping("/token/validation")
    @ResponseStatus(HttpStatus.OK)
    public void jwtValidate(@RequestParam String token)  {

        if (!jwtUtil.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid JWT");
        }

    }

}
