package WS3000.weatherstar3000;

public class DataRunner extends Thread {
	public void run() {
		if (Main.ztOption.equals("Location Time")) {
			Main.timeZone = Main.getTimeZone(Main.mainIcao);
		}
		//System.out.println("Data collection");
		Main.currentConditions = new CurrentConditions();
		Main.hourlyObservations = new HourlyObservations();
		Main.regionalConditions = new RegionalConditions();
		Main.localForecast = new LocalForecast();
		Main.almanac = new Almanac();
		Main.regionalForecast = new RegionalForecast();
		Main.extendedForecast = new ExtendedForecast();
		Main.outlook = new Outlook();
		Main.travelForecast = new TravelForecast();
		Main.bulletin = new Bulletin();
		Main.tides = new Tides();
		
		Main.currentConditions.start();
		Main.hourlyObservations.start();
		Main.regionalConditions.start();
		Main.localForecast.start();
		Main.almanac.start();
		Main.regionalForecast.start();
		Main.extendedForecast.start();
		Main.outlook.start();
		Main.travelForecast.start();
		Main.bulletin.start();
		Main.tides.start();
		
		try {
			Main.currentConditions.join();
			Main.hourlyObservations.join();
			Main.regionalConditions.join();
			Main.localForecast.join();
			Main.almanac.join();
			Main.regionalForecast.join();
			Main.extendedForecast.join();
			Main.outlook.join();
			Main.travelForecast.join();
			Main.bulletin.join();
			Main.tides.join();
			LoadingScreen.dataFinish = true;
			SlidesRunner.collectingData = false;
			//ystem.out.println("data finished, destroying thread");
			//wr.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
