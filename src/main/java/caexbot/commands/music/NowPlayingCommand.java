package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import caexbot.functions.music.GuildPlayerManager;
import caexbot.util.StringUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class NowPlayingCommand extends AbstractMusicCommand{

	@Override
	public void process(String[] args, MessageReceivedEvent event) {
		if(!GuildPlayerManager.getPlayer(event.getGuild()).isPlaying()) return;//ignore command if not playing
		
		AudioTrack playing = GuildPlayerManager.getPlayer(event.getGuild()).getPlayingTrack();
		
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setAuthor("Now Playing");
		eb.setTitle(
			String.format("%s\n",
				playing.getInfo().title
			),
			playing.getInfo().uri
		);
		eb.setDescription(playingBar(playing));
		eb.setFooter("by "+playing.getInfo().author,null);
		
		event.getChannel().sendMessage(eb.build()).queue();
	}

	private String playingBar(AudioTrack playing) {
		StringBuilder sb = new StringBuilder();
		long currentTime = playing.getPosition();
		long length = playing.getDuration();
		int barLength = 32;
		
		long timePerBar = length/barLength;
		
		sb.append("```\n");
		sb.append("-");
		for (int i = 0; i < barLength; i++) {
			sb.append("-");
		}
		sb.append("-\n");
		sb.append("|");
		
		for (int i = 0; i < currentTime/timePerBar; i++) {
			sb.append("=");
		}
		sb.append("|");
		for (int i = 0; i < barLength-1-(currentTime/timePerBar); i++) {
			sb.append(" ");			
		}
		
		sb.append("|\n");
		sb.append("-");
		for (int i = 0; i < barLength; i++) {
			sb.append("-");
		}
		sb.append("-\n");
		sb.append("```\n");
		sb.append(StringUtil.calcTime(currentTime/1000) 
				+ " of " 
				+ StringUtil.calcTime(length/1000));
		
		return sb.toString();
	}

	@Override
	public String getLongDescription() {
		return super.getLongDescription()
				+ "also shows a neat graphical representation of time remaining";
	}
	@Override
	public String getShortDescription() {
		return "Shows the current playing track and time remaining";
	}
	@Override
	public List<String> getAlias() {
		return Arrays.asList("playing","np","now-playing");
	}

}
