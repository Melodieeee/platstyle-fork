package com.example.platstyle;

import com.example.platstyle.entities.User;
import com.example.platstyle.respositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class PlatStyleApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlatStyleApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository){
        return args -> {
            //userRepository.save(new User(null, "Sam", "Yang", new Date(), "yangsam810@gmail.com", 1, "60435268888"));
            userRepository.findAll().forEach(p->{
                System.out.println(p.getFirstName());
            });
        };
    }
}
