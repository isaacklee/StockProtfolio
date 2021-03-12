package csci310.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import csci310.StockMethods;

/**
 * Accessible via post request to /api/stockhistory
 * Servlet that queries API for history of a stock and returns it as a JSON string
 * @author John Meyering
 */
public class StockHistoryServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		
		PrintWriter pw = response.getWriter();
		String msg = "";
		
		if(username.equals("") || username.equals(null)) {
			// Somebody tried to access our data without being logged in
			msg = "";
		}
		else {
			String ticker = request.getParameter("ticker");
			// The user is logged in, let's check the ticker
			boolean tickerGood = StockMethods.doesTickerExist(ticker);
			if(!tickerGood) {
				msg = "Query Failure";
			}
			else {
				// The ticker is good, let's get the data
				msg = StockMethods.stockHistory(ticker);
			}
		}
		
		// Output the message, be it error or JSON
		try {
			pw.write(msg);
			pw.flush();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}