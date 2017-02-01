package caexbot.functions.music;

import caexbot.commands.music.MusicCommand;
import caexbot.functions.music.events.MusicEvent;
import caexbot.functions.music.events.MusicJoinEvent;
import caexbot.functions.music.events.MusicLoadEvent;
import caexbot.functions.music.events.MusicSkipEvent;
import caexbot.functions.music.events.MusicStartEvent;
import caexbot.functions.music.events.PlaylistLoadEvent;
import net.dv8tion.jda.core.entities.TextChannel;

public class MusicListener implements MusicEventListener{
	private TextChannel musicChannel;

	public MusicListener() {
	}

	public TextChannel getMusicChannel() {
		return musicChannel;
	}

	public void setMusicChannel(TextChannel musicChannel) {
		this.musicChannel = musicChannel;
	}

	@Override
	public void handleEvent(MusicEvent event) {
		
		
		if(event instanceof MusicJoinEvent){
			getMusicChannel().sendMessage(String.format(MusicCommand.JOIN, ((MusicJoinEvent) event).getChannelJoined().getName())).queue();
		}
		
		else if (event instanceof MusicStartEvent){
			getMusicChannel().sendMessage(String.format(MusicCommand.NOW_PLAYING, "youtu.be/"+((MusicStartEvent) event).getSong().getIdentifier())).queue();
		}
		
		else if (event instanceof MusicSkipEvent){
			getMusicChannel().sendMessage("Skipping the rest of the Current song :stuck_out_tongue:").queue();
		}
		
		else if (event instanceof MusicLoadEvent){
			getMusicChannel().sendMessage(String.format("Loaded %s sucssesfuly\n%s", 
					((MusicLoadEvent) event).getSong().getInfo().title, "youtu.be/"+((MusicLoadEvent) event).getSong().getIdentifier())).queue();
		}
		
		else if (event instanceof PlaylistLoadEvent){
			getMusicChannel().sendMessage(String.format("Loaded Playlist: %s",
					((PlaylistLoadEvent) event).getList().getName())).queue();
		}
		
		else{
			getMusicChannel().sendMessage("This music event isn't hanndled yet.... Yell at ranzer ("+event.getClass().getSimpleName()+")").queue();
		
		}
		
		
	}
}