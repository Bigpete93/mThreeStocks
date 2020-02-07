package org.mThree;

import static spark.Spark.before;
import static spark.Spark.get;

public class SparkServer {
    public static void spark(){
        get("/getDaily", (req, res) -> {
                    TestGetStock gs = new TestGetStock();
                    //return new Gson().toJson(gs.sendGet());
                    String result = gs.getDaily();
                    System.out.println(result);
                    return result;
                }
        );

        get("/getWeekly", (req, res) -> {
                    TestGetStock gs = new TestGetStock();
                    //return new Gson().toJson(gs.sendGet());
                    String result = gs.getWeekly();
                    System.out.println(result);
                    return result;
                }
        );

        get("/getIntraday", (req, res) -> {
                    TestGetStock gs = new TestGetStock();
                    //return new Gson().toJson(gs.sendGet());
                    String result = gs.getIntraday();
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