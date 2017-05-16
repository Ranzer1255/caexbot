package caexbot;

import javax.security.auth.login.LoginException;
import caexbot.functions.background.CommandListener;
import caexbot.functions.levels.LevelUpdater;
import caexbot.util.Logging;
import caexbot.commands.admin.*;
import caexbot.commands.chat.*;
import caexbot.commands.games.*;
import caexbot.commands.music.*;
import caexbot.commands.search.*;
import caexbot.config.CaexConfiguration;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

/*
 * TODO add guild names to log output
 */
public class CaexBot {
	
	private static JDA JDA;
	private static CommandListener commands;

	public static void main (String[] args){
		Logging.info("Huu... Wha... who... Oh, I guess it's time to [start up]");
		
		CaexConfiguration config = CaexConfiguration.getInstance();
		
		
		JDABuilder build;
		if (config.isDebug()) {
			build = new JDABuilder(AccountType.BOT).setToken(config.getTestToken());
		} else {			
			build = new JDABuilder(AccountType.BOT).setToken(config.getToken());
		}
		
		try {
			JDA = build.buildBlocking();
		} catch (LoginException | IllegalArgumentException | InterruptedException | RateLimitedException e) {
			Logging.error(e.getMessage());
			Logging.log(e);
		}
		

		commands = new CommandListener(JDA);
		
		commands.addCommand(new HelpCommand(commands))
				.addCommand(new InsultCommand()) 
				.addCommand(new DiceCommand())
				.addCommand(new DraconicTranslateCommand())
				.addCommand(new EightBallCommand())
				.addCommand(new FacepalmCommand())
				.addCommand(new InfoCommand())
				.addCommand(new LevelCommand())
				.addCommand(new PingCommand())
				.addCommand(new ShutdownCommand())
				.addCommand(new YoutubeSearchCommand())
				.addCommand(new ZomDiceCommand())
				.addCommand(new PrefixCommand())
				.addCommand(new XPPermCommand())
				.addCommand(new MusicCommand())
				.addCommand(new HiLowCommand())
				.addCommand(new LevelAlertToggleCommand());

		JDA.addEventListener(commands);
		JDA.addEventListener(new LevelUpdater());
		JDA.getPresence().setGame(Game.of(config.getStatus()));
	
		Logging.info("Done Loading and ready to go!");
	}

	public static JDA getJDA(){
		return JDA;
	}
}
