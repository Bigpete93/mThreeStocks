package org.mThree;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONObject;

public class JsonParser {

	
    public static ArrayList<Record> JsonParse(URL url) throws Exception {
        ///URL url = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=MSFT&interval=5min&outputsize=full&apikey=demo");
        
    	//Parse URL into HttpURLConnection in order to open the connection in order to get the JSON data
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //Set the request to GET or POST as per the requirements
        conn.setRequestMethod("GET");
        //Use the connect method to create the connection bridge
        conn.connect();
        //Get the response status of the Rest API
        int responsecode = conn.getResponseCode();

        if(responsecode == 200) {
            System.out.println("Connected ...\n");
            InputStream stream = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = "";
            String line = "";
            while((line = br.readLine() )!= null)
                response += line;

            //System.out.println(response.substring(0,2000));

            JSONObject responseJson = new JSONObject(response);
            JSONObject data = responseJson.getJSONObject("Time Series (5min)");

            String[] dates = JSONObject.getNames(data);

            ArrayList<Record> dataList = new ArrayList<Record>();

            for(String date : dates) { //traverse all dates, get each entry
                Record point = new Record();
                JSONObject entry = data.getJSONObject(date);
                String entry_data = "";

                point.setDate(date);

                Iterator<String> iterator = entry.keys();

                for(int i = 0;  iterator.hasNext();) { //traverse all fields
                    entry_data = (String) entry.get(iterator.next());

                    Double entry_data_d = Double.parseDouble(entry_data);

                    switch( i ) {
                        case 0:
                            //low
                            point.setLow(entry_data_d);
                            break;
                        case 1:
                            //vol
                            Integer entry_data_i = Integer.parseInt(entry_data);
                            point.setVolume(entry_data_i);
                            break;
                        case 2:
                            //open
                            point.setOpen(entry_data_d);
                            break;
                        case 3:
                            //high
                            point.setHigh(entry_data_d);
                            break;
                        case 4:
                            //close
                            point.setClose(entry_data_d);
                            break;
                    }



                    if(i == 4) {
                        dataList.add(point);
                        //System.out.println(point);
                    }
                    i = (i + 1) % 5;

                }

            }
            return dataList;
        }
        else {
            System.out.println("Bad response");
        }
        return null;

    }

}
