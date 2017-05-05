package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import caexbot.functions.music.GuildPlayerManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PauseCommand extends CaexCommand implements Describable{

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {

		GuildPlayerManager.getPlayer(event.getGuild()).pause();
		
		 
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("pause");
	}

	@Override
	public String getShortDescription() {
		return "Pauses the currently playing song";
	}

	@Override
	public String getLongDescription() {
		// TODO Auto-generated method stub
		return getShortDescription();
	}
	
	@Override
	public Catagory getCatagory() {
		return Catagory.MUSIC;
	}
	
	@Override
	public String getUsage(Guild g) {
		// TODO Auto-generated method stub
		return null;
	}

}
