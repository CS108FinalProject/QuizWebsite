
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%String name = (String)getServletContext().getAttribute("accounts");%>

<title>Welcome <%=name%></title>
</head>
<body>

<h1>Nice to see you, <%=name %></h1>
</body>
</html> 