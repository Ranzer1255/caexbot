package net.ranzer.caexbot.database.model;

import net.dv8tion.jda.api.entities.Member;
import net.ranzer.caexbot.data.IGuildData;
import net.ranzer.caexbot.data.IRaffleData;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
@Entity
@Table(name = "guild")
public class GuildDataModel {

	@Id
	@Column(name = "guild_id")
	private String guildID;

	@Basic
	private String prefix;

	@Column(name = "jl_announcement")
	private boolean JLAnnouncement = true;
	@Column(name = "xp_timeout")
	private long XPTimeout = IGuildData.DEFAULT_MESSAGE_TIMEOUT;
	@Column(name = "xp_low")
	private int XPLowBound = IGuildData.DEFAULT_XP_LOWBOUND;
	@Column(name = "xp_high")
	private int XPHighBound = IGuildData.DEFAULT_XP_HIGHBOUND;
	@Column(name = "XPAnnouncement")
	private boolean XPAnnouncement = true;
	@Column(name = "announcement_channel")
	private String announcementChannelID;
	@Column(name = "music_channel")
	private String musicChannelID;


	@OneToMany(mappedBy = "gdm", cascade = CascadeType.ALL)
	private final List<MemberDataModel> members = new ArrayList<>();

	@Column(name = "raffle_threshold")
	private int raffleThreshold = IRaffleData.DEFAULT_RAFFLE_THRESHOLD;
	@ElementCollection
	@CollectionTable(name = "raffle_roles")
	@JoinColumn(name = "guild_id")
	@Column(name = "role_id")
	private Set<String> raffleRoleIDs;

	@ElementCollection
	@CollectionTable(name = "moderation_roles")
	@JoinColumn(name = "guild_id")
	@Column(name = "role_id")
	private Set<String> modRoleIDs;


	private GuildDataModel() {
	}

	public GuildDataModel(String guildID) {
		this.guildID = guildID;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getId() {
		return guildID;
	}

	public String getAnnouncementChannelID() {
		return announcementChannelID;
	}

	public void setAnnouncementChannelID(String id) {
		announcementChannelID = id;
	}

	public void setMusicChannelID(String id) {
		musicChannelID = id;
	}

	public String getMusicChannelID() {
		return musicChannelID;
	}

	public void setJLAnnouncement(boolean allowed) {
		JLAnnouncement = allowed;
	}

	public boolean getJLAnnouncement() {
		return JLAnnouncement;
	}

	public long getXPTimeout() {
		return XPTimeout;
	}

	public void setXPTimeout(long timeout) {
		this.XPTimeout = timeout;
	}

	public void setXPBounds(int low, int high) {
		this.XPLowBound = low;
		this.XPHighBound = high;
	}

	public int getXPLowBound() {
		return XPLowBound;
	}

	public int getXPHighBound() {
		return XPHighBound;
	}

	public void setXPAnnouncement(boolean allowed) {
		XPAnnouncement = allowed;
	}

	public boolean getXPAnnouncement() {
		return XPAnnouncement;
	}

	public void addMember(Member m) {
		members.add(new MemberDataModel(m, this));
	}

	public void removeMember(MemberDataModel m) {
		members.remove(m);
	}

	public int getRaffleThreshold() {
		return raffleThreshold;
	}

	public void setRaffleThreshold(int threshold) {
		raffleThreshold = threshold;
	}

	public Set<String> getRaffleRoleIDs() {
		return raffleRoleIDs;
	}

	public boolean addRaffleRole(String id) {
		return raffleRoleIDs.add(id);
	}

	public boolean removeRaffleRole(String id) {
		return raffleRoleIDs.remove(id);
	}

	public Set<String> getModRoleIDs() {
		return modRoleIDs;
	}

	public boolean addModRole(String id) {
		return modRoleIDs.add(id);
	}

	public boolean removeModRole(String id) {
		return modRoleIDs.remove(id);
	}

	public List<MemberDataModel> getMembers() {
		return members;
	}
}
