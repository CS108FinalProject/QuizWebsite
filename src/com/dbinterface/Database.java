package com.dbinterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	private Statement stmt;
	
	/**
	 * Constructor: establishes a connection with 
	 * a database
	 */
	public Database() {
		Connector con = new Connector();
		this.stmt = con.getStatement();
	}
	
	/**
	 * Removes table.
	 * User can check status of table by calling
	 * showTables()
	 * @param tablename
	 */
	public void removeTable(String tableName) {
		if ( tableName == null ) {
			throw new RuntimeException("Table name cannot be null");
		}
		String query = "DROP TABLE " + tableName + ";";
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
	 * 
	 */
	public void createTable(String tableName, Map<String, String> columnTypeAndName ) throws RuntimeException {
		
		//***************ERROR CHECKING*****************//
		if ( tableName == null ) {
			throw new RuntimeException("tableName is null");
		} else if ( tableName.equals("") ) {
			throw new RuntimeException("tableName is empty");
		}
		
 		this.tableName = tableName;
		
 			// Ensure that columnTypes are valid
 		String SQLQuery = "";
 		if ( columnTypeAndNameValid( columnTypeAndName ) ) {
 			SQLQuery = getCreationQuery( tableName, columnTypeAndName );
			this.columnTypeAndName = columnTypeAndName;
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
	private String getCreationQuery(String tableName, Map<String, String> columnTypeAndName ) {
		if ( columnTypeAndName == null ) {
			throw new RuntimeException("columnTypeAndName is null.");
		} else if ( columnTypeAndName.size() == 0 ) {
			throw new RuntimeException("The table needs atleast 1 column.");
		}
		
		
		String output = "CREATE TABLE " + tableName + " (\n";
		
		// know we have atleast one column
		int count = 0;
		for (String key : columnTypeAndName.keySet()) {
			if ( columnTypeAndName.get(key).equals( "String") ) {
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
			} else if ( columnTypeAndName.get(key).equals( "int" ) || key.equals( "long")) {
				count++;
				if ( count == columnTypeAndName.size() ) {
					// no comma
					output += key + " BIGINT\n";
				} else {
					output += key + " BIGINT,\n";
				}
			}  
		}
		output += ");";
		
		return output;
	}
	
	
	/*
	 * Checks to see if the map's keys contain valid datatypes
	 */
	private boolean columnTypeAndNameValid( Map<String, String> columnTypeAndName) {
		if ( columnTypeAndName == null ) {
			throw new RuntimeException("columnTypeAndName is null.");
		} else if ( columnTypeAndName.size() == 0 ) {
			throw new RuntimeException("The table needs atleast 1 column.");
		}

		Collection<String> s = columnTypeAndName.values();
		for (String key : s) {
			if ( key.equals( "String") ) {
				// Good, do nothing!
			} else if ( key.equals( "double") ) {
			} else if ( key.equals( "int") ) {
			} else if ( key.equals( "long") ) {
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
	public void addRow(List<Object> rowContent) {
		if ( rowContent.size() != columnTypeAndName.size() ) {
			throw new RuntimeException("Need as many items as there are columns");
		}
		
		Set<String> ks = columnTypeAndName.keySet();
		Object [] keys = ks.toArray();
		
		for(int i = 0; i < rowContent.size(); i++) {
			Class c = rowContent.get(i).getClass();
			if ( keys[i] instanceof Double ) {
				
			}
		}
	}
}
