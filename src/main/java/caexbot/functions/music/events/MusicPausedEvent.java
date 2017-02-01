package caexbot.functions.music.events;

public class MusicPausedEvent extends MusicEvent {

	private boolean paused;
	
	public MusicPausedEvent(boolean paused) {
		this.paused=paused;
	}
	
	public boolean getPaused(){
		return paused;
	}
}
