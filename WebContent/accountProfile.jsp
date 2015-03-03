<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="css//main.css" ></link>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Insert title here</title>
</head>
<body>
	
	<%
		String sender_name =  (String) getServletContext().getAttribute("session_user");
		String friend_name;
		if (request.getParameter("friend_id") != null) {
			friend_name = (String)request.getParameter("friend_id");
		} else {
			friend_name = (String)request.getAttribute("friend_id");
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
					
				<th><a href = <%="\"homepage.jsp?id="+ sender_name +"\""%>>Homepage</a></th>
				<th><a href = "showAnnouncements.jsp">Announcements</a></th>
				<th>My Achievements</th>

				<% //System.out.println( "In Homepage: " + name); %>
          		<th> <a href="searchFriends.jsp?id=<%=sender_name%>"> Find Friends</a> </th>


				<th>My Messages 
					<form action = <%="\"showMessage.jsp?id="+(String) getServletContext().getAttribute("session_user")+"\""%>>					
						<select name = "choice">
							<option>Received Messages</option>
							<option>Sent Messages</option>
							<option>Send A Message</option>
						</select>
						<input name="choice" type="hidden" value=<%=(String)request.getParameter("choice")%>>
						<input name="id" type="hidden" value=<%=(String) getServletContext().getAttribute("session_user")%>>
						<input type = "submit" value = "Go">
					</form>
				</th>
				<th>Quizzes</th>
			</tr>
	</table>
	
	<form action="SendMessageServlet" method="post"> 
		<input type="text" value="" name="msg_content"> 
		<input type = "hidden" name = "id" value = <%="\""+sender_name+"\""%>>
		<input type = "hidden" name = "friend_id" value = <%="\""+friend_name+"\""%>>
		<input type="submit" name=message_type value="Send Note">
	</form>
	
		<form action="SendMessageServlet" method="post"> 
		<input type = "hidden" name = "id" value = <%="\""+sender_name+"\""%>>
		<input type = "hidden" name = "friend_id" value = <%="\""+friend_name+"\""%>>

		<input type="submit" name=message_type value="Add Friend">
	</form>
	<br>

	
	
</body>
</html>
