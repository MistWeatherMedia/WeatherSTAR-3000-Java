package WS3000.weatherstar3000;

import org.json.JSONArray;
import org.json.JSONObject;

import util.Utilities;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;

public class LocalForecast extends Thread {
    ArrayList<String> bulletin = new ArrayList<>();
    ArrayList<String> forecastOne = new ArrayList<>();
    ArrayList<String> forecastTwo = new ArrayList<>();
    ArrayList<String> forecastThree = new ArrayList<>();
    Utilities utl = new Utilities();
    
    ArrayList<String> getForecast(String icaoCode, String key, int forecastId) {
        ArrayList<String> words = new ArrayList<>();
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/forecast/daily/7day?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                words.add("NO REPORT");
                return words;
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);
            JSONObject dpData = JSONResponse.getJSONArray("daypart").getJSONObject(0);
            int bump = dpData.getJSONArray("dayOrNight").isNull(0) ? 1 : 0;
            String fullText = dpData.getJSONArray("daypartName").getString(forecastId + bump).replaceAll("Tomorrow", JSONResponse.getJSONArray("dayOfWeek").getString(bump)).toUpperCase() +
                    "..." +
                    dpData.getJSONArray("narrative").getString(forecastId + bump).toUpperCase();
            String[] splitwords = fullText.split("\\s+");

            words.addAll(Arrays.asList(splitwords));

            return words;
        } catch (Exception e) {
            e.printStackTrace();
            words.clear();
            words.add("NO REPORT");
            return words;
        }
    }
    
    ArrayList<String> getBulletin(String key, String lat, String lon) {
        ArrayList<String> words = new ArrayList<>();
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/alerts/headlines?geocode=" + lat + "," + lon + "&format=json&language=en-US&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                words.add("NOBULLETIN");
                return words;
            }
            
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);
            JSONArray alertData = JSONResponse.getJSONArray("alerts");
            StringBuilder fullText = new StringBuilder();
            
            for (int i = 0; i < alertData.length(); i++) {
            	if (utl.isHeadline(alertData.getJSONObject(i).getString("eventDescription"))) {
            		if (!fullText.isEmpty()) {fullText.append(" *NEWLINE* ");}
            		fullText.append(alertData.getJSONObject(i).getString("headlineText").toUpperCase());
            	}
            }

            String[] splitwords = fullText.toString().split("\\s+");
            
            words.addAll(Arrays.asList(splitwords));
            
            return words;
        } catch (Exception e) {
            e.printStackTrace();
            words.clear();
            words.add("NOBULLETIN");
            return words;
        }
    }
    public void run() {
    	String mainIcao = Main.mainIcao;
    	String key = Main.key;
    	String mainLat = Main.mainLat;
    	String mainLon = Main.mainLon;
    	Main.localForecast.bulletin = getBulletin(key, mainLat, mainLon);
        Main.localForecast.forecastOne = getForecast(mainIcao, key, 0);
        Main.localForecast.forecastTwo = getForecast(mainIcao, key, 1);
        Main.localForecast.forecastThree = getForecast(mainIcao, key, 2);
    }
}
