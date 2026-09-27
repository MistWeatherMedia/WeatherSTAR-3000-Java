package WS3000.weatherstar3000;

import org.json.JSONObject;

import util.Utilities;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.ArrayList;

public class HourlyObservations extends Thread {
	private final OkHttpClient client = new OkHttpClient();
	
    ArrayList<String> locName =  new ArrayList<>();
    ArrayList<String> temperature = new ArrayList<>();
    ArrayList<String> condition = new ArrayList<>();
    ArrayList<String> wind = new ArrayList<>();
    Utilities utl = new Utilities();

    String getLocName(String displayName) {
        return utl.ljust(displayName, 14, " ").toUpperCase().substring(0, 14);
    }
    
    String getTemperature(String icaoCode, String key) {
        String url = "https://api.weather.com/v3/wx/observations/current?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key;
        
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

            return utl.rjust(Integer.toString(jsonResponse.getInt("temperature")), 3, " ");
        } catch (Exception e) {
            e.printStackTrace();
            return "   ";
        }
    }

    String getCondition(String icaoCode, String key) {
        String url = "https://api.weather.com/v3/wx/observations/current?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key;
        
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

            return utl.getCondition(jsonResponse.getInt("iconCodeExtend"));
        } catch (Exception e) {
            e.printStackTrace();
            return "NO REPORT";
        }
    }

    String getWind(String icaoCode, String key) {
        String url = "https://api.weather.com/v3/wx/observations/current?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key;
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "    ";
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);

            int windDir = jsonResponse.getInt("windDirection");
            int windSpeed = jsonResponse.getInt("windSpeed");

            return utl.formatWind(windSpeed, windDir, false);
        } catch (Exception e) {
            e.printStackTrace();
            return "    ";
        }
    }

    public void run() {
    	ArrayList<String> nearNames = new ArrayList<>();
    	nearNames.addAll(Main.nearNames);
    	ArrayList<String> nearIcaos = new ArrayList<>();;
    	nearIcaos.addAll(Main.nearIcaos);
    	String key = Main.key;
    	Main.hourlyObservations.locName.clear();
    	Main.hourlyObservations.temperature.clear();
    	Main.hourlyObservations.condition.clear();
    	Main.hourlyObservations.wind.clear();
    	for (int i = 0; i < nearIcaos.size(); i++) {
            Main.hourlyObservations.locName.add(getLocName(nearNames.get(i)));
            Main.hourlyObservations.temperature.add(getTemperature(nearIcaos.get(i), key));
            Main.hourlyObservations.condition.add(getCondition(nearIcaos.get(i), key));
            Main.hourlyObservations.wind.add(getWind(nearIcaos.get(i), key));
        }
    }
}
