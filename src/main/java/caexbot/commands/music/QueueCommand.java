package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import caexbot.commands.CaexSubCommand;
import caexbot.commands.search.YouTubeSearcher;
import caexbot.functions.music.GuildPlayerManager;
import caexbot.functions.music.TrackQueue;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class QueueCommand extends CaexSubCommand {

	private static final String NO_VIDEO_FOUND = "i'm sorry I didn't find that"; 

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {

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
			GuildPlayerManager.getInstance().getPlayerManager().loadItem(videoID, new AudioLoadResultHandler() {
				
				@Override
				public void trackLoaded(AudioTrack track) {
					GuildPlayerManager.getInstance().getQueue(event.getGuild()).add(track);
					
				}
				
				@Override
				public void playlistLoaded(AudioPlaylist playlist) {
					TrackQueue queue = GuildPlayerManager.getInstance().getQueue(event.getGuild());
					for (AudioTrack track : playlist.getTracks()) {
						queue.add(track);
					}
					
				}
				
				@Override
				public void noMatches() {
					channel.sendMessage(NO_VIDEO_FOUND)	;			
				}
				
				@Override
				public void loadFailed(FriendlyException exception) {
					// TODO LoadFailed exception handling
					
				}
			});
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
