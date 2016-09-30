package caexbot.functions.levels;

import java.util.concurrent.ThreadLocalRandom;

import caexbot.util.Logging;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class LevelUpdater extends ListenerAdapter{

	public final static int XP_LOWBOUND = 15, XP_HIGHBOUND = 25;
	
	expTable xp = expTable.getInstance();

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

		Logging.debug(this, "adding XP to " + author.getUsername() + " on " + guild.getName());
		
		xp.addXP(guild, author, getRandomXP());
		
		
	}

	private int getRandomXP() {
		return ThreadLocalRandom.current().nextInt(XP_LOWBOUND, XP_HIGHBOUND+1);
	}

}
