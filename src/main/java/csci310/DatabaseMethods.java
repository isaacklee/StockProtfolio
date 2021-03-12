package csci310;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.google.gson.Gson;

/**
 * Contains static methods that communicate with the project Database
 * 
 * @author John Meyering
 *
 */
public class DatabaseMethods {

	private static final Connection connection = connectToDB();

	/**
	 * All of our database interactions use the same connection. Use this method to
	 * access it.
	 * 
	 * @return the static connection object
	 */
	public static Connection getConnection() {
		return connection;
	}

	/**
	 * Connect to stonks db
	 * 
	 * @return an open connection to the DB
	 */
	public static Connection connectToDB() {
		Connection connection = null;
		try {
            // DB params
            String url = "jdbc:mysql://138.197.198.49:6969/stonks";
            String username = "root";
            String password = "cs310stonks";
            
            // create a connection to the database
            connection = DriverManager.getConnection(url, username, password);
            
            System.out.println("Connection to MySQL has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    
		return connection;
	}

	/**
	 * Create the users and stocks table if it doesn't exist
	 */
	public static void createDB() throws SQLException {
		String createUsersTable = "CREATE TABLE IF NOT EXISTS `users` (\r\n" + "  `username` varchar(20) NOT NULL,\r\n"
				+ "  `password` varchar(64) NOT NULL,\r\n" + "  PRIMARY KEY (`username`),\r\n"
				+ "  UNIQUE KEY `username_UNIQUE` (`username`)\r\n" + ")" + "";
		String createStocksTable = "CREATE TABLE IF NOT EXISTS `stocks` (\r\n"
				+ "  `stocksId` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,\r\n"
				+ "  `stockTicker` varchar(10) NOT NULL,\r\n" + "  `numOfShares` int(11) NOT NULL,\r\n"
				+ "  `datePurchased` DATE NOT NULL,\r\n" + "  `dateSold` DATE,\r\n"
				+ "  `username` varchar(20) NOT NULL,\r\n"
				+ "  FOREIGN KEY (`username`) REFERENCES users(`username`)\r\n" +
				/*
				 * "  UNIQUE KEY `stockTicker_UNIQUE` (`stockTicker`)\r\n" + not 100% sure about
				 * this one
				 */
				")" + "";

		Statement createTableStatement = connection.createStatement();
		createTableStatement.executeUpdate(createUsersTable);
		createTableStatement.executeUpdate(createStocksTable);
		
		try {
			addUserToDB("asdf", "asdfasdf");
			
			java.util.Date pDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-11-3");
			java.sql.Date datePurchased = new java.sql.Date(pDate.getTime());
			
			java.util.Date sDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-11-4");
			java.sql.Date dateSold = new java.sql.Date(sDate.getTime());
			addStockToDB("TSLA", 1, datePurchased, dateSold, "asdf");
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

	/**
	 * Delete the users and stocks table if it exists
	 */
	public static void deleteDB() throws SQLException {
		String deleteUsersTable = "DROP TABLE IF EXISTS users";
		String deleteStocksTable = "DROP TABLE IF EXISTS stocks";
		Statement deleteTableStatement = connection.createStatement();

		deleteTableStatement.executeUpdate(deleteStocksTable);
		deleteTableStatement.executeUpdate(deleteUsersTable);
	}

	/**
	 * Tries to add a user the database Does not check if the user exists
	 */
	public static void addUserToDB(String username, String password) throws SQLException {
		String updateString = "INSERT INTO users (username, password)\r\n" + "VALUES (?, ?)";
		PreparedStatement updateStmt = connection.prepareStatement(updateString);
		password = HashPass.makePasswordSecure(password);
		updateStmt.setString(1, username);
		updateStmt.setString(2, password);

		updateStmt.executeUpdate();
	}

	/**
	 * @param input - username to be checked against the DB
	 * @return true if the username is available, else false
	 */
	public static boolean verifyUsernameAvailable(String input) throws SQLException {
		String selectString = "SELECT COUNT(*) from users\r\n" + "WHERE username = ?";

		PreparedStatement selectStmt = connection.prepareStatement(selectString);
		selectStmt.setString(1, input);

		ResultSet rs = selectStmt.executeQuery();

		// If the result of counting up the rows with username
		// is equal to 0, then the username is available
		rs.next();
		boolean usernameAvailable = rs.getString(1).equals("0") ? true : false;

		return usernameAvailable;
	}

	/**
	 * Check if a username/password combo is correct
	 * 
	 * @param username to be checked
	 * @param password to be checked against username
	 * @return true if the combo is valid, else false
	 */
	public static boolean verifyLogin(String username, String password) throws SQLException {
		String selectString = "SELECT COUNT(*) from users\r\n" + "WHERE username = ? AND password = ?";

		PreparedStatement selectStmt = connection.prepareStatement(selectString);
		password = HashPass.makePasswordSecure(password);
		selectStmt.setString(1, username);
		selectStmt.setString(2, password);
		ResultSet rs = selectStmt.executeQuery();

		// If the result of counting up the rows with username/password
		// is equal to 1, then the combo is valid
		rs.next();
		boolean correct = rs.getString(1).equals("1") ? true : false;

		return correct;
	}

	/**
	 * Tries to add a stock the database Does not check if the stock exists
	 */
	public static void addStockToDB(String stockTicker, int numOfShares, Date datePurchased, Date dateSold,
			String username) throws SQLException {
		String updateString = "INSERT INTO stocks (stockTicker, numOfShares, datePurchased, dateSold, username)\r\n"
				+ "VALUES (?, ?, ?, ?, ?)";
		PreparedStatement updateStmt = connection.prepareStatement(updateString);
		updateStmt.setString(1, stockTicker);
		updateStmt.setInt(2, numOfShares);
		updateStmt.setDate(3, datePurchased);
		updateStmt.setDate(4, dateSold);
		updateStmt.setString(5, username);

		updateStmt.executeUpdate();
	}

	/**
	 * @param username only get data that belongs to this user
	 * @return
	 * @throws SQLException
	 */
	public static String getStocksFromDB(String username) throws SQLException {
		Gson gson = new Gson();

		List<StockData> data = new ArrayList<>();

		String searchString = "SELECT * FROM stocks WHERE username = ?";
		PreparedStatement ps = connection.prepareStatement(searchString);
		ps.setString(1, username);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String stockTickerVal = rs.getString("stockTicker");
			int numOfSharesVal = rs.getInt("numOfShares");
			Date datePurchasedVal = rs.getDate("datePurchased");
			Date dateSoldVal = rs.getDate("dateSold");
			StockData stock = new StockData(stockTickerVal, numOfSharesVal, datePurchasedVal, dateSoldVal);
			data.add(stock);
		}
		// after while loop, convert stock data list to json string
		String jsonString = gson.toJson(data);
		return jsonString;
	}

	/**
	 * @param username    - username to be checked against the DB
	 * @param stockTicker - ticker to be checked if it exist in database
	 * @return true if the stock is not in database, else false
	 */
	public static boolean verifyStockNotInDB(String username, String stockTicker) throws SQLException {
		String selectString = "SELECT COUNT(*) from stocks\r\n" + "WHERE username = ? AND stockTicker = ?";

		PreparedStatement selectStmt = connection.prepareStatement(selectString);
		selectStmt.setString(1, username);
		selectStmt.setString(2, stockTicker);

		ResultSet rs = selectStmt.executeQuery();

		// If the result of counting up the rows with username
		// is equal to 0, then the stock does not exist in database
		rs.next();
		boolean stockNotInDb = rs.getString(1).equals("0") ? true : false;
		return stockNotInDb;
	}

	public static void deleteStockFromDB(String username, String stockTicker) throws SQLException {
		String deleteString = "DELETE from stocks\r\n" + "WHERE username = ? AND stockTicker = ?";

		PreparedStatement deleteStmt = connection.prepareStatement(deleteString);
		deleteStmt.setString(1, username);
		deleteStmt.setString(2, stockTicker);

		deleteStmt.executeUpdate();

	}
}
