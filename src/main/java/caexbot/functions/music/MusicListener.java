package caexbot.functions.music;

import caexbot.commands.CaexCommand;
import caexbot.commands.music.MusicCommand;
import caexbot.data.GuildManager;
import caexbot.functions.music.events.MusicEvent;
import caexbot.functions.music.events.MusicJoinEvent;
import caexbot.functions.music.events.MusicLoadEvent;
import caexbot.functions.music.events.MusicPausedEvent;
import caexbot.functions.music.events.MusicSkipEvent;
import caexbot.functions.music.events.MusicStartEvent;
import caexbot.functions.music.events.PlaylistLoadEvent;
import caexbot.functions.music.events.VolumeChangeEvent;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

public class MusicListener implements MusicEventListener{
	private Guild guild;
	private TextChannel lastMusicChannel;

	public MusicListener(Guild g) {
		guild = g;
	}

	public TextChannel getMusicChannel() {
		TextChannel mc = GuildManager.getGuildData(guild).getDefaultMusicChannel();
		
		if (mc==null){
			mc = lastMusicChannel;
		}
		
		return mc;
	}

	public void setMusicChannel(TextChannel musicChannel) {
		lastMusicChannel=musicChannel;
	}

	@Override
	public void handleEvent(MusicEvent event) {
		
		
		if(event instanceof MusicJoinEvent){
			getMusicChannel().sendMessage(String.format(MusicCommand.JOIN, ((MusicJoinEvent) event).getChannelJoined().getName())).queue();
		}
		
		else if (event instanceof MusicStartEvent){
			getMusicChannel().sendMessage(String.format(MusicCommand.NOW_PLAYING, ((MusicStartEvent) event).getSong().getInfo().uri)).queue();
		}
		
		else if (event instanceof MusicSkipEvent){
			getMusicChannel().sendMessage("Skipping the rest of the Current song :stuck_out_tongue:").queue();
		}
		
		else if (event instanceof MusicLoadEvent){
			getMusicChannel().sendMessage(String.format("Loaded %s successfully\n%s", 
					((MusicLoadEvent) event).getSong().getInfo().title, ((MusicLoadEvent) event).getSong().getInfo().uri)).queue();
		}
		
		else if (event instanceof PlaylistLoadEvent){
			getMusicChannel().sendMessage(String.format("Loaded Playlist: %s",
					((PlaylistLoadEvent) event).getList().getName())).queue();
		}
		
		else if (event instanceof MusicPausedEvent) {
			if (!((MusicPausedEvent) event).getPaused()) {
				getMusicChannel().sendMessage(String.format("Music paused. call `%sm play` or `%sm pause` to resume",
						CaexCommand.getPrefix(getMusicChannel().getGuild()),
						CaexCommand.getPrefix(getMusicChannel().getGuild()))).queue();
			}
			
		}
		
		else if (event instanceof VolumeChangeEvent) {
			MessageBuilder mb = new MessageBuilder();
			
			mb.append(String.format("Volume set to %d\n",((VolumeChangeEvent) event).getVol()));
			mb.append("```\n");
			mb.append("*-------------------------*--boost---*\n");
			mb.append(volumeBar(((VolumeChangeEvent) event).getVol())+"\n");
			mb.append("*-------------------------*----------*\n");
			mb.append("```");
			
			
			getMusicChannel().sendMessage(mb.build()).queue();
			
		}
		
		else{
			getMusicChannel().sendMessage("This music event isn't hanndled yet.... Yell at ranzer ("+event.getClass().getSimpleName()+")").queue();
		
		}
		
		
	}

	private CharSequence volumeBar(int vol) {
		StringBuilder rtn = new StringBuilder();
		rtn.append("*|");
		if (vol<=100) {
			int volBars = vol / 4;
			for (int i = 0; i < volBars - 2; i++) {
				rtn.append('=');
			}
			rtn.append('|');
			for (int i = 0; i < 25 - volBars; i++) {
				rtn.append(' ');
			}
			rtn.append("*          *");
		} else {
			int boost = vol-100;
			int boostBars = boost/5;
			rtn.append("========================*");
			for (int i = 0; i<boostBars-1;i++){
				rtn.append('=');
			}
			rtn.append("|");
			for (int i = 0; i<10-boostBars;i++){
				rtn.append(" ");
			}
			rtn.append('*');
		}
		return rtn.toString();
	}
}