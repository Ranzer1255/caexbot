package net.ranzer.caexbot.commands.music;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.functions.music.GuildPlayerManager;
import net.ranzer.caexbot.util.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InsertCommand extends AbstractMusicCommand implements Describable {

	@Override
	public List<String> getAlias() {
		return Collections.singletonList("insert");
	}

	@Override
	public void process(String[] args, MessageReceivedEvent event) {

		if (args[0].startsWith(getPrefix(event.getGuild()))) {
			GuildPlayerManager.getPlayer(event.getGuild()).insertID(args[0].substring(getPrefix(event.getGuild()).length()));
		} else {
			GuildPlayerManager.getPlayer(event.getGuild()).insertSearch(StringUtil.arrayToString(Arrays.asList(args), " "));
		}

	}
	@Override
	public String getShortDescription() {
		return "Add song to the Head of the queue";
	}
	
	@Override
	public String getLongDescription() {
		return super.getLongDescription()
				+ "this command will search Youtube for your track and add it to the start of the queue\n\n"
				+ "to Bypass the search and insert an url or YT video/Playlist code preface the code with the guild prefix";
	}
	
	@Override
	public String getUsage(Guild g) {
		
		return String.format("`%smusic %s <search string>`", getPrefix(g), getAlias().get(0)) + "\n"+
		       String.format("`%smusic %s %s<video or playlist code>`", getPrefix(g),getName(),getPrefix(g));
	}
}
