package renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.ARBTextureRectangle.*;

import static org.lwjgl.stb.STBImage.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.*;

import org.lwjgl.BufferUtils;

public class Texture {
	private String filepath;
	private int texID;
	private int width, height;
	
	public Texture() {
		texID = -1;
		width = -1;
		height = -1;
	}
	
	public Texture(int width, int height) {
		this.filepath = "Generated";
		
		texID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texID);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);
	}
	
	public Texture(String filepath) {
		this.filepath = filepath;
		
		texID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texID);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer channelsBuffer = BufferUtils.createIntBuffer(1);
		
		stbi_set_flip_vertically_on_load(true);

		// Ensure the path starts with a leading slash for classpath lookup
		String resourcePath = filepath.startsWith("/") ? filepath : "/" + filepath;
		ByteBuffer image = null;

		try (InputStream is = Texture.class.getResourceAsStream(resourcePath)) {
			if (is == null) {
				throw new IOException("Texture file not found inside JAR: " + resourcePath);
			}

			// Read bytes from the JAR stream into a native ByteBuffer
			byte[] bytes = is.readAllBytes();
			ByteBuffer nativeBuffer = BufferUtils.createByteBuffer(bytes.length);
			nativeBuffer.put(bytes);
			nativeBuffer.flip();

			// Tell STB to decode the image from raw memory instead of a disk path
			image = stbi_load_from_memory(nativeBuffer, widthBuffer, heightBuffer, channelsBuffer, 0);
		} catch (IOException e) {
			e.printStackTrace();
			assert false : "ERROR: Could not read stream for texture: " + filepath;
		}
		
		if (image != null) {
			this.width = widthBuffer.get(0);
			this.height = heightBuffer.get(0);
			int channels = channelsBuffer.get(0);
			
			if (channels == 3) {
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, this.width, this.height, 0, GL_RGB, GL_UNSIGNED_BYTE, image);
			} else if (channels == 4) {
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
			} else {
				stbi_image_free(image);
				assert false : "ERROR: (Texture) Unknown number of channels " + channels + ", " + filepath;
			}
			stbi_image_free(image);
		} else {
			assert false : "ERROR: STB failed to decode image " + filepath + " Reason: " + stbi_failure_reason();
		}
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, texID);
	}
	
	public void unBind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public String getFilePath() {
		return this.filepath;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getId() {
		return this.texID;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (!(o instanceof Texture)) return false;
		Texture oTex = (Texture) o;
		return oTex.getWidth() == this.width && oTex.getHeight() == this.height && oTex.getId() == this.texID && oTex.getFilePath().equals(this.filepath);
	}
}
