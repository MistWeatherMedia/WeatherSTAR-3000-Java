package WS3000.weatherstar3000;

import util.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.ArrayList;

import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Tides extends Thread {
	private final OkHttpClient client = new OkHttpClient();
	
	Utilities utl = new Utilities();
	ArrayList<String> locName = new ArrayList<>();
	ArrayList<String> highTide = new ArrayList<>();
	ArrayList<String> lowTide = new ArrayList<>();
	String sunrise;
	String sunset;
	
	DateTimeFormatter tideDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	DateTimeFormatter tideTimeFormatter = DateTimeFormatter.ofPattern("h:mma E", Locale.ENGLISH);
	
	String getLocName(String displayName) {
		return utl.cjust(displayName, 32, " ").toUpperCase().substring(0, 32);
	}
	
	String getTides(String station, String tide) {
	    String url = "https://api.tidesandcurrents.noaa.gov/api/prod/datagetter?begin_date=" + ZonedDateTime.now(Main.timeZone).format(tideDateFormatter) + "&end_date=" + ZonedDateTime.now(Main.timeZone).plusDays(1).format(tideDateFormatter) + "&station=" + station + "&product=predictions&datum=MLLW&time_zone=lst_ldt&interval=hilo&units=english&application=DataAPI_Sample&format=json";
	    
	    Request request = new Request.Builder()
	            .url(url)
	            .get()
	            .header("Accept", "application/json")
	            .build();

	    try (Response response = client.newCall(request).execute()) {
	        if (!response.isSuccessful()) {
	            return "                        ";
	        }

	        String responseBody = response.body().string();
	        JSONArray predictions = new JSONObject(responseBody).getJSONArray("predictions");
	        
	        StringBuilder sb = new StringBuilder();
	        int j = 0;
	        
	        for (int i = 0; i < predictions.length(); i++) {
	            if (predictions.getJSONObject(i).getString("type").equals(tide)) {
	                sb.append(utl.rjust(LocalDateTime.parse(predictions.getJSONObject(i).getString("t"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).format(tideTimeFormatter).toUpperCase(Locale.ENGLISH), 11, " "));
	                if (j < 1) sb.append("   ");
	                j++;
	                if (j == 2) break;
	            }
	        }
	        
	        return sb.toString();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "                        ";
	    }
	}
	
	String getSunData(String icaoCode, String key, String type) {
	    String url = "https://api.weather.com/v3/wx/forecast/daily/7day?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key;
	    
	    Request request = new Request.Builder()
	            .url(url)
	            .get()
	            .header("Accept", "application/json")
	            .build();

	    try (Response response = client.newCall(request).execute()) {
	        if (!response.isSuccessful()) {
	            return "       ";
	        }

	        String responseBody = response.body().string();
	        JSONObject jsonResponse = new JSONObject(responseBody);
	        String time = jsonResponse.getJSONArray(type).getString(0);

	        int hour = Integer.parseInt(time.substring(11, 13));
	        int dispHour = hour > 12 ? hour - 12 : hour;
	        String minutes = time.substring(14, 16);
	        String part = hour < 12 ? "AM" : "PM";

	        return utl.rjust(dispHour + ":" + minutes + part, 7, " ");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "       ";
	    }
	}

	public void run() {
		ArrayList<String> tideIDs = new ArrayList<>();
		tideIDs.addAll(Main.tideIDs);
		ArrayList<String> tideNames = new ArrayList<>();
		tideNames.addAll(Main.tideNames);
		String key = Main.key;
		Main.tides.highTide.clear();
		Main.tides.lowTide.clear();
		for (int i = 0; i < tideIDs.size(); i++) {
			Main.tides.locName.add(getLocName(Main.tideNames.get(i)));
			Main.tides.highTide.add(getTides(Main.tideIDs.get(i), "H"));
			Main.tides.lowTide.add(getTides(Main.tideIDs.get(i), "L"));
		}
		Main.tides.sunrise = getSunData(Main.mainIcao, key, "sunriseTimeLocal");
		Main.tides.sunset = getSunData(Main.mainIcao, key, "sunsetTimeLocal");
	}
}
