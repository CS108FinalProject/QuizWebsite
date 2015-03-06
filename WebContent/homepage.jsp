<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ page import = "com.accounts.*"%>
<%@ page import = "java.util.*"%>
<%@ page import = "javax.swing.*" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="css//main.css" ></link>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%String name = (String)getServletContext().getAttribute("session_user");
Account acct = AccountManager.getAccount(name);
String sel_type = (String)request.getParameter("choice");
%>




<title>Welcome <%=name%></title>


</head>

<body>

  <%String errMsg = (String)request.getAttribute("errMsg");%>
  <%if (errMsg != null) {%>
    <%= errMsg%>
  <%}%>

  <!-- HTML -->

  <nav>
    <ul>
      <%if(acct.isAdmin()) {
          out.println("<li><a href = \"adminHomepage.jsp\">Homepage</a></li>");
        } else {
          out.println("<li><a href = \"homepage.jsp\">Homepage</a></li>");
        }
      %> 

      <li><a href = "showAnnouncements.jsp">Announcements</a></li>
      <li><a href = "#">My Achievements</a></li>
      <li>
		Messages
        <div>
          <ul>
            <li><a href=<%="\"showMessage.jsp?id="+name+"\""%>>Received Messages</a></li>
            <li><a href=<%="\"showMessage.jsp?id="+name+"\""%>>Sent Messages</a></li>
          </ul>
        </div>

      </li>
      <li><a href = <%="\"searchFriends.jsp?id="+name+"\""%>>Lookup Users</a></li>
      <li><a href = "#">Quizzes</a></li> 
      <li><a href = <%="\"login.jsp?errMsg=\"LoggedOut\""%>>Logout</a></li>
    </ul>
  </nav>
  

  <section id="greeting">
  
  <h2>Nice to see you, <%=name %></h2>

  </section>

  <div id="row_1">

	 <section id="announcements">
	 	<%//Displays any admin announcements as a list%>
	     <%
	     ArrayList<String> admin_anmts = (ArrayList<String>)getServletContext().getAttribute("announcements");
	     %>
	    
	     <!--  HTML beginning for Announcments -->
	     <a href = "showAnnouncements.jsp">Announcements</a>
	     <%if (admin_anmts != null) { 
	         out.println("<ul>");
	         int anmts_len = admin_anmts.size();
	         for (int i = anmts_len - 1; i > -1; i--) { 
	           out.println("<li>"+admin_anmts.get(i)+"</li>");
	           if (i == anmts_len - 5) i = -1;
	         } 
	         out.println("</ul>");
	       } else {
	         out.println("<br></br>Hello, no new announcements.");
	       }
	       %> 
	 </section>
	 
	 <section id="messages">
	 			<div id = "send_messages">
					<a href = "searchFriends.jsp?id=<%=name%>">Lookup User</a>
				</div>
				
	 	<div id="view_messages">
	 		<h2>My Messages</h2>
	 		<form action = <%="\"homepage.jsp?id="+name+"\""%>>
	           <select name = "choice">
	             <option>Received Messages</option>
	             <option>Sent Messages</option>
	           </select>
	           <input type = "submit" value = "Display Messages">
	         </form>
	         <%          
	         
	           List<Message> messages = acct.getReceivedMessages();  
	         out.println("<a href =\"showMessage.jsp?choice="+sel_type+"&id="+name+"\""+">View All Messages</a>");
	
	         if (messages.size() > 0) {
	           out.println("<table>");
	           for ( int i = messages.size() -1; i > -1;i-- ) {
	             out.println("<tr>");
	             Message msg = messages.get(i);
	             out.println("<td><a href=\"accountProfile.jsp?friend_id=" + msg.getSender() 
	                 + "&username=" + msg.getRecipient() + "\">" + msg.getSender() + "</a></td>");
	             out.println("<td>"+msg.getDate()+"</td>");
	             out.println("<td>"+msg.getType()+"</td>");
	             if (i == messages.size() -5) i = -1;
	
	           }
	           out.println("</table>");
	         }  
	           
	         %>
	 	</div>
	 						 	
	 </section>

  </div>
  
  <div id="still_left">
	  <section  id="achievements">
	  	<h2>My Achievements</h2>
	  	<h2>Friends Achievements</h2>
	  </section>
	  
	  <section id="quizzes">
	  	<h2>Newly Created Quizzes</h2>
	  	<h2>Most Popular Quizzes</h2>
	  	<h2>Recent Quizzes</h2>
	  </section>
	  
	  <aside id="history">
	  	<h2>My History</h2>
	  </aside>
	  	
	  </section>






</section>



  </div>
 
  <div id="still_left">
  <section  id="achievements">
  <h2>My Achievements</h2>
  <h2>Friends Achievements</h2>
  </section>

  <section id="quizzes">
  <h2>Newly Created Quizzes</h2>
  <h2>Most Popular Quizzes</h2>
  <h2>Recent Quizzes</h2>
  </section>

  <aside id="history">
  <h2>My History</h2>
  </aside>
  </section>

  </div>
</body>

</html>
