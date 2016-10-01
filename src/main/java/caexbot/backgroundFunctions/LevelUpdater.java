package caexbot.backgroundFunctions;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class LevelUpdater extends ListenerAdapter{


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
