package org.mThree;
import static spark.Spark.*;
import com.google.gson.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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