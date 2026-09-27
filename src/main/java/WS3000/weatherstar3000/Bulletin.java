package WS3000.weatherstar3000;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONObject;

import util.Utilities;

public class Bulletin extends Thread {
	private final OkHttpClient client = new OkHttpClient();
	
	static Utilities utl = new Utilities();
	ArrayList<String> bulletinText = new ArrayList<>();
	
	public ArrayList<String> getBulletin(String key, String lat, String lon) {
	    ArrayList<String> words = new ArrayList<>();
	    
	    String url = "https://api.weather.com/v3/alerts/headlines?geocode=" + lat + "," + lon + "&format=json&language=en-US&apiKey=" + key;
	    
	    Request request = new Request.Builder()
	            .url(url)
	            .get()
	            .header("Accept", "application/json")
	            .build();

	    try (Response response = client.newCall(request).execute()) {
	        if (!response.isSuccessful()) {
	            words.add("None");
	            words.add("NOBULLETINCRAWL");
	            return words;
	        }
	        
	        JSONObject responseObject = new JSONObject(response.body().string());
	        
	        for (int i = 0; i < responseObject.getJSONArray("alerts").length(); i++) {
	            JSONObject alertItem = responseObject.getJSONArray("alerts").getJSONObject(i);
	            String headline = alertItem.getString("eventDescription");
	            String detailKey = alertItem.getString("detailKey");
	            
	            if (utl.isScroll(headline)) {
	                return eachAlert(detailKey, key);
	            }
	        }
	        
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
	    
	    String url = "https://api.weather.com/v3/alerts/detail?alertId=" + detailKey 
	            + "&format=json&language=en-US&apiKey=" + key;
	    
	    Request request = new Request.Builder()
	            .url(url)
	            .get()
	            .header("Accept", "application/json")
	            .build();

	    try (Response response = client.newCall(request).execute()) {
	        if (!response.isSuccessful()) {
	            words.add("None");
	            words.add("NOBULLETINCRAWL");
	            return words;
	        }
	        
	        JSONObject responseObj = new JSONObject(response.body().string());
	        JSONObject alertDetail = responseObj.getJSONObject("alertDetail");
	        
	        String headline = alertDetail.getString("eventDescription");
	        String alert = alertDetail.getJSONArray("texts").getJSONObject(0).getString("description").toUpperCase();
	        
	        words.addAll(Arrays.asList(alert.replace("\n\n", " *newline* ").split("\\s+")));
	        
	        StringBuilder sb = new StringBuilder();
	        ArrayList<String> finalWords = new ArrayList<>();
	        
	        finalWords.add(headline);
	        
	        for (int i = 0; i <= words.size() - 1; i++) {
	            if (words.get(i).equals("*newline*")) {
	                if (!(sb.length() == 0)) {
	                    finalWords.add(utl.ljust(sb.toString(), 32, " "));
	                    sb.setLength(0);
	                }
	                
	                finalWords.add(utl.ljust(sb.toString(), 32, " "));
	                sb.setLength(0);
	                continue;
	            }
	            
	            int wordlength = words.get(i).length();
	            if (wordlength > 32) {
	                finalWords.add(utl.ljust(sb.toString(), 32, " "));
	                sb.setLength(0);
	                
	                if (!(sb.length() == 0)) { sb.append(" "); }
	                sb.append(words.get(i));
	                
	                finalWords.add(utl.ljust(sb.toString(), 32, " "));
	                sb.setLength(0);
	            } else {
	                if (wordlength + sb.length() + 1 <= 32) {
	                    if (!(sb.length() == 0)) { sb.append(" "); }
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
	        e.printStackTrace();
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
    	Main.bulletin.bulletinText.clear();
		Main.bulletin.bulletinText = getBulletin(key, mainLat, mainLon);
	}
}
