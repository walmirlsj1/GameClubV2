package br.ufms.cpcx.api.gamersclub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GamersClubApplication {

    public static void main(String[] args) {
        SpringApplication.run(GamersClubApplication.class, args);
    }

}
