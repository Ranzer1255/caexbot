package net.ranzer.caexbot.functions.music.events;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class MusicStartEvent extends MusicEvent {

	private AudioTrack song;
	
	public MusicStartEvent(AudioTrack track) {
		song=track;
		
	}

	public AudioTrack getSong(){
		return song;
	}
}
