package naakcii.by.api.util.parser.multisheet;

import java.io.File;
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
import java.util.stream.Collectors;

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
import naakcii.by.api.unitofmeasure.UnitCode;
import naakcii.by.api.unitofmeasure.UnitOfMeasure;
import naakcii.by.api.unitofmeasure.UnitOfMeasureRepository;
import naakcii.by.api.util.ObjectFactory;
import naakcii.by.api.util.parser.DataParser;
import naakcii.by.api.util.parser.multisheet.mapper.CategoryColumnMapper;
import naakcii.by.api.util.parser.multisheet.mapper.ChainColumnMapper;
import naakcii.by.api.util.parser.multisheet.mapper.ChainProductTypeColumnMapper;
import naakcii.by.api.util.parser.multisheet.mapper.ChainProductColumnMapper;
import naakcii.by.api.util.parser.multisheet.mapper.SubcategoryColumnMapper;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class DataParserImpl implements DataParser{
	
	private static final Logger logger = LogManager.getLogger(DataParser.class);
	private static Validator validator;
	
	private static final String FILE_WITH_BASIC_DATA = "src" + File.separator + 
			   										   "test" + File.separator + 
			   										   "resources" + File.separator + 
			   										   "Basic_data.xlsx";
	private static final String SHEET_WITH_BASIC_CHAIN_PRODUCT_TYPES = "Basic_chain_product_types";
	private static final String SHEET_WITH_BASIC_CATEGORIES = "Basic_categories";
	private static final String SHEET_WITH_BASIC_SUBCATEGORIES = "Basic_subcategories";
	private static final String SHEET_WITH_BASIC_CHAINS = "Basic_chains";
	
	private static final String[] IS_ACTIVE = {"Да", "да"};
	private static final String[] IS_NOT_ACTIVE = {"Нет", "нет"};
	
	private static final String INDEFINITE_CATEGORY = "Indefinite category";
	private static final String INDEFINITE_SUBCATEGORY = "Indefinite subcategory";
	
	private static final String CHAIN_PRODUCT_TYPE_ONE_PLUS_ONE_SIGN = "Z050";
	private static final String CHAIN_PRODUCT_TYPE_DISCOUNT_SIGN = "Z040";
	private static final String CHAIN_PRODUCT_TYPE_ONE_PLUS_ONE_SYNONYM = "one_plus_one";
	private static final String CHAIN_PRODUCT_TYPE_DISCOUNT_SYNONYM = "discount";
	private static final String CHAIN_PRODUCT_TYPE_NICE_PRICE_SYNONYM = "good_price";
	
	private static final String PREPAIRING_FOR_PARSING_MESSAGE = "Source: '{}'. Target instance: '{}'. Preparing for parsing.";
	private static final String STARTING_PARSING_MESSAGE = "Source: '{}'. Target instance: '{}'. Starting process of parsing.";
	private static final String FINISHING_PARSING_MESSAGE = "Source: '{}'. Target instance: '{}'. Finishing process of parsing.";
	private static final String RESULT_OF_COLUMN_MAPPING_MESSAGE = "Source: '{}'. Target instance: '{}'. Returning result of column mapping. {}";
	private static final String RESULT_OF_PARSING_MESSAGE = "Source: '{}'. Target instance: '{}'. Returning result of parsing. {}";
	private static final String INSTANCE_WAS_PRESENTED_ENUM_MESSAGE = "Source: '{}'. Index: '{}'. Code: '{}'. Target instance: '{}'. New instance has been already presented in the database.";
	private static final String INSTANCE_WAS_PRESENTED_FILE_MESSAGE = "Source: '{}'. Sheet: '{}'. Row number: '{}'. Target instance: '{}'. New instance has been already presented in the database.";
	private static final String INSTANCE_WAS_SAVED_ENUM_MESSAGE = "Source: '{}'. Index: '{}'. Code: '{}'. Target instance: '{}'. New instance has been created and saved to the database.";
	private static final String INSTANCE_WAS_SAVED_FILE_MESSAGE = "Source: '{}'. Sheet: '{}'. Row number: '{}'. Target instance: '{}'. New instance has been created and saved to the database.";
	private static final String EXCEPTION_DURING_SAVING_ENUM_MESSAGE = "Source: '{}'. Index: '{}'. Code: '{}'. Target instance: '{}'. Exception has occurred during the saving of new instance to the database: {}.";
	private static final String EXCEPTION_DURING_SAVING_FILE_MESSAGE = "Source: '{}'. Sheet: '{}'. Row number: '{}'. Target instance: '{}'. Exception has occurred during the saving of new instance to the database: {}.";
	private static final String EXCEPTION_DURING_VALIDATION_ENUM_MESSAGE = "Source: '{}'. Index: '{}'. Code: '{}'. Target instance: '{}'. Exception(s) has occurred during the validation of new instance. See validation exception(s) below.";
	private static final String EXCEPTION_DURING_VALIDATION_FILE_MESSAGE = "Source: '{}'. Sheet: '{}'. Row number: '{}'. Target instance: '{}'. Exception(s) has occurred during the validation of new instance. See validation exception(s) below.";
	private static final String STARTING_WORKING_WITH_SHEET_MESSAGE = "Source: '{}'. Sheet: '{}'. Target instance: '{}'. Starting working with sheet.";
	private static final String FINISHING_WORKING_WITH_SHEET_MESSAGE = "Source: '{}'. Sheet: '{}'. Target instance: '{}'. Finishing working with sheet.";
	private static final String SHEET_WAS_NOT_FOUND_MESSAGE = "Source: '{}'. Target instance: '{}'. Sheet with name '{}' wasn't found.";
	private static final String REFERENCE_ON_INSTANCE_WAS_NOT_FOUND_MESSAGE = "Source: '{}'. Sheet: '{}'. Row number: '{}' Target instance: '{}'. Reference on instance of '{}' with name '{}' hasn't been found in the database.";
	
	private static final String INSTANCE_WAS_PRESENTED_WARNING = "New instance has been already presented in the database.";
	private static final String VALIDATION_EXCEPTION = "Validation exception: {}";
	
	private ObjectFactory objectFactory;
	private ChainRepository chainRepository;
	private CategoryRepository categoryRepository;
	private SubcategoryRepository subcategoryRepository;
	private ProductRepository productRepository;
	private ChainProductRepository chainProductRepository;
	private ChainProductTypeRepository chainProductTypeRepository;
	private CountryRepository countryRepository;
	private UnitOfMeasureRepository unitOfMeasureRepository; 
	
	@Autowired
	public DataParserImpl(
					  ObjectFactory objectFactory,
					  ChainRepository chainRepository,
					  CategoryRepository categoryRepository,
					  SubcategoryRepository subcategoryRepository,
					  ProductRepository productRepository,
					  ChainProductRepository chainProductRepository,
					  ChainProductTypeRepository chainProductTypeRepository,
					  CountryRepository countryRepository,
					  UnitOfMeasureRepository unitOfMeasureRepository
					 ) {
		this.objectFactory = objectFactory;
		this.chainRepository = chainRepository;
		this.categoryRepository = categoryRepository;
		this.subcategoryRepository = subcategoryRepository;
		this.productRepository = productRepository;
		this.chainProductRepository = chainProductRepository;
		this.chainProductTypeRepository = chainProductTypeRepository;
		this.countryRepository = countryRepository; 
		this.unitOfMeasureRepository = unitOfMeasureRepository;
	}
	
	@Override
	public List<ParsingResult<?>> parseBasicData() {
		List<ParsingResult<?>> results = parseBasicData(UnitOfMeasure.class, 
														Country.class, 
														ChainProductType.class, 
														Chain.class, 
														Category.class, 
														Subcategory.class);
		return results;
	}
	
	public List<ParsingResult<?>> parseBasicData(Class<?> ... classes) {
		List<ParsingResult<?>> results = new ArrayList<>();
		List<Class<?>> targetClasses = new ArrayList<>();
		targetClasses.addAll(Arrays.asList(classes));
		
		if (targetClasses.isEmpty()) {
			logger.warn("Creating of basic data has skipped due to empty list of target classes.");
		} else {
			logger.info("Creating of basic data has started.");
			StringBuilder message = new StringBuilder("Target classes: ");
			message.append(targetClasses.stream().map(Class::getName).collect(Collectors.joining("; ")));
			message.append(".");
			logger.info(message.toString());
			
			if (targetClasses.contains(Country.class)) {
				results.add(createCountries());
				targetClasses.remove(Country.class);
			}
			
			if (targetClasses.contains(UnitOfMeasure.class)) {
				results.add(createUnitsOfMeasure());
				targetClasses.remove(UnitOfMeasure.class);
			}
			
			if (targetClasses.contains(ChainProductType.class) || targetClasses.contains(Chain.class)  || targetClasses.contains(Category.class) || targetClasses.contains(Subcategory.class)) {
				try (FileInputStream fileInputStream = new FileInputStream(FILE_WITH_BASIC_DATA)) {
					try (XSSFWorkbook book = new XSSFWorkbook(fileInputStream)) {
						if (targetClasses.contains(ChainProductType.class)) {
							results.add(createBasicChainProductTypes(book));
							targetClasses.remove(ChainProductType.class);
						}
						
						if (targetClasses.contains(Chain.class)) {
							results.add(createBasicChains(book));
							targetClasses.remove(Chain.class);
						}
						
						if (targetClasses.contains(Category.class)) {
							results.add(createBasicCategories(book));
							targetClasses.remove(Category.class);
						}
						
						if (targetClasses.contains(Subcategory.class)) {
							results.add(createBasicSubcategories(book));
							targetClasses.remove(Subcategory.class);
						}
					}
				} catch (IOException ioException) {
					logger.error("File: '{}'. Target instances: '{}'. Input-output exception has occurred during opening the file: {}.", 
							FILE_WITH_BASIC_DATA, targetClasses.stream().map(Class::getName).collect(Collectors.joining("; ")), printStackTrace(ioException));
				}
			}
			
			logger.info("Creating of basic data has finished.");
		}
		
		return results;
	}

	private ParsingResult<Country> createCountries() {
		logger.info(PREPAIRING_FOR_PARSING_MESSAGE, CountryCode.class, Country.class.getName());
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		ParsingResult<Country> parsingResult = objectFactory.getInstance(ParsingResult.class, Country.class, CountryCode.class.getName());
		parsingResult.setStartTime();
		int index = 0;
		logger.info(STARTING_PARSING_MESSAGE, CountryCode.class, Country.class.getName());
		
		for (CountryCode countryCode : CountryCode.values()) {
			index ++;
			parsingResult.increaseTotalNumberOfInstances();
			Country country = objectFactory.getInstance(Country.class, countryCode);
			Set<ConstraintViolation<Country>> constraintViolations = validator.validate(country);
			
			if (constraintViolations.size() == 0) {
				if (countryRepository.findByAlphaCode2AndAlphaCode3(country.getAlphaCode2(), country.getAlphaCode3()).isPresent()) {
					logger.warn(INSTANCE_WAS_PRESENTED_ENUM_MESSAGE, CountryCode.class, index, countryCode, Country.class.getName());
					logger.warn(country.toString());
					parsingResult.increaseNumberOfUnsavedInstances();
					parsingResult.increaseNumberOfAlreadyExistingInstances();
					parsingResult.addWarning(index, INSTANCE_WAS_PRESENTED_WARNING);
				} else {
					try {
						if (countryRepository.save(country) != null) {
							logger.info(INSTANCE_WAS_SAVED_ENUM_MESSAGE, CountryCode.class, index, countryCode, Country.class.getName());
							logger.info(country.toString());
							parsingResult.increaseNumberOfSavedInstances();
						}
					} catch (Exception savingException) {
						logger.error(EXCEPTION_DURING_SAVING_ENUM_MESSAGE, CountryCode.class, index, countryCode, Country.class.getName(), printStackTrace(savingException));
						parsingResult.increaseNumberOfUnsavedInstances();
						parsingResult.addException(index, savingException);
					}
				}	
			} else {
				logger.error(EXCEPTION_DURING_VALIDATION_ENUM_MESSAGE, CountryCode.class, index, countryCode, Country.class.getName());
				parsingResult.increaseNumberOfUnsavedInstances();
				parsingResult.increaseNumberOfInvalidInstances();
				
				for (ConstraintViolation<Country> violation : constraintViolations) {
					logger.error(VALIDATION_EXCEPTION, violation.getMessage());
					parsingResult.addConstraintViolation(index, violation);
				}
			}
		}
		
		logger.info(FINISHING_PARSING_MESSAGE, CountryCode.class, Country.class.getName());	
		parsingResult.setFinishTime();
		logger.info(RESULT_OF_PARSING_MESSAGE,	CountryCode.class, Country.class.getName(), parsingResult.toString());
		return parsingResult;
	}
	
	private ParsingResult<UnitOfMeasure> createUnitsOfMeasure() {
		logger.info(PREPAIRING_FOR_PARSING_MESSAGE, UnitCode.class, UnitOfMeasure.class.getName());
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		ParsingResult<UnitOfMeasure> parsingResult = objectFactory.getInstance(ParsingResult.class, UnitOfMeasure.class, UnitCode.class.getName());
		parsingResult.setStartTime();
		int index = 0;
		logger.info(STARTING_PARSING_MESSAGE, UnitCode.class, UnitOfMeasure.class.getName());
		
		for (UnitCode unitCode : UnitCode.values()) {
			index ++;
			parsingResult.increaseTotalNumberOfInstances();
			UnitOfMeasure unitOfMeasure = objectFactory.getInstance(UnitOfMeasure.class, unitCode);
			Set<ConstraintViolation<UnitOfMeasure>> constraintViolations = validator.validate(unitOfMeasure);
			
			if (constraintViolations.size() == 0) {
				if (unitOfMeasureRepository.findByName(unitOfMeasure.getName()).isPresent()) {
					logger.warn(INSTANCE_WAS_PRESENTED_ENUM_MESSAGE, UnitCode.class, index, unitCode, UnitOfMeasure.class.getName());
					logger.warn(unitOfMeasure.toString());
					parsingResult.increaseNumberOfUnsavedInstances();
					parsingResult.increaseNumberOfAlreadyExistingInstances();
					parsingResult.addWarning(index, INSTANCE_WAS_PRESENTED_WARNING);
				} else {
					try {
						if (unitOfMeasureRepository.save(unitOfMeasure) != null) {
							logger.info(INSTANCE_WAS_SAVED_ENUM_MESSAGE, UnitCode.class, index, unitCode, UnitOfMeasure.class.getName());
							logger.info(unitOfMeasure.toString());
							parsingResult.increaseNumberOfSavedInstances();
						}
					} catch (Exception savingException) {
						logger.error(EXCEPTION_DURING_SAVING_ENUM_MESSAGE, UnitCode.class, index, unitCode, UnitOfMeasure.class.getName(), printStackTrace(savingException));
						parsingResult.increaseNumberOfUnsavedInstances();
						parsingResult.addException(index, savingException);
					}
				}	
			} else {
				logger.error(EXCEPTION_DURING_VALIDATION_ENUM_MESSAGE, UnitCode.class, index, unitCode, UnitOfMeasure.class.getName());
				parsingResult.increaseNumberOfUnsavedInstances();
				parsingResult.increaseNumberOfInvalidInstances();
				
				for (ConstraintViolation<UnitOfMeasure> violation : constraintViolations) {
					logger.error(VALIDATION_EXCEPTION, violation.getMessage());
					parsingResult.addConstraintViolation(index, violation);
				}
			}
		}
		
		logger.info(FINISHING_PARSING_MESSAGE, UnitCode.class, UnitOfMeasure.class.getName());	
		parsingResult.setFinishTime();
		logger.info(RESULT_OF_PARSING_MESSAGE,	UnitCode.class, UnitOfMeasure.class.getName(), parsingResult.toString());
		return parsingResult;
	}
	
	private ParsingResult<ChainProductType> createBasicChainProductTypes(XSSFWorkbook book) {
		logger.info(PREPAIRING_FOR_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, ChainProductType.class.getName());
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		ParsingResult<ChainProductType> parsingResult = objectFactory.getInstance(ParsingResult.class, ChainProductType.class, FILE_WITH_BASIC_DATA);
		parsingResult.setStartTime();
		logger.info(STARTING_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, ChainProductType.class.getName());		
		XSSFSheet sheet = book.getSheet(SHEET_WITH_BASIC_CHAIN_PRODUCT_TYPES);
				
			if (sheet != null) {
				parsingResult.setSheetName(sheet.getSheetName());
				parsingResult.setSheetIndex(book.getSheetIndex(sheet));
				logger.info(STARTING_WORKING_WITH_SHEET_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), ChainProductType.class.getName());
				ChainProductTypeColumnMapper columnMapper = objectFactory.getInstance(ChainProductTypeColumnMapper.class);
				Iterator<Row> rowIterator = sheet.iterator();
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					columnMapper.mapColumn(cell.getStringCellValue(), cell.getColumnIndex());
				}
				
				logger.info(RESULT_OF_COLUMN_MAPPING_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), ChainProductType.class.getName(), columnMapper.toString());		
				
				while (rowIterator.hasNext()) {
					row = rowIterator.next();
					ChainProductType chainProductType = objectFactory.getInstance(ChainProductType.class);
					parsingResult.increaseTotalNumberOfInstances();
					
					if (columnMapper.isNameMapped()) {
						Cell chainProductTypeName = row.getCell(columnMapper.getNameColumnIndex());
						
						if ((chainProductTypeName != null) && (chainProductTypeName.getCellType() == CellType.STRING)) {
							chainProductType.setName(chainProductTypeName.getStringCellValue().trim());
						}	
					}
						
					if (columnMapper.isSynonymMapped()) {
						Cell chainProductTypeSynonym = row.getCell(columnMapper.getSynonymColumnIndex());
						
						if ((chainProductTypeSynonym != null) && (chainProductTypeSynonym.getCellType() == CellType.STRING)) {
							System.out.println(chainProductTypeSynonym.getCellType());
							chainProductType.setSynonym(chainProductTypeSynonym.getStringCellValue().trim());
						}	
					}
					
					if (columnMapper.isTooltipMapped()) {
						Cell chainProductTypeTooltip = row.getCell(columnMapper.getTooltipColumnIndex());
						
						if ((chainProductTypeTooltip != null) && (chainProductTypeTooltip.getCellType() == CellType.STRING)) {
							chainProductType.setTooltip(chainProductTypeTooltip.getStringCellValue().trim());
						}	
					}
						
					Set<ConstraintViolation<ChainProductType>> constraintViolations = validator.validate(chainProductType);
						
					if (constraintViolations.size() == 0) {
						if (chainProductTypeRepository.findByNameAndSynonym(chainProductType.getName(), chainProductType.getSynonym()).isPresent()) {
							logger.warn(INSTANCE_WAS_PRESENTED_FILE_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), ChainProductType.class.getName());
							logger.warn(chainProductType.toString());
							parsingResult.increaseNumberOfUnsavedInstances();
							parsingResult.increaseNumberOfAlreadyExistingInstances();
							parsingResult.addWarning(row.getRowNum(), INSTANCE_WAS_PRESENTED_WARNING);
						} else {
							try {
								if (chainProductTypeRepository.save(chainProductType) != null) {
									logger.info(INSTANCE_WAS_SAVED_FILE_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), ChainProductType.class.getName());
									logger.info(chainProductType.toString());
									parsingResult.increaseNumberOfSavedInstances();
								}
							} catch (Exception savingException) {
								logger.error(EXCEPTION_DURING_SAVING_FILE_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), ChainProductType.class.getName(), printStackTrace(savingException));
								parsingResult.increaseNumberOfUnsavedInstances();
								parsingResult.addException(row.getRowNum(), savingException);
							}
						}	
					} else {
						logger.error(EXCEPTION_DURING_VALIDATION_FILE_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), ChainProductType.class.getName());
						parsingResult.increaseNumberOfUnsavedInstances();
						parsingResult.increaseNumberOfInvalidInstances();
							
						for (ConstraintViolation<ChainProductType> violation : constraintViolations) {
							logger.error(VALIDATION_EXCEPTION, violation.getMessage());
							parsingResult.addConstraintViolation(row.getRowNum(), violation);
						}
					}
				}
					
				logger.info(FINISHING_WORKING_WITH_SHEET_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), ChainProductType.class.getName());
				logger.info(FINISHING_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, ChainProductType.class.getName());
				} else {
					logger.warn(SHEET_WAS_NOT_FOUND_MESSAGE, FILE_WITH_BASIC_DATA, ChainProductType.class.getName(), SHEET_WITH_BASIC_CHAIN_PRODUCT_TYPES);
					parsingResult.addCommonWarning("Sheet with name '" + SHEET_WITH_BASIC_CHAIN_PRODUCT_TYPES + "' wasn't found.");
				}
			 		
		parsingResult.setFinishTime();
		logger.info(RESULT_OF_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, ChainProductType.class.getName(), parsingResult.toString());
		return parsingResult;
	}
	
	private ParsingResult<Chain> createBasicChains(XSSFWorkbook book) {
		logger.info(PREPAIRING_FOR_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, Chain.class.getName());
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		ParsingResult<Chain> parsingResult = objectFactory.getInstance(ParsingResult.class, Chain.class, FILE_WITH_BASIC_DATA);
		parsingResult.setStartTime();
		logger.info(STARTING_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, Chain.class.getName());
		XSSFSheet sheet = book.getSheet(SHEET_WITH_BASIC_CHAINS);
			
		if (sheet != null) {
			parsingResult.setSheetName(sheet.getSheetName());
			parsingResult.setSheetIndex(book.getSheetIndex(sheet));
			logger.info(STARTING_WORKING_WITH_SHEET_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), Chain.class.getName());
			ChainColumnMapper columnMapper = objectFactory.getInstance(ChainColumnMapper.class);
			Iterator<Row> rowIterator = sheet.iterator();
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
					
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				columnMapper.mapColumn(cell.getStringCellValue(), cell.getColumnIndex());
			}
					
			logger.info(RESULT_OF_COLUMN_MAPPING_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), Chain.class.getName(), columnMapper.toString());			
				
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				Chain chain = objectFactory.getInstance(Chain.class);
				parsingResult.increaseTotalNumberOfInstances();
						
				if (columnMapper.isNameMapped()) {
					Cell chainName = row.getCell(columnMapper.getNameColumnIndex());
						
					if ((chainName != null) && (chainName.getCellType() == CellType.STRING)) {
						chain.setName(chainName.getStringCellValue().trim());
					}	
				}
				
				if (columnMapper.isSynonymMapped()) {
					Cell chainSynonym = row.getCell(columnMapper.getSynonymColumnIndex());
							
					if ((chainSynonym != null) && (chainSynonym.getCellType() == CellType.STRING)) {
						chain.setSynonym(chainSynonym.getStringCellValue().trim());
					}	
				}
						
				if (columnMapper.isLinkMapped()) {
					Cell chainLink = row.getCell(columnMapper.getLinkColumnIndex());
						
					if ((chainLink != null) && (chainLink.getCellType() == CellType.STRING)) {
						chain.setLink(chainLink.getStringCellValue().trim());
					}	
				}
						
				if (columnMapper.isLogoMapped()) {
					Cell chainLogo = row.getCell(columnMapper.getLogoColumnIndex());
						
					if ((chainLogo != null) && (chainLogo.getCellType() == CellType.STRING)) {
						chain.setLogo(chainLogo.getStringCellValue().trim());
					}	
				}
						
				if (columnMapper.isActiveMapped()) {
					Cell chainIsActive = row.getCell(columnMapper.getIsActiveColumnIndex());
							
					if ((chainIsActive != null) && (chainIsActive.getCellType() == CellType.STRING)) {
								
						if (Arrays.stream(IS_ACTIVE).anyMatch(chainIsActive.getStringCellValue().trim()::equalsIgnoreCase)) {
							chain.setIsActive(true);
						} else if (Arrays.stream(IS_NOT_ACTIVE).anyMatch(chainIsActive.getStringCellValue().trim()::equalsIgnoreCase)) {
							chain.setIsActive(false);
						}
					}	
				}
						
				Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
						
				if (constraintViolations.size() == 0) {
					if (chainRepository.findByNameAndSynonym(chain.getName(), chain.getSynonym()).isPresent()) {
						logger.warn(INSTANCE_WAS_PRESENTED_FILE_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Chain.class);
						logger.warn(chain.toString());
						parsingResult.increaseNumberOfUnsavedInstances();
						parsingResult.increaseNumberOfAlreadyExistingInstances();
						parsingResult.addWarning(row.getRowNum(), INSTANCE_WAS_PRESENTED_WARNING);
					} else {
						try {
							if (chainRepository.save(chain) != null) {
								logger.info(INSTANCE_WAS_SAVED_FILE_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Chain.class.getName());
								logger.info(chain.toString());
								parsingResult.increaseNumberOfSavedInstances();
							}
						} catch (Exception savingException) {
							logger.error(EXCEPTION_DURING_SAVING_FILE_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Chain.class.getName(), printStackTrace(savingException));
							parsingResult.increaseNumberOfUnsavedInstances();
							parsingResult.addException(row.getRowNum(), savingException);
						}
					}	
				} else {
					logger.error(EXCEPTION_DURING_VALIDATION_FILE_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Chain.class.getName());
					parsingResult.increaseNumberOfUnsavedInstances();
					parsingResult.increaseNumberOfInvalidInstances();
						
					for (ConstraintViolation<Chain> violation : constraintViolations) {
						logger.error(VALIDATION_EXCEPTION, violation.getMessage());
						parsingResult.addConstraintViolation(row.getRowNum(), violation);
					}
				}
			}
					
			logger.info(FINISHING_WORKING_WITH_SHEET_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), Chain.class.getName());
			logger.info(FINISHING_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, Chain.class.getName());
		} else {
			logger.warn(SHEET_WAS_NOT_FOUND_MESSAGE, FILE_WITH_BASIC_DATA, Chain.class.getName(), SHEET_WITH_BASIC_CHAINS);
			parsingResult.addCommonWarning("Sheet with name '" + SHEET_WITH_BASIC_CHAINS + "' wasn't found.");
		}					
		
		parsingResult.setFinishTime();
		logger.info(RESULT_OF_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, Chain.class.getName(), parsingResult.toString());
		return parsingResult;
	}
	
	private ParsingResult<Category> createBasicCategories(XSSFWorkbook book) {
		logger.info(PREPAIRING_FOR_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, Category.class.getName());
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		ParsingResult<Category> parsingResult = objectFactory.getInstance(ParsingResult.class, Category.class, FILE_WITH_BASIC_DATA);
		parsingResult.setStartTime();
		logger.info(STARTING_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, Category.class.getName());
		XSSFSheet sheet = book.getSheet(SHEET_WITH_BASIC_CATEGORIES);
				
		if (sheet != null) {
			parsingResult.setSheetName(sheet.getSheetName());
			parsingResult.setSheetIndex(book.getSheetIndex(sheet));
			logger.info(STARTING_WORKING_WITH_SHEET_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), Category.class.getName());
			CategoryColumnMapper columnMapper = objectFactory.getInstance(CategoryColumnMapper.class);
			Iterator<Row> rowIterator = sheet.iterator();
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
					
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				columnMapper.mapColumn(cell.getStringCellValue(), cell.getColumnIndex());
			}
					
			logger.info(RESULT_OF_COLUMN_MAPPING_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), Category.class.getName(), columnMapper.toString());
							
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				Category category = objectFactory.getInstance(Category.class);
				parsingResult.increaseTotalNumberOfInstances();
				
				if (columnMapper.isNameMapped()) {
					Cell categoryName = row.getCell(columnMapper.getNameColumnIndex());
					
					if ((categoryName != null) && (categoryName.getCellType() == CellType.STRING)) {
						category.setName(categoryName.getStringCellValue().trim());
					}	
				}
						
				if (columnMapper.isIconMapped()) {
					Cell categoryIcon = row.getCell(columnMapper.getIconColumnIndex());
						
					if ((categoryIcon != null) && (categoryIcon.getCellType() == CellType.STRING)) {
						category.setIcon(categoryIcon.getStringCellValue().trim());
					}	
				}
						
				if (columnMapper.isPriorityMapped()) {
					Cell categoryPriority = row.getCell(columnMapper.getPriorityColumnIndex());
					
					if ((categoryPriority != null) && (categoryPriority.getCellType() == CellType.NUMERIC)) {
						category.setPriority((int) categoryPriority.getNumericCellValue());
					}	
				}
						
				if (columnMapper.isActiveMapped()) {
					Cell categoryIsActive = row.getCell(columnMapper.getIsActiveColumnIndex());
							
					if ((categoryIsActive != null) && (categoryIsActive.getCellType() == CellType.STRING)) {
							
						if (Arrays.stream(IS_ACTIVE).anyMatch(categoryIsActive.getStringCellValue().trim()::equalsIgnoreCase)) {
							category.setIsActive(true);
						} else if (Arrays.stream(IS_NOT_ACTIVE).anyMatch(categoryIsActive.getStringCellValue().trim()::equalsIgnoreCase)) {
							category.setIsActive(false);
						}
					}	
				}
						
				Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);
					
				if (constraintViolations.size() == 0) {
					if (categoryRepository.findByName(category.getName()).isPresent()) {
						logger.warn(INSTANCE_WAS_PRESENTED_FILE_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Category.class.getName());
						logger.warn(category.toString());
						parsingResult.increaseNumberOfUnsavedInstances();
						parsingResult.increaseNumberOfAlreadyExistingInstances();
						parsingResult.addWarning(row.getRowNum(), INSTANCE_WAS_PRESENTED_WARNING);
					} else {
						try {
							if (categoryRepository.save(category) != null) {
								logger.info(INSTANCE_WAS_SAVED_FILE_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Category.class.getName());
								logger.info(category.toString());
								parsingResult.increaseNumberOfSavedInstances();
							}
						} catch (Exception savingException) {
							logger.error(EXCEPTION_DURING_SAVING_FILE_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Category.class.getName(), printStackTrace(savingException));
							parsingResult.increaseNumberOfUnsavedInstances();
							parsingResult.addException(row.getRowNum(), savingException);
						}
					}	
				} else {
					logger.error(EXCEPTION_DURING_VALIDATION_FILE_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Category.class.getName());
					parsingResult.increaseNumberOfUnsavedInstances();
					parsingResult.increaseNumberOfInvalidInstances();
					
					for (ConstraintViolation<Category> violation : constraintViolations) {
						logger.error(VALIDATION_EXCEPTION, violation.getMessage());
						parsingResult.addConstraintViolation(row.getRowNum(), violation);
					}
				}
			}
					
			logger.info(FINISHING_WORKING_WITH_SHEET_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), Category.class.getName());
			logger.info(FINISHING_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, Category.class.getName());
		} else {
			logger.warn(SHEET_WAS_NOT_FOUND_MESSAGE, FILE_WITH_BASIC_DATA, Category.class.getName(), SHEET_WITH_BASIC_CATEGORIES);
			parsingResult.addCommonWarning("Sheet with name '" + SHEET_WITH_BASIC_CATEGORIES + "' wasn't found.");
		}					
		
		parsingResult.setFinishTime();
		logger.info(RESULT_OF_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, Category.class.getName(), parsingResult.toString());
		return parsingResult;
	}
	
	private ParsingResult<Subcategory> createBasicSubcategories(XSSFWorkbook book) {
		logger.info(PREPAIRING_FOR_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, Subcategory.class.getName());
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		ParsingResult<Subcategory> parsingResult = objectFactory.getInstance(ParsingResult.class, Subcategory.class, FILE_WITH_BASIC_DATA);
		parsingResult.setStartTime();
		logger.info(STARTING_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, Subcategory.class.getName());	
		XSSFSheet sheet = book.getSheet(SHEET_WITH_BASIC_SUBCATEGORIES);
				
		if (sheet != null) {
			parsingResult.setSheetName(sheet.getSheetName());
			parsingResult.setSheetIndex(book.getSheetIndex(sheet));
			logger.info(STARTING_WORKING_WITH_SHEET_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), Subcategory.class.getName());
			SubcategoryColumnMapper columnMapper = objectFactory.getInstance(SubcategoryColumnMapper.class);
			Iterator<Row> rowIterator = sheet.iterator();
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
					
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				columnMapper.mapColumn(cell.getStringCellValue(), cell.getColumnIndex());
			}
					
			logger.info(RESULT_OF_COLUMN_MAPPING_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), Subcategory.class.getName(), columnMapper.toString());			
				
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				Subcategory subcategory = objectFactory.getInstance(Subcategory.class);
				parsingResult.increaseTotalNumberOfInstances();
				
				if (columnMapper.isNameMapped()) {
					Cell subcategoryName = row.getCell(columnMapper.getNameColumnIndex());
					
					if ((subcategoryName != null) && (subcategoryName.getCellType() == CellType.STRING)) {
						subcategory.setName(subcategoryName.getStringCellValue().trim());
					}	
				}
					
				if (columnMapper.isCategoryMapped()) {
					Cell categoryName = row.getCell(columnMapper.getCategoryColumnIndex());
						
					if ((categoryName != null) && (categoryName.getCellType() == CellType.STRING)) {
						Optional<Category> category = categoryRepository.findByName(categoryName.getStringCellValue().trim());
							
						if (category.isPresent()) {
							subcategory.setCategory(category.get());
						} else {
							logger.warn(REFERENCE_ON_INSTANCE_WAS_NOT_FOUND_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Subcategory.class.getName(), Category.class.getName(), categoryName.getStringCellValue().trim());
							parsingResult.addWarning(row.getRowNum(), "Reference on instance of '" + Category.class.getName() + "' with name '" + categoryName.getStringCellValue().trim() + "' hasn't been found in the database.");
						}
					}	
				}
						
				if (columnMapper.isPriorityMapped()) {
					Cell subcategoryPriority = row.getCell(columnMapper.getPriorityColumnIndex());
					
					if ((subcategoryPriority != null) && (subcategoryPriority.getCellType() == CellType.NUMERIC)) {
						subcategory.setPriority((int) subcategoryPriority.getNumericCellValue());
					}	
				}
						
				if (columnMapper.isActiveMapped()) {
					Cell subcategoryIsActive = row.getCell(columnMapper.getIsActiveColumnIndex());
					
					if ((subcategoryIsActive != null) && (subcategoryIsActive.getCellType() == CellType.STRING)) {
								
						if (Arrays.stream(IS_ACTIVE).anyMatch(subcategoryIsActive.getStringCellValue().trim()::equalsIgnoreCase)) {
							subcategory.setIsActive(true);
						} else if (Arrays.stream(IS_NOT_ACTIVE).anyMatch(subcategoryIsActive.getStringCellValue().trim()::equalsIgnoreCase)) {
							subcategory.setIsActive(false);
						}
					}	
				}
						
				Set<ConstraintViolation<Subcategory>> constraintViolations = validator.validate(subcategory);
				
				if (constraintViolations.size() == 0) {
					if (subcategoryRepository.findByNameAndCategoryName(subcategory.getName(), subcategory.getCategory().getName()).isPresent()) {
						logger.warn(INSTANCE_WAS_PRESENTED_FILE_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Subcategory.class.getName());
						logger.warn(subcategory.toString());
						parsingResult.increaseNumberOfUnsavedInstances();
						parsingResult.increaseNumberOfAlreadyExistingInstances();
						parsingResult.addWarning(row.getRowNum(), INSTANCE_WAS_PRESENTED_WARNING);
					} else {
						try {
							if (subcategoryRepository.save(subcategory) != null) {
								logger.info(INSTANCE_WAS_SAVED_FILE_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Subcategory.class.getName());
								logger.info(subcategory.toString());
								parsingResult.increaseNumberOfSavedInstances();
							}
						} catch (Exception savingException) {
							logger.error(EXCEPTION_DURING_SAVING_FILE_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Subcategory.class.getName(), printStackTrace(savingException));
							parsingResult.increaseNumberOfUnsavedInstances();
							parsingResult.addException(row.getRowNum(), savingException);
						}
					}	
				} else {
					logger.error(EXCEPTION_DURING_VALIDATION_FILE_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Subcategory.class.getName());
					parsingResult.increaseNumberOfUnsavedInstances();
					parsingResult.increaseNumberOfInvalidInstances();
							
					for (ConstraintViolation<Subcategory> violation : constraintViolations) {
						logger.error(VALIDATION_EXCEPTION, violation.getMessage());
						parsingResult.addConstraintViolation(row.getRowNum(), violation);
					}
				}
			}
					
			logger.info(FINISHING_WORKING_WITH_SHEET_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), Subcategory.class.getName());
			logger.info(FINISHING_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, Subcategory.class.getName());
		} else {
			logger.warn(SHEET_WAS_NOT_FOUND_MESSAGE, FILE_WITH_BASIC_DATA, Subcategory.class.getName(), SHEET_WITH_BASIC_SUBCATEGORIES);
			parsingResult.addCommonWarning("Sheet with name '" + SHEET_WITH_BASIC_SUBCATEGORIES + "' wasn't found.");
		}				
		
		parsingResult.setFinishTime();
		logger.info(RESULT_OF_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, Subcategory.class.getName(), parsingResult.toString());
		return parsingResult;
	}
	
	@Override
	public List<ParsingResult<?>> parseChainProducts(String file, String chainSynonym) {
		logger.info("Source: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Preparing for parsing.", 
				file, chainSynonym, Product.class.getName(), ChainProduct.class.getName());
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		List<ParsingResult<?>> results = new ArrayList<>();
		ParsingResult<Product> productParsingResult = objectFactory.getInstance(ParsingResult.class, Product.class, file);
		ParsingResult<ChainProduct> chainProductParsingResult = objectFactory.getInstance(ParsingResult.class, ChainProduct.class, file);
		productParsingResult.setStartTime();
		chainProductParsingResult.setStartTime();
		
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			logger.info("Source: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Starting process of parsing.", 
					file, Product.class.getName(), ChainProduct.class.getName(), chainSynonym);
		
			try (XSSFWorkbook book = new XSSFWorkbook(fileInputStream)) {
				XSSFSheet sheet = book.getSheetAt(0);
				
				if (sheet != null) {
					productParsingResult.setSheetName(sheet.getSheetName());
					chainProductParsingResult.setSheetName(sheet.getSheetName());
					productParsingResult.setSheetIndex(book.getSheetIndex(sheet));
					chainProductParsingResult.setSheetIndex(book.getSheetIndex(sheet));
					logger.info("Source: '{}'. Sheet: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Starting working with sheet.", 
							file, sheet.getSheetName(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym);
					ChainProductColumnMapper columnMapper = objectFactory.getInstance(ChainProductColumnMapper.class);
					Iterator<Row> rowIterator = sheet.iterator();
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						columnMapper.mapColumn(cell.getStringCellValue(), cell.getColumnIndex());
					}
					
					logger.info("Source: '{}'. Sheet: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Returning result of column mapping. {}", 
							file, sheet.getSheetName(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym, columnMapper.toString());
				
					while (rowIterator.hasNext()) {
						row = rowIterator.next();
						Product product = objectFactory.getInstance(Product.class);
						ChainProduct chainProduct = objectFactory.getInstance(ChainProduct.class);
						productParsingResult.increaseTotalNumberOfInstances();
						chainProductParsingResult.increaseTotalNumberOfInstances();
						
						if (columnMapper.isNameMapped()) {
							Cell productName = row.getCell(columnMapper.getNameColumnIndex());
							
							if ((productName != null) && (productName.getCellType() == CellType.STRING)) {
								product.setName(productName.getStringCellValue().trim());
							}	
						}
						
						if (columnMapper.isBarcodeMapped()) {
							Cell productBarcode = row.getCell(columnMapper.getBarcodeColumnIndex());
							
							if ((productBarcode != null) && (productBarcode.getCellType() == CellType.NUMERIC)) {
								Formatter formatter = new Formatter();
								product.setBarcode(formatter.format("%.0f", productBarcode.getNumericCellValue()).toString());
								formatter.close();
							}
						}
						
						if (columnMapper.isUnitMapped()) {
							Cell productUnit = row.getCell(columnMapper.getUnitColumnIndex());
							
							if ((productUnit != null) && (productUnit.getCellType() == CellType.STRING)) {
								Optional<UnitOfMeasure> unitOfMeasure = unitOfMeasureRepository.findByName(productUnit.getStringCellValue().trim());
								
								if (unitOfMeasure.isPresent()) {
									product.setUnitOfMeasure(unitOfMeasure.get());
								} else {
									logger.warn("Source: '{}'. Sheet: '{}'. Row number: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Reference on instance of '{}' with name '{}' hasn't been found in the database.", 
											file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym, UnitOfMeasure.class.getName(), productUnit.getStringCellValue().trim());
									productParsingResult.addWarning(row.getRowNum(), "Reference on instance of '" + UnitOfMeasure.class.getName() + "' with name '" + productUnit.getStringCellValue().trim() + "' hasn't been found in the database.");
								}
							}
						}
						
						if (columnMapper.isManufacturerMapped()) {
							Cell productManufacturer = row.getCell(columnMapper.getManufacturerColumnIndex());
							
							if ((productManufacturer != null) && (productManufacturer.getCellType() == CellType.STRING)) {
								product.setManufacturer(productManufacturer.getStringCellValue().trim());
							}	
						}
						
						if (columnMapper.isBrandMapped()) {
							Cell productBrand = row.getCell(columnMapper.getBrandColumnIndex());
							
							if ((productBrand != null) && (productBrand.getCellType() == CellType.STRING)) {
								product.setBrand(productBrand.getStringCellValue().trim());
							}	
						}
						
						if (columnMapper.isCountryOfOriginMapped()) {
							Cell productCountryOfOrigin = row.getCell(columnMapper.getCountryOfOriginColumnIndex());
							
							if ((productCountryOfOrigin != null) && (productCountryOfOrigin.getCellType() == CellType.STRING)) {
								Optional<Country> countryOfOrigin = countryRepository.findByAlphaCode2(productCountryOfOrigin.getStringCellValue().trim());
								
								if (countryOfOrigin.isPresent()) {
									product.setCountryOfOrigin(countryOfOrigin.get());
								} else {
									logger.warn("Source: '{}'. Sheet: '{}'. Row number: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Reference on instance of '{}' with code '{}' hasn't been found in the database.", 
											file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym, Country.class.getName(), productCountryOfOrigin.getStringCellValue().trim());
									productParsingResult.addWarning(row.getRowNum(), "Reference on instance of '" + Country.class.getName() + "' with name '" + productCountryOfOrigin.getStringCellValue().trim() + "' hasn't been found in the database.");
								}
							}	
						}
						
						Optional<Subcategory> subcategory = subcategoryRepository.findByNameAndCategoryName(INDEFINITE_SUBCATEGORY, INDEFINITE_CATEGORY);
						
						if (subcategory.isPresent()) {
							product.setSubcategory(subcategory.get());
						} else {
							logger.warn("Source: '{}'. Sheet: '{}'. Row number: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Reference on instance of '{}' with name '{}' hasn't been found in the database.", 
									file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym, Subcategory.class.getName(), INDEFINITE_SUBCATEGORY);
							productParsingResult.addWarning(row.getRowNum(), "Reference on instance of '" + Subcategory.class.getName() + "' with name '" + INDEFINITE_SUBCATEGORY + "' hasn't been found in the database.");
						}
						
						product.setIsActive(true);
						
						if (columnMapper.isBasePriceMapped()) {
							Cell chainProductBasePrice = row.getCell(columnMapper.getBasePriceColumnIndex());
							
							if ((chainProductBasePrice != null) && (chainProductBasePrice.getCellType() == CellType.NUMERIC)) {
								chainProduct.setBasePrice(new BigDecimal(chainProductBasePrice.getNumericCellValue()).setScale(2, RoundingMode.HALF_UP));
							}	
						}
						
						if (columnMapper.isDiscountPriceMapped()) {
							Cell chainProductDiscountPrice = row.getCell(columnMapper.getDiscountPriceColumnIndex());
							
							if ((chainProductDiscountPrice != null) && (chainProductDiscountPrice.getCellType() == CellType.NUMERIC)) {
								chainProduct.setDiscountPrice(new BigDecimal(chainProductDiscountPrice.getNumericCellValue()).setScale(2, RoundingMode.HALF_UP));
							}	
						}
						
						if ((chainProduct.getBasePrice() != null) && (chainProduct.getDiscountPrice() != null)) {
							BigDecimal difference = chainProduct.getBasePrice().subtract(chainProduct.getDiscountPrice());
							BigDecimal discount = difference.divide(chainProduct.getBasePrice(), new MathContext(10, RoundingMode.HALF_UP));
							BigDecimal discountPercent = discount.scaleByPowerOfTen(2).setScale(0, RoundingMode.HALF_UP);
							chainProduct.setDiscountPercent(discountPercent);
						}
						
						if (columnMapper.isStartDateMapped()) {
							Cell chainProductStartDate = row.getCell(columnMapper.getStartDateColumnIndex());
							
							if ((chainProductStartDate != null) && (chainProductStartDate.getCellType() == CellType.NUMERIC)) {
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(chainProductStartDate.getDateCellValue());
								chainProduct.setStartDate(calendar);
							}
						}
						
						if (columnMapper.isEndDateMapped()) {
							Cell chainProductEndDate = row.getCell(columnMapper.getEndDateColumnIndex());
							
							if ((chainProductEndDate != null) && (chainProductEndDate.getCellType() == CellType.NUMERIC)) {
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(chainProductEndDate.getDateCellValue());
								chainProduct.setEndDate(calendar);
							}
						}
						
						Optional<Chain> chain = chainRepository.findBySynonym(chainSynonym);
						
						if (chain.isPresent()) {
							chainProduct.setChain(chain.get());
						} else {
							logger.warn("Source: '{}'. Sheet: '{}'. Row number: '{}' Target instances: '{}' and '{}'. Chain synonym: '{}'. Reference on instance of '{}' with synonym '{}' hasn't been found in the database.", 
									file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym, Chain.class.getName(), chainSynonym);
							chainProductParsingResult.addWarning(row.getRowNum(), "Reference on instance of '" + Chain.class.getName() + "' with synonym '" + chainSynonym + "' hasn't been found in the database.");
						}
						
						if (columnMapper.isTypeMapped()) {
							Cell chainProductType = row.getCell(columnMapper.getTypeColumnIndex());
							
							if ((chainProductType != null) && (chainProductType.getCellType() == CellType.STRING)) {
								String chainProductTypeSign = chainProductType.getStringCellValue().trim();
								String chainProductTypeSynonym = null;
								
								switch (chainProductTypeSign) {
									case CHAIN_PRODUCT_TYPE_DISCOUNT_SIGN:
										chainProductTypeSynonym = CHAIN_PRODUCT_TYPE_DISCOUNT_SYNONYM;
										break;
									
									case CHAIN_PRODUCT_TYPE_ONE_PLUS_ONE_SIGN:
										chainProductTypeSynonym = CHAIN_PRODUCT_TYPE_ONE_PLUS_ONE_SYNONYM;
										break;
										
									default:
										chainProductTypeSynonym = CHAIN_PRODUCT_TYPE_NICE_PRICE_SYNONYM;
								}
																
								Optional<ChainProductType> type = chainProductTypeRepository.findBySynonym(chainProductTypeSynonym);
								
								if (type.isPresent()) {
									chainProduct.setType(type.get());
								} else {
									logger.warn("Source: '{}'. Sheet: '{}'. Row number '{}' Target instances: '{}' and '{}'. Chain synonym: '{}'. Reference on instance of '{}' with synonym '{}' hasn't been found in the database.", 
											file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym, ChainProductType.class.getName(), chainProductTypeSynonym);
									chainProductParsingResult.addWarning(row.getRowNum(), "Reference on instance of '" + ChainProductType.class.getName() + "' with synonym '" + chainProductTypeSynonym + "' hasn't been found in the database.");
								}
							}
						}
						
						Set<ConstraintViolation<Product>> productConstraintViolations = validator.validate(product);
						
						if (productConstraintViolations.size() == 0) {
							Optional<Product> productFromDB = productRepository.findByNameAndBarcodeAndUnitOfMeasure(product.getName(), product.getBarcode(), product.getUnitOfMeasure());
							
							if (productFromDB.isPresent()) {
								chainProduct.setProduct(productFromDB.get());
								logger.warn("Source: '{}'. Sheet: '{}'. Row number: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. New instance of '{}' has been already presented in the database.",
										file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym, Product.class.getName());
								logger.warn(product.toString());
								productParsingResult.increaseNumberOfUnsavedInstances();
								productParsingResult.increaseNumberOfAlreadyExistingInstances();
								productParsingResult.addWarning(row.getRowNum(), INSTANCE_WAS_PRESENTED_WARNING);
							} else {								
								try {
									if (productRepository.save(product) != null) {
										chainProduct.setProduct(product);
										logger.info("Source: '{}'. Sheet: '{}'. Row number '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. New instance of '{}' has been created and saved to the database.",
												file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym, Product.class.getName());
										logger.info(product.toString());
										productParsingResult.increaseNumberOfSavedInstances();
									}
								} catch (Exception savingException) {
									logger.error("Source: '{}'. Sheet: '{}'. Row number: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Exception has occurred during the saving of new instance of '{}' to the database: {}.", 
											file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym, Product.class.getName(), printStackTrace(savingException));
									productParsingResult.increaseNumberOfUnsavedInstances();
									productParsingResult.addException(row.getRowNum(), savingException);
								}
							}
						} else {
							chainProduct.setProduct(product);
							logger.error("Source: '{}'. Sheet: '{}'. Row number '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Exception(s) has occurred during the validation of new instance of '{}'. See validation exception(s) below.", 
									file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym, Product.class.getName());
							productParsingResult.increaseNumberOfUnsavedInstances();
							productParsingResult.increaseNumberOfInvalidInstances();
							
							for (ConstraintViolation<Product> violation : productConstraintViolations) {
								logger.error(VALIDATION_EXCEPTION, violation.getMessage());
								productParsingResult.addConstraintViolation(row.getRowNum(), violation);
							}
						}
						
						Set<ConstraintViolation<ChainProduct>> chainProductConstraintViolations = validator.validate(chainProduct);
						
						if (chainProductConstraintViolations.size() == 0) {
							if (chainProductRepository.findByStartDateAndEndDateAndBasePriceAndDiscountPriceAndTypeIdAndChainIdAndProductId(
									chainProduct.getStartDate(), 
									chainProduct.getEndDate(),
									chainProduct.getBasePrice(),
									chainProduct.getDiscountPrice(),
									chainProduct.getType() == null ? null : chainProduct.getType().getId(),
									chainProduct.getChain() == null ? null : chainProduct.getChain().getId(),
									chainProduct.getProduct() == null ? null : chainProduct.getProduct().getId()).isPresent()) {
								logger.warn("Source: '{}'. Sheet: '{}'. Row number '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. New instance has been already presented in the database.",
										file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym);
								logger.warn(chainProduct.toString());
								chainProductParsingResult.increaseNumberOfUnsavedInstances();
								chainProductParsingResult.increaseNumberOfAlreadyExistingInstances();
								chainProductParsingResult.addWarning(row.getRowNum(), INSTANCE_WAS_PRESENTED_WARNING);
							} else {
								try {
									if (chainProductRepository.save(chainProduct) != null) {
										logger.info("Source: '{}'. Sheet: '{}'. Row number '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. New instance has been created and saved to the database.",
												file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym);
										logger.info(chainProduct.toString());
										chainProductParsingResult.increaseNumberOfSavedInstances();
									}
								} catch (Exception savingException) {
									logger.error("Source: '{}'. Sheet: '{}'. Row number '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Exception has occurred during the saving of new instance to the database: {}.", 
											file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym, printStackTrace(savingException));
									chainProductParsingResult.increaseNumberOfUnsavedInstances();
									chainProductParsingResult.addException(row.getRowNum(), savingException);
								}
							}	
						} else {
							logger.error("Source: '{}'. Sheet: '{}'. Row number '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Exception(s) has occurred during the validation of new instance. See validation exception(s) below.", 
									file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym);
							chainProductParsingResult.increaseNumberOfUnsavedInstances();
							chainProductParsingResult.increaseNumberOfInvalidInstances();
							
							for (ConstraintViolation<ChainProduct> violation : chainProductConstraintViolations) {
								logger.error(VALIDATION_EXCEPTION, violation.getMessage());
								chainProductParsingResult.addConstraintViolation(row.getRowNum(), violation);
							}
						}
					}
					
					logger.info("Source: '{}'. Sheet: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Finishing working with sheet.", 
							FILE_WITH_BASIC_DATA, sheet.getSheetName(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym);
					logger.info("Source: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Finishing process of parsing.", 
							FILE_WITH_BASIC_DATA, Product.class.getName(), ChainProduct.class.getName(), chainSynonym);
				} else {
					logger.warn("Source: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Sheet at index '{}' wasn't found.", file, Product.class.getName(), ChainProduct.class.getName(), chainSynonym, 0);
					productParsingResult.addCommonWarning("Sheet at index '" + 0 + "' wasn't found.");
					chainProductParsingResult.addCommonWarning("Sheet at index '" + 0 + "' wasn't found.");
				}				
			} 		
		} catch (IOException ioException) {
			logger.error("Source: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Input-output exception has occurred during opening the file: {}.", 
					file, Product.class.getName(), ChainProduct.class.getName(), chainSynonym, printStackTrace(ioException));
			productParsingResult.addCommonException(ioException);
			chainProductParsingResult.addCommonException(ioException);
		} 
		
		productParsingResult.setFinishTime();
		chainProductParsingResult.setFinishTime();
		logger.info("Source: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Returning result of parsing. {}", 
				file, Product.class.getName(), ChainProduct.class.getName(), chainSynonym, productParsingResult.toString());
		logger.info("Source: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Returning result of parsing. {}", 
				file, Product.class.getName(), ChainProduct.class.getName(), chainSynonym, chainProductParsingResult.toString());
		results.add(productParsingResult);
		results.add(chainProductParsingResult);
		return results;
	}
}
	