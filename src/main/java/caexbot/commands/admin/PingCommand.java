package caexbot.commands.admin;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.commands.DraconicCommand;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class PingCommand extends CaexCommand implements DraconicCommand{

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		Date startTime = new Date();
		try {
			Message pong = channel.sendMessage("pong!").block();
			Date endTime = new Date();
			long lag = endTime.getTime()-startTime.getTime();
			pong.editMessage(pong.getContent()+" `"+lag+"ms`").queue();
		} catch (RateLimitedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		Date startTime2 = new Date();
//		channel.sendMessage("pong again! ").queue(pong -> {
//			Date endTime = new Date();
//			long lag = endTime.getTime()-startTime2.getTime();
//			pong.editMessage(pong.getContent()+"`"+lag+"ms`").queue();
//		});
		
		
	}

	@Override
	public String getUsage(Guild g) {
		return getPrefix(g)+"ping";
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("ping");
	}

	@Override
	public String getDescription() {
		return "pong!";
	}

	@Override
	public List<String> getDraconicAlias() {
		return Arrays.asList("relgar");
	}


}
