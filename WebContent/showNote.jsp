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
	Message msg = (Message) request.getAttribute("message");
	String content = (String) request.getAttribute("message_content");
	String sender = (String) request.getAttribute("sender");
%>
<h2> Message from <%=sender%></h2>
<br> <br>
<p> <%=content%> </p>




</body>
</html>