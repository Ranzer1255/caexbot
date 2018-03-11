package caexbot.functions.music.events;

import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;

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
