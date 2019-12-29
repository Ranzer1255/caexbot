package caexbot.functions.levels;

import java.util.concurrent.ThreadLocalRandom;

import caexbot.CaexBot;
import caexbot.data.GuildData;
import caexbot.data.GuildManager;
//import caexbot.util.Logging;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class LevelUpdater extends ListenerAdapter{

	private static final long MESSAGE_TIMEOUT = 60000L;
	private final static int XP_LOWBOUND = 15, XP_HIGHBOUND = 25;
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event){
		GuildData gd = CaexBot.GUILD_MANAGER.getGuildData(event.getGuild());
//		Logging.debug(String.format("i heard %s speak on %s server", event.getAuthor().getName(), event.getGuild().getName()));

		if (isXPChannel(event, gd)) {
			if (isNotBot(event)) {
				if (isNotTimedout(event, gd)) {
					gd.addXP(event.getAuthor(), getRandomXP(),event.getChannel());

				}
			} 
		}
		
	}
	
	private boolean isNotTimedout(GuildMessageReceivedEvent event, GuildData gd) {
		
		return gd.getUserLevel(event.getMember())==null
				||(System.currentTimeMillis()- gd.getUserLevel(event.getMember()).getLastXPTime()) > MESSAGE_TIMEOUT;
	}
	
	private boolean isNotBot(GuildMessageReceivedEvent event) {
		return (event.getAuthor() != event.getJDA().getSelfUser()) && !event.getAuthor().isBot();
	}
	
	private boolean isXPChannel(GuildMessageReceivedEvent event, GuildData gd) {
		return gd.getChannel(event.getChannel()).getXPPerm();
	}
	
	private int getRandomXP() {
		
		return ThreadLocalRandom.current().nextInt(XP_LOWBOUND, XP_HIGHBOUND+1);
	}

}
