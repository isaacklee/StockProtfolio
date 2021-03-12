<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% String username = (String) session.getAttribute("username");
   if(username != null) {
	   response.sendRedirect("https://localhost:8443/dashboard.jsp");
   }
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="login.css">
<link href='https://fonts.googleapis.com/css?family=Montserrat' rel='stylesheet'>
<title>Stonks Sign Up</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>
	<div id="body">
		<div class="headercontainer">
			<h3>USC CS 310 Stock Portfolio Management</h3>
		</div>
		<div class="container">
			<form name = "signup" id="inner" method="POST" >
				<h1>Stonks!</h1>
				<font id="usernameError" class="error"></font>
				<input id = "username" type="text" name="username" placeholder="username">
				<font id="passwordError" class="error"></font>
				<input id = "password" type="password" name="password" placeholder="password">
				<input id ="confirmpassword" type="password" name="confirmpassword" placeholder="confirm password">
				<input class="button" type="submit" value="Create User" name="">
				<div id="bottom">Already have an account?<a href="login.jsp">Cancel</a>
				</div>
			</form>
			<script src="register.js"></script>
		</div>
	</div>
</body>
</html>
