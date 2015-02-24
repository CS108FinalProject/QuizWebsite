package com.dbinterface;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class DatabaseTest {
	Database db;
	
	@Before
	public void setUp() {
		db = new Database();
	}
	
	/*
	 * Checks to see if showTables prints the tables 
	 * in the database
	 */
	@Test
	public void test0() {
		System.out.println( db.displayTables() );
	}
	/*
	 * Tests RuntimeException on null values
	 */
	@Test(expected = RuntimeException.class)
	public void test1() {
		db.createTable(null, null);
	}
	
	/*
	 * Tests RuntimeException on empty table name but 
	 * valid columnTypeAndName
	 */
	@Test(expected = RuntimeException.class) 
	public void test2() {
		Map<String, String> columnTypeAndName = new HashMap<String, String>();
		columnTypeAndName.put("university", "String" );
		columnTypeAndName.put("price" , "double");
		db.createTable("", columnTypeAndName);
	}
	
	/*
	 * Tests RE on invalid columnType, with valid tableName
	 */
	@Test(expected = RuntimeException.class) 
	public void test3() {
		Map<String, String> columnTypeAndName = new HashMap<String, String>();
		columnTypeAndName.put("university", "String" );
		columnTypeAndName.put("price" , "Double");
		db.createTable("yourTable", columnTypeAndName);
		
	}
	
	/*
	 * Check remove table method
	 */
	@Test
	public void test4() {
		db.removeTable("today");
		System.out.println( db.displayTables() );
	}
	
	
	

}
