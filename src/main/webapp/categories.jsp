<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.models.User" %>
<%@ page import="com.models.CategoryDetails" %>
<%@ page import="com.models.Contact" %>
<%@ page import="com.dao.CategoriesDao" %>
<%@page import="com.dao.ContactDao"%>
<%@ page import="java.util.*" %>
<%@ page import="com.filters.AuthFilter" %>
<!DOCTYPE html>	
<html>
<head>
    <meta charset="UTF-8">
    <title>Categories</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="./styles/dashboard.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
:root {
    --primary-color: #2A2F4F;
    --secondary-color: #917FB3;
    --accent-color: #E5BEEC;
    --background-color: #FDE2F3;
    --text-color: #2A2F4F;
}

.container2 {
    background: white;
    padding: 2rem;
    border-radius: 1.5rem;
    box-shadow: 0 4px 20px rgba(0,0,0,0.1);
    margin: 2rem auto;
    max-width: 1200px;
}

h1, h3 {
    color: var(--primary-color);
    margin-bottom: 1.5rem;
}

.form-control {
    border: 2px solid #eee !important;
    border-radius: 8px !important;
    padding: 0.8rem !important;
    transition: all 0.3s ease;
}

.form-control:focus {
    border-color: var(--secondary-color) !important;
    box-shadow: none !important;
}

.mybutton {
    background: var(--secondary-color);
    color: white !important;
    border: none;
    padding: 0.8rem 1.5rem;
    border-radius: 8px;
    transition: all 0.3s ease;
    margin-top: 1rem;
}

.mybutton:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 15px rgba(42,47,79,0.2);
}

.table {
    width: 100%;
    border-collapse: collapse;
    background: white;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 4px 6px rgba(0,0,0,0.05);
    margin: 1.5rem 0;
}

.table th {
    background-color: var(--primary-color);
    color: white;
    padding: 1rem !important;
}

.table td {
    padding: 1rem !important;
}

.table tr:nth-child(even) {
    background-color: #f8f9fa;
}

.table tr:hover {
    background-color: #f1f3f5;
}

.btn-danger {
    background: #dc3545 !important;
    border: none;
    padding: 0.5rem 1rem;
    border-radius: 8px;
    transition: all 0.3s ease;
}

.btn-danger:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 15px rgba(220,53,69,0.2);
}

.form-group {
    margin-bottom: 1.5rem;
}

.form-group label {
    font-weight: 500;
    margin-bottom: 0.5rem;
    display: block;
    color: var(--primary-color);
}

.form-group select {
    width: 100%;
    padding: 0.8rem;
    border: 2px solid #eee;
    border-radius: 8px;
    transition: all 0.3s ease;
}

.form-group select:focus {
    border-color: var(--secondary-color);
    outline: none;
}

@media (max-width: 768px) {
    .container2 {
        padding: 1rem;
        margin: 1rem;
    }

    h1 {
        font-size: 1.8rem;
    }

    h3 {
        font-size: 1.4rem;
    }

    .table th, .table td {
        padding: 0.8rem !important;
    }
}
</style>
</head>
<body>
<div class="sidebar">
    <div class="sidebar-header">
        <h2>Contact Manager</h2>
    </div>
    <nav class="sidebar-nav">
        <a href="/contacts">
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
        <a class="active" href="/categories">
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
    <div class="container2">
        <h1 class="mt-3">Manage Categories</h1>
        
        <%
                User user = (User) request.getAttribute("user");

				List<CategoryDetails> categories = (List<CategoryDetails>) request.getAttribute("categories");
				List<Contact> contacts = (List<Contact>) request.getAttribute("contacts");

                String addResult = (String)request.getAttribute("addResult");
                if (addResult!=null)out.println(addResult);
          %>

        <form action="/categories?action=create" method="post">
            <div class="mb-3">
                <label for="categoryName" class="form-label">Category Name</label>
                <input type="text" class="form-control" id="categoryName" name="categoryName" required>
            </div>
            <button type="submit" class="mybutton">Create Category</button>
        </form>
        </div>
        
		<div class="container2">
        
        

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
                    out.println( String.format("<td><a href='/categories/%d' class='mybutton'>",category.getCategoryId())+ category.getCategoryName() + "</a></td>");
                    out.print("<td>");
                    if (catcontacts.isEmpty()) {
                        out.print("No contacts found.");
                    } else {
                        for (Contact contact : catcontacts) {
                            out.println(contact.getAlias_name());
                        }
                    }
                    out.println("</td>");
                    out.println("<td>");
                    out.println("    <form action='/categories/" + category.getCategoryId() + "?action=delete' method='post' style='display:inline;'>");
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

    </div>
    </div>
</body>
</html>
