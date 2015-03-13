package com.accounts;

public class Announcement {
	
	private String content;
	private String username;
	private String date;
	
	/**
	 * @param content
	 * @param username
	 * @param date
	 */
	public Announcement(String content, String username, String date) {
		super();
		this.content = content;
		this.username = username;
		this.date = date;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	
	
	
	

}
