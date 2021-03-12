package csci310.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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

/**
 * 
 * @author John Meyering
 */
public class StockHistoryServletTest extends Mockito {
	
	@Before
	public void setUp() throws Exception {
		DatabaseMethods.deleteDB();
		DatabaseMethods.createDB();
	}

	@Test
	public void testDoPost() throws SQLException {
		StockHistoryServlet servlet = new StockHistoryServlet();
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		
		when(request.getSession()).thenReturn(session);
		
		// When the caller is not logged in
		StringWriter stringWriter1 = new StringWriter();
		PrintWriter writer1 = new PrintWriter(stringWriter1);
		try {
			when(response.getWriter()).thenReturn(writer1);
			servlet.doPost(request, response);
			writer1.flush();
			assertEquals("", stringWriter1.toString());
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		// When the ticker is not real
		when(session.getAttribute("username")).thenReturn("asdf");
		when(request.getParameter("ticker")).thenReturn("YEET");
		StringWriter stringWriter2 = new StringWriter();
		PrintWriter writer2 = new PrintWriter(stringWriter2);
		try {
			when(response.getWriter()).thenReturn(writer2);
			servlet.doPost(request, response);
			writer2.flush();
			assertEquals("Query Failure", stringWriter2.toString());
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		// When all goes well
		when(session.getAttribute("username")).thenReturn("asdf");
		when(request.getParameter("ticker")).thenReturn("TSLA");
		StringWriter stringWriter3 = new StringWriter();
		PrintWriter writer3 = new PrintWriter(stringWriter3);
		try {
			when(response.getWriter()).thenReturn(writer3);
			servlet.doPost(request, response);
			writer3.flush();
			assertNotEquals("", stringWriter3.toString());
			assertNotEquals("Query Failure", stringWriter3.toString());
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
