package org.mThree.FrontEnd;

import static spark.Spark.before;
import static spark.Spark.get;
import java.awt.*;
import java.io.File;
import java.util.stream.Stream;

public class SparkServer {
    public static void spark(){

        try {
            File htmlFile = new File("C:\\Users\\roger\\Documents\\Projects\\MThree\\stockanalyzer_vanilla\\frontend\\test.html");
//              API Team, please replace the link once it's deployed on AWS
            Desktop.getDesktop().browse(htmlFile.toURI());
        } catch (Exception ex) {
            System.err.println("wrong path");
        }

        get("/getWeekly", (req, res) -> {
                    String result = String.join("\n", new Seed().WEEKLYSTRINGIFIED);
                    System.out.println(result);
                    return result;
                }
        );

        get("/getDaily", (req, res) -> {
                    String result = String.join("\n", new Seed().DAILYLYSTRINGIFIED);
                    System.out.println(result);
                    return result;
                }
        );

        get("/getIntraday", (req, res) -> {
                    String result = String.join("\n", new Seed().INTRADAYSTRINGIFIED);
                    System.out.println(result);
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

        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Headers", "*");
            res.type("application/json");
        });
    }
}