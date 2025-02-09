<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.models.Contact"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Contact Details</title>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">


<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
	crossorigin="anonymous"></script>

<link rel="stylesheet" type="text/css"
	href="<%= request.getContextPath() %>/styles/dashboard.css">
</head>
<body>
	<%
	Contact contact = (Contact) request.getAttribute("contact");
	%>
<div class="sidebar">
    <div class="sidebar-header">
        <h2>Contact Manager</h2>
    </div>
    <nav class="sidebar-nav">
        <a class="active" href="/contacts">
            <i class="bi bi-person-lines-fill"></i>
            All Contacts
        </a>
        <a href="/profile">
            <i class="bi bi-person-circle"></i>
            Profile
        </a>
        <a href="/group/archived">
            <i class="bi bi-archive"></i>
            Archived
        </a>
        <a href="/group/favourites">
            <i class="bi bi-heart"></i>
            Favourites
        </a>
        <a href="/categories">
            <i class="bi bi-tags"></i>
            Categories
        </a>
        <a href="logout" class="logout">
            <i class="bi bi-box-arrow-right"></i>
            Logout
        </a>
    </nav>
</div>
	<div class="content">
		<div class="container mt-4">
			<h1>Contact Details</h1>
			<table class="table table-bordered">
				<tr>
					<th>Alias Name</th>
					<td><%=contact.getAlias_name()%></td>
				</tr>
				<tr>
					<th>Email</th>
					<td><%=contact.getFriend_email()%></td>
				</tr>
				<tr>
					<th>Phone</th>
					<td><%=contact.getPhone()%></td>
				</tr>
				<tr>
					<th>Address</th>
					<td><%=contact.getAddress()%></td>
				</tr>
			</table>

			<!-- Delete Contact -->
			<form
				action="/contacts/<%=contact.getMyContactsID()%>?action=delete"
				method="post" style="display: inline;">
				<button type="submit" class="btn btn-danger"
					onclick="return confirm('Are you sure you want to delete this contact?');">
					Delete</button>
			</form>

			<!-- Edit Contact Modal Trigger -->
			<button type="button" class="btn btn-primary" data-bs-toggle="modal"
				data-bs-target="#editContactModal">Edit Contact</button>

			<a href="/contacts" class="btn btn-secondary"> back</a>

			<!-- Edit Contact Modal -->
			<div class="modal fade" id="editContactModal" tabindex="-1"
				aria-labelledby="editContactModalLabel" aria-hidden="true">
				<div class="modal-dialog modal-lg">
					<div class="modal-content">
						<div class="modal-header">
							<h5 class="modal-title" id="editContactModalLabel">Edit
								Contact</h5>
							<button type="button" class="btn-close" data-bs-dismiss="modal"
								aria-label="Close"></button>
						</div>
						<div class="modal-body">
							<form
								action="/contacts/<%=contact.getMyContactsID()%>?action=edit"
								method="post">
								<div class="form-group">
									<label for="friend_email">Email:</label> <input type="email"
										id="friend_email" name="friend_email"
										value="<%=contact.getFriend_email()%>"
										class="form-control">
								</div>
								<div class="form-group">
									<label for="alias_fnd_name">Alias:</label> <input type="text"
										id="alias_fnd_name" name="alias_fnd_name"
										value="<%=contact.getAlias_name()%>" required
										class="form-control">
								</div>
								<div class="form-group">
									<label for="phone">Phone:</label> <input type="text" id="phone"
										name="phone" value="<%=contact.getPhone()%>" required
										class="form-control">
								</div>
								<div class="form-group">
									<label for="address">Address:</label> <input type="text"
										id="address" name="address"
										value="<%=contact.getAddress()%>" class="form-control">
								</div>
								<div class="form-group">
									<label>Is Archived:</label><br> <input type="radio"
										name="isArchived" value="1"
										<%=contact.getIsArchived() == 1 ? "checked" : ""%>>
									Yes <input type="radio" name="isArchived" value="0"
										<%=contact.getIsArchived() == 0 ? "checked" : ""%>>
									No
								</div>
								<div class="form-group">
									<label>Is Favorite:</label><br> <input type="radio"
										name="isFavorite" value="1"
										<%=contact.getIsFavorite() == 1 ? "checked" : ""%>>
									Yes <input type="radio" name="isFavorite" value="0"
										<%=contact.getIsFavorite() == 0 ? "checked" : ""%>>
									No
								</div>
								<input type="submit" class="btn btn-success mt-3"
									value="Update Contact">
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
