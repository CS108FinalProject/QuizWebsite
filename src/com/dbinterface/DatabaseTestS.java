package com.dbinterface;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
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
		row.put(HIGH_NUMBER, 123456789l);
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
		assertEquals(123456789l, row.get(HIGH_NUMBER));
	}

	@Test
	public void testRemoveRowsStringStringObjectStringObject() {
		int removed = Database.removeRows(TEST_TABLE, NAME, "sam", IS_CODING, true);
		assertEquals(1, removed);
		
		List<Map<String, Object>> table = Database.getTable(TEST_TABLE);
		assertEquals(0, table.size());
	}

	@Test
	public void testRemoveRowsStringStringObject() {
		int removed = Database.removeRows(TEST_TABLE, HIGH_NUMBER, 123456789l);
		assertEquals(1, removed);
		
		List<Map<String, Object>> table = Database.getTable(TEST_TABLE);
		assertEquals(0, table.size());
	}

	@Test
	public void testRemoveRowsStringMapOfStringObject() {
		Map<String, Object> row = new HashMap<String, Object>();
		row.put(NAME, "sam");
		row.put(IS_CODING, true);
		row.put(NUMBERS, 2);
		row.put(PRICE, 26.78);
		row.put(HIGH_NUMBER, 123456789l);
		int removed = Database.removeRows(TEST_TABLE, row);
		assertEquals(1, removed);
		
		List<Map<String, Object>> table = Database.getTable(TEST_TABLE);
		assertEquals(0, table.size());
	}

	@Test
	public void testGetRows() {
		Map<String, Object> row = new HashMap<String, Object>();
		row.put(NAME, "sam");
		row.put(IS_CODING, true);
		row.put(NUMBERS, 2);
		row.put(PRICE, 26.78);
		row.put(HIGH_NUMBER, 123456789l);
		
		List<Map<String, Object>> rows = Database.getRows(TEST_TABLE, NAME, "john");
		assertNull(rows);
		
		rows = Database.getRows(TEST_TABLE, NAME, "sam");
		assertEquals(1, rows.size());
		assertEquals(row, rows.get(0));
		
		rows = Database.getRows(TEST_TABLE, IS_CODING, true);
		assertEquals(1, rows.size());
		assertEquals(row, rows.get(0));
		
		rows = Database.getRows(TEST_TABLE, NUMBERS, 2);
		assertEquals(1, rows.size());
		assertEquals(row, rows.get(0));
		
		rows = Database.getRows(TEST_TABLE, PRICE, 26.78);
		assertEquals(1, rows.size());
		assertEquals(row, rows.get(0));
		
		rows = Database.getRows(TEST_TABLE, HIGH_NUMBER, 123456789l);
		assertEquals(1, rows.size());
		assertEquals(row, rows.get(0));
	}

	@Test
	public void testSetValuesStringStringObjectStringObject() {
		int modified = Database.setValues(TEST_TABLE, NAME, "sam", IS_CODING, false);
		assertEquals(1, modified);
		
		List<Map<String, Object>> table = Database.getTable(TEST_TABLE);
		assertEquals(1, table.size());
		
		Map<String, Object> row = table.get(0);
		assertEquals("sam", row.get(NAME));
		assertEquals(false, row.get(IS_CODING));
		assertEquals(2, row.get(NUMBERS));
		assertEquals(26.78, row.get(PRICE));
		assertEquals(123456789l, row.get(HIGH_NUMBER));
	}

	@Test
	public void testSetValuesStringStringObjectStringObjectStringObject() {
		int modified = Database.setValues(TEST_TABLE, IS_CODING, true, PRICE, 
				26.78, NAME, "john");
		assertEquals(1, modified);
		
		List<Map<String, Object>> table = Database.getTable(TEST_TABLE);
		assertEquals(1, table.size());
		
		Map<String, Object> row = table.get(0);
		assertEquals("john", row.get(NAME));
		assertEquals(true, row.get(IS_CODING));
		assertEquals(2, row.get(NUMBERS));
		assertEquals(26.78, row.get(PRICE));
		assertEquals(123456789l, row.get(HIGH_NUMBER));
	}

	@Test
	public void testGetValuesStringStringObjectString() {
		List<Object> values = Database.getValues(TEST_TABLE, NAME, "sam", 
				IS_CODING, true, NUMBERS);
		
		assertEquals(1, values.size());
		assertEquals(2, values.get(0));
	}

	@Test
	public void testGetValuesStringStringObjectStringObjectString() {
		List<Object> values = Database.getValues(TEST_TABLE, PRICE, 26.78, IS_CODING);
		assertEquals(1, values.size());
		assertEquals(true, values.get(0));
	}
}
