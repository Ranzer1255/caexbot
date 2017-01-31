package caexbot.commands.search;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import caexbot.config.CaexConfiguration;
import caexbot.util.Logging;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class YoutubeSearchCommand extends CaexCommand implements Describable{

	private static final String YOUTUBE_BASE_STRING = "https://youtu.be/";
	private YouTubeSearcher yts;
	
	public YoutubeSearchCommand() {
		yts = new YouTubeSearcher();
	}
	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		StringBuilder queryBuilder = new StringBuilder();
		
		for (int i = 0; i < args.length; i++) {
			queryBuilder.append(args[i]).append(" ");
		}
		String videoID = yts.searchForVideo(queryBuilder.toString());


		Logging.debug(queryBuilder.toString()+" : " +videoID);
		
		if(videoID!=null){
			StringBuilder youtubeURL = new StringBuilder();
			youtubeURL.append(YOUTUBE_BASE_STRING).append(videoID);
			channel.sendMessage(author.getAsMention() + " "+ youtubeURL.toString()).queue();
		} else {
			channel.sendMessage("I'm sorry, i didn't find anything").queue();			


		}

		

	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("youtube", "yt");
	}

	@Override
	public String getShortDescription() {
		return "Search YouTube for Videos!";
	}

	@Override
	public String getLongDescription() {
		// TODO make getLongDescription
		return getShortDescription();
	}
	
	@Override
	public String getUsage(Guild g) {
		return getPrefix(g)+getAlias().get(0) + " <your search terms>";//TODO match usage format
	}
	
	@Override
	public Catagory getCatagory() {
		return Catagory.SEARCH;
	}

}
