package csci310;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Test;

public class DatabaseMethodsTest {
	
	@Before
	public void setUp() { 
		try {
			DatabaseMethods.deleteDB();
			DatabaseMethods.createDB();
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}

	@Test
	public void testConnectToDB() {
		try { 
			Connection connection = DatabaseMethods.getConnection();
			DatabaseMetaData data = connection.getMetaData();
			
			String driverName = data.getDriverName();
			assertEquals("MySQL Connector/J", driverName);
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void testCreateDB() {
		// setUp() calls createDB() for us, we just need to verify it
		try {
			Connection connection = DatabaseMethods.getConnection();
			
			String tableName = connection.getCatalog();
			assertEquals("stonks", tableName);
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void testDeleteDB() {
		boolean usersTableExists = true;
		try {
			// setUp() calls createDB() for us, so we delete it
			DatabaseMethods.deleteDB();
			
			Connection connection = DatabaseMethods.getConnection();
			
			// This simple select will cause an SLQException if
			// the users table DNE
			String query = "SELECT * FROM users";
			Statement stmt = connection.createStatement();
			stmt.executeQuery(query);
			
		} catch (SQLException e) {
			usersTableExists = false;
		}
		assertEquals(false, usersTableExists);
	}
	
	@Test
	public void testAddUserToDB() {
		try {
			// Before the user was in the DB
			String username = "Tommy Trojan";
			boolean availableResult = DatabaseMethods.verifyUsernameAvailable(username);
			assertEquals(true, availableResult);
			
			// call addUserToDB()
			DatabaseMethods.addUserToDB(username, "Fight On!");
			
			// After the user was added to the DB
			boolean notAvailableResult = DatabaseMethods.verifyUsernameAvailable(username);
			assertEquals(false, notAvailableResult);
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	@Test
	public void testVerifyUsernameAvailable() {
		try {
			// Username is available
			String username = "Tommy Trojan";
			boolean availableResult = DatabaseMethods.verifyUsernameAvailable(username);
			assertEquals(true, availableResult);
			
			// Add the user to the DB
			String password = "12345678";
			DatabaseMethods.addUserToDB(username, password);
			
			// Username is not available
			boolean notAvailableResult = DatabaseMethods.verifyUsernameAvailable(username);
			assertEquals(false, notAvailableResult);
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void testVerifyLogin() {
		try {
			// username/password are wrong
			String username = "Tommy Trojan";
			String password = "Fight On!";
			boolean comboExists = DatabaseMethods.verifyLogin(username, password);
			assertEquals(false, comboExists);
			
			DatabaseMethods.addUserToDB(username, password);
			// username/password are correct
			comboExists = DatabaseMethods.verifyLogin(username, password);
			assertEquals(true, comboExists);
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	/**
	 * Test method for {@link csci310.DatabaseMethods#addStockToDB(java.lang.String, int, java.sql.Date)}.
	 */
	@Test
	public void testAddStockToDB() {
		try {
			DatabaseMethods.addUserToDB("Tommy", "anything");
			// Adds a stock to DB
			String stockTicker = "MSFT";
			int numOfShares = 42;
			Date datePurchased = Date.valueOf("2020-01-01");
			Date dateSold = Date.valueOf("2020-05-05");
			String username = "Tommy";
			DatabaseMethods.addStockToDB(stockTicker, numOfShares, datePurchased, dateSold, username);
			// this checks that add works
			
			// check if the stock is there
			String jsonString = DatabaseMethods.getStocksFromDB(username);
			
			Gson gson = new Gson();
			Type listType = new TypeToken<ArrayList<StockData>>() {}.getType();
			List<StockData> data = gson.fromJson(jsonString, listType);
			
			String ticker = data.get(0).getStockTicker();
			assertEquals(ticker, "MSFT");
			int numShares = data.get(0).getNumOfShares();
			assertEquals(numShares, 42);
			Date date = data.get(0).getDateSold();
			assertEquals(date, Date.valueOf("2020-05-05"));
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Test method for {@link csci310.DatabaseMethods#getStocksFromDB(java.lang.String)}.
	 */
	@Test
	public void testGetStocksFromDB() {
		try {
			DatabaseMethods.addUserToDB("Tommy", "anything");
			// Adds a stock to DB
			String stockTicker = "TSLA";
			int numOfShares = 69;
			Date datePurchased = Date.valueOf("2020-01-01");
			Date dateSold = Date.valueOf("2020-07-07");
			String username = "Tommy";
			DatabaseMethods.addStockToDB(stockTicker, numOfShares, datePurchased, dateSold, username);
			// this checks that add works
			
			// check if the stock is there
			String jsonString = DatabaseMethods.getStocksFromDB(username);
			
			Gson gson = new Gson();
			Type listType = new TypeToken<ArrayList<StockData>>() {}.getType();
			List<StockData> data = gson.fromJson(jsonString, listType);
			
			// for Tesla stocks
			String ticker = data.get(0).getStockTicker();
			assertEquals(ticker, "TSLA");
			int numShares = data.get(0).getNumOfShares();
			assertEquals(numShares, 69);
			Date date = data.get(0).getDateSold();
			assertEquals(date, Date.valueOf("2020-07-07"));
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * Test method for {@link csci310.DatabaseMethods#verifyStockNotInDB(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testVerifyStockNotInDB() {
		try {
			// nothing in DB so nothing should be in db -> returns true
			DatabaseMethods.addUserToDB("Tommy", "anything");
			boolean result = DatabaseMethods.verifyStockNotInDB("Tommy", "MSFT");
			assertEquals(result, true);
			
			// MSFT now in db -> returns false
			String stockTicker = "MSFT";
			int numOfShares = 42;
			Date datePurchased = Date.valueOf("2020-01-01");
			Date dateSold = Date.valueOf("2020-05-05");
			String username = "Tommy";
			DatabaseMethods.addStockToDB(stockTicker, numOfShares, datePurchased, dateSold, username);
			boolean result2 = DatabaseMethods.verifyStockNotInDB("Tommy", "MSFT");
			assertEquals(result2, false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Test method for {@link csci310.DatabaseMethods#deleteStockFromDB(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testDeleteStockFromDB() {
		try {
			// nothing in DB so nothing should be in db -> returns true
			DatabaseMethods.addUserToDB("Tommy", "anything");
			boolean result1 = DatabaseMethods.verifyStockNotInDB("Tommy", "MSFT");
			assertEquals(result1, true);
			
			// added MSFT to DB -> return false
			String stockTicker = "MSFT";
			int numOfShares = 42;
			Date datePurchased = Date.valueOf("2020-01-01");
			Date dateSold = Date.valueOf("2020-05-05");
			String username = "Tommy";
			DatabaseMethods.addStockToDB(stockTicker, numOfShares, datePurchased, dateSold, username);
			
			boolean result2 = DatabaseMethods.verifyStockNotInDB("Tommy", "MSFT");
			assertEquals(result2, false);
			
			// after deletion -> should return true
			DatabaseMethods.deleteStockFromDB("Tommy", "MSFT");
			boolean result3 = DatabaseMethods.verifyStockNotInDB("Tommy", "MSFT");
			assertEquals(result3, true);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
