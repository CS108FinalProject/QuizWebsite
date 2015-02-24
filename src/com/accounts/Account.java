package com.accounts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dbinterface.DatabaseStub;
import com.util.Util;

/**
 * Contains user account information. Talks to the database interface layer
 * to obtain and set fields.
 * @author Sam
 *
 */
public class Account {
	
	private final String userName;
	
	/**
	 * Adds a new user account entry to the database with the passed userName and password,
	 * sets the isAdmin value to false by default. Throws an exception if user already exists.
	 * @param userName
	 * @param password
	 */
	public Account(String userName, String password) {
		Util.validateString(userName);
		Util.validateString(password);
		
		// Ensure account doesn't already exists.
		List<Map<String, Object>> rows = DatabaseStub.getRows("Accounts", "userName", userName);
		if (rows != null) throw new RuntimeException("Username: " + userName + "alreadyExists");
		
		this.userName = userName;
		
		String hashedPassword = getHash(password);
		
		// Add info to DB.
		Map<String, Object> row = new HashMap<String, Object>();
		row.put("userName", userName);
		row.put("password", hashedPassword);
		row.put("isAdmin", false);
		
		DatabaseStub.addRow("Accounts", row);
	}
	
	
	/**
	 * Provides an interface to interact with the Accounts table of the database.
	 * Expects the passed userName to already exists in the database. Throws an exception
	 * if it doesn't. 
	 * @param userName
	 */
	public Account(String userName) {
		Util.validateString(userName);
		
		List<Map<String, Object>> row = DatabaseStub.getRows("Accounts", "userName", userName);
		
		// Make sure userName exists in DB.
		if (row.size() != 1) throw new RuntimeException("Corrupted Accounts table. Duplicated userName");
		
		this.userName = userName;
	}
	
	public String getUserName() {
		return userName;
	}
	
	// Returns a "SHA" hash representation of the passed string.
	private String getHash(String password) {
		return "";
	}

	
	public boolean passwordMatches(String password) {
		return false;
	}

	public void resetPassword(String newPassword) {
		Util.validateString(newPassword);
		
		DatabaseStub.setValue("Accounts", "userName", userName, "password", getHash(newPassword));
	}

	public boolean isAdmin() {
		List<Map<String, Object>> rows = DatabaseStub.getRows("Accounts", "userName", userName);
		if (rows.size() != 1) throw new RuntimeException("Corrupted Accounts table. Duplicated userName");
		return false;
	}

	public void setAdmin(boolean isAdmin) {
		
	}

	public Set<Account> getFriends() {
		return null;
	}
	
	public boolean isFriend(Account account) {
		return false;
	}

	public boolean addFriend(Account friend) {
		return true;
	}
	
	public boolean sendMessage(Message message) {
		return false;
	}
	
	public boolean receiveMessage(Message message) {
		return false;
	}
	
	public Set<Message> getMessages() {
		return null;
	}
}
