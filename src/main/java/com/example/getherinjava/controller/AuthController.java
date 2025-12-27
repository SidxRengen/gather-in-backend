package com.example.getherinjava.controller;

import com.example.getherinjava.config.JwtUtil;
import com.example.getherinjava.dto.LoginRequest;
import com.example.getherinjava.dto.SignupRequest;
import com.example.getherinjava.dto.ResponseBody;
import com.example.getherinjava.entry.User;
import com.example.getherinjava.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtUtil jwtUtil,PasswordEncoder passwordEncoder,UserRepository userRepository){
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userRepository =userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest){
        if(userRepository.findByEmail(signupRequest.getEmail()).orElse(null)!=null){
            ResponseBody responseBody = new ResponseBody("User Already Exists!",false,new HashMap<>());
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }
        User user = new User(
                signupRequest.getUserName(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword())
        );
        userRepository.save(user);
        ResponseBody responseBody = new ResponseBody("New User Successfully Created!",true,new HashMap<>());
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest){
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        if (user==null){
            ResponseBody responseBody = new ResponseBody("No User found",false,new HashMap<>());
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())) {
            ResponseBody responseBody = new ResponseBody("Incorrect Password",false,new HashMap<>());
            return new ResponseEntity<>(responseBody, HttpStatus.FORBIDDEN);
        }
        String token = jwtUtil.generateToken(user.getUserName(),user.getEmail());
        Map<String,String> data = new HashMap<>();
        data.put("token",token);
        data.put("username",user.getUserName());
        data.put("email",user.getEmail());
        ResponseBody responseBody = new ResponseBody("Login Successfully!",true,data);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
