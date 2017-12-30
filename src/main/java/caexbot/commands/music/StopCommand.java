package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.Describable;
import caexbot.functions.music.GuildPlayerManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class StopCommand extends AbstractMusicCommand implements Describable{

	@Override
	public void process(String[] args, MessageReceivedEvent event) {

		if (args.length>0&&args[0].equals("keep")){
			GuildPlayerManager.getPlayer(event.getGuild()).stop(false);
			return;
		}
		GuildPlayerManager.getPlayer(event.getGuild()).stop(true);
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("stop", "s");
	}

	@Override
	public String getShortDescription() {
		return "Stops the current song, leaves the VC and Clears the queue";
	}
	
	@Override
	public String getLongDescription() {
		return super.getLongDescription()+
				"`keep`: will do as above, except **__not__** clear the queue";
	}
	
	@Override
	public String getUsage(Guild g) {
		return String.format("`%smusic %s [keep]`", getPrefix(g), getName());
	}
}
