package aji.intern.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BankingIvrApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingIvrApiApplication.class, args);
    }

}
