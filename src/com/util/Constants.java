package com.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Holds the constants that are used throughout the project.
 * @author Sam
 *
 */
public interface Constants {
	
	// Accounts table
	public static final String ACCOUNTS = "Accounts";
	public static final String USERNAME = "userName";
	public static final String PASSWORD = "password";
	public static final String IS_ADMIN = "isAdmin";

	// Friends table
	public static final String FRIENDS = "Friends";
	public static final String STATUS = "status";
	public static final String FRIEND = "friend";
	public static final String PENDING = "pending";
	
	// Messages table
	public static final String MESSAGES = "Messages";
	public static final String SENDER = "sender";
	public static final String RECIPIENT = "recipient";
	public static final String CONTENT = "content";
	public static final String TYPE = "type";
	public static final String DATE = "date";
	public static final String SEEN = "seen";
	
	// Quizzes table
	public static final String QUIZZES = "Quizzes";
	public static final String QUIZ_NAME = "quiz_name";
	public static final String CREATOR = "creator";
	public static final String DESCRIPTION = "description";
	public static final String DATE_CREATED = "date_created";
	public static final String IS_RANDOM = "is_random";
	public static final String IS_ONE_PAGE = "is_one_page";
	public static final String IS_IMMEDIATE = "is_immediate";
	
	// History table
	public static final String HISTORY = "History";
	public static final String SCORE = "score";
	public static final String ELAPSED_TIME = "elapsed_time";
	
	// Response table
	public static final String RESPONSE = "Response";
	public static final String QUESTION = "question";
	public static final String ANSWER = "answer";
	
	// Fill_Blank table
	public static final String FILL_BLANK = "Fill_Blank";
	public static final String BLANK = "blank";
	
	// Multiple_Choice table
	public static final String MULTIPLE_CHOICE = "Multiple_Choice";
	public static final String OPTION = "option_db";
	public static final String IS_ANSWER = "is_answer";
	
	// Picture table
	public static final String PICTURE = "Picture";
	public static final String PICTURE_URL = "picture_url";
	
	// Multi_Response table
	public static final String MULTI_RESPONSE = "Multi_Response";
	public static final String IS_ORDERED = "is_ordered";
	public static final String ORDER = "order_db";
	
	//Matching table
	public static final String MATCHING = "Matching";
	public static final String LEFT = "left_db";
	public static final String RIGHT = "right_db";
	
	// Question Types list
	public static final List<String> QUESTION_TYPES = new ArrayList<String>(
			Arrays.asList(RESPONSE, FILL_BLANK, MULTIPLE_CHOICE, PICTURE, 
					MULTI_RESPONSE, MATCHING));
	
	// Record
	public static final double MIN_SCORE = 0.0;
	public static final double MAX_SCORE = 100.0;
	
	// Types
	public static final String STRING = "string"; 
	public static final String BOOLEAN = "boolean";
	public static final String INT = "integer";
	public static final String DOUBLE = "double";
	public static final String LONG = "long";
	
	// Columns table
	public static final String DB_TYPE = "Type";

	// Message Types
	public static final String MESSAGE_FRIEND_REQUEST = "friend_request";
	public static final String MESSAGE_CHALLENGE = "challenge";
	public static final String MESSAGE_NOTE = "note";
	
	// Database data types
	public static final String DB_STRING = "CHAR(64)";
	public static final String DB_INT = "INT(4)";
	public static final String DB_BOOLEAN = "TINYINT";
	public static final String DB_DOUBLE = "DOUBLE";
	public static final String DB_LONG = "BIGINT(8)";
	
	// Database 'Raw' types
	public static final String DB_RAW_STRING = "char(64)";
	public static final String DB_RAW_DOUBLE = "double";
	public static final String DB_RAW_INT = "int(4)";
	public static final String DB_RAW_LONG = "bigint(8)";
	public static final String DB_RAW_BOOLEAN = "tinyint(4)";
	
	// Json.
	public static final String JSON = "json";
	public static final String SUCCESS = "success";
	public static final String ERROR_MESSAGE = "errorMessage";
	public static final String QUESTIONS = "questions";
	public static final String ANSWERS = "answers";
	public static final String BLANKS_AND_ANSWERS = "blanksAndAnswers";
	public static final String MATCHES = "matches";
	
	// Request types
	public static final String REQUEST = "request";
	public static final String QUIZ = "quiz";
	public static final String ALL_QUIZZES = "allQuizzes";
	public static final String ALL_CREATOR_QUIZZES = "allCreatorQuizzes";
	public static final String MOST_POPULAR_QUIZZES = "mostPopularQuizzes";
	public static final String RECENTLY_CREATED_QUIZZES = "recentlyCreatedQuizzes";
	public static final String PAST_USER_PERFORMANCE = "pastUserPerformance";
	public static final String NUM_RECORDS = "numRecords";
	public static final String DATA = "data";
	public static final String TOP_PERFORMERS = "topPerformers";
	public static final String TOP_PERFORMERS_WITHIN_TIME_PERIOD = "topPerformersWithinTimePeriod";
	public static final String HOURS = "hours";
	public static final String RECENT_PERFORMANCE = "recentPerformance";
	public static final String AVG_PERFORMANCE = "avgPerformance";
	
}
