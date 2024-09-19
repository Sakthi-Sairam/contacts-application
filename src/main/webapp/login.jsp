<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" type="text/css" href="login.css">
</head>
<body>
    <div class="container">
        <h2 class="title">Login Page</h2>
        <form class="login-form" action="login" method="post">
            <label for="email">Email:</label>
            <input type="text" name="email" id="email" class="input-field" required>
            <label for="password">Password:</label>
            <input type="password" name="password" id="password" class="input-field" required>
            <input type="submit" value="Login" class="submit-button">
        </form>
    </div>
</body>
</html>
