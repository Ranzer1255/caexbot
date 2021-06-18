package net.ranzer.caexbot.data.hibernate.database.interfaces;

import net.ranzer.caexbot.data.hibernate.database.HibernateManager;
import org.hibernate.Session;

public abstract class AbstractData {

	protected void save(Object savable) {
		Session s = HibernateManager.getSessionFactory().openSession();
		s.beginTransaction();
		s.saveOrUpdate(savable);
		s.getTransaction().commit();
		s.close();
	}


}
