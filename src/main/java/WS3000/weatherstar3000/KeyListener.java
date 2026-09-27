package WS3000.weatherstar3000;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
	private static KeyListener instance;
	private boolean keyPressed[] = new boolean[350];
	
	private KeyListener() {
		
	}
	
	public static KeyListener get() {
		if (KeyListener.instance == null) {
			KeyListener.instance = new KeyListener();
		}
		
		return KeyListener.instance;
	}
	
	public static void keyCallback(long window, int key, int scancode, int action, int mods) {
		if (key >= 0 && key <= 350) {
			if (action == GLFW_PRESS) {
				get().keyPressed[key] = true;
			} else if (action == GLFW_RELEASE) {
				get().keyPressed[key] = false;
			}
		}
		return;
	}
	
	public static boolean isKeyPressed(int keyCode) {
		if (keyCode >= 0 && keyCode <= 350) {
			return get().keyPressed[keyCode];
		} else {
			return false;
		}
	}
}
