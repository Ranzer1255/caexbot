package caexbot.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 
 * @author Ranzer, Webscraping by Seth
 */
public class DraconicTranslator {
	

	/**
	 * 
	 * @param phrase String to translate
	 * @param common True if phrase is in common, false if phrase is in draconic.
	 * @return
	 */
	static public String translate(String phrase, Boolean common){
		String[] wordsToTranslate = phrase.split("[^a-zA-Z']+");
		String[] translations = new String[wordsToTranslate.length];
		Map<String, String> dict = common ? Dictonary.getCommon() : Dictonary.getDraconic();
		for (int i = 0; i < wordsToTranslate.length; i++) {
			System.out.println(dict.containsKey(wordsToTranslate[i].toLowerCase()));
			if(dict.containsKey(wordsToTranslate[i].toLowerCase())&&
					!dict.get(wordsToTranslate).equals("UNKNOWN")){
				translations[i] = dict.get(wordsToTranslate[i].toLowerCase());
			} else {
				translations[i] = "["+wordsToTranslate[i]+"]";
			}
		}
		
		for (int i = 0; i < translations.length; i++) {
			phrase = phrase.replaceFirst(wordsToTranslate[i], translations[i]);
		}
		
		return phrase;
	}
	
	private static class Dictonary{
		
		private static final String BACKUP_FILE_LOCATION = "/caexbot/draconic/dict.txt";
		
		//map holding the Common to Draconic dict
		static private Map<String,String> comToDrc = new HashMap<>();
		
		//map holding the Draconic to Common dict
		static private Map<String,String> drcToCom = new HashMap<>();
		
		//Timeout the Dict after 1 hour (1*60*60*1000)
		private static final long TIMEOUT = 3_600_000l;

		//timestamp for last update
		static private long lastUpdate;
		
		
		public static Map<String,String> getCommon(){
			dictCheck(comToDrc);
			return comToDrc;
		}
		public static Map<String,String> getDraconic(){
			dictCheck(drcToCom);
			return drcToCom;
		}
		
		
		
		private static void dictCheck(Map<String,String> check) {
			if (check.isEmpty()||dictTimeout()){
				updateDict();
			}
		}
		
		private static boolean dictTimeout() {
			if(System.currentTimeMillis()-lastUpdate > TIMEOUT){
				return true;
			}
			return false;
		}
		
		private static void updateDict() {
			lastUpdate = System.currentTimeMillis();
			
			try {
				//webscrape from Twilight realm
				Document doc = Jsoup.parse(new URL("http://draconic.twilightrealm.com/vocabulary.php?lang=&sort=all"), 3000);
				
				writeBackupFile(doc);
				
				comToDrc=parseDoc(doc);
				drcToCom=flipMap(comToDrc);
			} catch (IOException e) {//webscrabing failed loading from backup file
				try {
					Document doc = Jsoup.parse(new File(System.getProperty("user.home")), "UTF-8");
					comToDrc=parseDoc(doc);
					drcToCom=flipMap(comToDrc);
				} catch (IOException e2) {
					e2.printStackTrace();
					Logging.messageBoss(LogLevel.ERROR, "Draconic translation: hey boss, check loading from backup file");
				}
			}			
		}
		
		private static Map<String,String> parseDoc(Document doc) {
			Map<String,String> rtn = new HashMap<>();
			Elements table = doc.getElementsByTag("table").get(0).getElementsByTag("tr");
			for (int count = 1; count < table.size(); count++) {
				rtn.put(StringEscapeUtils.escapeHtml4(table.get(count).getElementsByTag("td").get(0).text())
						.replace("&nbsp;", ""),
						StringEscapeUtils.escapeHtml4(table.get(count).getElementsByTag("td").get(1).text())
								.replace("&nbsp;", ""));
			}
			return rtn;
		}
		
		private static Map<String, String> flipMap(Map<String, String> map) {
			Map<String, String> rtn = new HashMap<>();
			
			for (String key : map.keySet()) {
				rtn.put(map.get(key), key);
			}
			return rtn;
		}
		
		private static void writeBackupFile(Document doc) {
			File backupFile = new File(System.getProperty("user.home"),BACKUP_FILE_LOCATION);
			try (FileWriter fileWriter = new FileWriter(backupFile);){
				
				fileWriter.write(doc.toString());
				
			} catch (FileNotFoundException e){
				backupFile.getParentFile().mkdirs();
				try {
					backupFile.createNewFile();
				} catch (IOException e1) {
					Logging.error("Draconic Translation: something went wrong writing the backup file");
					Logging.error(e1.getMessage());
					Logging.log(e1);
					Logging.messageBoss(LogLevel.ERROR, "hey check backup File writting");
				}
			} catch (IOException e) {
				Logging.error("Draconic Translation: something went wrong writing the backup file");
				Logging.error(e.getMessage());
				Logging.log(e);
				Logging.messageBoss(LogLevel.ERROR, "Draconic Translation: hey check backup File writting");
			}
			
		}
	}
}