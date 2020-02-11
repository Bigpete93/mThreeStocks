package org.mThree.API;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class APIURLBuilder {
    //Can only accept one of three modes, as per API query requirement
    public enum Length {
        MIN, DAY, WEEK
    }
    public static String urlBuild(Length l, String ticker){


        String urlStr = "https://www.alphavantage.co/query?function=";
        switch (l) {
            case MIN:
                urlStr += "TIME_SERIES_INTRADAY&symbol=" + ticker + "&interval=5min&outputsize=full&apikey=";
                break;
            case DAY:
                urlStr += "TIME_SERIES_DAILY&symbol=" + ticker + "&outputsize=full&apikey=";
                break;
            case WEEK:
                urlStr += "TIME_SERIES_WEEKLY&symbol=" + ticker + "&apikey=";
                break;
            default:

        }
        BufferedReader br = null;
        // For security reasons, don't want the key public accessible, so read it privately.
        try {
            br = new BufferedReader(new FileReader("APIKey.txt"));
            String apiKey;
            while ((apiKey = br.readLine()) != null)
                urlStr += apiKey;


        } catch (IOException e) {
            e.printStackTrace();
            //if no API key exists, we'll throw the free demo;
            urlStr= "https://www.alphavantage.co/query?function=TIME" +
                    "_SERIES_INTRADAY&symbol=MSFT&interval=5min&outputsize=full&apikey=demo";
        }
        return urlStr;
    }
}
