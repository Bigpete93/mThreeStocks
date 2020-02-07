package org.mThree;

import org.mThree.API.APIURLBuilder;
import org.mThree.API.JsonParser;
import org.mThree.API.Record;

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

    	String urlStr = "";


    	switch(option) {
    	case MINUTES:
			urlStr = APIURLBuilder.urlBuild(APIURLBuilder.Length.MIN, ticker);
			break;
    	case DAILY:
			urlStr = APIURLBuilder.urlBuild(APIURLBuilder.Length.DAY, ticker);
			break;
    	case WEEKLY:
    		urlStr = APIURLBuilder.urlBuild(APIURLBuilder.Length.WEEK, ticker);
			break;
    	}


    	
    	
    	URL alphaVantage5min = new URL(urlStr);
        ArrayList<Record> list = JsonParser.JsonParse(alphaVantage5min, ticker);
        
        for(Record data: list)
        	System.out.println(data);
        
    }
}
