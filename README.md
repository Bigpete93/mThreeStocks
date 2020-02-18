# mThreeStocks
A full-stack project that uses a **Model View Controller** architecture which acts as a proof of concept to illustrate what has been learned during Mthree's Alumni Training Program.

## Technologies and Frameworks Used

### Back-end

* **JDBC**: Utilize SQL queries to get/set financial data from/to MariaDb.
* **Spark**: Allows our back-end to communicate with the front-end by exposing an API

### Front-end

* **Raw HTML/CSS/JavaScript**: To increase efficiency, the website is created without using a front-end framework.
* **Bootstrap**: The nav bar and positioning is abstracted with bootstrap.
* **D3js**: A data visualization library that is often used in the New York Times. Data determines how elements are displayed.
  *  ***D3fc***: Used in the candlestick graph, this simplifies functionalities necessary to display financial graphs, such as discontinuity ranges. After all, we do not want to factor in weekends and holidays in moving averages.
  * ***BriteCharts***: Used in the historical graph, most of the complexities of D3js are abstracted so you can focus on data designing. The most vital use of this is by establishing a relationship between the main historical line graph and the brush chart. Data used by the former is determined by the latter.

## Alumni Collaborators

 - Abby Dudra
 - Alex Giles
 - Asad Nawaz
 - Darren Gomez
 - Drake Sands
 - Harrison Ngo
 - Maodo Sow
 - Kurtis Bassman
 - Jared Hilliard
 - Jean-Pierre Salazar Lubo
 - Jesse Cohen
 - John Caley
 - Joseph 
 - Joshua Araujo
 - Kenny Yang
 - Pooya Motee
 - Roger Lester Palabasan
 - Siddarth Thakkar
 - Tyler Peterson
 - Zach Hammons
