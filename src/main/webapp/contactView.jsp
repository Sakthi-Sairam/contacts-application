<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.models.Contact" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Contact Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <% Contact contact = (Contact) request.getAttribute("contact"); %>
    <div class="container mt-5">
        <h2>Contact Details</h2>
        <form action="/contacts/<%= contact.getMyContactsID()%>?action=edit" method="post">
            <div class="mb-3">
                <label>Name:</label>
                <input type="text" name="alias_fnd_name" class="form-control" value="<%= contact.getAlias_name() %>" required>
            </div>
            <div class="mb-3">
                <label>Email:</label>
                <input type="email" name="friend_email" class="form-control" value="<%= contact.getFriend_email() %>" required>
            </div>
            <div class="mb-3">
                <label>Phone:</label>
                <input type="text" name="phone" class="form-control" value="<%= contact.getPhone() %>" required>
            </div>
            <div class="mb-3">
                <label>Address:</label>
                <input type="text" name="address" class="form-control" value="<%= contact.getAddress() %>" required>
            </div>
            <button type="submit" class="btn btn-primary">Update</button>
            <a href="/contacts" class="btn btn-secondary">Cancel</a>
        </form>
    </div>
</body>
</html>
    