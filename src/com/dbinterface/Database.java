package com.dbinterface;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.sql.Connection;
import com.accounts.AccountManager;
import com.util.Constants;
import com.util.Util;


/**
 * This is a program that will provide an interface
 * for interacting with a remote mySql database.
 * 
 * @author eliezer && Sam
 */
public class Database implements Constants {
	private static Statement stmt;
	private static Connector con;
	
	/**
	 * Establishes a connection with 
	 * a database
	 */
	public Database() {
		this.con = new Connector();
		stmt = con.getStatement();
	}
	
	/**
	 * Removes table.
	 * User can check status of table by calling
	 * showTables()
	 * @param tableName name of the table in the database
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
		}
	}
	/**
	 * Show Tables in database specified by MyDBInfo file.
	 * Should be used for error checking and validation.
	 * @return String list of tables in database
	 */
	public String displayTables() {
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
		
 		// Ensure that columnTypes are valid
 		String SQLQuery = "";
 		if ( columnTypeAndNameValid(tableName, columnTypeAndName ) ) {
 			SQLQuery = getCreationQuery( tableName, columnTypeAndName );
 		}
 		
 		// make live query to check current Tables
 		Set<String> tables = new HashSet<String>();
 		
 		try {
			String query = "SHOW TABLES;";
			ResultSet rs = stmt.executeQuery(query);
			rs.beforeFirst();
			while ( rs.next() ) {
				String tab = rs.getString("Tables_in_" + MyDBInfo.MYSQL_DATABASE_NAME);
				tables.add(tab);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
 		
 			// checks if the table already exists in the database
 		if ( tables.contains( tableName ) ) {
 			throw new RuntimeException("The table: \"" + tableName + "\" already exists in the database.");
 		}
 		
 		//*****************TABLE CREATION******************//
 		try {
			stmt.executeUpdate(SQLQuery);
		} catch (SQLException e) {
			System.out.println("Failed to execute query");
			e.printStackTrace();
		}
	}
	
	/*
	 * Gets the corresponding table creation query
	 */
	private static String getCreationQuery(String tableName, Map<String, String> columnTypeAndName ) {
		if ( columnTypeAndName == null ) {
			throw new RuntimeException("columnTypeAndName is null.");
		} else if ( columnTypeAndName.isEmpty() ) {
			throw new RuntimeException("The table needs atleast 1 column.");
		}
		
		
		String output = "CREATE TABLE " + tableName + " (\n";
		
		// know we have atleast one column
		int count = 0;
		for (String key : columnTypeAndName.keySet()) {
			if ( columnTypeAndName.get(key).equalsIgnoreCase( "string") ) {
				count++;
				if ( count == columnTypeAndName.size() ) {
					// no comma
					output += key + " " + DB_STRING + "\n";
				} else {
					output += key + " " + DB_STRING + ",\n";
				}
			} else if ( columnTypeAndName.get(key).equalsIgnoreCase( "double") ) {
				count++;
				if ( count == columnTypeAndName.size() ) {
					// no comma
					output += key + " " + DB_DOUBLE + "\n";
				} else {
					output += key +  " " + DB_DOUBLE + ",\n";
				}
			} else if ( columnTypeAndName.get(key).equalsIgnoreCase( "long") ||
					columnTypeAndName.get(key).equalsIgnoreCase( "integer")) {
				count++;
				if ( count == columnTypeAndName.size() ) {
					// no comma
					output += key +  " " + DB_INT + "\n";
				} else {
					output += key + " " + DB_INT + ",\n";
				}
			}  else if ( columnTypeAndName.get(key).equals( "boolean")) {
				count++;
				if ( count == columnTypeAndName.size() ) {
					// no comma
					output += key + " " + DB_BOOLEAN + "\n";
				} else {
					output += key + " " + DB_BOOLEAN + ",\n";
				}
			}  
		}
		output += ");";
		
		return output;
	}
	
	
	/*
	 * Checks to see if the map's keys contain valid datatypes
	 */
	private static boolean columnTypeAndNameValid( String tableName, Map<String, String> columnTypeAndName) {
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
	        
        	if ( columnType.equals("CHAR") || columnType.equals("DOUBLE") || 
        			columnType.equals("TINYINT") ) 
        	{
        		// okay 
        	} else if ( columnType.equals("string") ) {
        		// okay
        	} else if (columnType.equals("boolean") || columnType.equals("long") ) {
        		// okay
        	} else if ( columnType.equals("integer")  ) {
        		// okay
        	} else {
        		throw new RuntimeException(columnType + " is an invalid data type");
        	}
	    }
		return true;
	}
	
	
	//Helper
	// get columnName and Type for given table
	private static Map<String, String> getColumnNameAndType(String tableName) {
		Map<String, String> map = new HashMap<String, String>();
		if ( tableExists( tableName ) ) {
			String query = "SELECT * FROM " + tableName;
			ResultSet rs;
			try {
				Connection cnn = con.getConnection();
				Statement stmt2 = cnn.createStatement();
				stmt2.executeQuery("USE " + MyDBInfo.MYSQL_DATABASE_NAME);
				rs = stmt2.executeQuery(query);
				ResultSetMetaData rsmd = rs.getMetaData();
				for(int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnName = rsmd.getColumnName(i);
					String type = rsmd.getColumnTypeName(i);
					if ( type == DB_STRING.substring(0, 4) ) {
						type = "string";
					} else if ( type == DB_BOOLEAN ) {
						type = "boolean";
					} else if ( type == DB_DOUBLE ) {
						type = "double";
					}
					map.put(columnName, type);
				}
				stmt2.close();
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} finally {
				
			}
		}
		return map;
		
	}
	
	// TO-DO
	/**
	 * Should add the passed row to the specified table. Check correct type for each Object.
	 * @param tableName
	 * @param row key = column name, value = table value
	 * throw exception on failure (type missmatch, etc...)
	 */
	public static void addRow(String tableName, Map<String, Object> row) {
		if ( tableName == null ) {
			throw new RuntimeException("Table is null.");
		} else if ( !tableExists(tableName ) ) {
			throw new RuntimeException(tableName + " is not a valid table");
		} else {
			if ( row == null ) {
				throw new RuntimeException("Null value passed for row");
			}
			
			int columnCount = getColumnCount(tableName);
			// each map entry must correspond to a column
			if ( row.size() != columnCount ) {
				throw new RuntimeException("Wrong number of entries in row");
			}
			
			for(String key : row.keySet()) {
				Object value = row.get(key);
				String type = value.getClass().toString().substring(16).toLowerCase();
				
				// Turn boolean to integer for DB compatibility. (added by Sam)
		        if (type.equals(BOOLEAN)) {
		        	value = getIntFromBoolean((Boolean) value);
		        	type = value.getClass().toString().substring(16).toLowerCase();
		        }
		        //--------------------------------------------------------------
		        
		        Map<String, String> types = getColumnNameAndType(tableName);
		        if ( !types.containsKey(key) ) {
		        	throw new RuntimeException("Row does not contain this column name");
		        } else {
		        	if ( !types.get(key).equals(type) ) {
		        		throw new RuntimeException("Type Mismatch" + "\n\nExpected:" 
		        		+ types.get(key) + " but received: " + type);
		        	}
 		        }
		        
			}
		    
		    // Transform map for DB compatibility. (added by Sam)
		    normalizeObjectMapForDB(row);
		    //---------------------------------------------------
		    
		    
		    // if we get here we know our row content is good
		    // check if the table has not been deleted since our last processing
		    if ( tableExists(tableName) ) {
		    	try {
					stmt.executeUpdate(insertQuery(tableName, row));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}
	}
	
	// Private helper
	// insert query
	private static String insertQuery(String tableName, Map<String, Object> row ) {
		String output = "";
		output += "INSERT INTO " + tableName + " (\n";
		Set<String> keys = row.keySet();
		int columns = getColumnCount(tableName);
		int count = 0;
		for(String key : keys) {
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
	
	// Helper | format's the 
	// values query
	private static String formatValues(Collection<Object> values, String output) {
		for (Object val : values) {
			String type = val.getClass().toString().substring(16).toLowerCase();
			if ( type.equals("integer") || type.equals("long") ) {
				output += val + ",";
			} else if ( type.equals("string") ) {
				output += "\"" + val + "\",";
			}
		}
		output = output.replaceAll(",$", "");
		output += ");";
		return output;
	}
	
	// Private method that gets
	// row count of table
	private static int getRowCount(String tableName) {
		int row = 0;
		if ( tableExists( tableName ) ) {
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
			return row;
		}
		return row;
	}
	
	// Private method that obtains
	// the number of columns in a table
	private static int getColumnCount(String tableName) {
		int column = 0;
		if ( tableExists( tableName ) ) {
			String count = "SELECT * FROM " + tableName + ";";
			ResultSet rs;
			try {
				rs = stmt.executeQuery(count);
				ResultSetMetaData rsmd = rs.getMetaData();
				column = rsmd.getColumnCount();
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}		
			return column;
		}
		return column;
	}
	
	/**
	 * Self explanatory.
	 * @param tableName
	 * @return
	 */
	public static boolean tableExists(String tableName) {
		Set<String> tables = new HashSet<String>();
		String query = String.format("SHOW TABLES;");
		try {
			Connection cnn = con.getConnection();
			Statement stmt2 = cnn.createStatement();
			stmt2.executeQuery("USE " + MyDBInfo.MYSQL_DATABASE_NAME);
			ResultSet rs = stmt2.executeQuery(query);
			rs.beforeFirst();
			
			while ( rs.next() ) {
				tables.add( rs.getString("Tables_in_" + MyDBInfo.MYSQL_DATABASE_NAME));
			}
			rs.close();
		} catch (SQLException e) {

			throw new RuntimeException("Problem with query" + query);

		}
		return tables.contains( tableName );
	}
	
	
	/**
	 * Returns the whole table. Each row is a Map.
	 * Returns null if there is no such table or an empty list if the table is empty.
	 * @param tableName name of the table in database
	 * @return list of maps containing the row information
	 */
	public static List<Map<String, Object>> getTable(String tableName) {
		if ( !tableExists(tableName) ) return null;
		String query = "SELECT * FROM " + tableName + ";";
		
		try {
			int c = getRowCount(tableName);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			if ( c == 0 ) { return list; } // empty list
			else {
				ResultSet rs = stmt.executeQuery(query);
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				rs.beforeFirst();
				while ( rs.next() ) {
					Map<String, Object> entry = entry = new HashMap<String, Object>();
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
				
//				 //fix boolean values
//				for(int i = 0; i < list.size(); i++) {
//					normalizeObjectMap(tableName, list.get(i));
//				}
				return list;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main( String [] args ) {
		new Database();
		
//		Map<String, Object> row = new HashMap<String, Object>();
//		row.put(USERNAME, "Eliezer");
//		row.put(IS_ADMIN, true);
//		row.put(FRIEND, false);
//		addRow("TestBoolean", row);
		
//		// another row
//		Map<String, String> stuff = new HashMap<String, String>();
//		stuff.put("isHappy", BOOLEAN);
//		stuff.put("person", STRING);
//		Database.createTable("happiness", stuff);
		
		// test now
		List<Map<String, Object>> map = Database.getTable("TestBoolean");	
		System.out.println( map.toString() );
		
		
	}
	
	//Helper
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
	public static int removeRows(String tableName, String columnGuide1, Object guideValue1, 
			String columnGuide2, Object guideValue2) {
		
		Util.validateString(tableName);
		Util.validateString(columnGuide1);
		Util.validateString(columnGuide2);
		Util.validateObject(guideValue1, getColumnType(tableName, columnGuide1));
		Util.validateObject(guideValue2, getColumnType(tableName, columnGuide2));
		
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
		Util.validateObject(guideValue, getColumnType(tableName, columnGuide));
		
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
	 * Should return a list of rows (each a Map<String, Object>) where columnName is equals to the value.
	 * 
	 * Example: getRows("Accounts", "userName", "Eliezer") returns the List of rows (each a Map) where the
	 * value under the userName column is Eliezer.
	 * Should return null if no such row is found.
	 * @param tableName 
	 * @param columnName
	 * @param value
	 * @return
	 */
	public static List<Map<String, Object>> getRows(String tableName, String columnName, Object value) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		
		Util.validateString(tableName);
		Util.validateString(columnName);
		
		if (!tableExists(tableName)) { 
			throw new RuntimeException(tableName + " does not exist in the database.");
		}
		
		// validate object type
		String type = getColumnType(tableName, columnName);
		Util.validateObject(value, type);
		
		Map<String, String> m = getColumnNameAndType(tableName);
		if (!m.containsKey(columnName) ) {
			throw new RuntimeException( columnName + " is not a valid column in " 
					+ tableName );
		}
		
		String columnType = m.get(columnName);
		String passedObjType =  value.getClass().toString().substring(16).toLowerCase();
		if ( !columnType.equals(passedObjType) ) {
			throw new RuntimeException( "Expected: " + columnType + " but received: "
					+ passedObjType);
		}
		
		// Know passed type is a valid at this point; implement sql query
		String query = "SELECT * FROM " + tableName + " WHERE " + 
						columnName + " = " + "\"" + value + "\"" + ";";
		
		try {
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next() ) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int numColumns = getColumnCount(tableName);
				
				for(int i = 1; i <= numColumns; i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					Object obj = rs.getObject(i);
					String column = rsmd.getColumnName(i);
					map.put(column, obj);
					list.add(map);
				}
			}
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * Modifies the passed value in the specified table location.
	 * @param tableName 
	 * @param columnGuide name of the column that will be used to determine the row that should be modified  
	 * @param guideValue all the rows that have this in value in the "columnGuide column" should be modified
	 * @param columnToBeSet once the rows has been located, the columnToBeSet is the name of the column that should be 
	 * 			modified in these rows
	 * @param columnToBeSetValue the actual value that is being added (check for type correctness)
	 * @return amount of values that were modified
	 * 
	 * Example: setValue("Accounts", "userName", "Eliezer", "password", "thisIsMyNewPassword");
	 * look for all the rows that have value = "Eliezer" under the "userName" column and modify their "password" column
	 * to be "thisIsMyNewPassword" 
	 */
	public static int setValues(String tableName, String columnGuide, Object guideValue,
			String columnToBeSet, Object columnToBeSetValue) {
		
		Util.validateString(tableName);
		Util.validateString(columnGuide);
		Util.validateString(columnToBeSet);
		Util.validateObject(guideValue, getColumnType(tableName, columnGuide));
		Util.validateObject(columnToBeSetValue, getColumnType(tableName, columnToBeSet));
		
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
	 * @param columnGuide1 name of the first column that will be used to determine the row that should be modified  
	 * @param guideValue1 all the rows that have this in value in the "columnGuide1 column" should be modified
	 * @param columnGuide2 name of the second column that will be used to determine the row that should be modified  
	 * @param guideValue2 all the rows that have this in value in the "columnGuide2 column" should be modified
	 * @param columnToBeSet once the rows has been located, the columnToBeSet is the name of the column that should be 
	 * 			modified in these rows
	 * @param columnToBeSetValue the actual value that is being added (check for type correctness)
	 * @return amount of values that were modified
	 * 
	 * Example: setValue("Accounts", "userName", "Eliezer", "password", "thisIsMyNewPassword");
	 * look for all the rows that have value = "Eliezer" under the "userName" column and modify their "password" column
	 * to be "thisIsMyNewPassword" 
	 */
	public static int setValues(String tableName, String columnGuide1, Object guideValue1, String columnGuide2,
			Object guideValue2, String columnToBeSet, Object columnToBeSetValue) {
		
		Util.validateString(tableName);
		Util.validateString(columnGuide1);
		Util.validateString(columnGuide2);
		Util.validateString(columnToBeSet);
		Util.validateObject(guideValue1, getColumnType(tableName, columnGuide1));
		Util.validateObject(guideValue2, getColumnType(tableName, columnGuide2));
		Util.validateObject(columnToBeSetValue, getColumnType(tableName, columnToBeSet));
		
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
	 * In every row in the "Friends" table where under the "userName" column the value = "Eliezer" and under the
	 * "status" column the value = "friends" take the value under the "friend" column and add it to a list.
	 * Return the list or null if nothing was found.
	 * 
	 * @param tableName the requested table
	 * @param columnGuide1 the first specified column
	 * @param guideValue1 the first specified value
	 * @param columnGuide2 the second specified column
	 * @param guideValue2 the second specified value
	 * @param columnToGet the column where the returned values will be in.
	 * @return a list of objects matching the specification.
	 */
	public static List<Object> getValues(String tableName, String columnGuide, Object guideValue, String columnToGet) {
		Util.validateString(tableName);
		Util.validateString(columnGuide);
		Util.validateString(columnToGet);
		Util.validateObject(guideValue, getColumnType(tableName, columnGuide));
		
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
			//rs.beforeFirst();
			while (rs.next()) {
				if (getColumnType(tableName, columnToGet).equals(BOOLEAN)) {
					result.add(getBooleanFromInt((Integer)rs.getObject(columnToGet)));
					System.out.println(rs.isClosed() + "3");
				} else {
					System.out.println(rs.isClosed() + "4");
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
	 * In every row in the "Friends" table where under the "userName" column the value = "Eliezer" and under the
	 * "status" column the value = "friends" take the value under the "friend" column and add it to a list.
	 * Return the list or null if nothing was found.
	 * 
	 * @param tableName the requested table
	 * @param columnGuide1 the first specified column
	 * @param guideValue1 the first specified value
	 * @param columnGuide2 the second specified column
	 * @param guideValue2 the second specified value
	 * @param columnToGet the column where the returned values will be in.
	 * @return a list of objects matching the specification.
	 */
	public static List<Object> getValues(String tableName, String columnGuide1, Object guideValue1, String columnGuide2, 
			Object guideValue2, String columnToGet) {
		
		Util.validateString(tableName);
		Util.validateString(columnGuide1);
		Util.validateString(columnGuide2);
		Util.validateString(columnToGet);
		Util.validateObject(guideValue1, getColumnType(tableName, columnGuide1));
		Util.validateObject(guideValue2, getColumnType(tableName, columnGuide2));
		
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
	
	
	/*
	 * Given a table and column name it returns the column type.
	 * Throws a runtime exception if the table or columns do not exist.
	 */
	private static String getColumnType(String tableName, String columnName) {
		if(!tableExists(tableName)) {
			throw new RuntimeException("Table " +  tableName + " does not exist.");
		}
		
		String query = "";
		String type = "";
		try {
			Connection cnn = con.getConnection();
			Statement stmt2 = cnn.createStatement();
			stmt2.executeQuery("USE " + MyDBInfo.MYSQL_DATABASE_NAME);
			query = "SHOW COLUMNS FROM " + tableName + " WHERE Field = \"" + columnName + "\"";
			ResultSet rs = stmt2.executeQuery(query);
			rs.next();
			type = rs.getString(DB_TYPE);
			rs.close();
			
		} catch (SQLException e) {
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
		if (dbType.equalsIgnoreCase(DB_BOOLEAN)) return BOOLEAN;
		if (dbType.equalsIgnoreCase(DB_DOUBLE)) return DOUBLE;
		if (dbType.equalsIgnoreCase(DB_INT)) return INT;
		if (dbType.equalsIgnoreCase(DB_LONG)) return LONG;
		throw new RuntimeException("Passed type " + dbType + " is not a valid DBType.");
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
			if (nameAndType.get(name).equals(BOOLEAN)) {
				// If the value is flagged as boolean, make sure it's an integer and transform it.
				Util.validateObject(map.get(name), INT);
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
	
	
	
}
