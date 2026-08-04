package util;
import renderer.Shader;
import renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import components.Spritesheet;

public class AssetPool {
	private static Map<String, Shader> shaders = new HashMap<>();
	private static Map<String, Texture> textures = new HashMap<>();
	private static Map<String, Spritesheet> spritesheets = new HashMap<>();
	
	public static Shader getShader(String resourceName) {
		if (AssetPool.shaders.containsKey(resourceName)) {
			return AssetPool.shaders.get(resourceName);
		} else {
			Shader shader = new Shader(resourceName);
			shader.compile();
			AssetPool.shaders.put(resourceName, shader);
			return shader;
		}
	}
	
	public static Texture getTexture(String resourceName) {
		if (AssetPool.textures.containsKey(resourceName)) {
			return AssetPool.textures.get(resourceName);
		} else {
			Texture texture = new Texture(resourceName);
			AssetPool.textures.put(resourceName, texture);
			return texture;
		}
	}
	
	public static void addSpritesheet(String resourceName, Spritesheet spritesheet) {
		if (!AssetPool.spritesheets.containsKey(resourceName)) {
			AssetPool.spritesheets.put(resourceName, spritesheet);
		}
	}
	
	public static Spritesheet getSpritesheet(String resourceName) {
		if (!AssetPool.spritesheets.containsKey(resourceName)) {
			assert false : "Error: Tried to access spritesheet '" + resourceName + "' and it has not been added to the asset pool.";
		}
		return AssetPool.spritesheets.getOrDefault(resourceName, null);
	}
}
