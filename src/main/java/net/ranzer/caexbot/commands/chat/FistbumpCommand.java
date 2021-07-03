package net.ranzer.caexbot.commands.chat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.ranzer.caexbot.commands.BotCommand;
import net.ranzer.caexbot.commands.Category;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.commands.DraconicCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class FistbumpCommand extends BotCommand implements Describable, DraconicCommand {

	@Override
	public Category getCategory() {
		return Category.CHAT;
	}

	@Override
	public String getShortDescription() {
		return "Fistbump a victory!";
	}

	@Override
	public String getLongDescription() {
		return getShortDescription() + "\n\n"
				+ "Need a dramatic flair to pinpoint your success? FistBump! Boom! (W/ mandatory explosion!)";
		
	}

	@Override
	public boolean isApplicableToPM() {
		return true;
	}

	@Override
	public void process(String[] args, MessageReceivedEvent event) {
		Message m = event.getChannel().sendMessage(":right_facing_fist:    :left_facing_fist:").complete();
		try {
			Thread.sleep(1000L);
			m.editMessage(":right_facing_fist::left_facing_fist:").complete();
			Thread.sleep(1000L);
			m.editMessage(":boom:").complete();
		} catch (Exception ignored) {	}
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("fistbump","fb");
	}

	@Override
	public List<String> getDraconicAlias() {
		return Collections.singletonList("bemin");
	}

}
