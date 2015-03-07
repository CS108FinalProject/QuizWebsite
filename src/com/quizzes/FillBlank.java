package com.quizzes;

import java.util.HashSet;
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
	 * Adds a blank with a corresponding answer to the question.
	 * If the blank already exists the passed answer is added to its corresponding
	 * set, if not, a the new blank and answer are added to the question.
	 * @param blank
	 * @param answers
	 */
	public void addBlank(String blank, String answer) {
		Util.validateString(blank);
		Util.validateString(answer);
		if (!question.contains(blank)) {
			throw new IllegalArgumentException(blank + " is not a substring of " + question);
		}
		
		if (blanksAndAnswers.containsKey(blank)) {
			blanksAndAnswers.get(blank).add(answer);
			
		} else {
			Set<String> answers = new HashSet<String>();
			answers.add(answer);
			blanksAndAnswers.put(blank, answers);
		}
	}
	
	
	/**
	 * Removes the passed blank from the question
	 */
	public void removeBlank(String blank) {
		Util.validateString(blank);
		if (!blanksAndAnswers.containsKey(blank)) {
			throw new IllegalArgumentException(blank + " is not currently in the question");
		}
		blanksAndAnswers.remove(blank);
	}
	
	
	/**
	 * Determines whether the provided answer for the provided blank is correct.
	 * @param blank that answer corresponds to
	 * @param answer provided by the quiz taker
	 * @return true if correct, false otherwise
	 */
	public boolean answerIsCorrect(String blank, String answer) {
		Util.validateString(blank);
		Util.validateString(answer);

		if (!blanksAndAnswers.containsKey(blank)) {
			throw new IllegalArgumentException(blank + " is not a part of this question");
		}
		
		if (blanksAndAnswers.get(blank).contains(answer)) return true;
		return false;
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
