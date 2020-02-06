package org.mThree;


import java.util.Scanner;

public class APIURLBuilder {
    public enum Length {
        MIN, DAY, WEEK
    }
    public static String urlBuild(Length l, String ticker){


        String urlStr = "https://www.alphavantage.co/query?function=";


        switch (l) {
            case MIN:
                urlStr = "TIME_SERIES_INTRADAY&symbol=" + ticker + " interval=5min&outputsize=full&apikey=";
                break;
            case DAY:
                urlStr = "TIME_SERIES_DAILY&symbol=" + ticker + "&outputsize=full&apikey=";
                break;
            case WEEK:
                urlStr = "TIME_SERIES_WEEKLY&symbol=" + ticker + "&apikey=";
                break;
            default:

        }

        /*IMPORTANT - REMOVE TO SEPARATE TEXT FILE!*/
        String apiKey = "VHWKYL5TZYEGPEMF";
        urlStr += apiKey;



        return urlStr;
    }
}
