package WS3000.weatherstar3000;

import org.json.JSONObject;

import util.Utilities;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.time.ZonedDateTime;
import java.util.ArrayList;

public class RegionalForecast extends Thread {
	private final OkHttpClient client = new OkHttpClient();
	
	ArrayList<String> locName = new ArrayList<>();
	ArrayList<String> condition = new ArrayList<>();
	ArrayList<String> low = new ArrayList<>();
	ArrayList<String> high = new ArrayList<>();
	String first;
	Utilities utl = new Utilities();
	
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
		ArrayList<String> regForNames = new ArrayList<>();
		regForNames.addAll(Main.regForNames);
		ArrayList<String> regForIcaos = new ArrayList<>();
		regForIcaos.addAll(Main.regForIcaos);
		String key = Main.key;
		Main.regionalForecast.locName.clear();
		Main.regionalForecast.condition.clear();
		Main.regionalForecast.low.clear();
		Main.regionalForecast.high.clear();
		for (int i = 0; i < regForIcaos.size(); i++) {
			Main.regionalForecast.locName.add(getLocName(regForNames.get(i)));
			Main.regionalForecast.condition.add(getCondition(regForIcaos.get(i), key));
			Main.regionalForecast.low.add(getLow(regForIcaos.get(i), key));
			Main.regionalForecast.high.add(getHigh(regForIcaos.get(i), key));
		}
		int hr = ZonedDateTime.now(Main.timeZone).getHour();
		Main.regionalForecast.first = hr >= 4 && hr < 16 ? "H" : "L";
	}
}
