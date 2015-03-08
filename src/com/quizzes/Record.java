package com.quizzes;

import java.util.HashMap;
import java.util.Map;

import com.accounts.Account;
import com.util.Constants;
import com.util.Util;

/**
 * Provides record information of user performance.
 * @author Sam
 */
public class Record implements Constants {

	private String quizName;
	private Account user;
	private double score;
	private String date;
	private double elapsedTime;
	
	
	/**
	 * @param quizName that was taken
	 * @param user Account of the quiz taker
	 * @param score on a scale of 100
	 * @param date when was the quiz taken
	 * @param elapsedTime how long (in minutes) did it take the user to finish
	 */
	public Record(String quizName, Account user, double score, String date,
			double elapsedTime) {
		
		Util.validateString(quizName);
		Util.validateObject(user);
		Util.validateString(date);
		if (score < MIN_SCORE || score > MAX_SCORE) {
			throw new IllegalArgumentException(score + " is not between " + MIN_SCORE 
					+ " and " + MAX_SCORE);
		}
		
		this.quizName = quizName;
		this.user = user;
		this.score = score;
		this.date = date;
		this.elapsedTime = elapsedTime;
	}


	/**
	 * @return the quizName
	 */
	public String getQuizName() {
		return quizName;
	}


	/**
	 * @return the user
	 */
	public Account getUser() {
		return user;
	}


	/**
	 * @return the score
	 */
	public double getScore() {
		return score;
	}


	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}


	/**
	 * @return the elapsedTime
	 */
	public double getElapsedTime() {
		return elapsedTime;
	}
	
	
	/**
	 * @return a Map<String, Object> representation of the Record.
	 */
	public Map<String, Object> toMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(QUIZ_NAME, quizName);
		result.put(USERNAME, user.getUserName());
		result.put(SCORE, score);
		result.put(DATE, date);
		result.put(ELAPSED_TIME, elapsedTime);
		return result;
	}
}
