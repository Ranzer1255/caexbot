package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.Describable;
import caexbot.functions.music.GuildPlayerManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class VolCommand extends AbstractMusicCommand implements Describable{

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {

		if(args.length==0){
			GuildPlayerManager.getPlayer(event.getGuild()).vol();
			return;
		}
		try {
			int vol = Integer.parseInt(args[0]);
			GuildPlayerManager.getPlayer(event.getGuild()).vol(vol);
		} catch (NumberFormatException e) {
			// TODO Bitch at user for not giving us a number
		}
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("vol");
	}

	@Override
	public String getShortDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLongDescription() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getUsage(Guild g) {
		// TODO Auto-generated method stub
		return null;
	}

}
