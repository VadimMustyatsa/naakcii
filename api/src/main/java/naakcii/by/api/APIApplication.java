package naakcii.by.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@SpringBootApplication

public class APIApplication {
	@Bean

	public WebMvcConfigurer corsConfigurer() {

		return new WebMvcConfigurerAdapter() {

			@Override

			public void addCorsMappings(CorsRegistry registry) {

				registry.addMapping("/**").allowedMethods("GET", "PUT", "POST", "DELETE", "PATCH", "HEADER");

			}

		};

	}

	public static void main(String[] args) {
		SpringApplication.run(APIApplication.class, args);
	}
}
