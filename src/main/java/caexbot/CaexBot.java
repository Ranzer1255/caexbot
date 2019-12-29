package caexbot;

import caexbot.config.CaexConfiguration;
import caexbot.data.GuildManager;
import caexbot.data.json.JSONGuildManager;
import caexbot.data.mysql.MySQLGuildManager;
import caexbot.functions.levels.LevelUpdater;
import caexbot.functions.listeners.CommandListener;
import caexbot.functions.listeners.DraconicListener;
import caexbot.functions.listeners.JoinLeaveListener;
import caexbot.util.Logging;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;

/**
 * Credit where credit is due:
 * <p> the basic structure of the command system of this bot was heavily based on
 * <a href="https://github.com/JoshCode/gilmore">GilmoreBot</a>
 *  by <a href=https://github.com/JoshCode>JoshCode</a>
 *  <p> Thank you for the inspiration!
 * @author Ranzer
 *
 */
public class CaexBot {
	
	private static JDA JDA;
	public final static LocalDateTime START_TIME = LocalDateTime.now();
	private static CaexConfiguration config = CaexConfiguration.getInstance();
	public static GuildManager GUILD_MANAGER;

	public static void main (String[] args){
		Logging.info("Huu... Wha... who... Oh, I guess it's time to [start up]");
		
		
		
		JDABuilder build;
		
		//set token
		if (config.isDebug()) {
			build = new JDABuilder(AccountType.BOT).setToken(config.getTestToken());
		} else {			
			build = new JDABuilder(AccountType.BOT).setToken(config.getToken());
		}
		
		//add Listeners
		build.addEventListeners(CommandListener.getInstance(),
							   DraconicListener.getInstance(),
							   new JoinLeaveListener(),
//							   new LevelUpdater(),
							   new StartUpListener());
		build.setActivity(Activity.playing("Waking up, please wait"));
		build.setStatus(OnlineStatus.DO_NOT_DISTURB);
		
		//build
		try {
			JDA = build.build();
		} catch (LoginException | IllegalArgumentException e) {
			Logging.error(e.getMessage());
			Logging.log(e);
		}
	}	

	public static JDA getJDA(){

		return JDA;
	}
	
	private static class StartUpListener extends ListenerAdapter{
		
		
		
		@Override
		public void onReady(ReadyEvent event) {
			super.onReady(event);
			JDA=event.getJDA();
			GUILD_MANAGER = new JSONGuildManager();
			JDA.addEventListener(GUILD_MANAGER);
			JDA.getPresence().setActivity(Activity.playing(config.getStatus()));
			
			Logging.info("Done Loading and ready to go!");
			JDA.getPresence().setStatus(OnlineStatus.ONLINE);
		}
		
		@Override
		public void onShutdown(ShutdownEvent event) {
		
			Logging.info("Shutting down....");
			JDA.getPresence().setStatus(OnlineStatus.IDLE);
			JDA.getPresence().setActivity(Activity.playing("shutting down...."));
		}
		
	}
}
