package WS3000.weatherstar3000;

import org.json.JSONObject;

import util.Utilities;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Objects;

public class Almanac extends Thread {
    Utilities utl = new Utilities();
    
    private final OkHttpClient client = new OkHttpClient();
    
    String today;
    String tomorrow;
    String todaySunrise;
    String todaySunset;
    String tomorrowSunrise;
    String tomorrowSunset;
    String todayLow;
    String todayHigh;
    String tomorrowHigh;
    String tomorrowLow;
    String normalPrecip;
    
    String getDay(String icaoCode, String key, int dayNum) {
        String url = "https://api.weather.com/v3/wx/forecast/daily/7day?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key;
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "         ";
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            String day = jsonResponse.getJSONArray("dayOfWeek").getString(dayNum);

            String[] dayNames = {" SUNDAY  ", " MONDAY  ", " TUESDAY ", "WEDNESDAY", "THURSDAY ", " FRIDAY  ", "SATURDAY "};
            switch (day) {
                case "Sunday":    return dayNames[0];
                case "Monday":    return dayNames[1];
                case "Tuesday":   return dayNames[2];
                case "Wednesday": return dayNames[3];
                case "Thursday":  return dayNames[4];
                case "Friday":    return dayNames[5];
                case "Saturday":  return dayNames[6];
                default:          return "         ";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "         ";
        }
    }
    
    String getSunrise(String icaoCode, String key, int dayNum) {
        String url = "https://api.weather.com/v3/wx/forecast/daily/7day?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key;
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "        ";
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            String time = jsonResponse.getJSONArray("sunriseTimeLocal").getString(dayNum);

            int hour = Integer.parseInt(time.substring(11, 13));
            int dispHour = hour > 12 ? hour - 12 : hour;
            String minutes = time.substring(14, 16);
            String part = hour < 12 ? " AM" : " PM";

            return utl.rjust(dispHour + ":" + minutes + part, 8, " ");
        } catch (Exception e) {
            e.printStackTrace();
            return "        ";
        }
    }
    
    String getSunset(String icaoCode, String key, int dayNum) {
        String url = "https://api.weather.com/v3/wx/forecast/daily/7day?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key;
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "        ";
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            String time = jsonResponse.getJSONArray("sunsetTimeLocal").getString(dayNum);

            int hour = Integer.parseInt(time.substring(11, 13));
            int dispHour = hour > 12 ? hour - 12 : hour;
            String minutes = time.substring(14, 16);
            String part = hour < 12 ? " AM" : " PM";

            return utl.rjust(dispHour + ":" + minutes + part, 8, " ");
        } catch (Exception e) {
            e.printStackTrace();
            return "        ";
        }
    }
    
    String getMonth(String icaoCode, String key) {
        String url = "https://api.weather.com/v3/wx/forecast/daily/7day?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key;
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "error";
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            
            return jsonResponse.getJSONArray("validTimeLocal").getString(0).substring(5, 7);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    
    String getDayDate(String icaoCode, String key) {
        String url = "https://api.weather.com/v3/wx/forecast/daily/7day?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key;
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "error";
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            
            return jsonResponse.getJSONArray("validTimeLocal").getString(0).substring(8, 10);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    
    String getTempMax(String icaoCode, String key, int dayNum, String month, String day) {
        if (Objects.equals(month, "error") || Objects.equals(day, "error")) {
            return "      ";
        }
        
        String url = "https://api.weather.com/v3/wx/almanac/daily/5day?icaoCode=" + icaoCode + "&format=json&units=e&startDay=" + day + "&startMonth=" + month + "&apiKey=" + key;
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "      ";
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            
            int temp = jsonResponse.getJSONArray("temperatureAverageMax").getInt(dayNum);

            return utl.rjust(temp + " \\F", 6, " ");
        } catch (Exception e) {
            e.printStackTrace();
            return "      ";
        }
    }
    
    String getTempMin(String icaoCode, String key, int dayNum, String month, String day) {
        if (Objects.equals(month, "error") || Objects.equals(day, "error")) {
            return "      ";
        }
        
        String url = "https://api.weather.com/v3/wx/almanac/daily/5day?icaoCode=" + icaoCode + "&format=json&units=e&startDay=" + day + "&startMonth=" + month + "&apiKey=" + key;
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "      ";
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            
            int temp = jsonResponse.getJSONArray("temperatureAverageMin").getInt(dayNum);

            return utl.rjust(temp + " \\F", 6, " ");
        } catch (Exception e) {
            e.printStackTrace();
            return "      ";
        }
    }
    
    String getPrecip(String icaoCode, String key, String month, String day) {
        if (Objects.equals(month, "error") || Objects.equals(day, "error")) {
            return utl.ljust("", 32, " ");
        }
        
        String url = "https://api.weather.com/v3/wx/almanac/daily/5day?icaoCode=" + icaoCode + "&format=json&units=e&startDay=" + day + "&startMonth=" + month + "&apiKey=" + key;
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return utl.ljust("", 32, " ");
            }

            String responseBody = response.body().string();
            String[] monthNames = {"", "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
            JSONObject jsonResponse = new JSONObject(responseBody);
            
            String precip = jsonResponse.getJSONArray("precipitationAverage").isNull(0) 
                    ? "NO REPORT" 
                    : String.valueOf(jsonResponse.getJSONArray("precipitationAverage").getFloat(0));

            String precipString = precip.equals("NO REPORT") ? "NO REPORT" : utl.rjust(precip + " IN", 9, " ");
            String cmonth = utl.ljust("NORMAL " + monthNames[Integer.parseInt(month)] + " PRECIP", 22, " ");

            return utl.ljust(cmonth + precipString, 31, " ") + " ";
        } catch (Exception e) {
            e.printStackTrace();
            return utl.ljust("", 32, " ");
        }
    }
    
    public void run() {
    	String mainIcao = Main.mainIcao;
    	String key = Main.key;
    	String todayMonth = getMonth(mainIcao, key);;
    	String todayDay = getDayDate(mainIcao, key);;
    	Main.almanac.today = getDay(mainIcao, key, 0);
    	Main.almanac.tomorrow = getDay(mainIcao, key, 1);
    	Main.almanac.todaySunrise = getSunrise(mainIcao, key, 0);
    	Main.almanac.todaySunset = getSunset(mainIcao, key, 0);
    	Main.almanac.tomorrowSunrise = getSunrise(mainIcao, key, 1);
    	Main.almanac.tomorrowSunset = getSunset(mainIcao, key, 1);
    	Main.almanac.todayHigh = getTempMax(mainIcao, key, 0, todayMonth, todayDay);
    	Main.almanac.todayLow = getTempMin(mainIcao, key, 0, todayMonth, todayDay);
    	Main.almanac.tomorrowHigh = getTempMax(mainIcao, key, 1, todayMonth, todayDay);
    	Main.almanac.tomorrowLow = getTempMin(mainIcao, key, 1, todayMonth, todayDay);
    	Main.almanac.normalPrecip = getPrecip(mainIcao, key, todayMonth, todayDay);
    }
}