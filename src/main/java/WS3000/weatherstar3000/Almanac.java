package WS3000.weatherstar3000;

import org.json.JSONObject;

import util.Utilities;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.Objects;

public class Almanac extends Thread {
    Utilities utl = new Utilities();
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
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/forecast/daily/7day?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return "         ";
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);
            String day = JSONResponse.getJSONArray("dayOfWeek").getString(dayNum);

            String[] dayNames = {" SUNDAY  ", " MONDAY  ", " TUESDAY ", "WEDNESDAY", "THURSDAY ", " FRIDAY  ", "SATURDAY "};
            return switch (day) {
                case "Sunday" -> dayNames[0];
                case "Monday" -> dayNames[1];
                case "Tuesday" -> dayNames[2];
                case "Wednesday" -> dayNames[3];
                case "Thursday" -> dayNames[4];
                case "Friday" -> dayNames[5];
                case "Saturday" -> dayNames[6];
                default -> "         ";
            };
        } catch (Exception e) {
            e.printStackTrace();
            return "         ";
        }
    }
    String getSunrise(String icaoCode, String key, int dayNum) {
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/forecast/daily/7day?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return "        ";
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);
            String time = JSONResponse.getJSONArray("sunriseTimeLocal").getString(dayNum);

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
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/forecast/daily/7day?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return "        ";
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);
            String time = JSONResponse.getJSONArray("sunsetTimeLocal").getString(dayNum);

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
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/forecast/daily/7day?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return "error";
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);
            return JSONResponse.getJSONArray("validTimeLocal").getString(0).substring(5, 7);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    String getDayDate(String icaoCode, String key) {
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/forecast/daily/7day?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return "error";
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);
            return JSONResponse.getJSONArray("validTimeLocal").getString(0).substring(8, 10);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    String getTempMax(String icaoCode, String key, int dayNum, String month, String day) {
        if (Objects.equals(month, "error") || Objects.equals(day, "error")) {
            return "      ";
        }
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/almanac/daily/5day?icaoCode=" + icaoCode + "&format=json&units=e&startDay=" + day + "&startMonth=" + month + "&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return "      ";
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);
            int temp = JSONResponse.getJSONArray("temperatureAverageMax").getInt(dayNum);

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
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/almanac/daily/5day?icaoCode=" + icaoCode + "&format=json&units=e&startDay=" + day + "&startMonth=" + month + "&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return "      ";
            }
            String ResponseBody = Response.body();
            JSONObject JSONResponse = new JSONObject(ResponseBody);
            int temp = JSONResponse.getJSONArray("temperatureAverageMin").getInt(dayNum);

            return utl.rjust(temp + " \\F", 6, " ");
        } catch (Exception e) {
            e.printStackTrace();
            return "      ";
        }
    }
    String getPrecip(String icaoCode, String key, String month, String day) {
        if (Objects.equals(month, "error") || Objects.equals(day, "error")) {
            return "      ";
        }
        HttpClient Client = HttpClient.newBuilder().build();
        HttpRequest Request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.com/v3/wx/almanac/daily/5day?icaoCode=" + icaoCode + "&format=json&units=e&startDay=" + day + "&startMonth=" + month + "&apiKey=" + key))
                .GET()
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
            if (Response.statusCode() != 200) {
                return "      ";
            }
            String ResponseBody = Response.body();
            String[] monthNames = {"", "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
            JSONObject JSONResponse = new JSONObject(ResponseBody);
            float precip = JSONResponse.getJSONArray("precipitationAverage").getFloat(0);

            String precipString = utl.rjust(precip + " IN", 8, " ");
            String cmonth = utl.ljust("NORMAL " + monthNames[Integer.parseInt(month)] + " PRECIP", 23, " ");

            return utl.ljust(cmonth + precipString, 31, " ") + " ";
        } catch (Exception e) {
            e.printStackTrace();
            return "      ";
        }
    }
    public void run() {
    	String mainIcao = Main.mainIcao;
    	String key = Main.key;
    	String todayMonth = Main.todayMonth;
    	String todayDay = Main.todayDay;
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