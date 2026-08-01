package WS3000.weatherstar3000;

import org.json.JSONObject;

import util.Utilities;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
//import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;

public class CurrentConditions extends Thread {
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
    static Utilities utl = new Utilities();

    static String getLocName(String displayName) {return "CONDITIONS AT " + utl.ljust(displayName.toUpperCase(), 14, " ").substring(0, 14);}
    static String getCondition(String icaoCode, String key) {
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
            
            return JSONResponse.getString("wxPhraseLong").toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return "NO REPORT";
        }
    }
    static String getTemperature(String icaoCode, String key) {
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/observations/current?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return "           ";
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);

            String temp = utl.rjust(Integer.toString(JSONResponse.getInt("temperature")), 3, " ");

            return "TEMP: " + temp + "\\F";
        } catch (Exception e) {
            e.printStackTrace();
            return "           ";
        }
    }
    static String getFeelsLike(String icaoCode, String key) {
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/observations/current?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return "           ";
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);

            int feelTemp = JSONResponse.getInt("temperatureFeelsLike");
            int temp = JSONResponse.getInt("temperature");
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
    static String getHumidity(String icaoCode, String key) {
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/observations/current?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return "              ";
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);

            String humid = utl.rjust(Integer.toString(JSONResponse.getInt("relativeHumidity")), 3, " ");

            return "HUMIDITY: " + humid + "%";
        } catch (Exception e) {
            e.printStackTrace();
            return "              ";
        }
    }
    static String getDewPoint(String icaoCode, String key) {
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/observations/current?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return "              ";
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);

            String dewpt = utl.rjust(Integer.toString(JSONResponse.getInt("temperatureDewPoint")), 3, " ");

            return "DEWPOINT:" + dewpt + "\\F";
        } catch (Exception e) {
            e.printStackTrace();
            return "              ";
        }
    }
    static String getPressure(String icaoCode, String key) {
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/observations/current?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return "                              ";
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);

            //DecimalFormat df = new DecimalFormat("#.##");
            float pres = JSONResponse.getFloat("pressureAltimeter");
            String fpres = String.format("%.2f", pres);

            int presCode = JSONResponse.getInt("pressureTendencyCode");
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
    static String getWind(String icaoCode, String key) {
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/observations/current?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return "                ";
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);

            int windDir = JSONResponse.getInt("windDirection");
            int windSpeed = JSONResponse.getInt("windSpeed");

            if (windSpeed == 0) {
                return "WIND: CALM      ";
            } else {
                return "WIND: " + utl.degToThreeLetter(windDir) + utl.rjust(Integer.toString(windSpeed), 3, " ")  + " MPH";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "                ";
        }
    }
    static String getGusts(String icaoCode, String key) {
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/observations/current?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return "            ";
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);

            String gus;
            gus = JSONResponse.isNull("windGust") ? "            " : "GUSTS TO " + utl.rjust(Integer.toString(JSONResponse.getInt("windGust")), 3, " ");

            return gus;
        } catch (Exception e) {
            e.printStackTrace();
            return "            ";
        }
    }
    static String getVisiblity(String icaoCode, String key) {
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/observations/current?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return "              ";
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);

            String visib = utl.rjust(Integer.toString(JSONResponse.getInt("visibility")) + " MI.", 7, " ");

            return "VISIB: " + visib;
        } catch (Exception e) {
            e.printStackTrace();
            return "              ";
        }
    }
    String getCeiling(String icaoCode, String key) {
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/observations/current?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return "                 ";
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);

            String ceil = JSONResponse.isNull("cloudCeiling") ? "UNLIMITED" : utl.rjust(JSONResponse.getInt("cloudCeiling") + " FT.", 9, " ");

            return "CEILING:" + ceil;//three length
        } catch (Exception e) {
            e.printStackTrace();
            return "                 ";
        }
    }
    String getPrecip(String key, String lat, String lon) {
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v1/geocode/" + lat + "/" + lon + "/observations/current.json?language=en-US&units=e&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return " ";
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);

            LocalDate cdate = LocalDate.now();
            Month cmonth = cdate.getMonth();

            try {
                JSONObject obsObject = JSONResponse.getJSONObject("observation");
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
