package com.dbinterface;

/**
 * Holds the constants that are used to access the Database.
 * @author Sam
 *
 */
public interface DatabaseConstants {
	
	// Accounts table
	public static final String ACCOUNTS = "Accounts";
	public static final String USERNAME = "userName";
	public static final String PASSWORD = "password";
	public static final String IS_ADMIN = "isAdmin";

	// Friends table
	public static final String FRIENDS = "Friends";
	public static final String STATUS = "status";
	public static final String FRIEND = "friend";
	public static final String PENDING = "pending";
	
	// Messages table
	public static final String MESSAGES = "Messages";
	public static final String SENDER = "sender";
	public static final String RECIPIENT = "recipient";
	public static final String CONTENT = "content";
	public static final String TYPE = "type";
	public static final String DATE = "date";
	public static final String READ = "read";
	
	// Types
	public static final String STRING = "string";
	public static final String BOOLEAN = "boolean";
	public static final String INT = "int";
	public static final String DOUBLE = "double";
	public static final String LONG = "long";
	
	// Database datatypes
	public static final String DB_STRING = "CHAR(64)";
	public static final String DB_INT = "BIGINT";
	public static final String DB_BOOLEAN = "TINYINT(1)";
	public static final String DB_DOUBLE = "DOUBLE(50, 5)";
	public static final String DB_LONG = "BIGINT";
	

}
