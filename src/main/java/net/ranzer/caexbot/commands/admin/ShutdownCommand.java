package net.ranzer.caexbot.commands.admin;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.ranzer.caexbot.CaexBot;
import net.ranzer.caexbot.commands.CaexCommand;
import net.ranzer.caexbot.commands.chat.InsultCommand;
import net.ranzer.caexbot.config.CaexConfiguration;

import java.util.Collections;
import java.util.List;

/**
 * Hidden command not listed in the in-app help documentation. this command will shut the program off.
 * this command can only be issued by the discord user identified by the ID number in the "owner" field
 * of the Configuration file
 *
 * @see CaexConfiguration
 */
public class ShutdownCommand extends CaexCommand {
	
	@Override
	public void process(String[] args,  MessageReceivedEvent event) {
		if (event.getAuthor()!=event.getJDA().getUserById(CaexConfiguration.getInstance().getOwner())){
			noPermission(event);
			return;
		}
		if (args.length>0&&args[0].equals("alert")) {
			event.getChannel().sendMessage("i've told everyone. night night").queue();
		}
		event.getChannel().sendMessage("if you insist boss.... *blerg*").complete();
		if(InsultCommand.lastOwnerInsult!=null){
			InsultCommand.lastOwnerInsult.openPrivateChannel().complete()
			.sendMessage(event.getJDA().retrieveUserById(CaexConfiguration.getInstance().getOwner())
							.complete().getName()+
					" found out I insulted him behind his back....\n"
					+ "he just issued the shutdown command...... i'm.... goi... *cough* *cough*\n\n"
					+ "......goodbye........"
					).queue(this::successfulMessage, this::failedMessage);
		}
		try {Thread.sleep(1000L);} catch (InterruptedException ignored) {}
		event.getJDA().shutdown();
		System.exit(0);
	}

	private void successfulMessage(Message m) {
		m.getJDA().retrieveUserById(CaexConfiguration.getInstance().getOwner()).complete().openPrivateChannel().complete()
		.sendMessage("sent shutdown message to "+InsultCommand.lastOwnerInsult.getName()).queue();	
	}
	
	private void failedMessage(Throwable t){
		CaexBot.getJDA().retrieveUserById(CaexConfiguration.getInstance().getOwner()).complete().openPrivateChannel().complete()
		.sendMessage("failed to send shutdown message to "+InsultCommand.lastOwnerInsult.getName()
		+ "\n\nthis is why\n" + t.getMessage()).queue();
	}

	@Override
	public List<String> getAlias() {
		return Collections.singletonList("vdri");//this is "sleep" in draconic
	}

	@Override
	public boolean isApplicableToPM() {
		return true;
	}
}