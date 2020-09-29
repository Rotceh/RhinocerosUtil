/*
 * 資料庫連線建立程式
 * 目前只支援SQLite資料庫
 * 使用的第三方JAR檔 
 * sqlite-jdbc-3.27.2.1.jar 
 * 
 * @author 黃郁授,吳彥儒
 * @date 2020/09/25
 */
package rhinoceros.util.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnectionFactory {
	
	public Connection getSQLiteCon(String path, String dbName) throws Exception {
		/*
		 * 範例1(絕對路徑)
		 * path = "C:/xxxx/"
		 * dbname = "Club.org"
		 * 
		 * 範例2(相對路徑)
		 * path = ""
		 * dbname = "Club.org"
		 */
		return DriverManager.getConnection("jdbc:sqlite:" + path + dbName);
	}
}
