package naakcii.by.api.util.parser;

import java.util.List;

import naakcii.by.api.util.parser.ParsingResult;

/**
 * Provides the base interface for parsing instances of some classes, that make in conjunction the 
 * <i>model basis</i>.
 * This list of <i>fundamental classes</i> includes:
 * <ul>
 *   <li>{@link naakcii.by.api.category.Category}</li>
 *   <li>{@link naakcii.by.api.chain.Chain}</li>
 *   <li>{@link naakcii.by.api.chainproduct.ChainProduct}</li>
 *   <li>{@link naakcii.by.api.chainproducttype.ChainProductType}</li>
 *   <li>{@link naakcii.by.api.country.Country}</li>
 *   <li>{@link naakcii.by.api.product.Product}</li>
 *   <li>{@link naakcii.by.api.subcategory.Subcategory}</li>
 *   <li>{@link naakcii.by.api.unitofmeasure.UnitOfMeasure}</li>
 * </ul>
 * <p>
 * Parsing can be made from:
 * <ul>
 *   <li>internal sources - such as enumerations;</li>
 *   <li>external sources - such as .xls, .xlsx, .xml, .json and etc. files.</li>		
 * </ul>
 * 
 * @see naakcii.by.api.category.Category
 * @see naakcii.by.api.chain.Chain
 * @see naakcii.by.api.chainproduct.ChainProduct
 * @see naakcii.by.api.chainproducttype.ChainProductType
 * @see naakcii.by.api.country.Country
 * @see naakcii.by.api.product.Product
 * @see naakcii.by.api.subcategory.Subcategory
 * @see naakcii.by.api.unitofmeasure.UnitOfMeasure
 */
public interface DataParser {
	
	/**
	 * Retrieves, validates, checks on uniqueness and saves to the database instances of several 
	 * classes, that form together so called <i>basic data</i>.
	 * <i>Basic data</i> consists of the next classes:
	 * <ul>
	 *   <li>{@link naakcii.by.api.category.Category}</li>
	 *   <li>{@link naakcii.by.api.chain.Chain}</li>
	 *   <li>{@link naakcii.by.api.chainproducttype.ChainProductType}</li>
	 *   <li>{@link naakcii.by.api.country.Country}</li>
	 *   <li>{@link naakcii.by.api.subcategory.Subcategory}</li>
	 *   <li>{@link naakcii.by.api.unitofmeasure.UnitOfMeasure}</li>
	 * </ul>
	 * <p>
	 * Existence of <i>basic data</i> in the database is critically important for the parsing of 
	 * instances of such classes as: 
	 * <ul>
	 *   <li>{@link naakcii.by.api.product.Product}</li>  
	 *   <li>{@link naakcii.by.api.chainproduct.ChainProduct}</li>
	 * </ul>
	 * <p>
	 * Returns a list consisting of the {@link naakcii.by.api.util.parser.ParsingResult} class 
	 * instances. 
	 * Each element in the list contains full information about the parsing process of the 
	 * {@link naakcii.by.api.util.parser.ParsingResult#targetClass} instances, that is, the result 
	 * list has <i>6</i> elements (per each element included into the <b>basic data</b>):
	 * <ul>
	 *   <li>{@code ParsingResult<Category>}</li>
	 *   <li>{@code ParsingResult<Chain>}</li>
	 *   <li>{@code ParsingResult<ChainProductType>}</li>
	 *   <li>{@code ParsingResult<Country>}</li>
	 *   <li>{@code ParsingResult<Subcategory>}</li>
	 *   <li>{@code ParsingResult<UnitOfMeasure>}</li>
	 * </ul>
	 * 
	 * @return the list of {@code parsing results} for each of the aforenamed {@code target classes}
	 * @see naakcii.by.api.util.parser.multisheet.ParsingResult
	 * @see naakcii.by.api.util.parser.multisheet.ParsingResult#targetClass
	 */
	List<ParsingResult<?, ?>> parseBasicData();
	
	/**
	 * Retrieves, validates, checks on uniqueness and saves to the database instances of the
	 * {@link naakcii.by.api.product.Product} and {@link naakcii.by.api.chainproduct.ChainProduct} 
	 * classes.
	 * Instances of the above classes can be successfully parsed if only the <i>basic data</i> 
	 * have been already added to the database.
	 * <p>
	 * Returns a list consisting of the {@link naakcii.by.api.util.parser.ParsingResult} class 
	 * instances. 
	 * Each element in the list contains full information about the parsing process of the 
	 * {@link naakcii.by.api.util.parser.ParsingResult#targetClass}, that is, the result list has 
	 * <i>2</i> elements (per each element specified above):
	 * <ul>
	 *   <li>{@code ParsingResult<Product>}</li>
	 *   <li>{@code ParsingResult<ChainProduct>}</li>
	 * </ul>
	 * 
	 * @param fileName the string containing the name of the file retrieving of data is made from
	 * @param chainSynonym the string representing {@code Chain's synonym} all parsed instances of 
	 * the {@code Chain Product} class are assigned on during the <i>parsing process</i>
	 * @return the list of {@code parsing results} for each of the aforenamed {@code target classes}
	 * @see naakcii.by.api.util.parser.multisheet.ParsingResult
	 * @see naakcii.by.api.util.parser.multisheet.ParsingResult#targetClass
	 * @see naakcii.by.api.chain.Chain#synonym
	 */
	List<ParsingResult<?, ?>> parseChainProducts(String fileName, String chainSynonym);
	
	/**
	 * Returns a readable exception description, including formatted list of stack trace elements.
	 * Each element of the list represents one stack frame and is written in a separate line. 
	 * The zeroth element of the list (assuming the list's length is non-zero) represents the top 
	 * of the stack, which is the last method invocation in the sequence. Typically, this is the 
	 * point at which this {@code Throwable} was created and thrown. The last element of the list 
     * (assuming the list's length is non-zero) represents the bottom of the stack, which is the 
     * first method invocation in the sequence.
	 * 
	 * @param the exception to make a readable description for
	 * @return the string containing description of the exception
	 */
	default String printStackTrace(Exception exception) {
		StringBuilder stackTrace = new StringBuilder();
		stackTrace.append(exception.getClass().getName()).append(" - ").append(exception.getMessage());
		
		for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
			stackTrace.append(System.lineSeparator());
			stackTrace.append("\t").append(stackTraceElement);
		}
	
		return stackTrace.toString();
	}
}
