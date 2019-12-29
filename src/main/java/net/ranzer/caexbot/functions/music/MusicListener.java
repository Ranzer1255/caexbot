package net.ranzer.caexbot.functions.music;

import net.ranzer.caexbot.CaexBot;
import net.ranzer.caexbot.commands.CaexCommand;
import net.ranzer.caexbot.commands.music.MusicCommand;
import net.ranzer.caexbot.functions.music.events.LoadFailedEvent;
import net.ranzer.caexbot.functions.music.events.MusicEvent;
import net.ranzer.caexbot.functions.music.events.MusicJoinEvent;
import net.ranzer.caexbot.functions.music.events.MusicLoadEvent;
import net.ranzer.caexbot.functions.music.events.MusicPausedEvent;
import net.ranzer.caexbot.functions.music.events.MusicSkipEvent;
import net.ranzer.caexbot.functions.music.events.MusicStartEvent;
import net.ranzer.caexbot.functions.music.events.NoMatchEvent;
import net.ranzer.caexbot.functions.music.events.PermErrorEvent;
import net.ranzer.caexbot.functions.music.events.PlaylistLoadEvent;
import net.ranzer.caexbot.functions.music.events.ShuffleEvent;
import net.ranzer.caexbot.functions.music.events.VolumeChangeEvent;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class MusicListener implements MusicEventListener{
	private Guild guild;
	private TextChannel lastMusicChannel;

	public MusicListener(Guild g) {
		guild = g;
	}

	public TextChannel getMusicChannel() {
		TextChannel mc = CaexBot.GUILD_MANAGER.getGuildData(guild).getDefaultMusicChannel();

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
		
		else if (event instanceof ShuffleEvent){
			getMusicChannel().sendMessage("*throws all the tracks up in the air....*").queue();
		}
		
		else if (event instanceof LoadFailedEvent){
			getMusicChannel().sendMessage(String.format("There was a problem loading that song sorry!\n(%s)", ((LoadFailedEvent) event).getException().getMessage())).queue();
		}
		
		else if (event instanceof NoMatchEvent){
			getMusicChannel().sendMessage("I'm sorry, but i didn't find anything matching that search").queue();
		}
		
		else if (event instanceof PermErrorEvent){
			getMusicChannel().sendMessage(
					String.format("I'm sorry I dont have permission to join %s\n"
							+ "(If this is in error insure I have `%s` and `Speak` in this channel)",
							((PermErrorEvent) event).getVoiceChannel().getName(),
							((PermErrorEvent) event).getException().getPermission().getName())
			).queue();
		}
		
		else{
			getMusicChannel().sendMessage("This music event isn't hanndled yet.... Yell at ranzer ("+event.getClass().getSimpleName()+")").queue();
		
		}
		
		
	}

	private CharSequence volumeBar(int vol) {
		StringBuilder rtn = new StringBuilder();
		rtn.append("*|");
		
		//not boosted
		if (vol<=100) {
			
			//number of bars to add (if the math comes out to neg set to 0
			int volBars = ((vol / 4)-2)>0?(vol / 4)-2:0;
			
			//add bars
			for (int i = 0; i < volBars; i++) {
				rtn.append('=');
			}
			rtn.append('|');
			
			//add blank space
			for (int i = 0; i < 23 - volBars; i++) {
				rtn.append(' ');
			}
			
			//fill out blank boost space
			rtn.append("*          *");
			
		//boosted volume
		} else {
			int boost = vol-100;
			int boostBars = boost/5;
			
			//fill in full standard bar
			rtn.append("========================*");
			
			//add boost bars
			for (int i = 0; i<boostBars-1;i++){
				rtn.append('=');
			}
			rtn.append("|");
			
			//add blank space
			for (int i = 0; i<10-boostBars;i++){
				rtn.append(" ");
			}
			rtn.append('*');
		}
		return rtn.toString();
	}
}