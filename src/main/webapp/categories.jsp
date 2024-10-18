<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.models.User" %>
<%@ page import="com.models.CategoryDetails" %>
<%@ page import="com.models.Contact" %>
<%@ page import="com.Dao.CategoriesDao" %>
<%@ page import="java.util.*" %>
<%@ page import="com.filters.SessionFilter" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Categories</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="./styles/dashboard.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    
</head>
<body>
    	<div class="sidebar">
	  <a class="" href="dashboard.jsp">All Contacts</a>
	<a class="" href="profile.jsp">Profile</a>
	  <a class="" href="archived.jsp">Archieved</a>
	  <a href="categories.jsp" class="active">Categories</a>
	  <a href="logout" class="logout">Logout</a>
	</div>
	<div class="content">
    <div class="container2">
        <h1 class="mt-3">Manage Categories</h1>
        
        <%
    		User user = (User)SessionFilter.getCurrentUser();


            List<CategoryDetails> categories = CategoriesDao.getCategoriesByUserId(user.getUserId());
            List<Contact> contacts = user.getMyContacts();
        %>
        
        <%
        String addResult = (String)request.getAttribute("addResult");
        if (addResult!=null)out.println(addResult);
        
        %>

        <form action="createCategory" method="post">
            <div class="mb-3">
                <label for="categoryName" class="form-label">Category Name</label>
                <input type="text" class="form-control" id="categoryName" name="categoryName" required>
                <input type="hidden" name="userId" value="<%=user.getUserId()%>">
            </div>
            <button type="submit" class="mybutton">Create Category</button>
        </form>

        <h3 class="mt-4">Existing Categories</h3>
        <table class="table">
            <thead>
                <tr>
                    <th>Category Name</th>
                    <th>Contacts</th>
                    <th>Others</th>
                </tr>
            </thead>
            <tbody>
                <%
                for (CategoryDetails category : categories) {
                    List<Contact> catcontacts = CategoriesDao.getContactsByCategoryId(category.getCategoryId());
                    out.println("<tr>");
                    out.println("<td>" + category.getCategoryName() + "</td>");
                    out.print("<td>");
                    if (catcontacts.isEmpty()) {
                        out.print("No contacts found.");
                    } else {
                        for (Contact contact : catcontacts) {
                            out.println(contact.getAlias_name() + " (" + contact.getFriend_email() + "), ");
                        }
                    }
                    out.println("</td>");
                    out.println("<td>");
                    out.println("    <form action='deleteCategory' method='post' style='display:inline;'>");
                    out.println("        <input type='hidden' name='categoryId' value='" + category.getCategoryId() + "'>");
                    out.println("        <button type='submit' class='btn btn-danger' title='Delete' onclick='return confirm(\"Are you sure you want to delete this category?\");'>");
                    out.println("            <i class='bi bi-trash3-fill'></i>");
                    out.println("        </button>");
                    out.println("    </form>");
                    out.println("</td>");
                    out.println("</tr>");
                }
                %>
            </tbody>
        </table>
    </div>
    
    
        <h3>Assign Contact to Category</h3>
        <form action="assignContactToCategory" method="post">
            <div class="form-group">
                <label for="category">Select Category:</label>
                <select id="category" name="categoryId" required>
                    <%
                        for (CategoryDetails category : categories) {
                            out.println("<option value='" + category.getCategoryId() + "'>" + category.getCategoryName() + "</option>");
                        }
                    %>
                </select>
            </div>

            <div class="form-group">
                <label for="contact">Select Contact:</label>
                <select id="contact" name="contactId" required>
                    <%
                        for (Contact contact : contacts) {
                            out.println("<option value='" + contact.getMyContactsID() + "'>" + contact.getAlias_name() + "</option>");
                        }
                    %>
                </select>
            </div>
            
            <button type="submit" class="mybutton">Assign Contact</button>
        </form>

    </div>
    </div>
</body>
</html>
