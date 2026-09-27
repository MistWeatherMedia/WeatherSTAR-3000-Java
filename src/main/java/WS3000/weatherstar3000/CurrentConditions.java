package WS3000.weatherstar3000;

import org.json.JSONObject;

import util.Utilities;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
//import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.time.Month;

public class CurrentConditions extends Thread {
	private final OkHttpClient client = new OkHttpClient();
	
    String locName;
    String condition;
    String temperature;
    String feelsLike;
    String humidity;
    String dewPoint;
    String pressure;
    String wind;
    String gusts;
    String visibility;
    String ceiling;
    String precip;
    
    Utilities utl = new Utilities();

    String getLocName(String displayName) {return "CONDITIONS AT " + utl.ljust(displayName.toUpperCase(), 14, " ").substring(0, 14);}
    
    String getCondition(String icaoCode, String key) {
        String url = "https://api.weather.com/v3/wx/observations/current?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key;
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "NO CURRENT REPORT";
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            
            return jsonResponse.getString("wxPhraseLong").toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return "NO CURRENT REPORT";
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
                return "           ";
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);

            String temp = utl.rjust(Integer.toString(jsonResponse.getInt("temperature")), 3, " ");

            return "TEMP: " + temp + "\\F";
        } catch (Exception e) {
            e.printStackTrace();
            return "           ";
        }
    }
    
    String getFeelsLike(String icaoCode, String key) {
        String url = "https://api.weather.com/v3/wx/observations/current?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key;
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "           ";
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);

            int feelTemp = jsonResponse.getInt("temperatureFeelsLike");
            int temp = jsonResponse.getInt("temperature");
            
            if (temp == feelTemp) {
                return "                 ";
            } else {
                if (feelTemp > 65) {
                    return "HEAT INDEX:" + utl.rjust(Integer.toString(feelTemp), 3, " ") + "\\F";
                } else {
                    return "WIND CHILL:" + utl.rjust(Integer.toString(feelTemp), 3, " ") + "\\F";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "                 ";
        }
    }
    
    String getHumidity(String icaoCode, String key) {
        String url = "https://api.weather.com/v3/wx/observations/current?icaoCode=" + icaoCode 
                + "&units=e&language=en-US&format=json&apiKey=" + key;
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "              ";
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);

            String humid = utl.rjust(Integer.toString(jsonResponse.getInt("relativeHumidity")), 3, " ");

            return "HUMIDITY: " + humid + "%";
        } catch (Exception e) {
            e.printStackTrace();
            return "              ";
        }
    }
    
    String getDewPoint(String icaoCode, String key) {
        String url = "https://api.weather.com/v3/wx/observations/current?icaoCode=" + icaoCode 
                + "&units=e&language=en-US&format=json&apiKey=" + key;
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "              ";
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);

            String dewpt = utl.rjust(Integer.toString(jsonResponse.getInt("temperatureDewPoint")), 3, " ");

            return "DEWPOINT:" + dewpt + "\\F";
        } catch (Exception e) {
            e.printStackTrace();
            return "              ";
        }
    }
    
    String getPressure(String icaoCode, String key) {
        String url = "https://api.weather.com/v3/wx/observations/current?icaoCode=" + icaoCode 
                + "&units=e&language=en-US&format=json&apiKey=" + key;
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "                              ";
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);

            float pres = jsonResponse.getFloat("pressureAltimeter");
            String fpres = String.format("%.2f", pres);

            int presCode = jsonResponse.getInt("pressureTendencyCode");
            String fpresCode = "";
            if (presCode == 0) {
                fpresCode = "IN.";
            } else if (presCode == 1 || presCode == 3) {
                fpresCode = "R";
            } else if (presCode == 2 || presCode == 4) {
                fpresCode = "F";
            }

            return "BAROMETRIC PRESSURE: " + fpres + " " + fpresCode;
        } catch (Exception e) {
            e.printStackTrace();
            return "                              ";
        }
    }
    
    String getWind(String icaoCode, String key) {
        String url = "https://api.weather.com/v3/wx/observations/current?icaoCode=" + icaoCode 
                + "&units=e&language=en-US&format=json&apiKey=" + key;
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "                ";
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);

            int windDir = jsonResponse.getInt("windDirection");
            int windSpeed = jsonResponse.getInt("windSpeed");

            if (windSpeed == 0) {
                return "WIND: CALM      ";
            } else {
                return "WIND: " + utl.degToThreeLetter(windDir) + utl.rjust(Integer.toString(windSpeed), 3, " ") + " MPH";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "                ";
        }
    }
    
    String getGusts(String icaoCode, String key) {
        String url = "https://api.weather.com/v3/wx/observations/current?icaoCode=" + icaoCode 
                + "&units=e&language=en-US&format=json&apiKey=" + key;
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "            ";
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);

            String gus = jsonResponse.isNull("windGust") 
                    ? "            " 
                    : "GUSTS TO " + utl.rjust(Integer.toString(jsonResponse.getInt("windGust")), 3, " ");

            return gus;
        } catch (Exception e) {
            e.printStackTrace();
            return "            ";
        }
    }
    
    String getVisiblity(String icaoCode, String key) {
        String url = "https://api.weather.com/v3/wx/observations/current?icaoCode=" + icaoCode 
                + "&units=e&language=en-US&format=json&apiKey=" + key;
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "              ";
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);

            String visib = utl.rjust(Integer.toString(jsonResponse.getInt("visibility")) + " MI.", 7, " ");

            return "VISIB: " + visib;
        } catch (Exception e) {
            e.printStackTrace();
            return "              ";
        }
    }
    
    String getCeiling(String icaoCode, String key) {
        String url = "https://api.weather.com/v3/wx/observations/current?icaoCode=" + icaoCode 
                + "&units=e&language=en-US&format=json&apiKey=" + key;
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "                 ";
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);

            String ceil = jsonResponse.isNull("cloudCeiling") 
                    ? "UNLIMITED" 
                    : utl.rjust(jsonResponse.getInt("cloudCeiling") + " FT.", 9, " ");

            return "CEILING:" + ceil;
        } catch (Exception e) {
            e.printStackTrace();
            return "                 ";
        }
    }
    
    String getPrecip(String key, String lat, String lon) {
        String url = "https://api.weather.com/v1/geocode/" + lat + "/" + lon + "/observations/current.json?language=en-US&units=e&apiKey=" + key;
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return " ";
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);

            ZonedDateTime cdate = ZonedDateTime.now(Main.timeZone);
            Month cmonth = cdate.getMonth();

            try {
                JSONObject obsObject = jsonResponse.getJSONObject("observation");
                JSONObject impObj = obsObject.getJSONObject("imperial");
                String fprecip = String.format("%.2f", impObj.getFloat("precip_mtd"));
                String prec = utl.rjust(fprecip + " IN", 8, " ");

                return cmonth.name().toUpperCase() + " PRECIPITATION:" + prec;
            } catch (Exception e) {
                return " ";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return " ";
        }
    }

    public void run() {
    	String mainLocName = Main.mainLocName.getText();
    	String key = Main.key;
    	String mainIcao = Main.mainIcao;
    	String mainLat = Main.mainLat;
    	String mainLon = Main.mainLon;
    	Main.currentConditions.locName = getLocName(mainLocName);
    	Main.currentConditions.condition = getCondition(mainIcao, key);
    	Main.currentConditions.temperature = getTemperature(mainIcao, key);
    	Main.currentConditions.feelsLike = getFeelsLike(mainIcao, key);
    	Main.currentConditions.humidity = getHumidity(mainIcao, key);
    	Main.currentConditions.dewPoint = getDewPoint(mainIcao, key);
    	Main.currentConditions.pressure = getPressure(mainIcao, key);
    	Main.currentConditions.wind = getWind(mainIcao, key);
    	Main.currentConditions.gusts = getGusts(mainIcao, key);
        Main.currentConditions.visibility = getVisiblity(mainIcao, key);
        Main.currentConditions.ceiling = getCeiling(mainIcao, key);
        Main.currentConditions.precip = getPrecip(key, mainLat, mainLon);
    }
}
