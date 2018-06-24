package naakcii.by.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import naakcii.by.api.repository.util.DataParser;


@SpringBootApplication
public class APIApplication implements CommandLineRunner {

	@Autowired
	private DataParser dp;
	
    public static void main(String[] args) {
        SpringApplication.run(APIApplication.class, args);
    }

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		dp.parseCategories("src/main/resources/Test_data.xlsx");
		dp.parseChains("src/main/resources/Test_data.xlsx");
		dp.parseActions("src/main/resources/Test_data.xlsx");
		//dp.test();
	}
}
