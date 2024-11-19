package com.ssafy.wattagatta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WattagattaApplication {
    public static void main(String[] args) {
        SpringApplication.run(WattagattaApplication.class, args);
    }
}
