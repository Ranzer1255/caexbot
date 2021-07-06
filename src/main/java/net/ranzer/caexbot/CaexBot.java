package net.ranzer.caexbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.ranzer.caexbot.commands.admin.InfoCommand;
import net.ranzer.caexbot.config.CaexConfiguration;
import net.ranzer.caexbot.data.GuildManager;
import net.ranzer.caexbot.functions.levels.LevelUpdater;
import net.ranzer.caexbot.functions.listeners.CommandListener;
import net.ranzer.caexbot.functions.listeners.DraconicListener;
import net.ranzer.caexbot.functions.listeners.JoinLeaveListener;
import net.ranzer.caexbot.util.Logging;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

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
	private static final CaexConfiguration config = CaexConfiguration.getInstance();

	public static void main (String[] args){
		Logging.info("Huu... Wha... who... Oh, I guess it's time to [start up]");
		
		
		
		JDABuilder build;

		//setup intents
		Collection<GatewayIntent> intents = Arrays.asList(
				GatewayIntent.GUILD_MEMBERS, //privileged
				GatewayIntent.GUILD_PRESENCES, //privileged
				GatewayIntent.DIRECT_MESSAGES,
				GatewayIntent.GUILD_MESSAGES,
				GatewayIntent.GUILD_VOICE_STATES

		);

		//set token
		if (config.isDebug()) {
			build = JDABuilder.create(config.getTestToken(),intents);
		} else {
			build = JDABuilder.create(config.getToken(),intents);
		}

		build.enableIntents(GatewayIntent.GUILD_MEMBERS);
		build.setMemberCachePolicy(MemberCachePolicy.ALL);
		
		//add Listeners
		build.addEventListeners(new StartUpListener());
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
		public void onReady(@NotNull ReadyEvent event) {
			super.onReady(event);
			JDA=event.getJDA();
			JDA.addEventListener(new GuildManager(),
								 CommandListener.getInstance(),
								 DraconicListener.getInstance(),
								 new JoinLeaveListener(),
								 new LevelUpdater());
			JDA.setRequiredScopes("applications.commands");

			for(Guild g: JDA.getGuilds()){
				//todo add slash commands from command listener
				g.updateCommands().addCommands(new InfoCommand().getCommandData()).queue();
			}

			JDA.getPresence().setActivity(Activity.playing(config.getStatus()));
			
			Logging.info("Done Loading and ready to go!");
			JDA.getPresence().setStatus(OnlineStatus.ONLINE);
		}
		
		@Override
		public void onShutdown(@NotNull ShutdownEvent event) {
		
			Logging.info("Shutting down....");
			JDA.getPresence().setStatus(OnlineStatus.IDLE);
			JDA.getPresence().setActivity(Activity.playing("shutting down...."));
		}
		
	}
}
