package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.Describable;
import caexbot.functions.music.GuildPlayerManager;
import caexbot.util.StringUtil;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class InsertCommand extends AbstractMusicCommand implements Describable {

	@Override
	public List<String> getAlias() {
		return Arrays.asList("insert");
	}

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {

		if (args[0].startsWith(getPrefix(event.getGuild()))) {//test code TODO handle this better
			GuildPlayerManager.getPlayer(event.getGuild()).insertID(args[0].substring(getPrefix(event.getGuild()).length(), args[0].length()));
		} else {
			GuildPlayerManager.getPlayer(event.getGuild()).insertSearch(StringUtil.arrayToString(Arrays.asList(args), " "));
		}

	}

	@Override
	public String getUsage(Guild g) {
		return null;//TODO
	}

	@Override
	public String getShortDescription() {
		return "Add song to the Head of the queue";
	}

	@Override
	public String getLongDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
