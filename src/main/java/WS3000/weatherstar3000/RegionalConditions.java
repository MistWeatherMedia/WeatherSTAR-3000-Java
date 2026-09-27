package WS3000.weatherstar3000;

import org.json.JSONObject;

import util.Utilities;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.ArrayList;

public class RegionalConditions extends Thread {
	private final OkHttpClient client = new OkHttpClient();
	
    ArrayList<String> locName =  new ArrayList<>();
    ArrayList<String> temperature = new ArrayList<>();
    ArrayList<String> condition = new ArrayList<>();
    Utilities utl = new Utilities();

    String getLocName(String displayName){
        return utl.ljust(displayName, 19, " ").toUpperCase().substring(0, 19);
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

    public void run() {
    	ArrayList<String> regConNames = new ArrayList<>();
    	regConNames.addAll(Main.regConNames);
    	ArrayList<String> regConIcaos = new ArrayList<>();
    	regConIcaos.addAll(Main.regConIcaos);
    	String key = Main.key;
    	Main.regionalConditions.locName.clear();
    	Main.regionalConditions.temperature.clear();
    	Main.regionalConditions.condition.clear();
    	for (int i = 0; i < regConIcaos.size(); i++) {
            Main.regionalConditions.locName.add(getLocName(regConNames.get(i)));
            Main.regionalConditions.temperature.add(getTemperature(regConIcaos.get(i), key));
            Main.regionalConditions.condition.add(getCondition(regConIcaos.get(i), key));
        }
    }
}