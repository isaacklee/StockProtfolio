/**
 * 
 */
package csci310.servlets;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import csci310.DatabaseMethods;
import csci310.StockData;

/**
 * @author vonzg
 *
 */
public class GetStockServletTest extends Mockito {

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
	 * {@link csci310.servlets.GetStockServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testDoPost() throws SQLException {
		// ensure at least has 1 user in database
		DatabaseMethods.addUserToDB("1234", "12345678");

		GetStockServlet servlet = new GetStockServlet();
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		
		when(request.getSession()).thenReturn(session);
		
		DatabaseMethods.addStockToDB("AAPL", 69, Date.valueOf("2020-01-01"), Date.valueOf("2020-10-07"), "1234");
		DatabaseMethods.addStockToDB("MSFT", 42, Date.valueOf("2020-02-02"), Date.valueOf("2020-05-05"), "1234");

		// validates whatever returned in jsonString is correct
		when(session.getAttribute("username")).thenReturn("1234");
		StringWriter sw1 = new StringWriter();
		PrintWriter pw1 = new PrintWriter(sw1);

		try {
			when(response.getWriter()).thenReturn(pw1);
			servlet.doPost(request, response);
			pw1.flush();
			String jsonString = sw1.toString();
			
			// parse the json
			Gson gson = new Gson();
			Type listType = new TypeToken<ArrayList<StockData>>() {}.getType();
			List<StockData> data = gson.fromJson(jsonString, listType);
			
			// check for AAPL
			String ticker = data.get(0).getStockTicker();
			assertEquals(ticker, "AAPL");
			int numShares = data.get(0).getNumOfShares();
			assertEquals(numShares, 69);
			Date pDate = data.get(0).getDatePurchased();
			assertEquals(pDate, Date.valueOf("2020-01-01"));
			Date date = data.get(0).getDateSold();
			assertEquals(date, Date.valueOf("2020-10-07"));
			
			// check for MSFT
			ticker = data.get(1).getStockTicker();
			assertEquals(ticker, "MSFT");
			numShares = data.get(1).getNumOfShares();
			assertEquals(numShares, 42);
			pDate = data.get(1).getDatePurchased();
			assertEquals(pDate, Date.valueOf("2020-02-02"));
			date = data.get(1).getDateSold();
			assertEquals(date, Date.valueOf("2020-05-05"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
