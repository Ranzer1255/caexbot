package caexbot.functions.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class TrackLoader implements AudioLoadResultHandler {

	public TrackLoader(TrackQueue queue) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void trackLoaded(AudioTrack track) {
		// TODO make trackLoaded
		
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		// TODO make playlistLoaded
		
	}

	@Override
	public void noMatches() {
		// TODO make noMatches
		
	}

	@Override
	public void loadFailed(FriendlyException exception) {
		// TODO make loadFailed
		
	}

}
