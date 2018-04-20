package naakcii.by.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Bean

 public WebMvcConfigurer corsConfigurer() {

  return new WebMvcConfigurerAdapter() {

   @Override

   public void addCorsMappings(CorsRegistry registry) {

    registry.addMapping("/**").allowedMethods("GET", "PUT", "POST", "DELETE", "PATCH", "HEADER");

   }

  };

 }

@SpringBootApplication
public class APIApplication {

	public static void main(String[] args) {
		SpringApplication.run(APIApplication.class, args);
	}
}
