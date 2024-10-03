<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.models.User" %>
<%@ page import="com.models.Contact" %>
<%@ page import="java.util.*" %>



<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="./styles/dashboard.css">
</head>
<body>

    <%
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
    %>
    
    	<div class="sidebar">
	  <a class="" href="dashboard.jsp">All Contacts</a>
	<a class="" href="profile.jsp">Profile</a>
	  <a class="active" href="archived.jsp">Archieved</a>
	  <a href="categories.jsp">Categories</a>
	  <a href="logout" class="logout">Logout</a>
	</div>
	<div class="content">
	    <table>
    <thead>
        <tr>
            <th>Contact Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Address</th>
        </tr>
    </thead>
    <tbody>
	<h3 class="heading">Archieved Contacts</h3>
	    <%
        List<Contact> contacts = user.getArchieves();
        if (contacts != null && !contacts.isEmpty()) {
            for (Contact i : contacts) {
                out.println("<tr>");
                out.println("<td>" + i.getAlias_name() + "</td>");
                out.println("<td>" + i.getFriend_email() + "</td>");
                out.println("<td>" + i.getPhone() + "</td>");
                out.println("<td>" + i.getAddress() + "</td>");
                out.println("</tr>");
            }
        } else {
            out.println("<tr><td colspan='4'>No contacts found.</td></tr>");
        }
        %>
        </tbody>
        </table>
        </div>

</body>
</html>