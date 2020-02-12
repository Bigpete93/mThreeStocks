const state = {
  selection: {
    Company: 'Microsoft',
    Ticker: 'MSFT'
  },
  data: {
    Weekly: [],
    Daily: [],
    Intraday: []
  }
}

const utils = {
  inputValidation: {
    Weekly: d => {
      let dformat = new Date(`${d} GMT-05:00`);
      const day = dformat.getDay();
      if (day === 5 || d === state.data.Weekly[0].date)
        return d;
      dformat.setDate(dformat.getDate() - (day === 6 ? 1 : 2 + day));

      return `${new Date(dformat.getTime() - (dformat.getTimezoneOffset() * 60000 ))
                .toISOString()
                .split("T")[0]}`;
    },
    Daily: d => {
      let dformat = new Date(`${d} GMT-05:00`);
      const day = dformat.getDay();
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
      const day = dformat.getDay();
      const hour = dformat.getHours();
      const minute = dformat.getMinutes();

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
  },
  tableTitle: {
    Weekly: 'Weekly Prices (open, high, low, close) and Volumes',
    Daily: 'Daily Time Series with Splits and Dividend Events',
    Intraday: 'Intraday (5min) open, high, low, close prices and volume'
  },
  loadFromCSV: (data, table) => {
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
        });
  },
  getAllDataCSV: () => {
    fetch("http://localhost:4567/getWeekly")
        .then(response => response.text())
        .then(csv => utils.loadFromCSV(csv, 'Weekly'));
  
    fetch("http://localhost:4567/getDaily")
        .then(response => response.text())
        .then(csv => utils.loadFromCSV(csv, 'Daily'));
  
    fetch("http://localhost:4567/getIntraday")
        .then(response => response.text())
        .then(csv => utils.loadFromCSV(csv, 'Intraday'));
  }
}

// RENDERS

const renderLoading = () => {
  const loadingContainer = d3.select('.opening');
  const line = britecharts.line();
  loadingContainer.html(line.loadingState());
}

const renderHome = () => {
  document.querySelector("#main").innerHTML = `
  <h1 id="parameter" class="text-center">Welcome to the Stock Analysis Application</h1>
  <div class="d-flex justify-content-center">
    <img src="logo.png" alt="logo" height="250" width="250"/>
  </div>`
}

const renderNotFound = () => {
  document.querySelector("#main").innerHTML = `
  <h1 id="parameter" class="text-center">No Data Available</h1>
  <h3 class="text-center">This was probably a holiday. The Stock Market was closed.</h3>
  <div class="d-flex justify-content-center">
    <img src="logo.png" alt="logo" height="250" width="250"/>
  </div>`
}

const renderAllTable = mode => {
  document.querySelector("#main").innerHTML = `
  <h1 id="parameter" class="text-center">${utils.tableTitle[mode]}</h1>
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

const renderInputForm = async mode => {
  const inputType = mode === "Intraday" ? "datetime-local" : "date";
  const data = state.data[mode];
  const latestTime = data[0].date.replace(" ","T");
  const earliestTime = data[data.length - 1].date.replace(" ","T");
  document.querySelector("#main").innerHTML =
  `<h1 id="parameter" class="text-center">${mode} - Please select your parameters:</h1>
  <div class="d-flex flex-column align-items-center">
    <form id="userinput">
      <input  type=${inputType}
              name="param"
              max=${latestTime}
              min=${earliestTime}>
      <input type="submit">
    </form>
  </div>`;
};

const renderSingleInputTable = (mode, data) => {
  if (!data) {
    renderNotFound();
  } else {
    document.querySelector("#main").innerHTML = `
    <h1 id="parameter" class="text-center">${utils.tableTitle[mode]}</h1>
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
}

const renderHistorical = () => {
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

const renderDayTradePlatform = selectedDate => {
  const latestDay = state.data.Intraday[0].date.split(" ")[0];
  const earliestDay = state.data.Intraday[state.data.Intraday.length - 1].date.split(" ")[0];
  document.querySelector("#main").innerHTML = `
  <h1 id="parameter" class="text-center">Day Trade Platform for ${state.selection.Company}</h1>
  <div id='day-trade' style='height: 500px'></div>
  <div class="d-flex flex-column align-items-center">
     <form id="userinput">Selected Date:
       <input  type="date"
               name="param"
               value=${selectedDate ? selectedDate : latestDay}
               max=${latestDay}
               min=${earliestDay}>
       <input  type="submit"
               value="Re-Render"/>
     </form>
   </div>`
  return selectedDate ? selectedDate : latestDay;
}

// HISTORICAL

const armHistorical = lineData => {

  const volumeData = lineData.dataByTopic[0].dates

  const linecontainer = d3.select('.historical-line-container');
  const selectorContainer = d3.select('.historical-volume-container');
  let containerWidth = linecontainer.node().getBoundingClientRect().width;
  const lineHistorical = britecharts.line();
  const brushHistorical = britecharts.brush();

  lineHistorical
    .isAnimated(true)
    .grid('full')
    .width(containerWidth)
    .height(350)
    .xAxisFormat(britecharts.line().axisTimeCombinations.CUSTOM)
    .xAxisCustomFormat("%b %Y")
    
  brushHistorical
    .width(containerWidth)
    .height(100)
    .xAxisFormat(britecharts.brush().axisTimeCombinations.CUSTOM)
    .xAxisCustomFormat("%b %Y")
    .margin({top:0, bottom: 40, left: 50, right: 30})
    .on('customBrushEnd', ([brushStart, brushEnd]) => {
      if (brushStart && brushEnd) {
        let view = JSON.parse(JSON.stringify(lineData));
        view.dataByTopic[0].dates = volumeData.filter(w => (
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

const armDayTradePlatform = day => {
  
  const candlestickSeries = fc.seriesSvgCandlestick()
    .bandwidth(3);
  
  const movingAverageSeries = fc.seriesSvgLine()
    .mainValue(d => d.ma)
    .crossValue(d => d.date);
  
  const mergedSeries = fc.seriesSvgMulti()
    .series([movingAverageSeries, candlestickSeries]);
  
  const xScale = fc.scaleDiscontinuous(d3.scaleTime())
    .discontinuityProvider(fc.discontinuitySkipWeekends());
  
  const chart = fc.chartCartesian(xScale, d3.scaleLinear())
    .yOrient('left')
    .svgPlotArea(mergedSeries);
  
  const xExtent = fc.extentDate()
    .accessors([d => d.date])
  
  const yExtent = fc.extentLinear()
    .accessors([d => d.high, d => d.low])
    .pad([0.1, 0.1]);
  
  const ma = fc.indicatorMovingAverage()
      .value(d => d.open);

  
  d3.csv(`http://localhost:4567/getIntraday/tradealgo/${day}`,
    row => ({
      date: new Date(row.Date),
      open: Number(row.Open),
      close: Number(row.Close),
      high: Number(row.High),
      low: Number(row.Low)
    })).then(data => {
      if(data.length === 0) {
        renderNotFound();
      } else {
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
      }
    });
}

// DYNAMIC METHODS

const armSubmit = mode => {
  const userinput = document.querySelector("#userinput");
  userinput.addEventListener('submit', e => {
    e.preventDefault();
    if(mode === 'daytrade') {
      armDayTradePlatform(
        renderDayTradePlatform(
          utils.inputValidation.Daily((e.target.param.value))));
      armSubmit('daytrade');
    } else {
      let input = mode !== 'Intraday' ? e.target.param.value : `${e.target.param.value.replace("T"," ")}:00`;
      input = utils.inputValidation[mode](input);
      const data = state.data[mode].find(w => w.date === input);
      renderSingleInputTable(mode, data);
    }
  });
};

// ONLOAD

const onLoad = async () => {
  renderLoading();
  await utils.getAllDataCSV();
  setTimeout(() => renderHome(), 1234)
};

onLoad();
