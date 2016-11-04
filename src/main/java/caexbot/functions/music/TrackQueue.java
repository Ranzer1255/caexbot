package caexbot.functions.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

/**
 * handles queuing of tracks to be played
 * 
 * @author Ranzer
 *
 */
public interface TrackQueue {

	/**
	 * adds one track to the queue to be played.
	 * 
	 * @param track track to be added to the queue
	 */
	public void add(AudioTrack track);
	
	/**
	 * 
	 * @return next track in the queue using FIFO order
	 */
	public AudioTrack remove();
}
