package org.mThree.FrontEnd;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TestGetStock {
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public String getDaily() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=MSFT&apikey=6PD2VKYE4A1XZ62U"))
                //.header("content-type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        //print status code
        //System.out.println(response.statusCode());

        //print response body
        //System.out.println(response.body());

        //Gson gson = new Gson();
        //String json = gson.toJson(response.body());
        return response.body();
    }
    public String getWeekly() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://www.alphavantage.co/query?function=TIME_SERIES_WEEKLY&symbol=MSFT&apikey=demo"))
                //.header("content-type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        //print status code
        //System.out.println(response.statusCode());

        //print response body
        //System.out.println(response.body());

        //Gson gson = new Gson();
        //String json = gson.toJson(response.body());
        return response.body();
    }

    public String getIntraday() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=MSFT&interval=5min&outputsize=full&apikey=demo"))
                //.header("content-type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        //print status code
        //System.out.println(response.statusCode());

        //print response body
        //System.out.println(response.body());

        //Gson gson = new Gson();
        //String json = gson.toJson(response.body());
        return response.body();
    }
}