package naakcii.by.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import naakcii.by.api.repository.util.DataParser;

@SpringBootApplication
public class APIApplication implements CommandLineRunner {

	@Autowired
	DataParser dp;

	public static void main(String[] args) {
		SpringApplication.run(APIApplication.class, args);
	}
	
	public void run(String... args) throws Exception {
		dp.parseCategories("src/main/resources/Test_data.xlsx");
		dp.parseActions("src/main/resources/Test_data.xlsx");
		//Code to run at application startup
		/*Category c1 = new Category("c1", true);
		Category c2 = new Category("c2", true);
		Subcategory s1 = new Subcategory("s1", true, c1);
		Subcategory s2 = new Subcategory("s2", true, c1);
		Subcategory s3 = new Subcategory("s3", true, c1);
		Subcategory s4 = new Subcategory("s4", true, c1);
		c1.getSubcategories().add(s1);
		c1.getSubcategories().add(s2);
		c1.getSubcategories().add(s3);
		c2.getSubcategories().add(s4);
				
		cd.save(c1);
		cd.save(c2);
		
		Chain ch1 = new Chain("chain1", "ch l1", true);
		Chain ch2 = new Chain("chain2", "ch l2", true);
		ichd.save(ch1);
		ichd.save(ch2);
		
		/*Product p1 = new Product("p1", true, s1);
		Product p2 = new Product("p2", true, s1);
		Product p3 = new Product("p3", true, s2);
		Product p4 = new Product("p4", true, s2);
		Product p5 = new Product("p5", true, s3);
		Product p6 = new Product("p6", true, s4);
		ProductInfo pi1 = new ProductInfo("pi1 descr", 1, "measure1");
		ProductInfo pi2 = new ProductInfo("pi2 descr", 1, "measure2");
		ProductInfo pi3 = new ProductInfo("pi3 descr", 1, "measure3");
		ProductInfo pi4 = new ProductInfo("pi4 descr", 1, "measure4");
		ProductInfo pi5 = new ProductInfo("pi5 descr", 1, "measure5");
		ProductInfo pi6 = new ProductInfo("pi6 descr", 1, "measure6");
		s1.getProducts().add(p1);
		s1.getProducts().add(p2);
		s2.getProducts().add(p3);
		s2.getProducts().add(p4);
		s3.getProducts().add(p5);
		s4.getProducts().add(p6);
		p1.setProductInfo(pi1);
		p2.setProductInfo(pi2);
		p3.setProductInfo(pi3);
		p4.setProductInfo(pi4);
		p5.setProductInfo(pi5);
		p6.setProductInfo(pi6);
		pi1.setProduct(p1);
		pi2.setProduct(p2);
		pi3.setProduct(p3);
		pi4.setProduct(p4);
		pi5.setProduct(p5);
		pi6.setProduct(p6);
		
		Chain ch1 = new Chain("chain1", "ch l1", true);
		Chain ch2 = new Chain("chain2", "ch l2", true);
		ichd.save(ch1);
		ichd.save(ch2);
		
		icd.save(c1);
		icd.save(c2);
		
		Action ac1 = new Action(p1, ch1, 1.0);
		Action ac2 = new Action(p2, ch1, 1.0);
		Action ac3 = new Action(p3, ch1, 1.0);
		Action ac4 = new Action(p4, ch1, 1.0);
		Action ac5 = new Action(p5, ch2, 1.0);
		Action ac6 = new Action(p6, ch2, 1.0);
		
		iad.save(ac1);
		iad.save(ac2);
		iad.save(ac3);
		iad.save(ac4);
		iad.save(ac5);
		iad.save(ac6);
		
		for (Product p: ipd.findAllWithDetails()) {
			System.out.println("Product "+ p.getName());
			System.out.println("Product subcategory "+ p.getSubcategory().getName());
			System.out.println("Product info "+ p.getProductInfo().getDescription());
		}
		for (Subcategory sub: isd.findByCategoryId(c1.getId())) {
			System.out.println("Subcategory name " + sub.getName());
		}
		for (Product p: ipd.findBySubcategoryId(s1.getId())) {
			System.out.println("Product name " + p.getName());
		}
		for (Action a : iad.findByChainIdAndProductSubcategory(ch1.getId(), s2.getId())) {
			System.out.println("Product name " + a.getProduct().getName());
		}*/
	}
}