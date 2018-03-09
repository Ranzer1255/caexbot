package caexbot.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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
	private String testDatabaseName = "cekipdb";
	private Boolean debug = false;
	private String logPath = "/caexbot/logs/caex.log";//Default path for log Location
	private File logLocation;
	private String prefix = "!";
	private String botToken = "token";
	private String testToken = "token";
	private String GoogleToken = "token";
	private String statusMessage = "with Gilmore!";
	private String owner = "userID";
	private String version = "Test_Build";
	
	private long lastModified = 0;
	
	private CaexConfiguration(){

		this.logLocation =new File(System.getProperty("user.home"),logPath);
	}
	
	//getters
	public static CaexConfiguration getInstance() {
		
		if (instance==null){
			instance = new CaexConfiguration();
			instance.load();
			instance.startConfigMonitorThread();
		}
		return instance;
	}

	public String getStatus() {
		return statusMessage;
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

	public String getTestDatabaseName() {
		return testDatabaseName;
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
	
	public String getTestToken() {
		return testToken;
	}

	public String getGToken() {
		return GoogleToken;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getOwner() {
		return this.owner;
	}

	public String getVersion() {
		return version;
	}

	//setters
	@CaexConfigItem(key="statusMessage", type=String.class,_default="with Gilmore!")
	public void setStatus(String status) {
		this.statusMessage = status;
	}
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
	@CaexConfigItem(key="dbName", type=String.class,_default="caexdb2")
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	@CaexConfigItem(key="testDBName", type=String.class, _default="cekipdb2")
	public void setTestDatabaseName(String testDatabaseName) {
		this.testDatabaseName = testDatabaseName;
	}
	@CaexConfigItem(key="logLocation", type=String.class, _default="/caexbot/logs/caex.log")
	public void setLogLocation(String logLocation) {
		this.logLocation =new File(System.getProperty("user.home"),logLocation);
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
	public void setBotToken(String token){
		this.botToken = token;
	}
	@CaexConfigItem(key="testToken", type=String.class, _default="token")
	public void setTestToken(String token) {
		testToken=token;
	}
	@CaexConfigItem(key="googleToken", type=String.class, _default="token")
	public void setGoogleToken(String token){
		this.GoogleToken=token;
	}
	@CaexConfigItem(key="owner",type=String.class, _default = "143929240440537089")
	public void setOwner(String owner){
		this.owner = owner; 
	}

	public void setVersion(String version) {
		System.out.printf("[CaexConfig] setVersion: (%s)\n", version);
		this.version = version;
	}

	public void load() {

		loadVersionFromJAR(); 
		
		try {
			String home = System.getProperty("user.home");
			File configurationFile = new File(home, CONFIG_PATH);
			lastModified = configurationFile.lastModified();
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
			File configurationFile = new File(System.getProperty("user.home"), CONFIG_PATH);
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
				e.printStackTrace();
			}
		} catch (Exception ex) {
			System.out.println(String.format("Exception when loading in configuration, using default configuration values.", CONFIG_PATH));
			ex.printStackTrace();
		}
		
		

	}
	
	private void loadVersionFromJAR(){
		Properties pom = new Properties();
				try {
					InputStream in = getClass().getResourceAsStream("/META-INF/maven/com.ranzer.caexbot/caexbot/pom.properties");
					if(in==null){
						setVersion("TESTING_VERSION");
						return;
					}
					pom.load(in);
					setVersion(pom.getProperty("version"));
					
				} catch (IOException e) {
					setVersion("TESTING_VERSION");
				}
	}

	private void startConfigMonitorThread() {
		
	new Thread(){
		@Override
		public void run() {
			
			setName("Config Monitor");
			
			while(true){
				String home = System.getProperty("user.home");
				File configurationFile = new File(home, CONFIG_PATH);
				if(configurationFile.lastModified()!=lastModified){
					load();
				}
				try {sleep(60000);} catch (InterruptedException e) {}
			}
		}
	}.start();
		
	}
}
