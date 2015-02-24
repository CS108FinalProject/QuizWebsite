package com.accounts;

// TODO: Guy, this is all done in the Account class, feel free to
// update the Account manager.
public class AccountStub {
	
	// Constructor 1
	public AccountStub(String username) {
		// done.
	}
	
	// Constructor 2
	public AccountStub(String username, String password) {
		// done.
	}
	
	// Removes the account from the DB by deleting its row
	public void removeAccount(String username) {
		// done.
	}
	
	// Sends a given message according to its fields.
	// The Message fields are: sender, recipient, content and type.
	public void sendMessage(MessageStub message) {
		// done.
	}
	
	// Checks whether a given string matches its user
	public boolean passwordMatches(String password) {
		// done.
		return false;
	}
}
