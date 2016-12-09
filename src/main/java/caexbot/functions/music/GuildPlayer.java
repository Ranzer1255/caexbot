package caexbot.functions.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.entities.Guild;

public class GuildPlayer extends AudioEventAdapter implements AudioSendHandler{
	
	private AudioPlayerManager pm;
	private Guild guild;
	private TrackQueue queue;
	private TrackLoader loader;
	private AudioPlayer player;
	private AudioFrame lastFrame;
	
	public GuildPlayer(AudioPlayerManager pm, Guild guild) {
		this.pm = pm;
		player = this.pm.createPlayer();
		player.addListener(this);
		queue = new TrackQueue();
		loader = new TrackLoader(queue,this.pm);
		this.guild= guild;
		guild.getAudioManager().setSendingHandler(this);
		
	}

	//music controls
	/**
	 * Start first song in queue
	 */
	public void play(){
		//TODO play
		if(queue.isEmpty()){
			//TODO nothing to play
		}
		
		player.playTrack(queue.remove());
		
		
	}
	
	
	
	//AudioSendHandler methods
	@Override
	public boolean canProvide() {
		lastFrame = player.provide();
		return lastFrame!=null;
	}

	@Override
	public byte[] provide20MsAudio() {
		 return lastFrame.data;
	}
	
	@Override
	public boolean isOpus(){
		return true;
	}
	
	//AudioEventHandler methods
	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
	    // TODO onTrackEnd play next in queue
	  }
}
