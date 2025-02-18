<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error</title>
    <style>
        body { font-family: Arial, sans-serif; text-align: center; margin-top: 50px; }
        .error-container { background: #f8d7da; padding: 20px; border-radius: 5px; display: inline-block; }
        h1 { color: #721c24; }
        p { color: #721c24; }
    </style>
</head>
<body>

	<%
	String errorMessageAttribute = (String)request.getAttribute("errorMessage");
	String errorMessageParameter = (String)request.getParameter("errorMessage");

	
	
	%>
    <div class="error-container">
        <h1>Oops! Something went wrong.</h1>
        
        <%if(errorMessageAttribute!=null){%> <p><%=errorMessageAttribute%></p> <%} %>
		<%if(errorMessageParameter!=null){%> <p><%=errorMessageParameter%></p> <%} %>
        <a href="/contacts">Go to Home</a>
    </div>
</body>
</html>
