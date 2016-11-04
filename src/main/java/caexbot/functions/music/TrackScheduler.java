package caexbot.functions.music;

import java.util.LinkedList;
import java.util.Queue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

//TODO this might not be needed
public class TrackScheduler extends AudioEventAdapter {

	private Queue<AudioTrack> queue = new LinkedList<>();
	
	public void addTrack(AudioTrack track){
		queue.add(track);
	}

	public AudioTrack nextTrack(){
		return queue.remove();
	}

	@Override
	public void onPlayerPause(AudioPlayer player) {
		// Player was paused
	}

	@Override
	public void onPlayerResume(AudioPlayer player) {
		// Player was resumed
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		// A track started playing
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (endReason==AudioTrackEndReason.FINISHED){
			if(queue.isEmpty())
				return;
			player.playTrack(queue.remove());
			return;
		}
		
		// endReason == FINISHED: A track finished (or died by an exception) - just start the next one
		// endReason == STOPPED: The player was stopped - makes no sense to start the next track
		// endReason == REPLACED: Another track started playing while this had not finished, do nothing
		// endReason == CLEANUP: Player hasn't been queried for a while, if you want you can put a
		//                       clone of this back to your queue
	}

	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
		// An already playing track threw an exception (track end event will still be received separately)
	}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
		// Audio track has been unable to provide us any audio, might want to just start a new track
	}
}
