package WS3000.weatherstar3000;

import javazoom.jl.player.Player;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

public class AudioManager extends Thread {
	private Player player;
	private List<File> playlist;
	private volatile boolean finished;
	
	public AudioManager(String folder) {
		this.player = null;
		this.finished = true;
		
		File musicFolder = new File(folder);
		
		if (!musicFolder.exists() || !musicFolder.isDirectory()) {
			System.err.println("\"" + folder + "\" folder not found.");
			return;
		}
		
		this.playlist = Arrays.asList(musicFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3")));
		
		for (int i = 0; i < this.playlist.size(); i++) {
			//System.out.println(getSong(i));
		}
		
	}
	
	public List<File> getPlayList() {
		return this.playlist;
	}
	
	public File getSong(int index) {
		return this.playlist.get(index);
	}
	
	private void playSong(File file) {
		try (FileInputStream fis = new FileInputStream(file)) {
            player = new Player(fis);
            player.play(); 
            
        } catch (Exception e) {
            System.err.println("Error playing file " + file.getName() + ": " + e.getMessage());
            return;
        }
	}
	
	public void close() {
		this.finished = true;
		if (player != null) {
            player.close();
        }
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public void startPlayList() {
		finished = false;
		while (!this.finished && playlist != null && !playlist.isEmpty()) {
			for (int i = 0; i < this.playlist.size(); i++) {
				if (this.finished) {
					break;
					
				}
				//System.out.println("Playing " + this.playlist.get(i));
				playSong(this.playlist.get(i));
			}
		}
		
		if (player != null) {
            player.close();
        }
		
		finished = true;
	}
	
	public void run() {
		startPlayList();
	}
}
