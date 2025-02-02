<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.models.User" %>
<%@page import="com.dao.ContactDao"%>
<%@ page import="com.models.Contact" %>
<%@ page import="com.filters.SessionFilter" %>
<%@ page import="java.util.*" %>



<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>


    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<link rel="stylesheet" type="text/css" href="./styles/dashboard.css">
    
</head>
<body>

	<%
	User user = (User)SessionFilter.getCurrentUser();
	%>
    
    	<div class="sidebar">
	  <a class="" href="dashboard.jsp">All Contacts</a>
	<a class="" href="profile.jsp">Profile</a>
	  <a class="active" href="archived.jsp">Archieved</a>
	  <a href="categories.jsp">Categories</a>
	  <a href="logout" class="logout">Logout</a>
	</div>
	<div class="content pt-5">
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
       // List<Contact> contacts = ContactDao.getArchivedContactsByUserId(user.getUserId());
	    List<Contact> contacts = null;
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