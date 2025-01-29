<%@page import="com.filters.SessionFilter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%@ page import="com.models.Email"%>
<%@ page import="com.models.User"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

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
<link rel="stylesheet" type="text/css"
	href="<%= request.getContextPath() %>/styles/profile.css">



</head>
<body>
	<div class="sidebar">
		<a class="" href="contacts">All Contacts</a> <a class="active"
			href="profile">Profile</a> <a href="archived">Archieved</a> <a
			href="categories">Categories</a> <a href="logout" class="logout">Logout</a>
	</div>
	<%
	User user = (User)SessionFilter.getCurrentUser();
	%>
	<div class="content">
		<div class="mycontainer">
			<div class="profile">
				<h3>Profile</h3>

				<h5>
					Hello
					<%=user.getFirstName()%>
					<%=user.getLastName()%>...
				</h5>
				<img alt="profile-img"
					src="https://upload.wikimedia.org/wikipedia/commons/7/7c/Profile_avatar_placeholder_large.png?20150327203541">
				<div class="profile-datas">
					<p>
						<strong>User ID:</strong>
						<%=user.getUserId()%></p>
					<p>
						<strong>Name:</strong>
						<%=user.getFirstName() + " " + user.getLastName()%></p>
					<p>
						<strong>Age:</strong>
						<%=user.getAge()%></p>
					<p>
						<strong>Address:</strong>
						<%=user.getAddress()%></p>
					<p>
						<strong>Phone:</strong>
						<%=user.getPhone()%></p>
				</div>
				<form action="googleLogin">
					<input type="submit" value="import contacts from google"
						class="btn btn-primary">
				</form>

			</div>

		</div>
		<div class="mycontainer">
			<h3>Emails</h3>

			<table>
				<thead>
					<tr>
						<th>Email_id</th>
						<th>Primary</th>
					</tr>
				</thead>
				<tbody>

					<%
					List<Email> emails = user.getEmails();
											if (emails != null && !emails.isEmpty()) {
												for (Email i : emails) {
													out.println("<tr>");
													out.println("<td>" + i.getEmail() + "</td>");
													out.println("<td>");
													String endPoint = String.format("email/%d/%d/%d?action=changePrimary",user.getUserId(),user.getPrimaryEmailId(),i.getId());
													out.println("    <form action='"+ endPoint +"' method='post' style='display:inline;'>");
													out.println(
													"        <button type='submit' class='btn btn-warning' title='primary email' onclick='return confirm(\"Are you sure you want to change this as primary?\");'>");
													out.println(
													i.getIsPrimary() == 1 ? " <img src='"+request.getContextPath()+"/styles/star.svg' alt='Star Icon' style='width: 20px; height: 20px;'"
															: "<img src='"+request.getContextPath()+"/styles/transparentstar.svg' alt='Star Icon' style='width: 20px; height: 20px;'");
													out.println("        </button>");
													out.println("    </form>");
													out.println("</td>");
													out.println("</tr>");
												}
											} else {
												out.println("<tr><td colspan='4'>No emails found.</td></tr>");
											}
					%>
				</tbody>
			</table>

			<%
			String result = (String) request.getAttribute("result");
					if (result != null)
						out.println(result);
			%>

			<button type="button" class="mybutton mt-2" data-bs-toggle="modal"
				data-bs-target="#exampleModal">Add new Email</button>


			<div class="modal fade" id="exampleModal" tabindex="-1"
				aria-labelledby="exampleModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<h1 class="modal-title fs-5" id="exampleModalLabel">Add new
								email</h1>
							<button type="button" class="btn-close" data-bs-dismiss="modal"
								aria-label="Close"></button>
						</div>
						<div class="modal-body">
							<div class="container">
								<form action="/email/<%=user.getUserId()%>?action=add"
									method="post">
									<div class="form-group">
										<label for="email">Email:</label> <input type="email"
											id="email" name="email" required>
									</div>
									<!--  <input type="hidden" name="user_id"
										value="<%=user.getUserId()%>">-->
									<input type="submit" class="mybutton mt-1" value="Add Email">
								</form>

							</div>
						</div>
					</div>
				</div>
			</div>



		</div>
	</div>
</body>
</html>