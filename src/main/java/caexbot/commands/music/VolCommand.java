package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.Describable;
import caexbot.functions.music.GuildPlayerManager;
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
			channel.sendMessage("I'm sorry please use an Integer between 1-150").queue();
		}
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("vol");
	}

	@Override
	public String getShortDescription() {
		return "Adjust the volume of the song on a scale from 1-150";
	}
}
