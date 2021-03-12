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
 * Accessible via post request to /api/login
 * Handles login form inputs
 * @author John Meyering
 * @author Abraham Bazbaz
 */
public class LoginServlet extends HttpServlet {
	
	public int failedLoginCounter = 0;
	
	/* 60 Seconds after the 1st failed login attempt
	*  Resets if there haven't been 3 login attempts in 60 seconds
	*/   
	private long startFailTime = 0L; 
	
	/*
	*   Time at which the user will no longer be locked out
	*/
	private long elapsedTime = 0L;

	
	/**
	 * Do login verification and return a String to the caller
	 * with the result (either an error message, or "Success")
	 * 
	 * NOTE: Since the hashing of passwords is a seperate feature,
	 * that functionality will be added at a later date.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		PrintWriter pw = response.getWriter();
		String msg = loginValidation(username, password);
		
		if(msg.equals(""))
		{
			
			if(elapsedTime >= System.currentTimeMillis())
			{
				msg = "You have been locked out from logging in for " + (elapsedTime-System.currentTimeMillis())/1000 + " seconds.";
			}
			else {
				// If the username/password combo is not valid
				// Either the username DNE, or the password is wrong
				boolean validCombo = false;
				try {
					validCombo = DatabaseMethods.verifyLogin(username, password);
				} catch(SQLException e) {
					System.out.println(e.getMessage());
				}
					
				//If the username/password combination is false
				if(!validCombo) {
					//Set default error message
					msg = "The entered username/password does not exist";
						
					failedLoginCounter++;
										
					if(failedLoginCounter == 1)
					{
						startFailTime = System.currentTimeMillis()+60000;
					}
					
					if(startFailTime <= System.currentTimeMillis())
					{
						failedLoginCounter = 1;
					}
										
					if(failedLoginCounter == 3 && startFailTime >= System.currentTimeMillis())
					{
						elapsedTime = System.currentTimeMillis() + 60000;
						msg = "You have been locked out from logging in for 60 seconds.";
						failedLoginCounter = 0;
					}
				}
				
				// If all is well
				else {
					msg = "Success";
					HttpSession session = request.getSession();
					session.setAttribute("username", username);
				}
			}
		}
		pw.write(msg);
		pw.flush();
	}
	
	String loginValidation(String username, String password)
	{
		String msg = "";
		// If the username is too short
		if(username.length() < 4) {
			msg = "Please enter a username with at least 4 characters";
		}
		// If the username is too long
		else if(username.length() > 20) {
			msg = "Please enter a username with 20 or fewer characters";
		}
		// If the password is too short
		else if(password.length() < 8) {
			msg = "Please enter a password with 8 or more characters";
		}
		// If the password is too long
		else if(password.length() > 20) {
			msg = "Please enter a password with 20 or fewer characters";
		}

		return msg;
	}
	
	void resetFLC()
	{
		failedLoginCounter = 0;
		startFailTime = 0L;
		elapsedTime = 0L;
	}
}