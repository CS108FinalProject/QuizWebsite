package com.dbinterface;


import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DatabaseTest {
	Database db;
	
	@Before
	public void setUp() {
		db = new Database();
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
		columnTypeAndName.put("university", "string" );
		columnTypeAndName.put("price" , "double");
		db.createTable("", columnTypeAndName);
	}
	
	/*
	 * Tests RE on invalid columnType, with valid tableName
	 */
	@Test(expected = RuntimeException.class) 
	public void test3() {
		Map<String, String> columnTypeAndName = new HashMap<String, String>();
		columnTypeAndName.put("university", "string" );
		columnTypeAndName.put("price" , "double");
		db.createTable("yourTable", columnTypeAndName);
		
	}
	
	/*
	 * Test if table exists. Note database may have changed since
	 * last use so, some of these tests might fail. So it's important
	 * to manually check the state of the database while testing.
	 */
	@Test 
	public void test4() {
		assertEquals( true,Database.tableExists("yesterday"));
		assertEquals( false, Database.tableExists("tomorrow"));
		assertEquals( true,Database.tableExists("products"));
		assertEquals( true, Database.tableExists("Accounts"));
	}
	
	/*
	 * Simple Tests for getTable
	 */
	@Test
	public void getTableTest() {
		assertEquals(null, db.getTable(null));
	}
	
	/*
	 * Simple tests for addRow
	 */
	@Test
	public void addRowTest() {
		Map<String, Object> metro = new HashMap<String, Object>();
		metro.put("metropolis", "Addis Ababa");
		metro.put("continent", "Africa");
		metro.put("population", (long) 3000000);
		Database.addRow("metropolises", metro);
	}
	
}
