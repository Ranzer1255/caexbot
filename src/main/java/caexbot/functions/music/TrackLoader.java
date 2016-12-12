package caexbot.functions.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class TrackLoader implements AudioLoadResultHandler {

	TrackQueue queue;
	
	public TrackLoader(TrackQueue queue, AudioPlayerManager playerManager) {
		this.queue = queue;
	}

	@Override
	public void trackLoaded(AudioTrack track) {
		System.out.println("adding song to queue");
		queue.add(track);
		
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
