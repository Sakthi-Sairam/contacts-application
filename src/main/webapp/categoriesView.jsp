<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.models.User"%>
<%@ page import="com.models.CategoryDetails"%>
<%@ page import="com.models.Contact"%>
<%@ page import="com.dao.CategoriesDao"%>
<%@page import="com.dao.ContactDao"%>
<%@ page import="java.util.*"%>
<%@ page import="com.filters.AuthFilter"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

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

<style type="text/css">
.container2 {
	background: white;
	padding: 2rem;
	border-radius: 1.5rem;
	box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
	margin: 2rem auto;
	max-width: 1200px;
}

.form-group {
	margin-bottom: 1.5rem;
}

.form-group label {
	font-weight: 500;
	margin-bottom: 0.5rem;
	display: block;
	color: var(--primary-color);
}

.form-group select {
	width: 100%;
	padding: 0.8rem;
	border: 2px solid #eee;
	border-radius: 8px;
	transition: all 0.3s ease;
}

.form-group select:focus {
	border-color: var(--secondary-color);
	outline: none;
}
</style>
</head>
<body>

	<%
	CategoryDetails category = (CategoryDetails) request.getAttribute("category");
	List<Contact> categoryContacts = category.getCategoryContacts();
	List<Contact> contacts = (List<Contact>) request.getAttribute("contacts");

	String addResult = (String) request.getAttribute("addResult");
	if (addResult != null)
		out.println(addResult);
	%>
	<div class="sidebar">
		<div class="sidebar-header">
			<h2>Contact Manager</h2>
		</div>
		<nav class="sidebar-nav">
			<a href="/contacts"> <i class="bi bi-person-lines-fill"></i> All
				Contacts
			</a> <a href="/profile"> <i class="bi bi-person-circle"></i> Profile
			</a> <a href="/group/archived"> <i class="bi bi-archive"></i>
				Archived
			</a> <a href="/group/favourites"> <i class="bi bi-heart"></i>
				Favourites
			</a> <a class="active" href="/categories"> <i class="bi bi-tags"></i>
				Categories
			</a> <a href="/logout" class="logout"> <i
				class="bi bi-box-arrow-right"></i> Logout
			</a>
		</nav>
	</div>
	<div class="content">
	
			<%
		String errorMessage = (String) request.getParameter("errorMessage");
		if (errorMessage != null && !errorMessage.isEmpty()) {
		%>
		<div class="alert alert-danger alert-dismissible fade show"
			role="alert">
			<strong>ERROR</strong>
			<%=errorMessage%>
			<button type="button" class="btn-close" data-bs-dismiss="alert"
				aria-label="Close"></button>
		</div>
		<%
		}
		%>
	<div class="container2">
		<h1 class="mt-3 mb-3 heading "><%=category.getCategoryName()%></h1>
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
				if (categoryContacts != null && !categoryContacts.isEmpty()) {
					for (Contact i : categoryContacts) {
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
					<td colspan="4">No categoryContacts found.</td>
				</tr>
				<%
				}
				%>
			</tbody>
		</table>
		</div>
		<div class="container2">
			<h3>Add Contact to Category</h3>
			<form
				action="/categories?categoryId=<%=category.getCategoryId()%>&action=assignContact"
				method="post">

				<div class="form-group">
					<label for="contact">Select Contact:</label> <select id="contact"
						name="contactId" required>
						<%
						for (Contact contact : contacts) {
							out.println("<option value='" + contact.getMyContactsID() + "'>" + contact.getAlias_name() + "</option>");
						}
						%>
					</select>
				</div>

				<button type="submit" class="mybutton">Assign Contact</button>
			</form>
		</div>


	</div>



</body>
</html>