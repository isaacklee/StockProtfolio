/**
 * 
 */
package csci310.servlets;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;
import java.sql.SQLException;

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
public class RemoveStockServletTest extends Mockito {

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
	 * {@link csci310.servlets.RemoveStockServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testDoPost() throws SQLException {
		DatabaseMethods.addUserToDB("1234", "12345678");

		RemoveStockServlet servlet = new RemoveStockServlet();
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		
		when(request.getSession()).thenReturn(session);
		// Case 1: trying to delete stock that doesn't exist
		// unlikely scenario because the UI should only allow
		// to remove existing stock data
		when(session.getAttribute("username")).thenReturn("1234");
		when(request.getParameter("stockTicker")).thenReturn("AAPL");
		StringWriter stringWriter1 = new StringWriter();
		PrintWriter writer1 = new PrintWriter(stringWriter1);
		try {
			when(response.getWriter()).thenReturn(writer1);
			servlet.doPost(request, response);
			writer1.flush();
			assertEquals("The stock you are trying to remove does not exist in our database\n",
					stringWriter1.toString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		// Case 2: delete sth that exists in database -> successful
		DatabaseMethods.addStockToDB("AAPL", 69, Date.valueOf("2020-01-01"), Date.valueOf("2020-10-07"), "1234");
		when(session.getAttribute("username")).thenReturn("1234");
		when(request.getParameter("stockTicker")).thenReturn("AAPL");
		StringWriter stringWriter2 = new StringWriter();
		PrintWriter writer2 = new PrintWriter(stringWriter2);
		try {
			when(response.getWriter()).thenReturn(writer2);
			servlet.doPost(request, response);
			writer2.flush();
			assertEquals("Stock ticker successfully removed\n", stringWriter2.toString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}
	
	@Test
	public void testCheckRemove() throws SQLException {
		DatabaseMethods.addUserToDB("1234", "12345678");
		
		RemoveStockServlet servlet = new RemoveStockServlet();
		String msg = servlet.checkRemove(true, "1234", "GOOG");
		assertEquals("The stock you are trying to remove does not exist in our database\n", msg);
		
		DatabaseMethods.addStockToDB("AAPL", 69, Date.valueOf("2020-01-01"), Date.valueOf("2020-10-07"), "1234");
		msg = servlet.checkRemove(false, "1234", "AAPL");
		assertEquals("Stock ticker successfully removed\n", msg);
	}


}
