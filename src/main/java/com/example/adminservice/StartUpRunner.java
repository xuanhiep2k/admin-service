//package com.example.adminservice;
//
//import com.example.adminservice.model.User;
//import com.example.adminservice.repository.UserRepository;
//import com.example.adminservice.utils.Constants;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class StartUpRunner implements CommandLineRunner {
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public void run(String... args) throws Exception {
//        if (!userRepository.existsByUsername("admin")) {
//            User admin = new User();
//            admin.setUsername("admin");
//            admin.setPassword(passwordEncoder.encode("abc@123"));
//            admin.setStatus(Constants.STATUS.ACTIVE);
//            userRepository.saveAndFlush(admin);
//        }
//    }
//}
