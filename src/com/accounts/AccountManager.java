package com.accounts;

import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.dbinterface.Database;
import com.util.Constants;
import com.util.Util;

/**
 * Provides access to main user account functionality.
 * @author Guy && Sam
 */
public class AccountManager implements Constants {
	
	/**
	 * Checks if the necessary DB tables for Account operations exist, and creates them
	 * if they don't. 
	 */
	public static void initTables() {
		if (!Database.tableExists(ACCOUNTS)) {
			Map<String, String> columns = new LinkedHashMap<String, String>();
			columns.put(USERNAME, STRING);
			columns.put(PASSWORD, STRING);
			columns.put(IS_ADMIN, BOOLEAN); 
			Database.createTable(ACCOUNTS, columns);
		}
		
		if (!Database.tableExists(FRIENDS)) {
			Map<String, String> columns = new LinkedHashMap<String, String>();
			columns.put(USERNAME, STRING);
			columns.put(FRIEND, STRING);
			columns.put(STATUS, STRING);
			Database.createTable(FRIENDS, columns);
		}
		
		if (!Database.tableExists(MESSAGES)) {
			Map<String, String> columns = new LinkedHashMap<String, String>();
			columns.put(SENDER, STRING);
			columns.put(RECIPIENT, STRING);
			columns.put(CONTENT, STRING);
			columns.put(TYPE, STRING);
			columns.put(DATE, STRING);
			columns.put(SEEN, BOOLEAN);
			Database.createTable(MESSAGES, columns);
		}
	}
	
	
	/**
	 * Creates a new user account with the given username and password.
	 * Throws a RuntimeException if the username already exists.
	 * @param username
	 * @param password
	 * @return the newly created account
	 * @throws NoSuchAlgorithmException
	 */
	public static Account createAccount(String username, String password) throws NoSuchAlgorithmException {
		Util.validateString(username);
		Util.validateString(password);
		return new Account(username, password);
	}
	
	
	/**
	 * Given a username, returns its account.
	 * Throws a RuntimeException if the account doesn't exist.
	 * @param username
	 * @return
	 */
	public static Account getAccount(String username) {
		Util.validateString(username);
		return new Account(username);
	}
	
	
	/**
	 * Removes the account that belongs to the passed username.
	 * Throws a RuntimeException if the account doesn't exist.
	 * @param username
	 */
	public static void removeAccount(String username) {
		Util.validateString(username);
		Account account = new Account(username);
		account.removeAccount();
	}
	
	
	/**
	 * Checks if an account exists for the passed username.
	 * @param userName
	 * @return true if it does, false otherwise
	 */
	public static boolean accountExists(String username) {
		Util.validateString(username);
		try {
			new Account(username);
		} catch (RuntimeException e) {
			System.out.println("false");
			return false;
		}
		return true;
	}
	
	
	/**
	 * Checks for password match.
	 * @param username
	 * @param password
	 * @return true if the account exists and the stored password matches the passed one, false otherwise.
	 * @throws NoSuchAlgorithmException
	 */
	public static boolean passwordMatches(String username, String password) throws NoSuchAlgorithmException {
		Util.validateString(username);
		Util.validateString(password);
	
		if (!accountExists(username)) return false;
		Account account = new Account(username);
		return account.passwordMatches(password);
	}
	
	
	/**
	 * Returns a list of all user Accounts.
	 * @return a list of all user Accounts.
	 */
	public static List<Account> getAllUsers() {
		return Account.getAllUsers();
	}
}
