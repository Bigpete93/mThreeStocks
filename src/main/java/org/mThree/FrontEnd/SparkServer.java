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
			String filepath = System.getProperty("user.dir") +"/src/main/java/org/mThree/Client/test.html";
			File htmlFile = new File(filepath);
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
			String result = controller.getDataByWeek(week);
			System.out.println("Weekly query from db on Date " + week + "\n" + result);
			return result;
		}
				);

		get("/getDaily/:day", (req, res) -> {
			String day = req.params(":day");
			System.out.println("Week Selected: " + day);
			String result = controller.getDataByDay(day);
			System.out.println("Daily query from db on Date " + day + "\n" + result);
			return result;
		}
				);

		get("/getIntraday/:minute", (req, res) -> {
			String minute = req.params(":minute");
			System.out.println("Minute Selected: " + minute);
			String result = controller.getDataBy5Min(minute);
			System.out.println("5Min query from db on DateTime " + minute + "\n" + result);
			return result;
		}
				);


	}
}