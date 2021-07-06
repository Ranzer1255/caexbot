package net.ranzer.caexbot.functions.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.ranzer.caexbot.commands.BotCommand;
import net.ranzer.caexbot.commands.DraconicCommand;
import net.ranzer.caexbot.commands.admin.*;
import net.ranzer.caexbot.commands.chat.*;
import net.ranzer.caexbot.commands.games.DiceCommand;
import net.ranzer.caexbot.commands.games.HiLowCommand;
import net.ranzer.caexbot.functions.music.commands.MusicCommand;
import net.ranzer.caexbot.commands.search.YoutubeSearchCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandListener extends ListenerAdapter {
	private static CommandListener cl;
	private final List<BotCommand> cmds = new ArrayList<>();
	private final DraconicListener dl;
	
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
	
	public CommandListener addCommand(BotCommand cmd){
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
						BotCommand.getPrefix(event.getGuild()),
						BotCommand.getPrefix(event.getGuild())
								)).queue();
			}
		}

		String message = event.getMessage().getContentRaw();

		if(event.isFromGuild()){
			if(!message.toLowerCase().startsWith(BotCommand.getPrefix(event.getGuild()))) {
				return;
			}
			findCommand(event, BotCommand.getPrefix(event.getGuild()), message);
		} else {
			findCommand(event, "", message);
		}
	}

	private boolean containsKeyWord(MessageReceivedEvent event) {
		List<String> keywords = Arrays.asList("prefix", "help", "command", "code");
		
		for (String string : keywords) {
			if(event.getMessage().getContentDisplay().contains(string))
				return true;
		}
		return false;
	}

	private void findCommand(MessageReceivedEvent event, String prefix, String message) {

		String[] args = parseArgs(message);
		String command = args[0].toLowerCase().replace(prefix, "");
		String[] finalArgs = Arrays.copyOfRange(args, 1, args.length);
		Optional<BotCommand> c = cmds.stream().filter(cc -> cc.getAlias().contains(command)).findFirst();

		if(c.isPresent()){
			BotCommand cmd = c.get();

			callCommand(event,finalArgs,  cmd);
		}

	}
	private String[] parseArgs(String message) {
//		return message.split(" ");

		List<String> list = new ArrayList<String>();

		//splits the message on whitespaces but anything in quotes stays at one argument
		Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(message);
		while (m.find())
			list.add(m.group(1).replace("\"",""));

		return list.toArray(new String[0]);
	}

	protected void callCommand(MessageReceivedEvent event, String[] finalArgs, 
			BotCommand cmd) {
		new Thread() {
			@Override
			public void run(){
				cmd.runCommand(finalArgs, event);
				interrupt();
			}
		}.start();
	}

	public List<BotCommand> getCommands() {
		
		return cmds;
	}

}
