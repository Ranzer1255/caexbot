package caexbot.functions.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;

import net.dv8tion.jda.entities.Guild;

import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration.ResamplingQuality;
public class GuildPlayerManager {
	
	private AudioPlayerManager pm;

	private static Map<Guild, TrackQueue> queues;
	private static GuildPlayerManager instance;

	private GuildPlayerManager() {
		pm = new DefaultAudioPlayerManager();
		pm.getConfiguration().setResamplingQuality(ResamplingQuality.LOW);
		pm.registerSourceManager(new YoutubeAudioSourceManager());
		pm.registerSourceManager(new SoundCloudAudioSourceManager());
	}

	public static GuildPlayerManager getInstance() {
		if (instance == null)
			instance = new GuildPlayerManager();
		return instance;
	}

	public TrackQueue getQueue(Guild guild) {
		TrackQueue rtn = queues.get(guild);
		if (rtn == null){
			//TODO make queue
		}
		return rtn;
	}
	
	public AudioPlayerManager getPlayerManager(){
		return pm;
	}
	
}
