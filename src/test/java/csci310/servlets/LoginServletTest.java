package csci310.servlets;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import csci310.DatabaseMethods;

public class LoginServletTest extends Mockito {
	
	protected Connection connection = null;
	
	@Before
	public void setUp() { 
		try {
			DatabaseMethods.deleteDB();
			DatabaseMethods.createDB();
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void testDoPostHttpServletRequestHttpServletResponse() {
		LoginServlet servlet = new LoginServlet();
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		
		/*
		 * Each of the following blocks of code is of the same format.
		 * The comment at the top of each describes the tested use case
		 */
		
		// If the username is too short
		when(request.getParameter("username")).thenReturn("123");
		when(request.getParameter("password")).thenReturn("12345678");
		StringWriter stringWriter1 = new StringWriter();
		PrintWriter writer1 = new PrintWriter(stringWriter1);
		try {
			when(response.getWriter()).thenReturn(writer1);
			servlet.doPost(request, response);
			writer1.flush();
			assertEquals("Please enter a username with at least 4 characters", stringWriter1.toString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		
		// If the username is too long
		when(request.getParameter("username")).thenReturn("abcdefghijklmnopqrstu");
		when(request.getParameter("password")).thenReturn("12345678");
		StringWriter stringWriter2 = new StringWriter();
		PrintWriter writer2 = new PrintWriter(stringWriter2);
		try {
			when(response.getWriter()).thenReturn(writer2);
			servlet.doPost(request, response);
			writer2.flush();
			assertEquals("Please enter a username with 20 or fewer characters", stringWriter2.toString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		// If the password is too short
		when(request.getParameter("username")).thenReturn("1234");
		when(request.getParameter("password")).thenReturn("123");
		StringWriter stringWriter3 = new StringWriter();
		PrintWriter writer3 = new PrintWriter(stringWriter3);
		try {
			when(response.getWriter()).thenReturn(writer3);
			servlet.doPost(request, response);
			writer3.flush();
			assertEquals("Please enter a password with 8 or more characters", stringWriter3.toString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		// If the password is too long
		when(request.getParameter("username")).thenReturn("1234");
		when(request.getParameter("password")).thenReturn("abcdefghijklmnopqrstu");
		StringWriter stringWriter4 = new StringWriter();
		PrintWriter writer4 = new PrintWriter(stringWriter4);
		try {
			when(response.getWriter()).thenReturn(writer4);
			servlet.doPost(request, response);
			writer4.flush();
			assertEquals("Please enter a password with 20 or fewer characters", stringWriter4.toString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		// If the user doesn't exist in the DB
		when(request.getParameter("username")).thenReturn("1234");
		when(request.getParameter("password")).thenReturn("12345678");
		StringWriter stringWriter5 = new StringWriter();
		PrintWriter writer5 = new PrintWriter(stringWriter5);
		try {
			when(response.getWriter()).thenReturn(writer5);
			servlet.doPost(request, response);
			writer5.flush();
			assertEquals("The entered username/password does not exist", stringWriter5.toString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		// If the user fails login three times, then user is locked out
		when(request.getParameter("username")).thenReturn("1234");
		when(request.getParameter("password")).thenReturn("12345678");
		StringWriter stringWriter8 = new StringWriter();
		PrintWriter writer8 = new PrintWriter(stringWriter8);
		try {
			when(response.getWriter()).thenReturn(writer8);
			servlet.doPost(request, response);
			servlet.doPost(request, response);
			writer8.flush();
			assertEquals("You have been locked out from logging in for", stringWriter8.toString().substring(44, 88));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	
		
		// The following tests assume the username exists in the DB
		try {
			DatabaseMethods.addUserToDB("1234", "12345678");
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		// If the user exists, but the password is wrong
		try {
			DatabaseMethods.addUserToDB("1243", "12345678");
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		when(request.getParameter("username")).thenReturn("1234");
		when(request.getParameter("password")).thenReturn("12345670");
		StringWriter stringWriter6 = new StringWriter();
		PrintWriter writer6 = new PrintWriter(stringWriter6);
		try {
			servlet.resetFLC();
			when(response.getWriter()).thenReturn(writer6);
			servlet.doPost(request, response);
			writer6.flush();
			assertEquals("The entered username/password does not exist", stringWriter6.toString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		// If all is well
		when(request.getParameter("username")).thenReturn("1234");
		when(request.getParameter("password")).thenReturn("12345678");
		HttpSession session = mock(HttpSession.class);
		when(request.getSession()).thenReturn(session);
		StringWriter stringWriter7 = new StringWriter();
		PrintWriter writer7 = new PrintWriter(stringWriter7);
		try {
			when(response.getWriter()).thenReturn(writer7);
			servlet = new LoginServlet();
			servlet.doPost(request, response);
			writer7.flush();
			assertEquals("Success", stringWriter7.toString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void testLoginValidation()
	{
		LoginServlet servlet = new LoginServlet();

		//If username.length() < 4
		String msg = servlet.loginValidation("abc", "abc");
		assertEquals("Testing too short username", msg, "Please enter a username with at least 4 characters");
		
		//If username.length() > 20
		msg = servlet.loginValidation("abcdefghijklmnopqrstuvwxyz", "abc");
		assertEquals("Testing too long username", msg, "Please enter a username with 20 or fewer characters");
		
		//If password.length() < 8
		msg = servlet.loginValidation("abcdef", "abc");
		assertEquals("Testing too short password", msg, "Please enter a password with 8 or more characters");
		
		//If password.length() > 20
		msg = servlet.loginValidation("abcdef", "abcdefghijklmnopqrstuvwxyz");
		assertEquals("Testing too long password", msg, "Please enter a password with 20 or fewer characters");
	}
	
	@Test
	public void testResetFLC()
	{
		LoginServlet servlet = new LoginServlet();
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		
		//Initial fail count
		int failureCount = servlet.failedLoginCounter;
		
		//Fail login and check to see if the counter incremented
		when(request.getParameter("username")).thenReturn("abcd");
		when(request.getParameter("password")).thenReturn("12341234");
		StringWriter stringWriter1 = new StringWriter();
		PrintWriter writer1 = new PrintWriter(stringWriter1);
		try {
			when(response.getWriter()).thenReturn(writer1);
			servlet.doPost(request, response);
			writer1.flush();
			assertEquals(failureCount+1, servlet.failedLoginCounter);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		//Reset login counter and check to see if it work
		servlet.resetFLC();
		assertEquals(0, servlet.failedLoginCounter);
	}
}
