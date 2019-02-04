package naakcii.by.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class APIApplication extends SpringBootServletInitializer implements CommandLineRunner {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(APIApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(APIApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("Hello world");
    }
}
