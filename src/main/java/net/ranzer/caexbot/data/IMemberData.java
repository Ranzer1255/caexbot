package net.ranzer.caexbot.data;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.ranzer.caexbot.functions.levels.UserLevel;

import java.util.Map;

public interface IMemberData {

    Member getMember();

    int getXP();

    void addXP(int amount, MessageChannel channel);
    void removeXP(int amount, MessageChannel channel);
    long lastXP();

    boolean isBannedFromRaffle();
    void setBannedFromRaffle(boolean banned);

    /**
     *
     * @return map of timed roles on this users key is the role, value is the time as a long after which this role is to
     * be removed
     */
    Map<Role,Long> getTimedRoles();

    /**
     * adds a role to the timed roles for this user
     * @param role the role to be added to this member
     * @param timeToRemoveRole time after which this role should be removed from this Member
     */
    void addTimedRole(Role role, long timeToRemoveRole);

    /**
     * removes a timed role from the DB
     * @param role role to be removed from this member
     */
	void removedTimedRole(Role role);

    /**
     * calculated based on xp
     * @return level based on formula calculation
     */
	int getLevel();

    UserLevel getUserLevel();
}
