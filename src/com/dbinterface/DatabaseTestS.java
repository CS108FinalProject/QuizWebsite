package com.dbinterface;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.util.Constants;

public class DatabaseTestS implements Constants {

	private static final String TEST_TABLE = "testTable";
	private static final String NAME = "name";
	private static final String IS_CODING = "isCoding";
	private static final String NUMBERS = "numbers";
	private static final String PRICE = "price";
	private static final String HIGH_NUMBER = "highNumber";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		new Database();
	}

	@Before
	public void setUp() throws Exception {
		// Create a table
		if (!Database.tableExists(TEST_TABLE)) {
			Map<String, String> columns = new LinkedHashMap<String, String>();
			columns.put(NAME, STRING);
			columns.put(IS_CODING, BOOLEAN);
			columns.put(NUMBERS, INT);
			columns.put(PRICE, DOUBLE);
			columns.put(HIGH_NUMBER, LONG);
			Database.createTable(TEST_TABLE, columns);
		}
		
		
		// Add row
		Map<String, Object> row = new HashMap<String, Object>();
		row.put(NAME, "sam");
		row.put(IS_CODING, true);
		row.put(NUMBERS, 2);
		row.put(PRICE, 26.78);
		row.put(HIGH_NUMBER, 123456789);
		Database.addRow(TEST_TABLE, row);
	}

	@After
	public void tearDown() throws Exception {
		Database.removeTable(TEST_TABLE);
	}

	@Test
	public void testGetTable() {
		List<Map<String, Object>> table = Database.getTable(TEST_TABLE);
		assertEquals(1, table.size());
		
		Map<String, Object> row = table.get(0);
		assertEquals("sam", row.get(NAME));
		assertEquals(true, row.get(IS_CODING));
		assertEquals(2, row.get(NUMBERS));
		assertEquals(26.78, row.get(PRICE));
		assertEquals(123456789, row.get(HIGH_NUMBER));
	}

	@Test
	public void testRemoveRowsStringStringObjectStringObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveRowsStringStringObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveRowsStringMapOfStringObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRows() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetValuesStringStringObjectStringObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetValuesStringStringObjectStringObjectStringObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetValuesStringStringObjectString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetValuesStringStringObjectStringObjectString() {
		fail("Not yet implemented");
	}

}
