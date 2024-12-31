package com.alten;

import com.alten.entities.AppUser;
import com.alten.service.AccountService;
import com.alten.utils.ControllerConst;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
public class KataTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(KataTestApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner start(AccountService accountService){
        return args -> {
            accountService.addNewUser(new AppUser(null,"user1","user1", ControllerConst.ADMIN,"1234"));
        };
    }

}
