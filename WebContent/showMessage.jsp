<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ page import="com.accounts.Account, com.accounts.AccountManager, com.accounts.Message, com.util.Constants, java.util.*, java.sql.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="css//main.css" ></link>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Insert title here</title>
</head>
<body>
	<%
		String name = (String)request.getParameter("id");
	%>
	<h1>Here are your messages, <%=name%></h1>
	<ul>
		<%
		
			// Check which messages to receive based on the Select tag in the homepage.jsp
			Account account = AccountManager.getAccount(name);
			List<Message> messages;
			String msgToDisplay = request.getParameter("choice");
			System.out.println(msgToDisplay);
			
			// ????????????
			/* Message msg1 = new Message("Guy", "Guy", "", Constants.MESSAGE_FRIEND_REQUEST, "date", false);
			Message msg2 = new Message("Guy3", "Guy4", "Hi Guy!", Constants.MESSAGE_NOTE, "date", false);
			messages = new ArrayList<Message>();
			messages.add(0, msg1);
			messages.add(1, msg2); */
			
			if (msgToDisplay.equals("Received Messages")) { 
				messages = account.getReceivedMessages();		
			} else {
				messages = account.getSentMessages();		
			}
			
			// Print messages
			for (Message msg : messages) {
				//request.setAttribute("message", msg);
				//String sender = (String) request.getAttribute("sender");
				if (msg.getType().equals(Constants.MESSAGE_FRIEND_REQUEST)) {
					if (!msg.isRead()) {
						out.println("<form action=\"MessageServlet\" method=\"post\">"); 
						out.print("<li><p>" + msg.getSender() + " sent you a friend request!</p>");
						out.println("<input type=\"submit\" value=\"Confirm\", name=\"message_button\"> <input type=\"submit\" value=\"Decline\", name=\"message_button\">");
						out.println("<input name=\"sender\" " + "type=\"hidden\" value=\"" + msg.getSender() + "\"/>");
						out.println("<input name=\"recipient\" " + "type=\"hidden\" value=\"" + msg.getRecipient() + "\"/>");
						out.println("</form>");
						
						if (request.getAttribute("isAdded") != null) {
							if (((String)request.getAttribute("isAdded")).equals("added")) {
								out.println("<p> Friend Added! </p>");
							} else if (((String)request.getAttribute("isAdded")).equals("already_friends")) {
								out.println("<p> Already Friends! </p>");
							} 
						}
						account.readMessage(msg); // uncomment!
					}
				} else if (msg.getType().equals(Constants.MESSAGE_CHALLENGE)) {
					// go to challenge page
				} else if (msg.getType().equals(Constants.MESSAGE_NOTE)) {
					out.println("<form action=\"MessageServlet\" method=\"post\">");
					out.print("<li><p>" + msg.getSender() + " sent you a message!</p>");
					out.println("<input type=\"submit\" value=\"Read Message\", name=\"message_button\">");
					out.println("<input name=\"message_content\" " + "type=\"hidden\" value=\"" + msg.getContent() + "\"/>");
					out.println("<input name=\"sender\" " + "type=\"hidden\" value=\"" + msg.getSender() + "\"/>");
					out.println("</form>");
				}	
			} 
		%>
	</ul>	
</body>
</html>