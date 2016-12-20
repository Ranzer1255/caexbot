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

import caexbot.commands.search.YouTubeSearcher;
import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;

/*
 * TODO make commands listeners to this.
 * this will throw events when tracks change and what not. the listener will handle sending messages
 * to discord. 
 */
import net.dv8tion.jda.core.managers.AudioManager;
public class GuildPlayer extends AudioEventAdapter implements AudioSendHandler{
	
	private AudioPlayerManager pm;
	private AudioManager guildAM;
	private TrackQueue queue;
	private TrackLoader loader;
	private AudioPlayer player;
	private AudioFrame lastFrame;
	private boolean loading;
	
	public GuildPlayer(AudioPlayerManager pm, Guild guild) {
		this.pm = pm;
		player = this.pm.createPlayer();
		player.addListener(this);
		queue = new TrackQueue();
		loader = new TrackLoader(queue,this.pm);
		guildAM = guild.getAudioManager();
		guildAM.setSendingHandler(this);
		
	}
	
	public boolean isConnected(){
		return guildAM.isConnected();
	}

	public void join(VoiceChannel channel){
		guildAM.openAudioConnection(channel);
	}
	
	/**
	 * searches Youtube for a song and adds it to the queue
	 * @param song
	 */
	public void queue(String song){
		
		YouTubeSearcher yts = new YouTubeSearcher();
		String videoID = yts.searchForVideo(song);
		
		System.out.println(videoID);
		loading = true;
		pm.loadItem(videoID, loader);
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
		while(loading){try {Thread.sleep(5);} catch (InterruptedException e) {}};
		
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
		queue.clear();
		guildAM.closeAudioConnection();
		
	}
	
	public void vol(int vol){
		player.setVolume(vol);
	}

	public void pause() {
		player.setPaused(!player.isPaused());
		
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
	
	/**
	 * Loads tracks into the queue for the guild
	 * 
	 * @author Ranzer
	 *
	 */
	private class TrackLoader implements AudioLoadResultHandler {

		TrackQueue queue;
		
		public TrackLoader(TrackQueue queue, AudioPlayerManager playerManager) {
			this.queue = queue;
		}

		@Override
		public void trackLoaded(AudioTrack track) {
			//TODO track added event
			queue.add(track);
			loading = false;
			
		}

		@Override
		public void playlistLoaded(AudioPlaylist playlist) {
			//TODO playlist added event

			for (AudioTrack track : playlist.getTracks()) {
				queue.add(track);
			}
			loading = false;
			
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
		
		public void clear() {
			queue.clear();
			
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

	//this isn't used at this time
	public enum EventType {
		QUEUED, PAUSED, SKIPPED, VOLUME, STOPPED, JOIN
	}
}
