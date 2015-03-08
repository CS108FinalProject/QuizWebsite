package com.quizzes;

import java.util.HashMap;
import java.util.Map;

import com.util.Constants;
import com.util.Util;

public class MultipleChoice extends Question implements Constants {
	
	Map<String, Boolean> options;

	/**
	 * Constructor
	 * @param quizName
	 * @param question
	 * @param options maps a multiple choice option to a boolean that
	 * is true if option is correct and false otherwise.
	 */
	public MultipleChoice(String quizName, String question, 
			Map<String, Boolean> options) {
		
		super(quizName, question);
		Util.validateObject(options);
		this.options = options;
	}

	/**
	 * @return the options
	 */
	public Map<String, Boolean> getOptions() {
		return options;
	}

	/**
	 * @param options the options to set
	 */
	public void setOptions(Map<String, Boolean> options) {
		Util.validateObject(options);
		this.options = options;
	}
	
	
	/**
	 * Adds the passed option to the question.
	 * @param option multiple choice option
	 * @param correct true if correct, false otherwise
	 */
	public void addOption(String option, boolean correct) {
		Util.validateString(option);
		options.put(option, correct);
	}
	
	
	/**
	 * Removes tha passed option from the question.
	 * @param option
	 */
	public void removeOption(String option) {
		Util.validateString(option);
		if (!options.containsKey(option)) {
			throw new IllegalArgumentException(option + " is not a current option");
		}
		options.remove(option);
	}
	
	
	/**
	 * Determines whether the passed answer is correct.
	 * @param option selected by the quiz taker
	 * @return true if correct, false otherwise.
	 */
	public boolean answerIsCorrect(String option) {
		Util.validateString(option);
		
		if (!options.containsKey(option)) {
			throw new IllegalArgumentException(option + "is not a valid "
					+ "option for this question.");
		}
		
		return options.get(option);
	}
	
	
	/**
	 * @return a representation of the object as a Map<String, Object>
	 */
	public Map<String, Object> toMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(QUIZ_NAME, quizName);
		result.put(QUESTION, question);
		result.put(ANSWERS, options);
		return result;
	}

}
