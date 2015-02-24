package com.accounts;

import java.util.Date;

/**
 * Used as a data structure, provides encapsulation for message information.
 * @author Sam
 *
 */
public class Message {
	
	private Account sender;
	private Account recipient;
	private String content;
	private String type;
	private Date date;
	private boolean read;
	
	public Message(Account sender, Account recipient, String content, String type, boolean read) {
		this.sender = sender;
		this.recipient = recipient;
		this.content = content;
		this.type = type;
		date = new Date(System.currentTimeMillis());
		this.read = read;
	}

	/**
	 * @return the sender
	 */
	public Account getSender() {
		return sender;
	}

	/**
	 * @return the recipient
	 */
	public Account getRecipient() {
		return recipient;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date.toString();
	}

	/**
	 * @return the read
	 */
	public boolean isRead() {
		return read;
	}
}
