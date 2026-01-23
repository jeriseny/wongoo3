package org.wongoo.wongoo3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Wongoo3Application {

    public static void main(String[] args) {
        SpringApplication.run(Wongoo3Application.class, args);
    }

}
