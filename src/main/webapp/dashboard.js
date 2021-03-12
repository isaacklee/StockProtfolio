var lastActivity = new Date().getTime();

window.onload = function () {
  var timeOut = 1000 * 60 * 2; // 2 minutes
  checkTimeOut = function () {
    if (new Date().getTime() > lastActivity + timeOut) {
      // redirect to timeout page
      window.location.href = "/logout.jsp";
    } else {
      window.setTimeout(checkTimeOut, 1000); // check once per second
    }
  };
  checkTimeOut();

  // populate fields
  populatePortfolioTable()
    .then(() => loadPortfolioHistoryData())
    .then(() => displayTotalValue());
  createChart();
};

// reset lastActivity so user doesn't timeout
window.onclick = function () {
  lastActivity = new Date().getTime();
  console.log(lastActivity);
};

/**
 *   File Structure:
 *   - Globals
 *   - Modals
 *   - Functions for the rows
 *   - Queries
 *   - Form submission functions
 */

// GLOBALS ---------------------------------------------------------------------
var currentModal = "";
var historicalStockData = [];
var portfolioStockData = []; // save data here whether it is charted or not

// MODALS ----------------------------------------------------------------------
/**
 *  Clears errors and inputs
 */
function clearModals() {
  clearErrors();
  clearInputs();
}
function clearErrors() {
  let errorElements = document.querySelectorAll(".error");
  for (let i = 0; i < errorElements.length; ++i) {
    errorElements[i].innerHTML = "";
  }
}
function clearInputs() {
  let inputElements = document.querySelectorAll(".form-input");
  for (let i = 0; i < inputElements.length; ++i) {
    inputElements[i].innerHTML = "";
    inputElements[i].value = "";
  }
}
function closeModal() {
  let modalBackground = document.getElementById("opaque-layer");
  let modalParent = document.getElementById("modal-parent");
  let modal = document.getElementById(currentModal);

  modalBackground.style.display = "none";
  modal.style.display = "none";
  modalParent.style.display = "none";

  clearModals();
}

function openModal(modal_id) {
  let modalBackground = document.getElementById("opaque-layer");
  let modal = document.getElementById(modal_id);
  let modalParent = document.getElementById("modal-parent");

  modalBackground.style.display = "block";
  modal.style.display = "block";
  modalParent.style.display = "flex";

  currentModal = modal_id;
}

function openAddStockModal() {
  openModal("addStockModal");
}

function openSellStockModal(ticker) {
  openModal("sellStockModal");

  let sellStockerTicker = document.getElementById("sellStockTicker");
  sellStockTicker.innerHTML = ticker;
}

function openImportCsvModal() {
  openModal("importCsvModal");
}

function openHistoricalModal() {
  openModal("add-historical-modal");
}

// ROW FUNCTIONS (ADD/REMOVE/TRACK) ---------------------------------------------
function displayPortfolioEntry(ticker) {
  let table = document.querySelector("#portfolio-table");
  table.innerHTML += `
    <div class='portfolio-table-item'>
        <font class="row-item row-item-text">${ticker}</font>
        <div class="row-item">
            <label class="slider-switch">
                <input type="checkbox" class="track-portfolio-input" onclick="toggleTrack('${ticker}', false)">
                <span class="slider round"></span>
            </label>
        </div>
        <div class="row-item">
            <button class="sell-button" onclick="openSellStockModal('${ticker}')"><i class="fas fa-trash"></i></button>
        </div>
    </div>
    `;
}
function displayHistoricalEntry(ticker) {
  let table = document.querySelector("#historical-table");
  table.innerHTML += `
    <div class='portfolio-table-item'>
        <font class="row-item row-item-text">${ticker}</font>
        <div class="row-item">
            <label class="slider-switch">
                <input type="checkbox" class="track-historical-input" onclick="toggleTrack('${ticker}', true)">
                <span class="slider round"></span>
            </label>
        </div>
        <div class="row-item">
            <button class="sell-button" onclick="removeHistoricalStock(this, '${ticker}');"><i class="fas fa-trash"></i></button>
        </div>
    </div>
    `;
}

function removeHistoricalStock(buttonElement, ticker) {
  buttonElement.parentNode.parentNode.remove();
  console.log("Removing Historical Stock: " + ticker);
}

function selectAllHistorical() {
  let historicalSliders = document.querySelectorAll(".track-historical-input");
  for (let i = 0; i < historicalSliders.length; ++i) {
    if (!historicalSliders[i].checked) {
      historicalSliders[i].click();
    }
  }
}
function deselectAllHistorical() {
  let historicalSliders = document.querySelectorAll(".track-historical-input");
  for (let i = 0; i < historicalSliders.length; ++i) {
    if (historicalSliders[i].checked) {
      historicalSliders[i].click();
    }
  }
}

function selectAllPortfolio() {
  let portfolioSliders = document.querySelectorAll(".track-portfolio-input");
  for (let i = 0; i < portfolioSliders.length; ++i) {
    if (!portfolioSliders[i].checked) {
      portfolioSliders[i].click();
    }
  }
}
function deselectAllPortfolio() {
  let portfolioSliders = document.querySelectorAll(".track-portfolio-input");
  for (let i = 0; i < portfolioSliders.length; ++i) {
    if (portfolioSliders[i].checked) {
      portfolioSliders[i].click();
    }
  }
}

/**
 *  Toggle a stock onto/off the chart (redraws the chart)
 */
function toggleTrack(ticker, dashed) {
  console.log("TOGGLING " + ticker);

  // If the ticker is on the chart, we take it off
  let onChart = false;
  for (let i = 0; i < seriesOptions.length; ++i) {
    if (seriesOptions[i].name == ticker) {
      onChart = true;
      break;
    }
  }

  if (onChart) {
    removeChartStock(ticker);
    createChart();
  } else {
    // The stock isn't on the chart, we need to change that
    // If the stock data is in memory, then we don't need to query for it
    let haveData = false;
    if (dashed) {
      // dashed == historical
      haveData = haveHistoricalStockData(ticker);
    } else {
      // not-dashed == portfolio
      haveData = havePortfolioStockData(ticker);
    }

    if (!haveData) {
      queryStockHistory(ticker).done((response) => {
        let data = parseStockData(response);
        addChartStock(ticker, data, dashed);
        createChart(); // createChart() inside of here because of desync
      });
    } else {
      // we have the data, dashed will tell us where to look
      let data = undefined;
      if (dashed) {
        for (let i = 0; i < historicalStockData.length; ++i) {
          if (historicalStockData[i].stockTicker == ticker) {
            data = historicalStockData[i].data;
            break;
          }
        }
      } else {
        for (let i = 0; i < portfolioStockData.length; ++i) {
          if (portfolioStockData[i].stockTicker == ticker) {
            data = portfolioStockData[i].data;
            break;
          }
        }
      }
      addChartStock(ticker, data, dashed);
      createChart();
    }
  }
}

// QUERIES ---------------------------------------------------------------------
/**
 *  Note: the query function return a promise.
 *  To access the response of a query, use `query().done( (response) => { })`
 *                                     or  `query().done( callback(response))`
 */

/**
 *  QUERY IF STOCK EXISTS (probably won't be used)
 *  @param - ticker - stock to be checked against api
 *  @return - true if exists, else false
 */
function queryIfStockExists(ticker) {
  let params = {
    ticker,
  };
  return $.post("api/stockexists", $.param(params), function (response) {
    // stuff decoupled from use of response goes here
  });
}

/**
 *  QUERY TO GET USER'S PORTFOLIO OF STOCKS
 *  Sets the global `portfolio` variable
 */
function queryForPortfolio() {
  let params = {};
  return $.post("api/getstock", $.param(params), function (response) {
    // stuff decoupled from use of response goes here
  });
}

/**
 *  QUERY TO GET THE HISTORY OF A SPECIFIC STOCK
 *  @param - ticker - stock for which we desire the history
 *  @return - JSON string which includes date-value data or an empty string
 */
function queryStockHistory(ticker) {
  let params = {
    ticker,
  };
  return $.post("api/stockhistory", $.param(params), function (response) {
    // stuff decoupled from use of response goes here
    console.log("Query for " + ticker + " stock History Completed");
  });
}

/**
 *  QUERY TO REMOVE A STOCK
 *  @param - ticker - stock to remove
 *  @return - true on success, false on failure
 */
function queryRemoveStock(ticker) {
  let params = {
    stockTicker: ticker,
  };
  return $.post("api/removestock", $.param(params), function (response) {
    // stuff decoupled from use of response goes here
  });
}

/**
 *  QUERY TO ADD A STOCK
 *  @param - ticker - stock to add
 *  @param - numShares - the quantity of the stock
 *  @param - datePurchased - required
 *  @param - dateSold - optional
 *  @return - Stock data on success, else an error message
 */
function queryAddStock(ticker, numShares, datePurchased, dateSold) {
  let params = {
    stockTicker: ticker,
    numOfShares: numShares,
    datePurchased,
    dateSold,
  };
  return $.post("api/addstock", $.param(params), function (response) {
    // stuff decoupled from use of response goes here
  });
}

function populatePortfolioTable() {
  return new Promise((resolve, reject) => {
    // when query returns a promise
    queryForPortfolio().done((response) => {
      // parse the json of the entire portfolio of logged in user
      let userPortfolio = JSON.parse(response);

      // print out every stock the logged in user has
      for (var i = 0; i < userPortfolio.length; i++) {
        // displays all portfolio in the designated format (ticker | track | remove)
        const ticker = userPortfolio[i].stockTicker;
        displayPortfolioEntry(ticker);
        // add a data array to the object
        userPortfolio[i].data = [];
        // add directly to the global portfolio array
        portfolioStockData.push(userPortfolio[i]);
      }
      resolve();
    });
  });
}

// FORM SUBMISSION FUNCTIONS ---------------------------------------------------

// addStockForm
$(document).ready(function () {
  document
    .querySelector("#addStockForm")
    .addEventListener("submit", function (event) {
      event.preventDefault();
      clearErrors();

      let ticker = document.querySelector("#stockTicker").value;
      let numShares = document.querySelector("#numOfShares").value;
      let datePurchased = document.querySelector("#dateBought").value;
      let dateSold = document.querySelector("#dateSold").value;

      let noErrors = false;

      if (ticker.length > 5 || ticker.length < 1) {
        let tickerErrorElement = document.querySelector("#ticker-input-error");
        tickerErrorElement.innerHTML = "Stock ticker must be a valid length";
      } else if (numShares < 1) {
        let numSharesErrorElement = document.querySelector(
          "#numShares-input-error"
        );
        numSharesErrorElement.innerHTML =
          "The number of shares must be a positive integer";
      } else if (numShares.match(/[^\d]/)) {
        // numShares must be an int
        let numSharesErrorElement = document.querySelector(
          "#numShares-input-error"
        );
        numSharesErrorElement.innerHTML =
          "The number of shares must be a positive integer";
      } else if (!datePurchased.match(/[\d]{4}-[\d]{2}-[\d]{2}/)) {
        // the datePurchased should follow yyyy-mm-dd
        let dateBoughtInputErrorElement = document.querySelector(
          "#dateBought-input-error"
        );
        dateBoughtInputErrorElement.innerHTML = "Please use a valid date";
      } else if (dateSold != "") {
        if (!dateSold.match(/[\d]{4}-[\d]{2}-[\d]{2}/)) {
          // the dateSold should follow yyyy-mm-dd
          let dateSoldInputErrorElement = document.querySelector(
            "#dateSold-input-error"
          );
          dateSoldInputErrorElement.innerHTML = "Please use a valid date";
        } else if (
          datePurchased.match(/[\d]{4}/)[0] > dateSold.match(/[\d]{4}/)[0]
        ) {
          // should be purchaseYear <= sellYear
          let dateSoldInputErrorElement = document.querySelector(
            "#dateSold-input-error"
          );
          dateSoldInputErrorElement.innerHTML =
            "Sell date must be after purchase date";
        } else if (
          datePurchased.match(/[\d]{4}/)[0] == dateSold.match(/[\d]{4}/)[0]
        ) {
          // if the year is the same, we need to check months/days
          if (
            datePurchased.match(/-[\d]{2}-/)[0] > dateSold.match(/-[\d]{2}-/)[0]
          ) {
            // purchase month shouldn't be > sold month if same year
            let dateSoldInputErrorElement = document.querySelector(
              "#dateSold-input-error"
            );
            dateSoldInputErrorElement.innerHTML =
              "Sell date must be after purchase date";
          } else if (
            datePurchased.match(/-[\d]{2}-/)[0] ==
            dateSold.match(/-[\d]{2}-/)[0]
          ) {
            // if the month and year are the same
            if (
              datePurchased.match(/[\d]{2}$/) > dateSold.match(/[\d]{2}$/)[0]
            ) {
              // day purchased shouldn't be after day sold
              let dateSoldInputErrorElement = document.querySelector(
                "#dateSold-input-error"
              );
              dateSoldInputErrorElement.innerHTML =
                "Sell date must be after purchase date";
            } else {
              noErrors = true;
            }
          } else {
            noErrors = true;
          }
        } else {
          noErrors = true;
        }
      } else {
        noErrors = true;
      }

      if (noErrors) {
        // No errors! Send the query!
        queryAddStock(ticker, numShares, datePurchased, dateSold).done(
          (response) => {
            // if no response, means good news, we can display to user "Success"
            if (response == "") {
              closeModal();
              swal({
                title: "Success!",
                text: "You have added " + ticker + " stocks to your portfolio!",
                icon: "success",
                buttons: false,
                timer: 2000,
              });
              displayPortfolioEntry(ticker);
            } else {
              // if not, display the error onto ticker-input-error
              let tickerErrorElement = document.querySelector(
                "#ticker-input-error"
              );
              tickerErrorElement.innerHTML = response;
            }
          }
        );
      }
    });
});

// add-historical-form
$(document).ready(function () {
  document
    .querySelector("#add-historical-form")
    .addEventListener("submit", function (event) {
      event.preventDefault();
      clearErrors();

      let ticker = document.querySelector("#historical-stock-ticker").value;
      let errorElement = document.querySelector(
        "#historical-ticker-input-error"
      );

      if (ticker.length < 1 || ticker.length > 5) {
        errorElement.innerHTML = "Stock ticker must be a valid length";
      }
      if (ticker.match(/[^a-zA-Z]/)) {
        errorElement.innerHTML = "Stocker ticker must be a valid format";
      } else {
        queryStockHistory(ticker).done((queryResponse) => {
          if (queryResponse == "Query Failure") {
            errorElement.innerHTML = ticker + " stock data not found";
          } else {
            closeModal();
            // parse data from response
            let data = parseStockData(queryResponse);
            // save it into historicalStockData array
            storeStockData(ticker, data, true);
            // display charts
            createChart();
            // add to historical table
            displayHistoricalEntry(ticker);
            // queryResponse is a JSON string containing the stock's data!
          }
        });
      }
    });
});

//import csv function
function importCsvFile() {
  $("#import-csv").trigger("click");
  $("#import-csv").change(function (doc) {
    let file = doc.target.files[0];
    var reader = new FileReader();
    reader.readAsText(file);
    reader.onload = function (event) {
      // close modal
      closeModal();
      let csvData = event.target.result;
      let parsed = $.csv.toArrays(csvData);
      if (parsed && parsed.length > 0) {
        let valid = true;
        let length = -1;
        parsed.forEach(function (array) {
          if (array.length != 3 && array.length != 4) {
            valid = false;
            length = array.length;
          }
        });
        /**
         * This part will read csv and do the following:
         * 1) Display onto portfolio section
         * 2) Add stocks to backend
         */
        if (valid) {
          let error = "";
          let counter = 0;
          parsed.forEach(function (array) {
            // do error checking before adding stocks
            let ticker = array[0];
            let numShares = array[1];
            let datePurchased = array[2];
            let dateSold = datePurchased;
            if (array.length == 4) {
              dateSold = array[3];
            } else {
              dateSold = "null";
            }
            // TODO: Do a check if stock ticker exists irl once the api works
            // Check if already in portfolio
            // .map creates an array by applying the lambda to the base array
            if (portfolioStockData.map((x) => x.stockTicker).includes(ticker)) {
              counter++;
              // then we don't add to database but we still increase counter
              if (counter == parsed.length) {
                if (error == "") {
                  // if everything goes well, print success message
                  swal({
                    title: "Success!",
                    text: "You have imported " + parsed.length + " stocks!",
                    icon: "success",
                    buttons: false,
                    timer: 2000,
                  });
                } else {
                  swal({
                    title: "Oh no!",
                    text: error,
                    icon: "error",
                    buttons: false,
                    timer: 5000,
                  });
                }
              }
            } else {
              queryAddStock(ticker, numShares, datePurchased, dateSold).done(
                function (response) {
                  counter++;
                  if (response == "") {
                    displayPortfolioEntry(ticker);
                  } else if (
                    response == "Stock ticker already exists in database\n"
                  ) {
                    // we want to ignore this case and not add an error
                    console.log("Stock already exists in database: " + ticker);
                  } else {
                    error += response;
                  }
                  // once all rows have been checked
                  if (counter == parsed.length) {
                    if (error == "") {
                      // if everything goes well, print success message
                      swal({
                        title: "Success!",
                        text: "You have imported " + parsed.length + " stocks!",
                        icon: "success",
                        buttons: false,
                        timer: 2000,
                      });
                    } else {
                      swal({
                        title: "Oh no!",
                        text: error,
                        icon: "error",
                        buttons: false,
                        timer: 5000,
                      });
                    }
                  }
                }
              );
            }
          });
        } else {
          swal({
            title: "Oh no!",
            text:
              "One or more rows in the csv file has " +
              length +
              " parameters (expecting 3 or 4)\n" +
              "Your file has not been imported",
            icon: "error",
            buttons: false,
            timer: 5000,
          });
        }
      } else {
        swal({
          title: "Oh no!",
          text: "You have nothing to import",
          icon: "error",
          buttons: false,
          timer: 2000,
        });
      }
    };
    reader.onerror = function () {
      swal({
        title: "Oh no!",
        text: "Unable to read " + file.name,
        icon: "error",
        buttons: false,
        timer: 2000,
      });
    };
  });
}

// delete stock from database
function deleteStock() {
  let ticker = sellStockTicker.innerHTML;
  queryRemoveStock(ticker).done((response) => {
    console.log(ticker + " " + response);
    closeModal();
    swal({
      title: "Success!",
      text: "You have successfully removed " + ticker + " from your portfolio!",
      icon: "success",
      buttons: false,
      timer: 2000,
    }).then(() => {
      window.location.reload();
    });
  });
}

/**
 *  Sum up the values of the user's stocks
 *  @param - daysAgo - the number of days since today (in the past)
 *  @return - the portfolio's value on a day specified
 */
function getDayValue(daysAgo) {
  let sum = 0;
  for (let i = 0; i < portfolioStockData.length; ++i) {
    // Add the stock's value * numShares
    let current = portfolioStockData[i];
    sum +=
      current.data[current.data.length - 1 - daysAgo][1] * current.numOfShares;
  }
  return sum;
}

// display total value
function displayTotalValue() {
  // Determine yesterday's total value
  let yesterdayValue = getDayValue(1);
  console.log("Yesterday's value " + yesterdayValue);

  // Determine today's total value
  let todayValue = getDayValue(0);
  console.log("Today's value " + todayValue);

  // Calculate the percentage difference
  let percentDiff = todayValue / yesterdayValue - 1;
  percentDiff = Math.round(percentDiff * 10000) / 100;

  // Display today's value and the percentage difference
  let banner = document.querySelector("#total-value-banner");
  let amount = document.querySelector("#total-value-amount");
  let change = document.querySelector("#total-value-change");
  let arrow = document.querySelector("#total-value-arrow");

  // Set the loading text
  amount.innerHTML = "Porfolio Value loading...";

  // Total Value
  if (todayValue != undefined) {
    //round off today's value
    let todayValueString = String(todayValue);
    let index = 0;
    // index points to the decimal point
    while (todayValueString[index] < todayValueString.length) {
      if (todayValueString[index] == ".") {
        break;
      }
      ++index;
    }
    todayValueString = todayValueString.substring(0, index + 2);
    console.log(todayValueString.length + " VS " + index + 2);
    if (todayValueString.length <= index + 2) {
      todayValueString += "0";
    }

    amount.innerHTML = "$" + todayValueString;

    // Percent Change specific stuffs
    if (!isNaN(percentDiff)) {
      console.log(percentDiff);
      change.innerHTML =
        percentDiff +
        "% " +
        "<i id='total-value-arrow' class='fas fa-long-arrow-alt-up'></i>";
      change.style.display = "block";
    } else {
      change.style.display = "none";
      amount.innerHTML = "Add stocks to your portfolio!";
      banner.style.color = "#268946";
      banner.style.backgroundColor = "white";
    }

    // Adjust styling according to gain/loss
    if (percentDiff >= 0) {
      // We aren't in the red
      banner.style.backgroundColor = "green";
      amount.style.color = "white";
      change.style.color = "white";
      setTimeout(() => {
        arrow = document.querySelector("#total-value-arrow");
        arrow.style.transition = "transform 1s";
        arrow.style.transform = "rotate(0deg)";
      }, 100);
    } else {
      // We are in the red
      banner.style.backgroundColor = "red";
      amount.style.color = "white";
      change.style.color = "white";
      setTimeout(() => {
        arrow = document.querySelector("#total-value-arrow");
        arrow.style.transition = "transform 1s";
        arrow.style.transform = "rotate(180deg)";
      }, 100);
    }
  }

  console.log("Value displayed");
}

/**
 *  Queries for the stock data of every portfolio stock
 *  and puts the data in the associated array
 *  For use onload
 *  @return - a promise so you can chain up your next operation
 */
function loadPortfolioHistoryData() {
  return new Promise((resolve, reject) => {
    let promises = [];

    // get the data in parallel
    console.log(
      "loadPortfolioHistoryData: length of array == " +
        portfolioStockData.length
    );
    console.log(portfolioStockData);
    for (let i = 0; i < portfolioStockData.length; ++i) {
      // Start the query for each ticker
      const ticker = portfolioStockData[i].stockTicker;
      console.log("Querying for " + ticker + " Stock History");
      let newPromise = loadStock(ticker);
      promises.push(newPromise);
    }

    // Await all of the queries
    Promise.all(promises).then(() => {
      console.log("Resolving loadPortfolioHistoryData");
      resolve();
    });
  });
}

/**
 *   Load one stock's data in off of a query
 */
function loadStock(ticker) {
  return new Promise((resolve, reject) => {
    queryStockHistory(ticker).then((response) => {
      let data = parseStockData(response);
      storeStockData(ticker, data, false);
      console.log(ticker + " data loaded");
      resolve();
    });
  });
}

function getTodaysDate() {
  let date = new Date();
  return (
    date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()
  );
}
