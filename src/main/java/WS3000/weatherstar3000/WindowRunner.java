package WS3000.weatherstar3000;

public class WindowRunner extends Thread {
	public void run() {
		Main.settingsWindow.setVisible(false);
		Window window = Window.get();
		window.runWindow();
	}
}
