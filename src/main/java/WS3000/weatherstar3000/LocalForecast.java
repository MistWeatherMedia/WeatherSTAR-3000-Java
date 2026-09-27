package WS3000.weatherstar3000;

import org.json.JSONArray;
import org.json.JSONObject;

import util.Utilities;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.Arrays;

public class LocalForecast extends Thread {
	private final OkHttpClient client = new OkHttpClient();
	
    ArrayList<String> bulletin = new ArrayList<>();
    ArrayList<String> forecastOne = new ArrayList<>();
    ArrayList<String> forecastTwo = new ArrayList<>();
    ArrayList<String> forecastThree = new ArrayList<>();
    Utilities utl = new Utilities();
    
    ArrayList<String> getForecast(String icaoCode, String key, int forecastId) {
        ArrayList<String> words = new ArrayList<>();
        String url = "https://api.weather.com/v3/wx/forecast/daily/7day?icaoCode=" + icaoCode 
                + "&units=e&language=en-US&format=json&apiKey=" + key;
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                words.add("NO");
                words.add("REPORT");
                return words;
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONObject dpData = jsonResponse.getJSONArray("daypart").getJSONObject(0);
            int bump = dpData.getJSONArray("dayOrNight").isNull(0) ? 1 : dpData.getJSONArray("dayOrNight").getString(0).equals("N") ? 1 : 0;
            
            String fullText = dpData.getJSONArray("daypartName").getString(forecastId + bump).replaceAll("Tomorrow", jsonResponse.getJSONArray("dayOfWeek").getString(1)).toUpperCase() +
                    "..." +
                    dpData.getJSONArray("narrative").getString(forecastId + bump).toUpperCase();
            String[] splitwords = fullText.split("\\s+");

            words.addAll(Arrays.asList(splitwords));

            return words;
        } catch (Exception e) {
            e.printStackTrace();
            words.clear();
            words.add("NO");
            words.add("REPORT");
            return words;
        }
    }

    ArrayList<String> getBulletin(String key, String lat, String lon) {
        ArrayList<String> words = new ArrayList<>();
        String url = "https://api.weather.com/v3/alerts/headlines?geocode=" + lat + "," + lon 
                + "&format=json&language=en-US&apiKey=" + key;
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                words.add("NOBULLETIN");
                return words;
            }
            
            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONArray alertData = jsonResponse.getJSONArray("alerts");
            StringBuilder fullText = new StringBuilder();
            
            for (int i = 0; i < alertData.length(); i++) {
                if (utl.isHeadline(alertData.getJSONObject(i).getString("eventDescription"))) {
                    if (!(fullText.length() == 0)) {
                        fullText.append(" *NEWLINE* ");
                    }
                    fullText.append(alertData.getJSONObject(i).getString("headlineText").toUpperCase());
                }
            }

            words.clear();
            
            if (!(fullText.length() == 0)) {
                String[] splitwords = fullText.toString().split("\\s+");
                words.addAll(Arrays.asList(splitwords));
            } else {
                words.add("NOBULLETIN");
            }
            
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
