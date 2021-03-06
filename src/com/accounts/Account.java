package com.accounts;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dbinterface.Database;
import com.quizzes.Activity;
import com.quizzes.Quiz;
import com.quizzes.QuizManager;
import com.quizzes.Record;
import com.util.Constants;
import com.util.Util;

/**
 * Contains user account information. Talks to the database interface layer
 * to obtain and set fields.
 * @author Sam
 *
 */
public class Account implements Constants {
	
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
		if (Database.getValues(ACCOUNTS, USERNAME, userName, USERNAME) != null) {
			throw new IllegalArgumentException("Username: " + userName + "already exists");
		}
		
		this.userName = userName;
		
		String hashedPassword = getHash(password);
		
		// Add info to DB.
		Map<String, Object> row = new HashMap<String, Object>();
		row.put(USERNAME, userName);
		row.put(PASSWORD, hashedPassword);
		row.put(IS_ADMIN, false);
		
		Database.addRow(ACCOUNTS, row);
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
		if (Database.getValues(ACCOUNTS, USERNAME, userName, USERNAME) == null) {
			throw new IllegalArgumentException("Cannot find Username: " + userName);
		}
		this.userName = userName;
	}
	
	
	/**
	 * Removes this user account from the database and sets the userName to null
	 * so it can no longer be used.
	 */
	public void removeAccount() {
		int removed = Database.removeRows(ACCOUNTS, USERNAME, userName);
		
		// Only 1 row should be removed.
		if (removed != 1) {
			throw new RuntimeException("Problem removing rows. Removed " + removed + " rows");
		}
		
		// Remove user quizzes.
		List<Quiz> quizzes = QuizManager.getQuizzes(this);
		for (Quiz quiz : quizzes) {
			quiz.removeQuiz();
		}
		
		// Unfriend everyone.
		List<Account> friends = getFriends();
		for (Account friend : friends) {
			unfriend(friend);
		}
		
		// Remove user from Messages.
		Database.removeRows(HISTORY, USERNAME, userName);
		Database.removeRows(MESSAGES, SENDER, userName);
		Database.removeRows(MESSAGES, RECIPIENT, userName);
		Database.removeRows(ANNOUNCEMENTS, USERNAME, userName);
		Database.removeRows(ACHIEVEMENTS, USERNAME, userName);
		
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
		List<Object> currentPasswordList = Database.getValues(ACCOUNTS, USERNAME, userName, PASSWORD);
		
		// Problem with DB if there is more than one.
		if (currentPasswordList.size() != 1) {
			throw new RuntimeException("Corrupt database integrity. Duplicate user accounts for " + userName); 
		}
		
		// Type validation.
		Util.validateObjectType(currentPasswordList.get(0), STRING);
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
		int modified = Database.setValues(ACCOUNTS, USERNAME, userName, PASSWORD, 
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
		List<Object> adminList = Database.getValues(ACCOUNTS, USERNAME, userName, IS_ADMIN);
		
		// Problem with DB if there is more than one.
		if (adminList.size() != 1) {
			throw new RuntimeException("Corrupt database integrity. Duplicate user accounts for " + userName); 
		}
		
		// Type validation.
		Util.validateObjectType(adminList.get(0), BOOLEAN);
		return (Boolean) adminList.get(0);
	}

	
	/**
	 * Sets the current user's administrator status.
	 * @param isAdmin true to make administrator, false otherwise.
	 */
	public void setAdmin(boolean isAdmin) {
		Database.setValues(ACCOUNTS, USERNAME, userName, IS_ADMIN, true);
	}

	
	/**
	 * Searches the DB for this user's friends.
	 * @return a list of Account objects for the friends of this user, or null if there are non.
	 */
	public List<Account> getFriends() {
		List<Account> friends = new ArrayList<Account>();
		List<Object> friendNames = Database.getValues(FRIENDS, USERNAME, userName, STATUS, 
				"friends", FRIEND);
		
		if (friendNames == null) return friends;
		for (Object friendName : friendNames) {
			Util.validateObjectType(friendName, "String");
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
		List<Object> statusList = Database.getValues(FRIENDS, USERNAME, userName, 
				FRIEND, friendName, STATUS);
		
		if (statusList == null) return false;
		
		// Problem with DB if there is more than one.
		if (statusList.size() != 1) {
			throw new RuntimeException("Corrupt database integrity. Duplicate entry in: " 
										+ FRIENDS + ", " + USERNAME + ": " + userName 
										+  ", " + FRIEND + ": " + friendName); 
		}
		
		// Type validation.
		Util.validateObjectType(statusList.get(0), STRING);
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
		// TODO: front-end should catch this and display a helpful message.
		// No self-befriending.
		if (account.getUserName().equals(userName)) {
			throw new RuntimeException("You cannot make friends with yourself.");
		}
		
		// If users are already friends, throw exception.
		// TODO: front end should catch this and notify user.
		if (isFriend(account)) {
			throw new RuntimeException("Already friends");
		}
		
		// If this friend already sent a friend request, do nothing.
		if (friendshipPending(account)) {
			return;
		}
		
		// If friendship was already initiated by the other party, activate it.
		if (account.friendshipPending(this)) {
			// Update database to reflect friendship for both.
			
			// Add a new entry in the database for the user who closes the friendship.
			Map<String, Object> row = new HashMap<String, Object>();
			row.put(USERNAME, userName);
			row.put(FRIEND, account.getUserName());
			row.put(STATUS, "friends");
			Database.addRow(FRIENDS, row);
			
			// Update the database entry for the user who initiated the friendship.
			int modified = Database.setValues(FRIENDS, USERNAME, account.getUserName(), 
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
			Database.addRow(FRIENDS, row);
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
		
		int removed = Database.removeRows(FRIENDS, USERNAME, userName, FRIEND, friendName);
		removed += Database.removeRows(FRIENDS, USERNAME, friendName, FRIEND, userName);
		
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
		row.put(SEEN, message.isRead());
		Database.addRow(MESSAGES, row);
	}
	
	
	/**
	 * Updates the database to show this message as seen = true.
	 * @param message
	 */
	public void readMessage(Message message) {
		String recipientName = message.getRecipient();
		Database.setValues(MESSAGES, RECIPIENT, recipientName, 
				DATE, message.getDate(), SEEN, true);
	}
	
	
	/**
	 * Removes the passed message entry from the database.
	 * @param message
	 */
	public void removeMessage(Message message) {
		Map<String, Object> row = new HashMap<String, Object>();
		row.put(SENDER, message.getSender());
		row.put(CONTENT, message.getContent());
		row.put(RECIPIENT, message.getRecipient());
		row.put(TYPE, message.getType());
		row.put(DATE, message.getDate());
		row.put(SEEN, message.isRead());
		Database.removeRows(MESSAGES, row);
	}
	
	
	/**
	 * Returns a list of messages that this user has sent.
	 * @return
	 */
	public List<Message> getSentMessages() {
		ArrayList<Message> result = new ArrayList<Message>();
		List<Map<String, Object>> messages = Database.getRows(MESSAGES, SENDER, userName);
		if (messages == null) return result;
		
		for (Map<String, Object> row : messages) {
			Util.validateObjectType(row.get(SENDER), STRING);
			Util.validateObjectType(row.get(RECIPIENT), STRING);
			Util.validateObjectType(row.get(CONTENT), STRING);
			Util.validateObjectType(row.get(TYPE), STRING);
			Util.validateObjectType(row.get(DATE), STRING);
			Util.validateObjectType(row.get(SEEN), BOOLEAN);
			
			String sender = (String) row.get(SENDER);
			String recipient = (String) row.get(RECIPIENT);
			String content = (String) row.get(CONTENT);
			String type = (String) row.get(TYPE);
			String date = (String) row.get(DATE);
			boolean read = (Boolean) row.get(SEEN);
			
			result.add(new Message(sender, recipient, content, type, date, read));
		}
		return result;
	}
	
	
	/**
	 * Returns a list of messages that this user has received.
	 * @return
	 */
	public List<Message> getReceivedMessages() {
		ArrayList<Message> result = new ArrayList<Message>();
		List<Map<String, Object>> messages = Database.getRows(MESSAGES, RECIPIENT, userName);
		if (messages == null) return result;
		
		for (Map<String, Object> row : messages) {
			Util.validateObjectType(row.get(SENDER), STRING);
			Util.validateObjectType(row.get(RECIPIENT), STRING);
			Util.validateObjectType(row.get(CONTENT), STRING);
			Util.validateObjectType(row.get(TYPE), STRING);
			Util.validateObjectType(row.get(DATE), STRING);
			Util.validateObjectType(row.get(SEEN), BOOLEAN);
			
			String sender = (String) row.get(SENDER);
			String recipient = (String) row.get(RECIPIENT);
			String content = (String) row.get(CONTENT);
			String type = (String) row.get(TYPE);
			String date = (String) row.get(DATE);
			boolean read = (Boolean) row.get(SEEN);

			result.add(new Message(sender, recipient, content, type, date, read));
		}
		return result;
	}
	
	
	/**
	 * Returns the user's past performance for all taken quizzes or null if there
	 * are no records.
	 * @param numRecords amount of entries desired (0 for all).
	 */
	public List<Record> getPastPerformance(int numRecords) {
		List<Record> result = new ArrayList<Record>();
		
		if (numRecords < 0) {
			throw new IllegalArgumentException(numRecords + " cannot be less than 0.");
		}
		
		List<Map<String, Object>> rows = Database.getSortedRows(HISTORY, USERNAME, 
				userName, DATE, true);
		
		// Nothing on record.
		if (rows == null) return result;
		
		// Get all records.
		if (numRecords == 0) {
			for (Map<String, Object> row : rows) {
				result.add(new Record(
						(String) row.get(QUIZ_NAME), 
						AccountManager.getAccount((String) row.get(USERNAME)),
						(Double) row.get(SCORE), (String) row.get(DATE), 
						(Double) row.get(ELAPSED_TIME)));
			}
			
		} else {
			for (int i = 0; i < Math.min(numRecords, rows.size()); i++) {
				Map<String, Object> row = rows.get(i);
				result.add(new Record(
						(String) row.get(QUIZ_NAME), 
						AccountManager.getAccount((String) row.get(USERNAME)),
						(Double) row.get(SCORE), (String) row.get(DATE), 
						(Double) row.get(ELAPSED_TIME)));
			}
		}
		return result;
	}
	
	
	/**
	 * Returns the most recently created quizzes for the user.
	 * @param numRecords amount of desired entries in result (0 for all)
	 * @return a list of Quiz objects.
	 */
	public List<Quiz> getRecentlyCreated(int numRecords) {
		List<Quiz> result = new ArrayList<Quiz>();
		
		if (numRecords < 0) {
			throw new IllegalArgumentException(numRecords + " cannot be less than 0.");
		}
		
		List<Map<String, Object>> rows = Database.getSortedRows(QUIZZES, CREATOR, userName, 
				DATE_CREATED, true);
		
		// No results.
		if (rows == null) return result;
		
		for (Map<String, Object> row : rows) {
			result.add(QuizManager.getQuiz((String) row.get(QUIZ_NAME)));
		}
		
		return result;
	}
	
	
	/**
	 * Returns the number of quizzes that the user has taken.
	 */
	public int getNumQuizzesTaken() {
		return getPastPerformance(0).size();
	}
	
	
	/**
	 * Returns the number of quizzes that the user has created. 
	 */
	public int getNumQuizzesCreated() {
		return QuizManager.getQuizzes(this).size();
	}
	
	/**
	 * create a new announcement in the announcements table. 
	 * @param content
	 */
	public void createAnnouncement(String content) {
		Util.validateString(content);
		
		if (this.isAdmin()) {
			// get date
			String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(Calendar.getInstance().getTime());
			String date = timeStamp.toString();
			
			// create and add row to announcements table 
			Map<String, Object> row = new HashMap<String, Object>();
			row.put(USERNAME,userName);
			row.put(CONTENT, content);
			row.put(DATE, date);
			Database.addRow(ANNOUNCEMENTS, row);
			
		} else {
			throw new RuntimeException("This user is not an admin.");
		}
	}
	
	
	/**
	 * @return Achievements for this user.
	 * A Map that links an Achievement string with a record object containing all info
	 * for how the achievement was obtained.
	 */
	public Map<String, Record> getAchievements() {
		Map<String, Record> result = new HashMap<String, Record>();
		List<Map<String, Object>> rows = Database.getRows(ACHIEVEMENTS, USERNAME, userName);
		if (rows == null) return result;
		
		for (Map<String, Object> row : rows) {
			String username = (String) row.get(USERNAME);
			Account user = AccountManager.getAccount(username);
			result.put((String) row.get(ACHIEVEMENT), new Record(
					(String) row.get(QUIZ_NAME), user,
					(Double) row.get(SCORE),
					(String) row.get(DATE),
					(Double) row.get(ELAPSED_TIME)));
		}
		return result;
	}
	
	
	/**
	 * @param numRecords desired number of entries
	 * @return List of Activity objects each containing details about recent activities.
	 * The list is sorted by most recent to less recent.
	 */
	public List<Activity> getRecentFriendActivity(int numRecords) {
		List<Activity> result = new ArrayList<Activity>();
		
		//Iterate over friends
		for (Account friend : getFriends()) {
			Map<String, Record> achievements = friend.getAchievements();
			
			// Iterate over achievements
			for (String achievement : achievements.keySet()) {
				Record record = achievements.get(achievement);
				
				result.add(new Activity(friend, "earned achievement " + achievement + " on ", 
						record.getQuizName(), record.getDate()));
			}
			
			// Iterate over recently created quizzes.
			for (Quiz quiz : friend.getRecentlyCreated(0)) {
				result.add(new Activity(friend, "created quiz", quiz.getName(), quiz.getCreationDate()));
			}
			
			// Iterate over recently taken quizzes.
			for (Record record : friend.getPastPerformance(0)) {
				Quiz quiz = QuizManager.getQuiz(record.getQuizName());
				result.add(new Activity(friend, "took quiz", quiz.getName(), record.getDate()));
			}
		}
		
		// Sort from most recent.
		Collections.sort(result);

		if (numRecords == 0) return result;
		
		// Only return requested amount of values.
		for (int i = result.size() - 1; i > numRecords - 1; i--) {
			result.remove(i);
		}
		return result;
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
