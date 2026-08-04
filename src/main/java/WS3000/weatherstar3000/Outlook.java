package WS3000.weatherstar3000;

//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import util.Utilities;

//import org.json.JSONObject;

public class Outlook extends Thread {
	static Utilities utl = new Utilities();
	String month;
	ArrayList<String> outlook = new ArrayList<>();
	String getMonth() {
		LocalDate cdate = LocalDate.now();
        Month cmonth = cdate.getMonth();
        
        return cmonth.name().toUpperCase();
    }
	ArrayList<String> getOutlook() {
		ArrayList<String> outlook = new ArrayList<>();
		outlook.add("NO");
		outlook.add("REPORT");
		return outlook;
	}
	public void run() {
		Main.outlook.month = getMonth();
		Main.outlook.outlook = getOutlook();
	}
}
