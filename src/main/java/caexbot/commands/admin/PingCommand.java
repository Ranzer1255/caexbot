package caexbot.commands.admin;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import caexbot.commands.DraconicCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PingCommand extends CaexCommand implements DraconicCommand, Describable{

	@Override
	public void process(String[] args, MessageReceivedEvent event) {
		Date startTime = new Date();
		Message pong = event.getChannel().sendMessage("pong!").complete();
		Date endTime = new Date();
		long lag = endTime.getTime()-startTime.getTime();
		pong.editMessage(pong.getContentDisplay()+" `"+lag+"ms`").queue();
		
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("ping");
	}

	@Override
	public String getShortDescription() {
		return "pong!";
	}
	
	@Override
	public String getLongDescription() {
		return "Tests the response time of the host server";
	}

	@Override
	public List<String> getDraconicAlias() {
		return Arrays.asList("relgar");
	}

	@Override
	public Catagory getCatagory() {
		return Catagory.ADMIN;
	}

	@Override
	public boolean isAplicableToPM() {
		return true;
	}


}
