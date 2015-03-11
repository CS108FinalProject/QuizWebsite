package com.quizzes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.util.Constants;
import com.util.Util;

public class MultiResponse extends Question implements Constants {
	
	private List<String> answers;
	private boolean isOrdered;

	/**
	 * @param quizName
	 * @param question
	 * @param answers a map of valid answers for this question.
	 * Maps from an int representing an ascending order of the answers
	 * to each answer in string form.
	 * If isOrdered = true, then the order is taken into account, otherwise
	 * is ignored.
	 * @param isOrdered determines if answers have to be provided in a 
	 * specific order
	 */
	public MultiResponse(String quizName, String question, List<String> answers,
			boolean isOrdered) {
		super(quizName, question);
		
		Util.validateObject(answers);
		this.answers = answers;
		this.isOrdered = isOrdered;
	}

	
	/**
	 * @return the answers
	 */
	public List<String> getAnswers() {
		return answers;
	}

	
	/**
	 * @param answers the answers to set
	 */
	public void setAnswers(List<String> answers) {
		Util.validateObject(answers);
		this.answers = answers;
	}
	
	
	/**
	 * Adds the passed answer to the question.
	 * @param answer
	 */
	public void addAnswer(String answer) {
		Util.validateString(answer);
		answers.add(answer);
	}
	
	
	/**
	 * Removes the passed answer from the question.
	 */
	public void removeAnswer(String answer) {
		Util.validateString(answer);
		
		if (!answers.contains(answer)) {
			throw new IllegalArgumentException(answer + " is not currently in this question.");
		}
		answers.remove(answer);
	}


	/**
	 * @return the isOrdered
	 */
	public boolean isOrdered() {
		return isOrdered;
	}


	/**
	 * @param isOrdered the isOrdered to set
	 */
	public void setOrdered(boolean isOrdered) {
		this.isOrdered = isOrdered;
	}
	
	
	/**
	 * TODO: client-side needs to forbid the user from entering duplicate answers.
	 * 
	 * Determines whether the passed answer is correct.
	 * Ignores the order parameter if question isOrdered = false.
	 */
	public boolean answerIsCorrect(List<String> providedAnswers) {
		Util.validateObject(providedAnswers);
		
		if (isOrdered) {
			return providedAnswers.equals(answers);
			
		} else {
			for (String providedAnswer : providedAnswers) {
				if (!answers.contains(providedAnswer)) return false;
			}
			return true;
		}
	}
	
	
	/**
	 * @return a representation of the object as a Map<String, Object>
	 */
	public Map<String, Object> toMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(TYPE, MULTI_RESPONSE);
		result.put(QUIZ_NAME, quizName);
		result.put(QUESTION, question);
		result.put(IS_ORDERED, isOrdered);
		result.put(ANSWERS, answers);
		return result;
	}
}
