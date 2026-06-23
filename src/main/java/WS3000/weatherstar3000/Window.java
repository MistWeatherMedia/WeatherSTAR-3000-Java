package WS3000.weatherstar3000;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.*;

import renderer.FrameBuffer;

public class Window {
	static int secFrameCount;
	static int fourSecFrameCount;
	static int slidesSecFrameCount;
	static int crawlSecFrameCount;
	static int bTimerSecFrameCount;
	static int bSlideSecFrameCount;
	
	int width, height;
	String title;
	
	private FrameBuffer framebuffer;
	
	private static long glfwWindow;
	
	public float r, g, b, a;
	
	private static boolean fullscreen;
	
	private static Window window;
	
	private static Scene currentScene;
	
	private Window(int width, int height, boolean fullscreen) {
		this.width = width;
		this.height = height;
		this.title = "WeatherSTAR 3000";
		this.fullscreen = fullscreen;
		r = 0.0f;
		g = 0.0f;
		b = 0.0f;
		a = 1.0f;
	}
	public static Window get() {
		if (Window.window == null) {
			Window.window = new Window(Main.simWidth, Main.simHeight, Main.fullscreen);
		}
		
		return Window.window;
	}
	public static long getGLFWWindow() {
		return glfwWindow;
	}
	
	public static Scene getScene() {
		return get().currentScene;
	}
	
	public void runWindow() {
		System.out.println("LWJGL" + Version.getVersion());
		
		init();
		loop();
		
		glfwFreeCallbacks(glfwWindow);
		glfwDestroyWindow(glfwWindow);
		
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		
		GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		
		PointerBuffer monitors = glfwGetMonitors();
        
        if (!monitors.equals(NULL)) {
            for (int i = 0; i < monitors.limit(); i++) {
                long monitorHandle = monitors.get(i);
                // Get the human-readable name of the monitor
                String monitorName = glfwGetMonitorName(monitorHandle);
                
                System.out.println("Monitor " + i + ": " + monitorName);
            }
        } else {
            System.err.println("No monitors found!");
        }
		
		if (this.fullscreen) {
			glfwWindow = glfwCreateWindow(vidMode.width(), vidMode.height(), "WeatherSTAR 3000", GLFW.glfwGetPrimaryMonitor(), NULL);
		} else {
			glfwWindow = glfwCreateWindow(this.width, this.height, "WeatherSTAR 3000", NULL, NULL);
		}
		
		if (glfwWindow == NULL) {
			throw new IllegalStateException("Failed to create GLFW window");
		}
		
		glfwSetFramebufferSizeCallback(glfwWindow, (windowHandle, newWidth, newHeight) -> {
		    this.width = newWidth;
		    this.height = newHeight;
		    glViewport(0, 0, newWidth, newHeight);
		});
		
		glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
		
		glfwMakeContextCurrent(glfwWindow);
		
		glfwSwapInterval(1);
		
		glfwShowWindow(glfwWindow);
		
		GL.createCapabilities();
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
		
		//this.framebuffer = new FrameBuffer(this.width, this.height);
		
		currentScene = new LoadingScreen();
		currentScene.init();
		currentScene.start();
	}
	
	public void loop() {
		float beginTime = (float)glfwGetTime();
		float endTime;
		float dt = 0.0f;
		secFrameCount = 0;
		System.out.println("start loop");
		while(!glfwWindowShouldClose(glfwWindow)) {
			glfwPollEvents();
			
			glClearColor(r, g, b, a);
			glClear(GL_COLOR_BUFFER_BIT);
			
			if (KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {
				System.exit(0);
			}
			
			if (dt >= 0) {
				currentScene.update(dt);
			}
			
			glfwSwapBuffers(glfwWindow);
			
			endTime = (float)glfwGetTime();
			
			if (SlidesRunner.slidesStarted) {
				secFrameCount++;
				fourSecFrameCount++;
				slidesSecFrameCount++;
				if (!SlidesRunner.bulletinActive) {
					bTimerSecFrameCount++;
				}
				
				if (SlidesRunner.adCrawlActive) {
					crawlSecFrameCount++;
				}
			}
			
			dt = endTime - beginTime;
			//System.out.println(1.0f/dt + " fps");
			beginTime = endTime;
		}
		
		if (SlidesRunner.audioManager != null) {
			SlidesRunner.audioManager.close();
		}
		System.exit(0);
	}
	
	static void finishInit() {
		currentScene = new SlidesRunner();
		currentScene.init();
		currentScene.start();
	}
}
