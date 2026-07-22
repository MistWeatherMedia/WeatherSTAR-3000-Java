package renderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import WS3000.weatherstar3000.GameObject;
import components.SpriteRenderer;

public class Renderer {
	private final int MAX_BATCH_SIZE = 2500;
	private List<RenderBatch> batches;
	
	public Renderer() {
		this.batches = new ArrayList<>();
		
	}
	
	public void add(GameObject go) {
		SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
		if (spr != null) {
			add(spr);
		}
	}
	
	private void add(SpriteRenderer sprite) {
	    boolean added = false;
	    for (RenderBatch batch : batches) {
	        if (batch.hasRoom() && batch.zIndex() == sprite.gameObject.zIndex()) {
	            Texture tex = sprite.getTexture();
	            if (tex == null || (batch.hasTexture(tex) || batch.hasTextureRoom())) {
	                batch.addSprite(sprite);
	                added = true;
	                break;
	            }
	        }
	    }
	    
	    if (!added) {
	        RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.zIndex());
	        newBatch.start();
	        batches.add(newBatch);
	        newBatch.addSprite(sprite);
	        Collections.sort(batches);
	    }
	}
	
	public void render() {
		for (RenderBatch batch : batches) {
			batch.render();
		}
	}
	
	public void clear() {
	    for (RenderBatch batch : batches) {
	        batch.clear(); // Resets numSprites to 0
	    }
	    //this.batches.clear();
	}
}
