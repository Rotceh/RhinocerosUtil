package rhinoceros.util.file;
/* 
 * UTF-8檔案解析程式
 * 
 * @author 黃郁授,吳彥儒
 * @date 2020/09/15
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class UTF8FileReader {
	/*
	 * 讀取UTF-8文字檔，並可將指定字串取代成特定字串後，以ArrayList<String>回傳文字檔的內文
	 * @param filePath 檔案的絕對路徑
	 * @param oldString 要被取代的字串，null的話，就不會做取代字串的動作
	 * @param newString 取代後的新字串，null的話，就不會做取代字串的動作
	 * @param trim true 字串前後的空白要去除 false 字串前後的字串不去除
	 * 註: oldString的部份，若填入 \\s+ 的話，是指對所有空白類的字元都取代
	 */
	public ArrayList<String> replace(String filePath, String oldString, String newString, boolean trim) throws Exception {
		InputStreamReader isr = null;
		ArrayList<String> data = new ArrayList<String>(); 
		try { 
			isr = new InputStreamReader(new FileInputStream(filePath), "utf-8"); 
			BufferedReader read = new BufferedReader(isr); 
			String s = null; 
			while ((s = read.readLine()) != null) {  
				if (s.trim().length()>1) { 
					if (oldString != null && newString != null) {
						s = s.replaceAll(oldString, newString);
					}	
					if (trim) {
						s = s.trim();
					}
					data.add(s); 
				} 
			} 
		} catch (Exception e) { 
			throw e;
		}
		return data;
	}
	/*
	 * 測試讀取UTF-8檔
	 */
	public static void main(String[] args) {
		UTF8FileReader u8r = new UTF8FileReader();
		ArrayList<String> data = null; 
		try {
			data = u8r.replace("C:/ColorSportsClubMDM/MyExcel.tsv", "\\s+", "|", true);
			for (String s : data) {
				System.out.println(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
