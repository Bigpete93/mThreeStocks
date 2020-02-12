let state = {
  selection: {
    Company: 'Microsoft',
    Ticker: 'MSFT'
  },
  data: {
    Weekly: [],
    Daily: [],
    Intraday: []
  },
  tableTitle: {
    Weekly: 'Weekly Prices (open, high, low, close) and Volumes',
    Daily: 'Daily Time Series with Splits and Dividend Events',
    Intraday: 'Intraday (5min) open, high, low, close prices and volume'
  },
  singleInputType: {
    Weekly: 'date',
    Daily: 'date',
    Intraday: 'datetime-local'
  },
  inputValidation: {
    Weekly: d => {
      let dformat = new Date(`${d} GMT-05:00`);
      let day = dformat.getDay();
      if (day === 5 || d === state.data.Weekly[0].date)
        return d;
      dformat.setDate(dformat.getDate() - (day === 6 ? 1 : 2 + day));

      return `${new Date(dformat.getTime() - (dformat.getTimezoneOffset() * 60000 ))
                .toISOString()
                .split("T")[0]}`;
    },
    Daily: d => {
      let dformat = new Date(`${d} GMT-05:00`);
      let day = dformat.getDay();
      if(day === 0)
        dformat.setDate(dformat.getDate() - 2);
      else if (day === 6)
        dformat.setDate(dformat.getDate() - 1);

      return `${new Date(dformat.getTime() - (dformat.getTimezoneOffset() * 60000 ))
        .toISOString()
        .split("T")[0]}`;
    },
    Intraday: dt => {
      let dformat = new Date(`${dt} GMT-05:00`);
      let day = dformat.getDay();
      let hour = dformat.getHours();
      let minute = dformat.getMinutes();

      dformat.setMinutes(minute - (minute % 5))

      if(hour < 9 || hour === 9 && minute < 35) {
        dformat.setHours(16)
        dformat.setMinutes(0);
        dformat.setDate(dformat.getDate() - 1)
      } else if (hour > 16 || hour === 16 && minute > 0) {
        dformat.setHours(16);
        dformat.setMinutes(0);
      }

      if(day === 0)
        dformat.setDate(dformat.getDate() - 2);
      else if (day === 6)
        dformat.setDate(dformat.getDate() - 1);

      return `${new Date(dformat.getTime() - (dformat.getTimezoneOffset() * 60000 ))
        .toISOString()
        .replace("T", " ")
        .substring(0, 19)}`;

    }
  }
}

// Renders

let renderHome = () => {
  let main = document.querySelector("#main");
  main.innerHTML = `<h1 id="parameter" class="text-center">Welcome to the EmThree Stock Analysis Application</h1>`;
}

let renderAllTable = mode => {
  document.querySelector("#main").innerHTML = `
  <h1 id="parameter" class="text-center">${state.tableTitle[mode]}</h1>
  <table class="blueTable">
    <thead>
      <tr>
        <th>Date</th>
        <th>Open</th>
        <th>High</th>
        <th>Low</th>
        <th>Close</th>
        <th>Volume</th>
      </tr>
    </thead>
    <tbody id="results">
    ${state.data[mode].map(w => `
      <tr>
        <td>${w.date}</td>
        <td>${Number(w.open).toFixed(2)}</td>
        <td>${Number(w.high).toFixed(2)}</td>
        <td>${Number(w.low).toFixed(2)}</td>
        <td>${Number(w.close).toFixed(2)}</td>
        <td>${Number(w.volume).toLocaleString()}</td>
      </tr>`).join('')}
    </tbody>
  </table>`;
};

let renderInputForm = async mode => {
  let inputType = state.singleInputType[mode]
  let data = state.data[mode];
  document.querySelector("#main").innerHTML =
  `<h1 id="parameter" class="text-center">${mode} - Please select your parameters:</h1>
  <div class="d-flex flex-column align-items-center">
    <form id="userinput">
      <input  type=${inputType}
              name="param"
              max=${data[0].date.replace(" ","T")}
              min=${data[data.length - 1].date.replace(" ","T")}>
      <input type="submit">
    </form>
  </div>`;
};

let renderSingleInputTable = (mode, data) => {
  document.querySelector("#main").innerHTML = `
  <h1 id="parameter" class="text-center">${state.tableTitle[mode]}</h1>
  <table class="blueTable">
  <thead>
    <tr>
      <th>Description</th>
      <th>Figures</th>
    </tr>
  </thead>
  <tbody id="results">
  ${Object.keys(data).map(d => `
    <tr>
      <td>${d.toUpperCase()}</td>
      ${d !== 'volume' ?
      `<td>${data[d]}</td>` :
      `<td>${Number(data.volume).toLocaleString()}</td>`}
    </tr>`).join('')}
  </tbody>
</table>`;
}

let renderHistorical = () => {
  document.querySelector("#main").innerHTML = `
  <h1 id="parameter" class="text-center">Historical Stock Price for ${state.selection.Company}</h1>
  <div class="historical-line-container border border-primary rounded"></div>
  <div class="historical-volume-container border border-primary rounded"></div>`

  return {
    dataByTopic: [
        {
          topic: `Historical Data of ${state.selection.Company}`,
          topicName: "Stock Price",
          dates: state.data.Weekly.map(w => ({
            value: w.close,
            date: new Date(`${w.date} GMT-05:00`)
        }))
      }
    ]
  };
}

let renderDayTradePlatform = date => {
  const data = state.data.Intraday;
  document.querySelector("#main").innerHTML = `
  <h1 id="parameter" class="text-center">Day Trade Platform for ${state.selection.Company}</h1>
  <div id='day-trade' style='height: 500px'></div>
  <div class="d-flex flex-column align-items-center">
     <form id="userinput">Selected Date:
       <input  type="date"
               name="param"
               value=${data[0].date.split(" ")[0]}
               max=${data[0].date.split(" ")[0]}
               min=${data[data.length - 1].date.split(" ")[0]}>
       <input  type="submit"
               value="Re-Render"/>
     </form>
   </div>`
  return date ? date : state.data.Intraday[0].date.split(" ")[0];
}

// HISTORICAL

const armHistorical = lineData => {

  const volumeData = lineData.dataByTopic[0].dates

  const linecontainer = d3.select('.historical-line-container');
  const selectorContainer = d3.select('.historical-volume-container');
  const containerWidth = linecontainer.node().getBoundingClientRect().width;
  const lineHistorical = britecharts.line();
  const brushHistorical = britecharts.brush();

  lineHistorical
    .isAnimated(true)
    .grid('full')
    .width(containerWidth)
    .height(350)
    .xAxisFormat(britecharts.line().axisTimeCombinations.CUSTOM)
    .xAxisCustomFormat("%m %Y")

  brushHistorical
    .width(containerWidth)
    .height(100)
    .xAxisFormat(britecharts.brush().axisTimeCombinations.CUSTOM)
    .xAxisCustomFormat("%m %Y")
    .margin({top:0, bottom: 40, left: 50, right: 30})
    .on('customBrushEnd', ([brushStart, brushEnd]) => {
      if (brushStart && brushEnd) {
        let view = JSON.parse(JSON.stringify(lineData));
        view.dataByTopic[0].dates = lineData.dataByTopic[0].dates.filter(w => (
          new Date(`${w.date} GMT-05:00`) > new Date(`${brushStart}`) &&
          new Date(`${w.date} GMT-05:00`) < new Date(`${brushEnd}`)
        ))
        linecontainer.datum(view).call(lineHistorical);
      }
  });

  linecontainer.datum(lineData).call(lineHistorical);
  selectorContainer.datum(volumeData).call(brushHistorical);

};

// DAY TRADE

const candlestickSeries = fc.seriesSvgCandlestick()
	.bandwidth(3);

const movingAverageSeries = fc.seriesSvgLine()
  .mainValue(d => d.ma)
  .crossValue(d => d.date);

const mergedSeries = fc.seriesSvgMulti()
	.series([movingAverageSeries, candlestickSeries]);

const xScale = fc.scaleDiscontinuous(d3.scaleTime())
  .discontinuityProvider(fc.discontinuitySkipWeekends());

const chart = fc.chartCartesian(
    xScale,
    d3.scaleLinear()
  )
	.yOrient('left')
	.svgPlotArea(mergedSeries);

const durationDay = 864e5;
const xExtent = fc.extentDate()
	.accessors([d => d.date])

const yExtent = fc.extentLinear()
	.accessors([d => d.high, d => d.low])
	.pad([0.1, 0.1]);

const parseDate = d3.timeParse("%m-%Y");

const ma = fc.indicatorMovingAverage()
    .value(d => d.open);

const armDayTradePlatform = day => {
  d3.csv(`http://localhost:4567/getIntraday/tradealgo/${day}`,
    row => ({
      open: Number(row.Open),
      close: Number(row.Close),
      high: Number(row.High),
      low: Number(row.Low),
      date: new Date(row.Date)
    })).then(data => {
      const maData = ma(data);
      const mergedData = data.map((d, i) =>
        Object.assign({}, d, {
          ma: maData[i]
        })
      );
     
      chart.xDomain(xExtent(mergedData))
        .yDomain(yExtent(mergedData))
  
      d3.select('#day-trade')
        .datum(mergedData)
        .call(chart);
    });
}

// DYNAMIC METHODS

let armSubmit = mode => {
  let userinput = document.querySelector("#userinput");
  userinput.addEventListener('submit', e => {
    e.preventDefault();
    if(mode === 'daytrade') {
      armDayTradePlatform(renderDayTradePlatform(state.inputValidation.Daily((e.target.param.value))));
      armSubmit('daytrade');
    } else {
      let input = mode !== 'Intraday' ? e.target.param.value : `${e.target.param.value.replace("T"," ")}:00`;
      input = state.inputValidation[mode](input);
      let data = state.data[mode].find(w => w.date === input);
      renderSingleInputTable(mode, data);
    }
  });
};

// FETCHES

let getAllDataCSV = () => {
  fetch("http://localhost:4567/getWeekly")
  .then(response => response.text())
  .then(csv => loadFromCSV(csv, 'Weekly'));

  fetch("http://localhost:4567/getDaily")
  .then(response => response.text())
  .then(csv => loadFromCSV(csv, 'Daily'));

  fetch("http://localhost:4567/getIntraday")
  .then(response => response.text())
  .then(csv => loadFromCSV(csv, 'Intraday'));
}

// STATE MODIFICATION

let loadFromCSV = (data, table) => {
  data.split('\n')
  .map(w => {
    let x = w.split(/,\s/);
    state.data[table].push({
      date: x[0],
      open: x[1],
      high: x[2],
      low: x[3],
      close: x[4],
      volume: x[5]
    })
  }
  );
}

// DATETIME TESTS
let dttests = () => {
  console.log(state.inputValidation.Weekly("2020-01-21"));
  console.log(state.inputValidation.Daily("2020-01-21"));
  console.log(" --- ")
  console.log(state.inputValidation.Weekly("2020-01-22"));
  console.log(state.inputValidation.Daily("2020-01-22"));
  console.log(" --- ")
  console.log(state.inputValidation.Weekly("2020-01-23"));
  console.log(state.inputValidation.Daily("2020-01-23"));
  console.log(" --- ")
  console.log(state.inputValidation.Weekly("2020-01-24"));
  console.log(state.inputValidation.Daily("2020-01-24"));
  console.log(" --- ")
  console.log(state.inputValidation.Weekly("2020-01-25"));
  console.log(state.inputValidation.Daily("2020-01-25"));
  console.log(" --- ")
  console.log(state.inputValidation.Weekly("2020-01-26"));
  console.log(state.inputValidation.Daily("2020-01-26"));
  console.log(" --- ")
  console.log(state.inputValidation.Weekly("2020-01-27"));
  console.log(state.inputValidation.Daily("2020-01-27"));
  console.log(" --- ")
  console.log(state.inputValidation.Weekly("2020-01-28"));
  console.log(state.inputValidation.Daily("2020-01-28"));
  console.log(" --- ")
  console.log(state.inputValidation.Weekly("2020-01-29"));
  console.log(state.inputValidation.Daily("2020-01-29"));
  console.log(" --- ")
  console.log(state.inputValidation.Weekly("2020-01-30"));
  console.log(state.inputValidation.Daily("2020-01-30"));
  console.log(" --- ")
  console.log(state.inputValidation.Weekly("2020-01-31"));
  console.log(state.inputValidation.Daily("2020-01-31"));
  console.log(" --- ")
  console.log(state.inputValidation.Weekly("2020-02-01"));
  console.log(state.inputValidation.Daily("2020-02-01"));
  console.log(" --- ")
  console.log(state.inputValidation.Weekly("2020-02-02"));
  console.log(state.inputValidation.Daily("2020-02-02"));
  console.log(" --- ")
  console.log(state.inputValidation.Weekly("2020-02-03"));
  console.log(state.inputValidation.Daily("2020-02-03"));
  console.log(" --- ")
  console.log(state.inputValidation.Weekly("2020-02-04"));
  console.log(state.inputValidation.Daily("2020-02-04"));
  console.log(" --- ")
  console.log(state.inputValidation.Weekly("2020-02-05"));
  console.log(state.inputValidation.Daily("2020-02-05"));
  console.log(" --- ")
  console.log(state.inputValidation.Intraday("2020-02-12 16:00:00"));
  console.log(state.inputValidation.Intraday("2020-02-11 16:03:00"));
  console.log(state.inputValidation.Intraday("2020-02-10 20:45:00"));
  console.log(state.inputValidation.Intraday("2020-02-09 12:00:00"));
  console.log(state.inputValidation.Intraday("2020-02-08 04:00:00"));
  console.log(state.inputValidation.Intraday("2020-02-07 12:00:00"));
  console.log(state.inputValidation.Intraday("2020-02-06 03:00:00"));
};

// ONLOAD

let onLoad = async () => {
  await getAllDataCSV();
  // setTimeout(dttests, 3000);
};

onLoad();
