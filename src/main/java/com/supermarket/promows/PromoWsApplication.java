package com.supermarket.promows;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.supermarket.promows")
@EntityScan("com.supermarket.promows.model")
@EnableJpaRepositories("com.supermarket.promows.repository")
@EnableJpaAuditing 
public class PromoWsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PromoWsApplication.class, args);
	}

}
