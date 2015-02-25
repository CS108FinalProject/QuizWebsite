package com.accounts;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.util.Util;

public class AccountManager {
	
	public AccountManager() {
		
	}
	
	public void createAccount(String username, String password) throws NoSuchAlgorithmException {
		Util.validateString(username);
		Util.validateString(password);
		 
		try {
			Account account = new Account(username,password);
		} catch (RuntimeException ex) {
			
			
		}
	}
	
	
	/**
	 * Checks if a userName is taken
	 * @param userName
	 * @return true if it is, false otherwise
	 */
	public boolean accountExists(String username) {
		return false;
	}
	
	public boolean passwordMatches(String username, String password) throws NoSuchAlgorithmException {
		Util.validateString(username);
		Util.validateString(password);
		
		Account account = new Account(username);
		return account.passwordMatches(password);
	}
	
	public void removeAccount(String username) {
		Account account = new Account(username);
		account.removeAccount();
	}
	
	public List<Account> getFriends(String username) {
		Account account = new Account(username);
		return account.getFriends();
	}
	
	
	public void sendMessage(String sender, String recipient, String content, String type, String date) {
		Message message = new Message(sender, recipient, content, type, date, false);
		
		Account senderAccount = new Account(sender);
		senderAccount.sendMessage(message);
	}
}
