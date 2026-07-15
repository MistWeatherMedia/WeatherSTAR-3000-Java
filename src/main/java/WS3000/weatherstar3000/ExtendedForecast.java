package WS3000.weatherstar3000;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import org.json.JSONObject;

import util.Utilities;

public class ExtendedForecast extends Thread {
	String dayOne;
	String dayTwo;
	String dayThree;
	ArrayList<String> conditionOne;
	ArrayList<String> conditionTwo;
	ArrayList<String> conditionThree;
	String lowOne;
	String lowTwo;
	String lowThree;
	String highOne;
	String highTwo;
	String highThree;
	Utilities utl = new Utilities();
	String getDayName(String icaoCode, String key, int day) {
		HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/forecast/daily/7day?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return "          ";
            }
            String ResponseBody = Response.body();
            //JSONObject JSONResponse = new JSONObject(ResponseBody);
            //JSONObject dpData = JSONResponse.getJSONArray("daypart").getJSONObject(0);
            //int bump = dpData.getJSONArray("dayOrNight").isNull(0) ? 1 : 0;

            return utl.ljust(new JSONObject(ResponseBody).getJSONArray("dayOfWeek").getString(day + 2).toUpperCase(), 10, " ");
        } catch (Exception e) {
            e.printStackTrace();
            return "          ";
        }
	}
	ArrayList<String> getCondition(String icaoCode, String key, int day) {
		ArrayList<String> values = new ArrayList<>();
		HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/forecast/daily/7day?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
            	values.add("          ");
            	values.add("          ");
                return values;
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);
            JSONObject dpData = JSONResponse.getJSONArray("daypart").getJSONObject(0);
            int bump = dpData.getJSONArray("dayOrNight").isNull(0) ? 5 : 4;
            int ex = day*2;
            
            values.add(utl.getForecastEx(dpData.getJSONArray("iconCodeExtend").getInt(ex + bump),  0));
            values.add(utl.getForecastEx(dpData.getJSONArray("iconCodeExtend").getInt(ex + bump),  1));
            
            return values;
        } catch (Exception e) {
            e.printStackTrace();
            values.add("          ");
        	values.add("          ");
            return values;
        }
	}
	String getLow(String icaoCode, String key, int day) {
		HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/forecast/daily/7day?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return "          ";
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);
            JSONObject dpData = JSONResponse.getJSONArray("daypart").getJSONObject(0);
            int bump = dpData.getJSONArray("dayOrNight").isNull(0) ? 5 : 4;
            int ex = day*2;
            
            return "LO:" + utl.rjust(Integer.toString(dpData.getJSONArray("temperature").getInt(ex + bump + 1)), 4, " ") + "   ";
        } catch (Exception e) {
            e.printStackTrace();
            return "          ";
        }
	}
	String getHigh(String icaoCode, String key, int day) {
		HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/forecast/daily/7day?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return "          ";
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);
            JSONObject dpData = JSONResponse.getJSONArray("daypart").getJSONObject(0);
            int bump = dpData.getJSONArray("dayOrNight").isNull(0) ? 5 : 4;
            int ex = day*2;
            
            return "HI:" + utl.rjust(Integer.toString(dpData.getJSONArray("temperature").getInt(ex + bump)), 4, " ") + "   ";
        } catch (Exception e) {
            e.printStackTrace();
            return "          ";
        }
	}
	public void run() {
		String key = Main.key;
		String mainIcao = Main.mainIcao;
		Main.extendedForecast.dayOne = getDayName(mainIcao, key, 0);
		Main.extendedForecast.dayTwo = getDayName(mainIcao, key, 1);
		Main.extendedForecast.dayThree = getDayName(mainIcao, key, 2);
		Main.extendedForecast.conditionOne = getCondition(mainIcao, key, 0);
		Main.extendedForecast.conditionTwo = getCondition(mainIcao, key, 1);
		Main.extendedForecast.conditionThree = getCondition(mainIcao, key, 2);
		Main.extendedForecast.lowOne = getLow(mainIcao, key, 0);
		Main.extendedForecast.lowTwo = getLow(mainIcao, key, 1);
		Main.extendedForecast.lowThree = getLow(mainIcao, key, 2);
		Main.extendedForecast.highOne = getHigh(mainIcao, key, 0);
		Main.extendedForecast.highTwo = getHigh(mainIcao, key, 1);
		Main.extendedForecast.highThree = getHigh(mainIcao, key, 2);
	}
}
