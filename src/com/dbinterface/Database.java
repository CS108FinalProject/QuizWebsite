package com.dbinterface;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.sql.Connection;

import com.util.Constants;
import com.util.Util;


/**
 * This class provides an interface to the remote mySql database.
 * @author eliezer && Sam
 */
public class Database implements Constants {
	private static Statement stmt;
	private static Connector con;
	
	
	/**
	 * Establishes a connection with the database.
	 * Has to be instantiated once before calling any of the static
	 * methods in this class.
	 */
	public Database() {
		con = new Connector();
		stmt = con.getStatement();
	}
	
	
	/**
	 * Removes a table.
	 * @param tableName name of the table in the database to be removed
	 */
	public static void removeTable(String tableName) {
		if ( tableName == null ) {
			throw new RuntimeException("Table name cannot be null");
		}
		String query = "DROP TABLE " + tableName + ";";
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Problem executing query: " + query);
		}
	}
	
	
	/**
	 * Shows all tables in database specified by MyDBInfo file.
	 * To be used for error checking and validation.
	 * @return String list of table names in the database
	 */
	public static String displayTables() {
		String output = "Empty";
		String query = String.format("SHOW TABLES;");
		try {
			output = "";
			ResultSet rs = stmt.executeQuery(query);
			rs.beforeFirst();
			output += "Tables in: " + MyDBInfo.MYSQL_DATABASE_NAME + "\n";
			while ( rs.next() ) {
				output += rs.getString("Tables_in_" + MyDBInfo.MYSQL_DATABASE_NAME) + "\n";
			}
		} catch (SQLException e) {
			
			output = "Query Failed";
			e.printStackTrace();
		}
		return output;
	}
	
	
	/**
	 * Given a name for a MYSQL table and a map of column 
	 * types and names, constructs a MYSQL table. For example
	 * if passed:
	 * 
	 *  the String "universities" and the map:
	 * 
	 * [{"universityName", "String" }, { "population", "double"}]
	 * 
	 * it will make a database table named universities with the
	 * columns universityName and population. 
	 * 
	 * Note: Valid keys for Map are only ("String", "double", 
	 * "int", "long"). All other keys are invalid and constructor
	 * will throw an exception.
	 * 
	 * @param tableName String value of the table name
	 * @param columnTypeAndName Map of column types and names
	 * 
	 */
	public static void createTable(String tableName, Map<String, String> columnTypeAndName ) throws RuntimeException {
		
		//***************ERROR CHECKING*****************//
		if ( tableName == null ) {
			throw new RuntimeException("tableName is null");
		} else if ( tableName.equals("") ) {
			throw new RuntimeException("tableName is empty");
		}
		
		// Check that table doesn't already exist.
		if (tableExists(tableName)) {
			throw new RuntimeException("Table: " + tableName + " already exists.");
		}
		
 		// Ensure that columnTypes are valid
 		validateColumnTypeAndName(tableName, columnTypeAndName);
 		
 		//*****************TABLE CREATION******************//
 		String query = getCreationQuery(tableName, columnTypeAndName);
 		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Problem executing query: " + query);
		}
	}
	
	
	/**
	 * Adds the passed row to the specified table. 
	 * @param tableName
	 * @param row a Map that represents the row to be added. (key = column name, value = table value)
	 */
	public static void addRow(String tableName, Map<String, Object> row) {
		Util.validateString(tableName);
		
		if (!tableExists(tableName)) {
			throw new RuntimeException(tableName + " is not a valid table");
		}
		
		if ( row == null ) {
				throw new RuntimeException("Null value passed for row");
		}
			
		int columnCount = getColumnCount(tableName);
		
		// each map entry must correspond to a column
		if ( row.size() != columnCount ) {
			throw new RuntimeException("Wrong number of entries in row");
		}
		
		// Validate each field.
		for(String columnName : row.keySet()) {
			Object value = row.get(columnName);
			String type = getColumnType(tableName, columnName);
			Util.validateObjectType(value, type);
		}
	    
	    // Transform map for DB compatibility.
	    normalizeObjectMapForDB(row);
	    
	    // if we get here we know our row content is good
    	String query = insertQuery(tableName, row);
	    try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Problem executing query: " + query);
		}
	}

	
	/**
	 * Determines whether the specified table exists in the database.
	 * @param tableName
	 * @return true if the specified table exists in the database, false otherwise.
	 */
	public static boolean tableExists(String tableName) {
		Set<String> tables = new HashSet<String>();
		String query = String.format("SHOW TABLES;");
		try {
			Statement statement = getNewStatement();
			ResultSet rs = statement.executeQuery(query);
			
			while (rs.next()) {
				tables.add( rs.getString("Tables_in_" + MyDBInfo.MYSQL_DATABASE_NAME));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Problem with query: " + query);
		}
		return tables.contains(tableName);
	}
	
	
	/**
	 * Returns a whole table. Each row is a Map.
	 * Returns null if there is no such table or an empty list if the table is empty.
	 * @param tableName name of the table in database
	 * @return list of maps containing row entries
	 */
	public static List<Map<String, Object>> getTable(String tableName) {
		if (!tableExists(tableName)) return null;
		
		int c = getRowCount(tableName);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if ( c == 0 ) return list; // empty list
		
		String query = "SELECT * FROM " + tableName;
		try {
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			
			while ( rs.next() ) {
				Map<String, Object> entry = new HashMap<String, Object>();
				for(int i = 1; i <= columnCount; i++) {
					String className = rsmd.getColumnClassName(i);
					String columnName = rsmd.getColumnName(i);
					String value = rs.getString(columnName);
					Object valObj = getObject(className, value);
					entry.put(columnName, valObj);
				}
				
				normalizeObjectMap(tableName, entry);
				list.add( entry );
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Problem executing query: " + query);
		}
		return list;
	}
	
	
	/**
	 * Returns a whole sorted table. Each row is a Map.
	 * Returns null if there is no such table or an empty list if the table is empty.
	 * @param tableName name of the table in database
	 * @param sortBy column to sort by
	 * @param descending if true sort descending, and ascending if false 
	 * @return list of maps containing row entries
	 */
	public static List<Map<String, Object>> getSortedTable(String tableName, String sortBy, 
			boolean descending) {
		
		if (!tableExists(tableName)) return null;
		
		int c = getRowCount(tableName);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if ( c == 0 ) return list; // empty list
		
		String query = "SELECT * FROM " + tableName + " ORDER BY " + sortBy;
		if (descending) query += " DESC";
		
		try {
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			
			while ( rs.next() ) {
				Map<String, Object> entry = new HashMap<String, Object>();
				for(int i = 1; i <= columnCount; i++) {
					String className = rsmd.getColumnClassName(i);
					String columnName = rsmd.getColumnName(i);
					String value = rs.getString(columnName);
					Object valObj = getObject(className, value);
					entry.put(columnName, valObj);
				}
				
				normalizeObjectMap(tableName, entry);
				list.add( entry );
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Problem executing query: " + query);
		}
		return list;
	}
	
	
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
	 */
	public static int removeRows(String tableName, String columnGuide1, Object guideValue1, 
			String columnGuide2, Object guideValue2) {
		
		Util.validateString(tableName);
		Util.validateString(columnGuide1);
		Util.validateString(columnGuide2);
		Util.validateObjectType(guideValue1, getColumnType(tableName, columnGuide1));
		Util.validateObjectType(guideValue2, getColumnType(tableName, columnGuide2));
		
		if(!tableExists(tableName)) {
			throw new RuntimeException("Table " +  tableName + " does not exist.");
		}
		
		// Get amount of rows before deletion.
		int rowCount = getRowCount(tableName);
		
		// Interface boolean.
		if (guideValue1 instanceof Boolean) guideValue1 = getIntFromBoolean((Boolean) guideValue1);
		if (guideValue2 instanceof Boolean) guideValue2 = getIntFromBoolean((Boolean) guideValue2);
		
		String query = "";
		try {
			query = "DELETE FROM " + tableName + " WHERE " + columnGuide1 
					+ " = \"" + guideValue1 + "\" AND " + columnGuide2 + " = \"" + guideValue2 + "\"";
			stmt.executeUpdate(query);
			
		} catch (SQLException e) {
			throw new RuntimeException("Problem executing query: " + query);
		}
		
		return rowCount - getRowCount(tableName);
	}
	
	
	/**
	 * In the specified table, removes all the rows that match the given description.
	 * @param tableName
	 * @param columnGuide name of the column that will be used to determine the row  
	 * @param guideValue value that columnGuide column should have in order for row to match
	 * @return number of rows removed.
	 * 
	 * Example:
	 * removeRows("Friends", "userName", "Eliezer");
	 * 
	 * finds all the rows in the "Friends" table that have "Eliezer" under the "userName" column,
	 * and removes them. 
	 */
	public static int removeRows(String tableName, String columnGuide, Object guideValue) {
		Util.validateString(tableName);
		Util.validateString(columnGuide);
		Util.validateObjectType(guideValue, getColumnType(tableName, columnGuide));
		
		if(!tableExists(tableName)) {
			throw new RuntimeException("Table " +  tableName + " does not exist.");
		}
		
		// Get amount of rows before deletion.
		int rowCount = getRowCount(tableName);
		
		// Interface boolean.
		if (guideValue instanceof Boolean) guideValue = getIntFromBoolean((Boolean) guideValue);
		
		String query = "";
		try {
			query = "DELETE FROM " + tableName + " WHERE " + columnGuide + " = \"" + guideValue + "\"";
			stmt.executeUpdate(query);
			
		} catch (SQLException e) {
			throw new RuntimeException("Problem executing query: " + query);
		}
		
		return rowCount - getRowCount(tableName);
	}
	

	/**
	 * In the specified table, removes all the rows that match the given description.
	 * @param tableName
	 * @param row Map that has all the corresponding columns and values to match  
	 * @return number of rows removed.
	 */
	public static int removeRows(String tableName, Map<String, Object> row) {
		Util.validateString(tableName);
		
		if (row == null) throw new NullPointerException();
		
		if(!tableExists(tableName)) {
			throw new RuntimeException("Table " +  tableName + " does not exist.");
		}
		
		// Get amount of rows before deletion.
		int rowCount = getRowCount(tableName);
		
		// Interface boolean.
		normalizeObjectMapForDB(row);
		
		String query = "DELETE FROM " + tableName + " WHERE ";
		try {
			String trail = " AND ";
			for (String columnGuide : row.keySet()) {
				query += columnGuide + " = \"" + row.get(columnGuide) + "\"" + trail;
			}
			
			query = query.substring(0, query.length() - trail.length());
			stmt.executeUpdate(query);
			
		} catch (SQLException e) {
			throw new RuntimeException("Problem executing query: " + query);
		}
		return rowCount - getRowCount(tableName);
	}
	
	
	/**
	 * Returns a list of rows (each a Map<String, Object>) where columnName is equals 
	 * to the value.
	 * 
	 * Example: getRows("Accounts", "userName", "Eliezer") returns the List of rows 
	 * (each a Map) where the value under the userName column is Eliezer.
	 * @param tableName 
	 * @param columnName to look for a match in
	 * @param value to be matched
	 * @return a list of rows in Map form, or null if criteria was not matched.
	 */
	public static List<Map<String, Object>> getRows(String tableName, String columnName, 
			Object value) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		Util.validateString(tableName);
		Util.validateString(columnName);
		
		if (!tableExists(tableName)) { 
			throw new RuntimeException(tableName + " does not exist in the database.");
		}
		
		// validate object type
		String type = getColumnType(tableName, columnName);
		Util.validateObjectType(value, type);
		
		// Interface boolean.
		if (type.equals(BOOLEAN)) value = getIntFromBoolean((Boolean) value);
		
		// Know passed type is a valid at this point; implement sql query
		String query = "SELECT * FROM " + tableName + " WHERE " + 
						columnName + " = " + "\"" + value + "\"";
		
		try {
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next() ) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int numColumns = getColumnCount(tableName);
				Map<String, Object> map = new HashMap<String, Object>();
				
				for(int i = 1; i <= numColumns; i++) {
					
					Object obj = rs.getObject(i);
					String column = rsmd.getColumnName(i);
					map.put(column, obj);
				}
				
				normalizeObjectMap(tableName, map);
				list.add(map);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Problem executing query: " + query);
		}
		
		if (list.size() == 0) return null;
		return list;
	}
	
	
	/**
	 * Returns a list of rows (each a Map<String, Object>) where columnName1 equals 
	 * value1 and columnName2 equals value2.
	 */
	public static List<Map<String, Object>> getRows(String tableName, String columnName1, 
			Object value1, String columnName2, Object value2) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		Util.validateString(tableName);
		Util.validateString(columnName1);
		Util.validateString(columnName1);
		
		if (!tableExists(tableName)) { 
			throw new RuntimeException(tableName + " does not exist in the database.");
		}
		
		// validate object type
		String type = getColumnType(tableName, columnName1);
		Util.validateObjectType(value1, type);
		
		// Interface boolean.
		if (type.equals(BOOLEAN)) value1 = getIntFromBoolean((Boolean) value1);
		
		type = getColumnType(tableName, columnName2);
		Util.validateObjectType(value2, type);
		
		// Interface boolean.
		if (type.equals(BOOLEAN)) value2 = getIntFromBoolean((Boolean) value2);
		
		String query = "SELECT * FROM " + tableName + " WHERE " + columnName1 + " = " + "\"" 
						+ value1 + "\" AND " + columnName2 + " = " + "\"" + value2 + "\"";
		
		try {
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next() ) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int numColumns = getColumnCount(tableName);
				Map<String, Object> map = new HashMap<String, Object>();
				
				for(int i = 1; i <= numColumns; i++) {
					
					Object obj = rs.getObject(i);
					String column = rsmd.getColumnName(i);
					map.put(column, obj);
				}
				
				normalizeObjectMap(tableName, map);
				list.add(map);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Problem executing query: " + query);
		}
		
		if (list.size() == 0) return null;
		return list;
	}
	
	
	/**
	 * Given a row specified by a Map, returns a list of rows that are equal to the passed row.
	 * @param tableName
	 * @param row
	 */
	public static List<Map<String, Object>> getRows(String tableName, Map<String, Object> row) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		Util.validateString(tableName);
		Util.validateObject(row);
		
		if (!tableExists(tableName)) { 
			throw new RuntimeException(tableName + " does not exist in the database.");
		}
		
		// Interface boolean
		normalizeObjectMapForDB(row);
		
		// Build query
		String query = "SELECT * FROM " + tableName + " WHERE ";
		int counter = 0;
		for (String columnName : row.keySet()) {
			query += columnName + " = \"" + row.get(columnName)  + "\"";
				
			if (++counter < row.size()) {
				query += " AND ";
			}
		}
		
		try {
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next() ) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int numColumns = getColumnCount(tableName);
				Map<String, Object> map = new HashMap<String, Object>();
				
				for(int i = 1; i <= numColumns; i++) {
					Object obj = rs.getObject(i);
					String column = rsmd.getColumnName(i);
					map.put(column, obj);
				}
				
				// Interface boolean.
				normalizeObjectMap(tableName, map);
				list.add(map);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Problem executing query: " + query);
		}
		
		// Interface boolean on original map.
		normalizeObjectMap(tableName, row);
		if (list.size() == 0) return null;
		return list;
	}
	
	
	/**
	 * Returns a list of sorted rows that match the specified criteria.
	 * @param tableName to get rows from
	 * @param columnName to match a value for
	 * @param value value to be matched
	 * @param sortBy column to sort
	 * @param descending sort descending if true, ascending if false.
	 * @return a list of matching rows or null ir none were found.
	 */
	public static List<Map<String, Object>> getSortedRows(String tableName, String columnName, 
			Object value, String sortBy, boolean descending) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		Util.validateString(tableName);
		Util.validateString(columnName);
		Util.validateString(sortBy);
		
		if (!tableExists(tableName)) { 
			throw new RuntimeException(tableName + " does not exist in the database.");
		}
		
		// validate object type
		String type = getColumnType(tableName, columnName);
		Util.validateObjectType(value, type);
		
		// Interface boolean.
		if (type.equals(BOOLEAN)) value = getIntFromBoolean((Boolean) value);
		
		// Define query
		String query = "SELECT * FROM " + tableName + " WHERE " + 
						columnName + " = " + "\"" + value + "\" ORDER BY " + sortBy;
		
		if (descending) {
			query += " DESC";
		}
		
		try {
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next() ) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int numColumns = getColumnCount(tableName);
				Map<String, Object> map = new HashMap<String, Object>();
				
				for(int i = 1; i <= numColumns; i++) {
					
					Object obj = rs.getObject(i);
					String column = rsmd.getColumnName(i);
					map.put(column, obj);
				}
				
				normalizeObjectMap(tableName, map);
				list.add(map);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Problem executing query: " + query);
		}
		
		if (list.size() == 0) return null;
		return list;
	}
	
	
	/**
	 * Queries the database performing a comparison and sorting by the specified
	 * criteria.
	 */
	public static List<Map<String, Object>> getSortedRowsWithComparison(String tableName, 
			String columnGuide, boolean isGreater, Object value, String sortBy, 
			boolean descending) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		Util.validateString(tableName);
		Util.validateString(columnGuide);
		Util.validateString(sortBy);
		
		if (!tableExists(tableName)) { 
			throw new RuntimeException(tableName + " does not exist in the database.");
		}
		
		// validate object type
		String type = getColumnType(tableName, columnGuide);
		Util.validateObjectType(value, type);
		
		// Interface boolean.
		if (type.equals(BOOLEAN)) value = getIntFromBoolean((Boolean) value);

		// Determine comparison.
		String comparison = (isGreater) ? " > " : " < ";
		
		// Define query
		String query = "SELECT * FROM " + tableName + " WHERE " + 
						columnGuide + comparison + "\"" + value + "\" ORDER BY " + sortBy;
		
		if (descending) {
			query += " DESC";
		}
		
		try {
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next() ) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int numColumns = getColumnCount(tableName);
				Map<String, Object> map = new HashMap<String, Object>();
				
				for(int i = 1; i <= numColumns; i++) {
					
					Object obj = rs.getObject(i);
					String column = rsmd.getColumnName(i);
					map.put(column, obj);
				}
				
				normalizeObjectMap(tableName, map);
				list.add(map);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Problem executing query: " + query);
		}
		
		if (list.size() == 0) return null;
		return list;
	}
	
	
	/**
	 * Returns a list of sorted rows that match the specified criteria.
	 * @param tableName to get rows from
	 * @param columnName to match a value for
	 * @param value to be matched
	 * @param sortBy1 first column to sort by
	 * @param sortBy2 second column to sort by
	 * @param descending1 sort the first column descending if true, ascending if false.
	 * @param descending2 sort the seconde column descending if true, ascending if false.
	 * @return a list of matching rows or null ir none were found.
	 */
	public static List<Map<String, Object>> getSortedRows(String tableName, String columnName, 
			Object value, String sortBy1, boolean descending1, String sortBy2, boolean descending2) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		Util.validateString(tableName);
		Util.validateString(columnName);
		Util.validateString(sortBy1);
		Util.validateString(sortBy2);
		
		if (!tableExists(tableName)) { 
			throw new RuntimeException(tableName + " does not exist in the database.");
		}
		
		// validate object type
		String type = getColumnType(tableName, columnName);
		Util.validateObjectType(value, type);
		
		// Interface boolean.
		if (type.equals(BOOLEAN)) value = getIntFromBoolean((Boolean) value);
		
		String order1 = (descending1) ? " DESC, " : ", ";
		String order2 = (descending2) ? " DESC" : "";
		
		
		// Define query
		String query = "SELECT * FROM " + tableName + " WHERE " + columnName + " = " 
						+ "\"" + value + "\" ORDER BY " + sortBy1 + order1 + sortBy2 + order2;
		
		try {
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next() ) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int numColumns = getColumnCount(tableName);
				Map<String, Object> map = new HashMap<String, Object>();
				
				for(int i = 1; i <= numColumns; i++) {
					
					Object obj = rs.getObject(i);
					String column = rsmd.getColumnName(i);
					map.put(column, obj);
				}
				
				normalizeObjectMap(tableName, map);
				list.add(map);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Problem executing query: " + query);
		}
		
		if (list.size() == 0) return null;
		return list;
	}
	
	
	/**
	 * Modifies the passed value in the specified table location.
	 * @param tableName 
	 * @param columnGuide name of the column that will be used to determine the row that 
	 * should be modified  
	 * @param guideValue all the rows that have this in value in the "columnGuide column" 
	 * should be modified
	 * @param columnToBeSet once the rows has been located, the columnToBeSet is the name 
	 * of the column that should be modified in these rows
	 * @param columnToBeSetValue the actual value that is being added 
	 * @return amount of values that were modified
	 * 
	 * Example: setValue("Accounts", "userName", "Eliezer", "password", "thisIsMyNewPassword");
	 * look for all the rows that have value = "Eliezer" under the "userName" column and modify 
	 * their "password" column to be "thisIsMyNewPassword" 
	 */
	public static int setValues(String tableName, String columnGuide, Object guideValue,
			String columnToBeSet, Object columnToBeSetValue) {
		
		Util.validateString(tableName);
		Util.validateString(columnGuide);
		Util.validateString(columnToBeSet);
		Util.validateObjectType(guideValue, getColumnType(tableName, columnGuide));
		Util.validateObjectType(columnToBeSetValue, getColumnType(tableName, columnToBeSet));
		
		if(!tableExists(tableName)) {
			throw new RuntimeException("Table " +  tableName + " does not exist.");
		}
		
		// Interface boolean.
		boolean guideValueSwitch = false;
		if (guideValue instanceof Boolean) {
			guideValue = getIntFromBoolean((Boolean) guideValue);
			guideValueSwitch = true;
		}
		
		boolean columnToBeSetValueSwitch = false;
		if (columnToBeSetValue instanceof Boolean) {
			columnToBeSetValue = getIntFromBoolean((Boolean) columnToBeSetValue);
			columnToBeSetValueSwitch = true;
		}
		
		String query = "";
		try {
			query = "UPDATE " + tableName + " SET " + columnToBeSet + " = \"" 
					+ columnToBeSetValue + "\" WHERE " + columnGuide + " = \"" + guideValue + "\"";
			stmt.executeUpdate(query);
			
		} catch (SQLException e) {
			throw new RuntimeException("Problem executing query: " + query);
		}
		
		if (guideValueSwitch) {
			guideValue = getBooleanFromInt((Integer) guideValue);
		}
		if (columnToBeSetValueSwitch) {
			columnToBeSetValue = getBooleanFromInt((Integer) columnToBeSetValue);
		}
			
		List<Object> values = getValues(tableName, columnGuide, guideValue, columnToBeSet, 
				columnToBeSetValue, columnToBeSet);
		
		return values.size();
	}
	
	
	/**
	 * Modifies the passed value in the specified table location.
	 * @param tableName 
	 * @param columnGuide1 name of the first column that will be used to determine the row that 
	 * should be modified  
	 * @param guideValue1 all the rows that have this in value in the "columnGuide1 column" 
	 * should be modified
	 * @param columnGuide2 name of the second column that will be used to determine the row that 
	 * should be modified  
	 * @param guideValue2 all the rows that have this in value in the "columnGuide2 column" should 
	 * be modified
	 * @param columnToBeSet once the rows has been located, the columnToBeSet is the name of the 
	 * column that should be modified in these rows
	 * @param columnToBeSetValue the actual value that is being added (check for type correctness)
	 * @return amount of values that were modified
	 * 
	 * Example: setValue("Accounts", "userName", "Eliezer", "password", "thisIsMyNewPassword");
	 * look for all the rows that have value = "Eliezer" under the "userName" column and modify 
	 * their "password" column to be "thisIsMyNewPassword" 
	 */
	public static int setValues(String tableName, String columnGuide1, Object guideValue1, 
			String columnGuide2, Object guideValue2, String columnToBeSet, 
			Object columnToBeSetValue) {
		
		Util.validateString(tableName);
		Util.validateString(columnGuide1);
		Util.validateString(columnGuide2);
		Util.validateString(columnToBeSet);
		Util.validateObjectType(guideValue1, getColumnType(tableName, columnGuide1));
		Util.validateObjectType(guideValue2, getColumnType(tableName, columnGuide2));
		Util.validateObjectType(columnToBeSetValue, getColumnType(tableName, columnToBeSet));
		
		if(!tableExists(tableName)) {
			throw new RuntimeException("Table " +  tableName + " does not exist.");
		}
		
		// Interface boolean.
		boolean guideValueSwitch1 = false;
		if (guideValue1 instanceof Boolean) {
			guideValue1 = getIntFromBoolean((Boolean) guideValue1);
			guideValueSwitch1 = true;
		}
		
		boolean guideValueSwitch2 = false;
		if (guideValue2 instanceof Boolean) {
			guideValue2 = getIntFromBoolean((Boolean) guideValue2);
			guideValueSwitch2 = true;
		}
		
		if (columnToBeSetValue instanceof Boolean) {
			columnToBeSetValue = getIntFromBoolean((Boolean) columnToBeSetValue);
		}
		
		String query = "";
		try {
			query = "UPDATE " + tableName + " SET " + columnToBeSet + " = \"" 
					+ columnToBeSetValue + "\" WHERE " + columnGuide1 + " = \"" + guideValue1 
					+ "\" AND " + columnGuide2 + " = \"" + guideValue2 + "\"";
			
			stmt.executeUpdate(query);
			
		} catch (SQLException e) {
			throw new RuntimeException("Problem executing query: " + query);
		}
		
		if (guideValueSwitch1) {
			guideValue1 = getBooleanFromInt((Integer) guideValue1);
		}
		if (guideValueSwitch2) {
			guideValue2 = getBooleanFromInt((Integer) guideValue2);
		}
			
		List<Object> values = getValues(tableName, columnGuide1, guideValue1, columnGuide2, 
				guideValue2, columnToBeSet);
		
		return values.size();
	}
	
	
	/**
	 * Returns a list of specified values from the database. (1 specifier)
	 * 
	 * Example: getValues("Friends", "userName", "Eliezer", "status", "friends", "friend");
	 * 
	 * In every row in the "Friends" table where under the "userName" column the value = "Eliezer" 
	 * and under the "status" column the value = "friends" take the value under the "friend" 
	 * column and add it to a list.
	 * 
	 * @param tableName the requested table
	 * @param columnGuide1 the first specified column
	 * @param guideValue1 the first specified value
	 * @param columnGuide2 the second specified column
	 * @param guideValue2 the second specified value
	 * @param columnToGet the column where the returned values will be in.
	 * @return a list of objects matching the specification, or null if non were found
	 */
	public static List<Object> getValues(String tableName, String columnGuide, Object guideValue, 
			String columnToGet) {
		
		Util.validateString(tableName);
		Util.validateString(columnGuide);
		Util.validateString(columnToGet);
		Util.validateObjectType(guideValue, getColumnType(tableName, columnGuide));
		
		if(!tableExists(tableName)) {
			throw new RuntimeException("Table " +  tableName + " does not exist.");
		}
		
		// Interface boolean.
		if (guideValue instanceof Boolean) guideValue = getIntFromBoolean((Boolean) guideValue);
		
		List<Object> result = new ArrayList<Object>();
		
		String query = "";
		try {
			query = "SELECT * FROM " + tableName + " WHERE " + columnGuide + " = \"" + guideValue + "\"";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				if (getColumnType(tableName, columnToGet).equals(BOOLEAN)) {
					result.add(getBooleanFromInt((Integer)rs.getObject(columnToGet)));
				} else {
					result.add(rs.getObject(columnToGet));
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Problem executing query: " + query);
		}
			
		if (result.size() == 0) return null;
		return result;
	}
	
	
	/**
	 * Returns a list of specified values from the database. (2 specifiers)
	 * 
	 * Example: getValues("Friends", "userName", "Eliezer", "status", "friends", "friend");
	 * 
	 * In every row in the "Friends" table where under the "userName" column the value = "Eliezer" 
	 * and under the "status" column the value = "friends" take the value under the "friend" column 
	 * and add it to a list. 
	 * 
	 * @param tableName the requested table
	 * @param columnGuide1 the first specified column
	 * @param guideValue1 the first specified value
	 * @param columnGuide2 the second specified column
	 * @param guideValue2 the second specified value
	 * @param columnToGet the column where the returned values will be in.
	 * @return a list of objects matching the specification, or null if non were found
	 */
	public static List<Object> getValues(String tableName, String columnGuide1, Object guideValue1, 
			String columnGuide2, Object guideValue2, String columnToGet) {
		
		Util.validateString(tableName);
		Util.validateString(columnGuide1);
		Util.validateString(columnGuide2);
		Util.validateString(columnToGet);
		Util.validateObjectType(guideValue1, getColumnType(tableName, columnGuide1));
		Util.validateObjectType(guideValue2, getColumnType(tableName, columnGuide2));
		
		if(!tableExists(tableName)) {
			throw new RuntimeException("Table " +  tableName + " does not exist.");
		}
		
		// Interface boolean.
		if (guideValue1 instanceof Boolean) guideValue1 = getIntFromBoolean((Boolean) guideValue1);
		if (guideValue2 instanceof Boolean) guideValue2 = getIntFromBoolean((Boolean) guideValue2);
		
		List<Object> result = new ArrayList<Object>();
		
		String query = "";
		try {
			query = "SELECT * FROM " + tableName + " WHERE " + columnGuide1 
					+ " = \"" + guideValue1 + "\" AND " + columnGuide2 + " = \"" + guideValue2 + "\"";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				if (getColumnType(tableName, columnToGet).equals(BOOLEAN)) {
					result.add(getBooleanFromInt((Integer)rs.getObject(columnToGet)));
				
				} else {
					result.add(rs.getObject(columnToGet));
				}
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("Problem executing query: " + query);
		}
			
		if (result.size() == 0) return null;
		return result;
	}
	
	
	/**
	 * Closes the database connection
	 */
	public void close() {
		con.close();
	}
	
	
	//----------------------------------- Helper Methods ---------------------------------------//
	
	
	// Given a type name and value in String form, returns the actual value Object. 
	private static Object getObject(String className, String value) {
		if ( className == "java.lang.String") { return value; } 
		else if ( className == "java.lang.Long") {
			return Long.parseLong(value);
		} else if ( className == "java.lang.Double") {
			return Double.parseDouble(value);
		} else if ( className == "java.lang.Boolean") {
			return Boolean.parseBoolean( value);
		} else if ( className == "java.lang.Integer") {
			return Integer.parseInt( value);
		} else {
			throw new RuntimeException("Row contains an invalid type");
		}
	}
	
	
	// Given a table name and a row, returns the SQL query to add it.
	private static String insertQuery(String tableName, Map<String, Object> row ) {
		String output = "";
		output += "INSERT INTO " + tableName + " (\n";
		int columns = getColumnCount(tableName);
		int count = 0;
		for(String key : row.keySet()) {
			count++;
			if ( count == columns ) { output += key + ")\n"; }
			else {
				output += key + ",";
			}
		}
		output += "VALUES (";
		Collection<Object> values = row.values();
		output = formatValues(values, output);
				
		return output;
	}
		
		
	// Formats the MySQL values query.
	private static String formatValues(Collection<Object> values, String output) {
		for (Object val : values) {
			String type = val.getClass().toString().substring(16).toLowerCase();
			if ( type.equals(INT) || type.equals(LONG) || type.equals(DOUBLE)) {
				output += val + ",";
			} else if (type.equals(STRING)) {
				output += "\"" + val + "\",";
			}
		}
		output = output.replaceAll(",$", "");
		output += ");";
		return output;
	}
	
	
	// Returns the row count of specified table.
	private static int getRowCount(String tableName) {
		int row = 0;
		if (tableExists(tableName)) {
			String count = "SELECT COUNT(*) FROM " + tableName + ";";
			ResultSet rs;
			try {
				rs = stmt.executeQuery(count);
				rs.next();
				row = rs.getInt("COUNT(*)"); 
			} catch (SQLException e) {
				System.out.println( "Unable to count rows in table");
				e.printStackTrace();
			}
		}
		return row;
	}
	
	
	// Returns the number of columns in the specified table.
	private static int getColumnCount(String tableName) {
		int column = 0;
		if (!tableExists(tableName)) {
			throw new RuntimeException("Table " + tableName + " does not exist.");
		}
		
		String query = "SELECT * FROM " + tableName + ";";
		try {
			Statement statement = getNewStatement();
			ResultSet rs = statement.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			column = rsmd.getColumnCount();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Problem executing query: " + query);
		}		
		return column;
	}
	
	
	/*
	 * Returns the corresponding table creation query
	 */
	private static String getCreationQuery(String tableName, Map<String, String> columnTypeAndName ) {
		if (columnTypeAndName == null) {
			throw new RuntimeException("Map is null.");
		} else if (columnTypeAndName.isEmpty()) {
			throw new RuntimeException("The table needs atleast 1 column.");
		}
		
		String output = "CREATE TABLE " + tableName + " (\n";
		
		// know we have at least one column
		int count = 0;
		for (String columnName : columnTypeAndName.keySet()) {
			String type = columnTypeAndName.get(columnName);
			
			count++;
			if (count == columnTypeAndName.size()) {
				// no comma
				output += columnName + " " + getDBTypeFromType(type) + "\n";
			} else {
				output += columnName + " " + getDBTypeFromType(type) + ",\n";
			}
		}
		output += ");";
		return output;
	}
	
	
	/*
	 * Checks if the map's keys contain valid data-types
	 */
	private static void validateColumnTypeAndName(String tableName, Map<String, String> columnTypeAndName) {
		if ( columnTypeAndName == null ) {
			throw new RuntimeException("columnTypeAndName is null.");
		} else if ( columnTypeAndName.size() == 0 ) {
			throw new RuntimeException("The table needs atleast 1 column.");
		}

	    for (String columnName : columnTypeAndName.keySet()) {
	    	String columnType = columnTypeAndName.get(columnName);
	        columnType = columnType.toLowerCase();
	        
	        Util.validateString(columnName);
	        Util.validateString(columnType);
	        
        	if (!(columnType.equals(DOUBLE) || columnType.equals(STRING)
        			|| columnType.equals(INT) || columnType.equals(BOOLEAN)
        			|| columnType.equals(LONG))) {
        		throw new RuntimeException(columnType + " is an invalid data type");
        	}
	    }
	}
	
	
	// Returns all columnNames and Types for the given table.
	private static Map<String, String> getColumnNameAndType(String tableName) {
		Map<String, String> map = new HashMap<String, String>();
		if (!tableExists( tableName ) ) {
			throw new RuntimeException("Table " + tableName + " does not exist.");
		}
			
		String query = "SELECT * FROM " + tableName;
		try {
			Statement statement = getNewStatement();
			ResultSet rs = statement.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			for(int i = 1; i <= rsmd.getColumnCount(); i++) {
				String columnName = rsmd.getColumnName(i);
				String type = rsmd.getColumnTypeName(i);
				map.put(columnName, type);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Problem executing query: " + query);
		}
		return map;
	}
	
	
	/*
	 * Given a table and column name it returns the column type.
	 * Throws a runtime exception if the table or columns do not exist.
	 */
	private static String getColumnType(String tableName, String columnName) {
		if(!tableExists(tableName)) {
			throw new RuntimeException("Table " +  tableName + " does not exist.");
		}
		
		String query = "SHOW COLUMNS FROM " + tableName + " WHERE Field = \"" + columnName + "\"";;
		String type = "";
		try {
			Statement statement = getNewStatement();
			ResultSet rs = statement.executeQuery(query);
			
			// Ensure there is content in the 
			if (!rs.next()) {
				throw new RuntimeException(columnName + " is not a valid column of " + tableName);
			}
			
			type = rs.getString(DB_TYPE);
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Problem executing query: " + query);
		}
		
		return getTypeFromDBType(type);
	}
	
	
	/*
	 * Given a DBType in String form, returns a regular type in String form.
	 * Throws an exception if the passed string is not a DB_TYPE.
	 */
	private static String getTypeFromDBType(String dbType) {
		Util.validateString(dbType);
		if (dbType.equalsIgnoreCase(DB_STRING)) return STRING;
		if (dbType.equalsIgnoreCase(DB_RAW_BOOLEAN)) return BOOLEAN;
		if (dbType.equalsIgnoreCase(DB_DOUBLE)) return DOUBLE;
		if (dbType.equalsIgnoreCase(DB_INT)) return INT;
		if (dbType.equalsIgnoreCase(DB_LONG)) return LONG;
		throw new RuntimeException("Passed type " + dbType + " is not a valid DBType.");
	}
	
	
	/*
	 * Given a regular type in String form, returns a database type in String form.
	 * Throws an exception if the passed string is not a regular type.
	 */
	private static String getDBTypeFromType(String type) {
		Util.validateString(type);
		if (type.equalsIgnoreCase(STRING)) return DB_STRING;
		if (type.equalsIgnoreCase(BOOLEAN)) return DB_BOOLEAN;
		if (type.equalsIgnoreCase(DOUBLE)) return DB_DOUBLE;
		if (type.equalsIgnoreCase(INT)) return DB_INT;
		if (type.equalsIgnoreCase(LONG)) return DB_LONG;
		throw new RuntimeException("Passed type " + type + " is not a valid type.");
	}
	
	
	// Given an integer value returns its equivalent boolean form.
	private static boolean getBooleanFromInt(int value) {
		return value == 1;
	}
	
	
	// Given a boolean value returns its equivalent int form.
	private static int getIntFromBoolean(boolean value) {
		if (value) return 1;
		return 0;
	}
	
	
	/*
	 * Given a map that may have boolean values in integer form, converts those values
	 * into actual booleans so that it is compatible with the client. To be used
	 * when returning a map to the client.
	 */
	private static void normalizeObjectMap(String tableName, Map<String, Object> map) {
		Util.validateString(tableName);
		
		// Get the name and type of the columns in the table.
		Map<String, String> nameAndType = getColumnNameAndType(tableName);
		for (String name : nameAndType.keySet()) {
			if (nameAndType.get(name).equals(DB_BOOLEAN)) {
				// If the value is flagged as boolean, make sure it's an integer and transform it.
				Util.validateObjectType(map.get(name), INT);
				boolean value = getBooleanFromInt((Integer)map.get(name));
				map.put(name, value);
			}
		}
	}
	
	
	/*
	 * Given a map that may have boolean values, it changes those values to integers so that
	 * the map is database compatible. To be used when receiving a map from the outside.
	 */
	private static void normalizeObjectMapForDB(Map<String, Object> map) {
		for (String name : map.keySet()) {
			if (Util.getType(map.get(name)).equals(BOOLEAN)) {

				// If the value is boolean, make it an integer so that it is DB compatible.
				int value = getIntFromBoolean((Boolean)map.get(name));
				map.put(name, value);
			}
		}
	}
	
	
	// Establishes a new connection to the database and returns a new statement object.
	private static Statement getNewStatement() {
		Connection connection = con.getConnection();
		Statement statement = null;
		
		try {
			statement = connection.createStatement();
			statement.executeQuery("USE " + MyDBInfo.MYSQL_DATABASE_NAME);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not initialize statement.");
		}
		return statement;
	}
	
	
	// To be used for testing purposes only.
	public static void main( String [] args ) {
		new Database();
		removeTable(ACCOUNTS);
		removeTable(RESPONSE);
		removeTable(FILL_BLANK);
		removeTable(MULTI_RESPONSE);
		removeTable(MULTIPLE_CHOICE);
		removeTable(MATCHING);
		removeTable(PICTURE);
		removeTable(QUIZZES);
		removeTable(HISTORY);
		removeTable(ACHIEVEMENTS);
		removeTable(MESSAGES);
		removeTable(FRIENDS);
		
	}
}
