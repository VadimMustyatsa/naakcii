package naakcii.by.api.repository.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import naakcii.by.api.repository.model.Action;
import naakcii.by.api.repository.model.Category;
import naakcii.by.api.repository.model.Chain;
import naakcii.by.api.repository.model.Product;
import naakcii.by.api.repository.model.Subcategory;
import naakcii.by.api.repository.model.repository.ActionRepository;
import naakcii.by.api.repository.model.repository.CategoryRepository;
import naakcii.by.api.repository.model.repository.ChainRepository;
import naakcii.by.api.repository.model.repository.ProductRepository;
import naakcii.by.api.repository.model.repository.SubcategoryRepository;

@Component
public class DataParser {
	
	@Autowired
	ProductDataHandler productDataHandler;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	SubcategoryRepository subcategoryRepository;
	
	@Autowired
	ChainRepository chainRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ActionRepository actionRepository;
	
	public void parseCategories(String fileName) {
		try (FileInputStream fis = new FileInputStream(fileName)) { 
			XSSFWorkbook book = new XSSFWorkbook(fis); 
			XSSFSheet sheet = book.getSheet("Каталог 0.2");
			Iterator<Row> rowIterator = sheet.iterator();
			rowIterator.next();
			Category category = null;
			Subcategory subcategory = null;
			Cell categoryCell = null;
			Cell subcategoryCell = null;
			String categoryName = null;
			String subcategoryName = null;
			Row row = rowIterator.next();
			while (rowIterator.hasNext()) {
				categoryCell = row.getCell(0);
				categoryName = categoryCell.getStringCellValue();
				if (categoryName != "") {
					category = new Category (categoryName, true);
					System.out.println("Category \"" + category.getName() + "\" was found.");
					while (rowIterator.hasNext()) {
						row = rowIterator.next();
						subcategoryCell = row.getCell(1);
						subcategoryName = subcategoryCell.getStringCellValue();
						if (subcategoryName != "") {
							subcategory = new Subcategory (subcategoryName, true, category);
							System.out.println("Subcategory \"" + category.getName() + "\"." + subcategory.getName() + "\" was found.");
							category.getSubcategories().add(subcategory);	
						} else {
							categoryRepository.save(category);
							break;
						}
					}
				} else break;
			}
		} catch (FileNotFoundException fe) { 
			fe.printStackTrace(); 
		} catch (IOException ie) { 
			ie.printStackTrace();
		}
	}
	
	public void parseActions(String fileName) {
		try (FileInputStream fis = new FileInputStream(fileName)) { 
			XSSFWorkbook book = new XSSFWorkbook(fis);
			XSSFSheet sheet;
			for (int i = 0; i <= 6; i++) {
				sheet = book.getSheetAt(i);
				Iterator<Row> rowIterator = sheet.iterator();
				Row row = rowIterator.next();
				Subcategory subcategory = null;
				Product product = null;
				Action action = null;
				Chain chain = new Chain(sheet.getSheetName(), sheet.getSheetName() + " link", true);
				chainRepository.save(chain);
				Cell categoryCell = null;
				Cell subcategoryCell = null;
				Cell productCell = null;
				Cell priceCell = null;
				Cell discountPriceCell = null;
				Cell dateCell = null;
				String categoryName = null;
				String subcategoryName = null;
				String productDescription = null;
				String dates = null;
				double discountPrice = 0.0;
				double price = 0.0;
				int discount = 0;
				while (rowIterator.hasNext()) {
					row = rowIterator.next();
					productCell = row.getCell(0);
					categoryCell = row.getCell(1);
					subcategoryCell = row.getCell(2);
					discountPriceCell = row.getCell(5);
					dateCell = row.getCell(6);
					productDescription = productCell.getStringCellValue();
					categoryName = categoryCell.getStringCellValue();
					subcategoryName = subcategoryCell.getStringCellValue();
					discountPrice = discountPriceCell.getNumericCellValue();
					dates = dateCell.getStringCellValue();
					if (productDescription != "" && categoryName != "" && subcategoryName != "" && discountPrice != 0) {
						priceCell = row.getCell(3);
						dateCell = row.getCell(6);
						price = priceCell.getNumericCellValue();
						subcategory = subcategoryRepository.findByNameAndCategoryName(subcategoryName, categoryName);
						System.out.println("Chain \"" + chain.getName() + "\".");
						System.out.println("Subcategory \"" + subcategory.getName() + "\".");
						product = new Product(productDescription, true, subcategory);
						System.out.println("Product \"" + product.getName() + "\".");
						Map<String, String> quantityAndMeasure =  productDataHandler.parseQuantityAndMeasure(productDescription);
						if (quantityAndMeasure.containsKey("quantity")) {
							product.setQuantity(Double.parseDouble(quantityAndMeasure.get("quantity")));
							System.out.println("Quantity " + product.getQuantity() + ".");
						}
						if (quantityAndMeasure.containsKey("measure")) {
							product.setMeasure(quantityAndMeasure.get("measure"));
							System.out.println("Measure \"" + product.getMeasure() + "\".");
						}
						productRepository.save(product);
						action = new Action(product, chain, discountPrice);
						System.out.println("Discount price " + action.getDiscountPrice() + ".");
						if (price != 0) {
							action.setPrice(price);
							System.out.println("Base price " + action.getPrice() + ".");
							discount = (int) (100.0 * (1.0 -discountPrice/price));
							action.setDiscount(discount);
							System.out.println("Discount " + action.getDiscount() + "%.");
						}
						if (dates != "") {
							Map<String, Calendar> datesMap = productDataHandler.parseDate(dates);
							if (datesMap.containsKey("start")) {
								action.setStartDate(datesMap.get("start"));
								System.out.println("Start date " + action.getStartDate().get(Calendar.DATE) + "." +
										action.getStartDate().get(Calendar.MONTH) + "." +
										action.getStartDate().get(Calendar.YEAR) + ".");
							}
							if (datesMap.containsKey("end")) {
								action.setEndDate(datesMap.get("end"));
								System.out.println("End date " + action.getEndDate().get(Calendar.DATE) + "." +
										action.getEndDate().get(Calendar.MONTH) + "." +
										action.getEndDate().get(Calendar.YEAR) + ".");
							}
						}
						actionRepository.save(action);
					} else break;	
				}
			}
		} catch (FileNotFoundException fe) { 
			fe.printStackTrace(); 
		} catch (IOException ie) { 
			ie.printStackTrace();
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void test() {
		System.out.println("Chains:");
		for (Chain ch: chainRepository.findAllByOrderByNameAsc()) {
			System.out.println(ch.getName());
		}
		System.out.println("Categories:");
		for (Category cat: categoryRepository.findAll()) {
			System.out.println(cat.getName());
		}
		System.out.println("Subcategories:");
		for (Subcategory subcat: subcategoryRepository.findByIsActiveTrueAndCategoryIdOrderByNameAsc(1009L)) {
			System.out.println(subcat.getName());
		}
		//subcategoryRepository.softDelete(1010L);
		//categoryRepository.softDelete(1000L);
		//chainRepository.softDelete(1124L);
		System.out.println("Categories:");
		for (Category cat: categoryRepository.findAllByIsActiveTrue()) {
			System.out.println(cat.getName());
		}
		System.out.println("Chains:");
		for (Chain ch: chainRepository.findAllByIsActiveTrueOrderByNameAsc()) {
			System.out.println(ch.getName());
		}
		System.out.println("Subcategories:");
		for (Subcategory subcat: subcategoryRepository.findByIsActiveTrueAndCategoryId(1009L)) {
			System.out.println(subcat.getName());
		}
		System.out.println("Subcategories:");
		for (Subcategory subcat: subcategoryRepository.findByIsActiveTrueAndCategoryIdOrderByNameDesc(1009L)) {
			System.out.println(subcat.getName());
		}
		Calendar currentDate = Calendar.getInstance();
		System.out.println("Products:");
		for (Action action: actionRepository.findAllBySubcategoryId(1030L, currentDate)) {
			System.out.println(action.getProduct().getName());
		}
		Set<Long> set = new HashSet<Long>();
		set.add(1015L);
		set.add(1016L);
		set.add(1029L);
		set.add(1030L);
		set.add(1007L);
		System.out.println("Products:");
		for (Action action: actionRepository.findAllBySubcategoriesIds(set, currentDate)) {
			System.out.println(action.getProduct().getName());
		}
	}
	
}
