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
	
	private static final int LOW = 0;
	private static final int VOLUME = 1;
	private static final int OPEN = 2;
	private static final int HIGH = 3;
	private static final int CLOSE = 4;

	public static ArrayList<Record> JsonParse(URL url, String symbol) throws Exception {
		
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

		JSONObject responseJson = new JSONObject(response);
		JSONObject data = responseJson.getJSONObject("Time Series (5min)");

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
				case CLOSE: // last field
					point.setClose(entry_data_d);
					point.setSymbol(symbol);
					dataList.add(point);
					break;
				}
			}
			
		}
		
		dataList.sort((o1,o2) -> o1.getDate().compareTo(o2.getDate()));
		return dataList;
	}
}
