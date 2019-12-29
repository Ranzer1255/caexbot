package net.ranzer.caexbot.functions.music.events;

public class VolumeChangeEvent extends MusicEvent {

	private int vol;
	
	public VolumeChangeEvent(int vol) {
		this.vol = vol;
	}
	
	public int getVol() {
		return vol;
	}
}
