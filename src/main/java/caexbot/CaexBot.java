package caexbot;

import javax.security.auth.login.LoginException;
import caexbot.functions.background.CommandListener;
import caexbot.functions.levels.LevelUpdater;
import caexbot.util.Logging;
import caexbot.commands.admin.HelpCommand;
import caexbot.commands.admin.InfoCommand;
import caexbot.commands.admin.PingCommand;
import caexbot.commands.admin.PrefixCommand;
import caexbot.commands.admin.ShutdownCommand;
import caexbot.commands.chat.DraconicTranslateCommand;
import caexbot.commands.chat.EightBallCommand;
import caexbot.commands.chat.FacepalmCommand;
import caexbot.commands.chat.InsultCommand;
import caexbot.commands.chat.LevelCommand;
import caexbot.commands.games.DiceCommand;
import caexbot.commands.games.ZomDiceCommand;
import caexbot.commands.search.YoutubeSearchCommand;
import caexbot.config.CaexConfiguration;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

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
				.addCommand(new PrefixCommand());

		JDA.addEventListener(commands);
		JDA.addEventListener(new LevelUpdater());
		JDA.getPresence().setGame(Game.of(config.getStatus()));
	
		//color controls buggy and unreliable ATM
		if(config.isDebug()){
			for (Guild g : JDA.getGuilds()) {
				g.getController().setNickname(g.getMember(JDA.getSelfUser()), null).queue();
				JDA.getPresence().setGame(Game.of("in Testing Mode"));
			}
		} else {
			for(Guild g: JDA.getGuilds()){
				g.getController().setNickname(g.getMember(JDA.getSelfUser()), null).queue();
			}
		}
		Logging.info("Done Loading and ready to go!");
	}

	public static JDA getJDA(){
		return JDA;
	}
}
