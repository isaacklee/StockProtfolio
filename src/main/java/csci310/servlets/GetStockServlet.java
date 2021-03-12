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
 * @author vonzg Handles getting all stocks from database
 */

public class GetStockServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// Might need to change
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		
		PrintWriter pw = response.getWriter();
		
		try {
			// i'm assuming i can just throw entire json to front end right now
			String jsonString = DatabaseMethods.getStocksFromDB(username);
			pw.write(jsonString);
			pw.flush();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
