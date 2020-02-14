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
import org.mThree.ControllerFiles.Controller;
import org.mThree.FrontEnd.SparkServer;

import spark.Spark;

/**
 * Hello world!
 *
 */
public class Main
{
	public static void main( String[] args ){

		/*******************  THREADS & BACKEND  *****************************/
		// Create a service with 3 threads.
		ScheduledExecutorService execService = Executors.newScheduledThreadPool(3);

		// Runs every five minutes
		execService.scheduleAtFixedRate(new Runnable() {
			public void run() {
				try {
					mainLoop(APIURLBuilder.Length.MIN);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0L, 5L, TimeUnit.MINUTES);

		//Runs every day
		execService.scheduleAtFixedRate(new Runnable() {
			public void run() {
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
		}, 0L, 7L, TimeUnit.DAYS);

		/******************* FRONT END CALL***********************************/

		//TO DO: launch front end
		SparkServer.spark();


	}


	/**********************************
	 * 	List of tasks Main should repeat in various threads,
	 * 	 mostly updating the database
	 **********************************/
    public static void mainLoop(APIURLBuilder.Length h) throws Exception {
		//Pull new data from API
		String urlStr = APIURLBuilder.urlBuild(h, "MSFT");
		Controller controller = new Controller();

		URL alphaVantage5min = new URL(urlStr);
		ArrayList<Record> ToSql = JsonParser.JsonParse(alphaVantage5min, "MSFT", h);
		//TO DO: For Loop ToDataBase

		//Reads each part of the data, checks if its in the database. If return is null,
		// adds the data to the database
		for(Record record: ToSql) {
			switch (h) {
				case WEEK:
					if(controller.getDataByWeek(record.getDate()) == null)
						controller.setDataByWeek(record.getDate(), record.getOpen(),
							record.getHigh(), record.getLow(), record.getClose(), record.getVolume());
					break;
				case DAY:
					if(controller.getDataByDay(record.getDate()) == null)
						controller.setDataByDay(record.getDate(), record.getOpen(),
								record.getHigh(), record.getLow(), record.getClose(), record.getVolume());
					break;
				case MIN:
					if(controller.getDataBy5Min(record.getDate()) == null)
						controller.setDataBy5Min(record.getDate(), record.getOpen(),
								record.getHigh(), record.getLow(), record.getClose(), record.getVolume());
					break;
			}
		}


	}
}
