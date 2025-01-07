<%@page import="com.Dao.ContactDao"%>
<%@page import="com.Dao.userDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.models.User" %>
<%@ page import="com.models.Contact" %>
<%@ page import="com.filters.SessionFilter" %>
<%@ page import="java.util.*" %>


<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/styles/dashboard.css">
    
</head>

<body>
	<div class="sidebar">
	  <a class="active" href="contacts">All Contacts</a>
	<a class="" href="profile">Profile</a>
	  <a href="archived">Archived</a>
	  <a href="categories">Categories</a>
	  <a href="logout" class="logout">Logout</a>
	</div>
<div class="content">

	<%
		String errorMessage = (String) request.getParameter("errorMessage");
		if (errorMessage != null && !errorMessage.isEmpty()) {
	%>
		<div class="alert alert-danger alert-dismissible fade show" role="alert">
		  <strong>ERROR</strong> <%= errorMessage %>
		  <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
		</div>
	<%
		}
	%>


    <h1 class="mt-3 mb-3 heading ">Welcome to Dashboard</h1>
    <%
    	User user = (User) request.getAttribute("user");
	    List<Contact> contacts = (List<Contact>) request.getAttribute("contacts");
        List<Contact> favourites = (List<Contact>) request.getAttribute("favourites");
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
        if (contacts != null && !contacts.isEmpty()) {
            for (Contact i : contacts) {
                out.println("<tr>");
                out.println("<td>" + i.getAlias_name() + "</td>");
                out.println("<td>" + i.getFriend_email() + "</td>");
                out.println("<td>" + i.getPhone() + "</td>");
                out.println("<td>" + i.getAddress() + "</td>");
                out.println("<td>");
                out.println("    <form action='/contacts/" + i.getMyContactsID() + "?action=delete' method='post' style='display:inline;'>");
                out.println("        <button type='submit' class='btn btn-danger' title='Delete' onclick='return confirm(\"Are you sure you want to delete this contact?\");'>");
                out.println("            <i class='bi bi-trash3-fill'></i>");
                out.println("        </button>");
                out.println("    </form>");
                out.println("<button type='button' class='btn btn-primary' data-bs-toggle='modal' data-bs-target='#editModal" + i.getMyContactsID() + "'><i class=\"bi bi-pencil-square\"></i></button>");
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

	<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-xl">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="exampleModalLabel">Add new Contact</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
      	<div class="container">
		<form action="/contacts?action=add" method="post" class="addcontact">
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
		    <input type="hidden" name="user_id" value="<%=user.getUserId()%>">
		    <input type="submit" class="mybutton" value="Add Contact">
		</form>
	</div>
      </div>
    </div>
  </div>
</div>

<% 
if (contacts != null && !contacts.isEmpty()) {
    for (Contact i : contacts) {
%>

<div class="modal fade" id="editModal<%= i.getMyContactsID() %>" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-xl">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="editModalLabel">Edit Contact</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <div class="container">
          <form action="/contacts/<%=i.getMyContactsID()%>?action=edit" method='post' class="editcontact addcontact">
            <input type="hidden" name="contactId" value="<%= i.getMyContactsID() %>">
            <div class="form-group">
                <label for="friend_email">Email:</label>
                <input type="email" id="friend_email" name="friend_email" value="<%= i.getFriend_email() %>" required>
            </div>
            <div class="form-group">
                <label for="alias_fnd_name">Alias:</label>
                <input type="text" id="alias_fnd_name" name="alias_fnd_name" value="<%= i.getAlias_name() %>" required>
            </div>
            <div class="form-group">
                <label for="phone">Phone:</label>
                <input type="text" id="phone" name="phone" value="<%= i.getPhone() %>" required>
            </div>
            <div class="form-group">
                <label for="address">Address:</label>
                <input type="text" id="address" name="address" value="<%= i.getAddress() %>">
            </div>
            <div class="form-group">
                <label>Is Archived:</label><br>
                <input type="radio" name="isArchived" value="1" <%= i.getIsArchived()==1 ? "checked" : "" %>> Yes
                <input type="radio" name="isArchived" value="0" <%= i.getIsArchived()==0 ? "checked" : "" %>> No
            </div>
            <div class="form-group">
                <label>Is Favorite:</label><br>
                <input type="radio" name="isFavorite" value="1" <%= i.getIsFavorite()==1 ? "checked" : "" %>> Yes
                <input type="radio" name="isFavorite" value="0" <%= i.getIsFavorite()==0 ? "checked" : "" %>> No
            </div>
            <input type="submit" class="mybutton" value="Update Contact">
          </form>
        </div>
      </div>
    </div>
  </div>
</div>
<%
            }
        }
%>



    
</div>
    
</body>
</html>
