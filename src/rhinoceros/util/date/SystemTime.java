/*
 * 取得系統時間
 * @author 黃郁授,吳彥儒
 * @date 2020/10/15
 */


package rhinoceros.util.date;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemTime {
	private Date date;
	private SimpleDateFormat sdf;
	
	/*
	 * @param format 請填入時間格式 西元年yyyy 月MM 日dd 小時HH 分mm 秒ss 毫秒SSS，例如yyyy/MM/dd HH:mm:ss:SSS 或 yyyyMMddHHmmssSSS
	 * @return 系統時間字串
	 */
	public String getNowTime(String format) {
		this.date = new Date(System.currentTimeMillis()); //取得系統時間
		this.sdf = new SimpleDateFormat(format);
		return this.sdf.format(date); 
	}
		
	public static void main(String[] args) {
		//測試用
		SystemTime st = new SystemTime();
		System.out.println(st.getNowTime("yyyyMMddHHmmssSSS"));
	}

}
