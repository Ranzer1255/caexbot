package net.ranzer.caexbot.functions.music.events;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;

public class PlaylistLoadEvent extends MusicEvent {

	private AudioPlaylist list;
	
	public PlaylistLoadEvent(AudioPlaylist list) {
		this.list=list;
	}
	
	public AudioPlaylist getList() {
		return list;
	}
}
