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

		// Create a service with 3 threads.
		ScheduledExecutorService execService = Executors.newScheduledThreadPool(3);

		// Runs every five minutes
		execService.scheduleAtFixedRate(new Runnable() {
			public void run() {
				System.out.println(APIURLBuilder.urlBuild(APIURLBuilder.Length.MIN, "MSFT"));

				try {
					mainLoop(APIURLBuilder.Length.MIN);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0L, 5L, TimeUnit.MINUTES);

		/******* TO DO: FIX API NOT WORKING FOR DAY/WEEK************/
/*		//Runs every day
		execService.scheduleAtFixedRate(new Runnable() {
			public void run() {
				System.out.println(APIURLBuilder.urlBuild(APIURLBuilder.Length.DAY, "MSFT"));

				try {
					mainLoop(APIURLBuilder.Length.DAY);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0L, 1L, TimeUnit.DAYS);

		//Runs every week
		execService.scheduleAtFixedRate(new Runnable() {
			public void run() {
				try {
					mainLoop(APIURLBuilder.Length.WEEK);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0L, 7L, TimeUnit.DAYS);*/
		//launches a browser if the user has one set as default
		if (Desktop.isDesktopSupported()) {
			Desktop.getDesktop().browse(new URI("http://localhost"));}
    }


    // List of tasks Main should repeat.
    public static void mainLoop(APIURLBuilder.Length h) throws Exception {
		String urlStr = APIURLBuilder.urlBuild(h, "MSFT");
		URL alphaVantage5min = new URL(urlStr);
		ArrayList<Record> list = JsonParser.JsonParse(alphaVantage5min, "MSFT");

//		Test commit, argh!!!!!
	}
}
