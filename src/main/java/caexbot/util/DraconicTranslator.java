package caexbot.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 
 * @author Logic: Seth, Formatting: Ranzer
 *
 */
public class DraconicTranslator {

	private Document doc;
	private Elements table;
	private BidiMap<String, String> map;
	private FileWriter fileWriter;

	public DraconicTranslator(File file) {

		map = new DualHashBidiMap<String, String>();
		try {
			doc = Jsoup.parse(new URL("http://draconic.twilightrealm.com/vocabulary.php?lang=&sort=all"), 3000);
			grabTable();
			try {
				fileWriter = new FileWriter(file);
			} catch (FileNotFoundException e) {
				System.out.println("you shouldn't be going this way");
				file.getParentFile().mkdirs();
				try {
					file.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			fileWriter.write(doc.toString());
			fileWriter.close();
		} catch (Exception e) {
			try {
				doc = Jsoup.parse(file, "UTF-8");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			grabTable();
		}

	}
	
	public String translate(String[] body, boolean english)
	{
		String rtn = "", temp, regexString;
		
		BidiMap<String, String> tempMap = english ? map : map.inverseBidiMap();
		
		for (String string : body) 
		{
			boolean isUpper = Character.isUpperCase(string.charAt(0));
			
			regexString = string.replaceAll("[^a-zA-Z]", "");
			
			if (tempMap.containsKey(regexString.toLowerCase())) 
			{
				temp = map.get(regexString.toLowerCase());
				
				if (isUpper)
					temp = temp.replaceFirst(temp.substring(0, 1), temp.substring(0, 1).toUpperCase());
			} 
			else 
			{
				temp = regexString;
			}
			
			rtn += string.replace(regexString, temp) + " ";
		}
		
		return rtn.trim();
	}

	public String translate(String body, boolean english) {
		String[] split = body.split(" ");
		return translate(split, english);
	}

	private void grabTable() {
		table = doc.getElementsByTag("table").get(0).getElementsByTag("tr");
		for (int count = 1; count < table.size(); count++) {
			map.put(StringEscapeUtils.escapeHtml4(table.get(count).getElementsByTag("td").get(0).text())
					.replace("&nbsp;", ""),
					StringEscapeUtils.escapeHtml4(table.get(count).getElementsByTag("td").get(1).text())
							.replace("&nbsp;", ""));
		}
	}
}