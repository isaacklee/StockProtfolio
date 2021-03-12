<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% 
	response.sendRedirect("https://localhost:8443/login.jsp");
%>
<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link rel="stylesheet" type="text/css" href="style.css">
<link href='https://fonts.googleapis.com/css?family=Montserrat' rel='stylesheet'>
<title>Index</title>
</head>
<body>  
	<div id="body">
		<div class="headercontainer">
			<h3>USC CS 310 Stock Portfolio Management</h3>
		</div>
	 	<div class = "container">
			<div id = "inner">
				<h1>Stonks!</h1>	
				<p><a class="button" href="dashboard.jsp"><span> Stock Analysis </span></a></p>
	      			<p><a class="button" href="login.jsp"><span> Sign in </span></a></p>
	     			<p><a class="button" href="signup.jsp"><span> Register</span></a></p>		
			</div>	
	 	</div>	
 	</div>
</body>
</html>
