<%@page import="com.dao.ContactDao"%>
<%@page import="com.dao.UserDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.models.User"%>
<%@ page import="com.models.Contact"%>
<%@ page import="com.filters.AuthFilter"%>
<%@ page import="java.util.*"%>


<%
String title = (String) request.getAttribute("title");
List<Contact> groupedContacts = (List<Contact>) request.getAttribute("groupedContacts");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%=title%></title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
	crossorigin="anonymous"></script>

<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/styles/dashboard.css">

</head>
<body>
<div class="sidebar">
    <div class="sidebar-header">
        <h2>Contact Manager</h2>
    </div>
    <nav class="sidebar-nav">
        <a href="/contacts">
            <i class="bi bi-person-lines-fill"></i>
            All Contacts
        </a>
        <a href="/profile">
            <i class="bi bi-person-circle"></i>
            Profile
        </a>
        <a class="<%if(title=="Archived") out.print("active"); %>" href="/group/archived">
            <i class="bi bi-archive"></i>
            Archived
        </a>
        <a class="<%if(title=="Favourites") out.print("active"); %>" href="/group/favourites">
            <i class="bi bi-heart"></i>
            Favourites
        </a>
        <a href="/categories">
            <i class="bi bi-tags"></i>
            Categories
        </a>
        <a href="/logout" class="logout">
            <i class="bi bi-box-arrow-right"></i>
            Logout
        </a>
    </nav>
</div>
<div class="content">
			<h1 class="mt-3 mb-3 heading "><%=title %></h1>
			<table>
			<thead>
				<tr>
					<th>Contact Name</th>
					<!--  <th>Email</th> -->
					<th>Phone</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody>
				<%
				if (groupedContacts != null && !groupedContacts.isEmpty()) {
					for (Contact i : groupedContacts) {
				%>
				<tr>
					<td><%=i.getAlias_name()%></td>
					<!--  <tdi.getFriend_email()l() %></td> -->
					<td><%=i.getPhone()%></td>
					<td><a href="/contacts/<%=i.getMyContactsID()%>"
						class="mybutton"> View Details </a></td>
				</tr>
				<%
				}
				} else {
				%>
				<tr>
					<td colspan="4">No groupedContacts found.</td>
				</tr>
				<%
				}
				%>
			</tbody>
		</table>
			

</div>

</body>
</html>