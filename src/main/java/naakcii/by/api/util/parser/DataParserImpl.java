package naakcii.by.api.util.parser;

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
import naakcii.by.api.util.parser.enumeration.EnumerationParsingResult;
import naakcii.by.api.util.parser.multisheet.MultisheetParsingResult;
import naakcii.by.api.util.parser.multisheet.mapper.CategoryColumnMapper;
import naakcii.by.api.util.parser.multisheet.mapper.ChainColumnMapper;
import naakcii.by.api.util.parser.multisheet.mapper.ChainProductTypeColumnMapper;
import naakcii.by.api.util.parser.multisheet.mapper.ChainProductColumnMapper;
import naakcii.by.api.util.parser.multisheet.mapper.SubcategoryColumnMapper;

/**
 * Provides the base implementation of the {@link naakcii.by.api.util.parser.DataParser} interface 
 * for use, when information is retrieved from the <i>.xls</i> or <i>.xlsx</i> files or 
 * <i>enumeration classes</i>. 
 * Provides utility methods for parsing instances of the next classes:
 * <ul>
 *   <li>{@link naakcii.by.api.category.Catgory}</li>
 *   <li>{@link naakcii.by.api.chain.Chain}</li>
 *   <li>{@link naakcii.by.api.chainproduct.ChainProduct}</li>
 *   <li>{@link naakcii.by.api.chainproducttype.ChainProductType}</li>
 *   <li>{@link naakcii.by.api.country.Country}</li>
 *   <li>{@link naakcii.by.api.product.Product}</li>
 *   <li>{@link naakcii.by.api.subcategory.Subcategory}</li>
 *   <li>{@link naakcii.by.api.unitofmeasure.UnitOfMeasure}</li>
 * </ul>
 * 
 * @see naakcii.by.api.util.parser.DataParser
 * @see naakcii.by.api.category.Category
 * @see naakcii.by.api.chain.Chain
 * @see naakcii.by.api.chainproduct.ChainProduct
 * @see naakcii.by.api.chainproducttype.ChainProductType
 * @see naakcii.by.api.country.Country
 * @see naakcii.by.api.product.Product
 * @see naakcii.by.api.subcategory.Subcategory
 * @see naakcii.by.api.unitofmeasure.UnitOfMeasure
 */
@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class DataParserImpl implements DataParser{
	
	private static final Logger logger = LogManager.getLogger(DataParser.class);
	private static Validator validator;
	
	/**
	 * {@value #FILE_WITH_BASIC_DATA} is the full name of the file containing information for 
	 * creating <i>basic data</i>.
	 */
	private static final String FILE_WITH_BASIC_DATA = 
			"src" + File.separator + "test" + File.separator + "resources" + File.separator + "Basic_data.xlsx";
	
	/**
	 * {@value SHEET_WITH_BASIC_CHAIN_PRODUCT_TYPES} is the name of the sheet in the 
	 * {@code file with basic data}, that contains information for creating <i>basic instances</i> 
	 * of the {@code Chain Product Type} class.
	 */
	private static final String SHEET_WITH_BASIC_CHAIN_PRODUCT_TYPES = "Basic_chain_product_types";
	
	/**
	 * {@value SHEET_WITH_BASIC_CATEGORIES} is the name of the sheet in the 
	 * {@code file with basic data}, that contains information for creating <i>basic instances</i> 
	 * of the {@code Category} class.
	 */
	private static final String SHEET_WITH_BASIC_CATEGORIES = "Basic_categories";
	
	/**
	 * {@value SHEET_WITH_BASIC_SUBCATEGORIES} is the name of the sheet in the 
	 * {@code file with basic data}, that contains information for creating <i>basic instances</i> 
	 * of the {@code Subcategory} class.
	 */
	private static final String SHEET_WITH_BASIC_SUBCATEGORIES = "Basic_subcategories";
	
	/**
	 * {@value SHEET_WITH_BASIC_CHAINS} is the name of the sheet in the 
	 * {@code file with basic data}, that contains information for creating <i>basic instances</i> 
	 * of the {@code Chain} class.
	 */
	private static final String SHEET_WITH_BASIC_CHAINS = "Basic_chains";
	
	/**
	 * {@value IS_ACTIVE} is the possible values to set {@code is active} parameter as {@code true}. 
	 * This values can be used to fill cells in columns responsible for {@code activity} property of 
	 * the next entities:
	 * <ul>
	 *   <li>{@code Category};</li>
	 *   <li>{@code Subcategory};</li>
	 *   <li>{@code Chain}.</li>
	 * </ul>
	 */
	private static final String[] IS_ACTIVE = {"Да", "да", "ДА"};
	
	/**
	 * {@value IS_NOT_ACTIVE} is the possible values to set {@code is active} parameter as {@code false}.
	 * This values can be used to fill cells in columns responsible for {@code activity} property of 
	 * the next entities:
	 * <ul>
	 *   <li>{@code Category};</li>
	 *   <li>{@code Subcategory};</li>
	 *   <li>{@code Chain}.</li>
	 * </ul>
	 */
	private static final String[] IS_NOT_ACTIVE = {"Нет", "нет", "НЕТ"};
	
	/**
	 * {@value INDEFINITE_CATEGORY} is the name of the <i>Indefinite category</i>.
	 */
	private static final String INDEFINITE_CATEGORY = "Indefinite category";
	
	/**
	 * {@value INDEFINITE_SUBCATEGORY} is the name of the <i>Indefinite subcategory</i>.
	 */
	private static final String INDEFINITE_SUBCATEGORY = "Indefinite subcategory";
	
	/**
	 * {@value CHAIN_PRODUCT_TYPE_ONE_PLUS_ONE_SIGN} is the only possible value to set reference to 
	 * the instance of the {@code Chain Product Type} class describing <i>One Plus One</i> type.
	 * This value can be used to fill cells in column responsible for {@code type} property of the 
	 * {@code Chain Product} class.
	 */
	private static final String CHAIN_PRODUCT_TYPE_ONE_PLUS_ONE_SIGN = "Z050";
	
	/**
	 * {@value CHAIN_PRODUCT_TYPE_ONE_PLUS_ONE_SIGN} is the only possible value to set reference to 
	 * the instance of the {@code Chain Product Type} class describing <i>Discount</i> type.
	 * This value can be used to fill cells in column responsible for {@code type} property of the 
	 * {@code Chain Product} class.
	 */
	private static final String CHAIN_PRODUCT_TYPE_DISCOUNT_SIGN = "Z040";
	
	/**
	 * {@value CHAIN_PRODUCT_TYPE_ONE_PLUS_ONE_SYNONYM} is the <i>synonym</i> of the 
	 * {@code Chain Product Type} class instance representing <i>One Plus One</i> type.
	 */
	private static final String CHAIN_PRODUCT_TYPE_ONE_PLUS_ONE_SYNONYM = "one_plus_one";
	
	/**
	 * {@value CHAIN_PRODUCT_TYPE_ONE_PLUS_ONE_SYNONYM} is the <i>synonym</i> of the 
	 * {@code Chain Product Type} class instance representing <i>Discount</i> type.
	 */
	private static final String CHAIN_PRODUCT_TYPE_DISCOUNT_SYNONYM = "discount";
	
	/**
	 * {@value CHAIN_PRODUCT_TYPE_ONE_PLUS_ONE_SYNONYM} is the <i>synonym</i> of the 
	 * {@code Chain Product Type} class instance representing <i>Good Price</i> type.
	 */
	private static final String CHAIN_PRODUCT_TYPE_NICE_PRICE_SYNONYM = "good_price";
	
	/**
	 * The message pattern that describes preparing for the <i>parsing process</i>.
	 * Contains two parameters:
	 * <ul>
	 *   <li>
	 *     the string containing the name of the {@code source enumeration class} retrieving 
	 *     of data is made from;
	 *   </li>
	 *   <li>
	 *     the string containing the name of the {@code target class}, instances of which are 
	 *     created during the <i>parsing process</i>.
	 *   </li>
	 * </ul>
	 * 
	 * @see naakcii.by.api.util.parser.ParsingResult#targetClass
	 * @see naakcii.by.api.util.parser.enumeration.EnumerationParsingResult#sourceEnumeration
	 */
	private static final String ENUM_PREPAIRING_FOR_PARSING_MESSAGE = 
			"Source enumeration: '{}'. Target entity: '{}'. Preparing for the parsing process.";
	
	/**
	 * The message pattern that describes starting of the <i>parsing process</i>.
	 * Contains two parameters:
	 * <ul>
	 *   <li>
	 *     the string containing the name of the {@code source enumeration class} retrieving 
	 *     of data is made from;
	 *   </li>
	 *   <li>
	 *     the string containing the name of the {@code target class}, instances of which are 
	 *     created during the <i>parsing process</i>.
	 *   </li>
	 * </ul>
	 * 
	 * @see naakcii.by.api.util.parser.ParsingResult#targetClass
	 * @see naakcii.by.api.util.parser.enumeration.EnumerationParsingResult#sourceEnumeration
	 */
	private static final String ENUM_STARTING_PARSING_MESSAGE = 
			"Source enumeration: '{}'. Target entity: '{}'. Starting process of parsing.";
	
	/**
	 * The message pattern that describes occurrence of <i>constraint violation(s)</i> during the 
	 * validation of the newly created {@code target class} instance.
	 * Contains three parameters:
	 * <ul>
	 *   <li>
	 *     the string containing the name of the {@code source enumeration class} retrieving 
	 *     of data is made from;
	 *   </li>
	 *   <li>
	 *     the instance of the {@code source enumeration class} used for creating current instance 
	 *     of the {@code target class};
	 *   </li>
	 *   <li>
	 *     the string containing the name of the {@code target class}, instances of which are 
	 *     created during the <i>parsing process</i>.
	 *   </li>
	 * </ul>
	 * 
	 * @see naakcii.by.api.util.parser.ParsingResult#targetClass
	 * @see naakcii.by.api.util.parser.enumeration.EnumerationParsingResult#sourceEnumeration
	 */
	private static final String ENUM_VIOLATION_DURING_VALIDATION_MESSAGE = 
			"Source enumeration: '{}'. Code: '{}'. Target entity: '{}'. " + 
			"Violation(s) has occurred during the validation of the created instance. See validation exception(s) below.";
	
	/**
	 * The message pattern that describes situation, when newly created instance of the 
	 * {@code target class} has been already presented in the database.
	 * Contains three parameters:
	 * <ul>
	 *   <li>
	 *     the string containing the name of the {@code source enumeration class} retrieving 
	 *     of data is made from;
	 *   </li>
	 *   <li>
	 *     the instance of the {@code source enumeration class} used for creating current instance 
	 *     of the {@code target class};
	 *   </li>
	 *   <li>
	 *     the string containing the name of the {@code target class}, instances of which are 
	 *     created during the <i>parsing process</i>.
	 *   </li>
	 * </ul>
	 * 
	 * @see naakcii.by.api.util.parser.ParsingResult#targetClass
	 * @see naakcii.by.api.util.parser.enumeration.EnumerationParsingResult#sourceEnumeration
	 */
	private static final String ENUM_INSTANCE_WAS_PRESENTED_MESSAGE = 
			"Source enumeration: '{}'. Code: '{}'. Target entity: '{}'. " + 
			"Created instance has been already presented in the database.";
	
	/**
	 * The message pattern that describes occurrence of <i>exception</i> during the saving of the 
	 * newly created {@code target class} instance to the database.
	 * Contains four parameters:
	 * <ul>
	 *   <li>
	 *     the string containing the name of the {@code source enumeration class} retrieving 
	 *     of data is made from;
	 *   </li>
	 *   <li>
	 *     the instance of the {@code source enumeration class} used for creating current instance 
	 *     of the {@code target class};
	 *   </li>
	 *   <li>
	 *     the string containing the name of the {@code target class}, instances of which are 
	 *     created during the <i>parsing process</i>;
	 *   </li>
	 *   <li>
	 *     the string containing description of the occurred <i>saving exception</i>.
	 *   </li>
	 * </ul>
	 * 
	 * @see naakcii.by.api.util.parser.ParsingResult#targetClass
	 * @see naakcii.by.api.util.parser.enumeration.EnumerationParsingResult#sourceEnumeration
	 */
	private static final String ENUM_EXCEPTION_DURING_SAVING_MESSAGE = 
			"Source enumeration: '{}'. Code: '{}'. Target entity: '{}'. " + 
			"Exception has occurred during the saving of the created instance to the database: {}.";
	
	/**
	 * The message pattern that describes successful saving of the newly created 
	 * {@code target class} instance to the database.
	 * Contains three parameters:
	 * <ul>
	 *   <li>
	 *     the string containing the name of the {@code source enumeration class} retrieving 
	 *     of data is made from;
	 *   </li>
	 *   <li>
	 *     the instance of the {@code source enumeration class} used for creating current instance 
	 *     of the {@code target class};
	 *   </li>
	 *   <li>
	 *     the string containing the name of the {@code target class}, instances of which are 
	 *     created during the <i>parsing process</i>.
	 *   </li>
	 * </ul>
	 * 
	 * @see naakcii.by.api.util.parser.ParsingResult#targetClass
	 * @see naakcii.by.api.util.parser.enumeration.EnumerationParsingResult#sourceEnumeration
	 */
	private static final String ENUM_INSTANCE_WAS_SAVED_MESSAGE = 
			"Source enumeration: '{}'. Code: '{}'. Target entity: '{}'. " + 
			"New instance has been created and saved to the database.";
	
	/**
	 * The message pattern that describes finishing of the <i>parsing process</i>.
	 * Contains two parameters:
	 * <ul>
	 *   <li>
	 *     the string containing the name of the {@code source enumeration class} retrieving 
	 *     of data is made from;
	 *   </li>
	 *   <li>
	 *     the string containing the name of the {@code target class}, instances of which are 
	 *     created during the <i>parsing process</i>.
	 *   </li>
	 * </ul>
	 * 
	 * @see naakcii.by.api.util.parser.ParsingResult#targetClass
	 * @see naakcii.by.api.util.parser.enumeration.EnumerationParsingResult#sourceEnumeration
	 */
	private static final String ENUM_FINISHING_PARSING_MESSAGE = 
			"Source enumeration: '{}'. Target entity: '{}'. Finishing process of parsing.";
	
	/**
	 * The message pattern that describes the result of the completed <i>parsing process</i>.
	 * Contains three parameters:
	 * <ul>
	 *   <li>
	 *     the string containing the name of the {@code source enumeration class} retrieving 
	 *     of data is made from;
	 *   </li>
	 *   <li>
	 *     the string containing the name of the {@code target class}, instances of which are 
	 *     created during the <i>parsing process</i>;
	 *   </li>
	 *   <li>
	 *     the string containing {@code result} of the finished <i>parsing process</i>.
	 *   </li>
	 * </ul>
	 * 
	 * @see naakcii.by.api.util.parser.ParsingResult#targetClass
	 * @see naakcii.by.api.util.parser.enumeration.EnumerationParsingResult#sourceEnumeration
	 * @see naakcii.by.api.util.parser.ParsingResult#toString()
	 */
	private static final String ENUM_RESULT_OF_PARSING_MESSAGE = 
			"Source enumeration: '{}'. Target entity: '{}'. Returning result of the parsing process. {}";
	
	/**
	 * The message pattern that describes preparing for the <i>parsing process</i>.
	 * Contains two parameters:
	 * <ul>
	 *   <li>
	 *     the string containing the name of the <i>.xls</i> or <i>.xlsx</i> file, retrieving of 
	 *     data is made from;
	 *   </li>
	 *   <li>
	 *     the string containing the name of the {@code target class}, instances of which are 
	 *     created during the <i>parsing process</i>.
	 *   </li>
	 * </ul>
	 * 
	 * @see naakcii.by.api.util.parser.ParsingResult#targetClass
	 * @see naakcii.by.api.util.parser.ParsingResult#source
	 */
	private static final String FILE_PREPAIRING_FOR_PARSING_MESSAGE = 
			"Source file: '{}'. Target entity: '{}'. Preparing for the parsing process.";
	
	/**
	 * The message pattern that describes starting of the <i>parsing process</i>.
	 * Contains two parameters:
	 * <ul>
	 *   <li>
	 *     the string containing the name of the <i>.xls</i> or <i>.xlsx</i> file, retrieving of 
	 *     data is made from;
	 *   </li>
	 *   <li>
	 *     the string containing the name of the {@code target class}, instances of which are 
	 *     created during the <i>parsing process</i>.
	 *   </li>
	 * </ul>
	 * 
	 * @see naakcii.by.api.util.parser.ParsingResult#targetClass
	 * @see naakcii.by.api.util.parser.ParsingResult#source
	 */
	private static final String FILE_STARTING_PARSING_MESSAGE = 
			"Source file: '{}'. Target entity: '{}'. Starting process of parsing.";
	
	/**
	 * The message pattern that describes situation, when needed {@code sheet} in the <i>.xls</i> 
	 * or <i>.xlsx</i> file wasn't found.
	 * Contains three parameters:
	 * <ul>
	 *   <li>
	 *     the string containing the name of the <i>.xls</i> or <i>.xlsx</i> file, retrieving of 
	 *     data is made from;
	 *   </li>
	 *   <li>
	 *     the string containing the name of the {@code target class}, instances of which are 
	 *     created during the <i>parsing process</i>;
	 *   </li>
	 *   <li>
	 *     the string containing the {@code name of the required sheet}.
	 *   </li>
	 * </ul>
	 * 
	 * @see naakcii.by.api.util.parser.ParsingResult#targetClass
	 * @see naakcii.by.api.util.parser.ParsingResult#source
	 * @see naakcii.by.api.util.parser.multisheet.MultisheetParsingResult#sheetName
	 */
	private static final String FILE_SHEET_WAS_NOT_FOUND_MESSAGE = 
			"Source file: '{}'. Target entity: '{}'. Sheet with name '{}' wasn't found.";
	
	/**
	 * The message pattern that describes starting of the <i>parsing process</i>.
	 * Contains three parameters:
	 * <ul>
	 *   <li>
	 *     the string containing the name of the <i>.xls</i> or <i>.xlsx</i> file, retrieving of 
	 *     data is made from;
	 *   </li>
	 *   <li>
	 *     the string containing the {@code name of the sheet} involved in the <i>parsing 
	 *     process</i>;
	 *   </li>
	 *   <li>
	 *     the string containing the name of the {@code target class}, instances of which are 
	 *     created during the <i>parsing process</i>.
	 *   </li>
	 * </ul>
	 * 
	 * @see naakcii.by.api.util.parser.ParsingResult#targetClass
	 * @see naakcii.by.api.util.parser.ParsingResult#source
	 * @see naakcii.by.api.util.parser.multisheet.MultisheetParsingResult#sheetName
	 */
	private static final String FILE_STARTING_WORKING_WITH_SHEET_MESSAGE = 
			"Source file: '{}'. Sheet: '{}'. Target entity: '{}'. Starting working with sheet.";
	
	/**
	 * The message pattern that describes the result of the completed <i>column mapping process</i>.
	 * Contains four parameters:
	 * <ul>
	 *   <li>
	 *     the string containing the name of the <i>.xls</i> or <i>.xlsx</i> file, retrieving of 
	 *     data is made from;
	 *   </li>
	 *   <li>
	 *     the string containing the {@code name of the sheet} involved in the <i>parsing 
	 *     process</i>;
	 *   </li>
	 *   <li>
	 *     the string containing the name of the {@code target class}, instances of which are 
	 *     created during the <i>parsing process</i>.
	 *   </li>
	 *   <li>
	 *     the string containing {@ code result} of the finished <i>column mapping process</i>.
	 *   </li>
	 * </ul>
	 * 
	 * @see naakcii.by.api.util.parser.ParsingResult#targetClass
	 * @see naakcii.by.api.util.parser.ParsingResult#source
	 * @see naakcii.by.api.util.parser.multisheet.MultisheetParsingResult#sheetName
	 * @see naakcii.by.api.util.parser.multisheet.mapper.ColumnMapper
	 */
	private static final String FILE_RESULT_OF_COLUMN_MAPPING_MESSAGE = 
			"Source file: '{}'. Sheet: '{}'. Target entity: '{}'. Returning result of the column mapping process. {}";
	
	/**
	 * The message pattern that describes situation, when instance of a certain class wasn't 
	 * found in the database and reference on this entity will be {@code null} in the newly created 
	 * instance of the {@code target class}.
	 * Contains six parameters:
	 * <ul>
	 *   <li>
	 *     the string containing the name of the <i>.xls</i> or <i>.xlsx</i> file, retrieving of 
	 *     data is made from;
	 *   </li>
	 *   <li>
	 *     the string containing the {@code name of the sheet} involved in the <i>parsing 
	 *     process</i>;
	 *   </li>
	 *   <li>
	 *     the number of the table row used for creating current instance of the {@code target class};
	 *   </li>
	 *   <li>
	 *     the string containing the name of the {@code target class}, instances of which are 
	 *     created during the <i>parsing process</i>;
	 *   </li>
	 *   <li>
	 *     the string containing the name of a certain class, instance of which wasn't found in the 
	 *     database;
	 *   </li>
	 *   <li>
	 *     the string containing a value of the reference, more often it is the name of the 
	 *     required instance.
	 *   </li>
	 * </ul>
	 * 
	 * @see naakcii.by.api.util.parser.ParsingResult#targetClass
	 * @see naakcii.by.api.util.parser.ParsingResult#source
	 * @see naakcii.by.api.util.parser.multisheet.MultisheetParsingResult#sheetName
	 */
	private static final String FILE_REFERENCE_ON_INSTANCE_WAS_NOT_FOUND_MESSAGE = 
			"Source file: '{}'. Sheet: '{}'. Row number: '{}' Target entity: '{}'. " + 
			"Instance of the '{}' class with name '{}' wasn't found in the database. Reference on this entity will not be written in.";
	
	/**
	 * The message pattern that describes occurrence of <i>constraint violation(s)</i> during the 
	 * validation of the newly created {@code target class} instance.
	 * Contains four parameters:
	 * <ul>
	 *   <li>
	 *     the string containing the name of the <i>.xls</i> or <i>.xlsx</i> file, retrieving of 
	 *     data is made from;
	 *   </li>
	 *   <li>
	 *     the string containing the {@code name of the sheet} involved in the <i>parsing 
	 *     process</i>;
	 *   </li>
	 *   <li>
	 *     the number of the table row used for creating current instance of the {@code target class};
	 *   </li>
	 *   <li>
	 *     the string containing the name of the {@code target class}, instances of which are 
	 *     created during the <i>parsing process</i>.
	 *   </li>
	 * </ul>
	 * 
	 * @see naakcii.by.api.util.parser.ParsingResult#targetClass
	 * @see naakcii.by.api.util.parser.ParsingResult#source
	 * @see naakcii.by.api.util.parser.multisheet.MultisheetParsingResult#sheetName
	 */
	private static final String FILE_VIOLATION_DURING_VALIDATION_MESSAGE = 
			"Source file: '{}'. Sheet: '{}'. Row number: '{}'. Target instance: '{}'. " + 
			"Violation(s) has occurred during the validation of new instance. See validation exception(s) below.";
	
	/**
	 * The message pattern that describes situation, when newly created instance of the 
	 * {@code target class} has been already presented in the database.
	 * Contains four parameters:
	 * <ul>
	 *   <li>
	 *     the string containing the name of the <i>.xls</i> or <i>.xlsx</i> file, retrieving of 
	 *     data is made from;
	 *   </li>
	 *   <li>
	 *     the string containing the {@code name of the sheet} involved in the <i>parsing 
	 *     process</i>;
	 *   </li>
	 *   <li>
	 *     the number of the table row used for creating current instance of the {@code target class};
	 *   </li>
	 *   <li>
	 *     the string containing the name of the {@code target class}, instances of which are 
	 *     created during the <i>parsing process</i>.
	 *   </li>
	 * </ul>
	 * 
	 * @see naakcii.by.api.util.parser.ParsingResult#targetClass
	 * @see naakcii.by.api.util.parser.ParsingResult#source
	 * @see naakcii.by.api.util.parser.multisheet.MultisheetParsingResult#sheetName
	 */
	private static final String FILE_INSTANCE_WAS_PRESENTED_MESSAGE = 
			"Source file: '{}'. Sheet: '{}'. Row number: '{}'. Target instance: '{}'. " + 
			"New instance has been already presented in the database.";
	
	/**
	 * The message pattern that describes occurrence of <i>exception</i> during the saving
	 * of the newly created {@code target class} instance to the database.
	 * Contains five parameters:
	 * <ul>
	 *   <li>
	 *     the string containing the name of the <i>.xls</i> or <i>.xlsx</i> file, retrieving of 
	 *     data is made from;
	 *   </li>
	 *   <li>
	 *     the string containing the {@code name of the sheet} involved in the <i>parsing 
	 *     process</i>;
	 *   </li>
	 *   <li>
	 *     the number of the table row used for creating current instance of the {@code target class};
	 *   </li>
	 *   <li>
	 *     the string containing the name of the {@code target class}, instances of which are 
	 *     created during the <i>parsing process</i>;
	 *   </li>
	 *   <li>
	 *     the string containing description of the occurred <i>saving exception</i>.
	 *   </li>
	 * </ul>
	 * 
	 * @see naakcii.by.api.util.parser.ParsingResult#targetClass
	 * @see naakcii.by.api.util.parser.ParsingResult#source
	 * @see naakcii.by.api.util.parser.multisheet.MultisheetParsingResult#sheetName
	 */
	private static final String FILE_EXCEPTION_DURING_SAVING_MESSAGE = 
			"Source file: '{}'. Sheet: '{}'. Row number: '{}'. Target instance: '{}'. " + 
			"Exception has occurred during the saving of new instance to the database: {}.";
	
	/**
	 * The message pattern that describes successful saving of the newly created 
	 * {@code target class} instance to the database.
	 * Contains four parameters:
	 * <ul>
	 *   <li>
	 *     the string containing the name of the <i>.xls</i> or <i>.xlsx</i> file, retrieving of 
	 *     data is made from;
	 *   </li>
	 *   <li>
	 *     the string containing the {@code name of the sheet} involved in the <i>parsing 
	 *     process</i>;
	 *   </li>
	 *   <li>
	 *     the number of the table row used for creating current instance of the {@code target class};
	 *   </li>
	 *   <li>
	 *     the string containing the name of the {@code target class}, instances of which are 
	 *     created during the <i>parsing process</i>.
	 *   </li>
	 * </ul>
	 * 
	 * @see naakcii.by.api.util.parser.ParsingResult#targetClass
	 * @see naakcii.by.api.util.parser.ParsingResult#source
	 * @see naakcii.by.api.util.parser.multisheet.MultisheetParsingResult#sheetName
	 */
	private static final String FILE_INSTANCE_WAS_SAVED_MESSAGE = 
			"Source file: '{}'. Sheet: '{}'. Row number: '{}'. Target instance: '{}'. " + 
			"New instance has been created and saved to the database.";
	private static final String FILE_FINISHING_WORKING_WITH_SHEET_MESSAGE = "File: '{}'. Sheet: '{}'. Target instance: '{}'. Finishing working with sheet.";
	
	/**
	 * The message pattern that describes finishing of the <i>parsing process</i>.
	 * Contains two parameters:
	 * <ul>
	 *   <li>
	 *     the string containing the name of the <i>.xls</i> or <i>.xlsx</i> file, retrieving of 
	 *     data is made from;
	 *   </li>
	 *   <li>
	 *     the string containing the name of the {@code target class}, instances of which are 
	 *     created during the <i>parsing process</i>.
	 *   </li>
	 * </ul>
	 * 
	 * @see naakcii.by.api.util.parser.ParsingResult#targetClass
	 * @see naakcii.by.api.util.parser.ParsingResult#source
	 */
	private static final String FILE_FINISHING_PARSING_MESSAGE = 
			"Source file: '{}'. Target instance: '{}'. Finishing process of parsing.";
	
	/**
	 * The message pattern that describes the result of the completed <i>parsing process</i>.
	 * Contains three parameters:
	 * <ul>
	 *   <li>
	 *     the string containing the name of the <i>.xls</i> or <i>.xlsx</i> file, retrieving of 
	 *     data is made from;
	 *   </li>
	 *   <li>
	 *     the string containing the name of the {@code target class}, instances of which are 
	 *     created during the <i>parsing process</i>.
	 *   </li>
	 *   <li>
	 *   the string containing result of the finished <i>parsing process</i>.
	 *   </li>
	 * </ul>
	 * 
	 * @see naakcii.by.api.util.parser.ParsingResult#targetClass
	 * @see naakcii.by.api.util.parser.ParsingResult#source
	 * @see naakcii.by.api.util.parser.ParsingResult#toString()
	 */
	private static final String FILE_RESULT_OF_PARSING_MESSAGE = 
			"Source file: '{}'. Target instance: '{}'. Returning result of parsing. {}";
	
	/**
	 * {@value INSTANCE_WAS_PRESENTED_WARNING} is the warning message to pass to the 
	 * {@code parsing result}, when newly created instance of the {@code target class} 
	 * has been already presented in the database.
	 * 
	 * @see naakcii.by.api.util.parser.ParsingResult
	 * @see naakcii.by.api.util.parser.ParsingResult#addWarning(Object, String)
	 */
	private static final String INSTANCE_WAS_PRESENTED_WARNING = "New instance has been already presented in the database.";
	
	/**
	 * The message pattern that describes <i>constraint violation</i> occurred during the validation 
	 * of the newly created {@code target class}  instance.
	 * Contains single parameter: the string containing description of the occurred 
	 * <i>constraint violation</i>.
	 */
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
	
	/**
	 * Constructs a new instance of the {@code data parser} specifying necessary collaborators.
	 * 
	 * @param objectFactory the instance of {@code Object Factory} class used for creating new 
	 * instances of required classes
	 * @param chainRepository the implementation of {@code Chain Repository} interface used for 
	 * searching, modifying and saving to the database instances of the {@code Chain} class
	 * @param categoryRepository the implementation of {@code Category Repository} interface used 
	 * for searching, modifying and saving to the database instances of the {@code Category} class
	 * @param subcategoryRepository the implementation of {@code Subcategory Repository} interface 
	 * used for searching, modifying and saving to the database instances of the 
	 * {@code Subcategory} class
	 * @param productRepository the implementation of {@code Product Repository} interface used 
	 * for searching, modifying and saving to the database instances of the {@code Product} class
	 * @param chainProductRepository the implementation of {@code Chain Product Repository} 
	 * interface used for searching, modifying and saving to the database instances of the 
	 * {@code Chain Product} class
	 * @param chainProductTypeRepository the implementation of {@code Chain Product Type Repository} 
	 * interface used for searching, modifying and saving to the database instances of the 
	 * {@code Chain Product Type} class
	 * @param countryRepository the implementation of {@code Country Repository} interface used 
	 * for searching, modifying and saving to the database instances of the {@code Country} class
	 * @param unitOfMeasureRepository the implementation of {@code Unit Of Measure Repository} 
	 * interface used for searching, modifying and saving to the database instances of the 
	 * {@code Unit Of Measure} class
	 * @see naakcii.by.api.util.ObjectFactory
	 * @see naakcii.by.api.chain.ChainRepository
	 * @see naakcii.by.api.category.CategoryRepository
	 * @see naakcii.by.api.subcategory.SubcategoryRepository
	 * @see naakcii.by.api.product.ProductRepository
	 * @see naakcii.by.api.chainproduct.ChainProductRepository
	 * @see naakcii.by.api.country.CountryRepository
	 * @see naakcii.by.api.unitofmeasure.UnitOfMeasureRepository
	 */
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
			UnitOfMeasureRepository unitOfMeasureRepository) {
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
	
	/**
	 * Implements method {@link naakcii.by.api.util.parser.DataParser#parseBasicData()} of the 
	 * master {@code Data Parser} interface.
	 * 
	 * @return the list of {@code parsing results} for each {@code target class} of the <i>basic 
	 * data</i>
	 * @see naakcii.by.api.util.parser.DataParser
	 * @see naakcii.by.api.util.parser.multisheet.ParsingResult
	 */
	@Override
	public List<ParsingResult<?, ?>> parseBasicData() {
		List<ParsingResult<?, ?>> results = parseBasicData(
				UnitOfMeasure.class, 
				Country.class, 
				ChainProductType.class, 
				Chain.class, 
				Category.class, 
				Subcategory.class);
		return results;
	}
	
	/**
	 * Retrieves, validates, checks on uniqueness and saves to the database instances of classes 
	 * being a part of the <i>basic data</i>.
	 * The list of classes, instances of which are involved in the <i>parsing process</i>, is 
	 * defined as a parameter of the method and can include the next classes:
	 * <ul>
	 *   <li>{@code naakcii.by.api.category.Category}</li>
	 *   <li>{@code naakcii.by.api.chain.Chain}</li>
	 *   <li>{@code naakcii.by.api.chainproducttype.ChainProductType}</li>
	 *   <li>{@code naakcii.by.api.country.Country}</li>
	 *   <li>{@code naakcii.by.api.subcategory.Subcategory}</li>
	 *   <li>{@code naakcii.by.api.unitofmeasure.UnitOfMeasure}</li>
	 * </ul>
	 * 
	 * @param classes variable number of classes instances of which are involved in the <i>parsing process</i>
	 * @return the list of {@code parsing results} for each of the aforenamed {@code target classes} 
	 * if such class was passed in the parameter of the method
	 * @see naakcii.by.api.util.parser.DataParser
	 * @see naakcii.by.api.util.parser.multisheet.ParsingResult
	 */
	protected List<ParsingResult<?, ?>> parseBasicData(Class<?> ... classes) {
		List<ParsingResult<?, ?>> results = new ArrayList<>();
		List<Class<?>> targetClasses = new ArrayList<>();
		targetClasses.addAll(Arrays.asList(classes));
		
		if (targetClasses.isEmpty()) {
			logger.warn("Creating of basic data has skipped due to empty list of target classes.");
		} else {
			logger.info("Creating of basic data has started.");
			logger.info("Target classes: {}.", targetClasses.stream().map(Class::getName).collect(Collectors.joining("; ")));
			
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

	/**
	 * Retrieves, validates, checks on uniqueness and saves to the database instances of the 
	 * {@code naakcii.by.api.country.Country} class.
	 * {@code Source enumeration class} for the <i>parsing process</i> is {@code naakcii.by.api.country.CountryCode}.
	 * 
	 * @return {@code enumeration parsing result} containing detailed information about the 
	 * completed <i>parsing process</i> of the {@code Country} class instances
	 * @see naakcii.by.api.country.Country
	 * @see naakcii.by.api.util.parser.enumeration.EnumerationParsingResult
	 */
	EnumerationParsingResult<Country, CountryCode> createCountries() {
		logger.info(ENUM_PREPAIRING_FOR_PARSING_MESSAGE, CountryCode.class, Country.class.getName());
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		/*
		 * Creates instance of the naakcii.by.api.util.parser.enumeration.EnumerationParsingResult 
		 * class to accumulate detailed information about the parsing process.
		 */
		EnumerationParsingResult<Country, CountryCode> parsingResult = 
				objectFactory.getInstance(EnumerationParsingResult.class, Country.class, CountryCode.class);
		parsingResult.setStartTime();
		logger.info(ENUM_STARTING_PARSING_MESSAGE, CountryCode.class, Country.class.getName());
		
		/*
		 * Iterates through the all elements of the naakcii.by.api.country.CountryCode enumeration 
		 * and tries to build a new instance of the naakcii.by.api.country.Country class from each 
		 * element of the enumeration.
		 */
		for (CountryCode countryCode : CountryCode.values()) {
			parsingResult.increaseTotalNumberOfInstances();
			/*
			 * Creates new instance of the nakcii.by.api.country.Country class from the current 
			 * element of the naakcii.by.api.country.CountryCode enumeration.
			 */
			Country country = objectFactory.getInstance(Country.class, countryCode);
			/*
			 * Validates newly created instance of the naakcii.by.api.country.Country class.
			 */
			Set<ConstraintViolation<Country>> constraintViolations = validator.validate(country);
			
			/*
			 * If there are not any constraint violations then a new instance of the 
			 * nakcii.by.api.country.Country class will be checked on uniqueness, else all occurred 
			 * constraint violations will be written to the log and to the enumeration parsing result.
			 */
			if (constraintViolations.size() == 0) {
				/*
				 * Checks on uniqueness a new instance of the nakcii.by.api.country.Country class 
				 * (i.e. checks if such instance has been already presented in the database) according 
				 * to the following properties of the entity:
				 * - Country#alphaCode2;
				 * - Country#alphaCode3.
				 * If check on uniqueness has passed successfully then a new instance is saved to 
				 * the database, else an appropriate warning is written to the log and to the 
				 * enumeration parsing result.
				 */
				if (countryRepository.findByAlphaCode2AndAlphaCode3(country.getAlphaCode2(), country.getAlphaCode3()).isPresent()) {
					logger.warn(ENUM_INSTANCE_WAS_PRESENTED_MESSAGE, CountryCode.class, countryCode, Country.class.getName());
					logger.warn(country.toString());
					parsingResult.increaseNumberOfUnsavedInstances();
					parsingResult.increaseNumberOfAlreadyExistingInstances();
					parsingResult.addWarning(countryCode, INSTANCE_WAS_PRESENTED_WARNING);
				} else {
					/*
					 * Tries to save newly created instance of the naakcii.by.api.country.Country 
					 * class to the database. 
					 * If an exception has occurred during the saving process than it is written to 
					 * the log and to the enumeration parsing result.
					 */
					try {
						if (countryRepository.save(country) != null) {
							logger.info(ENUM_INSTANCE_WAS_SAVED_MESSAGE, CountryCode.class, countryCode, Country.class.getName());
							logger.info(country.toString());
							parsingResult.increaseNumberOfSavedInstances();
						}
					} catch (Exception savingException) {
						logger.error(ENUM_EXCEPTION_DURING_SAVING_MESSAGE, CountryCode.class, countryCode, Country.class.getName(), printStackTrace(savingException));
						parsingResult.increaseNumberOfUnsavedInstances();
						parsingResult.addException(countryCode, savingException);
					}
				}	
			} else {
				logger.error(ENUM_VIOLATION_DURING_VALIDATION_MESSAGE, CountryCode.class, countryCode, Country.class.getName());
				parsingResult.increaseNumberOfUnsavedInstances();
				parsingResult.increaseNumberOfInvalidInstances();
				
				for (ConstraintViolation<Country> violation : constraintViolations) {
					logger.error(VALIDATION_EXCEPTION, violation.getMessage());
					parsingResult.addConstraintViolation(countryCode, violation);
				}
			}
		}
		
		logger.info(ENUM_FINISHING_PARSING_MESSAGE, CountryCode.class, Country.class.getName());	
		parsingResult.setFinishTime();
		/*
		 * Writes a detailed description of the completed parsing process to the log.
		 */
		logger.info(ENUM_RESULT_OF_PARSING_MESSAGE,	CountryCode.class, Country.class.getName(), parsingResult.toString());
		return parsingResult;
	}
	
	/**
	 * Retrieves, validates, checks on uniqueness and saves to the database instances of the 
	 * {@code naakcii.by.api.unitofmeasure.UnitOfMeasure} class.
	 * {@code Source enumeration class} for the <i>parsing process</i> is {@code naakcii.by.api.unitofmeasure.UnitCode}.
	 * 
	 * @return {@code enumeration parsing result} containing detailed information about the 
	 * completed <i>parsing process</i> of the {@code Unit of Measure} class instances
	 * @see naakcii.by.api.unitofmeasure.UnitOfMeasure
	 * @see naakcii.by.api.util.parser.enumeration.EnumerationParsingResult
	 */
	EnumerationParsingResult<UnitOfMeasure, UnitCode> createUnitsOfMeasure() {
		logger.info(ENUM_PREPAIRING_FOR_PARSING_MESSAGE, UnitCode.class, UnitOfMeasure.class.getName());
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		/*
		 * Creates instance of the naakcii.by.api.util.parser.enumeration.EnumerationParsingResult 
		 * class to accumulate detailed information about the parsing process.
		 */
		EnumerationParsingResult<UnitOfMeasure, UnitCode> parsingResult = objectFactory.getInstance(EnumerationParsingResult.class, UnitOfMeasure.class, UnitCode.class);
		parsingResult.setStartTime();
		logger.info(ENUM_STARTING_PARSING_MESSAGE, UnitCode.class, UnitOfMeasure.class.getName());
		
		/*
		 * Iterates through the all elements of the naakcii.by.api.unitofmeasure.UnitCode 
		 * enumeration and tries to build a new instance of the naakcii.by.api.unitofmeasure.UnitOfMeasure 
		 * class from each element of the enumeration.
		 */
		for (UnitCode unitCode : UnitCode.values()) {
			parsingResult.increaseTotalNumberOfInstances();
			/*
			 * Creates new instance of the naakcii.by.api.unitofmeasure.UnitOfMeasure class from 
			 * the current element of the naakcii.by.api.unitofmeasure.UnitCode enumeration.
			 */
			UnitOfMeasure unitOfMeasure = objectFactory.getInstance(UnitOfMeasure.class, unitCode);
			/*
			 * Validates newly created instance of the naakcii.by.api.unitofmeasure.UnitOfMeasure 
			 * class.
			 */
			Set<ConstraintViolation<UnitOfMeasure>> constraintViolations = validator.validate(unitOfMeasure);
			
			/*
			 * If there are not any constraint violations then a new instance of the 
			 * naakcii.by.api.unitofmeasure.UnitOfMeasure class will be checked on uniqueness, 
			 * else all occurred constraint violations will be written to the log and to the 
			 * enumeration parsing result.
			 */
			if (constraintViolations.size() == 0) {
				/*
				 * Checks on uniqueness a new instance of the naakcii.by.api.unitofmeasure.UnitOfMeasure 
				 * class (i.e. checks if such instance has been already presented in the database) 
				 * according to the following properties of the entity:
				 * - UnitOfMeasure#name.
				 * If check on uniqueness has passed successfully then a new instance is saved to 
				 * the database, else an appropriate warning is written to the log and to the 
				 * enumeration parsing result.
				 */
				if (unitOfMeasureRepository.findByNameIgnoreCase(unitOfMeasure.getName()).isPresent()) {
					logger.warn(ENUM_INSTANCE_WAS_PRESENTED_MESSAGE, UnitCode.class, unitCode, UnitOfMeasure.class.getName());
					logger.warn(unitOfMeasure.toString());
					parsingResult.increaseNumberOfUnsavedInstances();
					parsingResult.increaseNumberOfAlreadyExistingInstances();
					parsingResult.addWarning(unitCode, INSTANCE_WAS_PRESENTED_WARNING);
				} else {
					/*
					 * Tries to save newly created instance of the naakcii.by.api.unitofmeasure.UnitOfMeasure 
					 * class to the database. 
					 * If an exception has occurred during the saving process than it is written to 
					 * the log and to the enumeration parsing result.
					 */
					try {
						if (unitOfMeasureRepository.save(unitOfMeasure) != null) {
							logger.info(ENUM_INSTANCE_WAS_SAVED_MESSAGE, UnitCode.class, unitCode, UnitOfMeasure.class.getName());
							logger.info(unitOfMeasure.toString());
							parsingResult.increaseNumberOfSavedInstances();
						}
					} catch (Exception savingException) {
						logger.error(ENUM_EXCEPTION_DURING_SAVING_MESSAGE, UnitCode.class, unitCode, UnitOfMeasure.class.getName(), printStackTrace(savingException));
						parsingResult.increaseNumberOfUnsavedInstances();
						parsingResult.addException(unitCode, savingException);
					}
				}	
			} else {
				logger.error(ENUM_VIOLATION_DURING_VALIDATION_MESSAGE, UnitCode.class, unitCode, UnitOfMeasure.class.getName());
				parsingResult.increaseNumberOfUnsavedInstances();
				parsingResult.increaseNumberOfInvalidInstances();
				
				for (ConstraintViolation<UnitOfMeasure> violation : constraintViolations) {
					logger.error(VALIDATION_EXCEPTION, violation.getMessage());
					parsingResult.addConstraintViolation(unitCode, violation);
				}
			}
		}
		
		logger.info(ENUM_FINISHING_PARSING_MESSAGE, UnitCode.class, UnitOfMeasure.class.getName());	
		parsingResult.setFinishTime();
		/*
		 * Writes a detailed description of the completed parsing process to the log.
		 */
		logger.info(ENUM_RESULT_OF_PARSING_MESSAGE,	UnitCode.class, UnitOfMeasure.class.getName(), parsingResult.toString());
		return parsingResult;
	}
	
	/**
	 * Retrieves, validates, checks on uniqueness and saves to the database instances of the 
	 * {@code naakcii.by.api.chainproducttype.ChainProductType} class.
	 * {@code Source} for the <i>parsing process</i> is <i>.xlsx</i> file 
	 * {@value #FILE_WITH_BASIC_DATA}, {@code name of the sheet} is {@value SHEET_WITH_BASIC_CHAIN_PRODUCT_TYPES}.
	 * 
	 * @param book the XSSFWorkbook representing the <i>.xlsx</i> file
	 * @return {@code multisheet parsing result} containing detailed information about the 
	 * completed <i>parsing process</i> of the {@code Chain Product Type} class instances
	 * @see naakcii.by.api.chainproducttype.ChainProductType
	 * @see naakcii.by.api.util.parser.multisheet.MultisheetParsingResult
	 */
	private MultisheetParsingResult<ChainProductType> createBasicChainProductTypes(XSSFWorkbook book) {
		logger.info(FILE_PREPAIRING_FOR_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, ChainProductType.class.getName());
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		/*
		 * Creates instance of the naakcii.by.api.util.parser.multisheet.MultisheetParsingResult 
		 * class to accumulate detailed information about the parsing process.
		 */
		MultisheetParsingResult<ChainProductType> parsingResult = objectFactory.getInstance(MultisheetParsingResult.class, ChainProductType.class, FILE_WITH_BASIC_DATA);
		parsingResult.setStartTime();
		logger.info(FILE_STARTING_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, ChainProductType.class.getName());		
		XSSFSheet sheet = book.getSheet(SHEET_WITH_BASIC_CHAIN_PRODUCT_TYPES);
				
			if (sheet != null) {
				parsingResult.setSheetName(sheet.getSheetName());
				parsingResult.setSheetIndex(book.getSheetIndex(sheet));
				logger.info(FILE_STARTING_WORKING_WITH_SHEET_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), ChainProductType.class.getName());
				/*
				 * Creates instance of the naakcii.by.api.util.parser.multisheet.mapper.ChainProductTypeColumnMapper 
				 * to map columns of the table on the properties of the naakcii.by.api.chainproducttype.ChainProductType 
				 * entity.
				 */
				ChainProductTypeColumnMapper columnMapper = objectFactory.getInstance(ChainProductTypeColumnMapper.class);
				Iterator<Row> rowIterator = sheet.iterator();
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				
				/*
				 * Reads the header row of the table and maps each column on a single property of 
				 * the naakcii.by.api.chainproducttype.ChainProductType class.
				 */
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					columnMapper.mapColumn(cell.getStringCellValue(), cell.getColumnIndex());
				}
				
				/*
				 * Writes a detailed description of the completed mapping process to the log.
				 */
				logger.info(FILE_RESULT_OF_COLUMN_MAPPING_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), ChainProductType.class.getName(), columnMapper.toString());		
				
				/*
				 * Reads rows of the table one by one and tries to build a new instance of the 
				 * naakcii.by.api.chainproducttype.ChainProductType class from the data containing 
				 * in each row.
				 */
				while (rowIterator.hasNext()) {
					row = rowIterator.next();
					/*
					 * Creates a new instance of the naakcii.by.api.chainproducttype.ChainProductType 
					 * class. All fields of the new instance aren't populated with any data yet.
					 */		
					ChainProductType chainProductType = objectFactory.getInstance(ChainProductType.class);
					parsingResult.increaseTotalNumberOfInstances();
					
					/*
					 * Sets 'name' property value of the newly created instance of the 
					 * naakcii.by.api.chainproducttype.ChainProductType class if this property 
					 * has been mapped on the appropriate column of the table.
					 */
					if (columnMapper.isNameMapped()) {
						Cell chainProductTypeName = row.getCell(columnMapper.getNameColumnIndex());
						
						if ((chainProductTypeName != null) && (chainProductTypeName.getCellType() == CellType.STRING)) {
							chainProductType.setName(chainProductTypeName.getStringCellValue().trim());
						}	
					}
					
					/*
					 * Sets 'synonym' property value of the newly created instance of the 
					 * naakcii.by.api.chainproducttype.ChainProductType class if this property 
					 * has been mapped on the appropriate column of the table.
					 */
					if (columnMapper.isSynonymMapped()) {
						Cell chainProductTypeSynonym = row.getCell(columnMapper.getSynonymColumnIndex());
						
						if ((chainProductTypeSynonym != null) && (chainProductTypeSynonym.getCellType() == CellType.STRING)) {
							System.out.println(chainProductTypeSynonym.getCellType());
							chainProductType.setSynonym(chainProductTypeSynonym.getStringCellValue().trim());
						}	
					}
					
					/*
					 * Sets 'tooltip' property value of the newly created instance of the 
					 * naakcii.by.api.chainproducttype.ChainProductType class if this property 
					 * has been mapped on the appropriate column of the table.
					 */
					if (columnMapper.isTooltipMapped()) {
						Cell chainProductTypeTooltip = row.getCell(columnMapper.getTooltipColumnIndex());
						
						if ((chainProductTypeTooltip != null) && (chainProductTypeTooltip.getCellType() == CellType.STRING)) {
							chainProductType.setTooltip(chainProductTypeTooltip.getStringCellValue().trim());
						}	
					}
					
					/*
					 * Validates newly created instance of the naakcii.by.api.chainproducttype.ChainProductType 
					 * class.
					 */
					Set<ConstraintViolation<ChainProductType>> constraintViolations = validator.validate(chainProductType);
					
					/*
					 * If there are not any constraint violations then a new instance of the 
					 * naakcii.by.api.chainproducttype.ChainProductType class will be checked 
					 * on uniqueness, else all occurred constraint violations will be written 
					 * to the log and to the multisheet parsing result.
					 */
					if (constraintViolations.size() == 0) {
						/*
						 * Checks on uniqueness a new instance of the naakcii.by.api.chainproducttype.ChainProductType 
						 * class (i.e. checks if such instance has been already presented in the database) 
						 * according to the following properties of the entity:
						 * - ChainProductType#name;
						 * - ChainProductType#synonym.
						 * If check on uniqueness has passed successfully then a new instance is 
						 * saved to the database, else an appropriate warning is written to the 
						 * log and to the multisheet parsing result.
						 */
						if (chainProductTypeRepository.findByNameAndSynonym(chainProductType.getName(), chainProductType.getSynonym()).isPresent()) {
							logger.warn(FILE_INSTANCE_WAS_PRESENTED_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), ChainProductType.class.getName());
							logger.warn(chainProductType.toString());
							parsingResult.increaseNumberOfUnsavedInstances();
							parsingResult.increaseNumberOfAlreadyExistingInstances();
							parsingResult.addWarning(row.getRowNum(), INSTANCE_WAS_PRESENTED_WARNING);
						} else {
							/*
							 * Tries to save newly created instance of the naakcii.by.api.chainproducttype.ChainProductType 
							 * class to the database. 
							 * If an exception has occurred during the saving process than it is written to 
							 * the log and to the multisheet parsing result.
							 */
							try {
								if (chainProductTypeRepository.save(chainProductType) != null) {
									logger.info(FILE_INSTANCE_WAS_SAVED_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), ChainProductType.class.getName());
									logger.info(chainProductType.toString());
									parsingResult.increaseNumberOfSavedInstances();
								}
							} catch (Exception savingException) {
								logger.error(FILE_EXCEPTION_DURING_SAVING_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), ChainProductType.class.getName(), printStackTrace(savingException));
								parsingResult.increaseNumberOfUnsavedInstances();
								parsingResult.addException(row.getRowNum(), savingException);
							}
						}	
					} else { 
						logger.error(FILE_VIOLATION_DURING_VALIDATION_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), ChainProductType.class.getName());
						parsingResult.increaseNumberOfUnsavedInstances();
						parsingResult.increaseNumberOfInvalidInstances();
							
						for (ConstraintViolation<ChainProductType> violation : constraintViolations) {
							logger.error(VALIDATION_EXCEPTION, violation.getMessage());
							parsingResult.addConstraintViolation(row.getRowNum(), violation);
						}
					}
				}
					
				logger.info(FILE_FINISHING_WORKING_WITH_SHEET_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), ChainProductType.class.getName());
				logger.info(FILE_FINISHING_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, ChainProductType.class.getName());
				} else {
					logger.warn(FILE_SHEET_WAS_NOT_FOUND_MESSAGE, FILE_WITH_BASIC_DATA, ChainProductType.class.getName(), SHEET_WITH_BASIC_CHAIN_PRODUCT_TYPES);
					parsingResult.addCommonWarning("Sheet with name '" + SHEET_WITH_BASIC_CHAIN_PRODUCT_TYPES + "' wasn't found.");
				}
			 		
		parsingResult.setFinishTime();
		/*
		 * Writes a detailed description of the completed parsing process to the log.
		 */
		logger.info(FILE_RESULT_OF_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, ChainProductType.class.getName(), parsingResult.toString());
		return parsingResult;
	}
	
	/**
	 * Retrieves, validates, checks on uniqueness and saves to the database instances of the 
	 * {@code naakcii.by.api.chain.Chain} class.
	 * {@code Source} for the <i>parsing process</i> is <i>.xlsx</i> file 
	 * {@value #FILE_WITH_BASIC_DATA}, {@code name of the sheet} is {@value SHEET_WITH_BASIC_CHAINS}.
	 * 
	 * @param book the XSSFWorkbook representing the <i>.xlsx</i> file
	 * @return {@code multisheet parsing result} containing detailed information about the 
	 * completed <i>parsing process</i> of the {@code Chain} class instances
	 * @see naakcii.by.api.chain.Chain
	 * @see naakcii.by.api.util.parser.multisheet.MultisheetParsingResult
	 */
	private MultisheetParsingResult<Chain> createBasicChains(XSSFWorkbook book) {
		logger.info(FILE_PREPAIRING_FOR_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, Chain.class.getName());
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		/*
		 * Creates instance of the naakcii.by.api.util.parser.multisheet.MultisheetParsingResult 
		 * class to accumulate detailed information about the parsing process.
		 */
		MultisheetParsingResult<Chain> parsingResult = objectFactory.getInstance(MultisheetParsingResult.class, Chain.class, FILE_WITH_BASIC_DATA);
		parsingResult.setStartTime();
		logger.info(FILE_STARTING_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, Chain.class.getName());
		XSSFSheet sheet = book.getSheet(SHEET_WITH_BASIC_CHAINS);
			
		if (sheet != null) {
			parsingResult.setSheetName(sheet.getSheetName());
			parsingResult.setSheetIndex(book.getSheetIndex(sheet));
			logger.info(FILE_STARTING_WORKING_WITH_SHEET_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), Chain.class.getName());
			/*
			 * Creates instance of the naakcii.by.api.util.parser.multisheet.mapper.ChainColumnMapper 
			 * to map columns of the table on the properties of the naakcii.by.api.chain.Chain 
			 * entity.
			 */
			ChainColumnMapper columnMapper = objectFactory.getInstance(ChainColumnMapper.class);
			Iterator<Row> rowIterator = sheet.iterator();
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			
			/*
			 * Reads the header row of the table and maps each column on a single property of 
			 * the naakcii.by.api.chain.Chain class.
			 */
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				columnMapper.mapColumn(cell.getStringCellValue(), cell.getColumnIndex());
			}
			
			/*
			 * Writes a detailed description of the completed mapping process to the log.
			 */
			logger.info(FILE_RESULT_OF_COLUMN_MAPPING_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), Chain.class.getName(), columnMapper.toString());			
			
			/*
			 * Reads rows of the table one by one and tries to build a new instance of the 
			 * naakcii.by.api.chain.Chain class from the data containing in each row.
			 */
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				/*
				 * Creates a new instance of the naakcii.by.api.chain.Chain class. All fields of 
				 * the new instance aren't populated with any data yet.
				 */	
				Chain chain = objectFactory.getInstance(Chain.class);
				parsingResult.increaseTotalNumberOfInstances();
				
				/*
				 * Sets 'name' property value of the newly created instance of the 
				 * naakcii.by.api.chain.Chain class if this property has been mapped on the 
				 * appropriate column of the table.
				 */
				if (columnMapper.isNameMapped()) {
					Cell chainName = row.getCell(columnMapper.getNameColumnIndex());
						
					if ((chainName != null) && (chainName.getCellType() == CellType.STRING)) {
						chain.setName(chainName.getStringCellValue().trim());
					}	
				}
				
				/*
				 * Sets 'synonym' property value of the newly created instance of the 
				 * naakcii.by.api.chain.Chain class if this property has been mapped on the 
				 * appropriate column of the table.
				 */
				if (columnMapper.isSynonymMapped()) {
					Cell chainSynonym = row.getCell(columnMapper.getSynonymColumnIndex());
							
					if ((chainSynonym != null) && (chainSynonym.getCellType() == CellType.STRING)) {
						chain.setSynonym(chainSynonym.getStringCellValue().trim());
					}	
				}
				
				/*
				 * Sets 'link' property value of the newly created instance of the 
				 * naakcii.by.api.chain.Chain class if this property has been mapped on the 
				 * appropriate column of the table.
				 */
				if (columnMapper.isLinkMapped()) {
					Cell chainLink = row.getCell(columnMapper.getLinkColumnIndex());
						
					if ((chainLink != null) && (chainLink.getCellType() == CellType.STRING)) {
						chain.setLink(chainLink.getStringCellValue().trim());
					}	
				}
				
				/*
				 * Sets 'logo' property value of the newly created instance of the 
				 * naakcii.by.api.chain.Chain class if this property has been mapped on the 
				 * appropriate column of the table.
				 */
				if (columnMapper.isLogoMapped()) {
					Cell chainLogo = row.getCell(columnMapper.getLogoColumnIndex());
						
					if ((chainLogo != null) && (chainLogo.getCellType() == CellType.STRING)) {
						chain.setLogo(chainLogo.getStringCellValue().trim());
					}	
				}
				
				/*
				 * Sets 'activity' property value of the newly created instance of the 
				 * naakcii.by.api.chain.Chain class if this property has been mapped on the 
				 * appropriate column of the table.
				 */
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
				
				/*
				 * Validates newly created instance of the naakcii.by.api.chain.Chain class.
				 */
				Set<ConstraintViolation<Chain>> constraintViolations = validator.validate(chain);
				
				/*
				 * If there are not any constraint violations then a new instance of the 
				 * naakcii.by.api.chain.Chain class will be checked on uniqueness, else all 
				 * occurred constraint violations will be written to the log and to the 
				 * multisheet parsing result.
				 */
				if (constraintViolations.size() == 0) {
					/*
					 * Checks on uniqueness a new instance of the naakcii.by.api.chain.Chain 
					 * class (i.e. checks if such instance has been already presented in the database) 
					 * according to the following properties of the entity:
					 * - Chain#name;
					 * - Chain#synonym.
					 * If check on uniqueness has passed successfully then a new instance is saved 
					 * to the database, else an appropriate warning is written to the log and to 
					 * the multisheet parsing result.
					 */
					if (chainRepository.findByNameAndSynonym(chain.getName(), chain.getSynonym()).isPresent()) {
						logger.warn(FILE_INSTANCE_WAS_PRESENTED_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Chain.class);
						logger.warn(chain.toString());
						parsingResult.increaseNumberOfUnsavedInstances();
						parsingResult.increaseNumberOfAlreadyExistingInstances();
						parsingResult.addWarning(row.getRowNum(), INSTANCE_WAS_PRESENTED_WARNING);
					} else {
						/*
						 * Tries to save newly created instance of the naakcii.by.api.chain.Chain 
						 * class to the database. 
						 * If an exception has occurred during the saving process than it is written to 
						 * the log and to the multisheet parsing result.
						 */
						try {
							if (chainRepository.save(chain) != null) {
								logger.info(FILE_INSTANCE_WAS_SAVED_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Chain.class.getName());
								logger.info(chain.toString());
								parsingResult.increaseNumberOfSavedInstances();
							}
						} catch (Exception savingException) {
							logger.error(FILE_EXCEPTION_DURING_SAVING_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Chain.class.getName(), printStackTrace(savingException));
							parsingResult.increaseNumberOfUnsavedInstances();
							parsingResult.addException(row.getRowNum(), savingException);
						}
					}	
				} else {
					logger.error(FILE_VIOLATION_DURING_VALIDATION_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Chain.class.getName());
					parsingResult.increaseNumberOfUnsavedInstances();
					parsingResult.increaseNumberOfInvalidInstances();
						
					for (ConstraintViolation<Chain> violation : constraintViolations) {
						logger.error(VALIDATION_EXCEPTION, violation.getMessage());
						parsingResult.addConstraintViolation(row.getRowNum(), violation);
					}
				}
			}
					
			logger.info(FILE_FINISHING_WORKING_WITH_SHEET_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), Chain.class.getName());
			logger.info(FILE_FINISHING_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, Chain.class.getName());
		} else {
			logger.warn(FILE_SHEET_WAS_NOT_FOUND_MESSAGE, FILE_WITH_BASIC_DATA, Chain.class.getName(), SHEET_WITH_BASIC_CHAINS);
			parsingResult.addCommonWarning("Sheet with name '" + SHEET_WITH_BASIC_CHAINS + "' wasn't found.");
		}					
		
		parsingResult.setFinishTime();
		/*
		 * Writes a detailed description of the completed parsing process to the log.
		 */
		logger.info(FILE_RESULT_OF_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, Chain.class.getName(), parsingResult.toString());
		return parsingResult;
	}
	
	/**
	 * Retrieves, validates, checks on uniqueness and saves to the database instances of the 
	 * {@code naakcii.by.api.category.Category} class.
	 * {@code Source} for the <i>parsing process</i> is <i>.xlsx</i> file 
	 * {@value #FILE_WITH_BASIC_DATA}, {@code name of the sheet} is {@value SHEET_WITH_BASIC_CATEGORIES}.
	 * 
	 * @param book the XSSFWorkbook representing the <i>.xlsx</i> file
	 * @return {@code multisheet parsing result} containing detailed information about the 
	 * completed <i>parsing process</i> of the {@code Category} class instances
	 * @see naakcii.by.api.category.Category
	 * @see naakcii.by.api.util.parser.multisheet.MultisheetParsingResult
	 */
	private MultisheetParsingResult<Category> createBasicCategories(XSSFWorkbook book) {
		logger.info(FILE_PREPAIRING_FOR_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, Category.class.getName());
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		/*
		 * Creates instance of the naakcii.by.api.util.parser.multisheet.MultisheetParsingResult 
		 * class to accumulate detailed information about the parsing process.
		 */
		MultisheetParsingResult<Category> parsingResult = objectFactory.getInstance(MultisheetParsingResult.class, Category.class, FILE_WITH_BASIC_DATA);
		parsingResult.setStartTime();
		logger.info(FILE_STARTING_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, Category.class.getName());
		XSSFSheet sheet = book.getSheet(SHEET_WITH_BASIC_CATEGORIES);
				
		if (sheet != null) {
			parsingResult.setSheetName(sheet.getSheetName());
			parsingResult.setSheetIndex(book.getSheetIndex(sheet));
			logger.info(FILE_STARTING_WORKING_WITH_SHEET_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), Category.class.getName());
			/*
			 * Creates instance of the naakcii.by.api.util.parser.multisheet.mapper.CategoryColumnMapper 
			 * to map columns of the table on the properties of the naakcii.by.api.category.Category
			 * entity.
			 */
			CategoryColumnMapper columnMapper = objectFactory.getInstance(CategoryColumnMapper.class);
			Iterator<Row> rowIterator = sheet.iterator();
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			
			/*
			 * Reads the header row of the table and maps each column on a single property of 
			 * the naakcii.by.api.category.Category class.
			 */
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				columnMapper.mapColumn(cell.getStringCellValue(), cell.getColumnIndex());
			}
			
			/*
			 * Writes a detailed description of the completed mapping process to the log.
			 */
			logger.info(FILE_RESULT_OF_COLUMN_MAPPING_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), Category.class.getName(), columnMapper.toString());
			
			/*
			 * Reads rows of the table one by one and tries to build a new instance of the 
			 * naakcii.by.api.category.Category class from the data containing in each row.
			 */
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				/*
				 * Creates a new instance of the naakcii.by.api.category.Category class. All fields 
				 * of the new instance aren't populated with any data yet.
				 */	
				Category category = objectFactory.getInstance(Category.class);
				parsingResult.increaseTotalNumberOfInstances();
				
				/*
				 * Sets 'name' property value of the newly created instance of the 
				 * naakcii.by.api.category.Category class if this property has been mapped on the 
				 * appropriate column of the table.
				 */
				if (columnMapper.isNameMapped()) {
					Cell categoryName = row.getCell(columnMapper.getNameColumnIndex());
					
					if ((categoryName != null) && (categoryName.getCellType() == CellType.STRING)) {
						category.setName(categoryName.getStringCellValue().trim());
					}	
				}
				
				/*
				 * Sets 'icon' property value of the newly created instance of the 
				 * naakcii.by.api.category.Category class if this property has been mapped on the 
				 * appropriate column of the table.
				 */
				if (columnMapper.isIconMapped()) {
					Cell categoryIcon = row.getCell(columnMapper.getIconColumnIndex());
						
					if ((categoryIcon != null) && (categoryIcon.getCellType() == CellType.STRING)) {
						category.setIcon(categoryIcon.getStringCellValue().trim());
					}	
				}
				
				/*
				 * Sets 'priority' property value of the newly created instance of the 
				 * naakcii.by.api.category.Category class if this property has been mapped on the 
				 * appropriate column of the table.
				 */
				if (columnMapper.isPriorityMapped()) {
					Cell categoryPriority = row.getCell(columnMapper.getPriorityColumnIndex());
					
					if ((categoryPriority != null) && (categoryPriority.getCellType() == CellType.NUMERIC)) {
						category.setPriority((int) categoryPriority.getNumericCellValue());
					}	
				}
				
				/*
				 * Sets 'activity' property value of the newly created instance of the 
				 * naakcii.by.api.category.Category class if this property has been mapped on the 
				 * appropriate column of the table.
				 */
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
				
				/*
				 * Validates newly created instance of the naakcii.by.api.category.Category class.
				 */
				Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);
				
				/*
				 * If there are not any constraint violations then a new instance of the 
				 * naakcii.by.api.category.Category class will be checked on uniqueness, else all 
				 * occurred constraint violations will be written to the log and to the 
				 * multisheet parsing result.
				 */
				if (constraintViolations.size() == 0) {
					/*
					 * Checks on uniqueness a new instance of the naakcii.by.api.category.Category 
					 * class (i.e. checks if such instance has been already presented in the database) 
					 * according to the following properties of the entity:
					 * - Chain#name;
					 * - Chain#synonym.
					 * If check on uniqueness has passed successfully then a new instance is saved 
					 * to the database, else an appropriate warning is written to the log and to 
					 * the multisheet parsing result.
					 */
					if (categoryRepository.findByName(category.getName()).isPresent()) {
						logger.warn(FILE_INSTANCE_WAS_PRESENTED_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Category.class.getName());
						logger.warn(category.toString());
						parsingResult.increaseNumberOfUnsavedInstances();
						parsingResult.increaseNumberOfAlreadyExistingInstances();
						parsingResult.addWarning(row.getRowNum(), INSTANCE_WAS_PRESENTED_WARNING);
					} else {
						/*
						 * Tries to save newly created instance of the naakcii.by.api.category.Category 
						 * class to the database. 
						 * If an exception has occurred during the saving process than it is written to 
						 * the log and to the multisheet parsing result.
						 */
						try {
							if (categoryRepository.save(category) != null) {
								logger.info(FILE_INSTANCE_WAS_SAVED_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Category.class.getName());
								logger.info(category.toString());
								parsingResult.increaseNumberOfSavedInstances();
							}
						} catch (Exception savingException) {
							logger.error(FILE_EXCEPTION_DURING_SAVING_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Category.class.getName(), printStackTrace(savingException));
							parsingResult.increaseNumberOfUnsavedInstances();
							parsingResult.addException(row.getRowNum(), savingException);
						}
					}	
				} else {
					logger.error(FILE_VIOLATION_DURING_VALIDATION_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Category.class.getName());
					parsingResult.increaseNumberOfUnsavedInstances();
					parsingResult.increaseNumberOfInvalidInstances();
					
					for (ConstraintViolation<Category> violation : constraintViolations) {
						logger.error(VALIDATION_EXCEPTION, violation.getMessage());
						parsingResult.addConstraintViolation(row.getRowNum(), violation);
					}
				}
			}
					
			logger.info(FILE_FINISHING_WORKING_WITH_SHEET_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), Category.class.getName());
			logger.info(FILE_FINISHING_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, Category.class.getName());
		} else {
			logger.warn(FILE_SHEET_WAS_NOT_FOUND_MESSAGE, FILE_WITH_BASIC_DATA, Category.class.getName(), SHEET_WITH_BASIC_CATEGORIES);
			parsingResult.addCommonWarning("Sheet with name '" + SHEET_WITH_BASIC_CATEGORIES + "' wasn't found.");
		}					
		
		parsingResult.setFinishTime();
		/*
		 * Writes a detailed description of the completed parsing process to the log.
		 */
		logger.info(FILE_RESULT_OF_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, Category.class.getName(), parsingResult.toString());
		return parsingResult;
	}
	
	/**
	 * Retrieves, validates, checks on uniqueness and saves to the database instances of the 
	 * {@code naakcii.by.api.subcategory.Subcategory} class.
	 * {@code Source} for the <i>parsing process</i> is <i>.xlsx</i> file 
	 * {@value #FILE_WITH_BASIC_DATA}, {@code name of the sheet} is {@value SHEET_WITH_BASIC_SUBCATEGORIES}.
	 * 
	 * @param book the XSSFWorkbook representing the <i>.xlsx</i> file
	 * @return {@code multisheet parsing result} containing detailed information about the 
	 * completed <i>parsing process</i> of the {@code Subcategory} class instances
	 * @see naakcii.by.api.subcategory.Subcategory
	 * @see naakcii.by.api.util.parser.multisheet.MultisheetParsingResult
	 */
	private MultisheetParsingResult<Subcategory> createBasicSubcategories(XSSFWorkbook book) {
		logger.info(FILE_PREPAIRING_FOR_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, Subcategory.class.getName());
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		/*
		 * Creates instance of the naakcii.by.api.util.parser.multisheet.MultisheetParsingResult 
		 * class to accumulate detailed information about the parsing process.
		 */
		MultisheetParsingResult<Subcategory> parsingResult = objectFactory.getInstance(MultisheetParsingResult.class, Subcategory.class, FILE_WITH_BASIC_DATA);
		parsingResult.setStartTime();
		logger.info(FILE_STARTING_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, Subcategory.class.getName());	
		XSSFSheet sheet = book.getSheet(SHEET_WITH_BASIC_SUBCATEGORIES);
				
		if (sheet != null) {
			parsingResult.setSheetName(sheet.getSheetName());
			parsingResult.setSheetIndex(book.getSheetIndex(sheet));
			logger.info(FILE_STARTING_WORKING_WITH_SHEET_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), Subcategory.class.getName());
			/*
			 * Creates instance of the naakcii.by.api.util.parser.multisheet.mapper.SubcategoryColumnMapper 
			 * to map columns of the table on the properties of the naakcii.by.api.subcategory.Subcategory 
			 * entity.
			 */
			SubcategoryColumnMapper columnMapper = objectFactory.getInstance(SubcategoryColumnMapper.class);
			Iterator<Row> rowIterator = sheet.iterator();
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			
			/*
			 * Reads the header row of the table and maps each column on a single property of 
			 * the naakcii.by.api.subcategory.Subcategory class.
			 */
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				columnMapper.mapColumn(cell.getStringCellValue(), cell.getColumnIndex());
			}
			
			/*
			 * Writes a detailed description of the completed mapping process to the log.
			 */
			logger.info(FILE_RESULT_OF_COLUMN_MAPPING_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), Subcategory.class.getName(), columnMapper.toString());			
			
			/*
			 * Reads rows of the table one by one and tries to build a new instance of the 
			 * naakcii.by.api.subcategory.Subcategory class from the data containing in each row.
			 */
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				/*
				 * Creates a new instance of the naakcii.by.api.subcategory.Subcategory class. All 
				 * fields of the new instance aren't populated with any data yet.
				 */
				Subcategory subcategory = objectFactory.getInstance(Subcategory.class);
				parsingResult.increaseTotalNumberOfInstances();
				
				/*
				 * Sets 'name' property value of the newly created instance of the 
				 * naakcii.by.api.subcategory.Subcategory class if this property has been mapped 
				 * on the appropriate column of the table.
				 */
				if (columnMapper.isNameMapped()) {
					Cell subcategoryName = row.getCell(columnMapper.getNameColumnIndex());
					
					if ((subcategoryName != null) && (subcategoryName.getCellType() == CellType.STRING)) {
						subcategory.setName(subcategoryName.getStringCellValue().trim());
					}	
				}
				
				/*
				 * Sets 'category' property value of the newly created instance of the 
				 * naakcii.by.api.subcategory.Subcategory class if this property has been mapped 
				 * on the appropriate column of the table.
				 */
				if (columnMapper.isCategoryMapped()) {
					Cell categoryName = row.getCell(columnMapper.getCategoryColumnIndex());
						
					if ((categoryName != null) && (categoryName.getCellType() == CellType.STRING)) {
						/*
						 * Searches in the database for an instance of the naakcii.by.api.category.Category 
						 * class where Category#name is equal to the cell string value. If such 
						 * instance has been found, then populates 'category' property with 
						 * reference to this instance, else an appropriate warning is written to 
						 * the log and to the multisheet parsing result.
						 */
						Optional<Category> category = categoryRepository.findByName(categoryName.getStringCellValue().trim());
							
						if (category.isPresent()) {
							subcategory.setCategory(category.get());
						} else {
							logger.warn(FILE_REFERENCE_ON_INSTANCE_WAS_NOT_FOUND_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Subcategory.class.getName(), Category.class.getName(), categoryName.getStringCellValue().trim());
							parsingResult.addWarning(row.getRowNum(), "Reference on instance of '" + Category.class.getName() + "' with name '" + categoryName.getStringCellValue().trim() + "' hasn't been found in the database.");
						}
					}	
				}
				
				/*
				 * Sets 'priority' property value of the newly created instance of the 
				 * naakcii.by.api.subcategory.Subcategory class if this property has been mapped 
				 * on the appropriate column of the table.
				 */
				if (columnMapper.isPriorityMapped()) {
					Cell subcategoryPriority = row.getCell(columnMapper.getPriorityColumnIndex());
					
					if ((subcategoryPriority != null) && (subcategoryPriority.getCellType() == CellType.NUMERIC)) {
						subcategory.setPriority((int) subcategoryPriority.getNumericCellValue());
					}	
				}
				
				/*
				 * Sets 'activity' property value of the newly created instance of the 
				 * naakcii.by.api.subcategory.Subcategory class if this property has been mapped 
				 * on the appropriate column of the table.
				 */
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
				
				/*
				 * Validates newly created instance of the naakcii.by.api.subcategory.Subcategory 
				 * class.
				 */
				Set<ConstraintViolation<Subcategory>> constraintViolations = validator.validate(subcategory);
				
				/*
				 * If there are not any constraint violations then a new instance of the 
				 * naakcii.by.api.subcategory.Subcategory class will be checked on uniqueness, 
				 * else all occurred constraint violations will be written to the log and to the 
				 * multisheet parsing result.
				 */
				if (constraintViolations.size() == 0) {
					/*
					 * Checks on uniqueness a new instance of the naakcii.by.api.subcategory.Subcategory 
					 * class (i.e. checks if such instance has been already presented in the database) 
					 * according to the following properties of the entity:
					 * - Subcategory#name;
					 * - Subcategory#category#name.
					 * If check on uniqueness has passed successfully then a new instance is saved 
					 * to the database, else an appropriate warning is written to the log and to 
					 * the multisheet parsing result.
					 */
					if (subcategoryRepository.findByNameAndCategoryName(subcategory.getName(), subcategory.getCategory().getName()).isPresent()) {
						logger.warn(FILE_INSTANCE_WAS_PRESENTED_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Subcategory.class.getName());
						logger.warn(subcategory.toString());
						parsingResult.increaseNumberOfUnsavedInstances();
						parsingResult.increaseNumberOfAlreadyExistingInstances();
						parsingResult.addWarning(row.getRowNum(), INSTANCE_WAS_PRESENTED_WARNING);
					} else {
						/*
						 * Tries to save newly created instance of the naakcii.by.api.subcategory.Subcategory 
						 * class to the database. 
						 * If an exception has occurred during the saving process than it is written to 
						 * the log and to the multisheet parsing result.
						 */
						try {
							if (subcategoryRepository.save(subcategory) != null) {
								logger.info(FILE_INSTANCE_WAS_SAVED_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Subcategory.class.getName());
								logger.info(subcategory.toString());
								parsingResult.increaseNumberOfSavedInstances();
							}
						} catch (Exception savingException) {
							logger.error(FILE_EXCEPTION_DURING_SAVING_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Subcategory.class.getName(), printStackTrace(savingException));
							parsingResult.increaseNumberOfUnsavedInstances();
							parsingResult.addException(row.getRowNum(), savingException);
						}
					}	
				} else {
					logger.error(FILE_VIOLATION_DURING_VALIDATION_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), row.getRowNum(), Subcategory.class.getName());
					parsingResult.increaseNumberOfUnsavedInstances();
					parsingResult.increaseNumberOfInvalidInstances();
							
					for (ConstraintViolation<Subcategory> violation : constraintViolations) {
						logger.error(VALIDATION_EXCEPTION, violation.getMessage());
						parsingResult.addConstraintViolation(row.getRowNum(), violation);
					}
				}
			}
					
			logger.info(FILE_FINISHING_WORKING_WITH_SHEET_MESSAGE, FILE_WITH_BASIC_DATA, sheet.getSheetName(), Subcategory.class.getName());
			logger.info(FILE_FINISHING_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, Subcategory.class.getName());
		} else {
			logger.warn(FILE_SHEET_WAS_NOT_FOUND_MESSAGE, FILE_WITH_BASIC_DATA, Subcategory.class.getName(), SHEET_WITH_BASIC_SUBCATEGORIES);
			parsingResult.addCommonWarning("Sheet with name '" + SHEET_WITH_BASIC_SUBCATEGORIES + "' wasn't found.");
		}				
		
		parsingResult.setFinishTime();
		/*
		 * Writes a detailed description of the completed parsing process to the log.
		 */
		logger.info(FILE_RESULT_OF_PARSING_MESSAGE, FILE_WITH_BASIC_DATA, Subcategory.class.getName(), parsingResult.toString());
		return parsingResult;
	}
	
	/**
	 * Implements method {@link naakcii.by.api.util.parser.DataParser#parseChainProducts(String fileName, String chainSynonym)} 
	 * of the master {@code Data Parser} interface.
	 * 
	 * @param fileName the string containing the name of the file retrieving of data is made from
	 * @param chainSynonym the string representing {@code Chain's synonym} all parsed instances of 
	 * the {@code Chain Product} class are assigned on during the <i>parsing process</i>
	 * @return the list of {@code multisheet parsing result} containing detailed information about 
	 * the completed <i>parsing process</i> of the {@code Product} and {@code Chain Product} 
	 * classes instances
	 * @see naakcii.by.api.product.Product
	 * @see naakcii.by.api.chainproduct.ChainProduct
	 * @see naakcii.by.api.util.parser.multisheet.ParsingResult
	 * @see naakcii.by.api.chain.Chain#synonym
	 */
	@Override
	public List<ParsingResult<?, ?>> parseChainProducts(String file, String chainSynonym) {
		logger.info("File: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Preparing for parsing.", 
				file, chainSynonym, Product.class.getName(), ChainProduct.class.getName());
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		List<ParsingResult<?, ?>> results = new ArrayList<>();
		/*
		 * Creates two instances of the naakcii.by.api.util.parser.multisheet.MultisheetParsingResult 
		 * class to accumulate detailed information about the parsing process.
		 */
		MultisheetParsingResult<Product> productParsingResult = objectFactory.getInstance(MultisheetParsingResult.class, Product.class, file);
		MultisheetParsingResult<ChainProduct> chainProductParsingResult = objectFactory.getInstance(MultisheetParsingResult.class, ChainProduct.class, file);
		productParsingResult.setStartTime();
		chainProductParsingResult.setStartTime();
		
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			logger.info("File: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Starting process of parsing.", 
					file, Product.class.getName(), ChainProduct.class.getName(), chainSynonym);
		
			try (XSSFWorkbook book = new XSSFWorkbook(fileInputStream)) {
				XSSFSheet sheet = book.getSheetAt(0);
				
				if (sheet != null) {
					productParsingResult.setSheetName(sheet.getSheetName());
					chainProductParsingResult.setSheetName(sheet.getSheetName());
					productParsingResult.setSheetIndex(book.getSheetIndex(sheet));
					chainProductParsingResult.setSheetIndex(book.getSheetIndex(sheet));
					logger.info("File: '{}'. Sheet: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Starting working with sheet.", 
							file, sheet.getSheetName(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym);
					/*
					 * Creates instance of the naakcii.by.api.util.parser.multisheet.mapper.ChainProductColumnMapper 
					 * to map columns of the table on the properties of the naakcii.by.api.product.Product 
					 * and naakcii.by.api.chainproduct.ChainProduct entities.
					 */
					ChainProductColumnMapper columnMapper = objectFactory.getInstance(ChainProductColumnMapper.class);
					Iterator<Row> rowIterator = sheet.iterator();
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					
					/*
					 * Reads the header row of the table and maps each column on a single property of 
					 * the naakcii.by.api.product.Product or naakcii.by.api.chainproduct.ChainProduct 
					 * classes.
					 */
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						columnMapper.mapColumn(cell.getStringCellValue(), cell.getColumnIndex());
					}
					
					/*
					 * Writes a detailed description of the completed mapping process to the log.
					 */
					logger.info("File: '{}'. Sheet: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Returning result of column mapping. {}", 
							file, sheet.getSheetName(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym, columnMapper.toString());
					
					/*
					 * Reads rows of the table one by one and tries to build a new instances of the 
					 * naakcii.by.api.product.Product and naakcii.by.api.chainproduct.ChainProduct 
					 * classes from the data containing in each row.
					 */
					while (rowIterator.hasNext()) {
						row = rowIterator.next();
						/*
						 * Creates a new instance of the naakcii.by.api.product.Product class. All 
						 * fields of the new instance aren't populated with any data yet.
						 */
						Product product = objectFactory.getInstance(Product.class);
						/*
						 * Creates a new instance of the naakcii.by.api.chainproduct.ChainProduct 
						 * class. All fields of the new instance aren't populated with any data yet.
						 */
						ChainProduct chainProduct = objectFactory.getInstance(ChainProduct.class);
						productParsingResult.increaseTotalNumberOfInstances();
						chainProductParsingResult.increaseTotalNumberOfInstances();
						
						/*
						 * Sets 'name' property value of the newly created instance of the 
						 * naakcii.by.api.product.Product class if this property has been mapped 
						 * on the appropriate column of the table.
						 */
						if (columnMapper.isNameMapped()) {
							Cell productName = row.getCell(columnMapper.getNameColumnIndex());
							
							if ((productName != null) && (productName.getCellType() == CellType.STRING)) {
								product.setName(productName.getStringCellValue().trim());
							}	
						}
						
						/*
						 * Sets 'barcode' property value of the newly created instance of the 
						 * naakcii.by.api.product.Product class if this property has been mapped 
						 * on the appropriate column of the table.
						 */
						if (columnMapper.isBarcodeMapped()) {
							Cell productBarcode = row.getCell(columnMapper.getBarcodeColumnIndex());
							
							if ((productBarcode != null) && (productBarcode.getCellType() == CellType.NUMERIC)) {
								Formatter formatter = new Formatter();
								product.setBarcode(formatter.format("%.0f", productBarcode.getNumericCellValue()).toString());
								formatter.close();
							}
						}
						
						/*
						 * Sets 'unit of measure' property value of the newly created instance of 
						 * the naakcii.by.api.product.Product class if this property has been 
						 * mapped on the appropriate column of the table.
						 */
						if (columnMapper.isUnitMapped()) {
							Cell productUnit = row.getCell(columnMapper.getUnitColumnIndex());
							
							if ((productUnit != null) && (productUnit.getCellType() == CellType.STRING)) {
								/*
								 * Searches in the database for an instance of the naakcii.by.api.unitofmeasure.UnitOfMeasure 
								 * class where UnitOfMeasure#name is equal to the cell string 
								 * value. If such instance has been found, then populates 
								 * 'unit of measure' property with reference to this instance, 
								 * else an appropriate warning is written to the log and to the 
								 * multisheet parsing result.
								 */
								Optional<UnitOfMeasure> unitOfMeasure = unitOfMeasureRepository.findByNameIgnoreCase(productUnit.getStringCellValue().trim());
								
								if (unitOfMeasure.isPresent()) {
									product.setUnitOfMeasure(unitOfMeasure.get());
								} else {
									logger.warn("File: '{}'. Sheet: '{}'. Row number: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Reference on instance of '{}' with name '{}' hasn't been found in the database.", 
											file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym, UnitOfMeasure.class.getName(), productUnit.getStringCellValue().trim());
									productParsingResult.addWarning(row.getRowNum(), "Reference on instance of '" + UnitOfMeasure.class.getName() + "' with name '" + productUnit.getStringCellValue().trim() + "' hasn't been found in the database.");
								}
							}
						}
						
						/*
						 * Sets 'manufacturer' property value of the newly created instance of 
						 * the naakcii.by.api.product.Product class if this property has been 
						 * mapped on the appropriate column of the table.
						 */
						if (columnMapper.isManufacturerMapped()) {
							Cell productManufacturer = row.getCell(columnMapper.getManufacturerColumnIndex());
							
							if ((productManufacturer != null) && (productManufacturer.getCellType() == CellType.STRING)) {
								product.setManufacturer(productManufacturer.getStringCellValue().trim());
							}	
						}
						
						/*
						 * Sets 'brand' property value of the newly created instance of 
						 * the naakcii.by.api.product.Product class if this property has been 
						 * mapped on the appropriate column of the table.
						 */
						if (columnMapper.isBrandMapped()) {
							Cell productBrand = row.getCell(columnMapper.getBrandColumnIndex());
							
							if ((productBrand != null) && (productBrand.getCellType() == CellType.STRING)) {
								product.setBrand(productBrand.getStringCellValue().trim());
							}	
						}
						
						/*
						 * Sets 'country of origin' property value of the newly created instance of 
						 * the naakcii.by.api.product.Product class if this property has been 
						 * mapped on the appropriate column of the table.
						 */
						if (columnMapper.isCountryOfOriginMapped()) {
							Cell productCountryOfOrigin = row.getCell(columnMapper.getCountryOfOriginColumnIndex());
							
							if ((productCountryOfOrigin != null) && (productCountryOfOrigin.getCellType() == CellType.STRING)) {
								/*
								 * Searches in the database for an instance of the naakcii.by.api.country.Country 
								 * class where Country#alphaCode2 is equal to the cell string 
								 * value. If such instance has been found, then populates 
								 * 'country of origin' property with reference to this instance, 
								 * else an appropriate warning is written to the log and to the 
								 * multisheet parsing result.
								 */
								Optional<Country> countryOfOrigin = countryRepository.findByAlphaCode2(productCountryOfOrigin.getStringCellValue().trim());
								
								if (countryOfOrigin.isPresent()) {
									product.setCountryOfOrigin(countryOfOrigin.get());
								} else {
									logger.warn("File: '{}'. Sheet: '{}'. Row number: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Reference on instance of '{}' with name '{}' hasn't been found in the database.", 
											file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym, Country.class.getName(), productCountryOfOrigin.getStringCellValue().trim());
									productParsingResult.addWarning(row.getRowNum(), "Reference on instance of '" + Country.class.getName() + "' with name '" + productCountryOfOrigin.getStringCellValue().trim() + "' hasn't been found in the database.");
								}
							}	
						}
						
						/*
						 * Sets 'subcategory' property value of the newly created instance of 
						 * the naakcii.by.api.product.Product class.
						 * Searches in the database for an instance of the naakcii.by.api.subcategory.Subcategory 
						 * class where Subcategory#name is equal to the 'Indefinite subcategory' 
						 * and Subcategory#category#name is equal to the 'Indefinite category'. If 
						 * such instance has been found, then populates 'subcategory' property 
						 * with reference to this instance, else an appropriate warning is written 
						 * to the log and to the multisheet parsing result.
						 */
						Optional<Subcategory> subcategory = subcategoryRepository.findByNameAndCategoryName(INDEFINITE_SUBCATEGORY, INDEFINITE_CATEGORY);
						
						if (subcategory.isPresent()) {
							product.setSubcategory(subcategory.get());
						} else {
							logger.warn("File: '{}'. Sheet: '{}'. Row number: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Reference on instance of '{}' with name '{}' hasn't been found in the database.", 
									file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym, Subcategory.class.getName(), INDEFINITE_SUBCATEGORY);
							productParsingResult.addWarning(row.getRowNum(), "Reference on instance of '" + Subcategory.class.getName() + "' with name '" + INDEFINITE_SUBCATEGORY + "' hasn't been found in the database.");
						}
						
						/*
						 * Sets 'activity' property value of the newly created instance of 
						 * the naakcii.by.api.product.Product class as 'true'.
						 */
						product.setIsActive(true);
						
						/*
						 * Sets 'base price' property value of the newly created instance of 
						 * the naakcii.by.api.chainproduct.ChainProduct class if this property has 
						 * been mapped on the appropriate column of the table.
						 */
						if (columnMapper.isBasePriceMapped()) {
							Cell chainProductBasePrice = row.getCell(columnMapper.getBasePriceColumnIndex());
							
							if ((chainProductBasePrice != null) && (chainProductBasePrice.getCellType() == CellType.NUMERIC)) {
								chainProduct.setBasePrice(new BigDecimal(chainProductBasePrice.getNumericCellValue()).setScale(2, RoundingMode.HALF_UP));
							}	
						}
						
						/*
						 * Sets 'discount price' property value of the newly created instance of 
						 * the naakcii.by.api.chainproduct.ChainProduct class if this property has 
						 * been mapped on the appropriate column of the table.
						 */
						if (columnMapper.isDiscountPriceMapped()) {
							Cell chainProductDiscountPrice = row.getCell(columnMapper.getDiscountPriceColumnIndex());
							
							if ((chainProductDiscountPrice != null) && (chainProductDiscountPrice.getCellType() == CellType.NUMERIC)) {
								chainProduct.setDiscountPrice(new BigDecimal(chainProductDiscountPrice.getNumericCellValue()).setScale(2, RoundingMode.HALF_UP));
							}	
						}
						
						/*
						 * Calculates 'discount percent' property value of the newly created 
						 * instance of the naakcii.by.api.chainproduct.ChainProduct class if 
						 * properties 'base price' and 'discount price' have been already set.
						 */
						if ((chainProduct.getBasePrice() != null) && (chainProduct.getDiscountPrice() != null)) {
							BigDecimal difference = chainProduct.getBasePrice().subtract(chainProduct.getDiscountPrice());
							BigDecimal discount = difference.divide(chainProduct.getBasePrice(), new MathContext(10, RoundingMode.HALF_UP));
							BigDecimal discountPercent = discount.scaleByPowerOfTen(2).setScale(0, RoundingMode.HALF_UP);
							chainProduct.setDiscountPercent(discountPercent);
						}
						
						/*
						 * Sets 'start date' property value of the newly created instance of 
						 * the naakcii.by.api.chainproduct.ChainProduct class if this property has 
						 * been mapped on the appropriate column of the table.
						 */
						if (columnMapper.isStartDateMapped()) {
							Cell chainProductStartDate = row.getCell(columnMapper.getStartDateColumnIndex());
							
							if ((chainProductStartDate != null) && (chainProductStartDate.getCellType() == CellType.NUMERIC)) {
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(chainProductStartDate.getDateCellValue());
								chainProduct.setStartDate(calendar);
							}
						}
						
						/*
						 * Sets 'end date' property value of the newly created instance of 
						 * the naakcii.by.api.chainproduct.ChainProduct class if this property has 
						 * been mapped on the appropriate column of the table.
						 */
						if (columnMapper.isEndDateMapped()) {
							Cell chainProductEndDate = row.getCell(columnMapper.getEndDateColumnIndex());
							
							if ((chainProductEndDate != null) && (chainProductEndDate.getCellType() == CellType.NUMERIC)) {
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(chainProductEndDate.getDateCellValue());
								chainProduct.setEndDate(calendar);
							}
						}
						
						/*
						 * Sets 'chain' property value of the newly created instance of 
						 * the naakcii.by.api.chainproduct.ChainProduct class.
						 * Searches in the database for an instance of the naakcii.by.api.chain.Chain 
						 * class where Chain#synonym is equal to the 'chain synonym' parameter 
						 * of the method. If such instance has been found, then populates 
						 * 'chain' property with reference to this instance, else an appropriate 
						 * warning is written to the log and to the multisheet parsing result.
						 */
						Optional<Chain> chain = chainRepository.findBySynonym(chainSynonym);
						
						if (chain.isPresent()) {
							chainProduct.setChain(chain.get());
						} else {
							logger.warn("File: '{}'. Sheet: '{}'. Row number: '{}' Target instances: '{}' and '{}'. Chain synonym: '{}'. Reference on instance of '{}' with synonym '{}' hasn't been found in the database.", 
									file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym, Chain.class.getName(), chainSynonym);
							chainProductParsingResult.addWarning(row.getRowNum(), "Reference on instance of '" + Chain.class.getName() + "' with synonym '" + chainSynonym + "' hasn't been found in the database.");
						}
						
						/*
						 * Sets 'type' property value of the newly created instance of 
						 * the naakcii.by.api.chainproduct.ChainProduct class if this property has 
						 * been mapped on the appropriate column of the table.
						 */
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
								
								/*
								 * Searches in the database for an instance of the naakcii.by.api.chainproducttype.ChainProductType 
								 * class where ChainProductType#synonym is equal to the mapped 
								 * string value of the cell. If such instance has been found, then 
								 * populates 'type' property with reference to this instance, else 
								 * an appropriate warning is written to the log and to the 
								 * multisheet parsing result.
								 */
								Optional<ChainProductType> type = chainProductTypeRepository.findBySynonym(chainProductTypeSynonym);
								
								if (type.isPresent()) {
									chainProduct.setType(type.get());
								} else {
									logger.warn("File: '{}'. Sheet: '{}'. Row number '{}' Target instances: '{}' and '{}'. Chain synonym: '{}'. Reference on instance of '{}' with synonym '{}' hasn't been found in the database.", 
											file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym, ChainProductType.class.getName(), chainProductTypeSynonym);
									chainProductParsingResult.addWarning(row.getRowNum(), "Reference on instance of '" + ChainProductType.class.getName() + "' with synonym '" + chainProductTypeSynonym + "' hasn't been found in the database.");
								}
							}
						}
						
						/*
						 * Validates newly created instance of the naakcii.by.api.product.Product 
						 * class.
						 */
						Set<ConstraintViolation<Product>> productConstraintViolations = validator.validate(product);
						
						/*
						 * If there are not any constraint violations then a new instance of the 
						 * naakcii.by.api.product.Product class will be checked on uniqueness, 
						 * else all occurred constraint violations will be written to the log and to 
						 * the multisheet parsing result.
						 */
						if (productConstraintViolations.size() == 0) {
							/*
							 * Checks on uniqueness a new instance of the naakcii.by.api.product.Product 
							 * class (i.e. checks if such instance has been already presented in the database) 
							 * according to the following properties of the entity:
							 * - Product#name;
							 * - Product#barcode;
							 * - Product#unitOfMeasure.
							 * If check on uniqueness has passed successfully then a new instance is saved 
							 * to the database, else an appropriate warning is written to the log and to 
							 * the multisheet parsing result.
							 */
							Optional<Product> existingProduct = productRepository.findByNameAndBarcodeAndUnitOfMeasure(product.getName(), product.getBarcode(), product.getUnitOfMeasure());
							
							if (existingProduct.isPresent()) {
								chainProduct.setProduct(existingProduct.get());
								logger.warn("File: '{}'. Sheet: '{}'. Row number: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. New instance of '{}' has been already presented in the database.",
										file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym, Product.class.getName());
								logger.warn(product.toString());
								productParsingResult.increaseNumberOfUnsavedInstances();
								productParsingResult.increaseNumberOfAlreadyExistingInstances();
								productParsingResult.addWarning(row.getRowNum(), INSTANCE_WAS_PRESENTED_WARNING);
							} else {
								/*
								 * Tries to save newly created instance of the naakcii.by.api.product.Product 
								 * class to the database. 
								 * If an exception has occurred during the saving process than it is written to 
								 * the log and to the multisheet parsing result.
								 */
								try {
									if (productRepository.save(product) != null) {
										/*
										 * Sets 'product' property value of the newly created instance of 
										 * the naakcii.by.api.chainproduct.ChainProduct class as 
										 * reference to the newly created and saved to the database 
										 * instance of the naakcii.by.api.product.Product class.
										 */
										chainProduct.setProduct(product);
										logger.info("File: '{}'. Sheet: '{}'. Row number '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. New instance of '{}' has been created and saved to the database.",
												file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym, Product.class.getName());
										logger.info(product.toString());
										productParsingResult.increaseNumberOfSavedInstances();
									}
								} catch (Exception savingException) {
									logger.error("File: '{}'. Sheet: '{}'. Row number: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Exception has occurred during the saving of new instance of '{}' to the database: {}.", 
											file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym, Product.class.getName(), printStackTrace(savingException));
									productParsingResult.increaseNumberOfUnsavedInstances();
									productParsingResult.addException(row.getRowNum(), savingException);
								}
							}
						} else {
							logger.error("File: '{}'. Sheet: '{}'. Row number '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Exception(s) has occurred during the validation of new instance of '{}'. See validation exception(s) below.", 
									file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym, Product.class.getName());
							productParsingResult.increaseNumberOfUnsavedInstances();
							productParsingResult.increaseNumberOfInvalidInstances();
							
							for (ConstraintViolation<Product> violation : productConstraintViolations) {
								logger.error(VALIDATION_EXCEPTION, violation.getMessage());
								productParsingResult.addConstraintViolation(row.getRowNum(), violation);
							}
						}
						
						/*
						 * Validates newly created instance of the naakcii.by.api.chainproduct.ChainProduct 
						 * class.
						 */
						Set<ConstraintViolation<ChainProduct>> chainProductConstraintViolations = validator.validate(chainProduct);
						
						/*
						 * If there are not any constraint violations then a new instance of the 
						 * naakcii.by.api.chainproduct.ChainProduct class will be checked on 
						 * uniqueness, else all occurred constraint violations will be written to 
						 * the log and to the multisheet parsing result.
						 */
						if (chainProductConstraintViolations.size() == 0) {
							/*
							 * Checks on uniqueness a new instance of the naakcii.by.api.chainproduct.ChainProduct 
							 * class (i.e. checks if such instance has been already presented in the database) 
							 * according to the following properties of the entity:
							 * - ChainProduct#startDate;
							 * - ChainProduct#endDate;
							 * - ChainProduct#basePrice;
							 * - ChainProduct#discountPrice;
							 * - ChainProduct#type;
							 * - ChainProduct#chainId;
							 * - ChainProduct#productId.
							 * If check on uniqueness has passed successfully then a new instance is saved 
							 * to the database, else an appropriate warning is written to the log and to 
							 * the multisheet parsing result.
							 */
							if (chainProductRepository.findByStartDateAndEndDateAndBasePriceAndDiscountPriceAndTypeIdAndChainIdAndProductId(
									chainProduct.getStartDate(), 
									chainProduct.getEndDate(),
									chainProduct.getBasePrice(),
									chainProduct.getDiscountPrice(),
									chainProduct.getType() == null ? null : chainProduct.getType().getId(),
									chainProduct.getChain() == null ? null : chainProduct.getChain().getId(),
									chainProduct.getProduct() == null ? null : chainProduct.getProduct().getId()).isPresent()) {
								logger.warn("File: '{}'. Sheet: '{}'. Row number '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. New instance has been already presented in the database.",
										file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym);
								logger.warn(chainProduct.toString());
								chainProductParsingResult.increaseNumberOfUnsavedInstances();
								chainProductParsingResult.increaseNumberOfAlreadyExistingInstances();
								chainProductParsingResult.addWarning(row.getRowNum(), INSTANCE_WAS_PRESENTED_WARNING);
							} else {
								/*
								 * Tries to save newly created instance of the naakcii.by.api.chainproduct.ChainProduct 
								 * class to the database. 
								 * If an exception has occurred during the saving process than it is written to 
								 * the log and to the multisheet parsing result.
								 */
								try {
									if (chainProductRepository.save(chainProduct) != null) {
										logger.info("File: '{}'. Sheet: '{}'. Row number '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. New instance has been created and saved to the database.",
												file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym);
										logger.info(chainProduct.toString());
										chainProductParsingResult.increaseNumberOfSavedInstances();
									}
								} catch (Exception savingException) {
									logger.error("File: '{}'. Sheet: '{}'. Row number '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Exception has occurred during the saving of new instance to the database: {}.", 
											file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym, printStackTrace(savingException));
									chainProductParsingResult.increaseNumberOfUnsavedInstances();
									chainProductParsingResult.addException(row.getRowNum(), savingException);
								}
							}	
						} else {
							logger.error("File: '{}'. Sheet: '{}'. Row number '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Exception(s) has occurred during the validation of new instance. See validation exception(s) below.", 
									file, sheet.getSheetName(), row.getRowNum(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym);
							chainProductParsingResult.increaseNumberOfUnsavedInstances();
							chainProductParsingResult.increaseNumberOfInvalidInstances();
							
							for (ConstraintViolation<ChainProduct> violation : chainProductConstraintViolations) {
								logger.error(VALIDATION_EXCEPTION, violation.getMessage());
								chainProductParsingResult.addConstraintViolation(row.getRowNum(), violation);
							}
						}
					}
					
					logger.info("File: '{}'. Sheet: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Finishing working with sheet.", 
							FILE_WITH_BASIC_DATA, sheet.getSheetName(), Product.class.getName(), ChainProduct.class.getName(), chainSynonym);
					logger.info("File: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Finishing process of parsing.", 
							FILE_WITH_BASIC_DATA, Product.class.getName(), ChainProduct.class.getName(), chainSynonym);
				} else {
					logger.warn("File: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Sheet at index '{}' wasn't found.", file, Product.class.getName(), ChainProduct.class.getName(), chainSynonym, 0);
					productParsingResult.addCommonWarning("Sheet at index '" + 0 + "' wasn't found.");
					chainProductParsingResult.addCommonWarning("Sheet at index '" + 0 + "' wasn't found.");
				}				
			} 		
		} catch (IOException ioException) {
			logger.error("File: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Input-output exception has occurred during opening the file: {}.", 
					file, Product.class.getName(), ChainProduct.class.getName(), chainSynonym, printStackTrace(ioException));
			productParsingResult.addCommonException(ioException);
			chainProductParsingResult.addCommonException(ioException);
		} 
		
		productParsingResult.setFinishTime();
		chainProductParsingResult.setFinishTime();
		/*
		 * Writes a detailed description of the completed parsing process to the log.
		 */
		logger.info("File: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Returning result of parsing. {}", 
				file, Product.class.getName(), ChainProduct.class.getName(), chainSynonym, productParsingResult.toString());
		logger.info("File: '{}'. Target instances: '{}' and '{}'. Chain synonym: '{}'. Returning result of parsing. {}", 
				file, Product.class.getName(), ChainProduct.class.getName(), chainSynonym, chainProductParsingResult.toString());
		results.add(productParsingResult);
		results.add(chainProductParsingResult);
		return results;
	}
}
	