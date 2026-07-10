package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.json.*;
import org.joml.Vector4f;

public class Utilities {
	public static Map<Character, Integer> mainFontMap = new HashMap<>();
    public static Map<Character, Integer> smallFontMap = new HashMap<>();
    public static String[] testFlavor = {"outlook","travelForecast", "localForecast", "outlook"};
    public static String[] flavorA = {"currentConditions","localForecast","hourlyObservations"};
    public static String[] flavorB = {"hourlyObservations", "almanac", "extendedForecast", "regionalConditions"};
    public static String[] flavorC = {"regionalConditions", "localForecast", "currentConditions"};
    public static String[] flavorD = {"hourlyObservations", "almanac", "localForecast", "regionalConditions"};
    public static String[] flavorE = {"localForecast", "extendedForecast","hourlyObservations"};
    public static String[] flavorF = {"currentConditions", "regionalConditions", "localForecast", "almanac"};
    public static String[] flavorG = {"localForecast"};
    public static String[] flavorH = {"localForecast", "regionalforecast", "almanac", "hourlyObservations"};
    public static String[] flavorI = {"currentConditions", "hourlyObservations", "regionalConditions"};
    public static String[] flavorJ = {"currentConditions", "hourlyObservations", "regionalConditions", "localForecast", "almanac", "regionalForecast", "travelForecast", "extendedForecast", "outlook"};
    public static String[] flavorK = {"currentConditions", "almanac", "regionalForecast", "localForecast", "extendedForecast", "hourlyObservations"};
    public static String[] flavorL = {"currentConditions", "hourlyObservations", "regionalConditions", "regionalForecast", "almanac", "localForecast", "extendedForecast"};
    public static String[] flavorM = {"currentConditions", "localForecast", "extendedForecast", "travelForecast"};
    public static String[] flavorN = {"currentConditions", "hourlyObservations", "regionalConditions", "localForecast", "almanac", "regionalForecast", "travelForecast", "extendedForecast", "outlook", "currentConditions", "localForecast", "extendedForecast", "travelForecast"};
    public static String[] flavorO = {"currentConditions", "hourlyObservations", "regionalConditions", "localForecast", "almanac", "regionalForecast", "travelForecast", "extendedForecast", "outlook", "currentConditions", "hourlyObservations", "regionalConditions", "localForecast", "almanac", "regionalForecast", "travelForecast", "extendedForecast", "outlook"};
    public static String[] flavorP = {"localForecast", "extendedForecast", "currentConditions"};
    public static String[] flavorQ = {"currentConditions", "almanac", "localForecast", "regionalConditions"};
    
    static {
        mainFontMap.put(' ', 0);
        mainFontMap.put('!', 1);
        mainFontMap.put('"', 2);
        mainFontMap.put('#', 3);
        mainFontMap.put('$', 4);
        mainFontMap.put('%', 5);
        mainFontMap.put('&', 6);
        mainFontMap.put('\'', 7);
        mainFontMap.put('(', 8);
        mainFontMap.put(')', 9);
        mainFontMap.put('*', 10);
        mainFontMap.put('+', 11);
        mainFontMap.put(',', 12);
        mainFontMap.put('-', 13);
        mainFontMap.put('.', 14);
        mainFontMap.put('/', 15);
        mainFontMap.put('0', 16);
        mainFontMap.put('1', 17);
        mainFontMap.put('2', 18);
        mainFontMap.put('3', 19);
        mainFontMap.put('4', 20);
        mainFontMap.put('5', 21);
        mainFontMap.put('6', 22);
        mainFontMap.put('7', 23);
        mainFontMap.put('8', 24);
        mainFontMap.put('9', 25);
        mainFontMap.put(':', 26);
        mainFontMap.put(';', 27);
        mainFontMap.put('<', 28);
        mainFontMap.put('=', 29);
        mainFontMap.put('>', 30);
        mainFontMap.put('?', 31);
        mainFontMap.put('@', 64);
        mainFontMap.put('A', 65);
        mainFontMap.put('B', 66);
        mainFontMap.put('C', 67);
        mainFontMap.put('D', 68);
        mainFontMap.put('E', 69);
        mainFontMap.put('F', 70);
        mainFontMap.put('G', 71);
        mainFontMap.put('H', 72);
        mainFontMap.put('I', 73);
        mainFontMap.put('J', 74);
        mainFontMap.put('K', 75);
        mainFontMap.put('L', 76);
        mainFontMap.put('M', 77);
        mainFontMap.put('N', 78);
        mainFontMap.put('O', 79);
        mainFontMap.put('P', 80);
        mainFontMap.put('Q', 81);
        mainFontMap.put('R', 82);
        mainFontMap.put('S', 83);
        mainFontMap.put('T', 84);
        mainFontMap.put('U', 85);
        mainFontMap.put('V', 86);
        mainFontMap.put('W', 87);
        mainFontMap.put('X', 88);
        mainFontMap.put('Y', 89);
        mainFontMap.put('Z', 90);
        mainFontMap.put('[', 91);
        mainFontMap.put('\\', 92);
        mainFontMap.put(']', 93);
        mainFontMap.put('^', 94);
        mainFontMap.put('_', 95);
        mainFontMap.put('`', 96);

        smallFontMap.put(' ', 32);
        smallFontMap.put('!', 33);
        smallFontMap.put('"', 34);
        smallFontMap.put('#', 35);
        smallFontMap.put('$', 36);
        smallFontMap.put('%', 37);
        smallFontMap.put('&', 38);
        smallFontMap.put('\'', 39);
        smallFontMap.put('(', 40);
        smallFontMap.put(')', 41);
        smallFontMap.put('*', 42);
        smallFontMap.put('+', 43);
        smallFontMap.put(',', 44);
        smallFontMap.put('-', 45);
        smallFontMap.put('.', 46);
        smallFontMap.put('/', 47);
        smallFontMap.put('0', 48);
        smallFontMap.put('1', 49);
        smallFontMap.put('2', 50);
        smallFontMap.put('3', 51);
        smallFontMap.put('4', 52);
        smallFontMap.put('5', 53);
        smallFontMap.put('6', 54);
        smallFontMap.put('7', 55);
        smallFontMap.put('8', 56);
        smallFontMap.put('9', 57);
        smallFontMap.put(':', 58);
        smallFontMap.put(';', 59);
        smallFontMap.put('<', 60);
        smallFontMap.put('=', 61);
        smallFontMap.put('>', 62);
        smallFontMap.put('?', 63);
        smallFontMap.put('A', 97);
        smallFontMap.put('B', 98);
        smallFontMap.put('C', 99);
        smallFontMap.put('D', 100);
        smallFontMap.put('E', 101);
        smallFontMap.put('F', 102);
        smallFontMap.put('G', 103);
        smallFontMap.put('H', 104);
        smallFontMap.put('I', 105);
        smallFontMap.put('J', 106);
        smallFontMap.put('K', 107);
        smallFontMap.put('L', 108);
        smallFontMap.put('M', 109);
        smallFontMap.put('N', 110);
        smallFontMap.put('O', 111);
        smallFontMap.put('P', 112);
        smallFontMap.put('Q', 113);
        smallFontMap.put('R', 114);
        smallFontMap.put('S', 115);
        smallFontMap.put('T', 116);
        smallFontMap.put('U', 117);
        smallFontMap.put('V', 118);
        smallFontMap.put('W', 119);
        smallFontMap.put('X', 120);
        smallFontMap.put('Y', 121);
        smallFontMap.put('Z', 122);
        smallFontMap.put('[', 123);
        smallFontMap.put('\\', 124);
        smallFontMap.put(']', 125);
        smallFontMap.put('^', 126);
        smallFontMap.put('`', 127);
        smallFontMap.put('_', 128);
    }
    
    public int getMainChar(char character) {
        return mainFontMap.get(character);
    }
    
    public int getSmallChar(char character) {
        return smallFontMap.get(character);
    }
    
    public String ljust(String s, int width, String fillChar) {
        if (s.length() >= width) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < width) {
            sb.append(fillChar);
        }
        return sb.toString();
    }
    
    public String rjust(String s, int width, String fillChar) {
        if (s.length() >= width) {
            return s;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < width - s.length()) {
            sb.append(fillChar);
        }
        sb.append(s);
        return sb.toString();
    }
    
    public String cjust(String s, int width, String fillChar) {
        if (s.length() >= width) {
            return s; // No centering needed if text is already too long
        }
        int padding = width - s.length();
        int leftPadding = padding / 2;
        int rightPadding = padding - leftPadding;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < leftPadding; i++) {
            sb.append(fillChar);
        }
        sb.append(s);
        for(int i = 0; i < rightPadding; i++) {
            sb.append(fillChar);
        }
        return sb.toString();
    }
    
    public String degToThreeLetter(int windDir) {
        if (windDir >= 0 && windDir <= 11.25) {
            return "N  ";
        } else if (windDir > 11.25 && windDir <= 33.75) {
            return "NNE";
        } else if (windDir > 33.75 && windDir <= 56.25) {
            return "NE ";
        } else if (windDir > 56.25 && windDir <= 78.75) {
            return "ENE";
        } else if (windDir > 78.75 && windDir <= 101.25) {
            return "E  ";
        } else if (windDir > 101.25 && windDir <= 123.75) {
            return "ESE";
        } else if (windDir > 123.75 && windDir <= 146.25) {
            return "SE ";
        } else if (windDir > 146.25 && windDir <= 168.75) {
            return "SSE";
        } else if (windDir > 168.75 && windDir <= 191.25) {
            return "S  ";
        } else if (windDir > 191.25 && windDir <= 213.75) {
            return "SSW";
        } else if (windDir > 213.75 && windDir <= 236.25) {
            return "SW ";
        } else if (windDir > 236.25 && windDir <= 258.75) {
            return "WSW";
        } else if (windDir > 258.75 && windDir <= 281.25) {
            return "W  ";
        } else if (windDir > 281.25 && windDir <= 303.75) {
            return "WNW";
        } else if (windDir > 303.75 && windDir <= 326.25) {
            return "NW ";
        } else if (windDir > 326.25 && windDir <= 348.75) {
            return "NNW";
        } else if (windDir > 348.75 && windDir <= 360) {
            return "N  ";
        } else {
            return "   ";
        }
    }
    
    public String degToTwoLetter(int windDir) {
        if (windDir >= 0 && windDir <= 22.5) {
            return "N ";
        } else if (windDir > 22.5 && windDir <= 67.5) {
            return "NE";
        } else if (windDir > 67.5 && windDir <= 112.5) {
            return "E ";
        } else if (windDir > 112.5 && windDir <= 157.5) {
            return "SE";
        } else if (windDir > 157.5 && windDir <= 202.5) {
            return "S ";
        } else if (windDir > 202.5 && windDir <= 247.5) {
            return "SW";
        } else if (windDir > 247.5 && windDir <= 292.5) {
            return "W ";
        } else if (windDir > 292.5 && windDir <= 337.5) {
            return "NW";
        } else if (windDir > 337.5 && windDir <= 360) {
            return "N ";
        } else {
            return "  ";
        }
    }
    
    public String degToOneLetter(int windDir) {
        if (windDir >= 0 && windDir <= 45) {
            return "N";
        } else if (windDir > 45 && windDir <= 135) {
            return "E";
        } else if (windDir > 135 && windDir <= 225) {
            return "S";
        } else if (windDir > 225 && windDir <= 315) {
            return "W";
        } else if (windDir > 315 && windDir <= 360) {
            return "N";
        } else {
            return " ";
        }
    }
    
    public String formatWind(int windSpeed, int windDir, boolean spaced) {
        String spacer = "";
        String ending = "";
        int speedLength = Integer.toString(windSpeed).length();
        if  (spaced) {spacer = " "; ending = " MPH";}
        if (windSpeed == 0) {return "CALM";}
        if (speedLength == 1) {
            return degToThreeLetter(windDir) + spacer + windSpeed + ending;
        } else if (speedLength == 2) {
            return degToTwoLetter(windDir) + spacer + windSpeed + ending;
        } else if (speedLength == 3) {
            return degToOneLetter(windDir) + spacer + windSpeed + ending;
        } else {
            return "    " + spacer + ending;
        }
    }
    
    public String formatShortCond(String condition) {
    	String conditionFinal = condition;
    	if (conditionFinal.contains("EARLY")) {
    		conditionFinal = "AM " + conditionFinal.replaceAll("EARLY", "");
    	}
    	if (conditionFinal.contains("LATE")) {
    		conditionFinal = "PM " + conditionFinal.replaceAll("LATE", "");
    	}
        return conditionFinal.replaceAll(" SHOWER", " SHWR").replaceAll("NEAR", "").replaceAll("LIGHT", "LGT").replaceAll("HEAVY", "HVY").replaceAll("PARTIAL", "PART").replaceAll("CLDY", "CLOUDY").replaceAll(" T-STORM", " T-STM").replaceAll("SHOWERS/", "SHWRS/");
    }
    
    private String readJsonFromResource(String path) throws IOException {
        StringBuilder jsonText = new StringBuilder();
        
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                throw new IOException("Resource not found inside JAR: " + path);
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = br.readLine()) != null) {
                    jsonText.append(line).append("\n");
                }
            }
        }
        return jsonText.toString();
    }
    
    public String getCondition(int iconCode) throws IOException {
        String json = readJsonFromResource("/resources/json/ObsMapping.json");
        return new JSONObject(json).getString(Integer.toString(iconCode)).toUpperCase();
    }
    
    public String getForecast(int iconCode) throws IOException {
        String json = readJsonFromResource("/resources/json/FcstMapping.json");
        return new JSONObject(json).getString(Integer.toString(iconCode)).toUpperCase();
    }
    
    public String getForecastEx(int iconCode, int lineNum) throws IOException {
        String json = readJsonFromResource("/resources/json/FcstTwoLineMapping.json");
        return ljust(new JSONObject(json).getJSONArray(Integer.toString(iconCode)).getString(lineNum).toUpperCase(), 10, " ");
    }
    
    public boolean isHeadline(String headline) throws IOException {
        String json = readJsonFromResource("/resources/json/AlertMapping.json");
        JSONObject jsonObject = new JSONObject(json);
        
        if (jsonObject.has(headline)) {
            return jsonObject.getJSONObject(headline).getBoolean("headline");
        } else {
            return false;
        }
    }
    
    public boolean isScroll(String headline) throws IOException {
        String json = readJsonFromResource("/resources/json/AlertMapping.json");
        JSONObject jsonObject = new JSONObject(json);
        
        if (jsonObject.has(headline)) {
            return jsonObject.getJSONObject(headline).getBoolean("scroll");
        } else {
            return false;
        }
    }
    
    public String getSeverity(String headline) throws IOException {
        String json = readJsonFromResource("/resources/json/AlertMapping.json");
        JSONObject jsonObject = new JSONObject(json);
        
        if (jsonObject.has(headline)) {
            return jsonObject.getJSONObject(headline).getString("severity");
        } else {
            return "None";
        }
    }
    
    public Vector4f getBulletinColor(String headline) throws IOException {
    	if (getSeverity(headline).equals("Warning")) {
    		return new Vector4f(104.0f/255.0f, 0.0f/255.0f, 8.0f/255.0f, 1.0f);
    	} else if (getSeverity(headline).equals("Advisory")) {
    		return new Vector4f(160.0f/255.0f, 78.0f/255.0f, 14.0f/255.0f, 1.0f);
    	} else {
    		return new Vector4f(0.0f/255.0f, 0.0f/255.0f, 0.0f/255.0f, 1.0f);
    	}
    }
    
    public void printThirtyTwoLine() {
        System.out.println("--------------------------------");
    }
}
