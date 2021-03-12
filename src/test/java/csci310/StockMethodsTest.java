package csci310;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class StockMethodsTest {
	
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
	public void testQuery() {
		StringBuffer content = null;
		URL url = null;
		String url1="https://finnhub.io/api/v1/stock/profile2?symbol=asdfasdf&token=bu7dlav48v6uhfp5quig";
		HttpURLConnection con = null;
		try {
			url = new URL(url1);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Authorization", "Bearer bu7dlav48v6uhfp5quig");
			con.setDoOutput(true);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertEquals("{}", content.toString());	
	}
	
	
	@Test
	public void testCalculateTotalPortfolioValue() 
	{
		try {
			//Create a test user for the DB
			DatabaseMethods.addUserToDB("Tommy", "anything");
			
			
			//Test with empty portfolio
			double testTotal = 0;
			testTotal = StockMethods.calculateTotalPortfolioValue("Tommy");
			assert(testTotal == 0);
			
			//Add a few stocks to DB to test
			String stockTicker = "MSFT";
			int numOfShares = 1;
			Date dateSold = Date.valueOf("2020-05-05");
			String username = "Tommy";
			DatabaseMethods.addStockToDB(stockTicker, numOfShares, dateSold, dateSold, username);

			stockTicker = "AAPL";
			numOfShares = 1;
			dateSold = Date.valueOf("2020-05-05");
			DatabaseMethods.addStockToDB(stockTicker, numOfShares, dateSold, dateSold, username);

			stockTicker = "BA";
			numOfShares = 1;
			dateSold = Date.valueOf("2020-05-05");
			DatabaseMethods.addStockToDB(stockTicker, numOfShares, dateSold, dateSold, username);
			
			//Test to see if the value was calculated correctly
			testTotal = StockMethods.calculateTotalPortfolioValue("Tommy");
			assert(testTotal != 0);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	@Test
	public void testDoesTickerExist() {
		StockMethods d = new StockMethods();
		assertEquals(false,d.doesTickerExist("aaasdfasdf") );
		assertEquals(true,d.doesTickerExist("aapl") );
	}
	@Test
	public void testStockHistory() {
		//test if invalid
		StockMethods d = new StockMethods();
		assertEquals("{\"s\":\"no_data\"}",d.stockHistory("aaasdfasf"));
		
		//test if valid
		long ut2 = System.currentTimeMillis() / 1000L;
		System.out.println(ut2);
		//Starting date 01/01/2015  
		long ut1 = 1420070400;
		
		//api query 
		String url = "stock/candle?symbol=asan&resolution=D&from="+ut1+"&to="+ut2;	
		String data = d.query(url);
		//parse price
		JsonElement jsonElement = new JsonParser().parse(data);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        data = jsonObject.get("c").toString();
        data = data.replace("[","");
        data = data.replace("]","");
        assertNotEquals("{\"c\":\"no_data\"}",d.stockHistory("asan"));
		
	}

}
