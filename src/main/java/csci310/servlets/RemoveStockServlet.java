/**
 * 
 */
package csci310.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import csci310.DatabaseMethods;

/**
 * @author vonzg Handles removing of stock to database
 */
public class RemoveStockServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		try {
			HttpSession session = request.getSession();
			String username = (String) session.getAttribute("username");
			String stockTicker = request.getParameter("stockTicker");

			PrintWriter pw = response.getWriter();

			// if that stock is currently in DB
			boolean doesNotExist = DatabaseMethods.verifyStockNotInDB(username, stockTicker);
			
			String msg = checkRemove(doesNotExist, username, stockTicker);
			pw.write(msg);
			pw.flush();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	String checkRemove(Boolean bool, String username, String stockTicker) throws SQLException {
		String msg = "";
		if(!bool) {
			DatabaseMethods.deleteStockFromDB(username, stockTicker);
			msg += "Stock ticker successfully removed\n";
		}
		else {
			// unlikely scenario because the UI should only allow
			// to remove existing stock data
			msg += "The stock you are trying to remove does not exist in our database\n";
		}
		
		return msg;
		
	}
}
