package WS3000.weatherstar3000;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONObject;

import util.Utilities;

public class TravelForecast extends Thread {
	private final OkHttpClient client = new OkHttpClient();
	
	ArrayList<String> locName = new ArrayList<>();
	ArrayList<String> condition = new ArrayList<>();
	ArrayList<String> low = new ArrayList<>();
	ArrayList<String> high = new ArrayList<>();
	Utilities utl = new Utilities();
	String first;
	
	ArrayList<String> tForNames = new ArrayList<>(Arrays.asList("ATLANTA", "BISMARCK", "BOISE", "BOSTON", "BUFFALO", "CHICAGO", "CINCINNATI", "DALLAS", "DENVER", "DETROIT", "KANSAS CITY", "LOS ANGELES", "MINNEAPOLIS", "NASHVILLE", "NEW ORLEANS", "NEW YORK CITY", "PHOENIX", "PORTLAND", "RALEIGH", "RAPID CITY", "SACRAMENTO", "SALT LAKE CITY", "TAMPA", "WASHINGTON, DC"));
	ArrayList<String> tForIcaos = new ArrayList<>(Arrays.asList("KATL", "KBIS", "KBOI", "KBOS", "KBUF", "KORD", "KCVG", "KDAL", "KDEN", "KDTW", "KMCI", "KLAX", "KMSP", "KDNA", "KMSY", "KLGA", "KPHX", "KPDX", "KRDU", "KRAP", "KSMF", "KSLC", "KTPA", "KDCA"));
	
	String getLocName(String displayName){
        return utl.ljust(displayName, 14, " ").toUpperCase().substring(0, 14);
    }
	
	String getCondition(String icaoCode, String key) {
	    String url = "https://api.weather.com/v3/wx/forecast/daily/7day?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key;
	    
	    Request request = new Request.Builder()
	            .url(url)
	            .get()
	            .header("Accept", "application/json")
	            .build();

	    try (Response response = client.newCall(request).execute()) {
	        if (!response.isSuccessful()) {
	            return "NO REPORT";
	        }

	        String responseBody = response.body().string();
	        JSONObject jsonResponse = new JSONObject(responseBody);
	        JSONObject dpData = jsonResponse.getJSONArray("daypart").getJSONObject(0);
	        int bump = dpData.getJSONArray("dayOrNight").isNull(0) ? 2 : 0;

	        return utl.getForecast(dpData.getJSONArray("iconCodeExtend").getInt(bump));
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "NO REPORT";
	    }
	}

	String getLow(String icaoCode, String key) {
	    String url = "https://api.weather.com/v3/wx/forecast/daily/7day?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key;
	    
	    Request request = new Request.Builder()
	            .url(url)
	            .get()
	            .header("Accept", "application/json")
	            .build();

	    try (Response response = client.newCall(request).execute()) {
	        if (!response.isSuccessful()) {
	            return "   ";
	        }

	        String responseBody = response.body().string();
	        JSONObject jsonResponse = new JSONObject(responseBody);
	        JSONObject dpData = jsonResponse.getJSONArray("daypart").getJSONObject(0);

	        return utl.rjust(Integer.toString(dpData.getJSONArray("temperature").getInt(1)), 3, " ");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "   ";
	    }
	}

	String getHigh(String icaoCode, String key) {
	    String url = "https://api.weather.com/v3/wx/forecast/daily/7day?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key;
	    
	    Request request = new Request.Builder()
	            .url(url)
	            .get()
	            .header("Accept", "application/json")
	            .build();

	    try (Response response = client.newCall(request).execute()) {
	        if (!response.isSuccessful()) {
	            return "   ";
	        }

	        String responseBody = response.body().string();
	        JSONObject jsonResponse = new JSONObject(responseBody);
	        JSONObject dpData = jsonResponse.getJSONArray("daypart").getJSONObject(0);
	        int bump = dpData.getJSONArray("dayOrNight").isNull(0) ? 2 : 0;

	        return utl.rjust(Integer.toString(dpData.getJSONArray("temperature").getInt(bump)), 3, " ");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "   ";
	    }
	}

	public void run() {
		String key = Main.key;
		Main.travelForecast.locName.clear();
		Main.travelForecast.condition.clear();
		Main.travelForecast.high.clear();
		Main.travelForecast.low.clear();
		for (int i = 0; i < tForIcaos.size(); i++) {
			Main.travelForecast.locName.add(getLocName(tForNames.get(i)));
			Main.travelForecast.condition.add(getCondition(tForIcaos.get(i), key));
			Main.travelForecast.low.add(getLow(tForIcaos.get(i), key));
			Main.travelForecast.high.add(getHigh(tForIcaos.get(i), key));
		}
		int hr = ZonedDateTime.now(Main.timeZone).getHour();
		Main.travelForecast.first = hr >= 4 && hr < 16 ? "H" : "L";
	}
}
