<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ page import="com.accounts.Account, com.accounts.AccountManager, com.accounts.Message, com.util.Constants, java.util.*, java.sql.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Insert title here</title>
</head>
<body>
	<%
		//String name = (String)getServletContext().getAttribute("accounts");
		String name = (String)request.getParameter("id");
	%>
	<h1>Here are your messages, <%=name%></h1>
	<ul>
		<%

			/*
				Check which messages to receive based on the Select tag in the homepage.jsp
				
				String msg_type = request.getParameter("choice");
				ArrayList<Message> messages = account.getReceivedMessages();
				if (msgToDisplay.equals("Inbox")) { 
					messages = acct.getReceivedMessages();		
				} else {
					messages = acct.getSentMessages();		
				}
			*/
			Account account = AccountManager.getAccount(name);
				//This type has been changed to ArrayList
			List<Message> messages = account.getReceivedMessages();
			for (Message msg : messages) {
				request.setAttribute("message", msg);
				if (msg.getType().equals(Constants.MESSAGE_FRIEND_REQUEST)) {
					if (!msg.isRead()) {
						out.println("<form action=\"MessageServlet\" method=\"post\">"); 
						out.println("<li><p>" + msg.getSender() + "sent you a friend request!</p>");
						out.println("<input type=\"submit\" value=\"Confirm\", name=\"message_button\"> <input type=\"submit\" value=\"Decline\", name=\"message_button\">");
						//out.println("<input name=\"sender\" " + "type=\"hidden\" value=\"" + msg.getSender() + "\"/>");
						out.println("</form>");
						account.readMessage(msg);
					}
				} else if (msg.getType().equals(Constants.MESSAGE_CHALLENGE)) {
					// go to challenge page
				} else if (msg.getType().equals(Constants.MESSAGE_NOTE)) {
					out.println("<form action=\"MessageServlet\" method=\"post\">");
					out.println("<li><p>" + msg.getSender() + " sent you a message!</p>");
					out.println("<input type=\"submit\" value=\"ReadMessage\", name=\"message_button\">");
					//out.println("<input name=\"message_content\" " + "type=\"hidden\" value=\"" + msg.getContent() + "\"/>");
					//out.println("<input name=\"sender\" " + "type=\"hidden\" value=\"" + msg.getSender() + "\"/>");
					out.println("</form>");
				}	
			} 
		%>
	</ul>	
</body>
</html>