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

<link rel="stylesheet" type="text/css" href="./styles/dashboard.css">

<style type="text/css">
* {
	box-sizing: border-box;
}

body {
	margin: 0;
	font-family: "Lato", sans-serif;
}

.sidebar {
	margin: 0;
	padding: 0;
	width: 200px;
	background-color: #f1f1f1;
	position: fixed;
	height: 100%;
	overflow: hidden;
}

.sidebar a {
	display: block;
	color: black;
	padding: 16px;
	text-decoration: none;
}

.sidebar a.active {
	background-color: #04AA6D;
	color: white;
}

.sidebar a:hover:not(.active) {
	background-color: #555;
	color: white;
}

.logout {
	position: absolute;
	bottom: 0;
	width: 100%;
}

div.content {
	margin-left: 200px;
	padding: 1px 16px;
	height: 1000px;
	    display: flex;
    justify-content: space-around;
}

@media screen and (max-width: 700px) {
	.sidebar {
		width: 100%;
		height: auto;
		position: relative;
	}
	.sidebar a {
		float: left;
	}
	div.content {
		margin-left: 0;
	}
}

@media screen and (max-width: 400px) {
	.sidebar a {
		text-align: center;
		float: none;
	}
}

table {
	width: 100%;
	border-collapse: collapse;
}

table, th, td {
	border: 1px solid #ddd;
}

th, td {
	padding: 8px;
	text-align: left;
}

th {
	background-color: #f2f2f2;
}

.profile {
	width: fit-content;
	padding: 40px;
	background: grey;
	height: fit-content;
	border-radius: 36px;
	text-align: center;
	margin-top: 50px;
	color: white;
}

img {
	width: 50%;
	height: 50%;
	border-radius: 50%;
}
.mycontainer{
    padding: 50px;
}
</style>


</head>
<body>
	<div class="sidebar">
		<a class="" href="dashboard.jsp">All Contacts</a> <a class="active"
			href="profile.jsp">Profile</a> <a href="archived.jsp">Archieved</a> <a
			href="categories.jsp">Categories</a> <a href="logout" class="logout">Logout</a>
	</div>
	<%
	User user = (User) session.getAttribute("user");
	if (user == null) {
		response.sendRedirect("login.jsp");
		return;
	}
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
							//                out.println("<td>" + i.getIsPrimary() + "</td>");
							out.println("<td>");
							out.println("    <form action='primaryemail' method='post' style='display:inline;'>");
							out.println("        <input type='hidden' name='user_id' value='" + user.getUserId() + "'>");
							out.println("        <input type='hidden' name='primaryEmailId' value='" + user.getPrimaryEmailId() + "'>");
							out.println("        <input type='hidden' name='emailId' value='" + i.getId() + "'>");
							out.println(
							"        <button type='submit' class='btn btn-warning' title='Delete' onclick='return confirm(\"Are you sure you want to change this as primary?\");'>");
							out.println(
							i.getIsPrimary() == 1 ? " <img src='styles/star.svg' alt='Star Icon' style='width: 20px; height: 20px;'"
									: "<img src='styles/transparentstar.svg' alt='Star Icon' style='width: 20px; height: 20px;'");
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
								<form action="addemail" method="post">
									<div class="form-group">
										<label for="email">Email:</label> <input type="email"
											id="email" name="email" required>
									</div>
									<input type="hidden" name="user_id"
										value="<%=user.getUserId()%>">
									<input type="submit"
										class="mybutton mt-1" value="Add Email">
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