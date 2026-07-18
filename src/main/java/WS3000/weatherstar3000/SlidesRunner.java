package WS3000.weatherstar3000;

import java.util.ArrayList;
import java.util.Objects;

import javax.swing.ImageIcon;

import org.joml.Vector2f;
import org.joml.Vector4f;

import components.SpriteRenderer;
import components.Spritesheet;
import components.Sprite;
import util.AssetPool;
import util.Utilities;

import static org.lwjgl.glfw.GLFW.*;

import java.io.File;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class SlidesRunner extends Scene {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm:ss a");
	
	Vector4f color1 = new Vector4f(82.0f/255.0f, 80.0f/255.0f, 80.0f/255.0f, 1.0f); //gray
	Vector4f color2 = new Vector4f(11.0f/255.0f, 104.0f/255.0f, 6.0f/255.0f, 1.0f); //green
	Vector4f color3 = new Vector4f(61.0f/255.0f, 0.0f/255.0f, 109.0f/255.0f, 1.0f); //purple
	Vector4f color4 = new Vector4f(104.0f/255.0f, 0.0f/255.0f, 8.0f/255.0f, 1.0f); //red
	Vector4f color5 = new Vector4f(4.0f/255.0f, 92.0f/255.0f, 82.0f/255.0f, 1.0f); //cyan
	Vector4f color6 = new Vector4f(46.0f/255.0f, 2.0f/255.0f, 120.0f/255.0f, 1.0f); //blue BG
	Vector4f color7 = new Vector4f(124.0f/255.0f, 104.0f/255.0f, 6.0f/255.0f, 1.0f); //gold
	Vector4f color8 = new Vector4f(3.0f/255.0f, 0.0f/255.0f, 0.0f/255.0f, 1.0f); //black
	Vector4f color9 = new Vector4f(106.0f/255.0f, 80.0f/255.0f, 57.0f/255.0f, 1.0f); //brown
	Vector4f color10 = new Vector4f(116.0f/255.0f, 133.0f/255.0f, 22.0f/255.0f, 1.0f); //yellow
	Vector4f color11 = new Vector4f(60.0f/255.0f, 0.0f/255.0f, 109.0f/255.0f, 1.0f); //purple (again?)
	Vector4f color12 = new Vector4f(132.0f/255.0f, 81.0f/255.0f, 74.0f/255.0f, 1.0f); //peach
	Vector4f color13 = new Vector4f(83.0f/255.0f, 77.0f/255.0f, 1.0f/255.0f, 1.0f); //hazel
	Vector4f color14 = new Vector4f(9.0f/255.0f, 121.0f/255.0f, 172.0f/255.0f, 1.0f); //light blue
	Vector4f color15 = new Vector4f(160.0f/255.0f, 78.0f/255.0f, 14.0f/255.0f, 1.0f); //orange
	Vector4f color16 = new Vector4f(255.0f/255.0f, 255.0f/255.0f, 255.0f/255.0f, 1.0f); //white
	Vector4f greenScreen = new Vector4f(0.0f/255.0f, 255.0f/255.0f, 0.0f/255.0f, 1.0f);
	Vector4f backgroundColor = color6;
	
	Spritesheet sprites;
	GameObject bg;
	GameObject dateBg;
	GameObject ldl;
	GameObject ldlBg;
	GameObject travelBg;
	
	GameObject crawlBg;
	GameObject cBumperOne;
	GameObject cBumperTwo;
	Utilities utl = new Utilities();
	
	LocalDateTime now = LocalDateTime.now();
	ZonedDateTime today = ZonedDateTime.now();
	final String prefix = today.format(DateTimeFormatter.ofPattern("E MMM"));
	
	//slide line variables
	ArrayList<String> ccMainLines = new ArrayList<>();
	private GameObject[] ccPool = new GameObject[256];
	
	ArrayList<String> hoMainLines = new ArrayList<>();
	private GameObject[] hoPool = new GameObject[288];
	
	ArrayList<String> roMainLines = new ArrayList<>();
	private GameObject[] roPool = new GameObject[288];
	
	ArrayList<String> lfMainLines = new ArrayList<>();
	ArrayList<String> lfbMainLines = new ArrayList<>();
	ArrayList<String> forecastOneLines = new ArrayList<>();
	ArrayList<String> forecastTwoLines = new ArrayList<>();
	ArrayList<String> forecastThreeLines = new ArrayList<>();
	private GameObject[] lfhPool = new GameObject[32];
	private GameObject[] lfPool = new GameObject[288];
	
	ArrayList<String> alMainLines = new ArrayList<>();
	private GameObject[] alPool = new GameObject[288];
	
	ArrayList<String> rfMainLines = new ArrayList<>();
	private GameObject[] rfPool = new GameObject[288];
	
	ArrayList<String> efMainLines = new ArrayList<>();
	private GameObject[] efPool = new GameObject[288];
	
	ArrayList<String> olMainLines = new ArrayList<>();
	private GameObject[] olPool = new GameObject[288];
	
	ArrayList<String> travelMainLines = new ArrayList<>();
	private GameObject[] tfPool = new GameObject[864];
	
	ArrayList<String> bulletinMainLines = new ArrayList<>();
	ArrayList<GameObject> buPool = new ArrayList<>();
	
	String dateLine = "";
	private GameObject[] dtPool = new GameObject[32];
	
	ArrayList<String> tickerLines = new ArrayList<>();
	private GameObject[] ctPool = new GameObject[32];
	
	ArrayList<GameObject> acPool = new ArrayList<>();
	
	static boolean slidesStarted = false;
	
	private boolean poolsInit = false;
	private boolean slideInit = false;
	
	GameObject tBanner;
	TravelForecast tfData;
	int travelOffset = 0;
	boolean travelDataInit = false;
	boolean travelForecastActive = false;
	
	Bulletin buData;
	GameObject bulletinBg;
	GameObject bulletinBanner;
	boolean bulletinDataInit = false;
	static boolean bulletinActive = false;
	int bulletinOffset = 0;
	Vector4f bulletinColor = new Vector4f(0.0f/255.0f, 255.0f/155.0f, 255.0f/255.0f, 1.0f);
	float bulletinLength = 0.0f;
	
	static String ccType = "";
	
	String crawlingText;
	int crawlOffset = 0;
	static boolean adCrawlActive = false;
	float crawlLength = 0.0f;
	
	int ccIdx = 0;
	int ccRounds = 0;
	CurrentConditions tickerData = Main.currentConditions;
	
	float slideLength = 600.0f;//length in frames
	int slideIdx = 0;
	String[] slideLineup;
	boolean loop;
	boolean ending = false;
	int slideRounds = 0;
	int round = 0;
	static boolean collectingData = false;
	
	static AudioManager audioManager;
	
	public SlidesRunner() {
		ctData();
	}
	
	@Override
	public void init() {
		System.out.println("start slides loop");
		
		crawlingText = Main.adCrawl + " ";
		loop = Main.loopSlides;
		slideLineup = Main.slidesList;
		if (Main.greenScreen) {
			backgroundColor = greenScreen;
		}
		
		if (ccType == "crawl") {
			adCrawlActive = true;
		} else {
			adCrawlActive = false;
		}
		
		loadResources();
		this.camera = new Camera(new Vector2f(0, 0));
		
		sprites = AssetPool.getSpritesheet("/resources/images/charMap.png");
		
		bulletinBg = new GameObject("bulletinBg", new Transform(new Vector2f(0, 0), new Vector2f(720, 382)), 0);
		bulletinBg.addComponent(new SpriteRenderer(color16));
		
		
		bulletinBanner = new GameObject("bulletinBanner", new Transform(new Vector2f(0, 0), new Vector2f(720, 42)), 0);
		bulletinBanner.addComponent(new SpriteRenderer(color16));
		
		initPools();
        
		//Z INDICES: 
		//0 - SLIDES/TRAVEL/BULLETIN CRAWL
		//2 - LOWERBAR
		ldl = new GameObject("ldl", new Transform(new Vector2f(0, 380), new Vector2f(720, 2)), 0);
		ldl.addComponent(new SpriteRenderer(color16));
		
        dateBg = new GameObject("dateBg", new Transform(new Vector2f(0, 382), new Vector2f(720, 18)), 2);
        dateBg.addComponent(new SpriteRenderer(backgroundColor));
        
        ldlBg = new GameObject("ldlBg", new Transform(new Vector2f(0, 400), new Vector2f(720, 80)), 2);
        ldlBg.addComponent(new SpriteRenderer(backgroundColor));
        
        bg = new GameObject("bg", new Transform(new Vector2f(0, 0), new Vector2f(720, 380)), 0);
        bg.addComponent(new SpriteRenderer(backgroundColor));
		
		travelBg = new GameObject("travelBg", new Transform(new Vector2f(0, 0), new Vector2f(720, 380)), 0);
		travelBg.addComponent(new SpriteRenderer(color1));
		
		tBanner = new GameObject("travelBg", new Transform(new Vector2f(0, 0), new Vector2f(720, 42)), 0);
		tBanner.addComponent(new SpriteRenderer(color1));
		
		crawlBg = new GameObject("crawlBg", new Transform(new Vector2f(0, 382), new Vector2f(720, 98)), 2);
		crawlBg.addComponent(new SpriteRenderer(backgroundColor));
		
		cBumperOne = new GameObject("cBumperOne", new Transform(new Vector2f(0, 382), new Vector2f(109, 98)), 2);
		cBumperOne.addComponent(new SpriteRenderer(backgroundColor));
		
		cBumperTwo = new GameObject("cBumperTwo", new Transform(new Vector2f(634, 382), new Vector2f(86, 98)), 2);
		cBumperTwo.addComponent(new SpriteRenderer(backgroundColor));
		
		float x = (float) crawlingText.length()+1;
		float crawlWidth = 17.5f * x;
		crawlLength = 262.5f + (crawlWidth/2) + 2.0f;
		
		Window.slidesSecFrameCount = 0;
		Window.secFrameCount = 0;
		Window.fourSecFrameCount = 0;
		Window.crawlSecFrameCount = 0;
		Window.bTimerSecFrameCount = 0;
		
		audioManager = new AudioManager(Main.jarFolder + File.separator + "music" + File.separator);
		audioManager.start();
		
		slidesStarted = true;
	}
	
	private void loadResources() {
		AssetPool.getShader("/resources/shaders/default.glsl");
		
		AssetPool.addSpritesheet("charMap.png", 
				new Spritesheet(AssetPool.getTexture("/resources/images/charMap.png"),
						32, 32, 128, 0));
	}
	
	private void initPools() {
	    if (poolsInit) return;
	    for (int i = 0; i < ccPool.length; i++) {
	    	ccPool[i] = new GameObject("ccPool" + i, new Transform(new Vector2f()), 0);
	    	ccPool[i].addComponent(new SpriteRenderer(new Vector4f(1,1,1,1)));
	    }
	    
	    for (int i = 0; i < hoPool.length; i++) {
	    	hoPool[i] = new GameObject("hoPool" + i, new Transform(new Vector2f()), 0);
	    	hoPool[i].addComponent(new SpriteRenderer(new Vector4f(1,1,1,1)));
	    }
	    
	    for (int i = 0; i < roPool.length; i++) {
	    	roPool[i] = new GameObject("roPool" + i, new Transform(new Vector2f()), 0);
	    	roPool[i].addComponent(new SpriteRenderer(new Vector4f(1,1,1,1)));
	    }
	    
	    for (int i = 0; i < lfPool.length; i++) {
	    	if (i < 32) {
	    		lfhPool[i] = new GameObject("lfhPool" + i, new Transform(new Vector2f()), 0);
		    	lfhPool[i].addComponent(new SpriteRenderer(new Vector4f(1,1,1,1)));
	    	}
	    	lfPool[i] = new GameObject("lfPool" + i, new Transform(new Vector2f()), 0);
	    	lfPool[i].addComponent(new SpriteRenderer(new Vector4f(1,1,1,1)));
	    }
	    
	    for (int i = 0; i < alPool.length; i++) {
	    	alPool[i] = new GameObject("alPool" + i, new Transform(new Vector2f()), 0);
	    	alPool[i].addComponent(new SpriteRenderer(new Vector4f(1,1,1,1)));
	    }
	    
	    for (int i = 0; i < rfPool.length; i++) {
	    	rfPool[i] = new GameObject("alPool" + i, new Transform(new Vector2f()), 0);
	    	rfPool[i].addComponent(new SpriteRenderer(new Vector4f(1,1,1,1)));
	    }
	    
	    for (int i = 0; i < efPool.length; i++) {
	    	efPool[i] = new GameObject("alPool" + i, new Transform(new Vector2f()), 0);
	    	efPool[i].addComponent(new SpriteRenderer(new Vector4f(1,1,1,1)));
	    }
	    
	    for (int i = 0; i < olPool.length; i++) {
	    	olPool[i] = new GameObject("alPool" + i, new Transform(new Vector2f()), 0);
	    	olPool[i].addComponent(new SpriteRenderer(new Vector4f(1,1,1,1)));
	    }

	    for (int i = 0; i < tfPool.length; i++) {
	    	tfPool[i] = new GameObject("tfPool" + i, new Transform(new Vector2f()), 0);
	    	tfPool[i].addComponent(new SpriteRenderer(new Vector4f(1,1,1,1)));
	    }
	    
	    for (int i = 0; i < dtPool.length; i++) {
	    	dtPool[i] = new GameObject("tfPool" + i, new Transform(new Vector2f()), 2);
	    	dtPool[i].addComponent(new SpriteRenderer(new Vector4f(1,1,1,1)));
	    }
	    
	    for (int i = 0; i < ctPool.length; i++) {
	    	ctPool[i] = new GameObject("tfPool" + i, new Transform(new Vector2f()), 2);
	    	ctPool[i].addComponent(new SpriteRenderer(new Vector4f(1,1,1,1)));
	    }
	    
	    if (ccType != "obs") {
	    	acPool.clear();
	    	for (int i = 0; i < crawlingText.length(); i++) {
	    		acPool.add(new GameObject("acPool" + i, new Transform(new Vector2f()), 2));
	    		acPool.get(i).addComponent(new SpriteRenderer(new Vector4f(1,1,1,1)));
	    	}
	    }
	    
	    initBulPool();
	    
	    poolsInit = true;   
	}
	public void initBulPool() {
		Bulletin bulletinData = Main.bulletin;
		buPool.clear();
		if (bulletinData.bulletinText.get(1) != "NOBULLETINCRAWL") {
			int j = 0;
			for (int i = 1; i < bulletinData.bulletinText.size()-1; i++) {
				for (int e = 0; e < bulletinData.bulletinText.get(i).length(); e++) {
					buPool.add(new GameObject("buPool" + j, new Transform(new Vector2f()), 0));
					buPool.get(j).addComponent(new SpriteRenderer(new Vector4f(1,1,1,1)));
					j++;
				}
			}
			
			try {
				bulletinBg.getComponent(SpriteRenderer.class).setColor(utl.getBulletinColor(bulletinData.bulletinText.get(0)));
			} catch (IOException e) {
				bulletinBg.getComponent(SpriteRenderer.class).setColor(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
				e.printStackTrace();
			}
			
			try {
				bulletinBanner.getComponent(SpriteRenderer.class).setColor(utl.getBulletinColor(bulletinData.bulletinText.get(0)));
			} catch (IOException e) {
				bulletinBanner.getComponent(SpriteRenderer.class).setColor(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void update(float dt) {
		//if (audioManager.isFinished()) {
		//	audioManager.start();
		//}
		
		this.gameObjects.clear();
		this.renderer.clear();
		
		if (Window.secFrameCount >= 60) {
			Window.secFrameCount = 0;
		}
		if (Window.fourSecFrameCount >= 240) {
			Window.fourSecFrameCount = 0;
		}
		if (Window.slidesSecFrameCount >= slideLength) {
			Window.slidesSecFrameCount = 0;
		}
		
		if (Window.bTimerSecFrameCount >= 2700) {
			Window.bTimerSecFrameCount = 0;
		}
		
		if (Window.slidesSecFrameCount % slideLength == 0) {
			
			if (ending && !bulletinActive) {
				System.exit(0);
				audioManager.close();
				//glfwSetWindowShouldClose(Window.getGLFWWindow(), true); 
				//Main.settingsWindow.setVisible(true);
			}
			travelForecastActive = false;
			travelDataInit = false;
			slideInit = false;
			round++;
			if (round >= slideRounds) {
				slideIdx++;
				if (slideIdx >= slideLineup.length) {
					slideIdx = 0;
					if (!loop) {
						ending = true;
					} else {
						DataRunner data = new DataRunner();
						if (!collectingData) {
							collectingData = true;
							ctData();
							data.start();//upon finish, collectingData will be turned false
							//if (!bulletinActive) {
							//	initBulPool();
							//}
						}
					}
				}
				round = 0;
			}
		}
		slideChooser();
		if (travelForecastActive) {
			if (Window.secFrameCount % 2 == 0) travelOffset--;
			travelForecast();
		}
		
		//ldl
		this.addGameObjectToScene(ldl);
		
		//update bulletin
		if (!bulletinActive) {
			if (Window.bTimerSecFrameCount == 0) {
				if (Main.bulletin.bulletinText.get(1) != "NOBULLETINCRAWL") {
					initBulPool();
					bulletinActive = true;
					Window.bSlideSecFrameCount++;
					bulletin();
				} else {
					bulletinActive = false;
				}
			}
		} else {
			if (Window.bSlideSecFrameCount % bulletinLength == 0) {
				//stop bulletin
				bulletinActive = false;
				bulletinDataInit = false;
				Window.bTimerSecFrameCount = 0;
				if (ending) {
					System.exit(0);
					audioManager.close();
					//glfwSetWindowShouldClose(Window.getGLFWWindow(), true); 
					//Main.settingsWindow.setVisible(true);
				}
			} else {
				//continue bulletin
				if (Window.secFrameCount % 2 == 0) bulletinOffset--;
				Window.bSlideSecFrameCount++;
				bulletin();
			}
		}
		
		//lowerArea update
		if (!adCrawlActive) {
			if (Window.fourSecFrameCount % 240.0f == 0) {
				if (ccRounds >= 1) {
					ccRounds = 0;
					toggleCrawl();
				} else {
					ccTicker(true);
				}
			}
			dateTime();
			ccTicker(false);
		} else {
			if (Window.crawlSecFrameCount >= crawlLength) {
				toggleCrawl();
				Window.crawlSecFrameCount = 0;
			}
			crawlOffset -= 2;
			adCrawl();
		}
		
		for (GameObject go : this.gameObjects) {
	        go.update(dt);
	        this.renderer.add(go); 
	    }
	    
	    this.renderer.render();
	}
	public void toggleCrawl() {
		if (ccType == "obs") adCrawlActive = false;
		if (ccType == "crawl") adCrawlActive = true; crawlOffset = 0;
		if (ccType == "both") {
			if (adCrawlActive) {
				adCrawlActive = false;
			} else {
				adCrawlActive = true; crawlOffset = 0;
			}
		}
	}
	
	public void adCrawl() {
	    this.addGameObjectToScene(crawlBg);
	    
	    int poolIdx = 0;
	    int textY = 382;
	    float textX = 634.0f + crawlOffset;
	    
	    for (int i = 0; i < crawlingText.length(); i++) {
	    	if (poolIdx >= acPool.size()) break;
	            
	        if (textX > 91 && textX < 635) {
	        	
	        	char c = crawlingText.toUpperCase().charAt(i);
	            
	            GameObject go = acPool.get(poolIdx++);
	            go.transform.position.set(textX, textY);
	            go.transform.scale.set(18, 32);
	            	
	            SpriteRenderer sr = go.getComponent(SpriteRenderer.class);
	            sr.setSprite(sprites.getSprite(utl.getMainChar(c)));
	            	
	            this.addGameObjectToScene(go);
	        }
	        if (i % 2 != 0) textX++;
	        textX += 17;
	    }
	    
	    this.addGameObjectToScene(cBumperOne);
	    this.addGameObjectToScene(cBumperTwo);
	}
	
	public void dateTime() {
		now = LocalDateTime.now();
		today = ZonedDateTime.now();
        
		dateLine = utl.ljust(String.format("%s %2d", prefix, now.getDayOfMonth()).toUpperCase(), 10, " ") + utl.ljust(" ", 11, " ") + utl.rjust(now.format(formatter), 11, " ");
		
		this.addGameObjectToScene(dateBg);
		
		int textX = 81;
		int textY = 382;
		int poolIdx = 0;
		
		for (int i = 0; i < dateLine.length(); i ++) {
    		if (poolIdx >= dtPool.length) break;
    		
    		char c = dateLine.charAt(i);
    		
    		GameObject go = dtPool[poolIdx++];
    		go.transform.position.set(textX, textY);
    		go.transform.scale.set(18, 32);
    		
    		SpriteRenderer sr = go.getComponent(SpriteRenderer.class);
            sr.setSprite(sprites.getSprite(utl.getSmallChar(c)));
            
            this.addGameObjectToScene(go);
            
            if (i % 2 != 0) textX++;
            textX += 17;
    	}
	}
	public void ccTicker(boolean advance) {
		if (advance) {
			ccIdx++;
			if (ccIdx >= tickerLines.size()) {
				ccIdx = 0;
				ccRounds++;
			}
		}
		
		this.addGameObjectToScene(ldlBg);
		int textX = 81;
        int textY = 404;
        int poolIdx = 0;
        
        String line = tickerLines.get(ccIdx);
        for (int i = 0; i < line.length(); i ++) {
    		if (poolIdx >= ctPool.length) break;
    		
    		char c = line.charAt(i);
    		
    		GameObject go = ctPool[poolIdx++];
    		go.transform.position.set(textX, textY);
    		go.transform.scale.set(18, 32);
    		
    		SpriteRenderer sr = go.getComponent(SpriteRenderer.class);
            sr.setSprite(sprites.getSprite(utl.getMainChar(c)));
            
            this.addGameObjectToScene(go);
            
            if (i % 2 != 0) textX++;
            textX += 17;
    	}
	}
	public void ctData() {
		CurrentConditions ctData = Main.currentConditions;
        
		tickerLines.clear();
        tickerLines.add(utl.ljust(ctData.locName, 32, " "));
        tickerLines.add(utl.ljust(ctData.condition, 32, " "));
        tickerLines.add(utl.ljust(ctData.temperature + "    " +  ctData.feelsLike, 32, " "));
        tickerLines.add(utl.ljust(ctData.humidity + "   " +  ctData.dewPoint, 32, " "));
        tickerLines.add(utl.ljust(ctData.pressure, 32, " "));
        tickerLines.add(utl.ljust(ctData.wind + "  " +  ctData.gusts, 32, " "));
        tickerLines.add(utl.ljust(ctData.visibility + " " +  ctData.ceiling, 32, " "));
        tickerLines.add(utl.ljust(ctData.precip, 32, " "));
	}
	public void currentConditions() {
		slideRounds = 0;
		slideLength = 600.0f;
        this.addGameObjectToScene(bg);
        
        if (!slideInit) {
        	CurrentConditions ccData = Main.currentConditions;
        	
        	ccMainLines.clear();
        	ccMainLines.add(utl.ljust(ccData.locName, 32, " "));
        	ccMainLines.add(utl.ljust(ccData.condition, 32, " "));
        	ccMainLines.add(utl.ljust(ccData.temperature + "    " +  ccData.feelsLike, 32, " "));
        	ccMainLines.add(utl.ljust(ccData.humidity + "   " +  ccData.dewPoint, 32, " "));
        	ccMainLines.add(utl.ljust(ccData.pressure, 32, " "));
        	ccMainLines.add(utl.ljust(ccData.wind + "  " +  ccData.gusts, 32, " "));
        	ccMainLines.add(utl.ljust(ccData.visibility + " " +  ccData.ceiling, 32, " "));
        	ccMainLines.add(utl.ljust(ccData.precip, 32, " "));
        	
        	slideInit = true;
        }
        
        int textX = 81;
        int textY = 44;
        int poolIdx = 0;
        int lineHeight = 40;
        
        for (int row = 0; row < ccMainLines.size(); row++) {
        	String line = ccMainLines.get(row);
        	for (int i = 0; i < line.length(); i ++) {
        		if (poolIdx >= ccPool.length) break;
        		
        		char c = line.charAt(i);
        		
        		GameObject go = ccPool[poolIdx++];
        		go.transform.position.set(textX, textY);
        		go.transform.scale.set(18, 32);
        		
        		SpriteRenderer sr = go.getComponent(SpriteRenderer.class);
	            sr.setSprite(sprites.getSprite(utl.getMainChar(c)));
	            
	            this.addGameObjectToScene(go);
	            
	            if (i % 2 != 0) textX++;
	            textX += 17;
        	}
        	
        	textX = 81;
        	textY += lineHeight;
        }
	}
	
	public void hourlyObservations() {
		slideRounds = 0;
		slideLength = 600.0f;
        this.addGameObjectToScene(bg);
        
        if (!slideInit) {
        	HourlyObservations hoData = Main.hourlyObservations;
        	
        	hoMainLines.clear();
        	hoMainLines.add("   LATEST HOURLY OBSERVATIONS   ");
        	hoMainLines.add("LOCATION       \\F WEATHER   WIND");
        	for (int i = 0; i < hoData.locName.size(); i++) {
        		hoMainLines.add(utl.ljust(hoData.locName.get(i), 14, " ") + utl.ljust(hoData.temperature.get(i), 3, " ") + " " + utl.ljust(hoData.condition.get(i), 10, " ") + hoData.wind.get(i));
        	};
        	
        	slideInit = true;
        }
        
        int textX = 81;
        int textY = 44;
        int poolIdx = 0;
        
        for (int row = 0; row < hoMainLines.size(); row++) {
        	int lineHeight = row == 0 ? 38 : row == 1 ? 22 : 40;
        	String line = hoMainLines.get(row);
        	for (int i = 0; i < line.length(); i ++) {
        		if (poolIdx >= hoPool.length) break;
        		
        		char c = line.charAt(i);
        		
        		GameObject go = hoPool[poolIdx++];
        		go.transform.position.set(textX, textY);
        		go.transform.scale.set(18, 32);
        		
        		SpriteRenderer sr = go.getComponent(SpriteRenderer.class);
        		if (row == 1) {sr.setSprite(sprites.getSprite(utl.getSmallChar(c)));} else {sr.setSprite(sprites.getSprite(utl.getMainChar(c)));}
	            
	            
	            this.addGameObjectToScene(go);
	            
	            if (i % 2 != 0) textX++;
	            textX += 17;
        	}
        	
        	textX = 81;
        	textY += lineHeight;
        }
	}
	
	public void regionalConditions() {
		slideRounds = 0;
		slideLength = 600.0f;
        this.addGameObjectToScene(bg);
        
        if (!slideInit) {
        	RegionalConditions rcData = Main.regionalConditions;
        	
        	roMainLines.clear();
        	roMainLines.add("  CONDITIONS ACROSS THE REGION  ");
        	roMainLines.add("CITY                WEATHER   \\F");
        	for (int i = 0; i < rcData.locName.size(); i++) {
        		roMainLines.add(utl.ljust(rcData.locName.get(i), 19, " ") + " " + utl.ljust(rcData.condition.get(i), 9, " ").substring(0, 9) + rcData.temperature.get(i));
        	}
        	
        	slideInit = true;
        }
        

        int textX = 81;
        int textY = 44;
        int poolIdx = 0;
        
        for (int row = 0; row < roMainLines.size(); row++) {
        	int lineHeight = row == 0 ? 38 : row == 1 ? 22 : 40;
        	String line = roMainLines.get(row);
        	for (int i = 0; i < line.length(); i ++) {
        		if (poolIdx >= roPool.length) break;
        		
        		char c = line.charAt(i);
        		
        		GameObject go = roPool[poolIdx++];
        		go.transform.position.set(textX, textY);
        		go.transform.scale.set(18, 32);
        		
        		SpriteRenderer sr = go.getComponent(SpriteRenderer.class);
        		if (row == 1) {sr.setSprite(sprites.getSprite(utl.getSmallChar(c)));} else {sr.setSprite(sprites.getSprite(utl.getMainChar(c)));}
	            
	            
	            this.addGameObjectToScene(go);
	            
	            if (i % 2 != 0) textX++;
	            textX += 17;
        	}
        	
        	textX = 81;
        	textY += lineHeight;
        }
	}
	
	public void localForecast() {
		slideRounds = 3;
		slideLength = 600.0f;
        this.addGameObjectToScene(bg);
        
        if (!slideInit) {
        	LocalForecast lfData = Main.localForecast;
        	
        	lfMainLines.clear();
        	lfbMainLines.clear();
        	forecastOneLines.clear();
        	forecastTwoLines.clear();
        	forecastThreeLines.clear();
        	
        	StringBuilder sb = new StringBuilder();
        	
        	if (lfData.bulletin.get(0) == "NOBULLETIN") {
                for (int i = 0; i <= lfData.bulletin.size()-1; i++) {
                    if (lfData.bulletin.get(i).equals("*NEWLINE*")) {
                    	lfbMainLines.add("*" + utl.cjust(sb.toString(), 30, " ") + "*");
                    	sb.setLength(0);
                        continue;
                    }
                    int wordlength = lfData.bulletin.get(i).length();
                    if (wordlength + sb.length() + 1 <= 30) {
                        if (!sb.isEmpty()) {sb.append(" ");}
                        sb.append(lfData.bulletin.get(i));
                    } else {
                        i--;
                        lfbMainLines.add("*" + utl.cjust(sb.toString(), 30, " ") + "*");
                        sb.setLength(0);
                    }
                }
                lfbMainLines.add("*" + utl.cjust(sb.toString(), 30, " ") + "*");
                sb.setLength(0);
            } else {
            	lfbMainLines.add(utl.ljust("", 32, " "));
            }
        	sb.setLength(0);
        	
            for (int i = 0; i <= lfData.forecastOne.size()-1; i++) {
                int wordlength =  lfData.forecastOne.get(i).length();
                if (wordlength + sb.length() + 1 <= 32) {
                    if (!sb.isEmpty()) {sb.append(" ");}
                    sb.append(lfData.forecastOne.get(i));
                } else {
                    i--;
                    forecastOneLines.add(utl.ljust(sb.toString(), 32, " "));
                    sb.setLength(0);
                }
            }
            forecastOneLines.add(utl.ljust(sb.toString(), 32, " "));
            sb.setLength(0);
            
            for (int i = 0; i <= lfData.forecastTwo.size()-1; i++) {
                int wordlength =  lfData.forecastTwo.get(i).length();
                if (wordlength + sb.length() + 1 <= 32) {
                    if (!sb.isEmpty()) {sb.append(" ");}
                    sb.append(lfData.forecastTwo.get(i));
                } else {
                    i--;
                    forecastTwoLines.add(utl.ljust(sb.toString(), 32, " "));
                    sb.setLength(0);
                }
            }
            forecastTwoLines.add(utl.ljust(sb.toString(), 32, " "));
            sb.setLength(0);
            
            for (int i = 0; i <= lfData.forecastThree.size()-1; i++) {
                int wordlength =  lfData.forecastThree.get(i).length();
                if (wordlength + sb.length() + 1 <= 32) {
                    if (!sb.isEmpty()) {sb.append(" ");}
                    sb.append(lfData.forecastThree.get(i));
                } else {
                    i--;
                    forecastThreeLines.add(utl.ljust(sb.toString(), 32, " "));
                    sb.setLength(0);
                }
            }
            forecastThreeLines.add(utl.ljust(sb.toString(), 32, " "));
            sb.setLength(0);
            
            int linesPassed = 0;
            if (lfbMainLines.get(0) == "                                 " || forecastOneLines.size() > 7) {
                for (int i = 0; i <= lfbMainLines.size()-1; i++) {
                    lfMainLines.add(lfbMainLines.get(i));
                    linesPassed++;
                }
            } else {
            	lfMainLines.add(utl.ljust("", 32, " "));
                linesPassed++;
            }
            for (int i = 0; i <= forecastOneLines.size()-1; i++) {
            	lfMainLines.add(forecastOneLines.get(i));
                linesPassed++;
            }
            while (linesPassed < 8) {
            	lfMainLines.add(utl.ljust("", 32, " "));
                linesPassed++;
            }
            lfMainLines.add(utl.ljust("", 32, " "));
            linesPassed++;
            for (int i = 0; i <= forecastTwoLines.size()-1; i++) {
            	lfMainLines.add(forecastTwoLines.get(i));
                linesPassed++;
            }
            while (linesPassed < 16) {
            	lfMainLines.add(utl.ljust("", 32, " "));
                linesPassed++;
            }
            lfMainLines.add(utl.ljust("", 32, " "));
            for (int i = 0; i <= forecastThreeLines.size()-1; i++) {
            	lfMainLines.add(forecastThreeLines.get(i));
                linesPassed++;
            }
            while (linesPassed < 24) {
            	lfMainLines.add(utl.ljust("", 32, " "));
                linesPassed++;
            }
        	
        	slideInit = true;
        }
		
		int textX = 81;
        int textY = 44;
		int poolIdx = 0;
		
		String header = "        YOUR TWC FORECAST       ";
		for (int i = 0; i < header.length(); i ++) {
    		if (poolIdx >= lfhPool.length) break;
    		
    		char c = header.charAt(i);
    		
    		GameObject go = lfhPool[poolIdx++];
    		go.transform.position.set(textX, textY);
    		go.transform.scale.set(18, 32);
    		
    		SpriteRenderer sr = go.getComponent(SpriteRenderer.class);
    		sr.setSprite(sprites.getSprite(utl.getSmallChar(c)));
            
            this.addGameObjectToScene(go);
            
            if (i % 2 != 0) textX++;
            textX += 17;
    	}
    	
		poolIdx = 0;
    	textX = 81;
		textY = 62;
		
		for (int row = 0; row < 8; row++) {
        	int lineHeight = 40;
        	String line = lfMainLines.get(row + (8*round));
        	for (int i = 0; i < line.length(); i ++) {
        		if (poolIdx >= lfPool.length) break;
        		
        		char c = line.charAt(i);
        		
        		GameObject go = lfPool[poolIdx++];
        		go.transform.position.set(textX, textY);
        		go.transform.scale.set(18, 32);
        		
        		SpriteRenderer sr = go.getComponent(SpriteRenderer.class);
        		sr.setSprite(sprites.getSprite(utl.getMainChar(c)));
	            
	            this.addGameObjectToScene(go);
	            
	            if (i % 2 != 0) textX++;
	            textX += 17;
        	}
        	
        	textX = 81;
        	textY += lineHeight;
        }
	}
	
	public void almanac() {
		slideRounds = 0;
		slideLength = 600.0f;
		this.addGameObjectToScene(bg);
		
		if (!slideInit) {
			Almanac almanacData = Main.almanac;
			
			alMainLines.clear();
			
			alMainLines.add("  THE WEATHER CHANNEL ALMANAC   ");
			alMainLines.add(utl.ljust("", 32, " "));//half space
	        alMainLines.add(utl.ljust("             " + almanacData.today + " " + almanacData.tomorrow, 32, " "));
	        alMainLines.add(utl.ljust("SUNRISE      " + almanacData.todaySunrise + "  " + almanacData.tomorrowSunrise, 32, " "));
	        alMainLines.add(utl.ljust("SUNSET       " + almanacData.todaySunset + "  " + almanacData.tomorrowSunset, 32, " "));
	        alMainLines.add(utl.ljust("NORMAL LOW     " + almanacData.todayLow + "    " + almanacData.tomorrowLow, 32, " "));
	        alMainLines.add(utl.ljust("NORMAL HIGH    " + almanacData.todayHigh + "    " + almanacData.tomorrowHigh, 32, " "));
	        alMainLines.add(utl.ljust("", 32, " "));//half space
	        alMainLines.add(utl.ljust(Main.almanac.normalPrecip, 32, " "));
	        
	        slideInit = true;
		}
		
		int textX = 81;
        int textY = 44;
        int poolIdx = 0;
        
        for (int row = 0; row < alMainLines.size(); row++) {
        	int lineHeight = row == 1 || row == 7 ? 22 : 40;
        	String line = alMainLines.get(row);
        	for (int i = 0; i < line.length(); i ++) {
        		if (poolIdx >= alPool.length) break;
        		
        		char c = line.charAt(i);
        		
        		GameObject go = alPool[poolIdx++];
        		go.transform.position.set(textX, textY);
        		go.transform.scale.set(18, 32);
        		
        		SpriteRenderer sr = go.getComponent(SpriteRenderer.class);
        		sr.setSprite(sprites.getSprite(utl.getMainChar(c)));
	            
	            this.addGameObjectToScene(go);
	            
	            if (i % 2 != 0) textX++;
	            textX += 17;
        	}
        	
        	textX = 81;
        	textY += lineHeight;
        }
	}
	
	public void regionalForecast() {
		slideRounds = 0;
		slideLength = 600.0f;
		this.addGameObjectToScene(bg);
		
		if (!slideInit) {
			RegionalForecast rfData = Main.regionalForecast;
			
			rfMainLines.clear();
			
			rfMainLines.add("   FORECAST ACROSS THE REGION   ");
			rfMainLines.add("CITY           WEATHER   LOW  HI");
			for (int i = 0; i < rfData.locName.size(); i++) {
				rfMainLines.add(utl.ljust(rfData.locName.get(i), 14, " ") + " " + utl.ljust(rfData.condition.get(i), 10, " ") + rfData.low.get(i) + " " + rfData.high.get(i));
			}
			
			slideInit = true;
		}
		
		int textX = 81;
        int textY = 44;
        int poolIdx = 0;
        
        for (int row = 0; row < rfMainLines.size(); row++) {
        	int lineHeight = row == 0 ? 38 : row == 1 ? 22 : 40;
        	String line = rfMainLines.get(row);
        	for (int i = 0; i < line.length(); i ++) {
        		if (poolIdx >= rfPool.length) break;
        		
        		char c = line.charAt(i);
        		
        		GameObject go = rfPool[poolIdx++];
        		go.transform.position.set(textX, textY);
        		go.transform.scale.set(18, 32);
        		
        		SpriteRenderer sr = go.getComponent(SpriteRenderer.class);
        		if (row == 1) {sr.setSprite(sprites.getSprite(utl.getSmallChar(c)));} else {sr.setSprite(sprites.getSprite(utl.getMainChar(c)));}
	            
	            
	            this.addGameObjectToScene(go);
	            
	            if (i % 2 != 0) textX++;
	            textX += 17;
        	}
        	
        	textX = 81;
        	textY += lineHeight;
        }
	}
	
	public void extendedForecast() {
		slideRounds = 0;
		slideLength = 600.0f;
		this.addGameObjectToScene(bg);
		
		if (!slideInit) {
			ExtendedForecast efData = Main.extendedForecast;
			
			efMainLines.clear();
			
			efMainLines.add("        EXTENDED FORECAST       ");
			efMainLines.add(utl.ljust("", 32, " "));
			efMainLines.add(" " + efData.dayOne + " " + efData.dayTwo + " " + efData.dayThree);
			efMainLines.add(" " + efData.conditionOne.get(0) + " " + efData.conditionTwo.get(0) + " " + efData.conditionThree.get(0));
			efMainLines.add(" " + efData.conditionOne.get(1) + " " + efData.conditionTwo.get(1) + " " + efData.conditionThree.get(1));
			efMainLines.add(utl.ljust("", 32, " "));
			efMainLines.add(" " + efData.lowOne + " " + efData.lowTwo + " " + efData.lowThree);
			efMainLines.add(" " + efData.highOne + " " + efData.highTwo + " " + efData.highThree);
        
			slideInit = true;
		}
		
		int textX = 81;
        int textY = 44;
        int poolIdx = 0;
        
        for (int row = 0; row < efMainLines.size(); row++) {
        	int lineHeight = 40;
        	String line = efMainLines.get(row);
        	for (int i = 0; i < line.length(); i ++) {
        		if (poolIdx >= efPool.length) break;
        		
        		char c = line.charAt(i);
        		
        		GameObject go = efPool[poolIdx++];
        		go.transform.position.set(textX, textY);
        		go.transform.scale.set(18, 32);
        		
        		SpriteRenderer sr = go.getComponent(SpriteRenderer.class);
	            sr.setSprite(sprites.getSprite(utl.getMainChar(c)));
	            
	            this.addGameObjectToScene(go);
	            
	            if (i % 2 != 0) textX++;
	            textX += 17;
        	}
        	
        	textX = 81;
        	textY += lineHeight;
        }
	}
	
	public void outlook() {
		slideRounds = 0;
		slideLength = 600.0f;
		this.addGameObjectToScene(bg);
		
		if (!slideInit) {
			Outlook oData = Main.outlook;
			
			olMainLines.clear();
			
			olMainLines.add(" NAT'L WEATHER SERVICE OUTLOOK  ");
			olMainLines.add(utl.ljust(" ", 32, " "));//half space
			olMainLines.add(utl.cjust(oData.month, 32, " "));
			olMainLines.add(utl.ljust(" ", 32, " "));
			
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i <= oData.outlook.size()-1; i++) {
				int cwordlength =  oData.outlook.get(i).length();
				if (cwordlength + sb.length() + 1 <= 32) {
					if (!sb.isEmpty()) {sb.append(" ");}
					sb.append(oData.outlook.get(i));
				} else {
					i--;
					olMainLines.add(utl.ljust(sb.toString(), 32, " "));
					sb.setLength(0);
				}
			}
			olMainLines.add(utl.ljust(sb.toString(), 32, " "));
			
			slideInit = true;
		}
		
        int textX = 81;
        int textY = 44;
        int poolIdx = 0;
        
        for (int row = 0; row < olMainLines.size(); row++) {
        	int lineHeight = row == 1 ? 22 : 40;
        	String line = olMainLines.get(row);
        	for (int i = 0; i < line.length(); i ++) {
        		if (poolIdx >= olPool.length) break;
        		
        		char c = line.charAt(i);
        		
        		GameObject go = olPool[poolIdx++];
        		go.transform.position.set(textX, textY);
        		go.transform.scale.set(18, 32);
        		
        		SpriteRenderer sr = go.getComponent(SpriteRenderer.class);
        		sr.setSprite(sprites.getSprite(utl.getMainChar(c)));
	            
	            
	            this.addGameObjectToScene(go);
	            
	            if (i % 2 != 0) textX++;
	            textX += 17;
        	}
        	
        	textX = 81;
        	textY += lineHeight;
        }
	}
	public void travelForecast() {
	    slideLength = 3600.0f;
	    travelForecastActive = true;
	   this.addGameObjectToScene(travelBg);

	    if (!travelDataInit) {
	        tfData = Main.travelForecast;
	        travelMainLines.clear();
	        travelMainLines.add(" TRAVEL CITIES FORECAST ");
	        travelMainLines.add(utl.ljust(" ", 32, " "));
	        travelMainLines.add("CITY           WEATHER   LOW  HI");
	        for (int i = 0; i < tfData.locName.size(); i++) {
	            travelMainLines.add(utl.ljust(tfData.locName.get(i), 15, " ") + 
	                               utl.ljust(tfData.condition.get(i), 10, " ") + 
	                               utl.rjust(tfData.low.get(i), 3, " ") + 
	                               utl.rjust(tfData.high.get(i), 4, " "));
	        }
	        travelDataInit = true;
	        travelOffset = 0;
	    }

	    float startY = 380 + travelOffset;
	    int poolIdx = 0;

	    for (int row = 0; row < travelMainLines.size(); row++) {
	    	int width = row == 0 ? 22 : 18;
	    	int spacingX = row == 0 ? 23 : 17;
	        String line = travelMainLines.get(row);
	        float rowY = startY + (row * 60);

	        if (rowY < -50 && rowY > 381) continue;

	        int charX = 81;
	        for (int i = 0; i < line.length(); i++) {
	            if (poolIdx >= tfPool.length) break;
	            
	            if (rowY > -50 && rowY < 381) {
	            	
	            	char c = line.charAt(i);
	            
	            	GameObject go = tfPool[poolIdx++];
	            	go.transform.position.set(charX, rowY);
	            	go.transform.scale.set(width, 47);
	            	
	            	SpriteRenderer sr = go.getComponent(SpriteRenderer.class);
	            	sr.setSprite(sprites.getSprite(utl.getMainChar(c)));
	            	
	            	this.addGameObjectToScene(go);
	            	
	            	if (i % 2 != 0) charX++;
	            	charX += spacingX;
	            }
	        }
	    }
	    
	    this.addGameObjectToScene(tBanner);
	}
	
	public void bulletin() {
	   bulletinActive = true;

	    if (!bulletinDataInit) {
	    	buData = Main.bulletin;
	    	//try {
			//	bulletinBg.addComponent(new SpriteRenderer(utl.getBulletinColor(buData.bulletinText.get(0))));
			//} catch (IOException e) {
			//	bulletinBg.addComponent(new SpriteRenderer(new Vector4f(0.5f, 0.5f, 0.5f, 1.0f)));
			//	e.printStackTrace();
			//}
	    	
	        bulletinMainLines.clear();
	        
	        for (int i = 1; i < buData.bulletinText.size(); i++) {
	        	bulletinMainLines.add(buData.bulletinText.get(i));
	        }
	        
	        bulletinDataInit = true;
	        bulletinOffset = 0;
	        
	        float rows = (float) buData.bulletinText.size()-7;
			bulletinLength = 764.0f + ((40.0f * rows)*2) + 2.0f;//length is an issue
	    }
	    
	    this.addGameObjectToScene(bulletinBg);

	    float startY = 382 + bulletinOffset;
	    int poolIdx = 0;

	    for (int row = 0; row < bulletinMainLines.size(); row++) {
	        String line = bulletinMainLines.get(row);
	        float rowY = startY + (row * 40);

	        if (rowY < 0 && rowY > 383) continue;

	        int charX = 81;
	        for (int i = 0; i < line.length(); i++) {
	            if (poolIdx >= buPool.size()) break;
	            
	            if (rowY > 0 && rowY < 383) {
	            	
	            	char c = line.charAt(i);
	            
	            	GameObject go = buPool.get(poolIdx++);
	            	go.transform.position.set(charX, rowY);
	            	go.transform.scale.set(18, 32);
	            	
	            	SpriteRenderer sr = go.getComponent(SpriteRenderer.class);
	            	sr.setSprite(sprites.getSprite(utl.getMainChar(c)));
	            	
	            	this.addGameObjectToScene(go);
	            	
	            	if (i % 2 != 0) charX++;
	            	charX += 17;
	            }
	        }
	    }
	    
	    this.addGameObjectToScene(bulletinBanner);
	}
	
	public void slideChooser() {
		//draw slide
		String slide = slideLineup[slideIdx];
		//System.out.println(slide);
		switch (slide) {
		case "currentConditions":
			currentConditions();
			break;
		case "hourlyObservations":
			hourlyObservations();
			break;
		case "regionalConditions":
			regionalConditions();
			break;
		case "localForecast":
			localForecast();
			break;
		case "almanac":
			almanac();
			break;
		case "regionalForecast":
			regionalForecast();
			break;
		case "extendedForecast":
			extendedForecast();
			break;
		case "outlook":
			outlook();
			break;
		case "travelForecast":
			travelForecast();
			break;
		default:
			System.out.println("Invalid product");
			System.exit(10);
			audioManager.close();
			break;
		}
	}
}
