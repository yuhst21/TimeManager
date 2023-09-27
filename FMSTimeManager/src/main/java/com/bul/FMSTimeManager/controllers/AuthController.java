//package com.bul.FMSTimeManager.controllers;
//
//import com.bul.FMSTimeManager.daos.UserRepository;
//import com.bul.FMSTimeManager.dto.AuthResponseDTO;
//import com.bul.FMSTimeManager.dto.LoginDto;
//import com.bul.FMSTimeManager.security.JWTGenerator;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//@RequestMapping("/tms")
//public class AuthController {
//
//    private AuthenticationManager authenticationManager;
//    private UserRepository userRepository;
//    private PasswordEncoder passwordEncoder;
//    private JWTGenerator jwtGenerator;
//
//    @Autowired
//    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
//                           PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator) {
//        this.authenticationManager = authenticationManager;
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.jwtGenerator = jwtGenerator;
//    }
//
//    @PostMapping("/log")
//    public String login(@RequestBody LoginDto loginDto){
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                loginDto.getUsername(),
//                loginDto.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String token = jwtGenerator.generateToken(authentication);
//       /* return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);*/
//        return "check";
//    }
//
//}
