package net.ranzer.caexbot.data.hibernate.database.interfaces;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.ranzer.caexbot.CaexBot;
import net.ranzer.caexbot.data.hibernate.IMemberData;
import net.ranzer.caexbot.data.hibernate.database.HibernateManager;
import net.ranzer.caexbot.data.hibernate.database.model.MemberDataModel;
import net.ranzer.caexbot.data.hibernate.database.model.MemberPK;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MemberData extends AbstractData implements IMemberData {

	MemberDataModel mdm;

	public MemberData(Member member,GuildData guildData) {
		try(Session session = HibernateManager.getSessionFactory().openSession()){
			mdm = session.createQuery("select e " +
					"from MemberDataModel e " +
					"where e.gdm.guildID = :guildId and " +
					"e.userID = :userId", MemberDataModel.class)
					.setParameter("guildId", member.getGuild().getId())
					.setParameter("userId", member.getUser().getId()).getSingleResult();

		} catch (NoResultException e){

			mdm = new MemberDataModel(member, guildData.getModel());
			save(mdm);
		}
	}


	@Override
	public Member getMember() {
		JDA jda = CaexBot.getJDA();
		Guild g = jda.getGuildById(mdm.getGuildId());
		User u = jda.getUserById(mdm.getUserId());
		return Objects.requireNonNull(g).retrieveMember(Objects.requireNonNull(u)).complete();
	}

	@Override
	public int getXP() {
		return mdm.getXp();
	}

	@Override
	public void addXP(int amount) {
		mdm.addXp(amount);
		save(mdm);
	}

	@Override
	public void removeXP(int amount) {
		mdm.removeXP(amount);
		save(mdm);
	}

	@Override
	public long lastXP() {
		return mdm.getLastXP();
	}

	@Override
	public boolean isBannedFromRaffle() {
		return mdm.getRaffleBan();
	}

	@Override
	public void setBannedFromRaffle(boolean banned) {
		mdm.setRaffleBan(banned);
		save(mdm);
	}

	@Override
	public Map<Role, Long> getTimedRoles() {
		try (Session s = HibernateManager.getSessionFactory().openSession()) {
			Map<Role, Long> rtn = new HashMap<>();

			s.load(mdm,new MemberPK(mdm.getUserId(),mdm.getGdm()));
			for (Map.Entry<String, Long> role : mdm.getTimedRoles().entrySet()) {
				rtn.put(
						CaexBot.getJDA().getRoleById(role.getKey()),
						role.getValue());
			}

			return rtn;
		}
	}

	@Override
	public void addTimedRole(Role role, long timeToRemoveRole) {
		Session s = HibernateManager.getSessionFactory().openSession();
		s.load(mdm, new MemberPK(mdm.getUserId(), mdm.getGdm()));

		mdm.getTimedRoles().put(role.getId(),timeToRemoveRole);
		s.beginTransaction();
		s.update(mdm);
		s.flush();
		s.close();
	}

	@Override
	public void removedTimedRole(Role role) {
		Session s = HibernateManager.getSessionFactory().openSession();
		s.load(mdm, new MemberPK(mdm.getUserId(), mdm.getGdm()));
		mdm.getTimedRoles().remove(role.getId());
		s.close();

		save(mdm);
	}



}
