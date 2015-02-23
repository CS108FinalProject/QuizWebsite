package com.dbinterface;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class DatabaseTest {

	/*
	 * Tests RuntimeException on null values
	 */
	@Test(expected = RuntimeException.class)
	public void test1() {
		Database db = new Database(null, null);
	}
	
	/*
	 * Tests RuntimeException on empty table name but 
	 * valid columnTypeAndName
	 */
	@Test(expected = RuntimeException.class) 
	public void test2() {
		Map<String, String> columnTypeAndName = new HashMap<String, String>();
		columnTypeAndName.put("String", "university");
		columnTypeAndName.put("double", "23452");
		
		Database db = new Database("", columnTypeAndName);
	}
	
	/*
	 * Tests RE on invalid columnType, with valid tableName
	 */
	@Test(expected = RuntimeException.class) 
	public void test3() {
		Map<String, String> columnTypeAndName = new HashMap<String, String>();
		columnTypeAndName.put("String", "university");
		columnTypeAndName.put("Double", "23452");
		
		Database db = new Database("universities", columnTypeAndName);
	}
	
	
	

}
