package caexbot.functions.levels;

import java.util.concurrent.ThreadLocalRandom;

import caexbot.data.GuildData;
import caexbot.data.GuildManager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class LevelUpdater extends ListenerAdapter{

	private static final long MESSAGE_TIMEOUT = 60000L;
	private final static int XP_LOWBOUND = 15, XP_HIGHBOUND = 25;
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event){

		if((event.getAuthor() != event.getJDA().getSelfUser())&&!event.getAuthor().isBot()){
		GuildData gd= GuildManager.getGuildData(event.getGuild());
			if ((System.currentTimeMillis() - gd.getUserLevel(event.getAuthor()).getLastXPTime().getTime())>MESSAGE_TIMEOUT) {
				GuildManager.getGuildData(event.getGuild()).addXP(event.getAuthor(), getRandomXP(),event.getChannel());

			}
		}
		
	}
	private int getRandomXP() {
		
		return ThreadLocalRandom.current().nextInt(XP_LOWBOUND, XP_HIGHBOUND+1);
	}

}
