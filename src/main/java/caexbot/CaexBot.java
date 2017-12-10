package caexbot;

import java.time.LocalDateTime;

import javax.security.auth.login.LoginException;

import caexbot.commands.admin.HelpCommand;
import caexbot.commands.admin.InfoCommand;
import caexbot.commands.admin.LevelAlertToggleCommand;
import caexbot.commands.admin.PingCommand;
import caexbot.commands.admin.PrefixCommand;
import caexbot.commands.admin.ShutdownCommand;
import caexbot.commands.admin.XPPermCommand;
import caexbot.commands.chat.DraconicTranslateCommand;
import caexbot.commands.chat.EightBallCommand;
import caexbot.commands.chat.FacepalmCommand;
import caexbot.commands.chat.InsultCommand;
import caexbot.commands.chat.LevelCommand;
import caexbot.commands.games.DiceCommand;
import caexbot.commands.games.HiLowCommand;
import caexbot.commands.games.ZomDiceCommand;
import caexbot.commands.music.MusicCommand;
import caexbot.commands.search.YoutubeSearchCommand;
import caexbot.config.CaexConfiguration;
import caexbot.functions.background.CommandListener;
import caexbot.functions.levels.LevelUpdater;
import caexbot.util.Logging;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

/*
 * TODO add guild names to log output
 */
/**
 * Credit where credit is due:
 * <p> the basic structure of the command system of this bot was heavily based on
 * <a href="https://github.com/JoshCode/gilmore">GilmoreBot</a>
 *  by <a href=https://github.com/JoshCode>JoshCode</a>
 *  <p> Thank you for the insperation!
 * @author Ranzer
 *
 */
public class CaexBot {
	
	private static JDA JDA;
	public final static LocalDateTime START_TIME = LocalDateTime.now();
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
		JDA.getPresence().setGame(Game.playing(config.getStatus()));
	
		Logging.info("Done Loading and ready to go!");
		Logging.info("Sending Online message to Owner");
		JDA.getUserById(config.getOwner()).openPrivateChannel().queue((t) -> {
				t.sendMessage(new MessageBuilder().append("Boss, I've just come online")
						.setEmbed(InfoCommand.infoEmbed(getJDA().getSelfUser()).build()).build()
						).queue();
				t.sendMessage(new MessageBuilder().setEmbed(
						InfoCommand.statusEmbed(getJDA().getSelfUser()).build()
						).build()).queue();
		});
	}

	public static JDA getJDA(){
		return JDA;
	}
}
