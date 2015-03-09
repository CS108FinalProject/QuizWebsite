package com.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides JSON functionality.
 * @author Sam
 *
 */
public class Json {

	private static int index;
	
	/**
	 * Given a JSON Object in String form, parses it into a Map<String, Object>
	 * @param source a JSON Object in String form
	 * @return a Map<String, Object> representation of the JSON.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> parseJsonObject(String source) {
		Util.validateString(source);
		index = 0;
		source = source.trim();
		if (source.charAt(0) != '{') {
			throw new IllegalArgumentException(source + " does not start with {");
		}
		
		return (Map<String, Object>) parseJsonHelper(source);
	}
	
	
	/**
	 * Given a JSON Array in String form, parses it into a Map<String, Object>
	 * @param source a JSON Array in String form
	 * @return a List<Object> representation of the JSON.
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> parseJsonArray(String source) {
		Util.validateString(source);
		index = 0;
		source = source.trim();
		if (source.charAt(0) != '[') {
			throw new IllegalArgumentException(source + " does not start with [");
		}
		
		return (List<Object>) parseJsonHelper(source);
	}
	
	
	/**
	 * Given a Collection or a Map, returns a JSON representation of the
	 * object in String form.
	 */
	public static String getJsonString(Object object) {
		Util.validateObject(object);
		
		if (!(object instanceof Collection || object instanceof Map)) {
			throw new IllegalArgumentException("Passed object is not a Collection or a Map.");
		}
		
		String result = "";
		return getJsonStringHelper(object, result);
	}
	
	
	//-------------------------- Helper Methods -------------------------------//
	
	
	/*
	 * Recursively explores the data type converting the element into a JSON
	 * String.
	 */
	private static String getJsonStringHelper(Object object, String result) {
		if (object instanceof String) {
			String str = (String) object;
			result += "\"" + str + "\"";
			return result;
			
		} else if (object instanceof Boolean) {
			boolean bool = (Boolean) object;
			if (bool) result += "true";
			else result += "false";
			return result;
			
		} else if (object instanceof Number) {
			Number nbr = (Number) object;
			result += nbr.toString();
			return result;
			
		} else if (object instanceof Collection<?>) {
			Collection<?> col = (Collection<?>) object;
			result += "[";
			
			int counter = 0;
			for (Object cur : col) {
				result += getJsonStringHelper(cur, "");
				
				counter++;
				
				// Not last index.
				if (counter < col.size()) {
					result += ","; 
				}
			}
			result += "]";
			return result;
			
		} else if (object instanceof Map) {
			Map<?, ?> map = (Map<?, ?>)object;
			result += "{";
			
			int counter = 0;
			for (Object cur : map.keySet()) {
				if (!(cur instanceof String)) {
					throw new IllegalArgumentException("key " + cur + " is not a String.");
				}
				
				cur = (String) cur;
				result += "\"" + cur + "\":";
				
				result += getJsonStringHelper(map.get(cur), "");
				
				counter++;
				
				// Not last index.
				if (counter < map.size()) {
					result += ","; 
				}
			}
			result += "}";
			return result;
			
		} else {
			throw new IllegalArgumentException(object + " is not a valid type.");
		}
	}
	
	
	
	/*
	 * Parses the JSON Recursively.
	 */
	private static Object parseJsonHelper(String source) {
		skipSpaces(source);
		char cur = source.charAt(index);
		
		// Determine the type of Object that we should parse.
		
		// Value is a String.
		if (cur == '\"') {
			String value = getString(source);
			if (index < source.length() - 1) index++;
			skipComma(source);
			return value;
			
		// Value is boolean
		} else if (cur == 't' || cur == 'f') {
			boolean value = getBoolean(source);
			skipComma(source);
			return value;
			
		// Value is a Number
		} else if (Character.isDigit(cur)) {
			Number value = getNumber(source);
			skipComma(source);
			return value;
			
		// Value is a List
		} else if (cur == '[') {
			index++;
			skipSpaces(source);
			cur = source.charAt(index);
			List<Object> list = new ArrayList<Object>();
			
			while (cur != ']') {
				// Get value recursively
				list.add(parseJsonHelper(source));
				skipComma(source);
				
				// Update cur
				cur = source.charAt(index);
			}
			
			if (index < source.length() - 1) index++;
			skipComma(source);
			return list;
			
		// Value is a Map
		} else if (cur == '{') {
			index++;
			skipSpaces(source);
			cur = source.charAt(index);
			Map<String, Object> map = new HashMap<String, Object>();
			
			while (cur != '}') {
				// Ensure we have a key
				if (cur != '\"') {
					throw new IllegalArgumentException("Cannot find a key at index " 
							+ index + " of " + source);
				}	
				
				// Extract the key and move index to the value.
				String key = getString(source);
				setIndexToValue(source);
				
				// Get value recursively
				map.put(key, parseJsonHelper(source));
				skipComma(source);
				
				// Update cur
				cur = source.charAt(index);
			}
			
			if (index < source.length() - 1) index++;
			skipComma(source);
			return map;
			
		} else {
			throw new IllegalArgumentException("Cannot parse " + cur + " at index " 
					+ index + " for " + source);
		}
	}
	
	
	// Parses a number and moves the index to the end of the value.
	private static Number getNumber(String source) {
		int end;
		int endComma = source.indexOf(',', index + 1);
		int endBrace = source.indexOf('}', index + 1);
		int endBracket = source.indexOf(']', index + 1);
			
		if (endComma == -1 && endBrace == -1 && endBracket == -1) {
			throw new IllegalArgumentException("Cannot parse number at index " 
				+ index + " in " + source);
		}
			
		if (endComma == -1) endComma = Integer.MAX_VALUE;
		if (endBrace == -1) endBrace = Integer.MAX_VALUE;
		if (endBracket == -1) endBracket = Integer.MAX_VALUE;
		
		end = Math.min(endComma, Math.min(endBrace, endBracket));
		
		String substring = source.substring(index, end);
		substring = substring.trim();
		index = end;
		
		if (substring.contains(".")) {
			return Double.parseDouble(substring);
			
		} else {
			return Integer.parseInt(substring);
		}
	}
	
	
	// Skips one comma and as many spaces if finds.
	private static void skipComma(String source) {
		// If there is a comma, skip it.
		skipSpaces(source);
		if (source.charAt(index) == ',') {
			index++;
			skipSpaces(source);
		}
	}
	
	
	// Parses boolean and moves the index to the end of the value.
	private static boolean getBoolean(String source) {
		int end;
		int endComma = source.indexOf(',', index + 1);
		int endBrace = source.indexOf('}', index + 1);
		int endBracket = source.indexOf(']', index + 1);
			
		if (endComma == -1 && endBrace == -1 && endBracket == -1) {
			throw new IllegalArgumentException("Cannot parse number at index " 
				+ index + " in " + source);
		}
			
		if (endComma == -1) endComma = Integer.MAX_VALUE;
		else if (endBrace == -1) endBrace = Integer.MAX_VALUE;
		else if (endBracket == -1) endBracket = Integer.MAX_VALUE;
		
		end = Math.min(endComma, Math.min(endBrace, endBracket));
		
		String substring = source.substring(index, end); 
		index = end;
		
		if (substring.contains("true")) return true;
		if (substring.contains("false")) return false;
		throw new IllegalArgumentException("Cannot parse boolean at index " 
				+ index + " in " + source);
	}
	
	
	// Returns the index of the next value.
	private static void setIndexToValue(String source) {
		index = source.indexOf(':', index);
		index++;
		skipSpaces(source);
	}
	
	
	/*
	 * Given the source string and the index of a first quote, returns the substring 
	 * between the quotes. Assumes the existence of ending quotes.
	 * Moves the index to the end of the extracted String.
	 */
	private static String getString(String source) {
		char first = source.charAt(index);
		if (first != '\"') {
			throw new IllegalArgumentException("Expecting: \" but got " + first);
		}
		
		int end = source.indexOf('\"', index + 1);
		if (end == -1) {
			throw new IllegalArgumentException("Incorrect JSON String format on index: " 
					+ index + " of " + source);
		}
		
		String substring = source.substring(index + 1, end);
		index = end;
		return substring; 
	}
	
	
	// Returns the index of the next valid character.
	private static void skipSpaces(String source) {
		while (true) {
			char cur = source.charAt(index);
			if (cur != ' ' && cur != '\n' && cur != '\t' && cur != '\r' 
					&& cur != '\f' && index < source.length()) break;
			index++;
		}
	}
	
	
	// To be used for testing purposes only.
	public static void main( String [] args ) {
		//String json = "{\"firstName\": \"John\", \"lastName\": \"Smith\", \"age\": 25, \"address\": { \"streetAddress\": \"21 2nd Street\",\"city\": \"New York\", \"state\": \"NY\", \"postalCode\": 10021},\"phoneNumbers\": [{\"type\": \"home\",\"number\": \"212 555-1234\"}, {\"type\": \"fax\", \"number\": \"646 555-4567\"}]}";
		//String json = "[1,2,3,4,5]";
		String json = "{\"quizMetaData\":{\"name\":\"Algebra\",\"description\":\"Slopes and Lines\",\"isImmediate\":true,\"isRandom\":true,\"isSinglePage\":false,\"questions\":[]}";
		//String json = "[]";
		
		System.out.println(parseJsonObject(json));
		
		
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("name", "sam");
//		map.put("fav number", 22);
//		map.put("isCoding", true);
//		map.put("double", 45.987);
//		List<Object> list = new ArrayList<Object>();
//		list.add(1);
//		list.add("hey");
//		list.add(false);
//		map.put("list", list);
//		
//		Set<Object> set = new HashSet<Object>();
//		set.add(4);
//		set.add(true);
//		set.add(9.8);
//		set.add("yo");
//		map.put("set", set);
//		
//		Map<String, Object> innerMap = new HashMap<String, Object>();
//		innerMap.put("inside", 5);
//		innerMap.put("hello!", "word");
//
//		list.add(innerMap);
//		map.put("innerMap", innerMap);
//		
//		System.out.println(getJsonString(map));
	}
}
