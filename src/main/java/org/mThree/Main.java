package org.mThree;

import java.awt.Desktop;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.mThree.API.APIURLBuilder;
import org.mThree.API.JsonParser;
import org.mThree.API.Record;

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
    	/*
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
        ArrayList<Record> list = JsonParser.JsonParse(alphaVantage5min, "MSFT");
        
        for(Record data: list)
        	System.out.println(data);
        
        if (Desktop.isDesktopSupported()) {
        	Desktop.getDesktop().browse(new URI("http://localhost"));
        }
		*/
		// Create a service with 3 threads.
		ScheduledExecutorService execService = Executors.newScheduledThreadPool(3);

// Schedule a task to run every 5 seconds with no initial delay.
		execService.scheduleAtFixedRate(new Runnable() {
			public void run() {
				try {
					mainLoop(APIURLBuilder.Length.MIN);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0L, 5L, TimeUnit.MINUTES);

    }


    public static void mainLoop(APIURLBuilder.Length h) throws Exception {
		String urlStr = APIURLBuilder.urlBuild(h, "MSFT");
		URL alphaVantage5min = new URL(urlStr);
		ArrayList<Record> list = JsonParser.JsonParse(alphaVantage5min, "MSFT");
		for(Record data: list)
			System.out.println(data);

	}
}
