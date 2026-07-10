package WS3000.weatherstar3000;

import org.json.JSONObject;

import util.Utilities;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class RegionalForecast extends Thread {
	ArrayList<String> locName = new ArrayList<>();
	ArrayList<String> condition = new ArrayList<>();
	ArrayList<String> low = new ArrayList<>();
	ArrayList<String> high = new ArrayList<>();
	Utilities utl = new Utilities();
	
	String getLocName(String displayName){
        return utl.ljust(displayName, 14, " ").substring(0, 14);
    }
	String getCondition(String icaoCode, String key) {
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/forecast/daily/7day?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return "NO REPORT";
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);
            JSONObject dpData = JSONResponse.getJSONArray("daypart").getJSONObject(0);
            int bump = dpData.getJSONArray("dayOrNight").isNull(0) ? 2 : 0;

            return utl.getForecast(dpData.getJSONArray("iconCodeExtend").getInt(bump));
        } catch (Exception e) {
            e.printStackTrace();
            return "NO REPORT";
        }
    }
	String getLow(String icaoCode, String key) {
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/forecast/daily/7day?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return "   ";
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);
            JSONObject dpData = JSONResponse.getJSONArray("daypart").getJSONObject(0);

            return utl.rjust(Integer.toString(dpData.getJSONArray("temperature").getInt(1)), 3, " ");
        } catch (Exception e) {
            e.printStackTrace();
            return "   ";
        }
    }
	String getHigh(String icaoCode, String key) {
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/forecast/daily/7day?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return "   ";
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);
            JSONObject dpData = JSONResponse.getJSONArray("daypart").getJSONObject(0);
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
		for (int i = 0; i < regForIcaos.size(); i++) {
			Main.regionalForecast.locName.add(getLocName(regForNames.get(i)));
			Main.regionalForecast.condition.add(getCondition(regForIcaos.get(i), key));
			Main.regionalForecast.low.add(getLow(regForIcaos.get(i), key));
			Main.regionalForecast.high.add(getHigh(regForIcaos.get(i), key));
		}
	}
}
