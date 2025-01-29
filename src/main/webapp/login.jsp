<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    
    <link rel="stylesheet" type="text/css" href="./styles/login.css">
    <style type="text/css">
    body {
    font-family: "Lato", sans-serif;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    margin: 0;
    /* font-family: lato; */
    background: aliceblue;
    background-image: url(https://images.pexels.com/photos/139366/pexels-photo-139366.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1);
    background-repeat: no-repeat;
    background-size: cover;
    background-position: center;
    backdrop-filter: blur(3px) brightness(0.8);
}
.container{
	    color: white;
    text-align: center;
    background: linear-gradient(109.6deg, rgb(102, 203, 149) 11.2%, rgb(39, 210, 175) 98.7%);
    padding: 6vw;
    border-radius: 36px;
    box-shadow: rgba(0, 0, 0, 0.56) 0px 22px 70px 4px;
}
.login-form{
	    display: flex;
    flex-direction: column;
    gap: 3px;
}
.title{
	font-size: 3vw;
	font-weight: 700;

}

input{
   /*  border-radius: 34px; */
    padding: 10px 19px;
    border: none;
    margin-bottom: 13px;
}
label{
	font-size: 2vw;
    font-weight: 300;
}
.btn {
    background-color: #04AA6D;
    color: white;
    padding: 10px 15px;
    border: none;
    cursor: pointer;
    text-decoration: none;
    margin-bottom: 1vw;
    display: inline-block;
}

a{
    text-decoration: none;
    color: #e7ecf0b3;
}
    
    </style>
    
</head>
<body>
    <div class="container">
        <h2 class="title">Login Page</h2>
        <form class="login-form" action="login" method="post">
            <label for="email">Email</label>
            <input type="text" name="email" id="email" class="input-field" required>
            <label for="password">Password</label>
            <input type="password" name="password" id="password" class="input-field" required>
            <input type="submit" value="Login" class="btn">
            <a href="register">Not have account?</a>
            <%
            String errmsg = (String) request.getAttribute("errorMessage");
            if(errmsg!=null) out.println(errmsg);
            %>
        </form>
        <!-- 
        <form action="googleLogin">
        <input type="submit" value="sign up with google" class="btn">
        </form>  -->
    </div>
</body>
</html>
