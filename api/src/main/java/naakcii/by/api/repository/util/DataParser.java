package naakcii.by.api.repository.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import naakcii.by.api.repository.dao.ActionDao;
import naakcii.by.api.repository.dao.CategoryDao;
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
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class DataParser {
	
	private static final Logger logger = LogManager.getLogger(DataParser.class);
	
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
			try (XSSFWorkbook book = new XSSFWorkbook(fis)) { 
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
						logger.info("New category \"" + category.getName() + "\" was found.");
						while (rowIterator.hasNext()) {
							row = rowIterator.next();
							subcategoryCell = row.getCell(1);
							subcategoryName = subcategoryCell.getStringCellValue();
							if (subcategoryName != "") {
								subcategory = new Subcategory (subcategoryName, true, category);
								logger.info("New subcategory \"" + category.getName() + "\"." + subcategory.getName() + "\" was found.");
								category.getSubcategories().add(subcategory);	
							} else {
								categoryRepository.save(category);
								break;
							}
						}
					} else break;
				}
			}
		} catch (FileNotFoundException fnfEx) {
			logger.error("FileNotFoundException: " + fnfEx.getMessage());
		} catch (IOException ioEx) {
			logger.error("IOException: " + ioEx.getMessage());
		}
	}
	
	public void parseChains(String fileName) {
		try (FileInputStream fis = new FileInputStream(fileName)) {	
			try (XSSFWorkbook book = new XSSFWorkbook(fis)) {
				XSSFSheet sheet = book.getSheet("Сети");
				Iterator<Row> rowIterator = sheet.iterator();
				Chain chain = null;
				Cell chainCell = null;
				Cell linkCell = null;
				String chainName = null;
				String chainLink = null;
				Row row = rowIterator.next();
				while (rowIterator.hasNext()) {
					row = rowIterator.next();
					chainCell = row.getCell(0);
					linkCell = row.getCell(1);
					chainName = chainCell.getStringCellValue();
					chainLink = linkCell.getStringCellValue();
					if (chainName != "" && chainLink != "") {
						chain = new Chain (chainName, chainLink, true);
						logger.info("New chain \"" + chain.getName() + "\" was found.");
						chainRepository.save(chain);
					}
				}
			}
		} catch (FileNotFoundException fnfEx) {
			logger.error("FileNotFoundException: " + fnfEx.getMessage());
		} catch (IOException ioEx) {
			logger.error("IOException: " + ioEx.getMessage());
		}
	}
	
	
	public void parseActions(String fileName) {
		try (FileInputStream fis = new FileInputStream(fileName)) { 
			try (XSSFWorkbook book = new XSSFWorkbook(fis)) {
				XSSFSheet sheet;
				for (int i = 0; i <= 6; i++) {
					sheet = book.getSheetAt(i);
					Chain chain = chainRepository.findByName(sheet.getSheetName());
					if (chain != null) {
						logger.info("Searching all actions for chain \"" + chain.getName() + "\".");
						Iterator<Row> rowIterator = sheet.iterator();
						Row row = rowIterator.next();
						Subcategory subcategory = null;
						Product product = null;
						Action action = null;
						Cell categoryCell = null;
						Cell subcategoryCell = null;
						Cell productCell = null;
						Cell priceCell = null;
						Cell discountPriceCell = null;
						Cell dateCell = null;
						Cell pictureCell = null;
						String categoryName = null;
						String subcategoryName = null;
						String productDescription = null;
						String dates = null;
						String picture = null;
						double discountPrice = 0.0;
						double price = 0.0;
						int discount = 0;
						while (rowIterator.hasNext()) {
							row = rowIterator.next();
							productCell = row.getCell(0);
							categoryCell = row.getCell(1);
							subcategoryCell = row.getCell(2);
							discountPriceCell = row.getCell(5);
							productDescription = productCell.getStringCellValue();
							categoryName = categoryCell.getStringCellValue();
							subcategoryName = subcategoryCell.getStringCellValue();
							discountPrice = discountPriceCell.getNumericCellValue();
							if (productDescription != "" && categoryName != "" && subcategoryName != "" && discountPrice != 0) {
								priceCell = row.getCell(3);
								dateCell = row.getCell(6);
								pictureCell = row.getCell(7);
								picture = pictureCell.getStringCellValue();
								dates = dateCell.getStringCellValue();
								price = priceCell.getNumericCellValue();
								subcategory = subcategoryRepository.findByNameAndCategoryName(subcategoryName, categoryName);
								if (subcategory != null) {
									product = new Product(productDescription, true, subcategory);
									logger.info("New product \"" + subcategory.getName() + "\"." + product.getName() + "\" was found.");
									Map<String, String> quantityAndMeasure =  productDataHandler.parseQuantityAndMeasure(productDescription);
									if (quantityAndMeasure.containsKey("quantity")) {
										product.setQuantity(Double.parseDouble(quantityAndMeasure.get("quantity")));
										logger.info("Quantity " + product.getQuantity() + ".");
									}
									if (quantityAndMeasure.containsKey("measure")) {
										product.setMeasure(quantityAndMeasure.get("measure"));
										logger.info("Measure \"" + product.getMeasure() + "\".");
									}
									if (picture != "") {
										product.setPicture(categoryName + "/" + subcategoryName + "/" + picture);
									}
									productRepository.save(product);
									action = new Action(product, chain, discountPrice);
									logger.info("Discount price " + action.getDiscountPrice() + ".");
									if (price != 0) {
										action.setPrice(price);
										logger.info("Base price " + action.getPrice() + ".");
										discount = (int) (100.0 * (1.0 -discountPrice/price));
										action.setDiscount(discount);
										logger.info("Discount " + action.getDiscount() + "%.");
									}
									if (dates != "") {
										Map<String, Calendar> datesMap = productDataHandler.parseDate(dates);
										if (datesMap.containsKey("start")) {
											action.setStartDate(datesMap.get("start"));
											logger.info("Start date " + action.getStartDate().get(Calendar.DATE) + "." +
												action.getStartDate().get(Calendar.MONTH) + "." +
												action.getStartDate().get(Calendar.YEAR) + ".");
										}
										if (datesMap.containsKey("end")) {
											action.setEndDate(datesMap.get("end"));
											logger.info("End date " + action.getEndDate().get(Calendar.DATE) + "." +
													action.getEndDate().get(Calendar.MONTH) + "." +
													action.getEndDate().get(Calendar.YEAR) + ".");
										}
									}
									actionRepository.save(action);
								} else {
									logger.info("Product \"" + productDescription + "\" with indentifinite subcaategory \"" + subcategoryName + "\" was found. It will be ignored.");
									continue;
								}
							} else if (productDescription == "") break;	
						}
					} else {
						logger.info("Indefinite chain \"" + sheet.getSheetName() + "\" was found. Actions for this chain will be ignored.");
						continue;
					}
				}
			}
		} catch (FileNotFoundException fnfEx) {
			logger.error("FileNotFoundException: " + fnfEx.getMessage());
		} catch (IOException ioEx) {
			logger.error("IOException: " + ioEx.getMessage());
		}
	}
	
	@Autowired
	CategoryDao cd;
	
	@Autowired
	ActionDao ac;
	
	public void test() {
		System.out.println("Repository test");
		/*for (Category category : categoryRepository.findAll()) {
			System.out.println("Category " + category.getName());
			for (Subcategory subcategory : category.getSubcategories())
				System.out.println("Subcategory " + subcategory.getName());
		}*/
		//categoryRepository.softDelete(1064L);
		//categoryRepository.softDelete(1058L);
		//for (Category category : categoryRepository.findAll()) {
		//	System.out.println("Category " + category.getName());
			//for (Subcategory subcategory : category.getSubcategories())
			//System.out.println("Subcategory " + subcategory.getName());
		//}
		/*for (Category category : categoryRepository.findAllByIsActiveTrue()) {
			System.out.println("Category " + category.getName());
			for (Subcategory subcategory : category.getSubcategories())
			System.out.println("Subcategory " + subcategory.getName());
		}*/
		
		/*Calendar currentDate = Calendar.getInstance();
		for (Action action : actionRepository.findAllBySubcategoryId(1006L, currentDate)) {
			System.out.println("Action product " + action.getProduct().getName());
			System.out.println("Action chain " + action.getChain().getName());
		}
		Set<Long> s = new HashSet<Long>();
		s.add(1006L);
		s.add(1007L);*/
		/*for (Action action : actionRepository.findAllBySubcategoriesIds(s, currentDate)) {
			System.out.println("Action product " + action.getProduct().getName());
			System.out.println("Action chain " + action.getChain().getName());
		}*/
		/*
		System.out.println("Dao test");
		for (Category category : cd.findAll()) {
			System.out.println("Category " + category.getName());
		}
		for (Category category : cd.findAllWithDetails()) {
			System.out.println("Category " + category.getName());
			for (Subcategory subcategory : category.getSubcategories())
			System.out.println("Subcategory " + subcategory.getName());
		}
		*/
		/*cd.softDelete(1058L);
		for (Category category : cd.findAllByIsActiveTrue()) {
			System.out.println("Category " + category.getName());
		}
		for (Category category : cd.findAllByIsActiveTrueWithDetails()) {
			System.out.println("Category " + category.getName());
			for (Subcategory subcategory : category.getSubcategories())
			System.out.println("Subcategory " + subcategory.getName());
		}*/
		/*for (Action action : ac.findAllBySubcategoryId(1006L, currentDate)) {
			System.out.println("Action product " + action.getProduct().getName());
			System.out.println("Action chain " + action.getChain().getName());
		}
		for (Action action : ac.findAllBySubcategoriesIds(s, currentDate)) {
			System.out.println("Action product " + action.getProduct().getName());
			System.out.println("Action chain " + action.getChain().getName());
		}*/
	}
	
}
