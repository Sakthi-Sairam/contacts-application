<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.models.User" %>
<%@ page import="com.models.Contact" %>
<%@ page import="java.util.*" %>


<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    
    <link rel="stylesheet" type="text/css" href="./styles/dashboard.css">

    
</head>

<body>
	<div class="sidebar">
	  <a class="active" href="dashboard.jsp">All Contacts</a>
	<a class="" href="profile.jsp">Profile</a>
	  <a href="archived.jsp">Archieved</a>
	  <a href="#">Categories</a>
	  <a href="logout" class="logout">Logout</a>
	</div>
<div class="content">
    <h1 class="mt-3 mb-3 heading ">Welcome to Dashboard</h1>
    <%
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        Boolean contactsLoaded = (Boolean)session.getAttribute("contactsLoaded");
        if(contactsLoaded==null || !contactsLoaded){
        	response.sendRedirect("viewcontacts");
        }
    %>
    <h2 class="text-center">Hello <%= user.getFirstName() %>....</h2>
    
    <button type="button" class="mybutton" data-bs-toggle="modal" data-bs-target="#exampleModal">
  Add new Contact
</button>
        
    
    		<h3 class="heading my-4">All Contacts</h3>
    <a href="viewcontacts" class="mybutton">Refresh contacts</a>

    <table>
    <thead>
        <tr>
            <th>Contact Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Address</th>
            <th>Others</th>
        </tr>
    </thead>
    <tbody>
    
        <%
        List<Contact> contacts = user.getMyContacts();
        if (contacts != null && !contacts.isEmpty()) {
            for (Contact i : contacts) {
                out.println("<tr>");
                out.println("<td>" + i.getAlias_name() + "</td>");
                out.println("<td>" + i.getFriend_email() + "</td>");
                out.println("<td>" + i.getPhone() + "</td>");
                out.println("<td>" + i.getAddress() + "</td>");
                out.println("<td>");
                out.println("    <form action='deleteContact' method='post' style='display:inline;'>");
                out.println("        <input type='hidden' name='contactId' value='" + i.getMyContactsID() + "'>");
                out.println("        <button type='submit' class='btn btn-danger' title='Delete' onclick='return confirm(\"Are you sure you want to delete this contact?\");'>");
                out.println("            <i class='bi bi-trash3-fill'></i>");
                out.println("        </button>");
                out.println("    </form>");
                out.println("</td>");
                out.println("</tr>");
            }
        } else {
            out.println("<tr><td colspan='4'>No contacts found.</td></tr>");
        }
        %>
        
     	</tbody>
	</table>
	<div class="my-4 pb-5">
		<h3 class="heading mb-3">Favourites</h3>
	
	    <table>
    <thead>
        <tr>
            <th>Contact Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Address</th>
        </tr>
    </thead>
    <tbody>
    
        <%
        List<Contact> favourites = user.getFavourites();
        if (favourites != null && !favourites.isEmpty()) {
            for (Contact i : favourites) {
                out.println("<tr>");
                out.println("<td>" + i.getAlias_name() + "</td>");
                out.println("<td>" + i.getFriend_email() + "</td>");
                out.println("<td>" + i.getPhone() + "</td>");
                out.println("<td>" + i.getAddress() + "</td>");
                out.println("</tr>");
            }
        } else {
            out.println("<tr><td colspan='4'>No contacts found.</td></tr>");
        }
        %>
        
     	</tbody>
	</table>
	</div>
<!-- 	
	<h3 class="heading">Add new Contact</h3>
	<div class="container">
		<form action="addcontact" method="post" class="addcontact">
		    <div class="form-group">
		        <label for="friend_email">Email:</label>
		        <input type="email" id="friend_email" name="friend_email" required>
		    </div>
		    <div class="form-group">
		        <label for="alias_fnd_name">Alias:</label>
		        <input type="text" id="alias_fnd_name" name="alias_fnd_name" required>
		    </div>
		    <div class="form-group">
		        <label for="phone">Phone:</label>
		        <input type="text" id="phone" name="phone" required>
		    </div>
		    <div class="form-group">
		        <label for="address">Address:</label>
		        <input type="text" id="address" name="address">
		    </div>
		    <div class="form-group">
		        <label>Is Archived:</label><br>
		        <input type="radio" name="isArchived" value="1"> Yes
		        <input type="radio" name="isArchived" value="0" checked> No
		    </div>
		    <div class="form-group">
		        <label>Is Favorite:</label><br>
		        <input type="radio" name="isFavorite" value="1"> Yes
		        <input type="radio" name="isFavorite" value="0" checked> No
		    </div>
		    <input type="hidden" name="user_id" value="<%= user.getUserId() %>">
		    <input type="submit" class="mybutton" value="Add Contact">
		</form>
	</div>
 -->
	
	<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-xl">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="exampleModalLabel">Add new Contact</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
      	<div class="container">
		<form action="addcontact" method="post" class="addcontact">
		    <div class="form-group">
		        <label for="friend_email">Email:</label>
		        <input type="email" id="friend_email" name="friend_email" required>
		    </div>
		    <div class="form-group">
		        <label for="alias_fnd_name">Alias:</label>
		        <input type="text" id="alias_fnd_name" name="alias_fnd_name" required>
		    </div>
		    <div class="form-group">
		        <label for="phone">Phone:</label>
		        <input type="text" id="phone" name="phone" required>
		    </div>
		    <div class="form-group">
		        <label for="address">Address:</label>
		        <input type="text" id="address" name="address">
		    </div>
		    <div class="form-group">
		        <label>Is Archived:</label><br>
		        <input type="radio" name="isArchived" value="1"> Yes
		        <input type="radio" name="isArchived" value="0" checked> No
		    </div>
		    <div class="form-group">
		        <label>Is Favorite:</label><br>
		        <input type="radio" name="isFavorite" value="1"> Yes
		        <input type="radio" name="isFavorite" value="0" checked> No
		    </div>
		    <input type="hidden" name="user_id" value="<%= user.getUserId() %>">
		    <input type="submit" class="mybutton" value="Add Contact">
		</form>
	</div>
      </div>
    </div>
  </div>
</div>

    
</div>
    
</body>
</html>
