package com.quizzes.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.accounts.Account;
import com.accounts.AccountManager;
import com.quizzes.*;
import com.util.Constants;
import java.text.DecimalFormat;
/**
 * Servlet implementation class HomepageQuizIndexServlet
 */
@WebServlet("/HomepageQuizIndexServlet")
public class HomepageQuizIndexServlet extends HttpServlet implements Constants {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomepageQuizIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");	

		String type_to_display = (String)request.getParameter("type_to_display");
		String username = (String)request.getSession().getAttribute("session_user");
		
		List<String> result_list = new ArrayList<String>();
		int num_records = 5;
		/*Default num records to put in the result_list*/
		
		/*Field for User Achievements*/
		if (type_to_display.equals("allQuizzes")) {
			try { 
				List<Quiz> quizzes = QuizManager.getAllQuizzes();
				if (quizzes != null) {
					int num_quizzes = quizzes.size();
					/*Iterate through usr_quizzes backwards so that we have newest quiz first */					
					for (int i = num_quizzes - 1; i >= 0;i--) {
						Quiz curr = quizzes.get(i);
						String quizName = curr.getName();
						String desc = curr.getDescription();
						String creator = curr.getCreator().getUserName();
						String str = "</td><td class = \"homepage-content-headers\">Quiz:</td><td><a href = \"quizSummary.jsp?"+QUIZ_NAME+"="+quizName+"\">"+quizName+"</a></td><td class = \"homepage-content-headers\">Created By:</td><td>"+creator+"</td><td></td><td class = \"homepage-content-headers\">Description:</td><td>"+desc+"</td><td>" ;
						result_list.add(str);
					}
					request.setAttribute("content_to_display",result_list);		
				} else {
					result_list.add("There are no quizzes.");
					request.setAttribute("content_to_display",result_list);

				}
			} catch (Exception e) {
				request.setAttribute("errMsg", "<h1> The query returned the error: "+e.getMessage()+" .</h1>");
			}
							
			/*Field for Friends' Activities*/
		} else if (type_to_display.equals("friendActivities")) {
			try {
				Account account = AccountManager.getAccount(username);
				
				List<Activity> acts = account.getRecentFriendActivity(num_records);

				/*If acts has some entry*/
				if (acts != null ) {

					int num_acts = acts.size();

					for (int i = 0; i < num_acts;i++) {
						Activity curr = acts.get(i);
						String activity = curr.getActivity();
						String quizName = curr.getQuizName();
						String date = curr.getDate();
						String user = curr.getUser().getUserName();
						String str;
						if (QuizManager.quizNameInUse(quizName)) {
							str = user+" "+activity+": <a href = \"quizSummary.jsp?"+QUIZ_NAME+"="+quizName+"\">"+quizName+"</a> on "+date;

						} else {
							str = user+" "+activity+": "+quizName+"on "+date;

						}
						result_list.add(str);
					}

						request.setAttribute("content_to_display", result_list);
						
						/*If acts has no entries*/
				} else {
		    		result_list.add("<h1>There are no recent activities.</h1>");
					request.setAttribute("content_to_display",result_list);
		    	}			
		    } catch (Exception e) {
				request.setAttribute("errMsg", "<h1> The query returned the error: "+e.getMessage()+" .</h1>");
			}
			/*My recently created quizzes*/
		} else if (type_to_display.equals("createdQuizzes")) {
			
				List<Quiz> usr_quizzes = QuizManager.getQuizzes(AccountManager.getAccount(username));
				/*If usr_quizzes is not null then we know that the size is at least one*/
				if (usr_quizzes != null ) {
					int num_quizzes = usr_quizzes.size();
					int counter = 0;
					/*Iterate through usr_quizzes backwards so that we have newest quiz first */
					for (int i = num_quizzes - 1; i >= 0;i--) {
						Quiz curr = usr_quizzes.get(i);
						String quizName = curr.getName();
						String str = username+", you created <a href = \"quizSummary.jsp?"+QUIZ_NAME+"="+quizName+"\">"+quizName;
						result_list.add(str);
						counter++;
						//if (counter == num_records) i = -1;
					}
						request.setAttribute("content_to_display",result_list);
					
				} else {
					result_list.add("You haven't created any new quizzes");
					request.setAttribute("content_to_display",result_list);
				}			
			/*All popular quizzes*/
		} else if  (type_to_display.equals("popularQuizzes")) {
			//System.out.println("popular quizzes");
			try {
				List<Quiz> popular_quizzes =  QuizManager.getMostPopularQuizzes(num_records);
			//	System.out.println(popular_quizzes.size());
	
				/*Construct a brief message for each popular quiz*/
				int pop_quizzes_len = popular_quizzes.size();
				for (int i = 0 ; i < pop_quizzes_len;i++) {
					Quiz curr = popular_quizzes.get(i);
					Map<String, Double> perf_map = curr.getAveragePerformance();
					DecimalFormat formatter = new DecimalFormat("0.00");
					double avg_score = perf_map.get("score");
					double elapsed_time = perf_map.get("elapsed_time");	
					String quizName =curr.getName();
					String to_insert = "The popular quiz " + "<a href = \"quizSummary.jsp?"+QUIZ_NAME+"="+quizName+"\">"+quizName+"</a>" + "  has an average score of "+formatter.format(avg_score)+" and average time taken of "+formatter.format(elapsed_time)+" mins.";
					result_list.add(to_insert);
					if ( i == num_records - 1) i = pop_quizzes_len;
				}
				/* Case where there are no popular quizzes in database */
				if (pop_quizzes_len > 0 ) {
					request.setAttribute("content_to_display",result_list);
				} else {
					result_list.add("There are no popular quizzes at this time.");
					request.setAttribute("content_to_display",result_list);
				}
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("errMsg", "<h1> The query returned the error: "+e.getMessage()+" .</h1>");

				//request.setAttribute("errMsg", "<h1>There are no popular quizzes at this time.</h1>");
			}
			/*All recently created quizzes*/
		} else if ( type_to_display.equals("recentQuizzes")) {
			try {
				List<Quiz> recent_quizzes =  QuizManager.getRecentlyCreatedQuizzes(num_records);
				int rec_quiz_len = recent_quizzes.size();
				/*For each Recent Quiz print the quiz data and a link to the quiz Summary page*/

				for (int i = 0; i < rec_quiz_len;i++ ) {
					Quiz curr = recent_quizzes.get(i);
					String creator = curr.getCreator().getUserName();
					String birthdate = curr.getCreationDate();
					String quizname = curr.getName();
					result_list.add("The quiz <a href = \"quizSummary.jsp?"+QUIZ_NAME+"="+quizname+"\">"+quizname+"</a> was created by "+creator+" on "+birthdate);
				}
				if (result_list.size() > 0) {
				//	System.out.println("This is the recently created quizzes "+result_list);
					request.setAttribute("content_to_display",result_list);
				} else {

					result_list.add(username+", you don't have any quizzes taken on record.");
					request.setAttribute("content_to_display",result_list);

				}
		
			}catch (Exception e) {
				request.setAttribute("errMsg", "<h1> The query returned the error: "+e.getMessage()+" .</h1>");

				//request.setAttribute("errMsg", "<h1>There are no recently created Quizzes.</h1>");
			}
		
			/*My Recently Taken Quizzes */
		} else if (type_to_display.equals("myTakenQuizzes")) {
			try {
				Account acct = AccountManager.getAccount(username);
				List<Record> quizzes_taken = acct.getPastPerformance(num_records);
				int quizzes_taken_len = quizzes_taken.size();
				for (int i = quizzes_taken_len - 1;i >= 0;i--) {
					Record rec = quizzes_taken.get(i);
					String quizName = rec.getQuizName();
					String date = rec.getDate();
					double score = rec.getScore();
					String to_insert = "The quiz <a href = \"quizSummary.jsp?"+QUIZ_NAME+"="+quizName+"\">"+quizName+"</a> was taken on "+date+" with a score of "+score;
					result_list.add(to_insert);
				}
				/*The case that no record found*/
				if (result_list.size() > 0) {
					request.setAttribute("content_to_display",result_list);
				} else {
					result_list.add(username+", you don't have any quizzes taken on record.");
					request.setAttribute("content_to_display",result_list);
				}
			} catch (Exception e) {
				/*The case that an exception is thrown. */
				request.setAttribute("errMsg", "<h1> The query returned the error: "+e.getMessage()+" .</h1>");

				//request.setAttribute("errMsg", "<h1>You have not taken any quizzes lately..</h1>");
			}
			
		} else {
			request.setAttribute("errMsg", "<h1> Sorry we could not process your request at this time</h1>");
		}
		
		RequestDispatcher dispatch = request.getRequestDispatcher("homepage.jsp"); 
		dispatch.forward(request, response);	

	}

}
