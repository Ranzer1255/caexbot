package caexbot.commands.admin;

import java.util.Arrays;
import java.util.List;

import caexbot.CaexBot;
import caexbot.commands.CaexCommand;
import caexbot.commands.chat.InsultCommand;
import caexbot.config.CaexConfiguration;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ShutdownCommand extends CaexCommand {
	
	private static String SHUTDOWN_MESSAGE = CaexBot.getJDA().getUserById(CaexConfiguration.getInstance().getOwner()).getName()+
			" found out I insulted him behind his back....\n"
			+ "he just issued the shutdown command...... i'm.... goi... *cough* *cough*\n\n"
			+ "......goodbye........";

	//TODO see if its possible to add in control words to the string to replace with functions like getPrefix() and such to make each message guild specific
	@Override
	public void process(String[] args,  MessageReceivedEvent event) {
		if (event.getAuthor()!=event.getJDA().getUserById(CaexConfiguration.getInstance().getOwner())){
			noPermission(event);
			return;
		}
		if (args.length>0&&args[0].equals("alert")) {
//			shutdownAlertBroadcast(args, event); TODO to be fix when announcement system is implemented
			event.getChannel().sendMessage("i've told everyone. night night").queue();
		}
		event.getChannel().sendMessage("if you insist boss.... *blerg*").complete();
		if(InsultCommand.lastOwnerInsult!=null){
			InsultCommand.lastOwnerInsult.openPrivateChannel().complete()
			.sendMessage(SHUTDOWN_MESSAGE).queue((m) ->	sucsesfulMessage(m), (t)->failedMessage(t));
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

	//TODO fix this when i implement an announcement system
//	private void shutdownAlertBroadcast(String[] args, MessageReceivedEvent event) {
//		for (Guild g : event.getJDA().getGuilds()) {
//			try {
//				g.getPublicChannel().sendMessage("I've got to go.... \n" + 
//			((args.length>1)?StringUtil.arrayToString(Arrays.asList(Arrays.copyOfRange(args, 1, args.length)), " ")+"\n":"")+
//			"*casts teleport and vanishes*").queue();
//			} catch (PermissionException e) {
//				Logging.error("i can't talk here sorry: " + e.getLocalizedMessage());
//				continue;
//			} catch (NullPointerException e){
//				Logging.error(g.getName() + " no longer has a public channel, skipping till annoucnement update");
//			}
//		}
//	}

	@Override
	public boolean isAplicableToPM() {
		return true;
	}
}