package com.dbinterface;

import java.util.List;
import java.util.Map;

/**
 * Mock class so that we can stub the interfaces we need.
 * These stub methods should be implemented in the Database class
 * and deleted from this one. 
 * @author Sam
 *
 */
public class DatabaseStub {
	
	/**
	 * Should add the passed row to the specified table. Check correct type for each Object.
	 * Use the Util.validateObject method.
	 * @param tableName
	 * @param row key = column name, value = table value
	 * throw exception on failure (type missmatch, etc...)
	 */
	public static void addRow(String tableName, Map<String, Object> row) {
	}
	
	/**
	 * Should return a list of rows (each a Map) where columnName is equals to the value.
	 * Use Util.validateObject() method to ensure that correct object type is passed.
	 * Should return null if no such row is found.
	 * @param tableName 
	 * @param columnName
	 * @param value
	 * @return
	 */
	public static List<Map<String, Object>> getRows(String tableName, String columnName, Object value) {
		return null;
	}
	
	/**
	 * Modified the passed value in the specified table and location.
	 * @param tableName 
	 * @param columnGuide name of the column that will be used to determine the row that should be modified  
	 * @param guideValue all the rows that have this in value in the "columnGuide column" should be modified
	 * @param columnToBeSet once the row has been located, the columnToBeSet is the name of the column that should be 
	 * 			modified in this row
	 * @param columnToBeSetValue the actual value that is being added (check for type correctness)
	 * Should throw exception on failure (type missmatch, etc...)
	 */
	public static void setValue(String tableName, String columnGuide, String guideValue,
			String columnToBeSet, Object columnToBeSetValue) {
	}

}
