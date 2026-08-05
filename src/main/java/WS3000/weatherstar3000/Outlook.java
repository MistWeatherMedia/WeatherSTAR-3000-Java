package WS3000.weatherstar3000;

//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.time.Month;
import java.util.ArrayList;

import util.Utilities;

//import org.json.JSONObject;

public class Outlook extends Thread {
	static Utilities utl = new Utilities();
	String month;
	ArrayList<String> outlook = new ArrayList<>();
	String getMonth() {
		try {
			ZonedDateTime cdate = ZonedDateTime.now(Main.timeZone);
        Month cmonth = cdate.getMonth();
        
        return cmonth.name().toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
    }
	ArrayList<String> getOutlook() {
		ArrayList<String> outlook = new ArrayList<>();
		try {
		outlook.add("NO");
		outlook.add("REPORT");
		return outlook;
		} catch (Exception e) {
			e.printStackTrace();
			outlook.add("NO");
			outlook.add("REPORT");
			return outlook;
		}
	}
	public void run() {
		Main.outlook.month = getMonth();
		Main.outlook.outlook = getOutlook();
	}
}
