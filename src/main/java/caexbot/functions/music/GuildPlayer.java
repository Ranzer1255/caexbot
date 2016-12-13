package caexbot.functions.music;

import java.util.LinkedList;
import java.util.Queue;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.entities.Guild;
/*
 * TODO make commands listeners to this.
 * this will throw events when tracks change and what not. the listener will handle sending messages
 * to discord. 
 */
public class GuildPlayer extends AudioEventAdapter implements AudioSendHandler{
	
	private AudioPlayerManager pm;
	private Guild guild;
	private TrackQueue queue;
	private TrackLoader loader;
	private AudioPlayer player;
	private AudioFrame lastFrame;
	
	public GuildPlayer(AudioPlayerManager pm, Guild guild) {
		this.pm = pm;
		player = this.pm.createPlayer();
		player.addListener(this);
		queue = new TrackQueue();
		loader = new TrackLoader(queue,this.pm);
		this.guild= guild;
		guild.getAudioManager().setSendingHandler(this);
		
	}

	public void queue(String song){
		System.out.println(song);
		pm.loadItem(song, loader);
	}

	public TrackQueue getQueue() {
		// TODO make getQueue
		return queue;
	}

	//music controls
	/**
	 * start the queue
	 */
	public void start() {
		//TODO player started event
		player.setPaused(false);
		playNext();
	}
	
	/**
	 * Play the next song in the queue
	 */
	public void playNext(){
		//TODO next song event
		if(queue.isEmpty()){
			stop();
			return;
		}
		
		player.playTrack(queue.remove());
		
		
	}

	/**
	 * stop playing and end the current song
	 */
	public void stop() {
		//TODO player stopped event
		player.setPaused(true);
		player.stopTrack();
		guild.getAudioManager().closeAudioConnection();
		
	}

	//AudioSendHandler methods
	@Override
	public boolean canProvide() {
		lastFrame = player.provide();
		return lastFrame!=null;
	}

	@Override
	public byte[] provide20MsAudio() {
		 return lastFrame.data;
	}
	
	@Override
	public boolean isOpus(){
		return true;
	}
	
	//AudioEventHandler methods
	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
	    playNext();
	  }
	
	private class TrackLoader implements AudioLoadResultHandler {

		TrackQueue queue;
		
		public TrackLoader(TrackQueue queue, AudioPlayerManager playerManager) {
			this.queue = queue;
		}

		@Override
		public void trackLoaded(AudioTrack track) {
			//TODO track added event
			queue.add(track);
			
		}

		@Override
		public void playlistLoaded(AudioPlaylist playlist) {
			//TODO playlist added event

			for (AudioTrack track : playlist.getTracks()) {
				queue.add(track);
			}
			
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
		}
		
		/**
		 * 
		 * @return next track in the queue using FIFO order
		 */
		public AudioTrack remove(){
			return queue.remove();
		} 

		public boolean isEmpty() {
			return queue.isEmpty();
		}
	}
}
