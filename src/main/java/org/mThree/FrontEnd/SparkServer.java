package org.mThree.FrontEnd;

import static spark.Spark.before;
import static spark.Spark.get;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

import org.mThree.ControllerFiles.Controller;

public class SparkServer {
	private static Controller controller;

	public static void spark(){

		try {
			File htmlFile = new File("/home/kurtis/Desktop/mthree_workspace/finalProject/mThreeStocks/src/main/java/org/mThree/Client/test.html");
			//              API Team, please replace the link once it's deployed on AWS
			controller = new Controller();
			Desktop.getDesktop().browse(htmlFile.toURI());
			System.out.println("Launched Desktop");
		} catch (Exception ex) {
			System.err.println("wrong path");
		}

		before((req, res) -> {
			res.header("Access-Control-Allow-Origin", "*");
			res.header("Access-Control-Allow-Headers", "*");
			res.type("application/json");
		});

		/************************************************************************************************************/

		get("/getWeekly", (req, res) -> {
			ArrayList<String> resultArr = controller.getAllWeek();
			Collections.reverse(resultArr);
			String result = String.join("\n", resultArr);
			System.out.println("The result of SparkServer calling Controller to get Weekly:\n"
					+ result.substring(0,1000) + "...continued...");
			return result;
		}
				);

		get("/getDaily", (req, res) -> {
			ArrayList<String> resultArr = controller.getAllDay();
			Collections.reverse(resultArr);
			String result = String.join("\n", resultArr);
			System.out.println("The result of SparkServer calling Controller to get Daily:\n"
					+ result.substring(0,1000) + "...continued...");
			return result;
		}
				);

		get("/getIntraday", (req, res) -> {
			ArrayList<String> resultArr = controller.getAll5Min();
			Collections.reverse(resultArr);
			String result = String.join("\n", resultArr);
			System.out.println("The result of SparkServer calling Controller to get Intraday:\n"
					+ result.substring(0,1000) + "...continued...");
			return result;
		}
				);


		get("/getWeekly/:week", (req, res) -> {
			String week = req.params(":week");
			System.out.println("Week Selected: " + week);
			String[] stringifiedArray = new Seed().WEEKLYSTRINGIFIED;
			String result = Stream.of(stringifiedArray).filter(w -> w.contains(week)).findFirst().get();
			System.out.println(result);
			return result;
		}
				);

		get("/getDaily/:day", (req, res) -> {
			String day = req.params(":day");
			System.out.println("Week Selected: " + day);
			String[] stringifiedArray = new Seed().DAILYLYSTRINGIFIED;
			String result = Stream.of(stringifiedArray).filter(w -> w.contains(day)).findFirst().get();
			System.out.println(result);
			return result;
		}
				);

		get("/getIntraday/:minute", (req, res) -> {
			String minute = req.params(":minute");
			System.out.println("Minute Selected: " + minute);
			String[] stringifiedArray = new Seed().INTRADAYSTRINGIFIED;
			String result = Stream.of(stringifiedArray).filter(m -> m.contains(minute)).findFirst().get();
			System.out.println(result);
			return result;
		}
				);


	}
}