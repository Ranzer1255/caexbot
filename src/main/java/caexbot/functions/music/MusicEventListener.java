package caexbot.functions.music;

import caexbot.functions.music.events.MusicEvent;

public interface MusicEventListener {

	public void handleEvent(MusicEvent event);
}
