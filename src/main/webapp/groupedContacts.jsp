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
				<a href="/contacts"> <i class="bi bi-person-lines-fill"></i> All
					Contacts
				</a> <a href="/profile"> <i class="bi bi-person-circle"></i> Profile
				</a> <a class="<%if (title == "Archived")
	out.print("active");%>"
					href="/group/archived"> <i class="bi bi-archive"></i> Archived
				</a> <a class="<%if (title == "Favourites")
	out.print("active");%>"
					href="/group/favourites"> <i class="bi bi-heart"></i>
					Favourites
				</a> <a href="/merge-contacts"> <i class="bi bi-intersect me-2"></i><span>Merge
						Duplicates</span>
				</a> <a href="/categories"> <i class="bi bi-tags"></i> Categories
				</a> <a href="/logout" class="logout"> <i
					class="bi bi-box-arrow-right"></i> Logout
				</a>
			</nav>
		</div>
		<div class="content">
			<div class="card contact-card">
				<div
					class="card-header d-flex justify-content-between align-items-center">
					<h3>
						<i class="bi bi-people"></i>
						<%=title%></h3>
					<span class="contact-count badge bg-primary"><%=groupedContacts != null ? groupedContacts.size() : 0%></span>
				</div>
				<div class="card-body">
					<%
					if (groupedContacts != null && !groupedContacts.isEmpty()) {
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
								for (Contact i : groupedContacts) {
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
						<p>No contacts found in this group.</p>
					</div>
					<%
					}
					%>
				</div>
			</div>
		</div>
	</div>
</body>
</html>