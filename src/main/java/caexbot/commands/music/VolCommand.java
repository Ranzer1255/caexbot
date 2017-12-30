package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.Describable;
import caexbot.functions.music.GuildPlayerManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class VolCommand extends AbstractMusicCommand implements Describable{

	@Override
	public void process(String[] args, MessageReceivedEvent event) {

		if(args.length==0){
			GuildPlayerManager.getPlayer(event.getGuild()).vol();
			return;
		}
		try {
			int vol = Integer.parseInt(args[0]);
			GuildPlayerManager.getPlayer(event.getGuild()).vol(vol);
		} catch (NumberFormatException e) {
			event.getChannel().sendMessage("I'm sorry please use an Integer between 1-150").queue();
		}
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("vol", "v");
	}

	@Override
	public String getShortDescription() {
		return "Adjust the volume of the song on a scale from 1-150";
	}
	
	@Override
	public String getLongDescription() {
		return super.getLongDescription()+
				"not supplying a value will give the current volume setting\n\n"
				+ "volume goes to values between `101` and `150` are \"boosted\" beyond the original track's volume use with caution";
	}
	
	@Override
	public String getUsage(Guild g) {
		return String.format("`%smusic %s [<1-150>]`", getPrefix(g), getName());
	}
}
