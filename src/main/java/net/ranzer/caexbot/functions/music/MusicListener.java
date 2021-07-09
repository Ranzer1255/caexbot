package net.ranzer.caexbot.functions.music;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.ranzer.caexbot.commands.BotCommand;
import net.ranzer.caexbot.data.GuildManager;
import net.ranzer.caexbot.functions.music.commands.MusicCommand;
import net.ranzer.caexbot.functions.music.commands.QueueCommand;
import net.ranzer.caexbot.functions.music.events.*;
import net.ranzer.caexbot.util.Logging;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MusicListener implements MusicEventListener{
	private final Guild guild;
	private TextChannel lastMusicChannel;
	private Message nowPlayingMessage;
	private static final String MUSIC_BUTTON_PREFIX = "ml_";

	public static final String ID_PAUSE 	   = MUSIC_BUTTON_PREFIX + "pause";
	public static final String ID_SHUFFLE 	   = MUSIC_BUTTON_PREFIX + "shuffle";
	public static final String ID_QUEUE 	   = MUSIC_BUTTON_PREFIX + "queue";
	public static final String ID_SKIP 		   = MUSIC_BUTTON_PREFIX + "skip";
	public static final String ID_STOP 		   = MUSIC_BUTTON_PREFIX + "stop";
	public static final String ID_CONFIRM      = MUSIC_BUTTON_PREFIX + "confirm";
	public static final String ID_CONFIRM_KEEP = MUSIC_BUTTON_PREFIX + "confirm_keep";

	public static final List<ActionRow> PLAYING_BUTTONS = Arrays.asList(
			ActionRow.of(
					Button.primary(ID_PAUSE,"Pause"),
					Button.secondary(ID_SHUFFLE,"Shuffle"),
					Button.secondary(ID_QUEUE,"Queue")
					),
			ActionRow.of(
					Button.primary(ID_SKIP,"Skip"),
					Button.danger(ID_STOP, "Stop")
			)
	);
	public static final List<ActionRow> PAUSED_BUTTONS = Arrays.asList(
			ActionRow.of(
					Button.primary(ID_PAUSE,"Play"),
					Button.secondary(ID_SHUFFLE,"Shuffle"),
					Button.secondary(ID_QUEUE,"Queue")
					),
			ActionRow.of(
					Button.primary(ID_SKIP,"Skip"),
					Button.danger(ID_STOP, "Stop")
			)
	);

	public MusicListener(Guild g) {
		guild = g;
		g.getJDA().addEventListener(new ListenerAdapter() {
				@Override
				public void onButtonClick(@NotNull ButtonClickEvent event) {
					//not a music button, don't handle it here
					if (!event.getComponentId().startsWith(MUSIC_BUTTON_PREFIX)) return;

					if (notInSameVoiceChannel(event.getUser())) {
						event.reply("you must be in voice with me to use these buttons").setEphemeral(true).queue();
						return;
					}
					switch (event.getComponentId()) {
						case ID_SKIP:
							Logging.debug("skip clicked");
							GuildPlayerManager.getPlayer(event.getGuild()).playNext(true);
							break;
						case ID_STOP:
							Logging.debug("stop clicked");
							event.reply("are you sure? dismiss this if not.").setEphemeral(true)
									.addActionRow(
											Button.danger(ID_CONFIRM, "YES!"),
											Button.primary(ID_CONFIRM_KEEP,"yes, but don't clear the queue!")
									).queue();
							return;
						case ID_CONFIRM:
							Logging.debug("confirm clicked");
							GuildPlayerManager.getPlayer(event.getGuild()).stop(true);
							break;
						case ID_CONFIRM_KEEP:
							Logging.debug("confirm_keep clicked");
							GuildPlayerManager.getPlayer(event.getGuild()).stop(false);
							break;
						case ID_PAUSE:
							Logging.debug("pause clicked");
							GuildPlayerManager.getPlayer(event.getGuild()).pause();
							break;
						case ID_SHUFFLE:
							Logging.debug("shuffle clicked");
							GuildPlayerManager.getPlayer(event.getGuild()).shuffle();
							break;
						case ID_QUEUE:
							Logging.debug("Queue clicked");
							event.replyEmbeds(QueueCommand.getQueueEmbed(GuildPlayerManager.getPlayer(event.getGuild()))).queue();
							return;
						default:
							Logging.error("[MusicListener button handler] Unhandled button pushed: " + event.getComponentId());
							event.reply(
									"This Button isn't handled by the music button handler yet.... Yell at ranzer ("+event.getComponentId()+")"
							).queue();
							return;
					}
					event.deferEdit().queue();
					if (!Objects.equals(event.getMessage(), nowPlayingMessage)){
						clearButtons(Objects.requireNonNull(event.getMessage()));
					}
				}
			});
	}

	protected boolean notInSameVoiceChannel(User u) {

		VoiceChannel requesterChannel = getVoiceChannel(guild.retrieveMember(u).complete());
		VoiceChannel botChannel = getVoiceChannel(guild.getSelfMember());

		return !Objects.equals(requesterChannel, botChannel);
	}

	private VoiceChannel getVoiceChannel(Member m){
		return Objects.requireNonNull(m.getVoiceState()).getChannel();
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

		else if (event instanceof MusicStopEvent){
			clearButtons(nowPlayingMessage);
			nowPlayingMessage=null;
		}

		else if (event instanceof MusicStartEvent){
			getMusicChannel().sendMessage(
					String.format(MusicCommand.NOW_PLAYING, ((MusicStartEvent) event).getSong().getInfo().uri)
			).queue(message -> setNowPlayingMessage(message,PLAYING_BUTTONS));
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
						BotCommand.getPrefix(getMusicChannel().getGuild()),
						BotCommand.getPrefix(getMusicChannel().getGuild()))).queue();
				setNowPlayingMessage(nowPlayingMessage,PAUSED_BUTTONS);
			} else {
				setNowPlayingMessage(nowPlayingMessage,PLAYING_BUTTONS);
			}

		}

		else if (event instanceof VolumeChangeEvent) {
			MessageBuilder mb = new MessageBuilder();

			mb.append(String.format("Volume set to %d\n",((VolumeChangeEvent) event).getVol()))
			.append("```\n")
			.append("*-------------------------*--boost---*\n")
			.append(volumeBar(((VolumeChangeEvent) event).getVol())).append("\n")
			.append("*-------------------------*----------*\n")
			.append("```");


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

	public void setNowPlayingMessage(Message message, List<ActionRow> buttons) {
		if (nowPlayingMessage!=null&&!nowPlayingMessage.equals(message)){
			clearButtons(nowPlayingMessage);
		}
		nowPlayingMessage = message;
		if (!nowPlayingMessage.getContentRaw().isEmpty())
			nowPlayingMessage.editMessage(nowPlayingMessage.getContentRaw()).setActionRows(buttons).queue();
		else
			nowPlayingMessage.editMessageEmbeds(nowPlayingMessage.getEmbeds()).setActionRows(buttons).queue();
	}

	private void clearButtons(Message message) {
		if (!message.getContentRaw().isEmpty())
			message.editMessage(message.getContentRaw()).setActionRows().queue();
		else
			message.editMessageEmbeds(message.getEmbeds()).setActionRows().queue();
	}

	private CharSequence volumeBar(int vol) {
		StringBuilder rtn = new StringBuilder();
		rtn.append("*|");

		//not boosted
		if (vol<=100) {

			//number of bars to add (if the math comes out to neg set to 0
			int volBars = Math.max(((vol / 4) - 2), 0);

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