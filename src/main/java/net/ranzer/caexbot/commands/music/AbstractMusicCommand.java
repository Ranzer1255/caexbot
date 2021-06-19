package net.ranzer.caexbot.commands.music;

import net.ranzer.caexbot.commands.CaexCommand;
import net.ranzer.caexbot.commands.Catagory;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.functions.music.GuildPlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class AbstractMusicCommand extends CaexCommand implements Describable{

	protected void setMusicChannel(TextChannel channel) {
		GuildPlayerManager.getPlayer(channel.getGuild()).getMusicListener().setMusicChannel(channel);
	}
	
	public Catagory getCatagory(){
		return Catagory.MUSIC;
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

	protected boolean inSameVoiceChannel(MessageReceivedEvent event) {
		return event.getMember().getVoiceState().inVoiceChannel()&&
				event.getMember().getVoiceState().getChannel()==event.getGuild().getSelfMember().getVoiceState().getChannel();
	}
}
