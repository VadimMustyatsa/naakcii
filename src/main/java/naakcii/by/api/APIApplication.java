package naakcii.by.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import naakcii.by.api.actiontype.ActionType;
import naakcii.by.api.actiontype.ActionTypeRepository;

@SpringBootApplication
public class APIApplication extends SpringBootServletInitializer implements CommandLineRunner {
	
	@Autowired
	ActionTypeRepository actionTypeRepository;
	
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
		//System.out.println("Hello world");
		//ActionType at = new ActionType("name1");
		//actionTypeRepository.save(at);
	}
}
