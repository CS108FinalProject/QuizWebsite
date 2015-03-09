package com.quizzes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.accounts.Account;
import com.dbinterface.Database;
import com.util.Util;

public class Achievement implements com.util.Constants{
	
	/**
	 * Add the passed record and achievement type to the Achievements table.
	 * @param record
	 * @param type of Achievement.
	 */
	public static void add(Record record, String type) {
		Util.validateObject(record);
		Util.validateString(type);
		
		// Add row to achievements table
		Map<String, Object> row = new HashMap<String, Object>();
		row.put(QUIZ_NAME, record.getQuizName());
		row.put(USERNAME, record.getUser().getUserName());
		row.put(SCORE, record.getScore());
		row.put(DATE, record.getDate());
		row.put(ELAPSED_TIME, record.getElapsedTime());
		row.put(ACHIEVEMENT, type);		
		Database.addRow(ACHIEVEMENTS, row);
	}
	
	
	/**
	 * Checks the number of created quizzes and adds an Achievement
	 * if a goal has been reached.
	 * @param quiz a recently created Quiz.
	 */
	public static void checkCreationGoals(Quiz quiz) {
		Record record = new Record(quiz.getName(), quiz.getCreator(), 0, quiz.getCreationDate(), 0);
		List<Quiz> quizzes = QuizManager.getQuizzes(quiz.getCreator());
		if (quizzes.size() == 1) {
			add(record,AMATEUR_AUTHOR);
		} else if (quizzes.size() == 5) {
			add(record,PROLIFIC_AUTHOR);
		} else if (quizzes.size() == 10) {
			add(record,PRODIGIOUS_AUTHOR);
		}
	}
	
	
	/**
	 * Checks quiz taking performance and adds and Achievement if a goal has been reached.
	 * @param user an Account
	 * @param record the recent quiz Record
	 * @param quizName the recent quiz name
	 * @param score the recent score
	 */
	public static void checkQuizTakingGoals(Account user, Record record, String quizName, double score) {
		Util.validateObject(user);
		Util.validateObject(record);
		Util.validateString(quizName);
		
		Quiz quiz = QuizManager.getQuiz(quizName);
		if (score > quiz.getTopPerformers(1).get(0).getScore()) {
			Achievement.add(record,I_AM_THE_GREATEST);
		}
		if (user.getNumQuizzesTaken() == 10) {
			Achievement.add(record,QUIZ_MACHINE);
		}
	}
}
