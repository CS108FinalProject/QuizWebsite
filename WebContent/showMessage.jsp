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
		String name;
		if (request.getParameter("id") != null) {
			name = request.getParameter("id");
		} else {
			name = (String) request.getAttribute("username");
		}
	%>
	
	<table id="header">
			<tr>
				<%/*if(acct.isAdmin()) {
					out.println("<th<a href = \"adminHomepage.jsp\">Homepage</a></th>");
				} else {
					out.println("<th<a href = \"homepage.jsp\">Homepage</a></th>");
				}
					*/%> 
					
				<th><a href = <%="\"homepage.jsp?id="+ name +"\""%>>Homepage</a></th>
				<th><a href = "showAnnouncements.jsp">Announcements</a></th>
				<th>My Achievements</th>

				<% //System.out.println( "In Homepage: " + name); %>
          		<th> <a href="searchFriends.jsp?id=<%=name%>"> Find Friends</a> </th>


				<th>My Messages 
					<form action = <%="\"showMessage.jsp?id="+name+"\""%>>					
						<select name = "choice">
							<option>Received Messages</option>
							<option>Sent Messages</option>
							<option>Send A Message</option>
						</select>
						<input name="choice" type="hidden" value=<%=(String)request.getParameter("choice")%>>
						<input name="id" type="hidden" value=<%=name%>>
						<input type = "submit" value = "Go">
					</form>
				</th>
				<th>Quizzes</th>
			</tr>
	</table>
	
	<h1>Here are your messages, <%=name%></h1>
	<ul>
		<%
		
			// Check which messages to receive based on the Select tag in the homepage.jsp
			Account account = AccountManager.getAccount(name);
			List<Message> messages;
			String msgToDisplay = request.getParameter("choice");
			
			// Get messages
			if (msgToDisplay.equals("Received Messages")) { 
				messages = account.getReceivedMessages();		
			} else {
				messages = account.getSentMessages();		
			}
			
			// Print messages
			for (Message msg : messages) {
				if (msg.getType().equals(Constants.MESSAGE_FRIEND_REQUEST)) { // case of friend request
					if (!msg.isRead()) { // case of read message
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
						account.readMessage(msg); 
					} else { // case of unread message
						if (account.isFriend(AccountManager.getAccount(msg.getSender()))) {
							out.println("<li><p> You accepted <a href=\"accountProfile.jsp?friend_id=" + msg.getSender() 
									+ "&username=" + msg.getRecipient() + "\">" + msg.getSender() + "</a>'s friend request</p>");
							
						} else {
							out.println("<li><p> You declined <a href=\"accountProfile.jsp?friend_id=" + msg.getSender() 
									+ "&username=" + msg.getRecipient() + "\">" + msg.getSender() + "</a>'s friend request</p>");
						}	
					}
				} else if (msg.getType().equals(Constants.MESSAGE_CHALLENGE)) { // case of challenge
					// go to challenge page
				} else if (msg.getType().equals(Constants.MESSAGE_NOTE)) { // case of note
					out.println("<form action=\"MessageServlet\" method=\"post\">");
					out.print("<li><p><a href=\"accountProfile.jsp?friend_id=" + msg.getSender() 
							+ "&username=" + msg.getRecipient() + "\">" + msg.getSender() + "</a> sent you a message!</p>");
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
