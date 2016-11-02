package caexbot;

import java.awt.Color;

import javax.security.auth.login.LoginException;

import caexbot.functions.background.CommandListener;
import caexbot.functions.levels.LevelUpdater;
import caexbot.util.Logging;
import caexbot.commands.DiceCommand;
import caexbot.commands.EightBallCommand;
import caexbot.commands.FacepalmCommand;
import caexbot.commands.HelpCommand;
import caexbot.commands.InfoCommand;
import caexbot.commands.LevelCommand;
import caexbot.commands.PingCommand;
import caexbot.commands.ShutdownCommand;
import caexbot.commands.YoutubeSearchCommand;
import caexbot.commands.ZomDiceCommand;
import caexbot.config.CaexConfiguration;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.Guild;

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

		JDABuilder build = new JDABuilder()
				.addListener(commands)
				.setBotToken(config.getToken());

		try {
			JDA = build.buildBlocking();
		} catch (LoginException | IllegalArgumentException | InterruptedException e) {
			Logging.error(e.getMessage());
			Logging.log(e);
		}
		
		JDA.addEventListener(new LevelUpdater());
		JDA.getAccountManager().setGame(config.getStatus());
		
		if(config.isDebug()){
			for (Guild g : JDA.getGuilds()) {
				g.getRolesByName(config.getRole()).get(0).getManager().setColor(new Color(0xb30000)).update();
				JDA.getAccountManager().setGame("in Testing Mode");
			}
		}else {
			for (Guild g: JDA.getGuilds()){
				g.getRolesByName(config.getRole()).get(0).getManager().setColor(new Color(0xa2760a)).update();
			}
		}
	}

	public static JDA getJDA(){
		return JDA;
	}
}
