package com.accounts;

/**
 * Used as a data structure, provides encapsulation for message information.
 * @author Sam
 *
 */
public class Message {
	
	private String sender;
	private String recipient;
	private String content;
	private String type;
	private String date;
	private boolean read;
	
	public Message(String sender, String recipient, String content, String type, 
			String date, boolean read) {
		
		this.sender = sender;
		this.recipient = recipient;
		this.content = content;
		this.type = type;
		this.date = date;
		this.read = read;
	}

	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @return the recipient
	 */
	public String getRecipient() {
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
		return date;
	}

	/**
	 * @return the read
	 */
	public boolean isRead() {
		return read;
	}
}
