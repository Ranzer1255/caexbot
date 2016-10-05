package caexbot.functions.levels;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import caexbot.util.Logging;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class LevelUpdater extends ListenerAdapter{

	private static final long MESSAGE_TIMEOUT = 60000L;
	private final static int XP_LOWBOUND = 15, XP_HIGHBOUND = 25;
	private static List<User> messageTimeout = new ArrayList<>();
	
	expTable xp = expTable.getInstance();

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event){
		
		if((event.getAuthor() != event.getJDA().getSelfInfo())&&!event.getAuthor().isBot()){
		
			if (!messageTimeout.contains(event.getAuthor())) {
				addXP(event.getGuild(), event.getAuthor());
				new Thread(){
					public void run(){
						messageTimeout.add(event.getAuthor());
						try {
							sleep(MESSAGE_TIMEOUT);
						} catch (InterruptedException e) {}//do nothing
						messageTimeout.remove(event.getAuthor());
					}
				}.start();
			}else{
				Logging.debug(event.getAuthorName()+" is on time out");
			}
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

		Logging.debug("adding XP to " + author.getUsername() + " on " + guild.getName());
		
		xp.addXP(guild, author, getRandomXP());
		
		
	}

	private int getRandomXP() {
		
		return ThreadLocalRandom.current().nextInt(XP_LOWBOUND, XP_HIGHBOUND+1);
	}

}
