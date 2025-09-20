package com.asv.hotel;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratorPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String rawPassword = "password"; // Ваш пароль
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);


    }
}
