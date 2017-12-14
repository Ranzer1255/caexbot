package caexbot.functions.music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
import caexbot.functions.music.events.LoadFailedEvent;
import caexbot.functions.music.events.MusicEvent;
import caexbot.functions.music.events.MusicJoinEvent;
import caexbot.functions.music.events.MusicLoadEvent;
import caexbot.functions.music.events.MusicPausedEvent;
import caexbot.functions.music.events.MusicSkipEvent;
import caexbot.functions.music.events.MusicStartEvent;
import caexbot.functions.music.events.NoMatchEvent;
import caexbot.functions.music.events.PlaylistLoadEvent;
import caexbot.functions.music.events.ShuffleEvent;
import caexbot.functions.music.events.VolumeChangeEvent;
import caexbot.util.Logging;
import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;

public class GuildPlayer extends AudioEventAdapter implements AudioSendHandler {

	private AudioPlayerManager pm;
	private AudioManager guildAM;
	private TrackQueue queue;
	private TrackLoader loader;
	private AudioPlayer player;
	private AudioFrame lastFrame;
	private boolean loading;
	private boolean insertFlag;

	private List<MusicEventListener> listeners = new ArrayList<>();
	private MusicListener musicListener;//This may move or become a different implementation

	public GuildPlayer(AudioPlayerManager pm, Guild guild) {
		this.pm = pm;
		player = this.pm.createPlayer();
		player.addListener(this);
		queue = new TrackQueue();
		loader = new TrackLoader(queue, this.pm);
		guildAM = guild.getAudioManager();
		guildAM.setSendingHandler(this);
		musicListener = new MusicListener(guild);
		addListener(musicListener);

	}

	public void addListener(MusicEventListener listener) {

		listeners.add(listener);
	}

	public MusicListener getMusicListener() {
		return musicListener;
	}

	private void notifyOfEvent(MusicEvent event) {

		for (MusicEventListener l : listeners) {
			l.handleEvent(event);
		}

	}

	public boolean isConnected() {
		return guildAM.isConnected();
	}

	public void join(VoiceChannel channel) {
		guildAM.openAudioConnection(channel);
		notifyOfEvent(new MusicJoinEvent(channel));
	}

	/**
	 * searches Youtube for a song and adds it to the queue
	 * 
	 * @param search
	 */
	public void queueSearch(String search) {

		YouTubeSearcher yts = new YouTubeSearcher();
		String videoID = yts.searchForVideo(search);

		queueID(videoID);
	}

	public void queueID(String songID) {
		System.out.println(songID);
		loading = true;
		pm.loadItem(songID, loader);
	}
	
	public void insertSearch(String search){
		insertFlag = true;
		queueSearch(search);
	}
	
	public void insertID(String songID){
		insertFlag = true;
		queueID(songID);
	}

	public TrackQueue getQueue() {
		return queue;
	}

	// music controls
	/**
	 * start the queue
	 */
	public void start() {
		// System.out.println(player.isPaused() +" : "+
		// player.getPlayingTrack().getInfo().title);
		if (player.isPaused()) {
			player.setPaused(false);
		}
		
		if (player.getPlayingTrack() == null) {
			playNext();
		}
	}

	/**
	 * Play the next song in the queue
	 */
	public void playNext() {
		while (loading) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {}
		}

		if (queue.isEmpty()) {
			stop(true);
			return;
		}
		player.playTrack(queue.remove());
		player.setPaused(false);

	}

	/**
	 * stop playing and end the current song
	 */
	public void stop(boolean clearQueue) {
		player.setPaused(true);
		player.stopTrack();
		if(clearQueue) clearQueue();
		close();
	}
	
	public void clearQueue(){
		queue.clear();		
	}
	
	public void close(){
		
		new Thread(){
			public void run() {
				guildAM.closeAudioConnection();
				interrupt();
			};
		}.start();
	}

	public void vol() {
		notifyOfEvent(new VolumeChangeEvent(player.getVolume()));
	}
	
	public void vol(int vol) {
		player.setVolume(vol);
		notifyOfEvent(new VolumeChangeEvent(player.getVolume()));
	}

	public void pause() {
		notifyOfEvent(new MusicPausedEvent(player.isPaused()));
		player.setPaused(!player.isPaused());

	}

	public void shuffle() {
		notifyOfEvent(new ShuffleEvent());
		queue.shuffle();
		
	}

	public boolean isPlaying() {
		return !player.isPaused();
	}

	// AudioSendHandler methods
	@Override
	public boolean canProvide() {
		lastFrame = player.provide();
		return lastFrame != null;
	}

	@Override
	public byte[] provide20MsAudio() {
		return lastFrame.data;
	}

	@Override
	public boolean isOpus() {
		return true;
	}

	// AudioEventHandler methods
	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (endReason == AudioTrackEndReason.FINISHED||endReason == AudioTrackEndReason.LOAD_FAILED)
			playNext();
		if (endReason == AudioTrackEndReason.REPLACED)
			notifyOfEvent(new MusicSkipEvent(track));
	}
	
	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		notifyOfEvent(new MusicStartEvent(track));
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
			if(insertFlag){
				queue.insert(track);
				insertFlag=false;
			} else {
				queue.add(track);
			}
			notifyOfEvent(new MusicLoadEvent(track));
			loading = false;

		}

		@Override
		public void playlistLoaded(AudioPlaylist playlist) {
			notifyOfEvent(new PlaylistLoadEvent(playlist));
			
			for (AudioTrack track : playlist.getTracks()) {
				queue.add(track);
			}
			loading = false;

		}

		@Override
		public void noMatches() {
			Logging.debug("No match found in search");
			notifyOfEvent(new NoMatchEvent());

		}

		@Override
		public void loadFailed(FriendlyException exception) {
			Logging.debug(exception.getMessage());
			notifyOfEvent(new LoadFailedEvent(exception));
			
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

		public List<AudioTrack> getQueue() {
			return (LinkedList<AudioTrack>) queue;
		}

		public void shuffle() {
			Collections.shuffle((List<?>) queue);
			
		}

		/**
		 * adds one track to the queue to be played.
		 * 
		 * @param track
		 *            track to be added to the queue
		 */
		public void add(AudioTrack track) {
			queue.add(track);
		}
		
		/**
		 * inserts track at the head of the queue
		 * 
		 * @param track
		 * 			track to be added
		 */
		public void insert(AudioTrack track){
			((LinkedList<AudioTrack>) queue).addFirst(track);
		}
		
		/**
		 * clears the queue
		 */
		public void clear() {
			queue.clear();

		}

		/**
		 * 
		 * @return next track in the queue using FIFO order
		 */
		public AudioTrack remove() {
			return queue.remove();
		}

		public boolean isEmpty() {
			Logging.debug(String.format("value of queue.isEmpty(): %s", queue.isEmpty()));
			return queue.isEmpty();
		}
	}

	public AudioTrack getPlayingTrack() {
		return player.getPlayingTrack();

	}
}
