package com.dbinterface;

import java.util.Map;

/**
 * This is a program that will provide an interface
 * for interacting with a remote mySql database
 * @author eliezer
 * @date Feb/23/2015 (last edit)
 * @time 8:18PM 
 */
public class Database {
	private String tableName;
	private Map<String, String> columnTypeAndName;
	
	/**
	 * Given a name for a MYSQL table and a map of column 
	 * types and names, constructs a MYSQL table. For example
	 * if passed:
	 * 
	 *  the String "universities" and the map:
	 * 
	 * [{"String", "universityName"}, {"double", "population"}]
	 * 
	 * it will make a database table named universities with the
	 * columns universityName and population. 
	 * 
	 * Note: Valid keys for Map are only ("String", "double", 
	 * "int", "long"). All other keys are invalid and constructor
	 * will throw an exception.
	 * 
	 * @param tableName String value of the table name
	 * @param columnNames Map of column types and names
	 */
	public Database(String tableName, Map<String, String> columnTypeAndName) throws RuntimeException {
		if ( tableName == null ) {
			throw new RuntimeException("tableName is null");
		} else if ( tableName.equals("") ) {
			throw new RuntimeException("tableName is empty");
		}
		
 		this.tableName = tableName;
		
		if ( columnTypeAndNameValid( columnTypeAndName ) ) {
			this.columnTypeAndName = columnTypeAndName;
		}
	}
	
	/*
	 * Checks to see if the map's keys contain valid datatypes
	 */
	private boolean columnTypeAndNameValid( Map<String, String> columnTypeAndName) {
		if ( columnTypeAndName == null ) {
			throw new RuntimeException("columnTypeAndName is null");
		}

		for (String key : columnTypeAndName.keySet()) {
			if ( key.equals( "String") ) {
				// Good, do nothing!
			} else if ( key.equals( "double") ) {
				// Actually don't need this right now.
//				String value = columnTypeAndName.get(key);
//				try {
//					Double d = Double.parseDouble(value);
//				} catch ( NumberFormatException e) {
//					throw new RuntimeException("Value for type \"double\" is invalid");
//				}
			} else if ( key.equals( "int") ) {
//				String value = columnTypeAndName.get(key);
//				try {
//					Integer i = Integer.parseInt(value);
//				} catch ( NumberFormatException e) {
//					throw new RuntimeException("Value for type \"int\" is invalid");
//				}
			} else if ( key.equals( "long") ) {
//				try {
//					Long l = Long.parseLong(value);
//				} catch ( NumberFormatException e) {
//					throw new RuntimeException("Value for type \"long\" is invalid");
//				}
			} else {
				throw new RuntimeException( key +  " is an invalid datatype" );
			}
		}
		return true;
	}
	
	/**
	 * Allows the client to add a row to the table based on the
	 * legal name and type specified in the heading.
	 */
	
}
