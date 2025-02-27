<%@page import="com.filters.AuthFilter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.models.Email"%>
<%@ page import="com.models.User"%>
<%@ page import="com.models.OAuthToken"%>
<%@ page import="java.util.*"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Contact Manager - Profile</title>

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
	href="<%=request.getContextPath()%>/styles/dashboard.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/styles/profile.css">
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
				</a> <a class="active" href="/profile"> <i
					class="bi bi-person-circle"></i> <span>Profile</span>
				</a> <a href="/group/archived"> <i class="bi bi-archive"></i> <span>Archived</span>
				</a> <a href="/group/favourites"> <i class="bi bi-heart"></i> <span>Favourites</span>
				</a> <a href="/merge-contacts"> <i class="bi bi-intersect me-2"></i><span>Merge
						Duplicates</span>
				</a> <a href="/categories"> <i class="bi bi-tags"></i> <span>Categories</span>
				</a> <a href="logout" class="logout"> <i
					class="bi bi-box-arrow-right"></i> <span>Logout</span>
				</a>
			</nav>
		</div>

		<%
		User user = (User) request.getAttribute("user");
		List<OAuthToken> oAuthTokens = (List<OAuthToken>) request.getAttribute("oAuthTokens");
		%>

		<div class="content">
			<div class="dashboard-header">
				<div>
					<h1 class="welcome-heading">User Profile</h1>
					<p class="welcome-subtitle">Manage your personal information
						and account settings</p>
				</div>
			</div>

			<div class="profile-grid">
				<!-- Profile Card -->
				<div class="card profile-card">
					<div class="card-header">
						<h3>
							<i class="bi bi-person-badge"></i> Personal Information
						</h3>
					</div>
					<div class="card-body text-center">
						<div class="profile-avatar-container">
							<img class="profile-avatar" alt="profile-img"
								src="https://upload.wikimedia.org/wikipedia/commons/7/7c/Profile_avatar_placeholder_large.png?20150327203541">
							<div class="profile-badge">
								<i class="bi bi-pencil-fill"></i>
							</div>
						</div>

						<h4 class="profile-name"><%=user.getFirstName() + " " + user.getLastName()%></h4>

						<div class="profile-details">
							<div class="profile-detail-item">
								<div class="detail-icon">
									<i class="bi bi-person-vcard"></i>
								</div>
								<div class="detail-content">
									<span class="detail-label">User ID</span> <span
										class="detail-value"><%=user.getUserId()%></span>
								</div>
							</div>

							<div class="profile-detail-item">
								<div class="detail-icon">
									<i class="bi bi-calendar3"></i>
								</div>
								<div class="detail-content">
									<span class="detail-label">Age</span> <span
										class="detail-value"><%=user.getAge()%></span>
								</div>
							</div>

							<div class="profile-detail-item">
								<div class="detail-icon">
									<i class="bi bi-geo-alt"></i>
								</div>
								<div class="detail-content">
									<span class="detail-label">Address</span> <span
										class="detail-value"><%=user.getAddress()%></span>
								</div>
							</div>

							<div class="profile-detail-item">
								<div class="detail-icon">
									<i class="bi bi-telephone"></i>
								</div>
								<div class="detail-content">
									<span class="detail-label">Phone</span> <span
										class="detail-value"><%=user.getPhone()%></span>
								</div>
							</div>
						</div>

						<div class="profile-actions">
							<button class="mybutton" data-bs-toggle="modal"
								data-bs-target="#editProfileModal">
								<i class="bi bi-pencil-square"></i> Edit Profile
							</button>
							<form action="googleLogin" style="display: inline-block;">
								<button type="submit" class="mybutton import-button">
									<i class="bi bi-google"></i> Import from Google
								</button>
							</form>
						</div>
					</div>
				</div>

				<!-- Email Card -->
				<div class="card email-card">
					<div
						class="card-header d-flex justify-content-between align-items-center">
						<h3>
							<i class="bi bi-envelope"></i> Email Addresses
						</h3>
						<button type="button" class="mybutton add-email-btn"
							data-bs-toggle="modal" data-bs-target="#exampleModal">
							<i class="bi bi-plus-circle"></i> Add Email
						</button>
					</div>
					<div class="card-body">
						<div class="table-responsive">
							<table class="table table-hover">
								<thead>
									<tr>
										<th>Email Address</th>
										<th class="text-center">Primary</th>
										<th class="text-center">Delete</th>

									</tr>
								</thead>
								<tbody>
									<%
									List<Email> emails = user.getEmails();
									if (emails != null && !emails.isEmpty()) {
										for (Email email : emails) {
									%>
									<tr>
										<td class="email-address"><%=email.getEmail()%></td>
										<td class="text-center">
											<form
												action="email/<%=user.getPrimaryEmailId()%>/<%=email.getId()%>?action=changePrimary"
												method="post" style="display: inline;">
												<button type="submit"
													class="btn btn-star <%=email.getIsPrimary() == 1 ? "active" : ""%>"
													title="Set as primary email"
													onclick="return confirm('Are you sure you want to set this as your primary email?');">
													<i
														class="bi <%=email.getIsPrimary() == 1 ? "bi-star-fill" : "bi-star"%>"></i>
												</button>
											</form>
										</td>
										<td class="text-center">
											<%
											if (email.getIsPrimary() != 1) {
											%>
											<form action="email/<%=email.getId()%>?action=delete"
												method="post">
												<button type="submit" class="btn-icon text-danger"
													title="Delete email" style="border: none">
													<i class="bi bi-trash-fill"></i>
												</button>
											</form> <%
 } else {
 %>
											<button type="submit" class="disabled" title="Delete email"
												style="border: none">
												<i class="bi bi-trash-fill"></i>
											</button> <%
 }
 %>
										</td>
									</tr>
									<%
									}
									} else {
									%>
									<tr>
										<td colspan="2">
											<div class="empty-state">
												<i class="bi bi-envelope-x empty-icon"></i>
												<p>No email addresses found. Add your first email
													address to get started!</p>
											</div>
										</td>
									</tr>
									<%
									}
									%>
								</tbody>
							</table>
						</div>

						<%
						String result = (String) request.getAttribute("result");
						if (result != null) {
						%>
						<div class="alert alert-success alert-dismissible fade show"
							role="alert">
							<%=result%>
							<button type="button" class="btn-close" data-bs-dismiss="alert"
								aria-label="Close"></button>
						</div>
						<%
						}
						%>
					</div>
				</div>

				<!-- Sync Settings Card -->
				<div class="card sync-card">
					<div class="card-header">
						<h3>
							<i class="bi bi-cloud-sync"></i> Google Sync Settings
						</h3>
					</div>
					<div class="card-body">
						<%
						if (oAuthTokens != null && !oAuthTokens.isEmpty()) {
						%>
						<%
						for (OAuthToken token : oAuthTokens) {
						%>
						<div class="sync-item">
							<div class="sync-email">
								<i class="bi bi-google"></i>
								<%=token.getEmail()%>
							</div>
							<form
								action="/profile?tokenId=<%=token.getId()%>&action=updateSyncInterval"
								method="post" class="sync-form">
								<div class="form-group">
									<select name="syncInterval" class="form-select">
										<option value="0"
											<%=token.getSyncInterval() == 0 ? "selected" : ""%>>No
											Sync</option>
										<option value="60"
											<%=token.getSyncInterval() == 60 ? "selected" : ""%>>1
											Hour</option>
										<option value="300"
											<%=token.getSyncInterval() == 300 ? "selected" : ""%>>5
											Hours</option>
										<option value="600"
											<%=token.getSyncInterval() == 600 ? "selected" : ""%>>10
											Hours</option>
										<option value="2880"
											<%=token.getSyncInterval() == 2880 ? "selected" : ""%>>2
											Days</option>
									</select>
								</div>
								<button type="submit" class="mybutton sync-button">
									<i class="bi bi-arrow-repeat"></i> Update
								</button>
							</form>
						</div>
						<%
						}
						%>
						<%
						} else {
						%>
						<div class="empty-state">
							<i class="bi bi-cloud-slash empty-icon"></i>
							<p>No Google accounts connected. Connect your Google account
								to sync contacts.</p>
						</div>
						<%
						}
						%>
					</div>
				</div>
			</div>

			<!-- Add Email Modal -->
			<div class="modal fade" id="exampleModal" tabindex="-1"
				aria-labelledby="exampleModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<h1 class="modal-title fs-5" id="exampleModalLabel">
								<i class="bi bi-envelope-plus"></i> Add New Email
							</h1>
							<button type="button" class="btn-close" data-bs-dismiss="modal"
								aria-label="Close"></button>
						</div>
						<div class="modal-body">
							<form action="/email/<%=user.getUserId()%>?action=add"
								method="post">
								<div class="form-group mb-4">
									<label for="email" class="form-label"> <i
										class="bi bi-envelope"></i> Email Address:
									</label> <input type="email" class="form-control" id="email"
										name="email" placeholder="Enter your email address" required>
								</div>
								<div class="d-grid">
									<button type="submit" class="mybutton btn-lg">
										<i class="bi bi-plus-circle"></i> Add Email
									</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>

			<!-- Edit Profile Modal -->
			<div class="modal fade" id="editProfileModal" tabindex="-1"
				aria-labelledby="editProfileModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<h1 class="modal-title fs-5" id="editProfileModalLabel">
								<i class="bi bi-person-gear"></i> Edit Profile
							</h1>
							<button type="button" class="btn-close" data-bs-dismiss="modal"
								aria-label="Close"></button>
						</div>
						<div class="modal-body">
							<form action="/profile?action=updateProfile" method="post">
								<div class="form-group mb-3">
									<label for="firstName" class="form-label"> <i
										class="bi bi-person"></i> First Name:
									</label> <input type="text" class="form-control" id="firstName"
										name="firstName" value="<%=user.getFirstName()%>" required>
								</div>
								<div class="form-group mb-3">
									<label for="lastName" class="form-label"> <i
										class="bi bi-person"></i> Last Name:
									</label> <input type="text" class="form-control" id="lastName"
										name="lastName" value="<%=user.getLastName()%>">
								</div>
								<div class="form-group mb-3">
									<label for="age" class="form-label"> <i
										class="bi bi-calendar3"></i> Age:
									</label> <input type="number" class="form-control" id="age" name="age"
										value="<%=user.getAge()%>" required>
								</div>
								<div class="form-group mb-4">
									<label for="address" class="form-label"> <i
										class="bi bi-geo-alt"></i> Address:
									</label> <input type="text" class="form-control" id="address"
										name="address" value="<%=user.getAddress()%>">
								</div>
								<div class="d-grid">
									<button type="submit" class="mybutton btn-lg">
										<i class="bi bi-save"></i> Save Changes
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