package caexbot.commands.music;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.data.GuildManager;
import net.dv8tion.jda.core.entities.TextChannel;

public abstract class AbstractMusicCommand extends CaexCommand {

	protected void setMusicChannel(TextChannel channel) {
		GuildManager.getGuildData(channel.getGuild()).setMusicChannel(channel);
	}
	
	public Catagory getCatagory(){
		return Catagory.MUSIC;
	}
}
