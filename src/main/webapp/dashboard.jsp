<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
   String username = (String) session.getAttribute("username");
   if(username == null) {
	   response.sendRedirect("https://localhost:8443/login.jsp");
   }
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Stonks Dashboard</title>

	<link href="dashboard.css" rel="stylesheet" type="text/css" >
	<link href='https://fonts.googleapis.com/css?family=Montserrat' rel='stylesheet'>
	<!-- alerts -->
	<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
    <!-- jQuery Garbo -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-csv/1.0.11/jquery.csv.min.js"></script>

    <!-- Font Awesome -->
    <script src="https://kit.fontawesome.com/e587026860.js" crossorigin="anonymous"></script>

	<!-- HIGHCHART STUFFS -->
	<script src="https://code.highcharts.com/stock/highstock.js"></script>
	<script src="https://code.highcharts.com/stock/modules/data.js"></script>
	<script src="https://code.highcharts.com/stock/modules/exporting.js"></script>
	<script src="https://code.highcharts.com/stock/modules/export-data.js"></script>
	<script src="highchart.js"></script>
	<!-- END OF HIGHCHART STUFFS -->

    <!-- Our Scripts -->
    <script type="text/javascript" src="dashboard.js"></script>
</head>
<body>
	<div id="modal-parent">
        <div id="opaque-layer" onclick="closeModal()"></div>
		<!-- Add Stock Modal -->
		<div id="addStockModal" class="modal">
	  		<!-- Modal content -->
	  		<div id="add-modal-content">
				<form id="addStockForm">
					<span class="close" onclick="closeModal()">X</span>

                    <div class="error-container">
                        <font id="ticker-input-error" class="error"></font>
                    </div>
					<input class="form-input" type="text" id="stockTicker" placeholder="Stock Ticker">

                    <div class="error-container">
                        <font id="numShares-input-error" class="error"></font>
                    </div>

					<input class="form-input" type="text" id="numOfShares" placeholder="# of shares">

                    <div class="error-container">
                        <font id="dateBought-input-error" class="error"></font>
                    </div>
					<input class="form-input datepicker" type="text" id="dateBought" placeholder="Date Bought" onfocus="(this.type = 'date')" onblur="(this.type = 'text')">

                    <div class="error-container">
                        <font id="dateSold-input-error" class="error"></font>
                    </div>
					<input class="form-input datepicker" type="text" id="dateSold" placeholder="Date Sold (Optional)" onfocus="(this.type = 'date')" onblur="(this.type = 'text')">

                    <div class="button-row">
                        <button class="button" type="submit">Add Stock</button>
                        <button class="button" onclick="closeModal(); return false;">Cancel</button>
                    </div>

   				</form>
	  		</div>
		</div>
        <!-- START OF HISTORICAL MODAL -->
        <div id="add-historical-modal" class="modal">
	  		<!-- Modal content -->
	  		<div id="add-historical-modal-content">
				<form id="add-historical-form">
					<span class="close" onclick="closeModal()">X</span>

                    <div class="error-container">
                        <font id="historical-ticker-input-error" class="error"></font>
                    </div>
					<input class="form-input" type="text" id="historical-stock-ticker" placeholder="Stock Ticker">

                    <div class="button-row">
                        <button class="button" id="view-stock-button">View Stock</button>
                        <button class="button" onclick="closeModal(); return false;">Cancel</button>
                    </div>
   				</form>
	  		</div>
		</div>
        <!-- END OF HISTORICAL MODAL -->
		<!-- Sell Stock Modal -->
		<div id="sellStockModal" class="modal">
 			<!-- Modal content -->
 			<div id="sell-modal-content">
   				<span class="close" onclick="closeModal()">X</span>
                    <div id="sellP">
                        Are you sure you want to remove <font id="sellStockTicker"></font> from your portfolio?
                    </div>
  					<div class="button-row">
  						<input class="button" type="submit" name="confirmSellStock" value="Delete Stock" onclick="deleteStock()">
  						<button class="button" onclick="closeModal()">Cancel</button>
  					</div>
 			</div>
		</div>
		<!-- Import CSV Modal -->
		<div id="importCsvModal" class="modal">
			<!-- Modal content -->
			<div id="import-modal-content">
				<span class="close" onclick="closeModal()">X</span>
				<div id="import-modal-text">Please enter a CSV file you would like to import</div>
				<input id="import-csv" type="file" name="import-csv" accept=".csv" />
				<div class="button-row">
					<button class="button" onClick="importCsvFile()">Upload File</button>
					<button class="button" onclick="closeModal()">Cancel</button>
				</div>

			</div>
		</div>
	</div>
    <!-- END OF MODALS -->

	<div id="page">
		<div id="header">
            <div id="headercontainer">
            	<h3>USC CS 310 Stock Portfolio Management</h3>
                <a href="logout.jsp" id="logout">
                    <h4>Logout</h4>
                </a>
            </div>
		</div>
		<div id="body">
			<!-- START OF TOTAL STOCK VALUE -->
			<div id="total-value-container">
				<div id="total-value-banner">
					<font id="total-value-amount">Porfolio Value loading...</font>
					<div id="total-value-change">
						5% <i id="total-value-arrow" class="fas fa-long-arrow-alt-up"></i>
					</div>
				</div>
			</div>
            <!-- END OF TOTAL STOCK VALUE -->
			<!-- CHART -->
			<div id="chart-container">
				<div id="chart"></div>
			</div>
			<!-- END OF CHART -->
			<div id="table-container">
                <!-- START OF BUTTON PANEL -->
                <div id="button-panel">
                    <button class="button-panel-button" onclick="openHistoricalModal()" id="add-historical-button">Add Historical Stock</button>
                    <button class="button-panel-button" onclick="openAddStockModal()" id="addStockButton">Add Porfolio Stock</button>
                    <button class="button-panel-button" onclick="openImportCsvModal()" id="import-csv-button">Import</button>
                </div>
                <!-- END OF BUTTON PANEL -->
                <!-- START OF HISTORICAL TABLE -->
                <div id="historical-table">
                    <div>
                        <h3 id="historical-table-title">HISTORICAL STOCKS</h3>
                    </div>
                    <div id="sp-container">
                    	<font>S&P 500</font>
                    	<label class="slider-switch" onclick="toggleTrack('SPX', false)">
                    		<input type="checkbox" class="track-historical-input">
				            <span class="slider round"></span>
				        </label>
                    </div>
                    <div class="table-select-container">
                        <button class="select-button-historical" onclick="selectAllHistorical();">Select All</button>
                        <button class="select-button-historical" onclick="deselectAllHistorical();">Deselect All</button>
                    </div>
                    <div id="historical-table-headers">
                        <font>Stock</font>
                        <font>Track</font>
                        <font>Remove</font>
                    </div>
                    <!-- HISTORICAL ENTRIES GO HERE -->
                </div>
                <!-- END OF HISTORICAL TABLE -->
                <!-- START OF PORTFOLIO TABLE -->
				 <div id="portfolio-table">
                    <div>
                        <h3 id="portfolio-table-title">YOUR PORTFOLIO</h3>
                    </div>
                    <div class="table-select-container">
                        <button class="select-button-portfolio" onclick="selectAllPortfolio();">Select All</button>
                        <button class="select-button-portfolio" onclick="deselectAllPortfolio();">Deselect All</button>
                    </div>
                    <div id="portfolio-table-headers">
                        <font>Stock</font>
                        <font>Track</font>
                        <font>Remove</font>
                    </div>
                    <!-- PORFOLIO ENTRIES GO HERE -->
                </div>
                <!-- END OF PORTFOLIO TABLE -->
			</div>
		</div>
	</div>
	<div style="display:none;">
  <font id="numOfDataPoints"></font>
	</div>
</body>

</html>
