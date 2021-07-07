package net.ranzer.caexbot.functions.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.ranzer.caexbot.CaexBot;
import net.ranzer.caexbot.commands.BotCommand;
import net.ranzer.caexbot.commands.DraconicCommand;
import net.ranzer.caexbot.commands.admin.*;
import net.ranzer.caexbot.commands.chat.*;
import net.ranzer.caexbot.commands.games.DiceCommand;
import net.ranzer.caexbot.commands.games.HiLowCommand;
import net.ranzer.caexbot.functions.music.commands.MusicCommand;
import net.ranzer.caexbot.commands.search.YoutubeSearchCommand;
import net.ranzer.caexbot.util.Logging;
import org.apache.commons.logging.Log;
import org.jetbrains.annotations.NotNull;

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

		List<CommandData> slashCmds = new ArrayList<>();
		for(BotCommand cmd :cmds){
			if(cmd.getCommandData()==null) continue;
			slashCmds.add(cmd.getCommandData());
		}

		for(Guild g: CaexBot.getJDA().getGuilds()){
			g.updateCommands().addCommands(slashCmds).queue();
		}
	}
	
	public CommandListener addCommand(BotCommand cmd){
		this.cmds.add(cmd);
		if (cmd instanceof DraconicCommand) dl.addCommand((DraconicCommand)cmd);
		return this;
	}

	@Override
	public void onSlashCommand(@NotNull SlashCommandEvent event) {
		Logging.debug("looking for a slash command");
		Optional<BotCommand> c = cmds.stream().filter(cmd -> cmd.getName().equals(event.getName())).findFirst();
		if (c.isPresent()) {
			Logging.debug("found one");
			for (OptionMapping o: event.getOptions()){
				Logging.debug(String.format("%s: %s",o.getName(),o.getAsString()));
			}
		}
		c.ifPresent(botCommand -> callSlashCommand(event,botCommand));
		//todo find the right command and call it
		//this will require rewriting the command system to not
		//require a "MessageReceivedEvent" to be sent down the line
		//perhaps have the command just send the message text back?
		//or a message builder with the required text and embeds
		//this way it will be sent as a reply if necessary by this method
		//or an actual message if in onMessageReceived()
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
				return;
			}
		}

		//setup required variables
		String message = event.getMessage().getContentRaw();
		String[] args = parseArgs(message);
		Optional<BotCommand> c;

		//find source of message and find command, if any
		if(event.isFromGuild()){
			String prefix = BotCommand.getPrefix(event.getGuild());
			if(!message.toLowerCase().startsWith(prefix)) return;
			c = findCommand(args[0].replace(prefix,""));
		} else {
			c = findCommand(args[0]);
		}

		//if command exists, fire the legacy Prefix logic
		if(c.isPresent()){
			BotCommand cmd = c.get();
			callPrefixCommand(event,Arrays.copyOfRange(args, 1, args.length),  cmd);
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

	private Optional<BotCommand> findCommand(String command) {
		return cmds.stream().filter(cc -> cc.getAlias().contains(command)).findFirst();
	}

	private String[] parseArgs(String message) {

		List<String> list = new ArrayList<>();

		//splits the message on whitespaces but anything in quotes stays at one argument
		Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(message);
		while (m.find())
			list.add(m.group(1).replace("\"",""));

		return list.toArray(new String[0]);
	}

	protected void callPrefixCommand(MessageReceivedEvent event, String[] finalArgs,
									 BotCommand cmd) {
		new Thread() {
			@Override
			public void run(){
				cmd.runPrefixCommand(finalArgs, event);
				interrupt();
			}
		}.start();
	}

	private void callSlashCommand(SlashCommandEvent event, BotCommand cmd){
		new Thread(){
			@Override
			public void run() {
				cmd.runSlashCommand(event);
				interrupt();
			}
		}.start();
	}

	public List<BotCommand> getCommands() {
		
		return cmds;
	}

}
