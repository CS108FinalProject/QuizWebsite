package com.quizzes;

import com.accounts.Account;
import com.util.Util;

/**
 * Defines a user activity which can be
 * taking a quiz, creating a quiz, or earning an achievement.
 * @author Sam
 *
 */
public class Activity implements Comparable<Activity> {
	
	private Account user;
	private String activity;
	private Quiz quiz;
	private String date;
	
	/**
	 * @param user who performed the activity
	 * @param activity what activity was performed
	 * @param quiz what quiz was the activity performed on
	 * @param date when was the activity performed
	 */
	public Activity(Account user, String activity, Quiz quiz, String date) {
		super();
		
		Util.validateObject(user);
		Util.validateString(activity);
		Util.validateObject(quiz);
		Util.validateString(date);
		
		this.user = user;
		this.activity = activity;
		this.quiz = quiz;
		this.date = date;
	}

	/**
	 * @return the user
	 */
	public Account getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(Account user) {
		Util.validateObject(user);
		this.user = user;
	}

	/**
	 * @return the activity
	 */
	public String getActivity() {
		return activity;
	}

	/**
	 * @param activity the activity to set
	 */
	public void setActivity(String activity) {
		Util.validateString(activity);
		this.activity = activity;
	}

	/**
	 * @return the quiz
	 */
	public Quiz getQuiz() {
		return quiz;
	}

	/**
	 * @param quiz the quiz to set
	 */
	public void setQuiz(Quiz quiz) {
		Util.validateObject(quiz);
		this.quiz = quiz;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		Util.validateString(date);
		this.date = date;
	}

	// Compare according to dates in a descending order.
	@Override
	public int compareTo(Activity o) {
		return (this.date.compareTo(o.getDate())) * -1;
	}
	
}
