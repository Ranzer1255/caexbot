package net.ranzer.caexbot.database;

import net.ranzer.caexbot.config.CaexConfiguration;
import net.ranzer.caexbot.util.Logging;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateManager {

	private static SessionFactory instance;

	public static SessionFactory getSessionFactory(){
		if (instance==null) {

			CaexConfiguration config = CaexConfiguration.getInstance();
			SessionFactory sessionFactory;

			String DBMS = config.getDatabaseManagementSystem();
			String host = config.getDatabaseHostname();
			Integer port = config.getDatabasePort();
			String DB = config.getDatabaseName();

			// A SessionFactory is set up once for an application!
			final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
					.configure()
					.applySetting("hibernate.connection.url", String.format("jdbc:%s://%s:%d/%s", DBMS, host, port, DB))
					.applySetting("hibernate.connection.username", config.getDatabaseUsername())
					.applySetting("hibernate.connection.password", config.getDatabasePassword())
					.build();
			try {
				instance = new MetadataSources(registry).buildMetadata().buildSessionFactory();
			} catch (Exception e) {
				// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
				// so destroy it manually.
				Logging.log(e);

				StandardServiceRegistryBuilder.destroy(registry);
			}
		}

		return instance;
	}
}
