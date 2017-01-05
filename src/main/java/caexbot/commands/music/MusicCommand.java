package caexbot.commands.music;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import caexbot.commands.CaexCommand;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MusicCommand extends CaexCommand {

	public static final String JOIN = "Joining Channel %s";
	public static final String ADD = "Adding song to queue:\n%s";
	
	List<CaexCommand> subCommands;
	
	public MusicCommand() {
		
		subCommands = new ArrayList<>();
		subCommands.add(new JoinCommand());
		subCommands.add(new QueueCommand());
		subCommands.add(new PlayCommand());
		subCommands.add(new PauseCommand());
		subCommands.add(new StopCommand());
		subCommands.add(new SkipCommand());
		subCommands.add(new VolCommand());
		
		
	}
	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		System.out.println("in muisc");
		if(args.length!=1){
			channel.sendMessage(author.getAsMention() + getUsage(event.getGuild()));
		}
		
		Optional<CaexCommand> c = subCommands.stream().filter(cc -> cc.getAlias().contains(args[0])).findFirst();
		
		if(!c.isPresent()){
			channel.sendMessage(invalidUsage(event.getGuild()));
			return;
		}
		
		c.get().runCommand(Arrays.copyOfRange(args, 1, args.length), author, channel, event);
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("music", "m");
	}

	@Override
	public String getDescription() {
		return "Play music!";
	}
	@Override
	public String getUsage(Guild g) {
		// TODO make getUsage
		return null;
	}

}
