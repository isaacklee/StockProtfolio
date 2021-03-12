/**
 * 
 */
package csci310.servlets;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import csci310.DatabaseMethods;

/**
 * @author vonzg
 *
 */
public class AddStockServletTest extends Mockito {

	protected Connection connection = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		DatabaseMethods.deleteDB();
		DatabaseMethods.createDB();
	}

	/**
	 * Test method for
	 * {@link csci310.servlets.AddStockServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testDoPost() throws SQLException {
		DatabaseMethods.addUserToDB("1234", "12345678");
		
		AddStockServlet servlet = new AddStockServlet();
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		
		when(request.getSession()).thenReturn(session);
		/*
		 * Each of the following blocks of code is of the same format. The comment at
		 * the top of each describes the tested use case
		 */

		// Everything entered correctly
		when(session.getAttribute("username")).thenReturn("1234");
		when(request.getParameter("stockTicker")).thenReturn("MSFT");
		when(request.getParameter("numOfShares")).thenReturn("42");
		when(request.getParameter("datePurchased")).thenReturn("2020-01-01");
		when(request.getParameter("dateSold")).thenReturn("2020-05-05");
		StringWriter stringWriter1 = new StringWriter();
		PrintWriter writer1 = new PrintWriter(stringWriter1);
		try {
			when(response.getWriter()).thenReturn(writer1);
			servlet.doPost(request, response);
			writer1.flush();
			assertEquals("", stringWriter1.toString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		// If invalid ticker
		when(session.getAttribute("username")).thenReturn("1234");
		when(request.getParameter("stockTicker")).thenReturn("aavdasdf");
		when(request.getParameter("numOfShares")).thenReturn("3");
		when(request.getParameter("datePurchased")).thenReturn("2020-01-01");
		when(request.getParameter("dateSold")).thenReturn("2020-05-05");
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		try {
			when(response.getWriter()).thenReturn(writer);
			servlet.doPost(request, response);
			writer.flush();
			assertEquals("Not a valid stock ticker\n", stringWriter.toString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		
		// If value of numShares is invalid
		when(session.getAttribute("username")).thenReturn("1234");
		when(request.getParameter("stockTicker")).thenReturn("TSLA");
		when(request.getParameter("numOfShares")).thenReturn("-1");
		when(request.getParameter("datePurchased")).thenReturn("2020-01-01");
		when(request.getParameter("dateSold")).thenReturn("2020-05-05");
		StringWriter stringWriter4 = new StringWriter();
		PrintWriter writer4 = new PrintWriter(stringWriter4);
		try {
			when(response.getWriter()).thenReturn(writer4);
			servlet.doPost(request, response);
			writer4.flush();
			assertEquals("Number of shares has to be a value greater than 0\n", stringWriter4.toString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		// If sold date entered is invalid
		when(session.getAttribute("username")).thenReturn("1234");
		when(request.getParameter("stockTicker")).thenReturn("AAPL");
		when(request.getParameter("numOfShares")).thenReturn("69");
		when(request.getParameter("datePurchased")).thenReturn("2020-10-08");
		when(request.getParameter("dateSold")).thenReturn("2021-10-08");
		StringWriter stringWriter5 = new StringWriter();
		PrintWriter writer5 = new PrintWriter(stringWriter5);
		try {
			when(response.getWriter()).thenReturn(writer5);
			servlet.doPost(request, response);
			writer5.flush();
			assertEquals("Invalid sold date entered: date entered is in the future\n", stringWriter5.toString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		// If purchase date entered is invalid
		when(session.getAttribute("username")).thenReturn("1234");
		when(request.getParameter("stockTicker")).thenReturn("AAPL");
		when(request.getParameter("numOfShares")).thenReturn("69");
		when(request.getParameter("datePurchased")).thenReturn("2021-10-10");
		when(request.getParameter("dateSold")).thenReturn("2020-10-11");
		StringWriter stringWriter6 = new StringWriter();
		PrintWriter writer6 = new PrintWriter(stringWriter6);
		try {
			when(response.getWriter()).thenReturn(writer6);
			servlet.doPost(request, response);
			writer6.flush();
			assertEquals("Invalid purchase date entered: date entered is in the future\n", stringWriter6.toString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		// If purchase date entered is invalid
		when(session.getAttribute("username")).thenReturn("1234");
		when(request.getParameter("stockTicker")).thenReturn("AAPL");
		when(request.getParameter("numOfShares")).thenReturn("69");
		when(request.getParameter("datePurchased")).thenReturn("2020-10-10");
		when(request.getParameter("dateSold")).thenReturn("2020-10-09");
		StringWriter stringWriter7 = new StringWriter();
		PrintWriter writer7 = new PrintWriter(stringWriter7);
		try {
			when(response.getWriter()).thenReturn(writer7);
			servlet.doPost(request, response);
			writer7.flush();
			assertEquals("Invalid purchase date: Purchase date is after sold date\n", stringWriter7.toString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		DatabaseMethods.addStockToDB("AAPL", 69, Date.valueOf("2020-01-01"), Date.valueOf("2020-10-07"), "1234");

		// If stock ticker already in Database
		when(session.getAttribute("username")).thenReturn("1234");
		when(request.getParameter("stockTicker")).thenReturn("AAPL");
		when(request.getParameter("numOfShares")).thenReturn("69");
		when(request.getParameter("datePurchased")).thenReturn("2020-01-01");
		when(request.getParameter("dateSold")).thenReturn("2020-10-07");
		StringWriter stringWriter8 = new StringWriter();
		PrintWriter writer8 = new PrintWriter(stringWriter8);
		try {
			when(response.getWriter()).thenReturn(writer8);
			servlet.doPost(request, response);
			writer8.flush();
			assertEquals("Stock ticker already exists in database\n", stringWriter8.toString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}
	
	@Test
	public void testValidate() {
		//Test for validate function
		int numOfShares = 1;	
		try {
			java.util.Date pDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-10-10");
			java.sql.Date datePurchased = new java.sql.Date(pDate.getTime());
			
			java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2020-10-09");
			java.sql.Date dateSold = new java.sql.Date(date.getTime());
			
			AddStockServlet servlet = new AddStockServlet();
			
			String msg = servlet.validate(numOfShares, datePurchased, dateSold);
			assertEquals("Invalid purchase date: Purchase date is after sold date\n", msg);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

}
