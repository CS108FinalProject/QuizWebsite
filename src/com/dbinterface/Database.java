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

import com.util.Util;


/**
 * This is a program that will provide an interface
 * for interacting with a remote mySql database
 * @author eliezer 
 */
public class Database {
	private static Statement stmt;
	private Connector con;
	
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
 		if ( columnTypeAndNameValid( columnTypeAndName ) ) {
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
		} else if ( columnTypeAndName.size() == 0 ) {
			throw new RuntimeException("The table needs atleast 1 column.");
		}
		
		
		String output = "CREATE TABLE " + tableName + " (\n";
		
		// know we have atleast one column
		int count = 0;
		for (String key : columnTypeAndName.keySet()) {
			if ( columnTypeAndName.get(key).equals( "string") ) {
				count++;
				if ( count == columnTypeAndName.size() ) {
					// no comma
					output += key + " CHAR(64)\n";
				} else {
					output += key + " CHAR(64),\n";
				}
			} else if ( columnTypeAndName.get(key).equals( "double") ) {
				count++;
				if ( count == columnTypeAndName.size() ) {
					// no comma
					output += key + " DOUBLE(50, 5)\n";
				} else {
					output += key + " DOUBLE(50, 5),\n";
				}
			} else if ( columnTypeAndName.get(key).equals( "long") ||
					columnTypeAndName.get(key).equals( "integer")) {
				count++;
				if ( count == columnTypeAndName.size() ) {
					// no comma
					output += key + " BIGINT\n";
				} else {
					output += key + " BIGINT,\n";
				}
			}  else if ( columnTypeAndName.get(key).equals( "boolean")) {
				count++;
				if ( count == columnTypeAndName.size() ) {
					// no comma
					output += key + " TINYINT(1)\n";
				} else {
					output += key + " TINYINT(1),\n";
				}
			}  
		}
		output += ");";
		
		return output;
	}
	
	
	/*
	 * Checks to see if the map's keys contain valid datatypes
	 */
	private static boolean columnTypeAndNameValid( Map<String, String> columnTypeAndName) {
		if ( columnTypeAndName == null ) {
			throw new RuntimeException("columnTypeAndName is null.");
		} else if ( columnTypeAndName.size() == 0 ) {
			throw new RuntimeException("The table needs atleast 1 column.");
		}

		Set<String> s = columnTypeAndName.keySet();
		for (String key : s) {
			Util.validateObject(key, columnTypeAndName.get(key));
		throw new RuntimeException( key.toString() +  " is an invalid datatype" );
			
		}
		return true;
	}
	
	// TO-DO
	/**
	 * Should add the passed row to the specified table. Check correct type for each Object.
	 * Use the Util.validateObject method for this.
	 * @param tableName
	 * @param row key = column name, value = table value
	 * throw exception on failure (type missmatch, etc...)
	 */
	public static void addRow(String tableName, Map<String, Object> row) {
		if ( tableName == null ) {
			throw new RuntimeException("Table is null.");
		} else if ( tableExists( tableName) ) {
			
		}
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
			ResultSet rs = stmt.executeQuery(query);
			rs.beforeFirst();
			
			while ( rs.next() ) {
				tables.add( rs.getString("Tables_in_" + MyDBInfo.MYSQL_DATABASE_NAME));
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
		
		// Get table count first
		String count = "SELECT COUNT(*) FROM " + tableName + ";";
		try {
			ResultSet rs = stmt.executeQuery(count);
			rs.next();
			int c = rs.getInt("COUNT(*)"); 
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			if ( c == 0 ) { return list; } // empty list
			else {
				ResultSet rs2 = stmt.executeQuery(query);
				ResultSetMetaData rsmd = rs2.getMetaData();
				int columnCount = rsmd.getColumnCount();
				rs2.beforeFirst();
				while ( rs2.next() ) {
					Map<String, Object> entry = entry = new HashMap<String, Object>();
					for(int i = 1; i <= columnCount; i++) {
						String className = rsmd.getColumnClassName(i);
						String columnName = rsmd.getColumnName(i);
						String value = rs2.getString(columnName);
						Object valObj = getObject(className, value);
						entry.put(columnName, valObj);
					}
					list.add( entry );
				}
				return list;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main( String [] args ) {
		Database db = new Database();
		List<Map<String, Object>> list = Database.getTable("universities");
		System.out.println( list.toString() );
	}
	//Helper
	public static Object getObject(String className, String value) {
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
	// TO-DO
	public static int removeRows(String tableName, String columnGuide1, String guideValue1, 
			String columnGuide2, String guideValue2) {
		return 0;
	}
	
	
	/**
	 * Same as previous but with only 1 specifier.
	 * @param tableName
	 * @param columnGuide
	 * @param guideValue
	 * @return
	 */
	// TO-DO
	public static int removeRows(String tableName, String columnGuide, String guideValue) {
		return 0;
	}
	
	
	/**
	 * Same as previous but this looks for and deleted the row that matches all parameters in the map.
	 * @param row
	 */
	// TO-DO
	public static void removeRow(Map<String, Object> row) {}
	
	
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
	// TO-DO
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
	// TO-DO
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
	// TO-DO
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
	// TO-DO
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
	// TO-DO
	public static List<Object> getValues(String tableName, String columnGuide1, String guideValue1, String columnGuide2, 
			String guideValue2, String columnToGet) {
		return null;
	}
	
	/**
	 * Closes the database connection
	 */
	public void close() {
		con.close();
	}

}
