package com.quizzes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.accounts.Account;
import com.dbinterface.Database;
import com.util.Constants;
import com.util.Util;

/**
 * Contains Quiz information. Talks to the database interface layer
 * to obtain and set fields.
 * 
 * @author Sam
 *
 */
public class Quiz implements Constants {
	
	private String name;
	
	
	/**
	 * Constructs a new Quiz object and adds the quiz info to the database.
	 */
	public Quiz(String name, Account creator, String description, String date, 
			boolean isRandom, boolean isOnePage, boolean isImmediate) {
		
		Util.validateString(name);
		Util.validateObject(creator);
		Util.validateString(description);
		Util.validateString(date);
		
		this.name = name;
		
		// Ensure quiz doesn't already exists.
		if (Database.getValues(QUIZZES, QUIZ_NAME, name, QUIZ_NAME) != null) {
			throw new IllegalArgumentException("Quiz " + name + "already exists");
		}
		
		Map<String, Object> row = new HashMap<String, Object>();
		row.put(QUIZ_NAME, name);
		row.put(CREATOR, creator.getUserName());
		row.put(DESCRIPTION, description);
		row.put(DATE_CREATED, date);
		row.put(IS_RANDOM, isRandom);
		row.put(IS_ONE_PAGE, isOnePage);
		row.put(IS_IMMEDIATE, isImmediate);
		Database.addRow(QUIZZES, row);
	}
	
	
	/**
	 * Provides an interface to interact with the Quizzes table of the database.
	 * Expects the passed quiz name to already exist in the database. Throws an exception
	 * if it doesn't. 
	 * @param userName
	 */
	public Quiz(String name) {
		Util.validateString(name);
		
		// Make sure quiz name exists in DB.
		if (Database.getValues(QUIZZES, QUIZ_NAME, name, QUIZ_NAME) == null) {
			throw new IllegalArgumentException("Cannot find quiz " + name);
		}
		this.name = name;
	}
	
	
	/**
	 * Removes the quiz from the database and sets the name to null
	 * so it can no longer be used.
	 */
	public void removeQuiz() {
		int removed = Database.removeRows(QUIZZES, QUIZ_NAME, name);
		
		// Only 1 row should be removed.
		if (removed != 1) {
			throw new RuntimeException("Problem removing rows. Removed " + removed + " rows");
		}
		
		// Remove questions for this quiz.
		for (String tableName : QUESTION_TYPES) {
			Database.removeRows(tableName, QUIZ_NAME, name);
		}
		
		name = null;
	}
	
	
	/**
	 * @return the quiz name for this Quiz.
	 */
	public String getName() {
		return name;
	}
	
	
	public <T extends Question> List<T> getQuestions() {
		List<T> result = new ArrayList<T>();
		
		for (String tableName : QUESTION_TYPES) {
			List<Map<String, Object>> questions = Database.getRows(tableName, QUIZ_NAME, name);
			
			// Move on to next table if there are no questions here.
			if (questions == null) continue;
			
			String question = (String) questions.get(0).get(QUESTION);
			
			
			
			
			for (Map<String, Object> row : questions) {
				
			}
			
			
//			for (Object question : questions) {
//				String cur = (String) question;
//				result.add(cur);
//			}
		}
		return result;
	}
	
	
	
	/**
	 * Returns all the current questions for this quiz in string form.
	 * See getQuestions to get the Question Objects.
	 */
	public Set<String> getQuestionsAsStrings() {
		Set<String> result = new HashSet<String>();
		
		for (String tableName : QUESTION_TYPES) {
			List<Object> questions = Database.getValues(tableName, QUIZ_NAME, name, QUESTION);
			for (Object question : questions) {
				String cur = (String) question;
				result.add(cur);
			}
		}
		return result;
	}
	
	
	/**
	 * Adds the passed question object to the database.
	 * @param question
	 */
	public <T extends Question> void addQuestion(T question) {
		Util.validateObject(question);
		
		if (question instanceof Response) addResponse((Response) question);
		else if (question instanceof FillBlank) addFillBlank((FillBlank) question);
		else if (question instanceof MultipleChoice) addMultipleChoice((MultipleChoice) question);
		else if (question instanceof Picture) addPicture((Picture) question);
		else if (question instanceof MultiResponse) addMultiResponse((MultiResponse) question);
		else if (question instanceof Matching) addMatching((Matching) question);
		else {
			throw new IllegalArgumentException("Passed question is invalid");
		}
	}
	
	
	/**
	 * Takes the old state of a question before it was modified and the new state after being
	 * modified. Looks for the old state in the database and replaces it with the new one.
	 * @param oldQuestion the question object before modification
	 * @param newQuestion the question object after modification
	 */
	public <T extends Question> void editQuestion(T oldQuestion, T newQuestion) {
		Util.validateObject(oldQuestion);
		Util.validateObject(newQuestion);
		
		if (oldQuestion instanceof Response) {
			editResponse((Response) oldQuestion, (Response) newQuestion);
			
		} else if (oldQuestion instanceof FillBlank) {
			editFillBlank((FillBlank) oldQuestion, (FillBlank) newQuestion);
			
		} else if (oldQuestion instanceof MultipleChoice) {
			editMultipleChoice((MultipleChoice) oldQuestion, (MultipleChoice) newQuestion);
			
		} else if (oldQuestion instanceof Picture) {
			editPicture((Picture) oldQuestion, (Picture) newQuestion);
			
		} else if (oldQuestion instanceof MultiResponse) {
			editMultiResponse((MultiResponse) oldQuestion, (MultiResponse) newQuestion);
			
		} else if (oldQuestion instanceof Matching) {
			editMatching((Matching) oldQuestion, (Matching) newQuestion);
			
		} else {
			throw new IllegalArgumentException("Passed question is invalid");
		}
	}
	
	
	public <T extends Question> void removeQuestion(T question) {
		// TODO: implement
	}
	
	
	
	//--------------------------- Helper Methods -------------------------------//
	
	
	// Adds a Response question to the database.
	private void addResponse(Response response) {
		// No duplicate questions.
		if (getQuestionsAsStrings().contains(response.getQuestion())) {
			throw new IllegalArgumentException("The question already exists in " + name);
		}
		
		// Add a row for every answer in the question.
		for (String answer : response.getAnswers()) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put(QUIZ_NAME, name);
			row.put(QUESTION, response.getQuestion());
			row.put(ANSWER, answer);
			
			// If row already exists, do nothing.
			if (Database.getRows(RESPONSE, row) != null) {
				return;
			}
			
			Database.addRow(RESPONSE, row);
		}
	}
	
	
	// Adds a FillBlank question to the database.
	private void addFillBlank(FillBlank fillBlank) {
		// No duplicate questions.
		if (getQuestionsAsStrings().contains(fillBlank.getQuestion())) {
			throw new IllegalArgumentException("The question already exists in " + name);
		}
		
		// Iterate over every blank.
		for (String blank : fillBlank.getBlanksAndAnswers().keySet()) {
			// Iterate over every answer per each blank.
			for (String answer : fillBlank.getBlanksAndAnswers().get(blank)) {
				Map<String, Object> row = new HashMap<String, Object>();
				row.put(QUIZ_NAME, name);
				row.put(QUESTION, fillBlank.getQuestion());
				row.put(BLANK, blank);
				row.put(ANSWER, answer);
				
				// If row already exists, do nothing.
				if (Database.getRows(FILL_BLANK, row) != null) {
					return;
				}
				
				Database.addRow(FILL_BLANK, row);
			}
		}
	}
	
	
	// Adds a MultipleChoice question to the database.
	private void addMultipleChoice(MultipleChoice multipleChoice) {
		// No duplicate questions.
		if (getQuestionsAsStrings().contains(multipleChoice.getQuestion())) {
			throw new IllegalArgumentException("The question already exists in " + name);
		}
		
		// Add a row for every answer in the question.
		for (String option : multipleChoice.getOptions().keySet()) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put(QUIZ_NAME, name);
			row.put(QUESTION, multipleChoice.getQuestion());
			row.put(OPTION, option);
			row.put(IS_ANSWER, multipleChoice.getOptions().get(option));
			
			// If row already exists, do nothing.
			if (Database.getRows(MULTIPLE_CHOICE, row) != null) {
				return;
			}
			
			Database.addRow(MULTIPLE_CHOICE, row);
		}
	}


	// Adds a Picture question to the database.
	private void addPicture(Picture picture) {
		// No duplicate questions.
		if (getQuestionsAsStrings().contains(picture.getQuestion())) {
			throw new IllegalArgumentException("The question already exists in " + name);
		}
				
		// Add a row for every answer in the question.
		for (String answer : picture.getAnswers()) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put(QUIZ_NAME, name);
			row.put(QUESTION, picture.getQuestion());
			row.put(PICTURE_URL, picture.getPictureUrl());
			row.put(ANSWER, answer);
			
			// If row already exists, do nothing.
			if (Database.getRows(PICTURE, row) != null) {
				return;
			}
			
			Database.addRow(PICTURE, row);
		}
	}


	// Adds a MultiResponse question to the database.
	private void addMultiResponse(MultiResponse multiResponse) {
		// No duplicate questions.
		if (getQuestionsAsStrings().contains(multiResponse.getQuestion())) {
			throw new IllegalArgumentException("The question already exists in " + name);
		}
		
		// Add a row for every answer in the question.
		for (int i = 0; i < multiResponse.getAnswers().size(); i++) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put(QUIZ_NAME, name);
			row.put(QUESTION, multiResponse.getQuestion());
			row.put(ANSWER, multiResponse.getAnswers().get(i));
			row.put(IS_ORDERED, multiResponse.isOrdered());
			row.put(ORDER, i);
			
			// If row already exists, do nothing.
			if (Database.getRows(MULTI_RESPONSE, row) != null) {
				return;
			}
			
			Database.addRow(MULTI_RESPONSE, row);
		}
	}


	// Adds a Matching question to the database.
	private void addMatching(Matching matching) {
		// No duplicate questions.
		if (getQuestionsAsStrings().contains(matching.getQuestion())) {
			throw new IllegalArgumentException("The question already exists in " + name);
		}
		
		// Add a row for every answer in the question.
		for (String left : matching.getMatches().keySet()) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put(QUIZ_NAME, name);
			row.put(QUESTION, matching.getQuestion());
			row.put(QUESTION_ID, matching.getId());
			row.put(LEFT, left);
			row.put(RIGHT, matching.getMatches().get(left));
			
			// If row already exists, do nothing.
			if (Database.getRows(MATCHING, row) != null) {
				return;
			}
			
			Database.addRow(MATCHING, row);
		}
	}
	
	
	// Edits a Response question in the database.
	private void editResponse(Response oldQuestion, Response newQuestion) {
		// Check type consistency
		if (!(newQuestion instanceof Response)) {
			throw new IllegalArgumentException("Both passed questions have to be the same type");
		}
		
		// Quiz name cannot be modified in a question.
		if (!oldQuestion.getQuizName().equals(newQuestion.getQuizName())) {
			throw new IllegalArgumentException("the quiz name in a question cannot be modified");
		}
		
		// If the question was modified, ensure it is not duplicate.
		if (!oldQuestion.getQuestion().equals(newQuestion.getQuestion())) {
			if (getQuestionsAsStrings().contains(newQuestion.getQuestion())) {
				throw new IllegalArgumentException("The question already exists in" 
						+ newQuestion.getQuizName());
			}
		}
		
		// remove oldQuestion
		addQuestion(newQuestion);
	}
	
	
	// Edits a FillBlank question in the database.
	private void editFillBlank(FillBlank oldQuestion, FillBlank newQuestion) {
		
	}
	
	
	// Edits a MultipleChoice question in the database.
	private void editMultipleChoice(MultipleChoice oldQuestion, MultipleChoice newQuestion) {
		
	}
	
	
	// Edits a Picture question in the database.
	private void editPicture(Picture oldQuestion, Picture newQuestion) {
		
	}
	
	
	// Edits a MultiResponse question in the database.
	private void editMultiResponse(MultiResponse oldQuestion, MultiResponse newQuestion) {
		
	}
	
	
	// Edits a Matching question in the database.
	private void editMatching(Matching oldQuestion, Matching newQuestion) {
		
	}
}
