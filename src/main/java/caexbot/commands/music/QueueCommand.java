package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import caexbot.commands.CaexSubCommand;
import caexbot.functions.music.GuildPlayerManager;
import caexbot.util.StringUtil;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class QueueCommand extends CaexSubCommand {

//	private static final String NO_VIDEO_FOUND = "i'm sorry I didn't find that"; //TODO use this

	@Override
	public void process(String[] args, User author, TextChannel channel, net.dv8tion.jda.core.events.message.MessageReceivedEvent event) {
		if (args.length<1) {
			for(AudioTrack t: GuildPlayerManager.getPlayer(event.getGuild()).getQueue().getQueue()){
				System.out.println(t.getIdentifier()+" "+t.getInfo().title);
			}
//			invalidUsage(event.getGuild());
			return;
		}
		GuildPlayerManager.getPlayer(event.getGuild()).queue(StringUtil.arrayToString(Arrays.asList(args), " "));
		channel.sendMessage("Video added").queue();//TODO handle confirmation differently (event handling in music player)
		
		
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("add", "queue");
	}

	@Override
	public String getDescription() {
		return "Add song to the play queue";
	}

}
