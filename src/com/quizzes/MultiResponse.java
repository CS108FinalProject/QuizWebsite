package com.quizzes;

import java.util.List;

import com.util.Util;

public class MultiResponse extends Question {
	
	private List<String> answers;
	private boolean isOrdered;

	/**
	 * @param quizName
	 * @param question
	 * @param answers a list of valid answers for this question.
	 * If isOrdered = true, then the order is the insertion order
	 * in the answers list
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
}
