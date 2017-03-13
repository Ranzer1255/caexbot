package caexbot.functions.music.events;

import net.dv8tion.jda.core.entities.VoiceChannel;

public class MusicJoinEvent extends MusicEvent {

	final private VoiceChannel channelJoined;
	
	public MusicJoinEvent(VoiceChannel channel) {
		channelJoined=channel;
	}

	public VoiceChannel getChannelJoined() {
		return channelJoined;
	}
}
