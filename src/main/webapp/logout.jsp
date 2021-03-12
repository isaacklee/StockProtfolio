<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
    String username = (String) session.getAttribute("username");
    if(username != null) {
        session.invalidate();
    }
    response.sendRedirect("https://localhost:8443/login.jsp");
%>
