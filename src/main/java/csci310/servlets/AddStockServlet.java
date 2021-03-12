/**
 * 
 */

package csci310.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import csci310.DatabaseMethods;
import csci310.StockMethods;

/**
 * @author vonzg Handles adding of stock to database
 */
public class AddStockServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		try {
			//Set up session get parameters
			HttpSession session = request.getSession();
			String username = (String) session.getAttribute("username");
			String stockTicker = request.getParameter("stockTicker");
			int numOfShares = Integer.parseInt(request.getParameter("numOfShares"));
			java.util.Date pDate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("datePurchased"));
			java.sql.Date datePurchased = new java.sql.Date(pDate.getTime());
			String dateSoldString = request.getParameter("dateSold");
			java.sql.Date dateSold = null;
			PrintWriter pw = response.getWriter();
			String msg = "";
			
			//First check if the ticker is valid
			Boolean exists= StockMethods.doesTickerExist(stockTicker);
			if(exists) {
				if(dateSoldString.equals("") || dateSoldString.equals("null")) {
					dateSold = null;
				}
				else {
					java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("dateSold"));
					dateSold = new java.sql.Date(date.getTime());
				}
				//check if date sold is empty
				if(dateSold == null) {
					msg = validate(numOfShares, datePurchased, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
				}
				else {
					msg = validate(numOfShares, datePurchased, dateSold);
				}
	
				// checks if stock ticker already exists in database
				boolean doesNotExist = DatabaseMethods.verifyStockNotInDB(username, stockTicker);
				if(!doesNotExist) {
					msg += "Stock ticker already exists in database\n";
				}
				// if no errors
				if (msg.equals("") && doesNotExist) {
					DatabaseMethods.addStockToDB(stockTicker, numOfShares, datePurchased, dateSold, username);
				}
				pw.write(msg);
				pw.flush();
      }
			else {
				msg = "Not a valid stock ticker\n";
				pw.write(msg);
				pw.flush();
			}
		} catch (ParseException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
    }
	}
	//Validation function if the ticker is valid
	public String validate(int numOfShares, java.sql.Date datePurchased, java.sql.Date dateSold) {
		String msg = "";
		
		// user entry validation
		if (numOfShares <= 0) {
			msg += "Number of shares has to be a value greater than 0\n";
		}
		java.sql.Date dateToday = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		//error when purchased date is after the current day
		if(datePurchased.after(dateToday)) {
			msg += "Invalid purchase date entered: date entered is in the future\n";
		}
		//error when sold date is after purchased date
		else if(datePurchased.after(dateSold)) {
			msg += "Invalid purchase date: Purchase date is after sold date\n";
		}
		//error when sold date is after the current day
		else if (dateSold.after(dateToday)) {
			msg += "Invalid sold date entered: date entered is in the future\n";
		}
		
		return msg;
	}
}


