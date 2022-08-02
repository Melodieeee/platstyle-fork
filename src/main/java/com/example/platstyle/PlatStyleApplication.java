package com.example.platstyle;

import com.example.platstyle.entities.Customer;
import com.example.platstyle.entities.User;
import com.example.platstyle.repositories.CustomerRepository;
import com.example.platstyle.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;

@SpringBootApplication
public class PlatStyleApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlatStyleApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, CustomerRepository customerRepository){
        return args -> {
            if(userRepository.count()==0) {
                User user = new User(null, "Admin", "Yang",new BCryptPasswordEncoder().encode("admin"), new Date(), "admin@gmail.com","M", "6041231235", "ROLE_ADMIN", null, null, null);
                userRepository.save(user);
                userRepository.flush();
                Customer customer = new Customer();
                customer.setUid(user.getUid());
                customerRepository.save(customer);
                user = new User(null, "Stylist", "Yang",new BCryptPasswordEncoder().encode("stylist"), new Date(), "stylist@gmail.com","M", "6041231235", "ROLE_USER", null, null, null);
                userRepository.save(user);
                userRepository.flush();
                customer = new Customer();
                customer.setUid(user.getUid());
                customerRepository.save(customer);
                user = new User(null, "User", "Yang",new BCryptPasswordEncoder().encode("user"), new Date(), "user@gmail.com","M", "6041231235", "ROLE_USER", null, null, null);
                userRepository.save(user);
                userRepository.flush();
                customer = new Customer();
                customer.setUid(user.getUid());
                customerRepository.save(customer);
            }
        };
    }
}
