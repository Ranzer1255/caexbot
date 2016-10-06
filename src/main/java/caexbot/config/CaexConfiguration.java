package caexbot.config;

public class CaexConfiguration {
	
	private String botToken;
	private static CaexConfiguration instance;

	
	private CaexConfiguration(){
		botToken = "MjI4MjE5OTEzNTkwOTMxNDY4.CtaTmA.JEbNdXoSXCMyWAte5ZgqP_IejI0";
		instance = this;
	}
	
	public static CaexConfiguration getInstance() {
		
		if (instance==null)
			instance = new CaexConfiguration();
		
		return instance;
	}

	public String getLogLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isDebug() {
		// TODO Auto-generated method stub
		return true;
	}

	public String getToken() {
		return botToken;
	}

}
