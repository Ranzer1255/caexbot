package caexbot.functions.music.events;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class MusicSkipEvent extends MusicEvent {

	private AudioTrack skippedTrack;
	public MusicSkipEvent(AudioTrack track) {
		skippedTrack = track;
	}
	
	public AudioTrack getSkippedTrack() {
		return skippedTrack;
	}
}
