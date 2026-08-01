package WS3000.weatherstar3000;

import org.json.JSONObject;

import util.Utilities;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class RegionalConditions extends Thread {
    ArrayList<String> locName =  new ArrayList<>();
    ArrayList<String> temperature = new ArrayList<>();
    ArrayList<String> condition = new ArrayList<>();
    Utilities utl = new Utilities();

    String getLocName(String displayName){
        return utl.ljust(displayName, 19, " ").substring(0, 19);
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
    public void run() {
    	ArrayList<String> regConNames = new ArrayList<>();
    	regConNames.addAll(Main.regConNames);
    	ArrayList<String> regConIcaos = new ArrayList<>();
    	regConIcaos.addAll(Main.regConIcaos);
    	String key = Main.key;
    	for (int i = 0; i < regConIcaos.size(); i++) {
            Main.regionalConditions.locName.add(getLocName(regConNames.get(i)));
            Main.regionalConditions.temperature.add(getTemperature(regConIcaos.get(i), key));
            Main.regionalConditions.condition.add(getCondition(regConIcaos.get(i), key));
        }
    }
}