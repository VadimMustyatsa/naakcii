package naakcii.by.api;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import naakcii.by.api.util.parser.DataParser;

@SpringBootApplication
@EnableScheduling
public class APIApplication extends SpringBootServletInitializer implements CommandLineRunner {

	//@Autowired
	//private DataParser dataParser;
	
	private static final String FILE_WITH_CHAIN_PRODUCTS = "src" + File.separator + 
			   "test" + File.separator +
			   "resources" + File.separator + 
			   "Test_chain_products.xlsx";
	private static final String TEST_FILE_WITH_CHAIN_PRODUCTS = "src" + File.separator + 
				"test" + File.separator +
				"resources" + File.separator + 
				"testcases" + File.separator +
				"almi" + File.separator +
				"2018_02_08_4.xlsx";
	
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
    	//dataParser.parseBasicData();
    	//dataParser.parseChainProducts(TEST_FILE_WITH_CHAIN_PRODUCTS, "almi");    	
        System.out.println("Hello world");
    }
}
