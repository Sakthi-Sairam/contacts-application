<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Register</title>
    
    
    <style>
body {
    font-family: "Lato", sans-serif;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    margin: 0;
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
.reg-form{
    text-align: end;
}
.title{
	margin-top:0;
	font-size: 4vw;
	font-weight: 700;

}

input{
    /* border-radius: 34px; */
    padding: 10px 19px;
    border: none;
    margin-bottom: 15px;
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

    <link rel="stylesheet" type="text/css" href="./styles/register.css">
    
</head>
<body>
<div class="container">
    <h2 class="title">Register</h2>
    <form action="register" method="post" class="reg-form" onsubmit="return validateForm()">
        Email: <input type="email" name="email" required><br>
        Password: <input type="password" name="password" required><br>
        First Name: <input type="text" name="firstName" required><br>
        Last Name: <input type="text" name="lastName"><br>
        Age: <input type="number" name="age"><br>
        Address: <input type="text" name="address"><br>
        Phone: <input type="text" name="phone" id="phone" required><br>
        <input type="submit" id="submit" value="Register" class="btn"><br>
        <a href="login">Already have account?</a>
    </form>
        <%
        String result = (String)request.getAttribute("result");
        if(result!=null) out.println(result);
    	%>
  </div>
  
  <script type="text/javascript">
  function validateForm() {
      var phoneInput = document.getElementById("phone");
      var mobPattern = /^[1-9]{1}[0-9]{9}$/;

      if (!mobPattern.test(phoneInput.value)) {
          alert("Please enter a valid mobile number.");
          phoneInput.focus();
          return false;
      }
      return true;
  }
</script>

</body>
</html>