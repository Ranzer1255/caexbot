package caexbot.commands.music;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import caexbot.functions.music.GuildPlayerManager;
import net.dv8tion.jda.core.entities.TextChannel;

public abstract class AbstractMusicCommand extends CaexCommand implements Describable{

	protected void setMusicChannel(TextChannel channel) {
		GuildPlayerManager.getPlayer(channel.getGuild()).getMusicListener().setMusicChannel(channel);
	}
	
	public Catagory getCatagory(){
		return Catagory.MUSIC;
	}
}
