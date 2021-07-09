package net.ranzer.caexbot.functions.music.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.ranzer.caexbot.functions.music.GuildPlayerManager;
import net.ranzer.caexbot.functions.music.MusicListener;
import net.ranzer.caexbot.util.StringUtil;

import java.util.Arrays;
import java.util.List;

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
		eb.setDescription(StringUtil.playingBar(playing.getPosition(), playing.getDuration()));
		eb.setFooter("by "+playing.getInfo().author,null);
		
		event.getChannel().sendMessageEmbeds(eb.build()).queue(message ->
				GuildPlayerManager.getPlayer(event.getGuild()).getMusicListener()
						.setNowPlayingMessage(message, MusicListener.PLAYING_BUTTONS));
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
