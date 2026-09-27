package components;

import WS3000.weatherstar3000.Component;

public class FontRenderer extends Component {
	
	
	@Override
	public void start() {
		if (gameObject.getComponent(SpriteRenderer.class) != null) {
			System.out.println("Found font renderer");
		}
	}
	
	@Override
	public void update(float dt) {
		
	}
	
}
