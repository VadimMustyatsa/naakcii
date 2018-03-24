package by.naakcii.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import by.naakcii.repository.dao.CategoryDao;
import by.naakcii.repository.dao.SubcategoryDao;
import by.naakcii.repository.model.Category;
import by.naakcii.repository.model.Subcategory;

@SpringBootApplication
public class RepositoryApplication implements CommandLineRunner {
	
	@Autowired
	CategoryDao icd;
	
	@Autowired
	SubcategoryDao isd;

	public static void main(String[] args) {
		SpringApplication.run(RepositoryApplication.class, args);
	}
	
	public void run(String... args) throws Exception {
		//Code to run at application startup
		Category c1 = new Category("c1", true);
		Category c2 = new Category("c2", true);
		Subcategory s1 = new Subcategory("s1", true, c1);
		Subcategory s2 = new Subcategory("s2", true, c1);
		Subcategory s3 = new Subcategory("s3", true, c1);
		Subcategory s4 = new Subcategory("s4", true, c1);
		c1.getSubcategories().add(s1);
		c1.getSubcategories().add(s2);
		c1.getSubcategories().add(s3);
		c2.getSubcategories().add(s4);
		icd.save(c1);
		icd.save(c2);
		//isd.save(s1);
		//isd.save(s2);
		//isd.save(s3);
		//isd.save(s4);
	}
}
