package caexbot.commands.admin;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.config.CaexConfiguration;
import caexbot.util.Logging;
import caexbot.util.StringUtil;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;

public class ShutdownCommand extends CaexCommand {

	//TODO see if its possible to add in control words to the string to replace with functions like getPrefix() and such to make each message guild specific
	@Override
	public void process(String[] args,  MessageReceivedEvent event) {
		if (event.getAuthor()!=event.getJDA().getUserById(CaexConfiguration.getInstance().getOwner())){
			noPermission(event);
			return;
		}
		if (args.length>0&&args[0].equals("alert")) {
			shutdownAlertBroadcast(args, event);
			event.getChannel().sendMessage("i've told everyone. night night").queue();
		}
		event.getChannel().sendMessage("if you insist boss.... *blerg*").queue();
		event.getJDA().shutdown();
		try {Thread.sleep(500L);} catch (InterruptedException e) {}
		System.exit(0);
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("vdri");//this is "sleep" in draconic
	}

	@SuppressWarnings("deprecation")//i'm going to keep this in until JDA devs break it completely
	private void shutdownAlertBroadcast(String[] args, MessageReceivedEvent event) {
		for (Guild g : event.getJDA().getGuilds()) {
			try {
				g.getPublicChannel().sendMessage("I've got to go.... \n" + 
			((args.length>1)?StringUtil.arrayToString(Arrays.asList(Arrays.copyOfRange(args, 1, args.length)), " ")+"\n":"")+
			"*casts teleport and vanishes*").queue();
			} catch (PermissionException e) {
				Logging.error("i can't talk here sorry: " + e.getLocalizedMessage());
				continue;
			} catch (NullPointerException e){
				Logging.error(g.getName() + " no longer has a public channel, skipping till annoucnement update");
			}
		}
	}

	@Override
	public boolean isAplicableToPM() {
		return true;
	}
}