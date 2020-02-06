package org.mThree;

import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class Main
{
	private final static int MINUTES = 0;
	private final static int DAILY = 1;
	private final static int WEEKLY = 2;
	
    public static void main( String[] args ) throws Exception {
    	Scanner console = new Scanner(System.in);
    	System.out.println("Enter ticker symbol");
    	String ticker = console.next();
    	System.out.println(ticker);
    	
    	System.out.println("Enter 0 for 5min, 1 for daily, 2 for weekly");
    	int option = console.nextInt();
    	
    	String urlStr = "https://www.alphavantage.co/query?function=";
    	
    	/*IMPORTANT*/
    	String apiKey = "VHWKYL5TZYEGPEMF";
    	
    	switch(option) {
    	case MINUTES:
    		urlStr = "TIME_SERIES_INTRADAY&symbol=" + ticker + " interval=5min&outputsize=full&apikey=";
    		break;
    	case DAILY:
    		urlStr = "TIME_SERIES_DAILY&symbol=" + ticker + "&outputsize=full&apikey=";
    		break;
    	case WEEKLY:
    		urlStr = "TIME_SERIES_WEEKLY&symbol=" + ticker + "&apikey=";
    		break;
    	}
    	
    	urlStr += apiKey;
    	
    	//example queries
    	//https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=MSFT&outputsize=full&apikey=demo
    	//https://www.alphavantage.co/query?function=TIME_SERIES_WEEKLY&symbol=MSFT&apikey=demo
    	//https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY&symbol=MSFT&apikey=demo
    	
    	
    	
    	URL alphaVantage5min = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=MSFT&interval=5min&outputsize=full&apikey=demo");
        ArrayList<Record> list = JsonParser.JsonParse(alphaVantage5min);
        
        for(Record data: list)
        	System.out.println(data);
        
    }
}
