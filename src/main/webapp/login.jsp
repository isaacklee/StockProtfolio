<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="login.css">
<link href='https://fonts.googleapis.com/css?family=Montserrat' rel='stylesheet'>
<title>Stonks Login</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>
	<div id="body">
		<div class="headercontainer">
			<h3>USC CS 310 Stock Portfolio Management</h3>
		</div>
		<div class="container" method="POST">
			<form name="login" id="inner">
				<h1>Stonks!</h1>
				<font id="error" class="error"></font>
				<input id="username" type="text" name="username" placeholder="username">
				<input id="password" type="password" name="password" placeholder="password">
				<input class="button" type="submit" value="Sign In" name="">
				<div id="bottom">Don't have an account?<a href="signup.jsp">Sign Up</a>
				</div>
			</form>
            		<script src="login.js"></script>
		</div>
	</div>
</body>
</html>
