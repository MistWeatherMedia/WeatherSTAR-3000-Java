package WS3000.weatherstar3000;

import org.json.JSONObject;

import util.Utilities;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class HourlyObservations extends Thread {
    ArrayList<String> locName =  new ArrayList<>();
    ArrayList<String> temperature = new ArrayList<>();
    ArrayList<String> condition = new ArrayList<>();
    ArrayList<String> wind = new ArrayList<>();
    Utilities utl = new Utilities();

    String getLocName(String displayName) {
        return utl.ljust(displayName, 14, " ").substring(0, 14);
    }
    String getTemperature(String icaoCode, String key) {
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/observations/current?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
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

            return utl.rjust(Integer.toString(JSONResponse.getInt("temperature")), 3, " ");
        } catch (Exception e) {
            e.printStackTrace();
            return "   ";
        }
    }
    String getCondition(String icaoCode, String key) {
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/observations/current?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
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

            return utl.getCondition(JSONResponse.getInt("iconCodeExtend"));
        } catch (Exception e) {
            e.printStackTrace();
            return "NO REPORT";
        }
    }
    String getWind(String icaoCode, String key) {
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/observations/current?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return "    ";
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);

            int windDir = JSONResponse.getInt("windDirection");
            int windSpeed = JSONResponse.getInt("windSpeed");

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
    	for (int i = 0; i < nearIcaos.size(); i++) {
            Main.hourlyObservations.locName.add(getLocName(nearNames.get(i)));
            Main.hourlyObservations.temperature.add(getTemperature(nearIcaos.get(i), key));
            Main.hourlyObservations.condition.add(getCondition(nearIcaos.get(i), key));
            Main.hourlyObservations.wind.add(getWind(nearIcaos.get(i), key));
        }
    }
}
