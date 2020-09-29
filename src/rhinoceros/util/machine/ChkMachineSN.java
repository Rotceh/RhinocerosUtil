/* 
 * 用來取得
 * Windows的主機板的序號 或 MacOS序號
 * 
 * @author 黃郁授,吳彥儒
 * @date 2020/09/16
 */

package rhinoceros.util.machine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class ChkMachineSN {
	/*
	 * 用來判斷是什麼作業系統 目前僅判斷 Windows Mac Linux 三種，其它都視為Others
	 * @return 字串為 Windows Mac Linux Other 四種其中一種
	 */
	public String getOSKind() {
		String osName = System.getProperty("os.name");
		if (osName.contains("Windows")) {
			return "Windows";
		} else if (osName.contains("Mac")) {
			return "Mac";
		} else if (osName.contains("Linux")) {
			return "Linux";
		} else {
			return "Others";
		}
	}

	/*
	 * 取得序號
	 * 若為Windows的話，會取得主機板序號
	 * 若為Mac的話，會取得MacOS序號
	 * 若無法取得序號時，會回傳 ""
	 * @return 序號字串
	 */
	public String getMachineSN() throws Exception {
		String result = "";		
		if (getOSKind().equalsIgnoreCase("Windows")) {
			try {
				File file = File.createTempFile("realhowto", ".vbs");
				file.deleteOnExit();
				FileWriter fw = new java.io.FileWriter(file);

				String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
						+ "Set colItems = objWMIService.ExecQuery _ \n" + "   (\"Select * from Win32_BaseBoard\") \n"
						+ "For Each objItem in colItems \n" + "    Wscript.Echo objItem.SerialNumber \n"
						+ "    exit for  ' do the first cpu only! \n" + "Next \n";

				fw.write(vbs);
				fw.close();
				Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;
				while ((line = input.readLine()) != null) {
					result += line;
				}
				input.close();
			} catch (Exception e) {
				throw e;
			}
		} else if (getOSKind().equalsIgnoreCase("Mac")) {
			OutputStream os = null;
			InputStream is = null;

			Runtime runtime = Runtime.getRuntime();
			Process process = null;
			try {
				process = runtime.exec(new String[] { "/usr/sbin/system_profiler", "SPHardwareDataType" });
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			os = process.getOutputStream();
			is = process.getInputStream();

			try {
				os.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			String marker = "Serial Number";
			try {
				while ((line = br.readLine()) != null) {
					if (line.contains(marker)) {
						result = line.split(":")[1].trim();
						break;
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			if (result == null) {
				throw new RuntimeException("");
			}		
		}
		return result.trim();
	}

	/*
	 * 測試ChkMachineSN使用
	 */
	public static void main(String[] args) {
		ChkMachineSN cmsn = new ChkMachineSN();
		try {
			System.out.println("你的主機序號:" + cmsn.getMachineSN());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
