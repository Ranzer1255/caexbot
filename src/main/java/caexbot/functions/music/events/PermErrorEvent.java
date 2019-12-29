package caexbot.functions.music.events;

import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

public class PermErrorEvent extends MusicEvent {

	private VoiceChannel vc;
	private InsufficientPermissionException e;
	
	
	public PermErrorEvent(VoiceChannel channel, InsufficientPermissionException e) {
		vc = channel;
		this.e = e;
	}
	
	public VoiceChannel getVoiceChannel(){
		return vc;
	}
	
	public InsufficientPermissionException getException(){
		return e;
	}

}
