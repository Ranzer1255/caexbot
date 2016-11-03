package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexSubCommand;
import caexbot.commands.search.YouTubeSearcher;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class QueueCommand extends CaexSubCommand {

	private static final String NO_VIDEO_FOUND = "i'm sorry I didn't find that"; 

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {

		/*TODO Queue command process
		 *add id to track queue 
		 */
		if (args.length<1) {
			invalidUsage();
			return;
		}
		
		StringBuilder queryBuilder = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			queryBuilder.append(args[i]);
		} 
		YouTubeSearcher yts = new YouTubeSearcher();
		String videoID = yts.searchForVideo(queryBuilder.toString());
		
		if (videoID!=null) {
			//TODO add to queue
		} else {
			channel.sendMessage(NO_VIDEO_FOUND);
		}
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
