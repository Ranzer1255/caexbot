package caexbot;

import javax.security.auth.login.LoginException;

import caexbot.backgroundFunctions.CommandListener;
import caexbot.backgroundFunctions.LevelUpdater;
import caexbot.commands.EightBallCommand;
import caexbot.commands.HelpCommand;
import caexbot.commands.InfoCommand;
import caexbot.commands.PingCommand;
import caexbot.commands.ShutdownCommand;
import caexbot.config.CaexConfiguration;
import caexbot.util.Logging;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;

public class CaexBot {
	
	private static JDA JDA;
	private static CommandListener commands = new CommandListener();

	public static void main (String[] args){
//		Logging.info(null, "Huu... Wha... who... Oh, I guess it's time to [start up]");	


		CaexConfiguration config = CaexConfiguration.getInstance();

		commands.addCommand(new HelpCommand(commands))
				.addCommand(new InfoCommand())
				.addCommand(new PingCommand())
				.addCommand(new EightBallCommand())
				.addCommand(new ShutdownCommand());

		JDABuilder build = new JDABuilder()
				.addListener(commands)
				.addListener(new LevelUpdater())
				.setBotToken(config.getToken());

		try {
			JDA = build.buildBlocking();
		} catch (LoginException | IllegalArgumentException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JDA.getAccountManager().setGame("with Gilmore");
	}

	public static JDA getJDA(){
		return JDA;
	}
}
