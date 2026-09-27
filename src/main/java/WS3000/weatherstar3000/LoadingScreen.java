package WS3000.weatherstar3000;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector4f;

import components.SpriteRenderer;
import components.Spritesheet;
import util.AssetPool;
import util.Utilities;

public class LoadingScreen extends Scene {
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
	Vector4f color16 = new Vector4f(188.0f/255.0f, 190.0f/255.0f, 189.0f/255.0f, 1.0f); //white
	Vector4f greenScreen = new Vector4f(0.0f/255.0f, 255.0f/255.0f, 0.0f/255.0f, 1.0f);
	
	Utilities utl = new Utilities();
	Spritesheet sprites;
	boolean dataInit = false;
	static boolean dataFinish = false;
	
	public LoadingScreen() {
	}
	@Override
	public void init() {
		System.out.println("start loading screen");
		
		loadResources();
		this.camera = new Camera(new Vector2f(0, 0));
		
		sprites = AssetPool.getSpritesheet("/resources/images/charMap.png");
		
		ArrayList<String> lines = new ArrayList<>();
		GameObject[] lsPool = new GameObject[256];
		
		for (int i = 0; i < lsPool.length; i++) {
	    	lsPool[i] = new GameObject("ccPool" + i, new Transform(new Vector2f()), 0);
	    	lsPool[i].addComponent(new SpriteRenderer(new Vector4f(1,1,1,1)));
	    }
		
		lines.add("LOADING DATA...");
		lines.add(" ");
		lines.add("MADE WITH JAVA, LWJGL, OPENGL,");
		lines.add("AND JLAYER BY JAVAZOOM");
		lines.add("BROUGHT TO YOU BY");
		lines.add("JOE MOLINELLI");
		lines.add("AND MIST WEATHER MEDIA");
		
		int textX = 81;
        int textY = 44;
        int poolIdx = 0;
        for (int row = 0; row < lines.size(); row++) {
        	int lineHeight = 40;
        	String line = lines.get(row);
        	for (int i = 0; i < line.length(); i ++) {
        		if (poolIdx >= lsPool.length) break;
        		
        		char c = line.charAt(i);
        		
        		GameObject go = lsPool[poolIdx++];
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
	private void loadResources() {
		AssetPool.getShader("/resources/shaders/default.glsl");
		AssetPool.addSpritesheet("/resources/images/charMap.png", new Spritesheet(AssetPool.getTexture("/resources/images/charMap.png"),32, 32, 128, 0));
	}
	
	@Override
	public void update(float dt) {
		if (!dataInit) {
			dataInit = true;
			DataRunner data = new DataRunner();
			data.start();
		}
		
		for (GameObject go : this.gameObjects) {
			go.update(dt);
		}
		
		this.renderer.render();
		if (dataFinish) {
			Window.finishInit();
		}
	}

}
