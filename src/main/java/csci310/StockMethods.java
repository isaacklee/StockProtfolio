package csci310;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


public class StockMethods {
	//API query with given params
    public static String query(String params) {
        URL url1;
        String apiKey = "&token=bu7dlav48v6uhfp5quig";
        String url = "https://finnhub.io/api/v1/";
        //combined url with given params
        url = url+params+apiKey; 
        StringBuffer content = new StringBuffer();
        try {
            url1 = new URL(url);
            HttpURLConnection con = (HttpURLConnection) url1.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer "+apiKey);
            con.setDoOutput(true);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

	
	public static double calculateTotalPortfolioValue(String username) throws SQLException
	{
		//Get user's stocks in portfolio
		String portfolioStocks = DatabaseMethods.getStocksFromDB(username);
		
		//Put them in an arraylist of StockData objects
		Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<StockData>>() {}.getType();
        List<StockData> data = gson.fromJson(portfolioStocks, listType);
		
		//Variables needed
		String ticker;
		String queryResult;
		double portfolioValue = 0;
		String price = "";
		int charIndex = 5;
		double currentPrice = 0;
		int shareNum = 0;
			
		//For each stock the user has in their portfolio, calculate the change between future and current per day
		for(int i=0; i<data.size();i++)
		{
			//Get the ticker and call the API to get the current price
			ticker = data.get(i).getStockTicker();
			queryResult = query("quote?symbol="+ticker);
			price = "";
			
			//Reset index for current price
			charIndex = 5;
	
			//Response will always have current price starting at 5th character until a comma
			while(queryResult.charAt(charIndex) != ',')
			{
				price += queryResult.charAt(charIndex);
				charIndex++;
			}
			
			//Parse the result and store
			currentPrice = Double.parseDouble(price);
			
			//Get the number of shares in the portfolio
			shareNum = data.get(i).getNumOfShares();
			
			portfolioValue += currentPrice*shareNum;
		}
		
		return portfolioValue;
	}
	
	
	//Check if the ticker exists
	public static boolean doesTickerExist(String ticker) {
		boolean exists = false;
		String url3 = "stock/profile2?symbol="+ticker;
		String profile = query(url3);
		//Returned string contains no content if ticker doesn't exist
		if (profile.toString().equals("{}")){
			//Ticker doesn't exist
			System.out.println("Ticker doesn't exist");
			return exists;
		};
		return !exists;
	}
	public static String stockHistory(String ticker) {
		//Get current date
		long ut2 = System.currentTimeMillis() / 1000L;
		
		//Starting date 1 year in the past 
		long ut1 = ut2 - 31536000L;
		
		//api query 
		String url = "stock/candle?symbol="+ticker+"&resolution=D&from="+ut1+"&to="+ut2;	
		String data = query(url);
		String price = "-1";
		
		JsonElement jsonElement = new JsonParser().parse(data);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        
        //Check if valid
        String valid = jsonObject.get("s").toString();
        valid = valid.replace("\"", "");
       
        //if there is data in the response get price history
        //if not valid returns -1
        if(valid.equals("ok")) {
        	price = jsonObject.get("c").toString();
        }
       
		return jsonElement.toString();
		
	}

}