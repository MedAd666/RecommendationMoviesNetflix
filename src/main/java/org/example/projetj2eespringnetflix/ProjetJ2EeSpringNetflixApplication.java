package org.example.projetj2eespringnetflix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@SpringBootApplication
public class ProjetJ2EeSpringNetflixApplication  {

    public static void main(String[] args) {
        SpringApplication.run(ProjetJ2EeSpringNetflixApplication.class, args);
    }

}
