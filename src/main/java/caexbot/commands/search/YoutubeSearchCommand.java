package caexbot.commands.search;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import caexbot.commands.CaexCommand;
import caexbot.config.CaexConfiguration;
import caexbot.util.Logging;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class YoutubeSearchCommand extends CaexCommand {

	private static final String YOUTUBE_BASE_STRING = "https://youtu.be/";
	private CaexConfiguration config;
	
	public YoutubeSearchCommand() {
		config = CaexConfiguration.getInstance();
	}
	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		StringBuilder queryBuilder = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			queryBuilder.append(args[i]).append(" ");
		}
		String query = queryBuilder.toString();
		Logging.debug(query);

		YouTube yt = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
			
			@Override
			public void initialize(HttpRequest request) throws IOException {}//no-op
		}).setApplicationName("CaexBot").build();
		
		try {
			YouTube.Search.List search = yt.search().list("snippet");
			search.setKey(config.getGToken());
			search.setQ(query);
			search.setType("video");
			
			SearchListResponse response = search.execute();
			List<SearchResult> resultList = response.getItems();
			for (SearchResult searchResult : resultList) {
				Logging.debug(searchResult.getId().getKind()+" "+searchResult.getSnippet().getTitle());
			}
			if (resultList.size()>0){
				StringBuilder youtubeURL = new StringBuilder().append(YOUTUBE_BASE_STRING)
						.append(resultList.get(0).getId().getVideoId());
				channel.sendMessage(author.getAsMention() + " "+ youtubeURL.toString()).queue();
			} else {
				channel.sendMessage("I'm sorry, i didnt find anything").queue();
			}
			
		} catch (IOException e) {
			Logging.error(e.getMessage());
			Logging.log(e);
		}
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("youtube", "yt");
	}

	@Override
	public String getDescription() {
		return "Search YouTube for your Videos!";
	}

	@Override
	public String getUsage(Guild g) {
		return getPrefix(g)+getAlias().get(0) + " <your search terms>";//TODO match usage format
	}

}
