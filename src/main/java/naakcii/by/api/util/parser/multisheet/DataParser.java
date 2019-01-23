package naakcii.by.api.util.parser.multisheet;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import naakcii.by.api.category.Category;
import naakcii.by.api.category.CategoryRepository;
import naakcii.by.api.chain.Chain;
import naakcii.by.api.chain.ChainRepository;
import naakcii.by.api.chainproduct.ChainProduct;
import naakcii.by.api.chainproduct.ChainProductRepository;
import naakcii.by.api.chainproducttype.ChainProductType;
import naakcii.by.api.chainproducttype.ChainProductTypeRepository;
import naakcii.by.api.country.Country;
import naakcii.by.api.country.CountryCode;
import naakcii.by.api.country.CountryRepository;
import naakcii.by.api.product.Product;
import naakcii.by.api.product.ProductRepository;
import naakcii.by.api.subcategory.Subcategory;
import naakcii.by.api.subcategory.SubcategoryRepository;
import naakcii.by.api.util.ObjectFactory;
import naakcii.by.api.util.parser.IDataParser;
import naakcii.by.api.util.parser.multisheet.mapper.ActionColumnMapper;
import naakcii.by.api.util.parser.multisheet.mapper.ActionTypeColumnMapper;
import naakcii.by.api.util.parser.multisheet.mapper.CategoryColumnMapper;
import naakcii.by.api.util.parser.multisheet.mapper.ChainColumnMapper;
import naakcii.by.api.util.parser.multisheet.mapper.ProductColumnMapper;
import naakcii.by.api.util.parser.multisheet.mapper.SubcategoryColumnMapper;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class DataParser implements IDataParser{
	
	private static final Logger logger = LogManager.getLogger(DataParser.class);
	private static Validator validator;
	private static final String FILE_WITH_BASIC_DATA = "src/test/resources/Basic_data.xlsx";
	private static final String SHEET_WITH_BASIC_ACTION_TYPES = "Basic_action_types";
	private static final String SHEET_WITH_BASIC_CATEGORIES = "Basic_categories";
	private static final String SHEET_WITH_BASIC_SUBCATEGORIES = "Basic_subcategories";
	private static final String SHEET_WITH_BASIC_CHAINS = "Basic_chains";
	private static final String[] IS_ACTIVE = {"Да", "да"};
	private static final String[] IS_NOT_ACTIVE = {"Нет", "нет"};
	private static final String INDEFINITE_CATEGORY = "Indefinite category";
	private static final String INDEFINITE_SUBCATEGORY = "Indefinite subcategory";
	private static final String ACTION_TYPE_ONE_PLUS_ONE_SYNONYM = "Z050";
	private static final String ACTION_TYPE_DISCOUNT_SYNONYM = "Z040";
	private static final String ACTION_TYPE_ONE_PLUS_ONE_NAME = "1+1";
	private static final String ACTION_TYPE_DISCOUNT_NAME = "Скидка";
	private static final String ACTION_TYPE_NICE_PRICE_NAME = "Хорошая цена";
	
	private ObjectFactory objectFactory;
	private ChainRepository chainRepository;
	private CategoryRepository categoryRepository;
	private SubcategoryRepository subcategoryRepository;
	private ProductRepository productRepository;
	private ChainProductRepository actionRepository;
	private ChainProductTypeRepository actionTypeRepository;
	private CountryRepository countryRepository;
	
	@Autowired
	public DataParser(
					  ObjectFactory objectFactory,
					  ChainRepository chainRepository,
					  CategoryRepository categoryRepository,
					  SubcategoryRepository subcategoryRepository,
					  ProductRepository productRepository,
					  ChainProductRepository actionRepository,
					  ChainProductTypeRepository actionTypeRepository,
					  CountryRepository countryRepository
					 ) {
		this.objectFactory = objectFactory;
		this.chainRepository = chainRepository;
		this.categoryRepository = categoryRepository;
		this.subcategoryRepository = subcategoryRepository;
		this.productRepository = productRepository;
		this.actionRepository = actionRepository;
		this.actionTypeRepository = actionTypeRepository;
		this.countryRepository = countryRepository; 
	}
	
	@Override
	public List<ParsingResult<?>> parseBasicData(String file) {
		logger.info("Creating of basic data has started.");
		List<ParsingResult<?>> results = new ArrayList<>();
		results.add(createCountries());
		results.add(createBasicActionTypes());
		results.add(createBasicChains());
		results.add(createBasicCategories());
		results.add(createBasicSubcategories());
		logger.info("Creating of basic data has finished.");
		return results;
	}

	@Override
	public List<ParsingResult<?>> parseActionProducts(String file, String chainSynonym) {
		List<ParsingResult<?>> results = new ArrayList<>();
		results.add(parseProducts(file));
		results.add(parseActions(file, chainSynonym));
		return results;
	}	

	public ParsingResult<Country> createCountries() {
		logger.info("Source: '{}'. Target instance: '{}'. Preparing for parsing.", CountryCode.class, Country.class);
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		ParsingResult<Country> parsingResult = objectFactory.getInstance(ParsingResult.class, Country.class, CountryCode.class.getName());
		parsingResult.setStartTime();
		int index = 0;
		logger.info("Source: '{}'. Target instance: '{}'. Starting process of parsing.", CountryCode.class, Country.class);
		
		for (CountryCode countryCode : CountryCode.values()) {
			index ++;
			parsingResult.increaseTotalNumberOfInstances();
			Country country = objectFactory.getInstance(Country.class, countryCode);
			Set<ConstraintViolation<Country>> constraintViolations = validator.validate(country);
			
			if (constraintViolations.size() == 0) {
				if (countryRepository.findByAlphaCode2(country.getAlphaCode2()).isPresent()) {
					logger.warn("Source: '{}'. Index '{}'. Value '{}'. Target instance: '{}'. New instance has been already presented in the database.",
							CountryCode.class, index, countryCode, Country.class);
					logger.warn(country.toString());
					parsingResult.increaseNumberOfUnsavedInstances();
					parsingResult.increaseNumberOfAlreadyExistingInstances();
					parsingResult.addWarning(index, "New instance has been already presented in the database." );
				} else {
					try {
						if (countryRepository.save(country) != null) {
							logger.info("Source: '{}'. Index '{}'. Value '{}'. Target instance: '{}'. New instance has been created and saved to the database.",
									CountryCode.class, index, countryCode, Country.class);
							logger.info(country.toString());
							parsingResult.increaseNumberOfSavedInstances();
						}
					} catch (Exception savingException) {
						logger.error("Source: '{}'. Index '{}'. Value '{}'. Target instance: '{}'. Exception has occurred during the saving of new instance to the database: {}.", 
								CountryCode.class, index, countryCode, Country.class, printStackTrace(savingException));
						parsingResult.increaseNumberOfUnsavedInstances();
						parsingResult.addException(index, savingException);
					}
				}	
			} else {
				logger.error("Source: '{}'. Index '{}'. Value '{}'. Target instance: '{}'. Exception(s) has occurred during the validation of new instance. See validation exception(s) below.", 
						CountryCode.class, index, countryCode, Country.class);
				parsingResult.increaseNumberOfUnsavedInstances();
				parsingResult.increaseNumberOfInvalidInstances();
				
				for (ConstraintViolation<Country> violation : constraintViolations) {
					logger.error("Validation exception: {}", violation.getMessage());
					parsingResult.addConstraintViolation(index, violation);
				}
			}
		}
		
		logger.info("Source: '{}'. Target instance: '{}'. Finishing process of parsing.", CountryCode.class, Country.class);	
		parsingResult.setFinishTime();
		logger.info("Source: '{}'. Target instance: '{}'. Returning result of parsing. {}",	CountryCode.class, Country.class, parsingResult.toString());
		return parsingResult;
	}
	
	public ParsingResult<ChainProductType> createBasicActionTypes() {
		logger.info("File: '{}'. Target instance: '{}'. Preparing for parsing.", FILE_WITH_BASIC_DATA, ChainProductType.class);
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		ParsingResult<ChainProductType> parsingResult = objectFactory.getInstance(ParsingResult.class, ChainProductType.class, FILE_WITH_BASIC_DATA);
		parsingResult.setStartTime();
		
		try (FileInputStream fileInputStream = new FileInputStream(FILE_WITH_BASIC_DATA)) {
			logger.info("File: '{}'. Target instance: '{}'. Starting process of parsing.", FILE_WITH_BASIC_DATA, ChainProductType.class);
		
			try (XSSFWorkbook book = new XSSFWorkbook(fileInputStream)) {
				XSSFSheet sheet = book.getSheet(SHEET_WITH_BASIC_ACTION_TYPES);
				
				if (sheet != null) {
					parsingResult.setSheetName(sheet.getSheetName());
					parsingResult.setSheetIndex(book.getSheetIndex(sheet));
					logger.info("File: '{}'. Sheet: '{}'. Target instance: '{}'. Starting working with sheet.", 
							FILE_WITH_BASIC_DATA, sheet.getSheetName(), ChainProductType.class);
					ActionTypeColumnMapper actionTypeColumnMapper = objectFactory.getInstance(ActionTypeColumnMapper.class);
					Iterator<Row> rowIterator = sheet.iterator();
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						actionTypeColumnMapper.mapColumn(cell.getStringCellValue(), cell.getColumnIndex());
					}
					
					logger.info("File: '{}'. Sheet: '{}'. Target instance: '{}'. {}", 
							FILE_WITH_BASIC_DATA, sheet.getSheetName(), ChainProductType.class, actionTypeColumnMapper.toString());
					
				
					while (rowIterator.hasNext()) {
						row = rowIterator.next();
						ChainProductType actionType = objectFactory.getInstance(ChainProductType.class);
						parsingResult.increaseTotalNumberOfInstances();
						
						if (actionTypeColumnMapper.isNameMapped()) {
							Cell actionTypeName = row.getCell(actionTypeColumnMapper.getNameColumnIndex());
							
							if ((actionTypeName != null) && 
									((actionTypeName.getCellType() == CellType.STRING) || (actionTypeName.getCellType() == CellType.BLANK))) {
								actionType.setName(actionTypeName.getStringCellValue().trim());
							}	
						}
						
						if (actionTypeColumnMapper.isTooltipMapped()) {
							Cell actionTypeTooltip = row.getCell(actionTypeColumnMapper.getTooltipColumnIndex());
							
							if ((actionTypeTooltip != null) && 
									((actionTypeTooltip.getCellType() == CellType.STRING) || (actionTypeTooltip.getCellType() == CellType.BLANK))) {
								actionType.setName(actionTypeTooltip.getStringCellValue().trim());
							}	
						}
						
						Set<ConstraintViolation<ChainProductType>> constraintViolations = validator.validate(actionType);
						
						if (constraintViolations.size() == 0) {
							if (actionTypeRepository.findByName(actionType.getName()).isPresent()) {
								logger.warn("File: '{}'. Sheet: '{}'. Row number '{}'. Target instance: '{}'. New instance has been already presented in the database.",
										FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), ChainProductType.class);
								logger.warn(actionType.toString());
								parsingResult.increaseNumberOfUnsavedInstances();
								parsingResult.increaseNumberOfAlreadyExistingInstances();
								parsingResult.addWarning(row.getRowNum(), "New instance has been already presented in the database." );
							} else {
								try {
									if (actionTypeRepository.save(actionType) != null) {
										logger.info("File: '{}'. Sheet: '{}'. Row number '{}'. Target instance: '{}'. New instance has been created and saved to the database.",
												FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), ChainProductType.class);
										logger.info(actionType.toString());
										parsingResult.increaseNumberOfSavedInstances();
									}
								} catch (Exception savingException) {
									logger.error("File: '{}'. Sheet: '{}'. Row number '{}'. Target instance: '{}'. Exception has occurred during the saving of new instance to the database: {}.", 
											FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), ChainProductType.class, printStackTrace(savingException));
									parsingResult.increaseNumberOfUnsavedInstances();
									parsingResult.addException(row.getRowNum(), savingException);
								}
							}	
						} else {
							logger.error("File: '{}'. Sheet: '{}'. Row number '{}'. Target instance: '{}'. Exception(s) has occurred during the validation of new instance. See validation exception(s) below.", 
									FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), ChainProductType.class);
							parsingResult.increaseNumberOfUnsavedInstances();
							parsingResult.increaseNumberOfInvalidInstances();
							
							for (ConstraintViolation<ChainProductType> violation : constraintViolations) {
								logger.error("Validation exception: {}", violation.getMessage());
								parsingResult.addConstraintViolation(row.getRowNum(), violation);
							}
						}
					}
					
					logger.info("File: '{}'. Sheet: '{}'. Target instance: '{}'. Finishing working with sheet.", 
							FILE_WITH_BASIC_DATA, sheet.getSheetName(), ChainProductType.class);
					logger.info("File: '{}'. Target instances: '{}'. Finishing process of parsing.", 
							FILE_WITH_BASIC_DATA, ChainProductType.class);
				} else {
					logger.warn("File: '{}'. Target instance: '{}'. Sheet with name '{}' wasn't found.", FILE_WITH_BASIC_DATA, ChainProductType.class, SHEET_WITH_BASIC_ACTION_TYPES);
					parsingResult.addCommonWarning("Sheet with name '" + SHEET_WITH_BASIC_ACTION_TYPES + "' wasn't found.");
				}
				
			} 		
		} catch (IOException ioException) {
			logger.error("File: '{}'. Target instance: '{}'. Input-output exception has occurred during opening the file: {}.", 
					FILE_WITH_BASIC_DATA, ChainProductType.class, printStackTrace(ioException));
			parsingResult.addCommonException(ioException);
		} 
		
		parsingResult.setFinishTime();
		logger.info("File: '{}'. Target instance: '{}'. Returning result of parsing. {}", 
				FILE_WITH_BASIC_DATA, ChainProductType.class, parsingResult.toString());
		return parsingResult;
	}
	
	public ParsingResult<Chain> createBasicChains() {
		logger.info("File: '{}'. Target instance: '{}'. Preparing for parsing.", FILE_WITH_BASIC_DATA, Chain.class);
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		ParsingResult<Chain> parsingResult = objectFactory.getInstance(ParsingResult.class, Chain.class, FILE_WITH_BASIC_DATA);
		parsingResult.setStartTime();
		
		try (FileInputStream fileInputStream = new FileInputStream(FILE_WITH_BASIC_DATA)) {
			logger.info("File: '{}'. Target instance: '{}'. Starting process of parsing.", FILE_WITH_BASIC_DATA, Chain.class);
		
			try (XSSFWorkbook book = new XSSFWorkbook(fileInputStream)) {
				XSSFSheet sheet = book.getSheet(SHEET_WITH_BASIC_CHAINS);
				
				if (sheet != null) {
					parsingResult.setSheetName(sheet.getSheetName());
					parsingResult.setSheetIndex(book.getSheetIndex(sheet));
					logger.info("File: '{}'. Sheet: '{}'. Target instance: '{}'. Starting working with sheet.", 
							FILE_WITH_BASIC_DATA, sheet.getSheetName(), Chain.class);
					ChainColumnMapper chainColumnMapper = objectFactory.getInstance(ChainColumnMapper.class);
					Iterator<Row> rowIterator = sheet.iterator();
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						chainColumnMapper.mapColumn(cell.getStringCellValue(), cell.getColumnIndex());
					}
					
					logger.info("File: '{}'. Sheet: '{}'. Target instance: '{}'. {}", 
							FILE_WITH_BASIC_DATA, sheet.getSheetName(), Chain.class, chainColumnMapper.toString());
					
				
					while (rowIterator.hasNext()) {
						row = rowIterator.next();
						Chain chain = objectFactory.getInstance(Chain.class);
						parsingResult.increaseTotalNumberOfInstances();
						
						if (chainColumnMapper.isNameMapped()) {
							Cell chainName = row.getCell(chainColumnMapper.getNameColumnIndex());
							
							if ((chainName != null) && 
									((chainName.getCellType() == CellType.STRING) || (chainName.getCellType() == CellType.BLANK))) {
								chain.setName(chainName.getStringCellValue().trim());
							}	
						}
						
						if (chainColumnMapper.isLinkMapped()) {
							Cell chainLink = row.getCell(chainColumnMapper.getLinkColumnIndex());
							
							if ((chainLink != null) && 
									((chainLink.getCellType() == CellType.STRING) || (chainLink.getCellType() == CellType.BLANK))) {
								chain.setLink(chainLink.getStringCellValue().trim());
							}	
						}
						
						if (chainColumnMapper.isLogoMapped()) {
							Cell chainLogo = row.getCell(chainColumnMapper.getLogoColumnIndex());
							
							if ((chainLogo != null) && 
									((chainLogo.getCellType() == CellType.STRING) || (chainLogo.getCellType() == CellType.BLANK))) {
								chain.setLogo(chainLogo.getStringCellValue().trim());
							}	
						}
						
						if (chainColumnMapper.isActiveMapped()) {
							Cell chainIsActive = row.getCell(chainColumnMapper.getIsActiveColumnIndex());
							
							if ((chainIsActive != null) && 
									((chainIsActive.getCellType() == CellType.STRING) || (chainIsActive.getCellType() == CellType.BLANK))) {
								
								if (Arrays.stream(IS_ACTIVE).anyMatch(chainIsActive.getStringCellValue().trim()::equals)) {
									chain.setIsActive(true);
								} else if (Arrays.stream(IS_NOT_ACTIVE).anyMatch(chainIsActive.getStringCellValue().trim()::equals)) {
									chain.setIsActive(false);
								}
							}	
						}
						
						if (chainColumnMapper.isSynonymMapped()) {
							Cell chainSynonym = row.getCell(chainColumnMapper.getSynonymColumnIndex());
							
							if ((chainSynonym != null) && 
									((chainSynonym.getCellType() == CellType.STRING) || (chainSynonym.getCellType() == CellType.BLANK))) {
								chain.setSynonym(chainSynonym.getStringCellValue().trim());
							}	
						}
						
						Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
						
						if (constraintViolations.size() == 0) {
							if (chainRepository.findByName(chain.getName()).isPresent()) {
								logger.warn("File: '{}'. Sheet: '{}'. Row number '{}'. Target instance: '{}'. New instance has been already presented in the database.",
										FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Chain.class);
								logger.warn(chain.toString());
								parsingResult.increaseNumberOfUnsavedInstances();
								parsingResult.increaseNumberOfAlreadyExistingInstances();
								parsingResult.addWarning(row.getRowNum(), "New instance has been already presented in the database." );
							} else {
								try {
									if (chainRepository.save(chain) != null) {
										logger.info("File: '{}'. Sheet: '{}'. Row number '{}'. Target instance: '{}'. New instance has been created and saved to the database.",
												FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Chain.class);
										logger.info(chain.toString());
										parsingResult.increaseNumberOfSavedInstances();
									}
								} catch (Exception savingException) {
									logger.error("File: '{}'. Sheet: '{}'. Row number '{}'. Target instance: '{}'. Exception has occurred during the saving of new instance to the database: {}.", 
											FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Chain.class, printStackTrace(savingException));
									parsingResult.increaseNumberOfUnsavedInstances();
									parsingResult.addException(row.getRowNum(), savingException);
								}
							}	
						} else {
							logger.error("File: '{}'. Sheet: '{}'. Row number '{}'. Target instance: '{}'. Exception(s) has occurred during the validation of new instance. See validation exception(s) below.", 
									FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Chain.class);
							parsingResult.increaseNumberOfUnsavedInstances();
							parsingResult.increaseNumberOfInvalidInstances();
							
							for (ConstraintViolation<Chain> violation : constraintViolations) {
								logger.error("Validation exception: {}", violation.getMessage());
								parsingResult.addConstraintViolation(row.getRowNum(), violation);
							}
						}
					}
					
					logger.info("File: '{}'. Sheet: '{}'. Target instance: '{}'. Finishing working with sheet.", 
							FILE_WITH_BASIC_DATA, sheet.getSheetName(), Chain.class);
					logger.info("File: '{}'. Target instance: '{}'. Finishing process of parsing.", 
							FILE_WITH_BASIC_DATA, Chain.class);
				} else {
					logger.warn("File: '{}'. Target instance: '{}'. Sheet with name '{}' wasn't found.", FILE_WITH_BASIC_DATA, Chain.class, SHEET_WITH_BASIC_CHAINS);
					parsingResult.addCommonWarning("Sheet with name '" + SHEET_WITH_BASIC_CHAINS + "' wasn't found.");
				}				
			} 		
		} catch (IOException ioException) {
			logger.error("File: '{}'. Target instance: '{}'. Input-output exception has occurred during opening the file: {}.", 
					FILE_WITH_BASIC_DATA, Chain.class, printStackTrace(ioException));
			parsingResult.addCommonException(ioException);
		} 
		
		parsingResult.setFinishTime();
		logger.info("File: '{}'. Target instance: '{}'. Returning result of parsing. {}", 
				FILE_WITH_BASIC_DATA, Chain.class, parsingResult.toString());
		return parsingResult;
	}
	
	public ParsingResult<Category> createBasicCategories() {
		logger.info("File: '{}'. Target instance: '{}'. Preparing for parsing.", FILE_WITH_BASIC_DATA, Category.class);
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		ParsingResult<Category> parsingResult = objectFactory.getInstance(ParsingResult.class, Category.class, FILE_WITH_BASIC_DATA);
		parsingResult.setStartTime();
		
		try (FileInputStream fileInputStream = new FileInputStream(FILE_WITH_BASIC_DATA)) {
			logger.info("File: '{}'. Target instance: '{}'. Starting process of parsing.", FILE_WITH_BASIC_DATA, Category.class);
		
			try (XSSFWorkbook book = new XSSFWorkbook(fileInputStream)) {
				XSSFSheet sheet = book.getSheet(SHEET_WITH_BASIC_CATEGORIES);
				
				if (sheet != null) {
					parsingResult.setSheetName(sheet.getSheetName());
					parsingResult.setSheetIndex(book.getSheetIndex(sheet));
					logger.info("File: '{}'. Sheet: '{}'. Target instance: '{}'. Starting working with sheet.", 
							FILE_WITH_BASIC_DATA, sheet.getSheetName(), Category.class);
					CategoryColumnMapper categoryColumnMapper = objectFactory.getInstance(CategoryColumnMapper.class);
					Iterator<Row> rowIterator = sheet.iterator();
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						categoryColumnMapper.mapColumn(cell.getStringCellValue(), cell.getColumnIndex());
					}
					
					logger.info("File: '{}'. Sheet: '{}'. Target instance: '{}'. {}", 
							FILE_WITH_BASIC_DATA, sheet.getSheetName(), Category.class, categoryColumnMapper.toString());
					
				
					while (rowIterator.hasNext()) {
						row = rowIterator.next();
						Category category = objectFactory.getInstance(Category.class);
						parsingResult.increaseTotalNumberOfInstances();
						
						if (categoryColumnMapper.isNameMapped()) {
							Cell categoryName = row.getCell(categoryColumnMapper.getNameColumnIndex());
							
							if ((categoryName != null) && 
									((categoryName.getCellType() == CellType.STRING) || (categoryName.getCellType() == CellType.BLANK))) {
								category.setName(categoryName.getStringCellValue().trim());
							}	
						}
						
						if (categoryColumnMapper.isIconMapped()) {
							Cell categoryIcon = row.getCell(categoryColumnMapper.getIconColumnIndex());
							
							if ((categoryIcon != null) && 
									((categoryIcon.getCellType() == CellType.STRING) || (categoryIcon.getCellType() == CellType.BLANK))) {
								category.setIcon(categoryIcon.getStringCellValue().trim());
							}	
						}
						
						if (categoryColumnMapper.isPriorityMapped()) {
							Cell categoryPriority = row.getCell(categoryColumnMapper.getPriorityColumnIndex());
							
							if ((categoryPriority != null) && (categoryPriority.getCellType() == CellType.NUMERIC)) {
								category.setPriority((int) categoryPriority.getNumericCellValue());
							}	
						}
						
						if (categoryColumnMapper.isActiveMapped()) {
							Cell categoryIsActive = row.getCell(categoryColumnMapper.getIsActiveColumnIndex());
							
							if ((categoryIsActive != null) && 
									((categoryIsActive.getCellType() == CellType.STRING) || (categoryIsActive.getCellType() == CellType.BLANK))) {
								
								if (Arrays.stream(IS_ACTIVE).anyMatch(categoryIsActive.getStringCellValue().trim()::equals)) {
									category.setIsActive(true);
								} else if (Arrays.stream(IS_NOT_ACTIVE).anyMatch(categoryIsActive.getStringCellValue().trim()::equals)) {
									category.setIsActive(false);
								}
							}	
						}
						
						Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);
						
						if (constraintViolations.size() == 0) {
							if (categoryRepository.findByName(category.getName()).isPresent()) {
								logger.warn("File: '{}'. Sheet: '{}'. Row number '{}'. Target instance: '{}'. New instance has been already presented in the database.",
										FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Category.class);
								logger.warn(category.toString());
								parsingResult.increaseNumberOfUnsavedInstances();
								parsingResult.increaseNumberOfAlreadyExistingInstances();
								parsingResult.addWarning(row.getRowNum(), "New instance has been already presented in the database." );
							} else {
								try {
									if (categoryRepository.save(category) != null) {
										logger.info("File: '{}'. Sheet: '{}'. Row number '{}'. Target instance: '{}'. New instance has been created and saved to the database.",
												FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Category.class);
										logger.info(category.toString());
										parsingResult.increaseNumberOfSavedInstances();
									}
								} catch (Exception savingException) {
									logger.error("File: '{}'. Sheet: '{}'. Row number '{}'. Target instance: '{}'. Exception has occurred during the saving of new instance to the database: {}.", 
											FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Category.class, printStackTrace(savingException));
									parsingResult.increaseNumberOfUnsavedInstances();
									parsingResult.addException(row.getRowNum(), savingException);
								}
							}	
						} else {
							logger.error("File: '{}'. Sheet: '{}'. Row number '{}'. Target instance: '{}'. Exception(s) has occurred during the validation of new instance. See validation exception(s) below.", 
									FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Category.class);
							parsingResult.increaseNumberOfUnsavedInstances();
							parsingResult.increaseNumberOfInvalidInstances();
							
							for (ConstraintViolation<Category> violation : constraintViolations) {
								logger.error("Validation exception: {}", violation.getMessage());
								parsingResult.addConstraintViolation(row.getRowNum(), violation);
							}
						}
					}
					
					logger.info("File: '{}'. Sheet: '{}'. Target instance: '{}'. Finishing working with sheet.", 
							FILE_WITH_BASIC_DATA, sheet.getSheetName(), Category.class);
					logger.info("File: '{}'. Target instance: '{}'. Finishing process of parsing.", 
							FILE_WITH_BASIC_DATA, Category.class);
				} else {
					logger.warn("File: '{}'. Target instance: '{}'. Sheet with name '{}' wasn't found.", FILE_WITH_BASIC_DATA, Category.class, SHEET_WITH_BASIC_CATEGORIES);
					parsingResult.addCommonWarning("Sheet with name '" + SHEET_WITH_BASIC_CATEGORIES + "' wasn't found.");
				}				
			} 		
		} catch (IOException ioException) {
			logger.error("File: '{}'. Target instance: '{}'. Input-output exception has occurred during opening the file: {}.", 
					FILE_WITH_BASIC_DATA, Category.class, printStackTrace(ioException));
			parsingResult.addCommonException(ioException);
		} 
		
		parsingResult.setFinishTime();
		logger.info("File: '{}'. Target instance: '{}'. Returning result of parsing. {}", 
				FILE_WITH_BASIC_DATA, Category.class, parsingResult.toString());
		return parsingResult;
	}
	
	public ParsingResult<Subcategory> createBasicSubcategories() {
		logger.info("File: '{}'. Target instance: '{}'. Preparing for parsing.", FILE_WITH_BASIC_DATA, Subcategory.class);
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		ParsingResult<Subcategory> parsingResult = objectFactory.getInstance(ParsingResult.class, Subcategory.class, FILE_WITH_BASIC_DATA);
		parsingResult.setStartTime();
		
		try (FileInputStream fileInputStream = new FileInputStream(FILE_WITH_BASIC_DATA)) {
			logger.info("File: '{}'. Target instance: '{}'. Starting process of parsing.", FILE_WITH_BASIC_DATA, Subcategory.class);
		
			try (XSSFWorkbook book = new XSSFWorkbook(fileInputStream)) {
				XSSFSheet sheet = book.getSheet(SHEET_WITH_BASIC_SUBCATEGORIES);
				
				if (sheet != null) {
					parsingResult.setSheetName(sheet.getSheetName());
					parsingResult.setSheetIndex(book.getSheetIndex(sheet));
					logger.info("File: '{}'. Sheet: '{}'. Target instance: '{}'. Starting working with sheet.", 
							FILE_WITH_BASIC_DATA, sheet.getSheetName(), Subcategory.class);
					SubcategoryColumnMapper subcategoryColumnMapper = objectFactory.getInstance(SubcategoryColumnMapper.class);
					Iterator<Row> rowIterator = sheet.iterator();
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						subcategoryColumnMapper.mapColumn(cell.getStringCellValue(), cell.getColumnIndex());
					}
					
					logger.info("File: '{}'. Sheet: '{}'. Target instance: '{}'. {}", 
							FILE_WITH_BASIC_DATA, sheet.getSheetName(), Subcategory.class, subcategoryColumnMapper.toString());
					
				
					while (rowIterator.hasNext()) {
						row = rowIterator.next();
						Subcategory subcategory = objectFactory.getInstance(Subcategory.class);
						parsingResult.increaseTotalNumberOfInstances();
						
						if (subcategoryColumnMapper.isNameMapped()) {
							Cell subcategoryName = row.getCell(subcategoryColumnMapper.getNameColumnIndex());
							
							if ((subcategoryName != null) && 
									((subcategoryName.getCellType() == CellType.STRING) || (subcategoryName.getCellType() == CellType.BLANK))) {
								subcategory.setName(subcategoryName.getStringCellValue().trim());
							}	
						}
						
						if (subcategoryColumnMapper.isCategoryMapped()) {
							Cell categoryName = row.getCell(subcategoryColumnMapper.getCategoryColumnIndex());
							
							if ((categoryName != null) && 
									((categoryName.getCellType() == CellType.STRING) || (categoryName.getCellType() == CellType.BLANK))) {
								Optional<Category> category = categoryRepository.findByName(categoryName.getStringCellValue().trim());
								
								if (category.isPresent()) {
									subcategory.setCategory(category.get());
								} else {
									logger.warn("File: '{}'. Sheet: '{}'. Row number '{}' Target instance: '{}'. Reference on instance of '{}' with name '{}' hasn't been found in the database.", 
											FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Subcategory.class, Category.class, categoryName.getStringCellValue().trim());
									parsingResult.addWarning(row.getRowNum(), "Reference on instance of '" + Category.class + "' with name '" + categoryName.getStringCellValue().trim() + "' hasn't been found in the database.");
								}
							}	
						}
						
						if (subcategoryColumnMapper.isPriorityMapped()) {
							Cell subcategoryPriority = row.getCell(subcategoryColumnMapper.getPriorityColumnIndex());
							
							if ((subcategoryPriority != null) && (subcategoryPriority.getCellType() == CellType.NUMERIC)) {
								subcategory.setPriority((int) subcategoryPriority.getNumericCellValue());
							}	
						}
						
						if (subcategoryColumnMapper.isActiveMapped()) {
							Cell subcategoryIsActive = row.getCell(subcategoryColumnMapper.getIsActiveColumnIndex());
							
							if ((subcategoryIsActive != null) && 
									((subcategoryIsActive.getCellType() == CellType.STRING) || (subcategoryIsActive.getCellType() == CellType.BLANK))) {
								
								if (Arrays.stream(IS_ACTIVE).anyMatch(subcategoryIsActive.getStringCellValue().trim()::equals)) {
									subcategory.setIsActive(true);
								} else if (Arrays.stream(IS_NOT_ACTIVE).anyMatch(subcategoryIsActive.getStringCellValue().trim()::equals)) {
									subcategory.setIsActive(false);
								}
							}	
						}
						
						Set<ConstraintViolation<Subcategory>> constraintViolations = validator.validate(subcategory);
						
						if (constraintViolations.size() == 0) {
							if (subcategoryRepository.findByNameAndCategoryName(subcategory.getName(), subcategory.getCategory().getName()).isPresent()) {
								logger.warn("File: '{}'. Sheet: '{}'. Row number '{}'. Target instance: '{}'. New instance has been already presented in the database.",
										FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Subcategory.class);
								logger.warn(subcategory.toString());
								parsingResult.increaseNumberOfUnsavedInstances();
								parsingResult.increaseNumberOfAlreadyExistingInstances();
								parsingResult.addWarning(row.getRowNum(), "New instance has been already presented in the database." );
							} else {
								try {
									if (subcategoryRepository.save(subcategory) != null) {
										logger.info("File: '{}'. Sheet: '{}'. Row number '{}'. Target instance: '{}'. New instance has been created and saved to the database.",
												FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Subcategory.class);
										logger.info(subcategory.toString());
										parsingResult.increaseNumberOfSavedInstances();
									}
								} catch (Exception savingException) {
									logger.error("File: '{}'. Sheet: '{}'. Row number '{}'. Target instance: '{}'. Exception has occurred during the saving of new instance to the database: {}.", 
											FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Subcategory.class, printStackTrace(savingException));
									parsingResult.increaseNumberOfUnsavedInstances();
									parsingResult.addException(row.getRowNum(), savingException);
								}
							}	
						} else {
							logger.error("File: '{}'. Sheet: '{}'. Row number '{}'. Target instance: '{}'. Exception(s) has occurred during the validation of new instance. See validation exception(s) below.", 
									FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Subcategory.class);
							parsingResult.increaseNumberOfUnsavedInstances();
							parsingResult.increaseNumberOfInvalidInstances();
							
							for (ConstraintViolation<Subcategory> violation : constraintViolations) {
								logger.error("Validation exception: {}", violation.getMessage());
								parsingResult.addConstraintViolation(row.getRowNum(), violation);
							}
						}
					}
					
					logger.info("File: '{}'. Sheet: '{}'. Target instance: '{}'. Finishing working with sheet.", 
							FILE_WITH_BASIC_DATA, sheet.getSheetName(), Subcategory.class);
					logger.info("File: '{}'. Target instance: '{}'. Finishing process of parsing.", 
							FILE_WITH_BASIC_DATA, Subcategory.class);
				} else {
					logger.warn("File: '{}'. Target instance: '{}'. Sheet with name '{}' wasn't found.", FILE_WITH_BASIC_DATA, Subcategory.class, SHEET_WITH_BASIC_SUBCATEGORIES);
					parsingResult.addCommonWarning("Sheet with name '" + SHEET_WITH_BASIC_SUBCATEGORIES + "' wasn't found.");
				}				
			} 		
		} catch (IOException ioException) {
			logger.error("File: '{}'. Target instance: '{}'. Input-output exception has occurred during opening the file: {}.", 
					FILE_WITH_BASIC_DATA, Subcategory.class, printStackTrace(ioException));
			parsingResult.addCommonException(ioException);
		} 
		
		parsingResult.setFinishTime();
		logger.info("File: '{}'. Target instance: '{}'. Returning result of parsing. {}", 
				FILE_WITH_BASIC_DATA, Subcategory.class, parsingResult.toString());
		return parsingResult;
	}
	
	public ParsingResult<Product> parseProducts(String file) {
		logger.info("File: '{}'. Target instance: '{}'. Preparing for parsing.", file, Product.class);
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		ParsingResult<Product> parsingResult = objectFactory.getInstance(ParsingResult.class, Product.class, file);
		parsingResult.setStartTime();
		
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			logger.info("File: '{}'. Target instance: '{}'. Starting process of parsing.", file, Product.class);
		
			try (XSSFWorkbook book = new XSSFWorkbook(fileInputStream)) {
				XSSFSheet sheet = book.getSheetAt(0);
				
				if (sheet != null) {
					parsingResult.setSheetName(sheet.getSheetName());
					parsingResult.setSheetIndex(book.getSheetIndex(sheet));
					logger.info("File: '{}'. Sheet: '{}'. Target instance: '{}'. Starting working with sheet.", 
							file, sheet.getSheetName(), Product.class);
					ProductColumnMapper productColumnMapper = objectFactory.getInstance(ProductColumnMapper.class);
					Iterator<Row> rowIterator = sheet.iterator();
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						productColumnMapper.mapColumn(cell.getStringCellValue(), cell.getColumnIndex());
					}
					
					logger.info("File: '{}'. Sheet: '{}'. Target instance: '{}'. {}", 
							file, sheet.getSheetName(), Product.class, productColumnMapper.toString());
				
					while (rowIterator.hasNext()) {
						row = rowIterator.next();
						Product product = objectFactory.getInstance(Product.class);
						parsingResult.increaseTotalNumberOfInstances();
						
						if (productColumnMapper.isNameMapped()) {
							Cell productName = row.getCell(productColumnMapper.getNameColumnIndex());
							
							if ((productName != null) && 
									((productName.getCellType() == CellType.STRING) || (productName.getCellType() == CellType.BLANK))) {
								product.setName(productName.getStringCellValue().trim());
							}	
						}
						
						if (productColumnMapper.isBarcodeMapped()) {
							Cell productBarcode = row.getCell(productColumnMapper.getBarcodeColumnIndex());
							
							if ((productBarcode != null) && (productBarcode.getCellType() == CellType.NUMERIC)) {
								Formatter formatter = new Formatter();
								product.setBarcode(formatter.format("%.0f", productBarcode.getNumericCellValue()).toString());
								formatter.close();
							}
						}
						
						if (productColumnMapper.isUnitMapped()) {
							Cell productUnit = row.getCell(productColumnMapper.getUnitColumnIndex());
							/*
							if ((productUnit != null) && 
									((productUnit.getCellType() == CellType.STRING) || (productUnit.getCellType() == CellType.BLANK))) {
								Optional<Unit> unit = Unit.getByRepresentation(productUnit.getStringCellValue().trim());
								
								if (unit.isPresent()) {
									product.setUnit(unit.get());
								} else {
									logger.warn("File: '{}'. Sheet: '{}'. Row number '{}' Target instance: '{}'. Reference on instance of '{}' with name '{}' hasn't been found in the inner sources.", 
											file, sheet.getSheetName(), row.getRowNum(), Product.class, Unit.class, productUnit.getStringCellValue().trim());
									parsingResult.addWarning(row.getRowNum(), "Reference on instance of '" + Unit.class + "' with name '" + productUnit.getStringCellValue().trim() + "' hasn't been found in the inner sources.");
								}
							}*/	
						}
						
						if (productColumnMapper.isManufacturerMapped()) {
							Cell productManufacturer = row.getCell(productColumnMapper.getManufacturerColumnIndex());
							
							if ((productManufacturer != null) && 
									((productManufacturer.getCellType() == CellType.STRING) || (productManufacturer.getCellType() == CellType.BLANK))) {
								product.setManufacturer(productManufacturer.getStringCellValue().trim());
							}	
						}
						
						if (productColumnMapper.isBrandMapped()) {
							Cell productBrand = row.getCell(productColumnMapper.getBrandColumnIndex());
							
							if ((productBrand != null) && 
									((productBrand.getCellType() == CellType.STRING) || (productBrand.getCellType() == CellType.BLANK))) {
								product.setBrand(productBrand.getStringCellValue().trim());
							}	
						}
						
						if (productColumnMapper.isCountryOfOriginMapped()) {
							Cell productCountryOfOrigin = row.getCell(productColumnMapper.getCountryOfOriginColumnIndex());
							
							if ((productCountryOfOrigin != null) && 
									((productCountryOfOrigin.getCellType() == CellType.STRING) || (productCountryOfOrigin.getCellType() == CellType.BLANK))) {
								Optional<Country> countryOfOrigin = countryRepository.findByAlphaCode2(productCountryOfOrigin.getStringCellValue().trim());
								
								if (countryOfOrigin.isPresent()) {
									product.setCountryOfOrigin(countryOfOrigin.get());
								} else {
									logger.warn("File: '{}'. Sheet: '{}'. Row number '{}' Target instance: '{}'. Reference on instance of '{}' with code '{}' hasn't been found in the database.", 
											file, sheet.getSheetName(), row.getRowNum(), Product.class, Country.class, productCountryOfOrigin.getStringCellValue().trim());
									parsingResult.addWarning(row.getRowNum(), "Reference on instance of '" + Country.class + "' with name '" + productCountryOfOrigin.getStringCellValue().trim() + "' hasn't been found in the database.");
								}
							}	
						}
						
						Optional<Subcategory> subcategory = subcategoryRepository.findByNameAndCategoryName(INDEFINITE_SUBCATEGORY, INDEFINITE_CATEGORY);
						
						if (subcategory.isPresent()) {
							product.setSubcategory(subcategory.get());
						} else {
							logger.warn("File: '{}'. Sheet: '{}'. Row number '{}' Target instance: '{}'. Reference on instance of '{}' with name '{}' hasn't been found in the database.", 
									file, sheet.getSheetName(), row.getRowNum(), Product.class, Subcategory.class, INDEFINITE_SUBCATEGORY);
							parsingResult.addWarning(row.getRowNum(), "Reference on instance of '" + Subcategory.class + "' with name '" + INDEFINITE_SUBCATEGORY + "' hasn't been found in the database.");
						}
						
						product.setIsActive(true);
						
						Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
						
						if (constraintViolations.size() == 0) {/*
							if (productRepository.findByNameAndBarcodeAndUnit(product.getName(), product.getBarcode(), product.getUnit()).isPresent()) {
								logger.warn("File: '{}'. Sheet: '{}'. Row number '{}'. Target instance: '{}'. New instance has been already presented in the database.",
										file, sheet.getSheetName(), row.getRowNum(), Product.class);
								logger.warn(product.toString());
								parsingResult.increaseNumberOfUnsavedInstances();
								parsingResult.increaseNumberOfAlreadyExistingInstances();
								parsingResult.addWarning(row.getRowNum(), "New instance has been already presented in the database." );
							} else {
								try {
									if (productRepository.save(product) != null) {
										logger.info("File: '{}'. Sheet: '{}'. Row number '{}'. Target instance: '{}'. New instance has been created and saved to the database.",
												file, sheet.getSheetName(), row.getRowNum(), Product.class);
										logger.info(product.toString());
										parsingResult.increaseNumberOfSavedInstances();
									}
								} catch (Exception savingException) {
									logger.error("File: '{}'. Sheet: '{}'. Row number '{}'. Target instance: '{}'. Exception has occurred during the saving of new instance to the database: {}.", 
											file, sheet.getSheetName(), row.getRowNum(), Product.class, printStackTrace(savingException));
									parsingResult.increaseNumberOfUnsavedInstances();
									parsingResult.addException(row.getRowNum(), savingException);
								}
							}*/	
						} else {
							logger.error("File: '{}'. Sheet: '{}'. Row number '{}'. Target instance: '{}'. Exception(s) has occurred during the validation of new instance. See validation exception(s) below.", 
									file, sheet.getSheetName(), row.getRowNum(), Product.class);
							parsingResult.increaseNumberOfUnsavedInstances();
							parsingResult.increaseNumberOfInvalidInstances();
							
							for (ConstraintViolation<Product> violation : constraintViolations) {
								logger.error("Validation exception: {}", violation.getMessage());
								parsingResult.addConstraintViolation(row.getRowNum(), violation);
							}
						}
					}
					
					logger.info("File: '{}'. Sheet: '{}'. Target instance: '{}'. Finishing working with sheet.", 
							FILE_WITH_BASIC_DATA, sheet.getSheetName(), Subcategory.class);
					logger.info("File: '{}'. Target instance: '{}'. Finishing process of parsing.", 
							FILE_WITH_BASIC_DATA, Product.class);
				} else {
					logger.warn("File: '{}'. Target instance: '{}'. Sheet at index '{}' wasn't found.", file, Product.class, 0);
					parsingResult.addCommonWarning("Sheet at index '" + 0 + "' wasn't found.");
				}				
			} 		
		} catch (IOException ioException) {
			logger.error("File: '{}'. Target instance: '{}'. Input-output exception has occurred during opening the file: {}.", 
					file, Product.class, printStackTrace(ioException));
			parsingResult.addCommonException(ioException);
		} 
		
		parsingResult.setFinishTime();
		logger.info("File: '{}'. Target instance: '{}'. Returning result of parsing. {}", 
				file, Product.class, parsingResult.toString());
		return parsingResult;
	}
	
	public ParsingResult<ChainProduct> parseActions(String file, String chainSynonym) {
		logger.info("File: '{}'. Target instance: '{}'. Chain synonym: '{}'. Preparing for parsing.", file, ChainProduct.class, chainSynonym);
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		ParsingResult<ChainProduct> parsingResult = objectFactory.getInstance(ParsingResult.class, ChainProduct.class, file);
		parsingResult.setStartTime();
		
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			logger.info("File: '{}'. Target instance: '{}'. Chain synonym: '{}'. Starting process of parsing.", file, ChainProduct.class, chainSynonym);
		
			try (XSSFWorkbook book = new XSSFWorkbook(fileInputStream)) {
				XSSFSheet sheet = book.getSheetAt(0);
				
				if (sheet != null) {
					parsingResult.setSheetName(sheet.getSheetName());
					parsingResult.setSheetIndex(book.getSheetIndex(sheet));
					logger.info("File: '{}'. Sheet: '{}'. Target instance: '{}'. Chain synonym: '{}'. Starting working with sheet.", 
							file, sheet.getSheetName(), ChainProduct.class, chainSynonym);
					ActionColumnMapper actionColumnMapper = objectFactory.getInstance(ActionColumnMapper.class);
					Iterator<Row> rowIterator = sheet.iterator();
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						actionColumnMapper.mapColumn(cell.getStringCellValue(), cell.getColumnIndex());
					}
					
					logger.info("File: '{}'. Sheet: '{}'. Target instance: '{}'. Chain synonym: '{}'. {}", 
							file, sheet.getSheetName(), ChainProduct.class, chainSynonym, actionColumnMapper.toString());
				
					while (rowIterator.hasNext()) {
						row = rowIterator.next();
						ChainProduct action = objectFactory.getInstance(ChainProduct.class);
						parsingResult.increaseTotalNumberOfInstances(); 
						
						if (actionColumnMapper.isBasePriceMapped()) {
							Cell actionBasePrice = row.getCell(actionColumnMapper.getBasePriceColumnIndex());
							
							if ((actionBasePrice != null) && (actionBasePrice.getCellType() == CellType.NUMERIC)) {
								action.setBasePrice(new BigDecimal(actionBasePrice.getNumericCellValue()).setScale(2, RoundingMode.HALF_UP));
							}	
						}
						
						if (actionColumnMapper.isDiscountPriceMapped()) {
							Cell actionDiscountPrice = row.getCell(actionColumnMapper.getDiscountPriceColumnIndex());
							
							if ((actionDiscountPrice != null) && (actionDiscountPrice.getCellType() == CellType.NUMERIC)) {
								action.setDiscountPrice(new BigDecimal(actionDiscountPrice.getNumericCellValue()).setScale(2, RoundingMode.HALF_UP));
							}	
						}
						
						if ((action.getBasePrice() != null) && (action.getDiscountPrice() != null)) {
							BigDecimal difference = action.getBasePrice().subtract(action.getDiscountPrice());
							BigDecimal discount = difference.divide(action.getBasePrice(), new MathContext(10, RoundingMode.HALF_UP));
							BigDecimal discountPercent = discount.scaleByPowerOfTen(2).setScale(0, RoundingMode.HALF_UP);
							action.setDiscountPercent(discountPercent);
						}
						
						if (actionColumnMapper.isStartDateMapped()) {
							Cell actionStartDate = row.getCell(actionColumnMapper.getStartDateColumnIndex());
							
							if ((actionStartDate != null) && (actionStartDate.getCellType() == CellType.NUMERIC)) {
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(actionStartDate.getDateCellValue());
								action.setStartDate(calendar);
							}
						}
						
						if (actionColumnMapper.isEndDateMapped()) {
							Cell actionEndDate = row.getCell(actionColumnMapper.getEndDateColumnIndex());
							
							if ((actionEndDate != null) && (actionEndDate.getCellType() == CellType.NUMERIC)) {
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(actionEndDate.getDateCellValue());
								action.setEndDate(calendar);
							}
						}
						
						Optional<Chain> chain = chainRepository.findBySynonym(chainSynonym);
						
						if (chain.isPresent()) {
							action.setChain(chain.get());
						} else {
							logger.warn("File: '{}'. Sheet: '{}'. Row number '{}' Target instance: '{}'. Chain synonym: '{}'. Reference on instance of '{}' with synonym '{}' hasn't been found in the database.", 
									file, sheet.getSheetName(), row.getRowNum(), ChainProduct.class, chainSynonym, Chain.class, chainSynonym);
							parsingResult.addWarning(row.getRowNum(), "Reference on instance of '" + Chain.class + "' with synonym '" + chainSynonym + "' hasn't been found in the database.");
						}
						
						if (actionColumnMapper.isActionTypeMapped()) {
							Cell actionType = row.getCell(actionColumnMapper.getActionTypeColumnIndex());
							
							if ((actionType != null) && 
									((actionType.getCellType() == CellType.STRING) || (actionType.getCellType() == CellType.BLANK))) {
								String actionTypeSynonym = actionType.getStringCellValue().trim();
								String actionTypeName = null;
								
								if ((actionTypeSynonym.equals(ACTION_TYPE_DISCOUNT_SYNONYM)) && (action.getBasePrice() != null) && (action.getDiscountPrice() != null)) {
									actionTypeName = ACTION_TYPE_DISCOUNT_NAME;
								} else if (actionTypeSynonym.equals(ACTION_TYPE_ONE_PLUS_ONE_SYNONYM)) {
									actionTypeName = ACTION_TYPE_ONE_PLUS_ONE_NAME;
								} else if (action.getBasePrice() != null) {
									actionTypeName = ACTION_TYPE_NICE_PRICE_NAME;
								}
								
								Optional<ChainProductType> type = actionTypeRepository.findByName(actionTypeName);
								
								if (type.isPresent()) {
									action.setType(type.get());
								} else {
									logger.warn("File: '{}'. Sheet: '{}'. Row number '{}' Target instance: '{}'. Chain synonym: '{}'. Reference on instance of '{}' with synonym '{}' hasn't been found in the database.", 
											file, sheet.getSheetName(), row.getRowNum(), ChainProduct.class, chainSynonym, ChainProductType.class, actionTypeSynonym);
									parsingResult.addWarning(row.getRowNum(), "Reference on instance of '" + ChainProductType.class + "' with synonym '" + actionTypeSynonym + "' hasn't been found in the database.");
								}
							}
						}
						
						String actionProductNameValue = null;
						String actionProductBarcodeValue = null;
						//Unit unitValue = null;
						
						if (actionColumnMapper.isProductNameMapped()) {
							Cell actionProductName = row.getCell(actionColumnMapper.getProductNameColumnIndex());
							
							if ((actionProductName != null) && 
									((actionProductName.getCellType() == CellType.STRING) || (actionProductName.getCellType() == CellType.BLANK))) {
								actionProductNameValue = actionProductName.getStringCellValue().trim();
							}	
						}
						
						if (actionColumnMapper.isProductBarcodeMapped()) {
							Cell actionProductBarcode = row.getCell(actionColumnMapper.getProductBarcodeColumnIndex());
							
							if ((actionProductBarcode != null) && (actionProductBarcode.getCellType() == CellType.NUMERIC)) {
								Formatter formatter = new Formatter();
								actionProductBarcodeValue = formatter.format("%.0f", actionProductBarcode.getNumericCellValue()).toString();
								formatter.close();
							}
						}
						
						if (actionColumnMapper.isProductUnitMapped()) {
							Cell actionProductUnit = row.getCell(actionColumnMapper.getProductUnitColumnIndex());
							
							/*if ((actionProductUnit != null) && 
									((actionProductUnit.getCellType() == CellType.STRING) || (actionProductUnit.getCellType() == CellType.BLANK))) {
								Optional<Unit> unit = Unit.getByRepresentation(actionProductUnit.getStringCellValue().trim());
								
								if (unit.isPresent()) {
									unitValue = unit.get();
								} else {
									logger.warn("File: '{}'. Sheet: '{}'. Row number '{}' Target instance: '{}'. Chain synonym: '{}'. Reference on instance of '{}' with name '{}' hasn't been found in the inner sources.", 
											file, sheet.getSheetName(), row.getRowNum(), Product.class, chainSynonym, Unit.class, actionProductUnit.getStringCellValue().trim());
									parsingResult.addWarning(row.getRowNum(), "Reference on instance of '" + Unit.class + "' with name '" + actionProductUnit.getStringCellValue().trim() + "' hasn't been found in the inner sources.");
								}
							}*/
						}
						
						//Optional<Product> product = productRepository.findByNameAndBarcodeAndUnit(actionProductNameValue, actionProductBarcodeValue, unitValue);
						/*
						if (product.isPresent()) {
							action.setProduct(product.get());
						} else {
							logger.warn("File: '{}'. Sheet: '{}'. Row number '{}' Target instance: '{}'. Chain synonym: '{}'. Reference on instance of '{}' with name '{}', barcode '{}' and unit '{}' hasn't been found in the database.", 
									file, sheet.getSheetName(), row.getRowNum(), ChainProduct.class, chainSynonym, Product.class, actionProductNameValue, actionProductBarcodeValue, unitValue);
							parsingResult.addWarning(row.getRowNum(), "Reference on instance of '" + Product.class + "' with name '" + actionProductNameValue + "', barcode '" + actionProductBarcodeValue + "' and unit '" + unitValue + "' hasn't been found in the database.");
						}
						*/
						Set<ConstraintViolation<ChainProduct>> constraintViolations = validator.validate(action);
						
						if (constraintViolations.size() == 0) {
							if (actionRepository.findByStartDateAndEndDateAndBasePriceAndDiscountPriceAndTypeIdAndChainIdAndProductId(
									action.getStartDate(), 
									action.getEndDate(),
									action.getBasePrice(),
									action.getDiscountPrice(),
									action.getType() == null ? null : action.getType().getId(),
									action.getChain() == null ? null : action.getChain().getId(),
									action.getProduct() == null ? null : action.getProduct().getId()).isPresent()) {
								logger.warn("File: '{}'. Sheet: '{}'. Row number '{}'. Target instance: '{}'. Chain synonym: '{}'. New instance has been already presented in the database.",
										file, sheet.getSheetName(), row.getRowNum(), ChainProduct.class, chainSynonym);
								logger.warn(action.toString());
								parsingResult.increaseNumberOfUnsavedInstances();
								parsingResult.increaseNumberOfAlreadyExistingInstances();
								parsingResult.addWarning(row.getRowNum(), "New instance has been already presented in the database." );
							} else {
								try {
									if (actionRepository.save(action) != null) {
										logger.info("File: '{}'. Sheet: '{}'. Row number '{}'. Target instance: '{}'. Chain synonym: '{}'. New instance has been created and saved to the database.",
												file, sheet.getSheetName(), row.getRowNum(), ChainProduct.class, chainSynonym);
										logger.info(action.toString());
										parsingResult.increaseNumberOfSavedInstances();
									}
								} catch (Exception savingException) {
									logger.error("File: '{}'. Sheet: '{}'. Row number '{}'. Target instance: '{}'. Chain synonym: '{}'. Exception has occurred during the saving of new instance to the database: {}.", 
											file, sheet.getSheetName(), row.getRowNum(), ChainProduct.class, chainSynonym, printStackTrace(savingException));
									parsingResult.increaseNumberOfUnsavedInstances();
									parsingResult.addException(row.getRowNum(), savingException);
								}
							}	
						} else {
							logger.error("File: '{}'. Sheet: '{}'. Row number '{}'. Target instance: '{}'. Chain synonym: '{}'. Exception(s) has occurred during the validation of new instance. See validation exception(s) below.", 
									file, sheet.getSheetName(), row.getRowNum(), ChainProduct.class, chainSynonym);
							parsingResult.increaseNumberOfUnsavedInstances();
							parsingResult.increaseNumberOfInvalidInstances();
							
							for (ConstraintViolation<ChainProduct> violation : constraintViolations) {
								logger.error("Validation exception: {}", violation.getMessage());
								parsingResult.addConstraintViolation(row.getRowNum(), violation);
							}
						}
					}
					
					logger.info("File: '{}'. Sheet: '{}'. Target instance: '{}'. Chain synonym: '{}'. Finishing working with sheet.", 
							FILE_WITH_BASIC_DATA, sheet.getSheetName(), ChainProduct.class, chainSynonym);
					logger.info("File: '{}'. Target instance: '{}'. Chain synonym: '{}'. Finishing process of parsing.", 
							FILE_WITH_BASIC_DATA, Subcategory.class, chainSynonym);
				} else {
					logger.warn("File: '{}'. Target instance: '{}'. Chain synonym: '{}'. Sheet at index '{}' wasn't found.", file, ChainProduct.class, chainSynonym, 0);
					parsingResult.addCommonWarning("Sheet at index '" + 0 + "' wasn't found.");
				}				
			} 		
		} catch (IOException ioException) {
			logger.error("File: '{}'. Target instance: '{}'. Chain synonym: '{}'. Input-output exception has occurred during opening the file: {}.", 
					file, ChainProduct.class, chainSynonym, printStackTrace(ioException));
			parsingResult.addCommonException(ioException);
		} 
		
		parsingResult.setFinishTime();
		logger.info("File: '{}'. Target instance: '{}'. Chain synonym: '{}'. Returning result of parsing. {}", 
				file, ChainProduct.class, chainSynonym, parsingResult.toString());
		return parsingResult;
	}
}
	