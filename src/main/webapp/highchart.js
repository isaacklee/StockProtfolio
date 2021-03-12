var seriesOptions = [],
  seriesCounter = 0,
  names = ["MSFT", "AAPL", "GOOG"];
var chart;

var options = {
    rangeSelector: {
            allButtonsEnabled: true,
            buttons: [
              {
                type: 'year',
                count: 1,
                text: 'Day',
                dataGrouping: {
                    forced: true,
                    units: [['day', [1]]]
                },
                events: {
                  click: function() {
                    document.querySelector("#numOfDataPoints").innerHTML = chart.series[0].processedXData.length;
                  }
                }
            }, {
                type: 'year',
                count: 1,
                text: 'Week',
                dataGrouping: {
                    forced: true,
                    units: [['week', [1]]]
                },
                events: {
                  click: function() {
                    document.querySelector("#numOfDataPoints").innerHTML = chart.series[0].processedXData.length;
                  }
                }
            }, {
                type: 'year',
                text: 'Month',
                dataGrouping: {
                    forced: true,
                    units: [['month', [1]]]
                },
                events: {
                  click: function() {
                    document.querySelector("#numOfDataPoints").innerHTML = chart.series[0].processedXData.length;
                  }
                }
            }, {
              text: "+",
              type: "day",
              count: 1
            }, {
              text: "-",
              type: "year"
            }
          ],
            buttonTheme: {
                width: 60
            },
            selected: 0
        },
    yAxis: {
        labels: {
            formatter: function () {
                return (this.value > 0 ? ' + ' : '') + this.value + '%';
            }
        },
        plotLines: [{
            value: 0,
            width: 2,
            color: 'silver'
        }]
    },
    xAxis: {
      range: 12 * 30 * 24 * 3600 * 1000 // 1 year
    },
  plotOptions: {
    series: {
      compare: "percent",
      showInNavigator: true,
    },
  },

  tooltip: {
    pointFormat:
      '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b> ({point.change}%)<br/>',
    valueDecimals: 2,
    split: true,
  },

  series: seriesOptions,
};

/**
 * Create the chart when all data is loaded
 * @returns {undefined}
 */
function createChart() {
  chart = Highcharts.stockChart("chart", options);
}

/**
 *  Add a stock to the model (does not redraw the chart)
 */
function addChartStock(ticker, data, dashed) {
  let dashStyle = dashed ? "ShortDash" : "Solid";
  let newStock = {
    name: ticker,
    data: data,
    dashStyle: dashStyle,
  };
  seriesOptions.push(newStock);

  storeStockData(ticker, data, dashed);
}

/**
 *  Load a stock into memory (doesn't interact with chart)
 *  Helper for addChartStock
 */
function storeStockData(ticker, data, dashed) {
  // store data if we haven't already
  if (dashed) {
    // historical
    if (!haveHistoricalStockData(ticker)) {
      appendData(historicalStockData, data, ticker);
    }
  } else {
    // portfolio
    if (!havePortfolioStockData(ticker)) {
      appendData(portfolioStockData, data, ticker);
    }
  }
}

/**
 *  Append data to the entry in dataArray corresponding to ticker
 */
function appendData(dataArray, data, ticker) {
  // if historical data, then we need to make the data object
  if (dataArray === historicalStockData) {
    let dataObject = {
      data,
      stockTicker: ticker,
    };
    dataArray.push(dataObject);
  } else {
    // if portfolio data
    let index = 0;
    while (index < dataArray.length) {
      if (dataArray[index].stockTicker == ticker) {
        break;
      }
      ++index;
    }
    dataArray[index].data = data;
  }
}

/**
 *  Tells you if stock data has been procured already
 *  @param - ticker - stock you might have data on
 *  @return - boolean, true if we have the data, else false
 */
function havePortfolioStockData(ticker) {
  // console.log("Checking if we have stock data for " + ticker + "...");
  for (let i = 0; i < portfolioStockData.length; ++i) {
    if (portfolioStockData[i].stockTicker == ticker) {
      if (portfolioStockData[i].data.length == 0) {
        // console.log("We have an entry for " + ticker + ", but no data!");
        return false;
      } else {
        // console.log("We have an entry for " + ticker + ", and the data!");
        return true;
      }
    }
  }
  // console.log("We don't have an entry for " + ticker + "!");
  return false;
}

/**
 *  Tells you if stock data has been procured already
 *  @param - ticker - stock you might have data on
 *  @return - boolean, true if we have the data, else false
 */
function haveHistoricalStockData(ticker) {
  for (let i = 0; i < historicalStockData.length; ++i) {
    if (historicalStockData[i].stockTicker == ticker) {
      if (historicalStockData[i].data.length == 0) {
        return false;
      } else {
        return true;
      }
    }
  }
  return false;
}

/**
 *  Remove a stock from the model (does not redraw the chart)
 */
function removeChartStock(ticker) {
  let index = 0;
  for (let i = 0; i < seriesOptions[i]; ++i) {
    // find the index of belonging to the stock
    if (seriesOptions[i].name == ticker) {
      index = i;
      break;
    }
  }
  if (index > -1) {
    // if the stock exists, remove it
    seriesOptions.splice(index, 1);
  }
}

/**
 *  Transform api data into chart data
 *  @param - apiData - js object of api data
 *  @return - a 2d array usuable by HighCharts
 */
function parseStockData(apiData) {
  apiData = JSON.parse(apiData);
  console.log(apiData);
  let result = [];
  for (let i = 0; i < apiData.c.length; ++i) {
    result.push([1000 * apiData.t[i], apiData.c[i]]);
  }
  return result;
}

function success(data) {
  var name = this.url.match(/(msft|aapl|goog)/)[0].toUpperCase();
  var i = names.indexOf(name);
  let dashStyle = i == 2 ? "ShortDash" : "Solid";
  seriesOptions[i] = {
    name: name,
    data: data,
    dashStyle: dashStyle,
  };

  // As we're loading the data asynchronously, we don't know what order it
  // will arrive. So we keep a counter and create the chart when all the data is loaded.
  seriesCounter += 1;

  if (seriesCounter === names.length) {
    createChart();
  }
}

// Highcharts.getJSON(
//     'https://cdn.jsdelivr.net/gh/highcharts/highcharts@v7.0.0/samples/data/msft-c.json',
//     success
// );
// Highcharts.getJSON(
//     'https://cdn.jsdelivr.net/gh/highcharts/highcharts@v7.0.0/samples/data/aapl-c.json',
//     success
// );
// Highcharts.getJSON(
//     'https://cdn.jsdelivr.net/gh/highcharts/highcharts@v7.0.0/samples/data/goog-c.json',
//     success
// );
