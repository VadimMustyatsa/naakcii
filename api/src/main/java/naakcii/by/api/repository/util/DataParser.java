package naakcii.by.api.repository.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import naakcii.by.api.repository.dao.ActionDao;
import naakcii.by.api.repository.dao.CategoryDao;
import naakcii.by.api.repository.dao.ChainDao;
import naakcii.by.api.repository.dao.ProductDao;
import naakcii.by.api.repository.dao.SubcategoryDao;
import naakcii.by.api.repository.model.Action;
import naakcii.by.api.repository.model.Category;
import naakcii.by.api.repository.model.Chain;
import naakcii.by.api.repository.model.Product;
import naakcii.by.api.repository.model.ProductInfo;
import naakcii.by.api.repository.model.Subcategory;

@Component
public class DataParser {
	
	@Autowired
	CategoryDao icd;
	
	@Autowired
	SubcategoryDao isd;
	
	@Autowired
	ProductDao ipd;
	
	@Autowired
	ChainDao ichd;
	
	@Autowired
	ActionDao iad;
	
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
			Row row = rowIterator.next();
			while (rowIterator.hasNext()) {
				categoryCell = row.getCell(0);
				if (categoryCell.getStringCellValue() != "") {
					System.out.println("Category " + categoryCell.getStringCellValue());
					category = new Category (categoryCell.getStringCellValue(), true);
					while (rowIterator.hasNext()) {
						row = rowIterator.next();
						subcategoryCell = row.getCell(1);
						if (subcategoryCell.getStringCellValue() != "") {
							System.out.println("Subcategory " + subcategoryCell.getStringCellValue());
							subcategory = new Subcategory (subcategoryCell.getStringCellValue(), true, category);
							category.getSubcategories().add(subcategory);	
						} else {
							icd.save(category);
							System.out.println("Saved " + category.getName());
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
				rowIterator.next();
				Subcategory subcategory = null;
				Product product = null;
				ProductInfo productInfo = null;
				Action action = null;
				Chain chain = new Chain(sheet.getSheetName(), sheet.getSheetName() + " link", true);
				ichd.save(chain);
				Cell categoryCell = null;
				Cell subcategoryCell = null;
				Cell productCell = null;
				Cell priceCell = null;
				Cell discountCell = null;
				Cell discountPriceCell = null;
				Row row = rowIterator.next();
				while (rowIterator.hasNext()) {
					row = rowIterator.next();
					if (row.getCell(0).getStringCellValue() != "" && row.getCell(1).getStringCellValue() != "" && row.getCell(2).getStringCellValue() != "") {
						productCell = row.getCell(0);
						categoryCell = row.getCell(1);
						subcategoryCell = row.getCell(2);
						priceCell = row.getCell(3);
						discountCell = row.getCell(4);
						discountPriceCell= row.getCell(5);
						subcategory = isd.findByNameAndCategoryNameWithDetails(subcategoryCell.getStringCellValue(), categoryCell.getStringCellValue());
						System.out.println(chain.getName());
						System.out.println(row.getRowNum());
						System.out.println(subcategoryCell.getStringCellValue());
						System.out.println(subcategory.getName());
						product = new Product(productCell.getStringCellValue(), true, subcategory);
						productInfo = new ProductInfo(product.getName() + "descr", 1, product.getName() + " measure");
						subcategory.getProducts().add(product);
						product.setProductInfo(productInfo);
						productInfo.setProduct(product);
						ipd.save(product);
						isd.save(subcategory);
						action = new Action(product, chain, priceCell.getNumericCellValue());
						action.setDiscountPrice(discountPriceCell.getNumericCellValue());
						action.setDiscount(discountCell.getNumericCellValue());
						iad.save(action);
					} else if (row.getCell(0).getStringCellValue() == "" && row.getCell(1).getStringCellValue() == "") 
						break;	
				}
			}
		} catch (FileNotFoundException fe) { 
			fe.printStackTrace(); 
		} catch (IOException ie) { 
			ie.printStackTrace();
		}
	}

}
