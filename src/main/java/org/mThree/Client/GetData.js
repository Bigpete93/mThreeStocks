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
      else if (day === 6) 
        dformat.setDate(dformat.getDate() - 1);
      else 
        dformat.setDate(dformat.getDate() - (2 + day));
      
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
      
      if(hour < 9 || (hour === 9 && minute < 35)) {
        dformat.setHours(16)
        dformat.setMinutes(0);
        dformat.setDate(dformat.getDate() - 1)
      } else if (hour > 16 || (hour === 16 && minute > 0)) {
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
  main.innerHTML = `<h1 id="parameter" class="text-center">Welcome to the MThree Stock Analysis Application</h1>`;
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

let renderSingleInputForm = async mode => {
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
}

// Dynamic Methods

let armSubmit = mode => {
  let userinput = document.querySelector("#userinput");
  userinput.addEventListener('submit', e => {
    e.preventDefault();
    let input = mode !== 'Intraday' ? e.target.param.value : `${e.target.param.value.replace("T"," ")}:00`;
    input = state.inputValidation[mode](input);
    let data = state.data[mode].find(w => w.date === input);
    renderSingleInputTable(mode, data);
  });
};

let armHistorical = () => {
  const lineData = {
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

  const volumeData = state.data.Weekly.map(w => ({
    value: w.close,
    date: new Date(`${w.date} GMT-05:00`)
  }))

  const linecontainer = d3.select('.historical-line-container');
  const volumecontainer = d3.select('.historical-volume-container');
  const containerWidth = linecontainer.node().getBoundingClientRect().width;
  const lineHistorical = britecharts.line();
  const brushHistorical = britecharts.brush();
  const chartTooltip = britecharts.tooltip();

  lineHistorical
    .isAnimated(true)
    .grid('full')
    .width(containerWidth)
    .height(350)
    .xAxisLabel("Date")
    .xAxisFormat(britecharts.line().axisTimeCombinations.CUSTOM)
    .xAxisCustomFormat("%Y");
    
  brushHistorical
    .width(containerWidth)
    .height(100)
    .xAxisFormat(britecharts.brush().axisTimeCombinations.CUSTOM)
    .xAxisCustomFormat("%Y")
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
  volumecontainer.datum(volumeData).call(brushHistorical);

  const tooltipContainer = d3.select('.historical-line-container .metadata-group ');
  tooltipContainer.call(chartTooltip);

  const redrawHistorical = () => {
    const newContainerWidth = linecontainer.node() ? linecontainer.node().getBoundingClientRect().width : false;
    lineHistorical.width(newContainerWidth);
    linecontainer.call(lineHistorical);
  };
  const throttledRedraw = _.throttle(redrawHistorical, 200);

  window.addEventListener("resize", throttledRedraw);

};

// Fetches

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

// let getSingleWeekly = week => {
//   fetch(`http://localhost:4567/getWeekly/${week}`)
//   .then(response => response.text())
//   .then(csv => displayTable(csv));
// };

// let getSingleDaily = day => {
//   fetch(`http://localhost:4567/getDaily/${day}`)
//   .then(response => response.text())
//   .then(csv => displayTable(csv));
// };

// let getSingleIntraday = minute => {
//   fetch(`http://localhost:4567/getIntraday/${minute}`)
//     .then(response => response.text())
//     .then(csv => displayTable(csv));
// };

// State Modification

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

// OnLoad

let onLoad = async () => {
  await getAllDataCSV();
  // setTimeout(dttests, 3000);
};

onLoad();
