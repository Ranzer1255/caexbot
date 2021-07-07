package net.ranzer.caexbot.functions.music.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import net.ranzer.caexbot.commands.BotCommand;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.commands.admin.HelpCommand;
import net.ranzer.caexbot.util.Logging;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MusicCommand extends AbstractMusicCommand implements Describable {

	public static final String JOIN = "Joining Channel %s";
	public static final String ADD = "Adding song to queue:\n%s";
	public static final String NOW_PLAYING = "Now playing:\n%s";

	private static final List<BotCommand> subCommands;

	static {
		subCommands = new ArrayList<>();
		subCommands.add(new JoinCommand());
		subCommands.add(new QueueCommand());
		subCommands.add(new InsertCommand());
		subCommands.add(new PlayCommand());
		subCommands.add(new PauseCommand());
		subCommands.add(new PlaylistCommand());
		subCommands.add(new StopCommand());
		subCommands.add(new SkipCommand());
		subCommands.add(new VolCommand());
		subCommands.add(new ShuffleCommand());
		subCommands.add(new NowPlayingCommand());
	}

	public MusicCommand() {

	}

	@Override
	public void processPrefix(String[] args, MessageReceivedEvent event) {
		if (args.length == 0) {
			event.getTextChannel().sendMessage(new MessageBuilder().setEmbeds(HelpCommand.getDescription(this, event.getGuild())).build()).queue();
			return;
		}

		Optional<BotCommand> c = subCommands.stream().filter(cc -> cc.getAlias().contains(args[0])).findFirst();

		// Silent failure of miss-typed subcommands
		if (!c.isPresent()) {
			Logging.debug("no music subcommand");
//			channel.sendMessage(invalidUsage(event.getGuild()));
			return;
		}
		Logging.debug("Music Subclass: "+c.get().getName());
		setMusicChannel(event.getTextChannel());
		c.get().runPrefixCommand(Arrays.copyOfRange(args, 1, args.length), event);
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("music", "m");
	}

	@Override
	public String getShortDescription() {
		return "Play music!";
	}
	
	@Override
	public String getLongDescription() {
		StringBuilder sb = new StringBuilder();
		
		
		sb.append(getShortDescription()).append("\n\n");
		
		for (BotCommand cmd : subCommands) {
			sb.append(
				String.format("**%s**: %s\n", cmd.getName(), ((Describable)cmd).getShortDescription())
			);
		}
		
		return sb.toString();
	}
	
	@Override
	public String getUsage(Guild g) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("`%s%s {", getPrefix(g), getName()));
		for(BotCommand cmd : subCommands){
			sb.append(String.format("%s|", cmd.getName()));
		}
		sb.delete(sb.length()-1,sb.length());
		sb.append("}`");
				
		return sb.toString();
	}
	
	@Override
	public boolean hasSubcommands() {
		return true;
	}
	
	@Override
	public List<BotCommand> getSubcommands() {
		return subCommands;
	}

}
