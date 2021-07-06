package net.ranzer.caexbot.functions.music;

import java.util.HashMap;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration.ResamplingQuality;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;

import net.ranzer.caexbot.util.Logging;
import net.dv8tion.jda.api.entities.Guild;

public class GuildPlayerManager {

	private static Map<Guild, GuildPlayer> players = new HashMap<>();
	private static AudioPlayerManager pm = intPlayerManager();
	
	public static GuildPlayer getPlayer(Guild k){
		GuildPlayer rtn = players.get(k);
		if(rtn == null){
			rtn = new GuildPlayer(pm, k);
			players.put(k, rtn);
		}
		return rtn;
	}

	//construction helper methods
	private static AudioPlayerManager intPlayerManager() {
		Logging.info("creating PlayerManager");
		AudioPlayerManager rtn = new DefaultAudioPlayerManager();
		rtn.getConfiguration().setResamplingQuality(ResamplingQuality.HIGH);
		rtn.registerSourceManager(new YoutubeAudioSourceManager());
		return rtn;
	}
}
