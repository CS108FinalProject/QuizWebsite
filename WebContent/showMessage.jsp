<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ page import="com.accounts.Account, com.accounts.AccountManager, com.accounts.Message, com.util.Constants, java.util.*, java.sql.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="css//style.css" ></link>
</head>
<style>
table, th, td {
    border: 1px solid black;
    text-align: center;
}

th {
    background-color: #34495E;
    color: white;
}
</style>
<body>
	<%
		String name;
		if (request.getParameter("id") != null) {
			name = request.getParameter("id");
		} else {
			name = (String) request.getAttribute("username");
		}
	%>
<header>	
	<table id="header">
			<tr>
				<%
				Account acct = AccountManager.getAccount(name);
				if(acct.isAdmin()) {
					out.println("<th class = \"btn\"><a href = \"adminHomepage.jsp\">Homepage</a></th>");
				} else {
					out.println("<th class = \"btn\"><a href = \"homepage.jsp\">Homepage</a></th>");
				}
				%> 			
					
				<th class = "btn"><a href = "showAnnouncements.jsp">Announcements</a></th>
				<th class = "btn"><a href = "showAchievements.jsp">My Achievements</a></th>

				<th class = "btn">My Messages 
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
				<th class = "btn"> <a href="searchFriends.jsp?id=<%=name%>"> Lookup Users</a> </th>
				<th class = "btn"><a href = <%="\"quizHome.html?user="+name+"\""%>> Quizzes</a></th>
			</tr>
	</table>
</header>	
<br><br>
	<h1>Here are your messages, <%=name%></h1>
	<br>
	<table>
		<tr>
			<th>Date</th>
			<th>Message</th>
			<th>Action</th>
		</tr>
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
				out.println("<tr>");
				out.println("<td>"+msg.getDate()+"</td>");
				request.setAttribute("message", msg);
				if (msg.getType().equals(Constants.MESSAGE_FRIEND_REQUEST)) { // case of friend request
					if (!msg.isRead()) { // case of unread message
						if (msgToDisplay.equals("Received Messages")) { 
							out.println("<form action=\"MessageServlet\" method=\"post\">"); 
							out.print("<td>" + msg.getSender() + " sent you a friend request!" + "</td>");
							out.println("<input name=\"sender\" " + "type=\"hidden\" value=\"" + msg.getSender() + "\"/>");
							out.println("<input name=\"recipient\" " + "type=\"hidden\" value=\"" + msg.getRecipient() + "\"/>");
							out.println("<td><input type=\"submit\" value=\"Confirm\" name=\"message_button\"> <input type=\"submit\" value=\"Decline\" name=\"message_button\"></td>");
							out.println("</form>");
						} else {
							out.println("<form action=\"MessageServlet\" method=\"post\">"); 
							out.print("<td>You sent " + msg.getRecipient() + "</a> a friend request!</td>");
							out.println("<input name=\"sender\" " + "type=\"hidden\" value=\"" + msg.getSender() + "\"/>");
							out.println("<input name=\"recipient\" " + "type=\"hidden\" value=\"" + msg.getRecipient() + "\"/>");
							out.println("<td><input type=\"submit\" value=\"Confirm\" name=\"message_button\"> <input type=\"submit\" value=\"Decline\" name=\"message_button\"></td>");
							out.println("</form>");
						}
						
					} else { // case of read message
						Account sender_account = AccountManager.getAccount(msg.getSender());
						Account recipient_account = AccountManager.getAccount(msg.getRecipient());
						if (recipient_account.isFriend(sender_account) || sender_account.isFriend(recipient_account)) {
							if (msgToDisplay.equals("Received Messages")) { 
								out.println("<td>You accepted <a href=\"accountProfile.jsp?friend_id=" + msg.getSender() 
										+ "&username=" + msg.getRecipient() + "\">" + msg.getSender() + "</a>'s friend request</td>");
							} else {
								out.println("<td><a href=\"accountProfile.jsp?friend_id=" + msg.getSender() 
										+ "&username=" + msg.getRecipient() + "\">" + msg.getRecipient() + "</a> accepted your friend request<td>");
							}
							
						} else {
							if (msgToDisplay.equals("Received Messages")) { 
								out.println("<td>You declined <a href=\"accountProfile.jsp?friend_id=" + msg.getSender() 
										+ "&username=" + msg.getRecipient() + "\">" + msg.getSender() + "</a>'s friend request</td>");
							} else {
								out.println("<td><a href=\"accountProfile.jsp?friend_id=" + msg.getSender() 
										+ "&username=" + msg.getRecipient() + "\">" + msg.getRecipient() + "</a> declined your friend request</td>");
							}
							
						}	
					}
				} else if (msg.getType().equals(Constants.MESSAGE_CHALLENGE)) { // case of challenge
					if (msgToDisplay.equals("Received Messages")) {
						out.println("<td><a href=\"accountProfile.jsp?friend_id=" + msg.getSender() 
								+ "&username=" + msg.getRecipient() + "\">" + msg.getSender() + 
								"</a> sent you a challenge for <a href=\"quizHome.html?user=" + name + "&quiz=" + msg.getContent() + "\"> quiz" + "</a></td>");
					} else {
						out.print("<td>You sent <a href=\"accountProfile.jsp?friend_id=" + msg.getRecipient() 
								+ "&username=" + msg.getSender() + "\">" + msg.getRecipient() + 
								"</a> a challenge for <a href=\"quizHome.html?user=" + name + "&quiz=" + msg.getContent() + "\"> quiz" + "</a></td>");
						}
				} else if (msg.getType().equals(Constants.MESSAGE_NOTE)) { // case of note
					if (msgToDisplay.equals("Received Messages")) { 
						out.println("<form action=\"MessageServlet\" method=\"post\">");
						out.println("<td><a href=\"accountProfile.jsp?friend_id=" + msg.getSender() 
								+ "&username=" + msg.getRecipient() + "\">" + msg.getSender() + "</a> sent you a message!</td>");
						out.println("<td><input type=\"submit\" value=\"Read Message\", name=\"message_button\"></td>");
						out.println("<input name=\"message_content\" " + "type=\"hidden\" value=\"" + msg.getContent() + "\"/>");
						out.println("<input name=\"sender\" " + "type=\"hidden\" value=\"" + msg.getSender() + "\"/>");
						out.println("<input name=\"recipient\" " + "type=\"hidden\" value=\"" + msg.getRecipient() + "\"/>");
						out.println("</form>");
					} else {
						out.println("<form action=\"MessageServlet\" method=\"post\">");
						out.println("<td>You sent <a href=\"accountProfile.jsp?friend_id=" + msg.getRecipient() 
								+ "&username=" + msg.getSender() + "\">" + msg.getRecipient() + "</a> a message!</td>");
						out.println("<td><input type=\"submit\" value=\"Read Message\", name=\"message_button\"></td>");
						out.println("<input name=\"message_content\" " + "type=\"hidden\" value=\"" + msg.getContent() + "\"/>");
						out.println("<input name=\"sender\" " + "type=\"hidden\" value=\"" + msg.getSender() + "\"/>");
						out.println("<input name=\"recipient\" " + "type=\"hidden\" value=\"" + msg.getRecipient() + "\"/>");
						out.println("</form>");
					}	
				}
				out.println("</tr>");
			}
			out.println("</table>");
			
		%>
	</ul>	
</body>
</html>
