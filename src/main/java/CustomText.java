import javax.security.auth.login.LoginException;

import caexbot.commands.chat.DraconicTranslateCommand;
import caexbot.config.CaexConfiguration;
import caexbot.functions.background.CommandListener;
import caexbot.util.DraconicTranslator;
import caexbot.util.Logging;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class CustomText {

	static net.dv8tion.jda.core.JDA JDA;
	public static void main(String[] args) {
		CaexConfiguration config = CaexConfiguration.getInstance();
		
		JDABuilder build = new JDABuilder(AccountType.BOT)
				.setToken(config.getToken());

		try {
			JDA = build.buildBlocking();
		} catch (LoginException | IllegalArgumentException | InterruptedException | RateLimitedException e) {
			Logging.error(e.getMessage());
			Logging.log(e);
		}
//		
//		for(TextChannel tc:JDA.getTextChannels()){
//			System.out.println(String.format("%s: %s", tc.getName(), tc.getId()));
//		}
//		for(User u:JDA.getUsers()){
//			
//			System.out.println(String.format("%s: %s", u.getName(), u.getAsMention()));
//		}
		for(Guild g:JDA.getGuilds()){
			System.out.println(g.getName());
		}
		System.out.println(JDA.getGuilds().size());
		
		System.exit(0);
	}

}
