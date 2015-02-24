package com.accounts;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import com.dbinterface.Database;

public class AccountManager {
	
	public AccountManager() {
		
	}
	
	public void createAccount(String username, String password) {
		//Util.validateString(username);
		//Util.validateString(password);
		 
		try {
			AccountStub account = new AccountStub(username,password);
		} catch (RuntimeException ex) {
			
			
		}
	}
	
	public boolean passwordMatches(String username, String password) {
		//Util.validateString(username);
		//Util.validateString(password);
		
		AccountStub account = new AccountStub(username,password);
		return account.passwordMatches(password);
	}
	
	public void removeAccount(String username) {
		AccountStub account = new AccountStub(username);
		account.removeAccount(username);
	}
	
	public List<String> getFriends(String username) {
		AccountStub account = new AccountStub(username);
		return getFriends(username);
	}
	
	public void sendMessage(String sender, String recipient, String content, String type) {
		AccountStub sender_account = new AccountStub(sender);
		AccountStub recipient_account = new AccountStub(recipient);
		MessageStub message = new MessageStub(sender,recipient,content,type);
		sender_account.sendMessage(message);
		//recipient_account.receiveMessage(message);
	}
}
