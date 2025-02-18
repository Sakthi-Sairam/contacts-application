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
	List<Contact> categoryContacts = (List<Contact>) request.getAttribute("categoryContacts");
	List<Contact> contacts = (List<Contact>) request.getAttribute("contacts");

	String addResult = (String) request.getAttribute("addResult");
	if (addResult != null)
		out.println(addResult);
	%>
	<div class="app-container">
		<input type="checkbox" id="sidebar-toggle" class="sidebar-toggle">
		<label for="sidebar-toggle" class="sidebar-toggle-label"> <i
			class="bi bi-list"></i>
		</label>
		<div class="sidebar">
			<div class="sidebar-header">
				<i class="bi bi-person-rolodex sidebar-logo"></i>
				<h2>Contact Manager</h2>
			</div>
			<nav class="sidebar-nav">
				<a class="active" href="/contacts"> <i
					class="bi bi-person-lines-fill"></i> <span>All Contacts</span>
				</a> <a href="/profile"> <i class="bi bi-person-circle"></i> <span>Profile</span>
				</a> <a href="/group/archived"> <i class="bi bi-archive"></i> <span>Archived</span>
				</a> <a href="/group/favourites"> <i class="bi bi-heart"></i> <span>Favourites</span>
				</a> <a href="/categories"> <i class="bi bi-tags"></i> <span>Categories</span>
				</a> <a href="logout" class="logout"> <i
					class="bi bi-box-arrow-right"></i> <span>Logout</span>
				</a>
			</nav>
		</div>
		<div class="content">

			<div class="card contact-card">
				<div
					class="card-header d-flex justify-content-between align-items-center">
					<h3>
						<i class="bi bi-people"></i>
						<%=category.getCategoryName()%></h3>
					<span class="contact-count badge bg-primary"><%=categoryContacts != null ? categoryContacts.size() : 0%></span>
				</div>
				<div class="card-body">
					<%
					if (categoryContacts != null && !categoryContacts.isEmpty()) {
					%>
					<div class="table-responsive">
						<table class="table table-hover">
							<thead>
								<tr>
									<th><i class="bi bi-person me-2"></i>Contact Name</th>
									<th class="text-end">Action</th>
								</tr>
							</thead>
							<tbody>
								<%
								for (Contact i : categoryContacts) {
								%>
								<tr>
									<td>
										<div class="contact-name"><%=i.getAlias_name()%></div>
									</td>
									<td class="text-end"><a
										href="/contacts/<%=i.getMyContactsID()%>"
										class="mybutton view-btn"> <i class="bi bi-eye"></i> View
									</a></td>
								</tr>
								<%
								}
								%>
							</tbody>
						</table>
					</div>
					<%
					} else {
					%>
					<div class="empty-state">
						<i class="bi bi-person-x empty-icon"></i>
						<p>No contacts found in this category.</p>
					</div>
					<%
					}
					%>
				</div>
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
	</div>
</body>
</html>