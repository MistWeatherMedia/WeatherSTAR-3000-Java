package WS3000.weatherstar3000;

public abstract class Component {

    public GameObject gameObject = null;

    public void start() {

    }

    public abstract void update(float dt);
}