/**
 * 
 */
package com.quizzes;

import java.util.Map;
import java.util.Set;

import com.util.Util;

/**
 * Data structure for FillBlank question.
 * @author Sam
 *
 */
public class FillBlank extends Question {
	
	private Map<String, Set<String>> blanksAndAnswers;

	/**
	 * Constructor
	 * @param quizName 
	 * @param question the question in string form
	 * @param blanksAndAnswers maps a blank to a set of correct questions for it.
	 * blank Strings have to be substrings of question.
	 */
	public FillBlank(String quizName, String question, 
			Map<String, Set<String>> blanksAndAnswers) {
		
		super(quizName, question);
		Util.validateObject(blanksAndAnswers);
		validateBlanksAndAnswers(blanksAndAnswers);
		this.blanksAndAnswers = blanksAndAnswers;
	}

	/**
	 * @return the blanksAndAnswers
	 */
	public Map<String, Set<String>> getBlanksAndAnswers() {
		return blanksAndAnswers;
	}

	/**
	 * @param blanksAndAnswers the blanksAndAnswers to set
	 */
	public void setBlanksAndAnswers(Map<String, Set<String>> blanksAndAnswers) {
		Util.validateObject(blanksAndAnswers);
		this.blanksAndAnswers = blanksAndAnswers;
	}
	
	
	/**
	 * Adds a blank with a corresponding set of answers to the question.
	 * @param blank
	 * @param answers
	 */
	public void addBlank(String blank, Set<String> answers) {
		Util.validateString(blank);
		Util.validateObject(answers);
		if (!question.contains(blank)) {
			throw new IllegalArgumentException(blank + " is not a substring of " + question);
		}
		blanksAndAnswers.put(blank, answers);
	}
	
	
	public void removeBlank(String blank) {
		Util.validateString(blank);
		if (!blanksAndAnswers.containsKey(blank)) {
			throw new IllegalArgumentException(blank + " is not currently in the question");
		}
		blanksAndAnswers.remove(blank);
	}
	
	
	// Ensures that every blank is a substring of the question.
	private void validateBlanksAndAnswers(Map<String, Set<String>> blanksAndAnswers) {
		for (String blank : blanksAndAnswers.keySet()) {
			if (!question.contains(blank)) {
				throw new IllegalArgumentException(blank + " is not a substring of " + question);
			}
		}
	}
}
