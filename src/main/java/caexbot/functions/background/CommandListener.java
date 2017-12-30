package caexbot.functions.background;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import caexbot.commands.CaexCommand;
import caexbot.commands.DraconicCommand;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
	
	private List<CaexCommand> cmds = new ArrayList<CaexCommand>();
	private DraconicListener dl;
	
	public CommandListener(JDA jda) {
		dl = new DraconicListener(this);
		jda.addEventListener(dl);
	}
	
	public CommandListener addCommand(CaexCommand cmd){
		this.cmds.add(cmd);
		if (cmd instanceof DraconicCommand) dl.addCommand((DraconicCommand)cmd);
		return this;
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		if (event.getAuthor().isBot()){return;}//ignore bots and self
		
		//user asked for prefix
		if (event.getMessage().isMentioned(event.getJDA().getSelfUser()) && !event.getMessage().mentionsEveryone()){
			if (containsKeyWord(event)) {
				event.getChannel().sendMessage(String.format(
						"My current prefix is: `%s`\n\n"
						+ "If you have the `administrator` permission, you may change my prefix using the `set-prefix` command.\n\n"
						+ "Do `%shelp set-prefix` for more information.",
						CaexCommand.getPrefix(event.getGuild()),
						CaexCommand.getPrefix(event.getGuild())
								)).queue();
			}
		}
		
		User author = event.getAuthor();
		String message = event.getMessage().getRawContent();
		
		if(!message.toLowerCase().startsWith(CaexCommand.getPrefix(event.getGuild())))
			return;
		findCommand(event, author, message); 
	}

	private boolean containsKeyWord(MessageReceivedEvent event) {
		List<String> keywords = Arrays.asList("prefix", "help", "command", "code");
		
		for (String string : keywords) {
			if(event.getMessage().getContent().contains(string))
				return true;
		}
		return false;
	}
	
	private void findCommand(MessageReceivedEvent event, User author, String message) {
		
		String[] args = message.split(" ");
		String command = args[0].toLowerCase().replace(CaexCommand.getPrefix(event.getGuild()), "");
		String[] finalArgs = Arrays.copyOfRange(args, 1, args.length);
		Optional<CaexCommand> c = cmds.stream().filter(cc -> cc.getAlias().contains(command)).findFirst();

		if(c.isPresent()){
			CaexCommand cmd = c.get();

			callCommand(event,finalArgs,  cmd);
		}

	}

	protected void callCommand(MessageReceivedEvent event, String[] finalArgs, 
			CaexCommand cmd) {
		new Thread() {
			@Override
			public void run(){
				cmd.runCommand(finalArgs, event);
				interrupt();
			}
		}.start();
	}

	public List<CaexCommand> getCommands() {
		
		return cmds;
	}

}
