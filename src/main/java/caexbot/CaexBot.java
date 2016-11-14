package caexbot;

import java.awt.Color;

import javax.security.auth.login.LoginException;

import caexbot.functions.background.CommandListener;
import caexbot.functions.levels.LevelUpdater;
import caexbot.util.Logging;
import caexbot.commands.admin.HelpCommand;
import caexbot.commands.admin.InfoCommand;
import caexbot.commands.admin.PingCommand;
import caexbot.commands.admin.ShutdownCommand;
import caexbot.commands.chat.EightBallCommand;
import caexbot.commands.chat.FacepalmCommand;
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
import net.dv8tion.jda.core.entities.impl.GameImpl;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class CaexBot {
	
	private static JDA JDA;
	private static CommandListener commands = new CommandListener();

	public static void main (String[] args){
		Logging.info("Huu... Wha... who... Oh, I guess it's time to [start up]");	


		CaexConfiguration config = CaexConfiguration.getInstance();

		commands.addCommand(new HelpCommand(commands))
				.addCommand(new DiceCommand())
				.addCommand(new EightBallCommand())
				.addCommand(new FacepalmCommand())
				.addCommand(new InfoCommand())
				.addCommand(new LevelCommand())
				.addCommand(new PingCommand())
				.addCommand(new ShutdownCommand())
				.addCommand(new YoutubeSearchCommand())
				.addCommand(new ZomDiceCommand());

		JDABuilder build = new JDABuilder(AccountType.BOT)
				.addListener(commands)
				.setToken(config.getToken());

		try {
			JDA = build.buildBlocking();
		} catch (LoginException | IllegalArgumentException | InterruptedException | RateLimitedException e) {
			Logging.error(e.getMessage());
			Logging.log(e);
		}
		
		JDA.addEventListener(new LevelUpdater());
		JDA.getPresence().setGame(Game.of(config.getStatus()));
		
		if(config.isDebug()){
			for (Guild g : JDA.getGuilds()) {
				g.getRolesByName(config.getRole(), false).get(0).getManager().setColor(new Color(0xb30000)).queue();
				JDA.getPresence().setGame(Game.of("in Testing Mode"));
			}
		}else {
			for (Guild g: JDA.getGuilds()){
				g.getRolesByName(config.getRole(), false).get(0).getManager().setColor(new Color(0xa2760a)).queue();
			}
		}
	}

	public static JDA getJDA(){
		return JDA;
	}
}
