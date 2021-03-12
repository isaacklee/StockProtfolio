package csci310;

import java.sql.Date;

public class StockData {
	private String stockTicker;
	private int numOfShares;
	private Date datePurchased;
	private Date dateSold;
	
	public StockData(String stockTicker, int numOfShares, Date datePurchased, Date dateSold) {
		this.setStockTicker(stockTicker);
		this.setNumOfShares(numOfShares);
		this.setDatePurchased(datePurchased);
		this.setDateSold(dateSold);
	}

	public String getStockTicker() {
		return stockTicker;
	}

	public void setStockTicker(String stockTicker) {
		this.stockTicker = stockTicker;
	}

	public int getNumOfShares() {
		return numOfShares;
	}

	public void setNumOfShares(int numOfShares) {
		this.numOfShares = numOfShares;
	}
	
	public Date getDatePurchased() {
		return datePurchased;
	}

	public void setDatePurchased(Date datePurchased) {
		this.datePurchased = datePurchased;
	}

	public Date getDateSold() {
		return dateSold;
	}

	public void setDateSold(Date dateSold) {
		this.dateSold = dateSold;
	}
}
