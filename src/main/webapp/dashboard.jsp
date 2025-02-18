<%@page import="com.dao.ContactDao"%>
<%@page import="com.dao.UserDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.models.User"%>
<%@ page import="com.models.Contact"%>
<%@ page import="com.filters.AuthFilter"%>
<%@ page import="java.util.*"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Contact Manager - Dashboard</title>
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

			<%
			User user = (User) request.getAttribute("user");
			List<Contact> contacts = (List<Contact>) request.getAttribute("contacts");
			boolean isLastPage = (boolean) request.getAttribute("isLastPage");
			int pageNumber = (int) request.getAttribute("pageNumber");
			boolean isFirstPage = pageNumber == 0;
			%>

			<div class="dashboard-header">
				<div>
					<h1 class="welcome-heading">
						Welcome,
						<%=user.getFirstName()%>!
					</h1>
					<p class="welcome-subtitle">Manage your contacts efficiently</p>
				</div>
				<button type="button" class="mybutton add-contact-btn"
					data-bs-toggle="modal" data-bs-target="#exampleModal">
					<i class="bi bi-plus-circle"></i> Add New Contact
				</button>
			</div>

			<div class="card contact-card">
				<div
					class="card-header d-flex justify-content-between align-items-center">
					<h3>
						<i class="bi bi-people"></i> All Contacts
					</h3>
					<span class="contact-count badge bg-primary"><%=contacts != null ? contacts.size() : 0%></span>
				</div>
				<div class="card-body">
					<%
					if (contacts != null && !contacts.isEmpty()) {
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
								for (Contact contact : contacts) {
								%>
								<tr>
									<td>
										<div class="contact-name"><%=contact.getAlias_name()%></div>
									</td>
									<td class="text-end"><a
										href="/contacts/<%=contact.getMyContactsID()%>"
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
						<p>No contacts found. Add your first contact to get started!</p>
					</div>
					<%
					}
					%>
				</div>

				<div class="card-footer">
					<nav aria-label="Contacts pagination">
						<ul class="pagination justify-content-center">
							<%
							if (!isFirstPage) {
							%>
							<li class="page-item"><a class="page-link"
								href="/contacts?page=<%=pageNumber - 1%>"> <i
									class="bi bi-chevron-left"></i> Previous
							</a></li>
							<%
							} else {
							%>
							<li class="page-item disabled"><span class="page-link"><i
									class="bi bi-chevron-left"></i> Previous</span></li>
							<%
							}
							%>

							<li class="page-item active"><span class="page-link">Page
									<%=pageNumber + 1%></span></li>

							<%
							if (!isLastPage) {
							%>
							<li class="page-item"><a class="page-link"
								href="/contacts?page=<%=pageNumber + 1%>"> Next <i
									class="bi bi-chevron-right"></i>
							</a></li>
							<%
							} else {
							%>
							<li class="page-item disabled"><span class="page-link">Next
									<i class="bi bi-chevron-right"></i>
							</span></li>
							<%
							}
							%>
						</ul>
					</nav>
				</div>
			</div>

			<!-- Add Contact Modal -->
			<div class="modal fade" id="exampleModal" tabindex="-1"
				aria-labelledby="exampleModalLabel" aria-hidden="true">
				<div class="modal-dialog modal-lg">
					<div class="modal-content">
						<div class="modal-header">
							<h1 class="modal-title fs-5" id="exampleModalLabel">
								<i class="bi bi-person-plus"></i> Add New Contact
							</h1>
							<button type="button" class="btn-close" data-bs-dismiss="modal"
								aria-label="Close"></button>
						</div>
						<div class="modal-body">
							<form action="/contacts?action=add" method="post"
								class="addcontact">
								<div class="row">
									<div class="col-md-6 mb-3">
										<div class="form-group">
											<label for="alias_fnd_name" class="form-label"> <i
												class="bi bi-person"></i> Alias/Name:
											</label> <input type="text" class="form-control" id="alias_fnd_name"
												name="alias_fnd_name" placeholder="Enter contact name"
												required>
										</div>
									</div>
									<div class="col-md-6 mb-3">
										<div class="form-group">
											<label for="friend_email" class="form-label"> <i
												class="bi bi-envelope"></i> Email:
											</label> <input type="email" class="form-control" id="friend_email"
												name="friend_email" placeholder="Enter email address"
												required>
										</div>
									</div>
								</div>

								<div class="row">
									<div class="col-md-6 mb-3">
										<div class="form-group">
											<label for="phone" class="form-label"> <i
												class="bi bi-telephone"></i> Phone:
											</label> <input type="tel" class="form-control" id="phone"
												name="phone" placeholder="Enter phone number" required>
										</div>
									</div>
									<div class="col-md-6 mb-3">
										<div class="form-group">
											<label for="address" class="form-label"> <i
												class="bi bi-geo-alt"></i> Address:
											</label> <input type="text" class="form-control" id="address"
												name="address" placeholder="Enter address">
										</div>
									</div>
								</div>

								<div class="row">
									<div class="col-md-6 mb-3">
										<div class="form-group">
											<label class="form-label"> <i class="bi bi-archive"></i>
												Is Archived:
											</label>
											<div class="btn-group" role="group">
												<input type="radio" class="btn-check" name="isArchived"
													value="0" id="isArchivedNo" checked> <label
													class="btn btn-outline-secondary" for="isArchivedNo">No</label>

												<input type="radio" class="btn-check" name="isArchived"
													value="1" id="isArchivedYes"> <label
													class="btn btn-outline-secondary" for="isArchivedYes">Yes</label>
											</div>
										</div>
									</div>
									<div class="col-md-6 mb-3">
										<div class="form-group">
											<label class="form-label"> <i class="bi bi-heart"></i>
												Is Favorite:
											</label>
											<div class="btn-group" role="group">
												<input type="radio" class="btn-check" name="isFavorite"
													value="0" id="isFavoriteNo" checked> <label
													class="btn btn-outline-secondary" for="isFavoriteNo">No</label>

												<input type="radio" class="btn-check" name="isFavorite"
													value="1" id="isFavoriteYes"> <label
													class="btn btn-outline-secondary" for="isFavoriteYes">Yes</label>
											</div>
										</div>
									</div>
								</div>

								<div class="d-grid gap-2 mt-4">
									<button type="submit" class="mybutton btn-lg">
										<i class="bi bi-person-plus-fill"></i> Add Contact
									</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>