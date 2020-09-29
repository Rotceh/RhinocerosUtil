/*
 * 檢查日期格式
 * 目前只支援檢核 yyyy-mm-dd 這個格式
 * @author 黃郁授,吳彥儒
 * @date 2020/09/25
 */

package rhinoceros.util.date;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChkDate {

	public boolean isDate(String date) {
		/**
		 * 判斷日期格式和範圍
		 */
		String rexp = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
		Pattern pat = Pattern.compile(rexp);
		Matcher mat = pat.matcher(date);
		boolean dateType = mat.matches();
		return dateType;
	}

	// 測試用
	public static void main(String[] args) {
		/**
		 * 日期格式正確
		 */
		String date1 = "2014-01-03";
		/**
		 * 日期範圍不正確---平年二月沒有29號
		 */
		String date2 = "2014-02-29";
		/**
		 * 日期月份範圍不正確---月份沒有13月
		 */
		String date3 = "2014-13-03";
		/**
		 * 日期範圍不正確---六月沒有31號
		 */
		String date4 = "2014-06-31";
		/**
		 * 日期範圍不正確 ----1月超過31天
		 */
		String date5 = "2014-01-32";
		/**
		 * 這個測試年份
		 */
		String date6 = "0014-01-03";

		ChkDate chkDate = new ChkDate();

		/**
		 * 輸出結果
		 */
		System.out.println(chkDate.isDate(date1));
		System.out.println(chkDate.isDate(date2));
		System.out.println(chkDate.isDate(date3));
		System.out.println(chkDate.isDate(date4));
		System.out.println(chkDate.isDate(date5));
		System.out.println(chkDate.isDate(date6));
	}
}
