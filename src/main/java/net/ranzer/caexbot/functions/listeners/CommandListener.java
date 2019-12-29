package net.ranzer.caexbot.functions.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import net.ranzer.caexbot.commands.CaexCommand;
import net.ranzer.caexbot.commands.DraconicCommand;
import caexbot.commands.admin.*;
import caexbot.commands.chat.*;
import caexbot.commands.games.*;
import caexbot.commands.music.*;
import caexbot.commands.search.*;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.ranzer.caexbot.commands.CaexCommand;
import net.ranzer.caexbot.commands.DraconicCommand;
import net.ranzer.caexbot.commands.admin.*;
import net.ranzer.caexbot.commands.chat.*;
import net.ranzer.caexbot.commands.games.DiceCommand;
import net.ranzer.caexbot.commands.games.HiLowCommand;
import net.ranzer.caexbot.commands.music.MusicCommand;
import net.ranzer.caexbot.commands.search.YoutubeSearchCommand;

public class CommandListener extends ListenerAdapter {
	private static CommandListener cl;
	private List<CaexCommand> cmds = new ArrayList<CaexCommand>();
	private DraconicListener dl;
	
	public static CommandListener getInstance(){
		if (cl==null) cl = new CommandListener();
		return cl;
	}
	
	private CommandListener() {
		dl = DraconicListener.getInstance();
		this.addCommand(new HelpCommand())
			.addCommand(new InsultCommand())
			.addCommand(new DiceCommand())
			.addCommand(new DraconicTranslateCommand())
			.addCommand(new EightBallCommand())
			.addCommand(new FacepalmCommand())
			.addCommand(new FistbumpCommand())
			.addCommand(new InfoCommand())
			.addCommand(new LevelCommand())
			.addCommand(new PingCommand())
			.addCommand(new ShutdownCommand())
			.addCommand(new YoutubeSearchCommand())
//			.addCommand(new ZomDiceCommand()) TODO handle cross guild play
			.addCommand(new PrefixCommand())
			.addCommand(new XPPermCommand())
			.addCommand(new MusicCommand())
			.addCommand(new HiLowCommand())
			.addCommand(new LevelAlertToggleCommand())
			.addCommand(new AnnouncementSettingCommand())
			.addCommand(new JoinLeaveSettingCommand())
			.addCommand(new XpGiftCommand());
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
		String message = event.getMessage().getContentRaw();
		
		if(!message.toLowerCase().startsWith(CaexCommand.getPrefix(event.getGuild())))
			return;
		findCommand(event, author, message); 
	}

	private boolean containsKeyWord(MessageReceivedEvent event) {
		List<String> keywords = Arrays.asList("prefix", "help", "command", "code");
		
		for (String string : keywords) {
			if(event.getMessage().getContentDisplay().contains(string))
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
