package net.ranzer.caexbot.commands.search;

import java.io.IOException;
import java.util.List;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import net.ranzer.caexbot.config.CaexConfiguration;
import net.ranzer.caexbot.util.Logging;

public class YouTubeSearcher {

	private YouTube yt;
	private CaexConfiguration config;
	
	public YouTubeSearcher() {
		yt = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
			
			@Override
			public void initialize(HttpRequest request) throws IOException {}//no-op
		}).setApplicationName("CaexBot").build();
		config=CaexConfiguration.getInstance();

	}
	
	/**
	 * finds the first video returned by your search string
	 * 
	 * @param query search query to pass to the youtube search
	 * @return videoID of the first video found
	 */
	public String searchForVideo(String query){
				
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
				return resultList.get(0).getId().getVideoId();
			}
			return null;
			
		} catch (IOException e) {
			Logging.error(e.getMessage());
			Logging.log(e);
			return null;
		}
	}
}
