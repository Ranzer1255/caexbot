package caexbot.functions.levels;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class LevelUpdater extends ListenerAdapter{

	expTable xp = expTable.getInstance();
	Pair<Guild,User> key;

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event){
		
		if(event.getAuthor() != event.getJDA().getSelfInfo()){
		
			addXP(event.getGuild(), event.getAuthor());
		}
		
	}

	/**
	 * adds experience to author on the guild supplied
	 * 
	 * @param guild 
	 * @param author
	 * @return true if author leveled up.
	 */
	private void addXP(Guild guild, User author) {

		
		
	}

}
