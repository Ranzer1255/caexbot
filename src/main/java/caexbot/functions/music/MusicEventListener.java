package caexbot.functions.music;

import caexbot.functions.music.events.MusicEvent;

public interface MusicEventListener {

	void handleEvent(MusicEvent event);
}
