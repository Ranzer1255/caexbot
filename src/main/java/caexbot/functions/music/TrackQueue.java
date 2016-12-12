package caexbot.functions.music;

import java.util.LinkedList;
import java.util.Queue;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

/**
 * handles queuing of tracks to be played
 * 
 * @author Ranzer
 *
 */
public class TrackQueue {

	Queue<AudioTrack> queue = new LinkedList<>();
	/**
	 * adds one track to the queue to be played.
	 * 
	 * @param track track to be added to the queue
	 */
	public void add(AudioTrack track){
		queue.add(track);
	}//TODO
	
	/**
	 * 
	 * @return next track in the queue using FIFO order
	 */
	public AudioTrack remove(){return queue.remove();} //TODO

	public boolean isEmpty() {
		// TODO make isEmpty
		return queue.isEmpty();
	}
}
