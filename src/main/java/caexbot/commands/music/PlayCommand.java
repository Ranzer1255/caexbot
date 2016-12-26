package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.functions.music.GuildPlayer;
import caexbot.functions.music.GuildPlayerManager;
import caexbot.util.StringUtil;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PlayCommand extends CaexCommand {

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {

		GuildPlayer player = GuildPlayerManager.getPlayer(event.getGuild());
		if(args.length>0){
//			channel.sendMessage("searching...").queue();//breadcrumb
			player.queueSearch(StringUtil.arrayToString(Arrays.asList(args), " "));
		}
		
		if(!player.isConnected()){
//			channel.sendMessage("joining...").queue();//breadcrumb
			player.join(event.getGuild().getMember(author).getVoiceState().getChannel());
		}
		
		player.start();
//		channel.sendMessage("playing...").queue();//bread crumb
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("play","p");
	}

	@Override
	public String getDescription() {
		return "Start playing the first song in queue";
	}

	@Override
	public String getUsage(Guild g) {
		// TODO Auto-generated method stub
		return null;
	}

}
