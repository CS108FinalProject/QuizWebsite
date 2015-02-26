package com.accounts;

import java.util.List;

public class AccountStub {
	
	// Constructor 1
	public AccountStub(String username) {
		
	}
	
	// Constructor 2
	public AccountStub(String username, String password) {
		
	}
	
	// Removes the account from the DB by deleting its row
	public void removeAccount(String username) {
		
	}
	
	// Sends a given message according to its fields.
	// The Message fields are: sender, recipient, content and type.
	public void sendMessage(Message message) {
		
	}
	
	public List<String> getFriends(String username) {
		return null;
	}
	
	// Checks whether a given string matches its user
	public boolean passwordMatches(String password) {
		return false;
	}
}
