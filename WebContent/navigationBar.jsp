<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>

<%
String userName = (String)request.getSession().getAttribute("session_user");
%>

<body>
	<table id="navBar">
					<tr >
						<th class = "btn"><a href = "homepage.jsp">Homepage</a></th>		
						<th class = "btn"><a href = "showAnnouncements.jsp">Announcements</a></th>
						<th class = "btn">My Achievements</th>
						<th  id = "msg-header" class = "btn">My Messages 
							<form action = <%="\"showMessage.jsp?id="+userName+"\""%>>					
								<select name = "choice">
									<option>Received Messages</option>
									<option>Sent Messages</option>
								</select>
								<input name="choice" type="hidden" value=<%=(String)request.getParameter("choice")%>>
								<input name="id" type="hidden" value=<%=userName%>>
								<input type = "submit" value = "Go">
							</form>
						</th>
						<th class = "btn"> <a href="searchFriends.jsp?id=<%=userName%>"> Lookup Users</a> </th>
						<th class = "btn"><a href = <%="\"quizHome.html?user="+userName+"\""%>> Quizzes</a></th>
						<th class = "btn"><a href = <%="\"login.jsp?errMsg=\"LoggedOut\""%>>Logout</a></th>						 
					</tr>
			</table>

</body>
</html>