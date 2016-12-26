package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import caexbot.commands.CaexSubCommand;
import caexbot.functions.music.GuildPlayer;
import caexbot.functions.music.GuildPlayerManager;
import caexbot.util.StringUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class QueueCommand extends CaexSubCommand {

//	private static final String NO_VIDEO_FOUND = "i'm sorry I didn't find that"; //TODO use this

	@Override
	public void process(String[] args, User author, TextChannel channel, net.dv8tion.jda.core.events.message.MessageReceivedEvent event) {
		if (args.length<1) {
			GuildPlayer gp = GuildPlayerManager.getPlayer(event.getGuild());
			EmbedBuilder eb = new EmbedBuilder();
			
			if(gp.getPlayingTrack()!=null){
				eb.setAuthor("Currently Playing", null, null);
				eb.setTitle(gp.getPlayingTrack().getInfo().title+"\nhttp://youtu.be/"+gp.getPlayingTrack().getIdentifier()+" by "+gp.getPlayingTrack().getInfo().author);
				StringBuilder sb = new StringBuilder();
				
				sb.append("Queue:\n");
				int i = 1;
				long runtime = 0;
				for (AudioTrack track : gp.getQueue().getQueue()) {
					if(i>10) break;
					sb.append(String.format("%d: [%s](%s)\n", i++, track.getInfo().title, "http://youtu.be/"+track.getIdentifier()));
				}
				eb.setDescription(sb.toString());
				
				for(AudioTrack track:gp.getQueue().getQueue()){
					runtime += track.getDuration();
				}
				runtime += gp.getPlayingTrack().getDuration();
				eb.setFooter("Estimated Runtime: "+StringUtil.calcTime(runtime/1000), null);
				
			}
			MessageBuilder mb = new MessageBuilder();
			channel.sendMessage(mb.setEmbed(eb.build()).build()).queue();
			
			String cp =  gp.getPlayingTrack()!=null ?  gp.getPlayingTrack().getInfo().title : "null";
			System.out.print("Currently playing: ");
			System.out.println(cp);
			System.out.println("Queue:");
			int i=1;
			for(AudioTrack t: GuildPlayerManager.getPlayer(event.getGuild()).getQueue().getQueue()){
				
				System.out.println(i +": "+t.getIdentifier()+" "+t.getInfo().title);
				i++;
			}
//			invalidUsage(event.getGuild());
			return;
		}
		if (args[0].startsWith(getPrefix(event.getGuild()))) {//test code TODO handle this better
			GuildPlayerManager.getPlayer(event.getGuild()).queueID(args[0].substring(getPrefix(event.getGuild()).length(), args[0].length()));
		} else {
			GuildPlayerManager.getPlayer(event.getGuild()).queueSearch(StringUtil.arrayToString(Arrays.asList(args), " "));
			channel.sendMessage("Video added").queue();//TODO handle confirmation differently (event handling in music player)
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
