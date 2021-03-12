package csci310.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import csci310.DatabaseMethods;

public class RegisterServletTest extends Mockito {

	@Before
	public void setUp() {
		try {
			DatabaseMethods.deleteDB();
			DatabaseMethods.createDB();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void testDoPostHttpServletRequestHttpServletResponse() {
		RegisterServlet servlet = new RegisterServlet();
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);

		/*
		 * Each of the following blocks of code is of the same format. The comment at
		 * the top of each describes the tested use case
		 */

		// If the username is too short
		when(request.getParameter("username")).thenReturn("123");
		when(request.getParameter("password")).thenReturn("12345678");
		when(request.getParameter("confirmpassword")).thenReturn("12345678");
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
		when(request.getParameter("confirmpassword")).thenReturn("12345678");
		HttpSession session = mock(HttpSession.class);
		when(request.getSession()).thenReturn(session);
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
		when(request.getParameter("confirmpassword")).thenReturn("123");
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
		when(request.getParameter("confirmpassword")).thenReturn("abcdefghijklmnopqrstu");
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

		// Yes, block commenting is disgusting
		// Yes, I did it for symmetry with LoginServletTest.java
		// If the user doesn't exist in the DB
//		when(request.getParameter("username")).thenReturn("1234");
//		when(request.getParameter("password")).thenReturn("12345678");
//		when(request.getParameter("confirmpassword")).thenReturn("12345678");
//		StringWriter stringWriter5 = new StringWriter();
//		PrintWriter writer5 = new PrintWriter(stringWriter5);
//		try {
//			when(response.getWriter()).thenReturn(writer5);
//			servlet.doPost(request, response);
//			writer5.flush();
//			assertEquals("The entered username/password does not exist", stringWriter5.toString());
//		} catch (IOException e) {
//			System.out.println(e.getMessage());
//		}

		// The following test assume the username exists in the DB
		try {
			DatabaseMethods.addUserToDB("1234", "12345678");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		// If the user exists already
		try {
			DatabaseMethods.addUserToDB("1243", "12345678");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		when(request.getParameter("username")).thenReturn("1234");
		when(request.getParameter("password")).thenReturn("12345678");
		when(request.getParameter("confirmpassword")).thenReturn("12345678");
		StringWriter stringWriter6 = new StringWriter();
		PrintWriter writer6 = new PrintWriter(stringWriter6);
		try {
			when(response.getWriter()).thenReturn(writer6);
			servlet.doPost(request, response);
			writer6.flush();
			assertEquals("The entered username is taken", stringWriter6.toString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		// Reordering tests would mean that we wouldn't have to delete
		// but I desire symmetry ~~~
		try {
			DatabaseMethods.deleteDB();
			DatabaseMethods.createDB();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		// If all is well
		when(request.getParameter("username")).thenReturn("1234");
		when(request.getParameter("password")).thenReturn("12345678");
		when(request.getParameter("confirmpassword")).thenReturn("12345678");
		StringWriter stringWriter7 = new StringWriter();
		PrintWriter writer7 = new PrintWriter(stringWriter7);
		try {
			when(response.getWriter()).thenReturn(writer7);
			servlet.doPost(request, response);
			writer7.flush();
			assertEquals("Success", stringWriter7.toString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		/*
		 * This case is at the bottom because it is the only case that doesn't appear in
		 * LoginServletTest (where these were copied from)
		 */
		// If the password and the confirmpassword don't match
		when(request.getParameter("username")).thenReturn("1234");
		when(request.getParameter("password")).thenReturn("12345678");
		when(request.getParameter("confirmpassword")).thenReturn("Humans Love Waffles");
		StringWriter stringWriter8 = new StringWriter();
		PrintWriter writer8 = new PrintWriter(stringWriter8);
		try {
			when(response.getWriter()).thenReturn(writer8);
			servlet.doPost(request, response);
			writer8.flush();
			assertEquals("Entered passwords do not match", stringWriter8.toString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link csci310.servlets.RegisterServlet#validation(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testValidation() {
		RegisterServlet servlet = new RegisterServlet();
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);

		// If username is too short
		String msg = servlet.validation("123", "12345678", "12345678");
		assertEquals(msg, "Please enter a username with at least 4 characters");

		// If username is too long
		msg = servlet.validation("abcdefghijklmnopqrstu", "12345678", "12345678");
		assertEquals(msg, "Please enter a username with 20 or fewer characters");

		// If password is too short
		msg = servlet.validation("1234", "123", "123");
		assertEquals(msg, "Please enter a password with 8 or more characters");

		// If password is too short
		msg = servlet.validation("1234", "abcdefghijklmnopqrstu", "abcdefghijklmnopqrstu");
		assertEquals(msg, "Please enter a password with 20 or fewer characters");

		// if password and confirm don't match
		msg = servlet.validation("1234", "12345678", "123456789");
		assertEquals(msg, "Entered passwords do not match");
	}
}
