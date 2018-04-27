package naakcii.by.api.repository.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class ProductDataHandler {
	
	public Map<String, String> parseQuantityAndMeasure(String productDescription) {
		Map<String, String> resultMap = new HashMap<String, String>();
		Pattern generalPattern = Pattern.compile("[0-9]+[.]*[0-9]*[кгмл]+");
		Matcher generalMatcher = generalPattern.matcher(productDescription);	
		if (generalMatcher.find()) {
			String quantityAndMeasure = generalMatcher.group();
			System.out.println("Quantity and measure string: \"" + quantityAndMeasure + "\".");
			Pattern quantityPattern = Pattern.compile("[0-9]+[.]*[0-9]*");
			Pattern measurePattern = Pattern.compile("[кгмл]+");
			Matcher quantityMatcher = quantityPattern.matcher(quantityAndMeasure);
			Matcher measureMatcher = measurePattern.matcher(quantityAndMeasure);
			if (quantityMatcher.find())
				resultMap.put("quantity", quantityMatcher.group());
			if (measureMatcher.find())
				resultMap.put("measure", measureMatcher.group());
		}
		return resultMap;
	}
	
	public Map<String, Calendar> parseDate(String twoDates) {
		Map<String, Calendar> datesMap = new HashMap<String, Calendar>();
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		Pattern twoDatesPattern = Pattern.compile(" - ");
		String twoDatesArray[] = twoDatesPattern.split(twoDates);
		if (twoDatesArray.length == 2) {
			int startDay = 0, endDay = 0;
			int startMonth, endMonth;
			Map<String, Integer> startDateMap = parseDayAndMonth(twoDatesArray[0]);
			Map<String, Integer> endDateMap = parseDayAndMonth(twoDatesArray[1]);
			if (endDateMap.containsKey("day") && endDateMap.containsKey("month")) {
				endDay = endDateMap.get("day");
				endMonth = endDateMap.get("month");
				endDate.set(endDate.get(Calendar.YEAR), endMonth, endDay);
				datesMap.put("end", endDate);
				if (startDateMap.containsKey("day")) {
					startDay = startDateMap.get("day");
					if (startDateMap.containsKey("month")) {
						startMonth = startDateMap.get("month");
					} else startMonth = endMonth;
					startDate.set(startDate.get(Calendar.YEAR), startMonth, startDay);
					datesMap.put("start", startDate);
				}
			}	
		} 
		return datesMap;		
	}
	
	public Map<String, Integer> parseDayAndMonth(String dayAndMonth) {
		Map<String, Integer> dayAndMonthMap = new HashMap<String, Integer>();
		Pattern dayAndMonthPattern = Pattern.compile("[ ]");
		String dayAndMonthArray[] = dayAndMonthPattern.split(dayAndMonth);
		if (dayAndMonthArray.length > 0) {
			dayAndMonthMap.put("day", Integer.parseInt(dayAndMonthArray[0]));
			if (dayAndMonthArray.length > 1) {
				switch (dayAndMonthArray[1]){
					case "января" : {
						dayAndMonthMap.put("month", 0);
						break;
					}
					case "февраля" : {
						dayAndMonthMap.put("month", 1);
						break;
					}
					case "марта" : {
						dayAndMonthMap.put("month", 2);
						break;
					}
					case "апреля" : {
						dayAndMonthMap.put("month", 3);
						break;
					}
					case "мая" : {
						dayAndMonthMap.put("month", 4);
						break;
					}
					case "июня" : {
						dayAndMonthMap.put("month", 5);
						break;
					}
					case "июля" : {
						dayAndMonthMap.put("month", 6);
						break;
					}
					case "августа" : {
						dayAndMonthMap.put("month", 7);
						break;
					}
					case "сентября" : {
						dayAndMonthMap.put("month", 8);
						break;
					}
					case "октября" : {
						dayAndMonthMap.put("month", 9);
						break;
					}
					case "ноября" : {
						dayAndMonthMap.put("month", 10);
						break;
					}
					case "декабря" : {
						dayAndMonthMap.put("month", 11);
						break;
					}
					default : break;
				}
			}
		}
		return dayAndMonthMap;
	}

}
