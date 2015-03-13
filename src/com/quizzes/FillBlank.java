package com.quizzes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.util.Constants;
import com.util.Util;

/**
 * Data structure for FillBlank question.
 * @author Sam
 *
 */
public class FillBlank extends Question implements Constants {
	
	private Map<String, List<String>> blanksAndAnswers;

	/**
	 * Constructor
	 * @param quizName 
	 * @param question the question in string form
	 * @param blanksAndAnswers maps a blank to a set of correct questions for it.
	 * blank Strings have to be substrings of question.
	 */
	public FillBlank(String quizName, String question, 
			Map<String, List<String>> blanksAndAnswers) {
		
		super(quizName, question);
		Util.validateObject(blanksAndAnswers);
		this.blanksAndAnswers = blanksAndAnswers;
	}

	/**
	 * @return the blanksAndAnswers
	 */
	public Map<String, List<String>> getBlanksAndAnswers() {
		return blanksAndAnswers;
	}

	/**
	 * @param blanksAndAnswers the blanksAndAnswers to set
	 */
	public void setBlanksAndAnswers(Map<String, List<String>> blanksAndAnswers) {
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
			List<String> answers = new ArrayList<String>();
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
	private void validateBlanksAndAnswers(Map<String, List<String>> blanksAndAnswers) {
		for (String blank : blanksAndAnswers.keySet()) {
			if (!question.contains(blank)) {
				throw new IllegalArgumentException(blank + " is not a substring of " + question);
			}
		}
	}
	
	
	/**
	 * @return a representation of the object as a Map<String, Object>
	 */
	public Map<String, Object> toMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(TYPE, FILL_BLANK);
		result.put(QUIZ_NAME, quizName);
		result.put(QUESTION, question);
		result.put(ANSWERS, blanksAndAnswers);
		return result;
	}
}
