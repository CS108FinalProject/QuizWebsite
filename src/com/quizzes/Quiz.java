package com.quizzes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.accounts.Account;
import com.accounts.AccountManager;
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
	protected Quiz(String name, Account creator, String description, String date, 
			boolean isRandom, boolean isOnePage, boolean isImmediate) {
		
		Util.validateString(name);
		Util.validateObject(creator);
		Util.validateString(description);
		Util.validateString(date);
		
		this.name = name;
		
		// Ensure quiz doesn't already exists.
		if (Database.getValues(QUIZZES, QUIZ_NAME, name, QUIZ_NAME) != null) {
			throw new IllegalArgumentException("Quiz " + name + " already exists");
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
	 * Constructs a new Quiz object given a Map<String, Object> representation of it.
	 */
	@SuppressWarnings("unchecked")
	protected Quiz(Map<String, Object> quizMap) {
		Util.validateObject(quizMap);
		
		// Validate Type
		if (!(quizMap.get(QUIZ_METADATA) instanceof Map<?, ?>)) { 
			throw new IllegalArgumentException("Invalid quizMap.");
		}
		
		Map<String, Object> metadata = (Map<String, Object>) quizMap.get(QUIZ_METADATA);
		
		String quizName = (String) metadata.get(QUIZ_NAME);
		Util.validateString(quizName);
		
		String creatorName = (String) metadata.get(CREATOR);
		Util.validateString(creatorName);
		Account creator = AccountManager.getAccount(creatorName);
		
		String description = (String) metadata.get(DESCRIPTION);
		Util.validateString(description);
		
		String date = (String) metadata.get(DATE_CREATED);
		Util.validateString(date);
		
		boolean isRandom = (Boolean) metadata.get(IS_RANDOM);
		boolean isOnePage = (Boolean) metadata.get(IS_ONE_PAGE);
		boolean isImmediate = (Boolean) metadata.get(IS_IMMEDIATE);
		
		// Create Quiz.
		Quiz quiz = new Quiz(quizName, creator, description, date, isRandom, isOnePage,
				isImmediate);
		
		this.name = quizName;
		
		
		// If adding questions fails, remove the created quiz.
		try {
			// Get questions.
			// Validate Type
			if (!(quizMap.get(QUESTIONS) instanceof List<?>)) {
				throw new IllegalArgumentException("Invalid quizMap.");
			}
			
			List<Map<String, Object>> questions = (List<Map<String, Object>>) quizMap.get(QUESTIONS);
			if (questions.size() == 0) {
				throw new IllegalArgumentException("The quiz must have at least 1 question");
			}
			
			for (Map<String, Object> questionMap : questions) {
				// Get question type.
				String type = (String) questionMap.get(TYPE);
				Util.validateString(type);
				
				Question question = null;
				if (type.equals(FILL_BLANK)) {
					String questionPrompt = (String) questionMap.get(QUESTION);
					Map<String, List<String>> answers = (Map<String, List<String>>) questionMap.get(BLANKS_AND_ANSWERS);
					question = new FillBlank(quizName, questionPrompt, answers);
	
				} else if (type.equals(MULTIPLE_CHOICE)) {
					String questionPrompt = (String) questionMap.get(QUESTION);
					Map<String, Boolean> answers = (Map<String, Boolean>) questionMap.get(OPTIONS);
					question = new MultipleChoice(quizName, questionPrompt, answers);
					
				} else if (type.equals(PICTURE)) {
					String questionPrompt = (String) questionMap.get(QUESTION);
					String pictureUrl = (String) questionMap.get(PICTURE_URL);
					List<String> answers = (List<String>) questionMap.get(ANSWERS);
					question = new Picture(quizName, questionPrompt, pictureUrl, answers);
					
				} else if (type.equals(MULTI_RESPONSE)) {
					String questionPrompt = (String) questionMap.get(QUESTION);
					boolean isOrdered = (Boolean) questionMap.get(IS_ORDERED);
					List<String> answers = (List<String>) questionMap.get(ANSWERS);
					question = new MultiResponse(quizName, questionPrompt, answers, isOrdered);
					
				} else if (type.equals(MATCHING)) {
					String questionPrompt = (String) questionMap.get(QUESTION);
					Map<String, String> answers = (Map<String, String>) questionMap.get(MATCHING_PAIRS);
					question = new Matching(quizName, questionPrompt, answers);
					
				} else if (type.equals(RESPONSE)) {
					String questionPrompt = (String) questionMap.get(QUESTION);
					List<String> answers = (List<String>) questionMap.get(ANSWERS);
					question = new Response(quizName, questionPrompt, answers);
					
				} else {
					throw new IllegalArgumentException("Cannot recognize question type " + type);
				}
				quiz.addQuestion(question);
			}
		
		} catch (Exception e) {
			if (quiz.getName() != null) quiz.removeQuiz();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	/**
	 * Provides an interface to interact with the Quizzes table of the database.
	 * Expects the passed quiz name to already exist in the database. Throws an exception
	 * if it doesn't. 
	 * @param userName
	 */
	protected Quiz(String name) {
		Util.validateString(name);
		
		// Make sure quiz name exists in DB.
		if (Database.getValues(QUIZZES, QUIZ_NAME, name, QUIZ_NAME) == null) {
			throw new IllegalArgumentException("Cannot find quiz " + name);
		}
		this.name = name;
	}
	
	
	/**
	 * @return the quiz name for this Quiz.
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * Changes the name of the quiz to the provided name. Iterates over
	 * all questions that belong to the quiz and changes names.
	 * provided name cannot already be in use.
	 * @param newName
	 */
	public void setName(String newName) {
		Util.validateString(newName);
		
		if (QuizManager.quizNameInUse(newName)) {
			throw new IllegalArgumentException("name " + newName + " already in use.");
		}
		
		Database.setValues(QUIZZES, QUIZ_NAME, name, QUIZ_NAME, newName);
		for (String questionType : QUESTION_TYPES) {
			Database.setValues(questionType, QUIZ_NAME, name, QUIZ_NAME, newName);
		}
		
		this.name = newName;
	}
	
	
	/**
	 * @return the Account of the quiz creator.
	 */
	public Account getCreator() {
		List<Object> values = Database.getValues(QUIZZES, QUIZ_NAME, name, CREATOR);
		if (values == null) {
			throw new RuntimeException("Corrupt table status for " + QUIZZES + 
					" . Cannot find creator for " + name);
		}
		
		if (values.size() > 1) {
			throw new RuntimeException("Corrupt table status for " + QUIZZES + 
					" . Duplicate entries for " + name);
		}
		
		Util.validateObjectType(values.get(0), STRING);
		return AccountManager.getAccount((String) values.get(0));
	}
	
	
	/**
	 * @return the quiz description.
	 */
	public String getDescription() {
		List<Object> values = Database.getValues(QUIZZES, QUIZ_NAME, name, DESCRIPTION);
		if (values == null) {
			throw new RuntimeException("Corrupt table status for " + QUIZZES + 
					" . Cannot find description for " + name);
		}
		
		if (values.size() > 1) {
			throw new RuntimeException("Corrupt table status for " + QUIZZES + 
					" . Duplicate entries for " + name);
		}
		
		Util.validateObjectType(values.get(0), STRING);
		return (String) values.get(0);
	}
	
	
	/**
	 * Sets the description.
	 * @param newDescription
	 */
	public void setDescription(String newDescription) {
		Util.validateString(newDescription);
		
		int modified = Database.setValues(QUIZZES, QUIZ_NAME, name, DESCRIPTION, newDescription);
		
		if (modified == 0) {
			throw new RuntimeException("Corrupt table status for " + QUIZZES + 
					" . Cannot find description for " + name);
		}
		
		if (modified > 1) {
			throw new RuntimeException("Corrupt table status for " + QUIZZES + 
					" . Duplicate entries for " + name);
		}
	}
	
	
	/**
	 * @return the Quiz creation date.
	 */
	public String getCreationDate() {
		List<Object> values = Database.getValues(QUIZZES, QUIZ_NAME, name, DATE_CREATED);
		if (values == null) {
			throw new RuntimeException("Corrupt table status for " + QUIZZES + 
					" . Cannot find creation date for " + name);
		}
		
		if (values.size() > 1) {
			throw new RuntimeException("Corrupt table status for " + QUIZZES + 
					" . Duplicate entries for " + name);
		}
		
		Util.validateObjectType(values.get(0), STRING);
		return (String) values.get(0);
	}
	
	
	/**
	 * @return whether or not the quiz is random.
	 */
	public boolean isRandom() {
		List<Object> values = Database.getValues(QUIZZES, QUIZ_NAME, name, IS_RANDOM);
		if (values == null) {
			throw new RuntimeException("Corrupt table status for " + QUIZZES + 
					" . Cannot find isRandom field for " + name);
		}
		
		if (values.size() > 1) {
			throw new RuntimeException("Corrupt table status for " + QUIZZES + 
					" . Duplicate entries for " + name);
		}
		
		Util.validateObjectType(values.get(0), BOOLEAN);
		return (Boolean) values.get(0);
	}
	
	
	/**
	 * Sets random status for the quiz.
	 */
	public void setRandom(boolean random) {
		int modified = Database.setValues(QUIZZES, QUIZ_NAME, name, IS_RANDOM, random);
		
		if (modified == 0) {
			throw new RuntimeException("Corrupt table status for " + QUIZZES + 
					" . Cannot find description for " + name);
		}
		
		if (modified > 1) {
			throw new RuntimeException("Corrupt table status for " + QUIZZES + 
					" . Duplicate entries for " + name);
		}
	}
	
	
	/**
	 * @return whether or not the quiz is one page.
	 */
	public boolean isOnePage() {
		List<Object> values = Database.getValues(QUIZZES, QUIZ_NAME, name, IS_ONE_PAGE);
		if (values == null) {
			throw new RuntimeException("Corrupt table status for " + QUIZZES + 
					" . Cannot find isOnePage field for " + name);
		}
		
		if (values.size() > 1) {
			throw new RuntimeException("Corrupt table status for " + QUIZZES + 
					" . Duplicate entries for " + name);
		}
		
		Util.validateObjectType(values.get(0), BOOLEAN);
		return (Boolean) values.get(0);
	}
	
	
	/**
	 * Sets onePage status for the quiz.
	 */
	public void setOnePage(boolean onePage) {
		int modified = Database.setValues(QUIZZES, QUIZ_NAME, name, IS_ONE_PAGE, onePage);
		
		if (modified == 0) {
			throw new RuntimeException("Corrupt table status for " + QUIZZES + 
					" . Cannot find description for " + name);
		}
		
		if (modified > 1) {
			throw new RuntimeException("Corrupt table status for " + QUIZZES + 
					" . Duplicate entries for " + name);
		}
	}
	
	
	/**
	 * @return whether or not the quiz is immediate.
	 */
	public boolean isImmediate() {
		List<Object> values = Database.getValues(QUIZZES, QUIZ_NAME, name, IS_IMMEDIATE);
		if (values == null) {
			throw new RuntimeException("Corrupt table status for " + QUIZZES + 
					" . Cannot find isImmediate field for " + name);
		}
		
		if (values.size() > 1) {
			throw new RuntimeException("Corrupt table status for " + QUIZZES + 
					" . Duplicate entries for " + name);
		}
		
		Util.validateObjectType(values.get(0), BOOLEAN);
		return (Boolean) values.get(0);
	}
	
	
	/**
	 * Sets immediate status for the quiz.
	 */
	public void setImmediate(boolean immediate) {
		int modified = Database.setValues(QUIZZES, QUIZ_NAME, name, IS_IMMEDIATE, immediate);
		
		if (modified == 0) {
			throw new RuntimeException("Corrupt table status for " + QUIZZES + 
					" . Cannot find description for " + name);
		}
		
		if (modified > 1) {
			throw new RuntimeException("Corrupt table status for " + QUIZZES + 
					" . Duplicate entries for " + name);
		}
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
		
		Database.removeRows(HISTORY, QUIZ_NAME, name);
		
		
		
		
		name = null;
	}
	
	
	/**
	 * Returns all question Objects.
	 */
	public List<Question> getQuestions() {
		List<Question> result = new ArrayList<Question>();
		Map<String, Question> questions = new LinkedHashMap<String, Question>();
		
		for (String questionType : QUESTION_TYPES) {
			// Get all rows for each question table for this quiz.
			List<Map<String, Object>> rows = Database.getRows(questionType, QUIZ_NAME, name);
			
			// Move on to next table if there are no questions here.
			if (rows == null) continue;
			
			for (Map<String, Object> row : rows) {
				String question = (String) row.get(QUESTION);
				
				if (questions.containsKey(question)) {
					addToQuestion(questions.get(question), row);
					
				} else {
					Question newQuestion = questionFactory(questionType, row);
					questions.put(question, newQuestion);
				}
			}
		}
		
		for (String question : questions.keySet()) {
			result.add(questions.get(question));
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
			if (questions == null) continue;
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
	public void addQuestion(Question question) {
		try {
			Util.validateObject(question);
			
			// No duplicate questions.
			if (getQuestionsAsStrings().contains(question.getQuestion())) {
				throw new IllegalArgumentException(question.getQuestion() + " already exists in " + name);
			}
			
			if (question instanceof Response) addResponse((Response) question);
			else if (question instanceof FillBlank) addFillBlank((FillBlank) question);
			else if (question instanceof MultipleChoice) addMultipleChoice((MultipleChoice) question);
			else if (question instanceof Picture) addPicture((Picture) question);
			else if (question instanceof MultiResponse) addMultiResponse((MultiResponse) question);
			else if (question instanceof Matching) addMatching((Matching) question);
			else {
				throw new IllegalArgumentException("Passed question is invalid");
			}
		
		} catch (Exception e) {
			this.removeQuiz();
			throw new RuntimeException("Unable to create quiz: " + e.getMessage());
		}
	}
	
	
	/**
	 * Edits a question from the old state to the new state.
	 * Takes the old state of a question before it was modified and the new state after being
	 * modified. Looks for the old state in the database and replaces it with the new one.
	 * @param oldQuestion the question object before modification
	 * @param newQuestion the question object after modification
	 */
	public void editQuestion(Question oldQuestion, Question newQuestion) {
		Util.validateObject(oldQuestion);
		Util.validateObject(newQuestion);
		
		// Ensure question belongs to this quiz.
		if (!oldQuestion.getQuizName().equals(name)) {
			throw new IllegalArgumentException("The question is not in this quiz.");
		}
		
		// Check type consistency
		if (oldQuestion.getClass() != newQuestion.getClass()) {
			throw new IllegalArgumentException("Both questions have to be the same type");
		}
		
//		// Quiz name cannot be modified in a question.
//		if (!oldQuestion.getQuizName().equals(newQuestion.getQuizName())) {
//			throw new IllegalArgumentException("The quiz name in a question cannot be modified");
//		}
		
		// If the question prompt was modified, ensure it is not duplicate.
		if (!oldQuestion.getQuestion().equals(newQuestion.getQuestion())) {
			if (getQuestionsAsStrings().contains(newQuestion.getQuestion())) {
				throw new IllegalArgumentException("The question already exists in" 
						+ newQuestion.getQuizName());
			}
		}
		
		// Remove old and add new.
		removeQuestion(oldQuestion);
		addQuestion(newQuestion);
	}
	
	
	/**
	 * Removes the passed question object from the Quiz.
	 */
	public void removeQuestion(Question question) {
		Util.validateObject(question);
		for (String tableName : QUESTION_TYPES) {
			Database.removeRows(tableName, QUIZ_NAME, name, QUESTION, question.getQuestion());
		}
	}
	
	
	/**
	 * Returns the top scorers for the quiz.
	 * @param numRecords number of top scorers to return. (all if numRecords = 0)
	 * @return a list of top scorers as Record Objects.
	 */
	public List<Record> getTopPerformers(int numRecords) {
		if (numRecords < 0) {
			throw new IllegalArgumentException(numRecords + " cannot be less than 0");
		}
		
		List<Record> result = new ArrayList<Record>();
		List<Map<String, Object>> rows = Database.getSortedRows(HISTORY, QUIZ_NAME, 
				name, SCORE, true, ELAPSED_TIME, false);
		
		if (rows == null) return result;
		
		// Get all records.
		if (numRecords == 0) {
			for (Map<String, Object> row : rows) {
				result.add(new Record(
						(String) row.get(QUIZ_NAME), 
						AccountManager.getAccount((String) row.get(USERNAME)),
						(Double) row.get(SCORE), (String) row.get(DATE), 
						(Double) row.get(ELAPSED_TIME)));
			}
			
		} else {
			for (int i = 0; i < Math.min(numRecords, rows.size()); i++) {
				Map<String, Object> row = rows.get(i);
				result.add(new Record(
						(String) row.get(QUIZ_NAME), 
						AccountManager.getAccount((String) row.get(USERNAME)),
						(Double) row.get(SCORE), (String) row.get(DATE), 
						(Double) row.get(ELAPSED_TIME)));
			}
		}
		return result;
	}
	
	
	/**
	 * TODO: client-side has to allow sorting this table by different fields.
	 * Given a user account, returns the user's past performance in this quiz.
	 * @param user to look records for
	 * @param numRecords desired number of entries to return (0 for all)
	 * @return a list of Record objects.
	 */
	public List<Record> getPastUserPerformance(Account user, int numRecords) {
		Util.validateObject(user);
		
		if (numRecords < 0) {
			throw new IllegalArgumentException(numRecords + " cannot be less than 0.");
		}
		
		List<Record> result = new ArrayList<Record>();
		
		List<Map<String, Object>> rows = Database.getRows(HISTORY, QUIZ_NAME, name, 
				USERNAME, user.getUserName());
		
		if (rows == null) return result;
		
		// Get all records.
		if (numRecords == 0) {
			for (Map<String, Object> row : rows) {
				result.add(new Record(
						(String) row.get(QUIZ_NAME), 
						AccountManager.getAccount((String) row.get(USERNAME)),
						(Double) row.get(SCORE), (String) row.get(DATE), 
						(Double) row.get(ELAPSED_TIME)));
			}
			
		} else {
			for (int i = 0; i < Math.min(numRecords, rows.size()); i++) {
				Map<String, Object> row = rows.get(i);
				result.add(new Record(
						(String) row.get(QUIZ_NAME), 
						AccountManager.getAccount((String) row.get(USERNAME)),
						(Double) row.get(SCORE), (String) row.get(DATE), 
						(Double) row.get(ELAPSED_TIME)));
			}
		}
		return result;
	}
	
	
	/**
	 * Returns the top scorers within the last specified time period.
	 * @param hours time period to get results for (3 for 3 hours, 0.25 for 15 minutes,
	 * 0 for all history)
	 * @return a list of Record objects.
	 */
	public List<Record> topScorersWithinTimePeriod(double hours) {
		if (hours < 0) throw new IllegalArgumentException(hours + " cannot be less than 0");
		if (hours > (365 * 24)) {
			throw new IllegalArgumentException("Easy now! The site doesn't go back that long");
		}
		
		List<Record> result = new ArrayList<Record>();
		
		long interval = (long) (hours * 60 * 60 * 1000);
		long timeCut = System.currentTimeMillis() - interval;
		Date date = new Date(timeCut);
		//-------------------------------------
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		String cut = format.format(date);
		
		List<Map<String, Object>> rows = Database.getSortedRowsWithComparison(HISTORY, DATE, 
				true, cut, SCORE, true);
		
		if (rows == null) return result;
		
		for (Map<String, Object> row : rows) {
			result.add(new Record(
					(String) row.get(QUIZ_NAME), 
					AccountManager.getAccount((String) row.get(USERNAME)),
					(Double) row.get(SCORE), (String) row.get(DATE), 
					(Double) row.get(ELAPSED_TIME)));
		}
		
		return result;
	}
	
	
	/**
	 * Returns the recent performance, good and bad, for this quiz.
	 * @param numRecords number of desired entries in the response.
	 * @return a list of Record objects.
	 */
	public List<Record> getRecentPerformance(int numRecords) {
		if (numRecords < 0) {
			throw new IllegalArgumentException(numRecords + " cannot be less than 0.");
		}
		
		List<Record> result = new ArrayList<Record>();
		
		List<Map<String, Object>> rows = Database.getSortedRows(HISTORY, QUIZ_NAME, name, 
				DATE, true);
		
		if (rows == null) return null;
		
		// Get all records.
		if (numRecords == 0) {
			for (Map<String, Object> row : rows) {
				result.add(new Record(
						(String) row.get(QUIZ_NAME), 
						AccountManager.getAccount((String) row.get(USERNAME)),
						(Double) row.get(SCORE), (String) row.get(DATE), 
						(Double) row.get(ELAPSED_TIME)));
			}
			
		} else {
			for (int i = 0; i < Math.min(numRecords, rows.size()); i++) {
				Map<String, Object> row = rows.get(i);
				result.add(new Record(
						(String) row.get(QUIZ_NAME), 
						AccountManager.getAccount((String) row.get(USERNAME)),
						(Double) row.get(SCORE), (String) row.get(DATE), 
						(Double) row.get(ELAPSED_TIME)));
			}
		}
		
		if (result.size() == 0) return null;
		return result;
	}
	
	
	/**
	 * Returns the overall average performance for the Quiz.
	 * @return a Map with keys "score" and "elapsed_time" providing such values.
	 */
	public Map<String, Double> getAveragePerformance() {
		Map<String, Double> result = new HashMap<String, Double>();
		List<Map<String, Object>> rows = Database.getTable(HISTORY);
		if (rows == null || rows.size() == 0) {
			result.put(SCORE, 0.0);
			result.put(ELAPSED_TIME, 0.0);
			return result;
		}
		
		double avgScore = 0;
		double avgElapsed = 0;
		
		for (Map<String, Object> row : rows) {
			avgScore += (Double) row.get(SCORE);
			avgElapsed += (Double) row.get(ELAPSED_TIME);
		}
		
		avgScore = avgScore / rows.size();
		avgElapsed = avgElapsed / rows.size();
		
		result.put(SCORE, avgScore);
		result.put(ELAPSED_TIME, avgElapsed);
		return result;
	}
	
	
	//--------------------------- Helper Methods -------------------------------//
	
	
	// Adds a Response question to the database.
	private void addResponse(Response response) {
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
		// Add a row for every answer in the question.
		for (String left : matching.getMatches().keySet()) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put(QUIZ_NAME, name);
			row.put(QUESTION, matching.getQuestion());
			row.put(LEFT, left);
			row.put(RIGHT, matching.getMatches().get(left));
			
			// If row already exists, do nothing.
			if (Database.getRows(MATCHING, row) != null) {
				return;
			}
			
			Database.addRow(MATCHING, row);
		}
	}
	
	
	/*
	 * Given a question type and a database row in the form of a Map<String, Object>,
	 * returns a Question Object.
	 */
	private Question questionFactory(String questionType, Map<String, Object> row) {
		Util.validateString(questionType);
		Util.validateObject(row);
		
		String question = (String) row.get(QUESTION);
		
		if (questionType.equals(RESPONSE)) {
			List<String> answers = new ArrayList<String>();
			answers.add((String) row.get(ANSWER));
			return new Response(name, question, answers);
			
		} else if (questionType.equals(FILL_BLANK)) {
			List<String> answers = new ArrayList<String>();
			answers.add((String) row.get(ANSWER));
			Map<String, List<String>> blanksAndAnswers = new HashMap<String, List<String>>();
			blanksAndAnswers.put((String) row.get(BLANK), answers);
			return new FillBlank(name, question, blanksAndAnswers);
			
		} else if (questionType.equals(MULTIPLE_CHOICE)) {
			Map<String, Boolean> options = new HashMap<String, Boolean>();
			options.put((String) row.get(OPTION), (Boolean) row.get(IS_ANSWER));
			return new MultipleChoice(name, question, options);
		
		} else if (questionType.equals(PICTURE)) {
			List<String> answers = new ArrayList<String>();
			answers.add((String) row.get(ANSWER));
			return new Picture(name, question, (String) row.get(PICTURE_URL), answers);
			
		} else if (questionType.equals(MULTI_RESPONSE)) {
			List<String> answers = new ArrayList<String>();
			answers.add((String) row.get(ANSWER));
			boolean isOrdered = (Boolean) row.get(IS_ORDERED);
			return new MultiResponse(name, question, answers, isOrdered);
			
		} else if (questionType.equals(MATCHING)) {
			Map<String, String> matches = new HashMap<String, String>();
			matches.put((String) row.get(LEFT), (String) row.get(RIGHT));
			return new Matching(name, question, matches);
		}
		
		throw new IllegalArgumentException(questionType + " is not a valid question type.");
	}
	
	
	/*
	 * Adds the passed row to the passed Question object.
	 */
	private void addToQuestion(Question question, Map<String, Object> row) {
		if (question instanceof Response) {
			Response response = (Response) question;
			response.addAnswer((String) row.get(ANSWER));
		 	
		} else if (question instanceof FillBlank) {
			FillBlank fillBlank = (FillBlank) question;
			fillBlank.addBlank((String) row.get(BLANK), (String) row.get(ANSWER));
			
		} else if (question instanceof MultipleChoice) {
			MultipleChoice multChoice = (MultipleChoice) question;
			multChoice.addOption((String) row.get(OPTION), (Boolean) row.get(IS_ANSWER));
			
		} else if (question instanceof Picture) {
			Picture picture = (Picture) question;
			picture.addAnswer((String) row.get(ANSWER));
			
		} else if (question instanceof MultiResponse) {
			MultiResponse multResponse = (MultiResponse) question;
			multResponse.addAnswer((String) row.get(ANSWER));
			
		} else if (question instanceof Matching) {
			Matching matching = (Matching) question;
			matching.addMatch((String) row.get(LEFT), (String) row.get(RIGHT));
		
		} else {
			throw new IllegalArgumentException("Invalid question object.");
		}
	}
	
	
	/**
	 * @return a JSON representation of the Quiz.
	 */
	public Map<String, Object> toMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		
		// Quiz Meta-data
		Map<String, Object> quizMetadata = new HashMap<String, Object>();
		quizMetadata.put(QUIZ_NAME, name);
		quizMetadata.put(CREATOR, getCreator().getUserName());
		quizMetadata.put(DESCRIPTION, getDescription());
		quizMetadata.put(DATE_CREATED, getCreationDate());
		quizMetadata.put(IS_RANDOM, isRandom());
		quizMetadata.put(IS_ONE_PAGE, isOnePage());
		quizMetadata.put(IS_IMMEDIATE, isImmediate());
		result.put(QUIZ_METADATA, quizMetadata);
		
		// Questions
		List<Map<String, Object>> questionList = new ArrayList<Map<String, Object>>();
		List<Question> questions = getQuestions();
		for (Question question : questions) {
			questionList.add(question.toMap());
		}
		
		if (isRandom()) {
			Collections.shuffle(questionList);
		}
		result.put(QUESTIONS, questionList);
		return result;
	}
}
