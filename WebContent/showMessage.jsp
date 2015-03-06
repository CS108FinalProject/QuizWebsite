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

				<th>My Messages 
					<form action = <%="\"showMessage.jsp?id="+name+"\""%>>					
						<select name = "choice">
							<option>Received Messages</option>
							<option>Sent Messages</option>
						</select>
						<input name="choice" type="hidden" value=<%=(String)request.getParameter("choice")%>>
						<input name="id" type="hidden" value=<%=name%>>
						<input type = "submit" value = "Go">
					</form>
				</th>
				<th> <a href="searchFriends.jsp?id=<%=name%>"> Lookup Users</a> </th>
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
			if (msgToDisplay == null) msgToDisplay = "Received Messages"; // redirected from MessageServlet
			
			// Get messages
			if (msgToDisplay.equals("Received Messages")) { 
				messages = account.getReceivedMessages();		
			} else {
				messages = account.getSentMessages();		
			}
			
			// Print messages
			for (Message msg : messages) {
				//Account sender = AccountManager.getAccount(msg.getSender());
				//Account recipient = AccountManager.getAccount(msg.getRecipient());
				//if (request.getAttribute("message_read") != null) account.readMessage(msg);
				request.setAttribute("message", msg);
				if (msg.getType().equals(Constants.MESSAGE_FRIEND_REQUEST)) { // case of friend request
					if (!msg.isRead()) { // case of unread message
						out.println("<form action=\"MessageServlet\" method=\"post\">"); 
						out.print("<li><p>" + msg.getSender() + " sent you a friend request!</p>");
						out.println("<input name=\"sender\" " + "type=\"hidden\" value=\"" + msg.getSender() + "\"/>");
						out.println("<input name=\"recipient\" " + "type=\"hidden\" value=\"" + msg.getRecipient() + "\"/>");
						out.println("<input type=\"submit\" value=\"Confirm\" name=\"message_button\"> <input type=\"submit\" value=\"Decline\" name=\"message_button\">");
						out.println("</form>");
						//if () account.readMessage(msg); 
					} else { // case of read message
						if (account.isFriend(AccountManager.getAccount(msg.getSender())) || AccountManager.getAccount(msg.getSender()).isFriend(account)) {
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
