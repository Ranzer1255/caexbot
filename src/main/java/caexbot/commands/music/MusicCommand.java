package caexbot.commands.music;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import caexbot.commands.CaexCommand;
import caexbot.commands.Describable;
import caexbot.util.Logging;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/*
 * TODO redesign music so that its not a subcommand list
 * i want to just do !join and not !music join ect.
 */
public class MusicCommand extends AbstractMusicCommand implements Describable {

	public static final String JOIN = "Joining Channel %s";
	public static final String ADD = "Adding song to queue:\n%s";
	public static final String NOW_PLAYING = "Now playing:\n%s";

	private static List<CaexCommand> subCommands;

	static {
		subCommands = new ArrayList<>();
		subCommands.add(new JoinCommand());
		subCommands.add(new QueueCommand());
		subCommands.add(new InsertCommand());
		subCommands.add(new PlayCommand());
		subCommands.add(new PauseCommand());
		subCommands.add(new StopCommand());
		subCommands.add(new SkipCommand());
		subCommands.add(new VolCommand());
		subCommands.add(new ShuffleCommand());
	}

	public MusicCommand() {

	}

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		System.out.println("in muisc");
		if (args.length != 1) {
			channel.sendMessage(author.getAsMention() + getUsage(event.getGuild()));
		}

		Optional<CaexCommand> c = subCommands.stream().filter(cc -> cc.getAlias().contains(args[0])).findFirst();

		// Silent failure of miss-typed subcommands
		if (!c.isPresent()) {
			Logging.debug("no music subcommand");
//			channel.sendMessage(invalidUsage(event.getGuild()));
			return;
		}
		Logging.debug("Music Subclass: "+c.get().getName());
		setMusicChannel(channel);
		c.get().runCommand(Arrays.copyOfRange(args, 1, args.length), author, channel, event);
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
		
		
		sb.append(getShortDescription() +"\n\n");
		
		for (CaexCommand cmd : subCommands) {
			sb.append(
				String.format("`%s`: %s\n", cmd.getName(), ((Describable)cmd).getShortDescription())
			);
		}
		
		return sb.toString();
	}
	
	@Override
	public String getUsage(Guild g) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("`%s%s {", getPrefix(g), getName()));
		for(CaexCommand cmd : subCommands){
			sb.append(String.format("%s | ", cmd.getName()));
		}
		sb.delete(sb.length()-3,sb.length());
		sb.append("}`");
				
		return sb.toString();
	}

}
