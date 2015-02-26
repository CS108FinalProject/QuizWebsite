package com.util;

/**
 * Holds the constants that are used to access the Database.
 * @author Sam
 *
 */
public interface Constants {
	
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
	public static final String STRING = "String"; 
	public static final String BOOLEAN = "boolean";
	public static final String INT = "int";
	public static final String DOUBLE = "double";
	public static final String LONG = "long";
	
	// Columns table
	public static final String DB_TYPE = "Type";

}
