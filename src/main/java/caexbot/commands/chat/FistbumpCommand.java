package caexbot.commands.chat;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class FistbumpCommand extends CaexCommand implements Describable {

	@Override
	public Catagory getCatagory() {
		return Catagory.CHAT;
	}

	@Override
	public String getShortDescription() {
		return "Fistbump a victory!";
	}

	@Override
	public String getLongDescription() {
		return getShortDescription() + "\n\n"
				+ "Need a dramatic flair to pinpoint your success? FistBump! Boom! (W/ manditory explosion!)";
		
	}

	@Override
	public boolean isAplicableToPM() {
		return true;
	}

	@Override
	public void process(String[] args, MessageReceivedEvent event) {
		Message m = event.getChannel().sendMessage(":right_facing_fist:    :left_facing_fist:").complete();
		try {
			Thread.sleep(1000l);
			m.editMessage(":right_facing_fist::left_facing_fist:").complete();
			Thread.sleep(1000l);
			m.editMessage(":boom:").complete();
		} catch (Exception e) {	}
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("fistbump","fb");
	}

}
