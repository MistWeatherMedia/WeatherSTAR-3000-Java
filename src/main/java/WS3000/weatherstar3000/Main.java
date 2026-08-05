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
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
	static String selectedFlavor;
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
	static Tides tides;
	
	static String ztOption;
	static ZoneId timeZone;
	
	static JFrame settingsWindow;
	
	static ArrayList<String> nearNames = new ArrayList<>();
	static ArrayList<String> nearIcaos = new ArrayList<>();
	static ArrayList<String> regConNames = new ArrayList<>();
	static ArrayList<String> regConIcaos = new ArrayList<>();
	static ArrayList<String> regForNames = new ArrayList<>();
	static ArrayList<String> regForIcaos = new ArrayList<>();
	static ArrayList<String> tideIDs = new ArrayList<>();
	static ArrayList<String> tideNames = new ArrayList<>();
	
	static String adCrawl;
	
	static boolean tidesEnabled = false;
	
	static String jarFolder = "";
	
	static JTextField mainLocName;
	
	public static void main(String[] args) throws InterruptedException {
		/*
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			
		} catch (ClassNotFoundException e) {
			
		} catch (InstantiationException e) {
			
		} catch (IllegalAccessException e) {
			
		}
		*/
		//things to add:
		//airport conditions
		//air quality
		//optimized LookAndFeels?
		System.out.println("WeatherSTAR 3000 Simulator Version 1.6.0");
		
		String userDir = System.getProperty("user.dir");
		if (userDir != null && !userDir.isEmpty()) {
		    jarFolder = userDir;
		} else {
		    jarFolder = new File(".").getAbsolutePath();
		}
		
		// read 
		File loadDataFile = new File(jarFolder + File.separator + "loadData.txt");
		ArrayList<String> loadDataLines = new ArrayList<>();
		
		settingsWindow = new JFrame("WeatherSTAR 3000 Settings");
		settingsWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		settingsWindow.setSize(460, 350);
		settingsWindow.setLocationRelativeTo(null);
		settingsWindow.setResizable(false);
		
		ImageIcon sfIcon = new ImageIcon(Main.class.getResource("/resources/images/icon.png"));
		settingsWindow.setIconImage(sfIcon.getImage());
		
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
		
		JPanel aPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		aPanel.setBorder(BorderFactory.createTitledBorder("IBM Api Key From api.weather.com"));
		
		JTextField apiKeyText = new JTextField(25);
		aPanel.add(apiKeyText);
		
		aPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, aPanel.getPreferredSize().height));
		
		contentPanel.add(aPanel);
		
		JPanel mlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		mlPanel.setBorder(BorderFactory.createTitledBorder("Main Location"));
		
		mlPanel.add(new JLabel("ICAO Code"));
		JTextField mainLocIcaoCode = new JTextField(5);
		mlPanel.add(mainLocIcaoCode);
		mlPanel.add(new JLabel("Location Name"));
		mainLocName = new JTextField(15);
		mlPanel.add(mainLocName);
		
		mlPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, mlPanel.getPreferredSize().height));
	
		contentPanel.add(mlPanel);
		
		JPanel nbPanel = new JPanel(new GridLayout(0, 1, 0, 0));
		nbPanel.setBorder(BorderFactory.createTitledBorder("Nearby Locations"));
		
		JPanel nb1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		nb1Panel.add(new JLabel("ICAO Code"));
		JTextField nearLocOneIcaoCode = new JTextField(5);
		nb1Panel.add(nearLocOneIcaoCode);
		nb1Panel.add(new JLabel("Location Name"));
		JTextField nearLocOneName = new JTextField(15);
		nb1Panel.add(nearLocOneName);
		
		nbPanel.add(nb1Panel);
		
		JPanel nb2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		nb2Panel.add(new JLabel("ICAO Code"));
		JTextField nearLocTwoIcaoCode = new JTextField(5);
		nb2Panel.add(nearLocTwoIcaoCode);
		nb2Panel.add(new JLabel("Location Name"));
		JTextField nearLocTwoName = new JTextField(15);
		nb2Panel.add(nearLocTwoName);
		
		nbPanel.add(nb2Panel);
		
		JPanel nb3Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		nb3Panel.add(new JLabel("ICAO Code"));
		JTextField nearLocThreeIcaoCode = new JTextField(5);
		nb3Panel.add(nearLocThreeIcaoCode);
		nb3Panel.add(new JLabel("Location Name"));
		JTextField nearLocThreeName = new JTextField(15);
		nb3Panel.add(nearLocThreeName);
		
		nbPanel.add(nb3Panel);
		
		JPanel nb4Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		nb4Panel.add(new JLabel("ICAO Code"));
		JTextField nearLocFourIcaoCode = new JTextField(5);
		nb4Panel.add(nearLocFourIcaoCode);
		nb4Panel.add(new JLabel("Location Name"));
		JTextField nearLocFourName = new JTextField(15);
		nb4Panel.add(nearLocFourName);
		
		nbPanel.add(nb4Panel);
		
		JPanel nb5Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		nb5Panel.add(new JLabel("ICAO Code"));
		JTextField nearLocFiveIcaoCode = new JTextField(5);
		nb5Panel.add(nearLocFiveIcaoCode);
		nb5Panel.add(new JLabel("Location Name"));
		JTextField nearLocFiveName = new JTextField(15);
		nb5Panel.add(nearLocFiveName);
		
		nbPanel.add(nb5Panel);
		
		JPanel nb6Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		nb6Panel.add(new JLabel("ICAO Code"));
		JTextField nearLocSixIcaoCode = new JTextField(5);
		nb6Panel.add(nearLocSixIcaoCode);
		nb6Panel.add(new JLabel("Location Name"));
		JTextField nearLocSixName = new JTextField(15);
		nb6Panel.add(nearLocSixName);
		
		nbPanel.add(nb6Panel);
		
		JPanel nb7Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		nb7Panel.add(new JLabel("ICAO Code"));
		JTextField nearLocSevenIcaoCode = new JTextField(5);
		nb7Panel.add(nearLocSevenIcaoCode);
		nb7Panel.add(new JLabel("Location Name"));
		JTextField nearLocSevenName = new JTextField(15);
		nb7Panel.add(nearLocSevenName);
		
		nbPanel.add(nb7Panel);
		
		nbPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, nbPanel.getPreferredSize().height));
		
		contentPanel.add(nbPanel);
		
		JPanel rcPanel = new JPanel(new GridLayout(0, 1, 0, 0));
		rcPanel.setBorder(BorderFactory.createTitledBorder("Regional Conditions Locations"));
		
		JPanel rc1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		rc1Panel.add(new JLabel("ICAO Code"));
		JTextField regConOneIcaoCode = new JTextField(5);
		rc1Panel.add(regConOneIcaoCode);
		rc1Panel.add(new JLabel("Location Name"));
		JTextField regConOneName = new JTextField(15);
		rc1Panel.add(regConOneName);
		
		rcPanel.add(rc1Panel);
		
		JPanel rc2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		rc2Panel.add(new JLabel("ICAO Code"));
		JTextField regConTwoIcaoCode = new JTextField(5);
		rc2Panel.add(regConTwoIcaoCode);
		rc2Panel.add(new JLabel("Location Name"));
		JTextField regConTwoName = new JTextField(15);
		rc2Panel.add(regConTwoName);
		
		rcPanel.add(rc2Panel);
		
		JPanel rc3Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		rc3Panel.add(new JLabel("ICAO Code"));
		JTextField regConThreeIcaoCode = new JTextField(5);
		rc3Panel.add(regConThreeIcaoCode);
		rc3Panel.add(new JLabel("Location Name"));
		JTextField regConThreeName = new JTextField(15);
		rc3Panel.add(regConThreeName);
		
		rcPanel.add(rc3Panel);
		
		JPanel rc4Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		rc4Panel.add(new JLabel("ICAO Code"));
		JTextField regConFourIcaoCode = new JTextField(5);
		rc4Panel.add(regConFourIcaoCode);
		rc4Panel.add(new JLabel("Location Name"));
		JTextField regConFourName = new JTextField(15);
		rc4Panel.add(regConFourName);
		
		rcPanel.add(rc4Panel);
		
		JPanel rc5Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		rc5Panel.add(new JLabel("ICAO Code"));
		JTextField regConFiveIcaoCode = new JTextField(5);
		rc5Panel.add(regConFiveIcaoCode);
		rc5Panel.add(new JLabel("Location Name"));
		JTextField regConFiveName = new JTextField(15);
		rc5Panel.add(regConFiveName);
		
		rcPanel.add(rc5Panel);
		
		JPanel rc6Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		rc6Panel.add(new JLabel("ICAO Code"));
		JTextField regConSixIcaoCode = new JTextField(5);
		rc6Panel.add(regConSixIcaoCode);
		rc6Panel.add(new JLabel("Location Name"));
		JTextField regConSixName = new JTextField(15);
		rc6Panel.add(regConSixName);
		
		rcPanel.add(rc6Panel);
		
		JPanel rc7Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		rc7Panel.add(new JLabel("ICAO Code"));
		JTextField regConSevenIcaoCode = new JTextField(5);
		rc7Panel.add(regConSevenIcaoCode);
		rc7Panel.add(new JLabel("Location Name"));
		JTextField regConSevenName = new JTextField(15);
		rc7Panel.add(regConSevenName);
		
		rcPanel.add(rc7Panel);
		
		rcPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, rcPanel.getPreferredSize().height));
		
		contentPanel.add(rcPanel);
		
		JPanel rfPanel = new JPanel(new GridLayout(0, 1, 0, 0));
		rfPanel.setBorder(BorderFactory.createTitledBorder("Regional Forecast Locations"));
		
		JPanel rf1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		rf1Panel.add(new JLabel("ICAO Code"));
		JTextField regForOneIcaoCode = new JTextField(5);
		rf1Panel.add(regForOneIcaoCode);
		rf1Panel.add(new JLabel("Location Name"));
		JTextField regForOneName = new JTextField(15);
		rf1Panel.add(regForOneName);
		
		rfPanel.add(rf1Panel);
		
		JPanel rf2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		rf2Panel.add(new JLabel("ICAO Code"));
		JTextField regForTwoIcaoCode = new JTextField(5);
		rf2Panel.add(regForTwoIcaoCode);
		rf2Panel.add(new JLabel("Location Name"));
		JTextField regForTwoName = new JTextField(15);
		rf2Panel.add(regForTwoName);
		
		rfPanel.add(rf2Panel);
		
		JPanel rf3Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		rf3Panel.add(new JLabel("ICAO Code"));
		JTextField regForThreeIcaoCode = new JTextField(5);
		rf3Panel.add(regForThreeIcaoCode);
		rf3Panel.add(new JLabel("Location Name"));
		JTextField regForThreeName = new JTextField(15);
		rf3Panel.add(regForThreeName);
		
		rfPanel.add(rf3Panel);
		
		JPanel rf4Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		rf4Panel.add(new JLabel("ICAO Code"));
		JTextField regForFourIcaoCode = new JTextField(5);
		rf4Panel.add(regForFourIcaoCode);
		rf4Panel.add(new JLabel("Location Name"));
		JTextField regForFourName = new JTextField(15);
		rf4Panel.add(regForFourName);
		
		rfPanel.add(rf4Panel);
		
		JPanel rf5Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		rf5Panel.add(new JLabel("ICAO Code"));
		JTextField regForFiveIcaoCode = new JTextField(5);
		rf5Panel.add(regForFiveIcaoCode);
		rf5Panel.add(new JLabel("Location Name"));
		JTextField regForFiveName = new JTextField(15);
		rf5Panel.add(regForFiveName);
		
		rfPanel.add(rf5Panel);
		
		JPanel rf6Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		rf6Panel.add(new JLabel("ICAO Code"));
		JTextField regForSixIcaoCode = new JTextField(5);
		rf6Panel.add(regForSixIcaoCode);
		rf6Panel.add(new JLabel("Location Name"));
		JTextField regForSixName = new JTextField(15);
		rf6Panel.add(regForSixName);
		
		rfPanel.add(rf6Panel);
		
		JPanel rf7Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		rf7Panel.add(new JLabel("ICAO Code"));
		JTextField regForSevenIcaoCode = new JTextField(5);
		rf7Panel.add(regForSevenIcaoCode);
		rf7Panel.add(new JLabel("Location Name"));
		JTextField regForSevenName = new JTextField(15);
		rf7Panel.add(regForSevenName);
		
		rfPanel.add(rf7Panel);
		
		rfPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, rfPanel.getPreferredSize().height));
		
		contentPanel.add(rfPanel);
		
		//tides
		JPanel tPanel = new JPanel(new GridLayout(0, 1, 0, 0));
		tPanel.setBorder(BorderFactory.createTitledBorder("Tides Locations"));
		
		JPanel t1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		t1Panel.add(new JLabel("Station ID"));
		JTextField tideOneID = new JTextField(5);
		t1Panel.add(tideOneID);
		t1Panel.add(new JLabel("Location Name"));
		JTextField tideOneName = new JTextField(15);
		t1Panel.add(tideOneName);
		
		tPanel.add(t1Panel);
		
		JPanel t2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		t2Panel.add(new JLabel("Station ID"));
		JTextField tideTwoID = new JTextField(5);
		t2Panel.add(tideTwoID);
		t2Panel.add(new JLabel("Location Name"));
		JTextField tideTwoName = new JTextField(15);
		t2Panel.add(tideTwoName);
		
		tPanel.add(t2Panel);
		
		tPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, tPanel.getPreferredSize().height));
		
		contentPanel.add(tPanel);
		
		//ad crawl
		JPanel cPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		cPanel.setBorder(BorderFactory.createTitledBorder("Ad Crawl"));
		
		JTextField adCrawlInput = new JTextField(35);
		cPanel.add(adCrawlInput);
		
		cPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, cPanel.getPreferredSize().height));
		
		contentPanel.add(cPanel);
		
		JPanel rPanel = new JPanel(new GridLayout(0, 1, 0, 0));
		rPanel.setBorder(BorderFactory.createTitledBorder("Run Simulation"));
		
		JPanel r1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		r1Panel.add(new JLabel("Width"));
		JTextField widthField = new JTextField(3);
		r1Panel.add(widthField);
		widthField.setText("720");
		r1Panel.add(new JLabel("Height"));
		JTextField heightField = new JTextField(3);
		heightField.setText("480");
		r1Panel.add(heightField);
		
		r1Panel.add(new JLabel("Flavor"));
		String[] flavorOptions = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q"};
		JComboBox flavorDropdown = new JComboBox(flavorOptions);
		flavorDropdown.setSelectedIndex(0);
		r1Panel.add(flavorDropdown);
		
		rPanel.add(r1Panel);
		
		JPanel r2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		r2Panel.add(new JLabel("LDL Options"));
		String[] ldlOptions = {"Observations", "Ad Crawl", "Both"};
		JComboBox ldlDropdown = new JComboBox(ldlOptions);
		ldlDropdown.setSelectedIndex(0);
		r2Panel.add(ldlDropdown);
		
		r2Panel.add(new JLabel("Time Zone"));
		String[] timeZoneOptions = {"Location Time", "System Time", "US/Eastern", "US/Central", "US/Mountain", "US/Arizona", "US/Pacific", "US/Alaska", "US/Aleutian", "US/Hawaii", "US/Samoa"};
		JComboBox timeZoneDropdown = new JComboBox(timeZoneOptions);
		timeZoneDropdown.setSelectedIndex(0);
		r2Panel.add(timeZoneDropdown);
		
		rPanel.add(r2Panel);
		
		JPanel r3Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JCheckBox tidesCheck = new JCheckBox("Tides");
		r3Panel.add(tidesCheck);

		JCheckBox loopCheck = new JCheckBox("Loop");
		r3Panel.add(loopCheck);
		
		JCheckBox gsCheck = new JCheckBox("Green Screen");
		r3Panel.add(gsCheck);
		
		JCheckBox fsCheck = new JCheckBox("Fullscreen");
		r3Panel.add(fsCheck);
		
		rPanel.add(r3Panel);

		JPanel r4Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton doneButton = new JButton("Run");
		r4Panel.add(doneButton);

		JButton saveButton = new JButton("Save Config");
		r4Panel.add(saveButton);

		JButton loadButton = new JButton("Load Config");
		r4Panel.add(loadButton);
		
		r4Panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, rPanel.getPreferredSize().height));
		
		rPanel.add(r4Panel);
		
		contentPanel.add(rPanel);

		//final
		JScrollPane scrollPanel = new JScrollPane(contentPanel);
		scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanel.getVerticalScrollBar().setUnitIncrement(16);

		mainPanel.add(scrollPanel, BorderLayout.CENTER);
		
		settingsWindow.setContentPane(mainPanel);

		try (Scanner myReader = new Scanner(loadDataFile)) {
			while (myReader.hasNextLine()) {
				loadDataLines.add(myReader.nextLine());
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(mainPanel, "Unable to read previous data.");
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
		tideOneID.setText(loadDataLines.get(45));
		tideOneName.setText(loadDataLines.get(46));
		tideTwoID.setText(loadDataLines.get(47));
		tideTwoName.setText(loadDataLines.get(48));

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
	                   	+ "\"locName\": \"" + mainLocName.getText() + "\""
	                   	+ "},"
	                   	+ "\"nearbyLocations\": ["
	                   	+ "{\"icaoCode\": \"" + nearLocOneIcaoCode.getText() + "\", \"locName\": \"" + nearLocOneName.getText() + "\"},"
	                   	+ "{\"icaoCode\": \"" + nearLocTwoIcaoCode.getText() + "\", \"locName\": \"" + nearLocTwoName.getText() + "\"},"
	                   	+ "{\"icaoCode\": \"" + nearLocThreeIcaoCode.getText() + "\", \"locName\": \"" + nearLocThreeName.getText() + "\"},"
	                   	+ "{\"icaoCode\": \"" + nearLocFourIcaoCode.getText() + "\", \"locName\": \"" + nearLocFourName.getText() + "\"},"
	                   	+ "{\"icaoCode\": \"" + nearLocFiveIcaoCode.getText() + "\", \"locName\": \"" + nearLocFiveName.getText() + "\"},"
	                   	+ "{\"icaoCode\": \"" + nearLocSixIcaoCode.getText() + "\", \"locName\": \"" + nearLocSixName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + nearLocSevenIcaoCode.getText() + "\", \"locName\": \"" + nearLocSevenName.getText() + "\"}"
	                   	+ "],"
	                   	+ "\"regionalForecastLocations\": ["
	                    + "{\"icaoCode\": \"" + regForOneIcaoCode.getText() + "\", \"locName\": \"" + regForOneName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + regForTwoIcaoCode.getText() + "\", \"locName\": \"" + regForTwoName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + regForThreeIcaoCode.getText() + "\", \"locName\": \"" + regForThreeName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + regForFourIcaoCode.getText() + "\", \"locName\": \"" + regForFourName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + regForFiveIcaoCode.getText() + "\", \"locName\": \"" + regForFiveName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + regForSixIcaoCode.getText() + "\", \"locName\": \"" + regForSixName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + regForSevenIcaoCode.getText() + "\", \"locName\": \"" + regForSevenName.getText() + "\"}"
	                    + "],"
	                    + "\"regionalConditionsLocations\": ["
	                    + "{\"icaoCode\": \"" + regConOneIcaoCode.getText() + "\", \"locName\": \"" + regConOneName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + regConTwoIcaoCode.getText() + "\", \"locName\": \"" + regConTwoName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + regConThreeIcaoCode.getText() + "\", \"locName\": \"" + regConThreeName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + regConFourIcaoCode.getText() + "\", \"locName\": \"" + regConFourName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + regConFiveIcaoCode.getText() + "\", \"locName\": \"" + regConFiveName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + regConSixIcaoCode.getText() + "\", \"locName\": \"" + regConSixName.getText() + "\"},"
	                    + "{\"icaoCode\": \"" + regConSevenIcaoCode.getText() + "\", \"locName\": \"" + regConSevenName.getText() + "\"}"
	                    + "],"
	                    + "\"tideLocations\": ["
	                    + "{\"id\":\"" + tideOneID.getText() + "\", \"locName\":\"" + tideOneName.getText() + "\"},"
	                    + "{\"id\":\"" + tideTwoID.getText() + "\", \"locName\":\"" + tideTwoName.getText() + "\"}"
	                    + "]"
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
	                    JOptionPane.showMessageDialog(mainPanel, "Error saving config file.");
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
						JOptionPane.showMessageDialog(mainPanel, "Please choose a json file.");
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
							regForOneIcaoCode.setText(settingsObj.getJSONArray("regionalForecastLocations").getJSONObject(0).getString("icaoCode"));
							regForOneName.setText(settingsObj.getJSONArray("regionalForecastLocations").getJSONObject(0).getString("locName"));
							regForTwoIcaoCode.setText(settingsObj.getJSONArray("regionalForecastLocations").getJSONObject(1).getString("icaoCode"));
							regForTwoName.setText(settingsObj.getJSONArray("regionalForecastLocations").getJSONObject(1).getString("locName"));
							regForThreeIcaoCode.setText(settingsObj.getJSONArray("regionalForecastLocations").getJSONObject(2).getString("icaoCode"));
							regForThreeName.setText(settingsObj.getJSONArray("regionalForecastLocations").getJSONObject(2).getString("locName"));
							regForFourIcaoCode.setText(settingsObj.getJSONArray("regionalForecastLocations").getJSONObject(3).getString("icaoCode"));
							regForFourName.setText(settingsObj.getJSONArray("regionalForecastLocations").getJSONObject(3).getString("locName"));
							regForFiveIcaoCode.setText(settingsObj.getJSONArray("regionalForecastLocations").getJSONObject(4).getString("icaoCode"));
							regForFiveName.setText(settingsObj.getJSONArray("regionalForecastLocations").getJSONObject(4).getString("locName"));
							regForSixIcaoCode.setText(settingsObj.getJSONArray("regionalForecastLocations").getJSONObject(5).getString("icaoCode"));
							regForSixName.setText(settingsObj.getJSONArray("regionalForecastLocations").getJSONObject(5).getString("locName"));
							regForSevenIcaoCode.setText(settingsObj.getJSONArray("regionalForecastLocations").getJSONObject(6).getString("icaoCode"));
							regForSevenName.setText(settingsObj.getJSONArray("regionalForecastLocations").getJSONObject(6).getString("locName"));
							
							// tides
							tideOneID.setText(settingsObj.getJSONArray("tideLocations").getJSONObject(0).getString("id"));
							tideOneName.setText(settingsObj.getJSONArray("tideLocations").getJSONObject(0).getString("locName"));
							tideTwoID.setText(settingsObj.getJSONArray("tideLocations").getJSONObject(1).getString("id"));
							tideTwoName.setText(settingsObj.getJSONArray("tideLocations").getJSONObject(1).getString("locName"));

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
									+ regForSevenName.getText() + "\n" + tideOneID.getText() + "\n"
									+ tideOneName.getText() + "\n" + tideTwoID.getText() + "\n"
									+ tideTwoName.getText() + "\n";
							try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFilePath))) {
								writer.write(content);
								writer.close();
							} catch (IOException y) {
								y.printStackTrace();
								JOptionPane.showMessageDialog(mainPanel,
										"Error saving loaded config data to program. Config will not load on next startup.");
							}
						} catch (JSONException ex) {
							JOptionPane.showMessageDialog(mainPanel,
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
				tidesEnabled = tidesCheck.isSelected();
				
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
				
				tideIDs.add(tideOneID.getText());
				tideIDs.add(tideTwoID.getText());
				
				tideNames.add(tideOneName.getText());
				tideNames.add(tideTwoName.getText());
				
				adCrawl = adCrawlInput.getText();
				
					WindowRunner wr = new WindowRunner();
					
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
					
					ztOption = timeZoneDropdown.getSelectedItem().toString();
					
					if (ztOption.equals("Location Time")) {
						timeZone = getTimeZone(mainIcao);
					} else if (ztOption.equals("System Time")) {
						timeZone = ZoneId.systemDefault();
					} else {
						timeZone = ZoneId.of(timeZoneDropdown.getSelectedItem().toString());
					}
					
					selectedFlavor = (String) flavorDropdown.getSelectedItem();
					
					simWidth = Integer.parseInt(widthField.getText());
					simHeight = Integer.parseInt(heightField.getText());
					
					mainLat = icaoToLat(mainIcao, key);
					mainLon = icaoToLon(mainIcao, key);
					
					wr.start();
				
				settingsErrorThrown = false;
				settingsErrorReasons.clear();
				popupErrors = "";
			}
		});
		
		settingsWindow.setVisible(true);
	}
	
	static ZoneId getTimeZone(String icaoCode) {
		try {
			HttpClient Client = HttpClient.newBuilder().build();
	        HttpRequest Request = HttpRequest.newBuilder()
	                .uri(URI.create("https://api.weather.com/v3/wx/forecast/daily/7day?icaoCode=" + icaoCode + "&units=e&language=en-US&format=json&apiKey=" + key))
	                .GET()
	                .header("Accept", "application/json")
	                .build();
	        try {
	            HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
	            if (Response.statusCode() != 200) {
	            	return ZoneId.systemDefault();
	            }
	            String ResponseBody = Response.body();
	            JSONObject JSONResponse = new JSONObject(ResponseBody);
	            String time = JSONResponse.getJSONArray("validTimeLocal").getString(0);

		        return ZonedDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")).getZone();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ZoneId.systemDefault();
	        }
		} catch (Exception e) {
			return ZoneId.systemDefault();
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
