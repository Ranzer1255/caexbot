package net.ranzer.caexbot.commands.music;

import net.dv8tion.jda.api.entities.*;
import net.ranzer.caexbot.commands.CaexCommand;
import net.ranzer.caexbot.commands.Category;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.functions.music.GuildPlayerManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

public abstract class AbstractMusicCommand extends CaexCommand implements Describable{

	protected void setMusicChannel(TextChannel channel) {
		GuildPlayerManager.getPlayer(channel.getGuild()).getMusicListener().setMusicChannel(channel);
	}
	
	public Category getCategory(){
		return Category.MUSIC;
	}
	
	@Override
	public String getLongDescription() {
		return getShortDescription()+"\n\n";
	}
	
	@Override
	public String getUsage(Guild g) {
		return String.format("`%smusic %s`", getPrefix(g), getName());
	}
	
	@Override
	public boolean isApplicableToPM() {
		return false;
	}

	protected boolean notInSameVoiceChannel(MessageReceivedEvent event) {

		VoiceChannel requesterChannel = getVoiceChannel(Objects.requireNonNull(event.getMember()));
		VoiceChannel botChannel = getVoiceChannel(event.getGuild().getSelfMember());

		return !Objects.equals(requesterChannel, botChannel);
	}

	protected VoiceChannel getVoiceChannel(Member m){
		return Objects.requireNonNull(m.getVoiceState()).getChannel();
	}
}
