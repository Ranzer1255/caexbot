package net.ranzer.caexbot.commands.search;

import java.util.Arrays;
import java.util.List;

import net.ranzer.caexbot.commands.CaexCommand;
import net.ranzer.caexbot.commands.Catagory;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.util.Logging;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class YoutubeSearchCommand extends CaexCommand implements Describable{

	private static final String YOUTUBE_BASE_STRING = "https://youtu.be/";
	private YouTubeSearcher yts;
	
	public YoutubeSearchCommand() {
		yts = new YouTubeSearcher();
	}
	@Override
	public void process(String[] args, MessageReceivedEvent event) {
		StringBuilder queryBuilder = new StringBuilder();
		
		for (int i = 0; i < args.length; i++) {
			queryBuilder.append(args[i]).append(" ");
		}
		String videoID = yts.searchForVideo(queryBuilder.toString());


		Logging.debug(queryBuilder.toString()+" : " +videoID);
		
		if(videoID!=null){
			StringBuilder youtubeURL = new StringBuilder();
			youtubeURL.append(YOUTUBE_BASE_STRING).append(videoID);
			event.getChannel().sendMessage(event.getAuthor().getAsMention() + " "+ youtubeURL.toString()).queue();
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
	public Catagory getCatagory() {
		return Catagory.SEARCH;
	}
	@Override
	public boolean isAplicableToPM() {
		return true;
	}

}
