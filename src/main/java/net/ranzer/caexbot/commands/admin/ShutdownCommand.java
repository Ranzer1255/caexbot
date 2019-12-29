package net.ranzer.caexbot.commands.admin;

import java.util.Arrays;
import java.util.List;

import net.ranzer.caexbot.CaexBot;
import net.ranzer.caexbot.commands.CaexCommand;
import net.ranzer.caexbot.commands.chat.InsultCommand;
import net.ranzer.caexbot.config.CaexConfiguration;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

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
			.sendMessage(event.getJDA().getUserById(CaexConfiguration.getInstance().getOwner()).getName()+
					" found out I insulted him behind his back....\n"
					+ "he just issued the shutdown command...... i'm.... goi... *cough* *cough*\n\n"
					+ "......goodbye........"
					).queue((m) ->	sucsesfulMessage(m), (t)->failedMessage(t));
		}
		try {Thread.sleep(1000L);} catch (InterruptedException e) {}
		event.getJDA().shutdown();
		System.exit(0);
	}

	private void sucsesfulMessage(Message m) {
		m.getJDA().getUserById(CaexConfiguration.getInstance().getOwner()).openPrivateChannel().complete()
		.sendMessage("sent shutdown message to "+InsultCommand.lastOwnerInsult.getName()).queue();	
	}
	
	private void failedMessage(Throwable t){
		CaexBot.getJDA().getUserById(CaexConfiguration.getInstance().getOwner()).openPrivateChannel().complete()
		.sendMessage("failed to send shutdown message to "+InsultCommand.lastOwnerInsult.getName()
		+ "\n\nthis is why\n" + t.getMessage()).queue();
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("vdri");//this is "sleep" in draconic
	}

	@Override
	public boolean isAplicableToPM() {
		return true;
	}
}