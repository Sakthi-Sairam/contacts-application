<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.models.Contact, java.util.List"%>

<%
List<List<Contact>> mergedContacts = (List<List<Contact>>) request.getAttribute("mergedContacts");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Merge Duplicate Contacts</title>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/styles/dashboard.css">
<style>
.merge-container {
	max-width: 1200px;
	margin: 20px auto;
	padding: 20px;
	background-color: #fff;
	border-radius: 10px;
	box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
}

.contact-group {
	margin-bottom: 30px;
	padding: 20px;
	border: 1px solid #e0e0e0;
	border-radius: 8px;
	background-color: #f9f9f9;
}

.contact-item {
	padding: 15px;
	margin-bottom: 10px;
	background-color: white;
	border-radius: 6px;
	border-left: 4px solid #007bff;
}

.no-duplicates {
	padding: 50px;
	text-align: center;
	background-color: #f9f9f9;
	border-radius: 8px;
}
</style>
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
				<a href="/contacts"> <i class="bi bi-person-lines-fill"></i> <span>All
						Contacts</span>
				</a> <a href="/profile"> <i class="bi bi-person-circle"></i> <span>Profile</span>
				</a> <a href="/group/archived"> <i class="bi bi-archive"></i> <span>Archived</span>
				</a> <a href="/group/favourites"> <i class="bi bi-heart"></i> <span>Favourites</span>
				</a> <a class="active" href="/merge-contacts"> <i
					class="bi bi-intersect me-2"></i><span>Merge Duplicates</span>
				</a> <a href="/categories"> <i class="bi bi-tags"></i> <span>Categories</span>
				</a> <a href="logout" class="logout"> <i
					class="bi bi-box-arrow-right"></i> <span>Logout</span>
				</a>
			</nav>
		</div>

		<div class="container">
			<%
			String errorMessage = (String) request.getParameter("errorMessage");
			if (errorMessage != null && !errorMessage.isEmpty()) {
			%>
			<div class="alert alert-danger alert-dismissible fade show"
				role="alert">
				<i class="bi bi-exclamation-triangle-fill me-2"></i> <strong>Error:</strong>
				<%=errorMessage%>
				<button type="button" class="btn-close" data-bs-dismiss="alert"
					aria-label="Close"></button>
			</div>
			<%
			}
			%>
			<div class="merge-container">
				<h1 class="mb-4">
					<i class="bi bi-intersect me-2"></i>Merge Duplicate Contacts
				</h1>

				<div class="alert alert-info">
					<i class="bi bi-info-circle-fill me-2"></i> Select groups of
					contacts to merge. Merging will combine contact details into a
					single contact.
				</div>

				<%
				if (mergedContacts == null || mergedContacts.isEmpty()) {
				%>
				<div class="no-duplicates">
					<i class="bi bi-check-circle-fill text-success"
						style="font-size: 3rem;"></i>
					<h3 class="mt-3">No duplicate contacts found</h3>
					<p class="text-muted">Your contact list doesn't have any
						contacts with identical names.</p>
					<a href="/contacts" class="btn btn-primary mt-3"> <i
						class="bi bi-arrow-left me-2"></i>Back to Contacts
					</a>
				</div>
				<%
				} else {
				%>
				<div class="d-flex justify-content-between mb-4">
					<a href="/contacts" class="btn btn-outline-secondary"> <i
						class="bi bi-arrow-left me-2"></i>Back to Contacts
					</a>
				</div>

				<%
				int groupIndex = 0;
				%>
				<%
				for (List<Contact> group : mergedContacts) {
				%>
				<%
				if (group != null && group.size() > 1) {
				%>
				<div class="contact-group">
					<form action="/merge-contacts" method="post">
						<h4>
							<i class="bi bi-people-fill me-2"></i><%=group.get(0).getAlias_name()%>
							<span class="badge bg-secondary"><%=group.size()%>
								duplicates</span>
						</h4>

						<input type="hidden" name="groupIndex" value="<%=groupIndex%>">

						<div class="row mt-3">
							<%
							for (Contact contact : group) {
							%>
							<div class="col-md-6 mb-3">
								<div class="contact-item">
									<input type="hidden" name="selectedContacts"
										value="<%=contact.getMyContactsID()%>">
									<div>
										<strong><%=contact.getAlias_name()%></strong> <small
											class="d-block text-muted">ID: <%=contact.getMyContactsID()%></small>
									</div>

									<div class="mt-2">
										<%
										if (contact.getPhoneNumbers() != null && !contact.getPhoneNumbers().isEmpty()) {
										%>
										<div>
											<i class="bi bi-telephone me-2"></i><%=contact.getPhoneNumbers().size()%>
											phone numbers
										</div>
										<%
										}
										%>

										<%
										if (contact.getEmails() != null && !contact.getEmails().isEmpty()) {
										%>
										<div>
											<i class="bi bi-envelope me-2"></i><%=contact.getEmails().size()%>
											email addresses
										</div>
										<%
										}
										%>
									</div>
								</div>
							</div>
							<%
							}
							%>
						</div>

						<div class="mt-3 text-center">
							<button type="submit" class="btn btn-success">
								<i class="bi bi-intersect me-1"></i>Merge This Group
							</button>
						</div>
					</form>
				</div>
				<%
				groupIndex++;

				}

				}

				}
				%>
			</div>
		</div>
	</div>
</body>
</html>