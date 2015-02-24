package com.accounts;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dbinterface.DatabaseStub;
import com.util.Util;

/**
 * Contains user account information. Talks to the database interface layer
 * to obtain and set fields.
 * @author Sam
 *
 */
public class Account {
	
	private static final String ACCOUNTS = "Accounts";
	private static final String USERNAME = "userName";
	private static final String PASSWORD = "password";
	private static final String IS_ADMIN = "isAdmin";

	private static final String FRIENDS = "Friends";
	private static final String STATUS = "status";
	private static final String FRIEND = "friend";
	private static final String PENDING = "pending";
	
	private static final String MESSAGES = "Messages";
	private static final String SENDER = "sender";
	private static final String RECIPIENT = "recipient";
	private static final String CONTENT = "content";
	private static final String TYPE = "type";
	private static final String DATE = "date";
	private static final String READ = "read";
	
	private static final String STRING = "String";
	private static final String BOOLEAN = "boolean";
	
	private String userName;
	
	/**
	 * Adds a new user account entry to the database with the passed userName and password,
	 * sets the isAdmin value to false by default. Throws an exception if user already exists.
	 * @param userName
	 * @param password
	 * @throws NoSuchAlgorithmException 
	 */
	public Account(String userName, String password) throws NoSuchAlgorithmException {
		Util.validateString(userName);
		Util.validateString(password);
		
		// Ensure account doesn't already exists.
		if (DatabaseStub.getValues(ACCOUNTS, USERNAME, userName, USERNAME) != null) {
			throw new RuntimeException("Username: " + userName + "already exists");
		}
		
		this.userName = userName;
		
		String hashedPassword = getHash(password);
		
		// Add info to DB.
		Map<String, Object> row = new HashMap<String, Object>();
		row.put(USERNAME, userName);
		row.put(PASSWORD, hashedPassword);
		row.put(IS_ADMIN, false);
		
		DatabaseStub.addRow(ACCOUNTS, row);
	}
	
	
	/**
	 * Provides an interface to interact with the Accounts table of the database.
	 * Expects the passed userName to already exist in the database. Throws an exception
	 * if it doesn't. 
	 * @param userName
	 */
	public Account(String userName) {
		Util.validateString(userName);
		
		// Make sure userName exists in DB.
		if (DatabaseStub.getValues(ACCOUNTS, USERNAME, userName, USERNAME) == null) {
			throw new RuntimeException("Cannot find Username: " + userName);
		}
		this.userName = userName;
	}
	
	
	/**
	 * Removes this user account from the database and sets the userName to null
	 * so it can no longer be used.
	 */
	public void removeAccount() {
		int removed = DatabaseStub.removeRows(ACCOUNTS, USERNAME, userName);
		
		// Only 1 row should be removed.
		if (removed != 1) {
			throw new RuntimeException("Problem removing rows. Removed " + removed + " rows");
		}
		userName = null;
	}

	
	/**
	 * @return the user name for this accounts.
	 */
	public String getUserName() {
		return userName;
	}
	
	
	/**
	 * Given a password checks if it matches the stored password for this account.
	 * @param password
	 * @return true if password matches, false otherwise.
	 * @throws NoSuchAlgorithmException
	 */
	public boolean passwordMatches(String password) throws NoSuchAlgorithmException {
		// Get info from the Database.
		List<Object> currentPasswordList = DatabaseStub.getValues(ACCOUNTS, USERNAME, userName, PASSWORD);
		
		// Problem with DB if there is more than one.
		if (currentPasswordList.size() != 1) {
			throw new RuntimeException("Corrupt database integrity. Duplicate user accounts for " + userName); 
		}
		
		// Type validation.
		Util.validateObject(currentPasswordList.get(0), STRING);
		String currentPassword = (String) currentPasswordList.get(0);
		return currentPassword.equals(getHash(password));
	}

	
	/**
	 * Resets this account's password to the passed one.
	 * @param newPassword the new password
	 * @throws NoSuchAlgorithmException
	 */
	public void resetPassword(String newPassword) throws NoSuchAlgorithmException {
		Util.validateString(newPassword);
		int modified = DatabaseStub.setValues(ACCOUNTS, USERNAME, userName, PASSWORD, 
				getHash(newPassword));
		
		if (modified != 1) {
			throw new RuntimeException("Problem setting value. Modified " + modified + " values");
		}
	}

	
	/**
	 * @return true if the current account belongs to an Administrator, false otherwise.
	 */
	public boolean isAdmin() {
		// Get info from the Database.
		List<Object> adminList = DatabaseStub.getValues(ACCOUNTS, USERNAME, userName, IS_ADMIN);
		
		// Problem with DB if there is more than one.
		if (adminList.size() != 1) {
			throw new RuntimeException("Corrupt database integrity. Duplicate user accounts for " + userName); 
		}
		
		// Type validation.
		Util.validateObject(adminList.get(0), BOOLEAN);
		return (Boolean) adminList.get(0);
	}

	
	/**
	 * Sets the current user's administrator status.
	 * @param isAdmin true to make administrator, false otherwise.
	 */
	public void setAdmin(boolean isAdmin) {
		DatabaseStub.setValues(ACCOUNTS, USERNAME, userName, IS_ADMIN, true);
	}

	
	/**
	 * Searches the DB for this user's friends.
	 * @return a list of Account objects for the friends of this user, or null if there are non.
	 */
	public List<Account> getFriends() {
		List<Object> friendNames = DatabaseStub.getValues(FRIENDS, USERNAME, userName, STATUS, 
				"friends", FRIEND);
		
		if (friendNames == null) return null;

		List<Account> friends = new ArrayList<Account>();
		for (Object friendName : friendNames) {
			Util.validateObject(friendName, "String");
			friends.add(new Account((String) friendName));
		}
		return friends;
	}
	
	
	/**
	 * Determines if the owner of the passed account if friends with the owner
	 * of this account.
	 * @param account a user account
	 * @return true if there is a friendship, false otherwise.
	 */
	public boolean isFriend(Account account) {
		String friendName = account.getUserName();
		
		List<Account> friends = getFriends();
		if (friends == null) return false;
		
		for (Account friend : friends) {
			if (friend.getUserName().equals(friendName)) return true;
		}
		return false;
	}

	
	/**
	 * Examines the database to determine if friendship is pending.
	 * @param account 
	 * @return true if friendship is pending, false otherwise.
	 */
	public boolean friendshipPending(Account account) {
		String friendName = account.getUserName();
		List<Object> statusList = DatabaseStub.getValues(FRIENDS, USERNAME, userName, 
				FRIEND, friendName, STATUS);
		
		if (statusList == null) return false;
		
		// Problem with DB if there is more than one.
		if (statusList.size() != 1) {
			throw new RuntimeException("Corrupt database integrity. Duplicate entry in: " 
										+ FRIENDS + ", " + USERNAME + " " + userName 
										+  FRIEND + " " + friendName); 
		}
		
		// Type validation.
		Util.validateObject(statusList.get(0), STRING);
		return statusList.get(0).equals(PENDING);
	}
	
	
	/**
	 * Attempts to make friendship with owner of passed account.
	 * If a friendship already exists, an exception is thrown.
	 * If the owner of the passed account had already initiated the process, the 
	 * friendship is activated.
	 * Otherwise, the friendship will be pending.
	 * @param account friendship will be attempted with the owner of this account.
	 */
	public void addFriend(Account account) {
		// If users are already friends, throw exception.
		// TODO: front end should catch this and notify user.
		if (isFriend(account)) {
			throw new RuntimeException("Already friends");
		}
		
		// If friendship was already initiated by the other party, activate it.
		if (account.friendshipPending(this)) {
			// Update database to reflect friendship for both.
			
			// Add a new entry in the database for the user who closes the friendship.
			Map<String, Object> row = new HashMap<String, Object>();
			row.put(USERNAME, userName);
			row.put(FRIEND, account.getUserName());
			row.put(STATUS, "friends");
			DatabaseStub.addRow(FRIENDS, row);
			
			// Update the database entry for the user who initiated the friendship.
			int modified = DatabaseStub.setValues(FRIENDS, USERNAME, account.getUserName(), 
					FRIEND, userName, STATUS, "friends");

			// Only 1 value should be modified.
			if (modified != 1) {
				throw new RuntimeException("Problem setting value. Modified " + modified + " values");
			}
			
		// If friendship has not already been initiated.
		} else {
			// add a this | account | pending entry to db.
			Map<String, Object> row = new HashMap<String, Object>();
			row.put(USERNAME, userName);
			row.put(FRIEND, account.getUserName());
			row.put(STATUS, PENDING);
			DatabaseStub.addRow(FRIENDS, row);
		}
	}
	
	
	/**
	 * Unfriends the owner of the passed account.
	 * If users were not already friends, an exception is thrown.
	 * @param account the owner of this account will be unfriended.
	 */
	public void unfriend(Account account) {
		// TODO: front end should catch this and notify user.
		if (!isFriend(account)) {
			throw new RuntimeException("Not friends");
		}
		
		String friendName = account.getUserName();
		
		int removed = DatabaseStub.removeRows(FRIENDS, USERNAME, userName, FRIEND, friendName);
		removed += DatabaseStub.removeRows(FRIENDS, USERNAME, friendName, FRIEND, userName);
		
		// Only 2 rows should be removed.
		if (removed != 2) {
			throw new RuntimeException("Problem removing rows. Removed " + removed + " rows");
		}
	}
	
	/**
	 * Adds the passed message as a new entry in the database
	 * and marks it as read = false.
	 * @param message
	 */
	public void sendMessage(Message message) {
		Map<String, Object> row = new HashMap<String, Object>();
		row.put(SENDER, message.getSender());
		row.put(CONTENT, message.getContent());
		row.put(RECIPIENT, message.getRecipient());
		row.put(TYPE, message.getType());
		row.put(DATE, message.getDate());
		row.put(READ, message.isRead());
		DatabaseStub.addRow(MESSAGES, row);
	}
	
	
	/**
	 * Updates the database to show this message as read = true.
	 * @param message
	 */
	public void readMessage(Message message) {
		String recipientName = message.getRecipient().getUserName();
		int modified = DatabaseStub.setValues(MESSAGES, recipientName, userName, 
				DATE, message.getDate(), READ, true);
		
		if (modified != 1) {
			throw new RuntimeException("Problem modifying rows. Modified " + modified + " rows");
		}
	}
	
	
	public void removeMessage(Message message) {
		
	}
	
	public List<Message> getSentMessages() {
		return null;
	}
	
	
	public List<Message> getReceivedMessages() {
		return null;
	}
	
	
	/**
	 * @return a list of all userNames in the Accounts table, or null if the table doesn't exist.
	 */
	public static List<String> getAllUserNames() {
		List<String> userNames = new ArrayList<String>();
		
		List<Map<String, Object>> table = DatabaseStub.getTable(ACCOUNTS);
		if (table == null) return null;
		
		for (Map<String, Object> account : table) {
			Util.validateObject(account.get(USERNAME), STRING);
			userNames.add((String) account.get(USERNAME));
		}
		return userNames;
	}
	
	
	
	
	//----------------------------Helper Methods-------------------------------//
	
	// Hashes a given string using SHA algorithm
	private String getHash(String str) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA");
		md.update(str.getBytes());
	    byte[] digest = md.digest();
		return hexToString(digest);
	}
	
	
	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	private String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
}
