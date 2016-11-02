package caexbot.commands.music;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import caexbot.commands.CaexCommand;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class MusicCommand extends CaexCommand {

	List<CaexCommand> subCommands;
	
	public MusicCommand() {
		subCommands = new ArrayList<>();
		subCommands.add(new QueueCommand());
		subCommands.add(new PlayCommand());
		subCommands.add(new PauseCommand());
		subCommands.add(new StopCommand());
		subCommands.add(new Skipcommand());
		subCommands.add(new VolCommand());
	}
	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		if(args.length!=1){
			channel.sendMessage(author.getAsMention() + getUsage());
		}
		
		Optional<CaexCommand> c = subCommands.stream().filter(cc -> cc.getAlias().contains(args[0])).findFirst();
		
		if(!c.isPresent()){
			channel.sendMessage(invalidUsage());
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return null;
	}

}
