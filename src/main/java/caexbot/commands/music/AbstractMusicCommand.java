package caexbot.commands.music;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.functions.music.GuildPlayerManager;
import net.dv8tion.jda.core.entities.TextChannel;

public abstract class AbstractMusicCommand extends CaexCommand {

	protected void setMusicChannel(TextChannel channel) {
		GuildPlayerManager.getPlayer(channel.getGuild()).getMusicListener().setMusicChannel(channel);
	}
	
	public Catagory getCatagory(){
		return Catagory.MUSIC;
	}
}
