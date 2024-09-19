<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Register</title>
    
</head>
<body>
    <h2>Register</h2>
    <form action="register" method="post">
        Email: <input type="text" name="email" required><br>
        Password: <input type="password" name="password" required><br>
        First Name: <input type="text" name="firstName" required><br>
        Last Name: <input type="text" name="lastName"><br>
        Age: <input type="number" name="age"><br>
        Address: <input type="text" name="address"><br>
        Phone: <input type="text" name="phone" required><br>
        <input type="submit" value="Register">
    </form>
</body>
</html>