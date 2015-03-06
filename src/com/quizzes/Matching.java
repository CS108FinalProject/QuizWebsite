package com.quizzes;

import java.util.Map;

import com.util.Util;

public class Matching extends Question {
	
	private Map<String, String> matches;

	/**
	 * Constructor
	 * @param quizName
	 * @param question
	 * @param matches maps pairs of matching items.
	 */
	public Matching(String quizName, String question, Map<String, String> matches) {
		super(quizName, question);
		
		Util.validateObject(matches);
		this.matches = matches;
	}


	/**
	 * @return the matches
	 */
	public Map<String, String> getMatches() {
		return matches;
	}

	/**
	 * @param matches the matches to set
	 */
	public void setMatches(Map<String, String> matches) {
		Util.validateObject(matches);
		this.matches = matches;
	}
	
	
	/**
	 * Adds the passed match to the question.
	 * @param left item on one side
	 * @param right item on the other
	 */
	public void addMatch(String left, String right) {
		Util.validateString(left);
		Util.validateString(right);
		matches.put(left, right);
	}
	
	
	/**
	 * Removes the passed match and its counterpart from the question.
	 * @param match
	 */
	public void removeMatch(String match) {
		Util.validateString(match);
		if (!matches.containsKey(match)) {
			throw new IllegalArgumentException(match + " is not currently in this question");
		}
		matches.remove(match);
	}
	
	
	/**
	 * Determines whether passed answer is correct
	 * @param left match on the left side provided by user
	 * @param right match on the right side provided by user
	 * @return true if correct, false otherwise
	 */
	public boolean answerIsCorrect(String left, String right) {
		Util.validateString(left);
		Util.validateString(right);
		
		if (!matches.containsKey(left)) {
			throw new IllegalArgumentException(left + " is not a valid match in this question.");
		}
		
		if (matches.get(left).equals(right)) return true;
		return false;
	}

}
