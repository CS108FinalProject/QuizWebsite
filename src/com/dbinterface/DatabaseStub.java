package com.dbinterface;

import java.util.List;
import java.util.Map;

/**
 * Mock class so that we can stub the interfaces we need.
 * These stub methods should be implemented in the Database class
 * and deleted from this one afterward. 
 * @author Sam
 *
 */
public class DatabaseStub {
	
	/**
	 * Similar to the already implemented database constructor except it should be a static method
	 * (lets not instantiate an object for each table). This should throw an exception if the table
	 * name already exists. Also, boolean should be added to the list of types (please take care of 
	 * interfacing between boolean and int(0,1) for this case) It will make our Java lives easier ;)
	 *
	 * @param tableName
	 * @param columnTypeAndName
	 */
	public static void createTable(String tableName, Map<String, String> columnTypeAndName) {}
	
	
	/**
	 * Self explanatory.
	 * @param tableName
	 * @return
	 */
	public static boolean tableExists(String tableName) {
		return false;
	}
	
	
	/**
	 * Should add the passed row to the specified table. Check correct type for each Object.
	 * Use the Util.validateObject method for this.
	 * @param tableName
	 * @param row key = column name, value = table value
	 * throw exception on failure (type missmatch, etc...)
	 */
	public static void addRow(String tableName, Map<String, Object> row) {}
	
	
	/**
	 * In the specified table, removes all the rows that match the given description.
	 * @param tableName
	 * @param columnGuide1 name of the 1st column that will be used to determine the row  
	 * @param guideValue1 value that columnGuide1 column should have in order for row to match
	 * @param columnGuide2 name of the 2nd column that will be used to determine the row  
	 * @param guideValue2 value that columnGuide2 column should have in order for row to match
	 * @return number of rows removed.
	 * 
	 * Example:
	 * removeRows("Friends", "userName", "Eliezer", "Friend", "Sam");
	 * 
	 * finds all the rows in the "Friends" table that have "Eliezer" under the "userName" column,
	 * and "Sam" under the "Friend" column and removes them. 
	 *  
	 */
	public static int removeRows(String tableName, String columnGuide1, String guideValue1, 
			String columnGuide2, String guideValue2) {
		return 0;
	}
	
	
	/**
	 * Should return a list of rows (each a Map<String, Object>) where columnName is equals to the value.
	 * 
	 * Example: getRows("Accounts", "userName", "Eliezer") returns the List of rows (each a Map) where the
	 * value under the userName column is Eliezer.
	 * 
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
	 * Modify the passed value in the specified table location.
	 * @param tableName 
	 * @param columnGuide name of the column that will be used to determine the row that should be modified  
	 * @param guideValue all the rows that have this in value in the "columnGuide column" should be modified
	 * @param columnToBeSet once the rows has been located, the columnToBeSet is the name of the column that should be 
	 * 			modified in these rows
	 * @param columnToBeSetValue the actual value that is being added (check for type correctness)
	 * 
	 * Should throw exception on failure (type missmatch, etc...)
	 * 
	 * Example: setValue("Accounts", "userName", "Eliezer", "password", "thisIsMyNewPassword");
	 * look for all the rows that have value = "Eliezer" under the "userName" column and modify their "password" column
	 * to be "thisIsMyNewPassword" 
	 * 
	 * Should return the amount of rows that it modified.
	 * 
	 */
	public static int setValues(String tableName, String columnGuide, String guideValue,
			String columnToBeSet, Object columnToBeSetValue) {
		return 0;
	}
	
	
	/**
	 * Same as the previous but now with 2 specifiers.
	 * @param tableName
	 * @param columnGuide1
	 * @param guideValue1
	 * @param columnGuide2
	 * @param guideValue2
	 * @param columnToBeSet
	 * @param columnToBeSetValue
	 */
	public static int setValues(String tableName, String columnGuide1, String guideValue1, String columnGuide2,
			String guideValue2, String columnToBeSet, Object columnToBeSetValue) {
		return 0;
	}
	
	
	/**
	 * Return a list of values that match the specified location.
	 * @param tableName
	 * @param columnGuide name of the column that will be used to determine the row
	 * @param guideValue all the rows that have this in value in the "columnGuide column" should be explored
	 * @param columnToGet once the rows has been located, the columnToGet is the name of the column that should be 
	 * 			obtained in these rows
	 * @return the value at location
	 * 
	 * Example: similar to previous but you are returning instead of modifying. Return null if nothing found.
	 * 
	 */
	public static List<Object> getValues(String tableName, String columnGuide, String guideValue, String columnToGet) {
		return null;
	}
	
	
	/**
	 * Same as the previous method but now it has 2 specifiers.
	 * 
	 * Example: getValues("Friends", "userName", "Eliezer", "status", "friends", "friend");
	 * 
	 * In every row in the "Friends" table where under the "userName" column the value = "Eliezer" and under the
	 * "status" column the value = "friends" take the value under the "friend" column and add it to a list.
	 * Return the list or null if nothing was found.
	 * 
	 * @param tableName
	 * @param columnGuide1
	 * @param guideValue1
	 * @param columnGuide2
	 * @param guideValue2
	 * @param columnToGet
	 * @return
	 */
	public static List<Object> getValues(String tableName, String columnGuide1, String guideValue1, String columnGuide2, 
			String guideValue2, String columnToGet) {
		return null;
	}

}
