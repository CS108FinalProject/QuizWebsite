package com.quizzes;

import java.util.TreeMap;

import com.util.Util;

public class MultiResponse extends Question {
	
	private TreeMap<Integer, String> answers;
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
	public MultiResponse(String quizName, String question, TreeMap<Integer, String> answers,
			boolean isOrdered) {
		super(quizName, question);
		
		Util.validateObject(answers);
		this.answers = answers;
		this.isOrdered = isOrdered;
	}

	
	/**
	 * @return the answers
	 */
	public TreeMap<Integer, String> getAnswers() {
		return answers;
	}

	
	/**
	 * @param answers the answers to set
	 */
	public void setAnswers(TreeMap<Integer, String> answers) {
		Util.validateObject(answers);
		this.answers = answers;
	}
	
	
	/**
	 * Adds the passed answer to the question.
	 * @param answer
	 */
	public void addAnswer(int order, String answer) {
		Util.validateString(answer);
		answers.put(order, answer);
	}
	
	
	/**
	 * Removes the passed answer from the question.
	 */
	public void removeAnswer(String answer) {
		Util.validateString(answer);
		
		for (int order : answers.keySet()) {
			String cur = answers.get(order);
			if (cur.equals(answer)) {
				answers.remove(order);
				return;
			}
		}
		throw new IllegalArgumentException(answer + " is not currently in this question.");
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
