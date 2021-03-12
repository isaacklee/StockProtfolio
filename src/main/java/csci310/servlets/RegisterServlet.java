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
 * Accessible via post requests to /api/register Handles register form inputs
 * Everything is public because of JUnit BS
 * 
 * @author John Meyering
 *
 */
public class RegisterServlet extends HttpServlet {

	/**
	 * Creates a new row in the DB with the user's username and hashed password
	 * 
	 * NOTE: Since the hashing of passwords is a seperate feature, that
	 * functionality will be added at a later date.
	 * 
	 * Sends a string to the caller detailing errors, or saying "Success"
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String confirmpassword = request.getParameter("confirmpassword");

		PrintWriter pw = response.getWriter();

		String msg = validation(username, password, confirmpassword);
		
		if(msg.equals("")) {
			boolean nameIsAvailable = false;
			try {
				nameIsAvailable = DatabaseMethods.verifyUsernameAvailable(username);
			} catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			// If the username is taken
			if(!nameIsAvailable) {
				msg = "The entered username is taken";
			}
			else {
				try {
					DatabaseMethods.addUserToDB(username, password);		
				} catch(SQLException e) {
					System.out.println(e.getMessage());
				}
				msg = "Success";
				HttpSession session = request.getSession();
				session.setAttribute("username", username);
			}
		}
		pw.write(msg);
		pw.flush();
	}

	String validation(String username, String password, String confirmpassword) {
		String msg = "";
		// If the username is too short
		if (username.length() < 4) {
			msg = "Please enter a username with at least 4 characters";
		}
		// If the username is too long
		else if (username.length() > 20) {
			msg = "Please enter a username with 20 or fewer characters";
		}
		// If the password is too short
		else if (password.length() < 8) {
			msg = "Please enter a password with 8 or more characters";
		}
		// If the password is too long
		else if (password.length() > 20) {
			msg = "Please enter a password with 20 or fewer characters";
		}
		// If the password and the confirm don't match
		else if (!password.equals(confirmpassword)) {
			msg = "Entered passwords do not match";
		}
		return msg;
	}
}
