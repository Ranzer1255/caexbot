package caexbot.functions.music;

import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration.ResamplingQuality;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import caexbot.util.Logging;
import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.entities.Guild;

public class GuildPlayer extends AudioEventAdapter implements AudioSendHandler{
	
	private static AudioPlayerManager pm = intPlayerManager();

	private Guild guild;
	private TrackQueue queue;
	private TrackLoader loader;
	private AudioPlayer player;
	
	public GuildPlayer(Guild guild) {
		player = pm.createPlayer();
		player.addListener(this);
		queue = new TrackQueue();
		loader = new TrackLoader(queue,getPlayerManager());
		this.guild= guild;
		guild.getAudioManager().setSendingHandler(this);
		
	}
	
	//construction helper methods
	private static AudioPlayerManager intPlayerManager() {
		Logging.info("creating PlayerManager");
		AudioPlayerManager rtn = new DefaultAudioPlayerManager();
		rtn.getConfiguration().setResamplingQuality(ResamplingQuality.LOW);
		rtn.registerSourceManager(new YoutubeAudioSourceManager());
		rtn.registerSourceManager(new SoundCloudAudioSourceManager());
		return rtn;
	}
	
	//static getter method
	public static AudioPlayerManager getPlayerManager(){
		return pm;
	}

	//music controls
	/**
	 * Start first song in queue
	 */
	public void play(){
		//TODO play
	}
	
	
	
	//AudioSendHandler methods
	@Override
	public boolean canProvide() {
		// TODO make canProvide
		return false;
	}

	@Override
	public byte[] provide20MsAudio() {
		// TODO make provide20MsAudio
		return null;
	}
	
	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
	    // TODO onTrackEnd play next in queue
	  }
}
