package caexbot.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

public class CaexConfiguration {
	
	private static final String CONFIG_PATH = "/caexbot/config/caex.conf";
	private static CaexConfiguration instance;
	
	private String databaseManagementSystem = "mysql";
	private String databaseHostname = "localhost";
	private Integer databasePort = 3306;
	private String databasePassword = "password";
	private String databaseUsername = "username";
	private String databaseName = "caexdb";
	private Boolean debug = false;
	private String logPath = "/caexbot/logs/caex.log";
	private File logLocation;
	private String prefix = "!";
	private String botToken = "token";
	
	private CaexConfiguration(){
		String home = System.getProperty("user.home");
		setLogLocation(new File(home,logPath));
	}
	
	//getters
	public static CaexConfiguration getInstance() {
		
		if (instance==null){
			instance = new CaexConfiguration();
			instance.load();
		}
		return instance;
	}

	public String getDatabaseManagementSystem() {
		return databaseManagementSystem;
	}

	public String getDatabaseHostname() {
		return databaseHostname;
	}

	public Integer getDatabasePort() {
		return databasePort;
	}

	public String getDatabaseUsername() {
		return databaseUsername;
	}

	public String getDatabasePassword() {
		return databasePassword;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public File getLogLocation() {
		return this.logLocation;
	}

	public boolean isDebug() {
		return debug;
	}

	public String getToken() {
		return botToken;
	}
	
	public String getPrefix() {
		return prefix;
	}

	//setters
	@CaexConfigItem(key="DBMS", type=String.class, _default="mysql")
	public void setDatabaseManagementSystem(String databaseManagementSystem) {
		this.databaseManagementSystem = databaseManagementSystem;
	}
	@CaexConfigItem(key="dbHostName", type=String.class, _default="localhost")
	public void setDatabaseHostname(String databaseHostname) {
		this.databaseHostname = databaseHostname;
	}
	@CaexConfigItem(key="dbPort", type=Integer.class, _default="3306")
	public void setDatabasePort(Integer databasePort) {
		this.databasePort = databasePort;
	}
	@CaexConfigItem(key="dbUsername", type=String.class, _default="username")
	public void setDatabaseUsername(String databaseUsername) {
		this.databaseUsername = databaseUsername;
	}
	@CaexConfigItem(key="dbPassword", type=String.class, _default="password")
	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
	}
	@CaexConfigItem(key="dbName", type=String.class,_default="caexdb")
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	@CaexConfigItem(key="logLocation", type=String.class, _default="/caexbot/logs/caex.log")
	public void setLogLocation(File logLocation) {
		this.logLocation = logLocation;
	}
	@CaexConfigItem(key="debug", type=Boolean.class, _default="false")
	public void setDebug(Boolean debug) {
		this.debug = debug;
	}
	@CaexConfigItem(key="prefix", type=String.class, _default="!")
	public void setPrefix(String prefix){
		this.prefix=prefix;
	}
	@CaexConfigItem(key="botToken", type=String.class, _default="token")
	public void setToken(String token){
		this.botToken = token;
	}

	public void load() {

		try {
			String home = System.getProperty("user.home");
			File configurationFile = new File(home, CONFIG_PATH);
			Properties properties = new Properties();
			properties.load(new FileReader(configurationFile));

			for (Method method : CaexConfiguration.class.getMethods()) {
				if (method.isAnnotationPresent(CaexConfigItem.class)) {
					CaexConfigItem item = method.getAnnotation(CaexConfigItem.class);

					if (properties.containsKey(item.key())) {
						if (item.type() == Integer.class) {
							Integer value = Integer.parseInt((String) properties.get(item.key()));
							method.invoke(this, value);
							System.out.println(String.format("[CaexConfig] %s(%s)", method.getName(), value));
							continue;
						} else if (item.type() == Boolean.class) {
							Boolean value = Boolean.parseBoolean((String) properties.get(item.key()));
							method.invoke(this, value);
							System.out.println(String.format("[CaexConfig] %s(%s)", method.getName(), value));
							continue;
						}

						String value = (String) properties.get(item.key());
						method.invoke(this, value);
						System.out.println(String.format("[CaexConfig] %s(%s)", method.getName(), value));
					}
				}
			}
		} catch (FileNotFoundException ex) {
			System.out.println(String.format("Configuration file '%s' not found, writing default configuration values.", CONFIG_PATH));
			String home = System.getProperty("user.home");
			File configurationFile = new File(home, CONFIG_PATH);
			configurationFile.getParentFile().mkdirs();
			try {
				configurationFile.createNewFile();
				BufferedWriter w = new BufferedWriter(new FileWriter(configurationFile));
				for (Method method: CaexConfiguration.class.getMethods()) {
					if(method.isAnnotationPresent(CaexConfigItem.class)){
						CaexConfigItem ann = method.getAnnotation(CaexConfigItem.class);
						w.write(ann	.key()+"="+ann._default());
						w.newLine();
					}
				}
				w.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception ex) {
			System.out.println(String.format("Exception when loading in configuration, using default configuration values.", CONFIG_PATH));
			ex.printStackTrace();
		}


	}

}
