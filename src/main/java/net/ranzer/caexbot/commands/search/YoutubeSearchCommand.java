package net.ranzer.caexbot.commands.search;

import java.util.Arrays;
import java.util.List;

import net.ranzer.caexbot.commands.BotCommand;
import net.ranzer.caexbot.commands.Category;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.util.Logging;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class YoutubeSearchCommand extends BotCommand implements Describable{

	private static final String YOUTUBE_BASE_STRING = "https://youtu.be/";
	private final YouTubeSearcher yts;
	
	public YoutubeSearchCommand() {
		yts = new YouTubeSearcher();
	}
	@Override
	public void processPrefix(String[] args, MessageReceivedEvent event) {
		StringBuilder queryBuilder = new StringBuilder();

		for (String arg : args) {
			queryBuilder.append(arg).append(" ");
		}
		String videoID = yts.searchForVideo(queryBuilder.toString());

		Logging.debug(queryBuilder+" : " +videoID);
		
		if(videoID!=null){
			event.getChannel().sendMessage(event.getAuthor().getAsMention() + " " + YOUTUBE_BASE_STRING + videoID).queue();
		} else {
			event.getChannel().sendMessage("I'm sorry, i didn't find anything").queue();
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
		return getShortDescription()+"\n\n"+
				"Enter your Search Query after the command and caex will return the top item returned by youtube";
	}
	
	@Override
	public String getUsage(Guild g) {
		return "`"+getPrefix(g)+getName() + " <your search terms>`";
	}
	
	@Override
	public Category getCategory() {
		return Category.SEARCH;
	}
	@Override
	public boolean isApplicableToPM() {
		return true;
	}

}
