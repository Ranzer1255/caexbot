package net.ranzer.caexbot.functions.levels;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.ranzer.caexbot.data.GuildManager;
import net.ranzer.caexbot.data.IGuildData;

import java.util.concurrent.ThreadLocalRandom;

public class LevelUpdater extends ListenerAdapter{

	private static final long MESSAGE_TIMEOUT = 60000L;
	private final static int XP_LOWBOUND = 15, XP_HIGHBOUND = 25;
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event){
		IGuildData gd = GuildManager.getGuildData(event.getGuild());
//		Logging.debug(String.format("i heard %s speak on %s server", event.getAuthor().getName(), event.getGuild().getName()));

		if (isXPChannel(event, gd)) {
			if (isNotBot(event)) {
				if (isNotTimedout(event, gd)) {
					gd.getMemberData(event.getAuthor()).addXP(getRandomXP(),event.getChannel());

				}
			} 
		}
		
	}
	
	private boolean isNotTimedout(GuildMessageReceivedEvent event, IGuildData gd) {
		
		return gd.getMemberData(event.getMember()).getUserLevel()==null
				||(System.currentTimeMillis()- gd.getMemberData(event.getMember()).getUserLevel().getLastXPTime()) > MESSAGE_TIMEOUT;
	}
	
	private boolean isNotBot(GuildMessageReceivedEvent event) {
		return (event.getAuthor() != event.getJDA().getSelfUser()) && !event.getAuthor().isBot();
	}
	
	private boolean isXPChannel(GuildMessageReceivedEvent event, IGuildData gd) {
		return gd.getChannel(event.getChannel()).getXPPerm();
	}
	
	private int getRandomXP() {
		
		return ThreadLocalRandom.current().nextInt(XP_LOWBOUND, XP_HIGHBOUND+1);
	}

}
