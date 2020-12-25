/*
 * 下載網址檔案
 * 
 * @author 黃郁授,吳彥儒
 * @date 2020/12/25
 */

package rhinoceros.util.url;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadFile {
	
	// 圖片的網址 
	private String url;
	// 下載時的檔名 
	private String fileName;
	// 下載至本機端的路徑 
	private String localPath;
	
	/*
	 * 下載檔案
	 * @param url 網址 例如 https://chart.apis.google.com/chart?cht=qr&chs=200x200&chl=A000001
	 * @param localPath 本地端下載檔案的目錄 例如 A000001.png
	 * @param fileName 下載後的檔名 例如 c:/temp/pic/
	 */
	public boolean download(String url, String localPath, String fileName) {

		if (url == null || url.length() <= 0) {
			return false;
		}
		if (localPath == null || localPath.length() <= 0) {
			return false;
		}
		if (fileName == null || fileName.length() <= 0) {
			return false;
		}
		this.url = url;
		this.localPath = localPath;
		this.fileName = fileName;
		// 建立URL物件
		URL imageURL = null;
		URLConnection urlCon = null;
		InputStream is = null;
		
		try {
			// 建立URL物件
			imageURL = new URL(this.url);
			// 建立URL連線
			urlCon = imageURL.openConnection();
			// 設定request時間不可超過5秒，超過則視為逾時
			urlCon.setConnectTimeout(5*1000);
			// 建立輸入資料流
			is = urlCon.getInputStream();
		} catch (Exception e) {
			return false;
		}
		// 設定緩衝記憶體空間(設定5K)
		byte[] bs = new byte[5120];
		// 讀取到資料流的長度
		int len;
		// 確認產出目錄是否存在
		File folder = new File(this.localPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		// 檢查Local端是否有同名檔，若有則先刪除
		File file = new File(this.localPath + this.fileName);
		if (file.exists()) {
			file.delete();
		}
		try {
			// 建立輸出資料流
			OutputStream os = new FileOutputStream(folder.getPath() + "/" + this.fileName);
			// 開始下載
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			// 關閉串流
			os.close();
			is.close();
		} catch (Exception e) {
			return false;
		} 
		return true;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public static void main(String[] args) {
		// 測試範例
		DownloadFile df = new DownloadFile();
		df.download("https://chart.apis.google.com/chart?cht=qr&chs=200x200&chl=A000001", "c:/temp/pic/", "A000001.png");		
	}

}
