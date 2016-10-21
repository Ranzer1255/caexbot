import javax.security.auth.login.LoginException;

import caexbot.config.CaexConfiguration;
import caexbot.util.Logging;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;

public class CustomText {

	static net.dv8tion.jda.JDA JDA;
	public static void main(String[] args) {
		CaexConfiguration config = CaexConfiguration.getInstance();
		
		JDABuilder build = new JDABuilder()
				.setBotToken(config.getToken());

		try {
			JDA = build.buildBlocking();
		} catch (LoginException | IllegalArgumentException | InterruptedException e) {
			Logging.error(e.getMessage());
			Logging.log(e);
		}
		
		for(TextChannel tc:JDA.getTextChannels()){
			System.out.println(String.format("%s: %s", tc.getName(), tc.getId()));
		}
		for(User u:JDA.getUsers()){
			System.out.println(String.format("%s: %s", u.getUsername(), u.getAsMention()));
		}
		
		System.exit(0);
	}

}
