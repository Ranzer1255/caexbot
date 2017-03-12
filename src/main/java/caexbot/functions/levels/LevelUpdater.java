package caexbot.functions.levels;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import caexbot.data.GuildManager;
import caexbot.util.Logging;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class LevelUpdater extends ListenerAdapter{

	private static final long MESSAGE_TIMEOUT = 60000L;
	private final static int XP_LOWBOUND = 15, XP_HIGHBOUND = 25;
	private static List<User> messageTimeout = new ArrayList<>();
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event){
		
		if (GuildManager.getGuildData(event.getGuild()).getChannel(event.getChannel()).getXPPerm()) {
			if ((event.getAuthor() != event.getJDA().getSelfUser()) && !event.getAuthor().isBot()) {

				if (!messageTimeout.contains(event.getAuthor())) {
					addXP(event.getGuild(), event.getAuthor(), event.getChannel());
					new Thread() {
						public void run() {
							messageTimeout.add(event.getAuthor());
							try {
								sleep(MESSAGE_TIMEOUT);
							} catch (InterruptedException e) {
							} //do nothing
							messageTimeout.remove(event.getAuthor());
							interrupt();
						}
					}.start();
				}
			} 
		}
		
	}

	/**
	 * adds experience to author on the guild supplied
	 * 
	 * @param guild 
	 * @param author
	 */
	private void addXP(Guild guild, User author, TextChannel channel) {

		Logging.debug("adding XP to " + author.getName() + " on " + guild.getName());
		
		GuildManager.getGuildData(guild).addXP(author, getRandomXP(),channel);
		
		
	}

	private int getRandomXP() {
		
		return ThreadLocalRandom.current().nextInt(XP_LOWBOUND, XP_HIGHBOUND+1);
	}

}
