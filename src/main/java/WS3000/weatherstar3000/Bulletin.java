package WS3000.weatherstar3000;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import util.Utilities;

public class Bulletin extends Thread {
	static Utilities utl = new Utilities();
	ArrayList<String> bulletinText = new ArrayList<>();
	//String color;
	//boolean severe;
	//boolean crawl;
	
	public ArrayList<String> getBulletin(String key, String lat, String lon) {
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
            	words.clear();
            	words.add("None");
                words.add("NOBULLETINCRAWL");
                return words;
            }
            
            JSONObject responseObject = new JSONObject(Response.body());
            
            for (int i = 0; i < responseObject.getJSONArray("alerts").length(); i++) {
            	String headline = responseObject.getJSONArray("alerts").getJSONObject(i).getString("eventDescription");
            
            	String detailKey = responseObject.getJSONArray("alerts").getJSONObject(i).getString("detailKey");
            	if (utl.isScroll(headline)) {
            		return eachAlert(detailKey, key);
            	} else {
            		continue;
            	}
            }
            
            words.clear();
            words.add("None");
            words.add("NOBULLETINCRAWL");
            
            return words;
        } catch (Exception e) {
            e.printStackTrace();
            words.clear();
            words.add("None");
            words.add("NOBULLETINCRAWL");
            return words;
        }
    }
	
	public ArrayList<String> eachAlert(String detailKey, String key) {
		ArrayList<String> words = new ArrayList<>();
		
		HttpClient Client = HttpClient.newBuilder().build();
		HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/alerts/detail?alertId=" + detailKey + "&format=json&language=en-US&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();
        try {
        	HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
            	words.clear();
            	words.add("None");
                words.add("NOBULLETINCRAWL");
                return words;
            }
            
            JSONObject ResponseObj = new JSONObject(Response.body());
            
            String headline = ResponseObj.getJSONObject("alertDetail").getString("eventDescription");
            
            String alert = ResponseObj.getJSONObject("alertDetail").getJSONArray("texts").getJSONObject(0).getString("description").toUpperCase();
            
            words.clear();
            
            words.addAll(Arrays.asList(alert.replace("\n\n", " *newline* ").split("\\s+")));
            
            StringBuilder sb = new StringBuilder();
            ArrayList<String> finalWords = new ArrayList<>();
            
            finalWords.add(headline);
            
            for (int i = 0; i <= words.size()-1; i++) {
            	if (words.get(i).equals("*newline*")) {
            		if (!sb.isEmpty()) {
            			finalWords.add(utl.ljust(sb.toString(), 32, " "));
            			sb.setLength(0);
            		}
            		
            		finalWords.add(utl.ljust(sb.toString(), 32, " "));
            		sb.setLength(0);
            		continue;
            	}
            	
                int wordlength =  words.get(i).length();
                if (wordlength > 32) {
            		finalWords.add(utl.ljust(sb.toString(), 32, " "));
            		sb.setLength(0);
            		
            		if (!sb.isEmpty()) {sb.append(" ");}
            		sb.append(words.get(i));
            		
            		finalWords.add(utl.ljust(sb.toString(), 32, " "));
            		sb.setLength(0);
                } else {
                	if (wordlength + sb.length() + 1 <= 32) {
                		if (!sb.isEmpty()) {sb.append(" ");}
                		sb.append(words.get(i));
                	} else {
                		i--;
                		finalWords.add(utl.ljust(sb.toString(), 32, " "));
                		sb.setLength(0);
                	}
                }
            }
            
            finalWords.add(utl.ljust(sb.toString(), 32, " "));
    		sb.setLength(0);
            
        	return finalWords;
        } catch (Exception e) {
        	words.clear();
        	words.add("None");
            words.add("NOBULLETINCRAWL");
            return words;
        }
	}
	
	public void run() {
		String key = Main.key;
    	String mainLat = Main.mainLat;
    	String mainLon = Main.mainLon;
		Main.bulletin.bulletinText = getBulletin(key, mainLat, mainLon);
		//System.out.println(Main.bulletin.bulletinText);
	}
}
