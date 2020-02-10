let displayTable = data => {
    console.log(data);
    let mainDataKey = Object.keys(data)[1];
    dateToTable(data[mainDataKey]);
    changeTitle(data["Meta Data"]["1. Information"]);
};

let changeTitle = title => {
document.getElementById("parameter").innerText = title;
};

let dateToTable = data => {
    let resultTable = document.getElementById("results");
    resultTable.innerHTML = Object.entries(data).map(
        w =>
            `<tr key=${w[0]}>
                <td>${w[0]}</td>
                <td>${Number(w[1]["1. open"]).toFixed(2)}</td>
                <td>${Number(w[1]["2. high"]).toFixed(2)}</td>
                <td>${Number(w[1]["3. low"]).toFixed(2)}</td>
                <td>${Number(w[1]["4. close"]).toFixed(2)}</td>
                <td>${Number(w[1]["6. volume"]).toLocaleString()}</td>
            </tr>`
        )
        .join("");
  };

  let sendCall = () => {
    fetch("http://localhost:4567/getStock")
      .then(response => {
        return response.json();
      })
      .then(myJson => {
        //console.log(myJson);
        return displayTable(myJson);
      });
  };
  
    
let changeTitle = (title) => {
    document.getElementById("parameter").innerText = title;
};
