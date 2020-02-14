package org.mThree.API;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import org.json.JSONObject;

public class JsonParser {

	private static final int LOW = 0;
	private static final int VOLUME = 1;
	private static final int OPEN = 2;
	private static final int HIGH = 3;
	private static final int CLOSE = 4;

	public static ArrayList<Record> JsonParse(URL url, String symbol, APIURLBuilder.Length durationType) throws Exception {
		String duration = "";
		//JSON object changes.
		switch(durationType) {
		case MIN: duration = "Time Series (5min)"; break;
		case DAY: duration = "Time Series (Daily)"; break;
		case WEEK:duration = "Weekly Time Series"; break;
		}

		//Parse URL into HttpURLConnection in order to open the connection in order to get the JSON data
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		//Set the request to GET or POST as per the requirements
		conn.setRequestMethod("GET");
		//Use the connect method to create the connection bridge
		conn.connect();
		//Get the response status of the Rest API
		int responsecode = conn.getResponseCode();
		if(responsecode != 200)
			System.err.println("Bad response:\t" + responsecode);
		System.out.println("Connected ...\n");
		InputStream stream = conn.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		String line, response = "";
		while((line = br.readLine() )!= null)
			response += line;

		//This helps split the object as we don't care about meta object.
		JSONObject responseJson = new JSONObject(response);
		JSONObject data = responseJson.getJSONObject(duration);

		String[] dates = JSONObject.getNames(data);
		ArrayList<Record> dataList = new ArrayList<Record>();

		for(String date : dates) { //traverse all dates, get each entry
			Record point = new Record();
			JSONObject entry = data.getJSONObject(date);

			point.setDate(date);

			Iterator<String> iterator = entry.keys();

			for(int i = 0;  iterator.hasNext(); i = (i + 1) % 5) { //traverse all fields
				String entry_data = (String) entry.get(iterator.next());

				Double entry_data_d = Double.parseDouble(entry_data);

				switch( i ) {
				case LOW:
					point.setLow(entry_data_d);
					break;
				case VOLUME:
					Integer entry_data_i = Integer.parseInt(entry_data);
					point.setVolume(entry_data_i);
					break;
				case OPEN:
					point.setOpen(entry_data_d);
					break;
				case HIGH:
					point.setHigh(entry_data_d);
					break;
				case CLOSE:
					point.setClose(entry_data_d);
					point.setSymbol(symbol);
					dataList.add(point);
					break;
				}
			}

		}

		//Make sure its in date order
		dataList.sort(Comparator.comparing(Record::getDate));
		return dataList;
	}
}
