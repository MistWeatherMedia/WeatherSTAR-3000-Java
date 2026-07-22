package WS3000.weatherstar3000;

import org.json.JSONException;
import org.json.JSONObject;

import util.Utilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.swing.*;
import java.awt.*;

import java.io.File;
import java.net.URISyntaxException;

public class Main {
	static String[] slidesList;
	static ArrayList<String> ldlList = new ArrayList<>(Arrays.asList("cityname", "condition", "temperature", "humidity", "pressure", "wind", "visibility", "precip"));
	static boolean settingsErrorThrown = false;
	static ArrayList<String> settingsErrorReasons = new ArrayList<>();
	static String popupErrors = "";
	static boolean loopSlides = false;
	static boolean greenScreen = false;
	static String key;
	static String mainIcao;
	static String mainLon;
	static String mainLat;
	static String todayMonth;
	static String todayDay;
	static int simWidth;
	static int simHeight;
	static boolean fullscreen = false;
	// static String mainName;
	static DataRunner dr;
	static CurrentConditions currentConditions;
	static HourlyObservations hourlyObservations;
	static RegionalConditions regionalConditions;
	static LocalForecast localForecast;
	static Almanac almanac;
	static RegionalForecast regionalForecast;
	static ExtendedForecast extendedForecast;
	static Outlook outlook;
	static TravelForecast travelForecast;
	static Bulletin bulletin;
	
	static ArrayList<String> nearNames = new ArrayList<>();
	static ArrayList<String> nearIcaos = new ArrayList<>();
	static ArrayList<String> regConNames = new ArrayList<>();
	static ArrayList<String> regConIcaos = new ArrayList<>();
	static ArrayList<String> regForNames = new ArrayList<>();
	static ArrayList<String> regForIcaos = new ArrayList<>();
	static JFrame settingsWindow;
	
	static String adCrawl;
	
	static String jarFolder = "";
	
	static JTextField mainLocName;
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println("WeatherSTAR 3000 Simulator Version 1.3.1");
		
		String userDir = System.getProperty("user.dir");
		if (userDir != null && !userDir.isEmpty()) {
		    jarFolder = userDir;
		} else {
		    // Definitive filesystem fallback if the OS environment fails to report
		    jarFolder = new File(".").getAbsolutePath();
		}
		
		settingsWindow = new JFrame("WeatherSTAR 3000 Settings");
		settingsWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		settingsWindow.setSize(540, 350);
		settingsWindow.setLocationRelativeTo(null);
		settingsWindow.setResizable(false);
		
		ImageIcon sfIcon = new ImageIcon(Main.class.getResource("/resources/images/icon.png"));
		settingsWindow.setIconImage(sfIcon.getImage());

		JPanel settingsFrame = new JPanel();
		settingsFrame.setPreferredSize(new Dimension(540, 840));
		settingsFrame.setLayout(new FlowLayout(FlowLayout.LEADING));

		// read 
		File loadDataFile = new File(jarFolder + File.separator + "loadData.txt");
		ArrayList<String> loadDataLines = new ArrayList<>();

		// info
		settingsFrame.add(new JLabel("***Maximum location name length: 14 characters, 19 for Regional Con***"));// 19
																														// if
																														// regional
																														// conditions

		// api key
		settingsFrame.add(new JLabel("api,weather.com IBM api key"));
		JTextField apiKeyText = new JTextField(20);
		settingsFrame.add(apiKeyText);

		// main location
		settingsFrame.add(new JLabel("Main Location ICAO Code and Name"));
		JTextField mainLocIcaoCode = new JTextField(4);
		settingsFrame.add(mainLocIcaoCode);
		mainLocName = new JTextField(14);
		settingsFrame.add(mainLocName);

		// spacer
		JPanel firstSpacer = new JPanel();
		firstSpacer.setPreferredSize(new Dimension(480, 10));
		settingsFrame.add(firstSpacer);

		// nearby locations
		settingsFrame.add(new JLabel("Nearby Location One ICAO Code and Name"));
		JTextField nearLocOneIcaoCode = new JTextField(4);
		settingsFrame.add(nearLocOneIcaoCode);
		JTextField nearLocOneName = new JTextField(14);
		settingsFrame.add(nearLocOneName);

		settingsFrame.add(new JLabel("Nearby Location Two ICAO Code and Name"));
		JTextField nearLocTwoIcaoCode = new JTextField(4);
		settingsFrame.add(nearLocTwoIcaoCode);
		JTextField nearLocTwoName = new JTextField(14);
		settingsFrame.add(nearLocTwoName);

		settingsFrame.add(new JLabel("Nearby Location Three ICAO Code and Name"));
		JTextField nearLocThreeIcaoCode = new JTextField(4);
		settingsFrame.add(nearLocThreeIcaoCode);
		JTextField nearLocThreeName = new JTextField(14);
		settingsFrame.add(nearLocThreeName);

		settingsFrame.add(new JLabel("Nearby Location Four ICAO Code and Name"));
		JTextField nearLocFourIcaoCode = new JTextField(4);
		settingsFrame.add(nearLocFourIcaoCode);
		JTextField nearLocFourName = new JTextField(14);
		settingsFrame.add(nearLocFourName);

		settingsFrame.add(new JLabel("Nearby Location Five ICAO Code and Name"));
		JTextField nearLocFiveIcaoCode = new JTextField(4);
		settingsFrame.add(nearLocFiveIcaoCode);
		JTextField nearLocFiveName = new JTextField(14);
		settingsFrame.add(nearLocFiveName);

		settingsFrame.add(new JLabel("Nearby Location Six ICAO Code and Name"));
		JTextField nearLocSixIcaoCode = new JTextField(4);
		settingsFrame.add(nearLocSixIcaoCode);
		JTextField nearLocSixName = new JTextField(14);
		settingsFrame.add(nearLocSixName);

		settingsFrame.add(new JLabel("Nearby Location Seven ICAO Code and Name"));
		JTextField nearLocSevenIcaoCode = new JTextField(4);
		settingsFrame.add(nearLocSevenIcaoCode);
		JTextField nearLocSevenName = new JTextField(14);
		settingsFrame.add(nearLocSevenName);

		// spacer
		JPanel secondSpacer = new JPanel();
		secondSpacer.setPreferredSize(new Dimension(480, 10));
		settingsFrame.add(secondSpacer);

		// nearby locations
		settingsFrame.add(new JLabel("Regional Conditions One ICAO Code and Name"));
		JTextField regConOneIcaoCode = new JTextField(4);
		settingsFrame.add(regConOneIcaoCode);
		JTextField regConOneName = new JTextField(14);
		settingsFrame.add(regConOneName);

		settingsFrame.add(new JLabel("Regional Conditions Two ICAO Code and Name"));
		JTextField regConTwoIcaoCode = new JTextField(4);
		settingsFrame.add(regConTwoIcaoCode);
		JTextField regConTwoName = new JTextField(14);
		settingsFrame.add(regConTwoName);

		settingsFrame.add(new JLabel("Regional Conditions Three ICAO Code and Name"));
		JTextField regConThreeIcaoCode = new JTextField(4);
		settingsFrame.add(regConThreeIcaoCode);
		JTextField regConThreeName = new JTextField(14);
		settingsFrame.add(regConThreeName);

		settingsFrame.add(new JLabel("Regional Conditions Four ICAO Code and Name"));
		JTextField regConFourIcaoCode = new JTextField(4);
		settingsFrame.add(regConFourIcaoCode);
		JTextField regConFourName = new JTextField(14);
		settingsFrame.add(regConFourName);

		settingsFrame.add(new JLabel("Regional Conditions Five ICAO Code and Name"));
		JTextField regConFiveIcaoCode = new JTextField(4);
		settingsFrame.add(regConFiveIcaoCode);
		JTextField regConFiveName = new JTextField(14);
		settingsFrame.add(regConFiveName);

		settingsFrame.add(new JLabel("Regional Conditions Six ICAO Code and Name"));
		JTextField regConSixIcaoCode = new JTextField(4);
		settingsFrame.add(regConSixIcaoCode);
		JTextField regConSixName = new JTextField(14);
		settingsFrame.add(regConSixName);

		settingsFrame.add(new JLabel("Regional Conditions Seven ICAO Code and Name"));
		JTextField regConSevenIcaoCode = new JTextField(4);
		settingsFrame.add(regConSevenIcaoCode);
		JTextField regConSevenName = new JTextField(14);
		settingsFrame.add(regConSevenName);

		// spacer
		JPanel thirdSpacer = new JPanel();
		thirdSpacer.setPreferredSize(new Dimension(480, 10));
		settingsFrame.add(thirdSpacer);

		// nearby locations
		settingsFrame.add(new JLabel("Regional Forecast One ICAO Code and Name"));
		JTextField regForOneIcaoCode = new JTextField(4);
		settingsFrame.add(regForOneIcaoCode);
		JTextField regForOneName = new JTextField(14);
		settingsFrame.add(regForOneName);

		settingsFrame.add(new JLabel("Regional Forecast Two ICAO Code and Name"));
		JTextField regForTwoIcaoCode = new JTextField(4);
		settingsFrame.add(regForTwoIcaoCode);
		JTextField regForTwoName = new JTextField(14);
		settingsFrame.add(regForTwoName);

		settingsFrame.add(new JLabel("Regional Forecast Three ICAO Code and Name"));
		JTextField regForThreeIcaoCode = new JTextField(4);
		settingsFrame.add(regForThreeIcaoCode);
		JTextField regForThreeName = new JTextField(14);
		settingsFrame.add(regForThreeName);

		settingsFrame.add(new JLabel("Regional Forecast Four ICAO Code and Name"));
		JTextField regForFourIcaoCode = new JTextField(4);
		settingsFrame.add(regForFourIcaoCode);
		JTextField regForFourName = new JTextField(14);
		settingsFrame.add(regForFourName);

		settingsFrame.add(new JLabel("Regional Forecast Five ICAO Code and Name"));
		JTextField regForFiveIcaoCode = new JTextField(4);
		settingsFrame.add(regForFiveIcaoCode);
		JTextField regForFiveName = new JTextField(14);
		settingsFrame.add(regForFiveName);

		settingsFrame.add(new JLabel("Regional Forecast Six ICAO Code and Name"));
		JTextField regForSixIcaoCode = new JTextField(4);
		settingsFrame.add(regForSixIcaoCode);
		JTextField regForSixName = new JTextField(14);
		settingsFrame.add(regForSixName);

		settingsFrame.add(new JLabel("Regional Forecast Seven ICAO Code and Name"));
		JTextField regForSevenIcaoCode = new JTextField(4);
		settingsFrame.add(regForSevenIcaoCode);
		JTextField regForSevenName = new JTextField(14);
		settingsFrame.add(regForSevenName);
		
		JPanel aSpacer = new JPanel();
		aSpacer.setPreferredSize(new Dimension(500, 10));
		settingsFrame.add(aSpacer);
		
		settingsFrame.add(new JLabel("Ad crawl message"));
		JTextField adCrawlInput = new JTextField(32);
		settingsFrame.add(adCrawlInput);
		
		// spacer
		JPanel fourthSpacer = new JPanel();
		fourthSpacer.setPreferredSize(new Dimension(500, 10));
		settingsFrame.add(fourthSpacer);
		
		settingsFrame.add(new JLabel("Width"));
		JTextField widthField = new JTextField(3);
		settingsFrame.add(widthField);
		widthField.setText("720");
		settingsFrame.add(new JLabel("Height"));
		JTextField heightField = new JTextField(3);
		heightField.setText("480");
		settingsFrame.add(heightField);
		
		settingsFrame.add(new JLabel("Flavor"));
		String[] flavorOptions = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q"};
		JComboBox flavorDropdown = new JComboBox(flavorOptions);
		flavorDropdown.setSelectedIndex(0);
		settingsFrame.add(flavorDropdown);
		
		settingsFrame.add(new JLabel("LDL Options"));
		String[] ldlOptions = {"Observations", "Ad Crawl", "Both"};
		JComboBox ldlDropdown = new JComboBox(ldlOptions);
		ldlDropdown.setSelectedIndex(0);
		settingsFrame.add(ldlDropdown);
		
		JPanel fifthSpacer = new JPanel();
		fifthSpacer.setPreferredSize(new Dimension(500, 10));
		settingsFrame.add(fifthSpacer);

		JCheckBox loopCheck = new JCheckBox("Loop");
		settingsFrame.add(loopCheck);
		
		JCheckBox gsCheck = new JCheckBox("Green Screen");
		settingsFrame.add(gsCheck);
		
		JCheckBox fsCheck = new JCheckBox("Fullscreen");
		settingsFrame.add(fsCheck);
		
		JPanel sixthSpacer = new JPanel();
		sixthSpacer.setPreferredSize(new Dimension(500, 10));
		settingsFrame.add(sixthSpacer);

		JButton doneButton = new JButton("Run");
		settingsFrame.add(doneButton);

		JButton saveButton = new JButton("Save Config");
		settingsFrame.add(saveButton);

		JButton loadButton = new JButton("Load Config");
		settingsFrame.add(loadButton);

		try (Scanner myReader = new Scanner(loadDataFile)) {
			while (myReader.hasNextLine()) {
				loadDataLines.add(myReader.nextLine());
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(settingsFrame, "Unable to read previous data.");
		}
		apiKeyText.setText(loadDataLines.get(0));
		mainLocIcaoCode.setText(loadDataLines.get(1));
		mainLocName.setText(loadDataLines.get(2));
		nearLocOneIcaoCode.setText(loadDataLines.get(3));
		nearLocOneName.setText(loadDataLines.get(4));
		nearLocTwoIcaoCode.setText(loadDataLines.get(5));
		nearLocTwoName.setText(loadDataLines.get(6));
		nearLocThreeIcaoCode.setText(loadDataLines.get(7));
		nearLocThreeName.setText(loadDataLines.get(8));
		nearLocFourIcaoCode.setText(loadDataLines.get(9));
		nearLocFourName.setText(loadDataLines.get(10));
		nearLocFiveIcaoCode.setText(loadDataLines.get(11));
		nearLocFiveName.setText(loadDataLines.get(12));
		nearLocSixIcaoCode.setText(loadDataLines.get(13));
		nearLocSixName.setText(loadDataLines.get(14));
		nearLocSevenIcaoCode.setText(loadDataLines.get(15));
		nearLocSevenName.setText(loadDataLines.get(16));
		regConOneIcaoCode.setText(loadDataLines.get(17));
		regConOneName.setText(loadDataLines.get(18));
		regConTwoIcaoCode.setText(loadDataLines.get(19));
		regConTwoName.setText(loadDataLines.get(20));
		regConThreeIcaoCode.setText(loadDataLines.get(21));
		regConThreeName.setText(loadDataLines.get(22));
		regConFourIcaoCode.setText(loadDataLines.get(23));
		regConFourName.setText(loadDataLines.get(24));
		regConFiveIcaoCode.setText(loadDataLines.get(25));
		regConFiveName.setText(loadDataLines.get(26));
		regConSixIcaoCode.setText(loadDataLines.get(27));
		regConSixName.setText(loadDataLines.get(28));
		regConSevenIcaoCode.setText(loadDataLines.get(29));
		regConSevenName.setText(loadDataLines.get(30));
		regForOneIcaoCode.setText(loadDataLines.get(31));
		regForOneName.setText(loadDataLines.get(32));
		regForTwoIcaoCode.setText(loadDataLines.get(33));
		regForTwoName.setText(loadDataLines.get(34));
		regForThreeIcaoCode.setText(loadDataLines.get(35));
		regForThreeName.setText(loadDataLines.get(36));
		regForFourIcaoCode.setText(loadDataLines.get(37));
		regForFourName.setText(loadDataLines.get(38));
		regForFiveIcaoCode.setText(loadDataLines.get(39));
		regForFiveName.setText(loadDataLines.get(40));
		regForSixIcaoCode.setText(loadDataLines.get(41));
		regForSixName.setText(loadDataLines.get(42));
		regForSevenIcaoCode.setText(loadDataLines.get(43));
		regForSevenName.setText(loadDataLines.get(44));

		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
	            chooser.setDialogTitle("Select a directory to save you config");
	            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	            
	            int result = chooser.showOpenDialog(null);
	            
	            if (result == JFileChooser.APPROVE_OPTION) {
	                File selectedFolder = chooser.getSelectedFile();
	                
	                String fileName = JOptionPane.showInputDialog(null, "Name the config file?");
	                
	                // 3. Create the JSON content
	                String jsonContent = String.format(
	                    "{\"starSettings\": {"
	                	
	                    + "\"api_key\": \"" + apiKeyText.getText() + "\","
	                   	+ "\"mainLocation\": {"
	                   	+ "\"icaoCode\": \"" + mainLocIcaoCode.getText() + "\","
	                   	+ "\"locName\": \"" + mainLocName.getText() + "\","
	                   	+ "},"
	                   	+ "\"nearbyLocations\": ["
	                   	+ "{\"icaoCode\": \"" + nearLocOneIcaoCode.getText() + "\", \"locName\": \"" + nearLocOneName.getText() + "\"},"
	                   	+ "{\"icaoCode\": \"" + nearLocTwoIcaoCode.getText() + "\", \"locName\": \"" + nearLocTwoName.getText() + "\"},"
	                   	+ "{\"icaoCode\": \"" + nearLocThreeIcaoCode.getText() + "\", \"locName\": \"" + nearLocThreeName.getText() + "\"},"
	                   	+ "{\"icaoCode\": \"" + nearLocFourIcaoCode.getText() + "\", \"locName\": \"" + nearLocFourName.getText() + "\"},"
	                   	+ "{\"icaoCode\": \"" + nearLocFiveIcaoCode.getText() + "\", \"locName\": \"" + nearLocFiveName.getText() + "\"},"
	                   	+ "{\"icaoCode\": \"" + nearLocSixIcaoCode.getText() + "\", \"locName\": \"" + nearLocSixName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + nearLocSevenIcaoCode.getText() + "\", \"locName\": \"" + nearLocSevenName.getText() + "\"},"
	                   	+ "],"
	                   	+ "\"regionalForecastLocations\": ["
	                    + "{\"icaoCode\": \"" + regForOneIcaoCode.getText() + "\", \"locName\": \"" + regForOneName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + regForTwoIcaoCode.getText() + "\", \"locName\": \"" + regForTwoName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + regForThreeIcaoCode.getText() + "\", \"locName\": \"" + regForThreeName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + regForFourIcaoCode.getText() + "\", \"locName\": \"" + regForFourName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + regForFiveIcaoCode.getText() + "\", \"locName\": \"" + regForFiveName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + regForSixIcaoCode.getText() + "\", \"locName\": \"" + regForSixName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + regForSevenIcaoCode.getText() + "\", \"locName\": \"" + regForSevenName.getText() + "\"},"
	                    + "],"
	                    + "\"regionalConditionsLocations\": ["
	                    + "{\"icaoCode\": \"" + regConOneIcaoCode.getText() + "\", \"locName\": \"" + regConOneName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + regConTwoIcaoCode.getText() + "\", \"locName\": \"" + regConTwoName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + regConThreeIcaoCode.getText() + "\", \"locName\": \"" + regConThreeName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + regConFourIcaoCode.getText() + "\", \"locName\": \"" + regConFourName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + regConFiveIcaoCode.getText() + "\", \"locName\": \"" + regConFiveName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + regConSixIcaoCode.getText() + "\", \"locName\": \"" + regConSixName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + regConSevenIcaoCode.getText() + "\", \"locName\": \"" + regConSevenName.getText() + "\"},"
	                    + "],"
	                    + "}}"
	                );

	                // 4. Save to the folder
	                if (fileName != null && !fileName.trim().isEmpty()) {
	                    if (!fileName.endsWith(".json")) fileName += ".json";
	                } else {
	                	fileName = "myConfig.json";
	                }
	                File fileToSave = new File(selectedFolder, fileName);
	                try (FileWriter writer = new FileWriter(fileToSave)) {
	                    writer.write(jsonContent);
	                    JOptionPane.showMessageDialog(null, "Saved to: " + fileToSave.getAbsolutePath());
	                } catch (IOException ex) {
	                    ex.printStackTrace();
	                    JOptionPane.showMessageDialog(settingsFrame, "Error saving config file.");
	                }
	            }
			}
		});
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser openFile = new JFileChooser();
				int returnval = openFile.showOpenDialog(null);
				File loadedFile = openFile.getSelectedFile();
				Path filePath;
				String absFilePath;
				String loadedExtension;
				if (returnval == JFileChooser.APPROVE_OPTION) {
					filePath = Paths.get(loadedFile.getAbsolutePath());
					absFilePath = loadedFile.getAbsolutePath();
					try {
						loadedExtension = Files.probeContentType(filePath);
					} catch (IOException ex) {
						loadedExtension = "error";
					}
					if (!loadedExtension.equals("application/json")) {
						JOptionPane.showMessageDialog(settingsFrame, "Please choose a json file.");
					} else {
						StringBuilder jsonText = new StringBuilder();
						try {
							BufferedReader br = new BufferedReader(new FileReader(absFilePath));
							String line;
							while ((line = br.readLine()) != null) {
								jsonText.append(line).append("\n");
							}
							br.close();
							JSONObject settingsObj = new JSONObject(jsonText.toString()).getJSONObject("starSettings");
							// api key
							apiKeyText.setText(settingsObj.getString("api_key"));

							// main location
							mainLocIcaoCode.setText(settingsObj.getJSONObject("mainLocation").getString("icaoCode"));
							mainLocName.setText(settingsObj.getJSONObject("mainLocation").getString("locName"));

							// nearby cities
							nearLocOneIcaoCode.setText(
									settingsObj.getJSONArray("nearbyLocations").getJSONObject(0).getString("icaoCode"));
							nearLocOneName.setText(
									settingsObj.getJSONArray("nearbyLocations").getJSONObject(0).getString("locName"));
							nearLocTwoIcaoCode.setText(
									settingsObj.getJSONArray("nearbyLocations").getJSONObject(1).getString("icaoCode"));
							nearLocTwoName.setText(
									settingsObj.getJSONArray("nearbyLocations").getJSONObject(1).getString("locName"));
							nearLocThreeIcaoCode.setText(
									settingsObj.getJSONArray("nearbyLocations").getJSONObject(2).getString("icaoCode"));
							nearLocThreeName.setText(
									settingsObj.getJSONArray("nearbyLocations").getJSONObject(2).getString("locName"));
							nearLocFourIcaoCode.setText(
									settingsObj.getJSONArray("nearbyLocations").getJSONObject(3).getString("icaoCode"));
							nearLocFourName.setText(
									settingsObj.getJSONArray("nearbyLocations").getJSONObject(3).getString("locName"));
							nearLocFiveIcaoCode.setText(
									settingsObj.getJSONArray("nearbyLocations").getJSONObject(4).getString("icaoCode"));
							nearLocFiveName.setText(
									settingsObj.getJSONArray("nearbyLocations").getJSONObject(4).getString("locName"));
							nearLocSixIcaoCode.setText(
									settingsObj.getJSONArray("nearbyLocations").getJSONObject(5).getString("icaoCode"));
							nearLocSixName.setText(
									settingsObj.getJSONArray("nearbyLocations").getJSONObject(5).getString("locName"));
							nearLocSevenIcaoCode.setText(
									settingsObj.getJSONArray("nearbyLocations").getJSONObject(6).getString("icaoCode"));
							nearLocSevenName.setText(
									settingsObj.getJSONArray("nearbyLocations").getJSONObject(6).getString("locName"));

							// regional conditions
							regConOneIcaoCode.setText(settingsObj.getJSONArray("regionalConditionsLocations")
									.getJSONObject(0).getString("icaoCode"));
							regConOneName.setText(settingsObj.getJSONArray("regionalConditionsLocations")
									.getJSONObject(0).getString("locName"));
							regConTwoIcaoCode.setText(settingsObj.getJSONArray("regionalConditionsLocations")
									.getJSONObject(1).getString("icaoCode"));
							regConTwoName.setText(settingsObj.getJSONArray("regionalConditionsLocations")
									.getJSONObject(1).getString("locName"));
							regConThreeIcaoCode.setText(settingsObj.getJSONArray("regionalConditionsLocations")
									.getJSONObject(2).getString("icaoCode"));
							regConThreeName.setText(settingsObj.getJSONArray("regionalConditionsLocations")
									.getJSONObject(2).getString("locName"));
							regConFourIcaoCode.setText(settingsObj.getJSONArray("regionalConditionsLocations")
									.getJSONObject(3).getString("icaoCode"));
							regConFourName.setText(settingsObj.getJSONArray("regionalConditionsLocations")
									.getJSONObject(3).getString("locName"));
							regConFiveIcaoCode.setText(settingsObj.getJSONArray("regionalConditionsLocations")
									.getJSONObject(4).getString("icaoCode"));
							regConFiveName.setText(settingsObj.getJSONArray("regionalConditionsLocations")
									.getJSONObject(4).getString("locName"));
							regConSixIcaoCode.setText(settingsObj.getJSONArray("regionalConditionsLocations")
									.getJSONObject(5).getString("icaoCode"));
							regConSixName.setText(settingsObj.getJSONArray("regionalConditionsLocations")
									.getJSONObject(5).getString("locName"));
							regConSevenIcaoCode.setText(settingsObj.getJSONArray("regionalConditionsLocations")
									.getJSONObject(6).getString("icaoCode"));
							regConSevenName.setText(settingsObj.getJSONArray("regionalConditionsLocations")
									.getJSONObject(6).getString("locName"));

							// regional forecast
							regForOneIcaoCode.setText(settingsObj.getJSONArray("regionalForecastLocations")
									.getJSONObject(0).getString("icaoCode"));
							regForOneName.setText(settingsObj.getJSONArray("regionalForecastLocations").getJSONObject(0)
									.getString("locName"));
							regForTwoIcaoCode.setText(settingsObj.getJSONArray("regionalForecastLocations")
									.getJSONObject(1).getString("icaoCode"));
							regForTwoName.setText(settingsObj.getJSONArray("regionalForecastLocations").getJSONObject(1)
									.getString("locName"));
							regForThreeIcaoCode.setText(settingsObj.getJSONArray("regionalForecastLocations")
									.getJSONObject(2).getString("icaoCode"));
							regForThreeName.setText(settingsObj.getJSONArray("regionalForecastLocations")
									.getJSONObject(2).getString("locName"));
							regForFourIcaoCode.setText(settingsObj.getJSONArray("regionalForecastLocations")
									.getJSONObject(3).getString("icaoCode"));
							regForFourName.setText(settingsObj.getJSONArray("regionalForecastLocations")
									.getJSONObject(3).getString("locName"));
							regForFiveIcaoCode.setText(settingsObj.getJSONArray("regionalForecastLocations")
									.getJSONObject(4).getString("icaoCode"));
							regForFiveName.setText(settingsObj.getJSONArray("regionalForecastLocations")
									.getJSONObject(4).getString("locName"));
							regForSixIcaoCode.setText(settingsObj.getJSONArray("regionalForecastLocations")
									.getJSONObject(5).getString("icaoCode"));
							regForSixName.setText(settingsObj.getJSONArray("regionalForecastLocations").getJSONObject(5)
									.getString("locName"));
							regForSevenIcaoCode.setText(settingsObj.getJSONArray("regionalForecastLocations")
									.getJSONObject(6).getString("icaoCode"));
							regForSevenName.setText(settingsObj.getJSONArray("regionalForecastLocations")
									.getJSONObject(6).getString("locName"));

							// after all of that, change the base config json that loads on start
							String dataFilePath = jarFolder + File.separator + "loadData.txt";
							String content = apiKeyText.getText() + "\n" + mainLocIcaoCode.getText() + "\n"
									+ mainLocName.getText() + "\n" + nearLocOneIcaoCode.getText() + "\n"
									+ nearLocOneName.getText() + "\n" + nearLocTwoIcaoCode.getText() + "\n"
									+ nearLocTwoName.getText() + "\n" + nearLocThreeIcaoCode.getText() + "\n"
									+ nearLocThreeName.getText() + "\n" + nearLocFourIcaoCode.getText() + "\n"
									+ nearLocFourName.getText() + "\n" + nearLocFiveIcaoCode.getText() + "\n"
									+ nearLocFiveName.getText() + "\n" + nearLocSixIcaoCode.getText() + "\n"
									+ nearLocSixName.getText() + "\n" + nearLocSevenIcaoCode.getText() + "\n"
									+ nearLocSevenName.getText() + "\n" + regConOneIcaoCode.getText() + "\n"
									+ regConOneName.getText() + "\n" + regConTwoIcaoCode.getText() + "\n"
									+ regConTwoName.getText() + "\n" + regConThreeIcaoCode.getText() + "\n"
									+ regConThreeName.getText() + "\n" + regConFourIcaoCode.getText() + "\n"
									+ regConFourName.getText() + "\n" + regConFiveIcaoCode.getText() + "\n"
									+ regConFiveName.getText() + "\n" + regConSixIcaoCode.getText() + "\n"
									+ regConSixName.getText() + "\n" + regConSevenIcaoCode.getText() + "\n"
									+ regConSevenName.getText() + "\n" + regForOneIcaoCode.getText() + "\n"
									+ regForOneName.getText() + "\n" + regForTwoIcaoCode.getText() + "\n"
									+ regForTwoName.getText() + "\n" + regForThreeIcaoCode.getText() + "\n"
									+ regForThreeName.getText() + "\n" + regForFourIcaoCode.getText() + "\n"
									+ regForFourName.getText() + "\n" + regForFiveIcaoCode.getText() + "\n"
									+ regForFiveName.getText() + "\n" + regForSixIcaoCode.getText() + "\n"
									+ regForSixName.getText() + "\n" + regForSevenIcaoCode.getText() + "\n"
									+ regForSevenName.getText() + "\n";
							try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFilePath))) {
								writer.write(content);
								writer.close();
							} catch (IOException y) {
								y.printStackTrace();
								JOptionPane.showMessageDialog(settingsFrame,
										"Error saving loaded config data to program. Config will not load on next startup.");
							}
						} catch (JSONException ex) {
							JOptionPane.showMessageDialog(settingsFrame,
									"Unable to read loaded configuration file, please choose a valid file.");
						} catch (IOException ex) {
							throw new RuntimeException(ex);
						}
					}
				}
			}
		});
		
		doneButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loopSlides = loopCheck.isSelected();
				greenScreen = gsCheck.isSelected();
				fullscreen = fsCheck.isSelected();
				key = apiKeyText.getText();
				mainIcao = mainLocIcaoCode.getText();
				/*
				if (!checkIcaoCode(mainIcao, key).equals("noerror")) {
					settingsErrorThrown = true;
					settingsErrorReasons.add("\nMain Location Error: " + checkIcaoCode(mainIcao, key));
				}
				*/
				nearIcaos.add(nearLocOneIcaoCode.getText());
				nearIcaos.add(nearLocTwoIcaoCode.getText());
				nearIcaos.add(nearLocThreeIcaoCode.getText());
				nearIcaos.add(nearLocFourIcaoCode.getText());
				nearIcaos.add(nearLocFiveIcaoCode.getText());
				nearIcaos.add(nearLocSixIcaoCode.getText());
				nearIcaos.add(nearLocSevenIcaoCode.getText());
				
				
				nearNames.add(nearLocOneName.getText());
				nearNames.add(nearLocTwoName.getText());
				nearNames.add(nearLocThreeName.getText());
				nearNames.add(nearLocFourName.getText());
				nearNames.add(nearLocFiveName.getText());
				nearNames.add(nearLocSixName.getText());
				nearNames.add(nearLocSevenName.getText());
				/*
				for (int i = 0; i < nearIcaos.size(); i++) {
					if (!checkIcaoCode(nearIcaos.get(i), key).equals("noerror")) {
						settingsErrorThrown = true;
						settingsErrorReasons
								.add("\nNear Location " + i + " Error: " + checkIcaoCode(nearIcaos.get(i), key));
					}
				}
				*/
				regConIcaos.add(regConOneIcaoCode.getText());
				regConIcaos.add(regConTwoIcaoCode.getText());
				regConIcaos.add(regConThreeIcaoCode.getText());
				regConIcaos.add(regConFourIcaoCode.getText());
				regConIcaos.add(regConFiveIcaoCode.getText());
				regConIcaos.add(regConSixIcaoCode.getText());
				regConIcaos.add(regConSevenIcaoCode.getText());
				
				regConNames.add(regConOneName.getText());
				regConNames.add(regConTwoName.getText());
				regConNames.add(regConThreeName.getText());
				regConNames.add(regConFourName.getText());
				regConNames.add(regConFiveName.getText());
				regConNames.add(regConSixName.getText());
				regConNames.add(regConSevenName.getText());
				/*
				for (int i = 0; i < regConIcaos.size(); i++) {
					if (!checkIcaoCode(regConIcaos.get(i), key).equals("noerror")) {
						settingsErrorThrown = true;
						settingsErrorReasons.add(
								"\nRegional Conditions Loc " + i + " Error: " + checkIcaoCode(regConIcaos.get(i), key));
					}
				}*/

				regForIcaos.add(regForOneIcaoCode.getText());
				regForIcaos.add(regForTwoIcaoCode.getText());
				regForIcaos.add(regForThreeIcaoCode.getText());
				regForIcaos.add(regForFourIcaoCode.getText());
				regForIcaos.add(regForFiveIcaoCode.getText());
				regForIcaos.add(regForSixIcaoCode.getText());
				regForIcaos.add(regForSevenIcaoCode.getText());
				
				regForNames.add(regForOneName.getText());
				regForNames.add(regForTwoName.getText());
				regForNames.add(regForThreeName.getText());
				regForNames.add(regForFourName.getText());
				regForNames.add(regForFiveName.getText());
				regForNames.add(regForSixName.getText());
				regForNames.add(regForSevenName.getText());
				
				adCrawl = adCrawlInput.getText();
				/*
				for (int i = 0; i < regForIcaos.size(); i++) {
					if (!checkIcaoCode(regForIcaos.get(i), key).equals("noerror")) {
						settingsErrorThrown = true;
						settingsErrorReasons
								.add("\nRegional Forecast Loc " + i + " Error: " + checkIcaoCode(regForIcaos.get(i), key));
					}
				}
				*/
					WindowRunner wr = new WindowRunner();
					//dr = new DataRunner();
					/*currentConditions = new CurrentConditions();
					hourlyObservations = new HourlyObservations();
					regionalConditions = new RegionalConditions();
					localForecast = new LocalForecast();
					almanac = new Almanac();
					regionalForecast = new RegionalForecast();
					extendedForecast = new ExtendedForecast();*/
					
				/*if (settingsErrorThrown) {
					for (int i = 0; i < settingsErrorReasons.size(); i++) {
						popupErrors = popupErrors + settingsErrorReasons.get(i);
					}
					JOptionPane.showMessageDialog(settingsFrame, "The following errors have occured: " + popupErrors);
					mainLat = "error";
					mainLon = "error";
					
					todayMonth = "error";
					todayDay = "error";
				*///} else {
					// after checking for icao code and key errors, get data
					
				//}
					// get cc data
					String ldltype = (String) ldlDropdown.getSelectedItem();
					switch (ldltype) {
					case "Observations":
						SlidesRunner.ccType = "obs";
						break;
					case "Ad Crawl":
						SlidesRunner.ccType = "crawl";
						break;
					case "Both":
						SlidesRunner.ccType = "both";
						break;
					default:
						System.out.println("Invalid LDL Option");
						break;
					}
					
					String selectedFlavor = (String) flavorDropdown.getSelectedItem();
					switch (selectedFlavor) {
					case "A":
						slidesList = Utilities.flavorA;
						break;
					case "B":
						slidesList = Utilities.flavorB;
						break;
					case "C":
						slidesList = Utilities.flavorC;
						break;
					case "D":
						slidesList = Utilities.flavorD;
						break;
					case "E":
						slidesList = Utilities.flavorE;
						break;
					case "F":
						slidesList = Utilities.flavorF;
						break;
					case "G":
						slidesList = Utilities.flavorG;
						break;
					case "H":
						slidesList = Utilities.flavorH;
						break;
					case "I":
						slidesList = Utilities.flavorI;
						break;
					case "J":
						slidesList = Utilities.flavorJ;
						break;
					case "K":
						slidesList = Utilities.flavorK;
						break;
					case "L":
						slidesList = Utilities.flavorL;
						break;
					case "M":
						slidesList = Utilities.flavorM;
						break;
					case "N":
						slidesList = Utilities.flavorN;
						break;
					case "O":
						slidesList = Utilities.flavorO;
						break;
					case "P":
						slidesList = Utilities.flavorP;
						break;
					case "Q":
						slidesList = Utilities.flavorQ;
						break;
					default:
						System.out.println("Invalid flavor");
						break;
					}
					//slidesList = Utilities.testFlavor;
					
					simWidth = Integer.parseInt(widthField.getText());
					simHeight = Integer.parseInt(heightField.getText());
					
					mainLat = icaoToLat(mainIcao, key);
					mainLon = icaoToLon(mainIcao, key);
					
					almanac = new Almanac();
					todayMonth = almanac.getMonth(mainIcao, key);
					todayDay = almanac.getDayDate(mainIcao, key);
					
					
					/*currentConditions.start();
					hourlyObservations.start();
					regionalConditions.start();
					localForecast.start();
					almanac.start();
					regionalForecast.start();
					extendedForecast.start();
					
					try {
						currentConditions.join();
						hourlyObservations.join();
						regionalConditions.join();
						localForecast.join();
						almanac.join();
						regionalForecast.join();
						extendedForecast.join();
						//wr.join();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}*/
					//dr.run();
					wr.start();
					// flavor manager
					

					// after all data, checks, and flavor management
					/*
					settingsWindow.setVisible(false);
					
					//BlueWindow blueWindow = new BlueWindow();
					//LDLRunner lr = new LDLRunner();
			        //lr.start();
			        //BlueRunner br = new BlueRunner();
					//br.start();
					*/
					
					//settingsWindow.setVisible(false);
					//Window window = Window.get();
					//window.runWindow();
				
				settingsErrorThrown = false;
				settingsErrorReasons.clear();
				popupErrors = "";
			}
		});

		JScrollPane settingsScrollFrame = new JScrollPane(settingsFrame);
		settingsScrollFrame.getVerticalScrollBar().setUnitIncrement(16);

		settingsScrollFrame.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		settingsScrollFrame.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		settingsWindow.add(settingsScrollFrame);
		settingsWindow.setVisible(true);
	}

	static String checkIcaoCode(String code, String key) {
		HttpClient Client = HttpClient.newBuilder().build();
		HttpRequest Request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.weather.com/v3/location/point?icaoCode=" + code
						+ "&language=en-US&format=json&apiKey=" + key))
				.GET() // Or .POST(HttpRequest.BodyPublishers.ofString("your_json_body"))
				.header("Accept", "application/json").build();
		try {
			HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
			if (Response.statusCode() != 200) {
				if (Response.statusCode() == 401) {
					return "API Key error";
				} else {
					return "Invalid ICAO Code";
				}
			}

			return "noerror";
		} catch (Exception e) {
			e.printStackTrace();
			return "Server Error";
		}
	}

	static String icaoToLat(String code, String key) {
		HttpClient Client = HttpClient.newBuilder().build();
		HttpRequest Request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.weather.com/v3/location/point?icaoCode=" + code
						+ "&language=en-US&format=json&apiKey=" + key))
				.GET() // Or .POST(HttpRequest.BodyPublishers.ofString("your_json_body"))
				.header("Accept", "application/json").build();
		try {
			HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
			String ResponseBody = Response.body();
			JSONObject JSONResponse = new JSONObject(ResponseBody);
			JSONObject locationObject = JSONResponse.getJSONObject("location");

			return Float.toString(locationObject.getFloat("latitude"));
		} catch (Exception e) {
			e.printStackTrace();
			return "Server Error";
		}
	}

	static String icaoToLon(String code, String key) {
		HttpClient Client = HttpClient.newBuilder().build();
		HttpRequest Request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.weather.com/v3/location/point?icaoCode=" + code
						+ "&language=en-US&format=json&apiKey=" + key))
				.GET() // Or .POST(HttpRequest.BodyPublishers.ofString("your_json_body"))
				.header("Accept", "application/json").build();
		try {
			HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
			String ResponseBody = Response.body();
			JSONObject JSONResponse = new JSONObject(ResponseBody);
			JSONObject locationObject = JSONResponse.getJSONObject("location");

			return Float.toString(locationObject.getFloat("longitude"));
		} catch (Exception e) {
			e.printStackTrace();
			return "Server Error";
		}
	}
}
