package com.accounts;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import com.dbinterface.Database;
import com.util.Util;

public class AccountManager {
	
	/**
	 * Constructor
	 */
	public AccountManager() {
		
	}
	
	/**
	 * Creates an account and updates the account table in the DB
	 * @param username
	 * @param password
	 */
	public void createAccount(String username, String password) {
		Util.validateString(username);
		Util.validateString(password);
		 
		try {
			AccountStub account = new AccountStub(username,password);
		} catch (RuntimeException ex) {
			
			
		}
	}
	
	/**
	 * Checks whether a given string matches its user
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean passwordMatches(String username, String password) {
		Util.validateString(username);
		Util.validateString(password);
		
		AccountStub account = new AccountStub(username,password);
		return account.passwordMatches(password);
	}
	
	/**
	 * Removes the account from the DB by deleting its row
	 * @param username
	 */
	public void removeAccount(String username) {
		Util.validateString(username);
		
		AccountStub account = new AccountStub(username);
		account.removeAccount(username);
	}
	
	/**
	 * Returns list of user's friends
	 * @param username
	 * @return
	 */
	public List<String> getFriends(String username) {
		Util.validateString(username);
		
		AccountStub account = new AccountStub(username);
		return account.getFriends(username);
	}
	
	/**
	 * Sends a given message according to its fields.
	 * The Message fields are: sender, recipient, content and type.
	 * @param sender
	 * @param recipient
	 * @param content
	 * @param type
	 */
	public void sendMessage(String sender, String recipient, String content, String type) {
		Util.validateString(sender);
		Util.validateString(recipient);
		Util.validateString(content);
		Util.validateString(type);
		
		AccountStub sender_account = new AccountStub(sender);
		AccountStub recipient_account = new AccountStub(recipient);
		Message message = new Message(sender,recipient,content,type);
		sender_account.sendMessage(message);
	}
	
	// Hashes a given string using SHA algorithm
		private static String hash_string(String str) throws NoSuchAlgorithmException {
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
		public static String hexToString(byte[] bytes) {
			StringBuffer buff = new StringBuffer();
			for (int i=0; i<bytes.length; i++) {
				int val = bytes[i];
				val = val & 0xff;  // remove higher bits, sign
				if (val<16) buff.append('0'); // leading 0
				buff.append(Integer.toString(val, 16));
			}
			return buff.toString();
		}
		
		/*
		 Given a string of hex byte values such as "24a26f", creates
		 a byte[] array of those values, one byte value -128..127
		 for each 2 chars.
		 (provided code)
		*/
		public static byte[] hexToArray(String hex) {
			byte[] result = new byte[hex.length()/2];
			for (int i=0; i<hex.length(); i+=2) {
				result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
			}
			return result;
		}
	
}
