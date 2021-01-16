/* 
 * 用來產生QRCode
 * 須使用下列第三方jar檔
 * core-3.3.3.jar
 * javase-3.3.3.jar
 * 
 * @author: LinWenLi
 * @date: 2018-08-23 12:45:34
 */

package rhinoceros.util.qrcode;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeUtils {

	/**
	 * 二維碼BufferedImage對象生成方法
	 * 
	 * @author LinWenLi
	 * @date 2018-08-23 12:51:00
	 * @param contents二維碼內容
	 * @param width二維碼圖片寬度
	 * @param height二維碼圖片高度
	 * @param margin二維碼邊框(0,2,4,8)
	 * @throws Exception
	 * @return: BufferedImage
	 */
	public static BufferedImage createQRCode(String contents, int width, int height, int margin) throws Exception {
		if (contents == null || contents.equals("")) {
			throw new Exception("contents不能為空。");
		}
		// 二維碼基本參數設置
		Map<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, CharacterSetECI.UTF8);// 設置編碼字符集utf-8
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);// 設置糾錯等級L/M/Q/H,糾錯等級越高越不易識別，當前設置等級為最高等級H
		hints.put(EncodeHintType.MARGIN, margin);// 可設置範圍為0-10，但僅四個變化0 1(2) 3(4 5 6) 7(8 9 10)
		// 生成圖片類型為QRCode
		BarcodeFormat format = BarcodeFormat.QR_CODE;
		// 創建位矩陣對象
		BitMatrix matrix = null;
		try {
			// 生成二維碼對應的位矩陣對象
			matrix = new MultiFormatWriter().encode(contents, format, width, height, hints);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		// 設置位矩陣轉圖片的參數
		MatrixToImageConfig config = new MatrixToImageConfig(Color.black.getRGB(), Color.white.getRGB());
		// 位矩陣對象轉BufferedImage對象
		BufferedImage qrcode = MatrixToImageWriter.toBufferedImage(matrix, config);
		return qrcode;
	}

	/**
	 * 二維碼添加LOGO
	 * 
	 * @author LinWenLi
	 * @date 2018-08-23 13:17:07
	 * @param qrcode
	 * @param width二維碼圖片寬度
	 * @param height二維碼圖片高度
	 * @param logoPath圖標LOGO路徑
	 * @param logoSizeMultiple二維碼與LOGO的大小比例
	 * @throws Exception
	 * @return: BufferedImage
	 */
	public static BufferedImage createQRCodeWithLogo(BufferedImage qrcode, int width, int height, String logoPath,
			int logoSizeMultiple) throws Exception {
		File logoFile = new File(logoPath);
		if (!logoFile.exists() && !logoFile.isFile()) {
			throw new Exception("指定的LOGO圖片路徑不存在！");
		}
		try {
			// 讀取LOGO
			BufferedImage logo = ImageIO.read(logoFile);
			// 設置LOGO寬高
			int logoHeight = qrcode.getHeight() / logoSizeMultiple;
			int logowidth = qrcode.getWidth() / logoSizeMultiple;
			// 設置放置LOGO的二維碼圖片起始位置
			int x = (qrcode.getWidth() - logowidth) / 2;
			int y = (qrcode.getHeight() - logoHeight) / 2;
			// 新建空畫板
			BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			// 新建畫筆
			Graphics2D g = (Graphics2D) combined.getGraphics();
			// 將二維碼繪制到畫板
			g.drawImage(qrcode, 0, 0, null);
			// 設置不透明度，完全不透明1f,可設置範圍0.0f-1.0f
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			// 繪制LOGO
			g.drawImage(logo, x, y, logowidth, logoHeight, null);
			return combined;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 導出到指定路徑
	 * 
	 * @author LinWenLi
	 * @date 2018-08-23 12:59:03
	 * @param bufferedImage
	 * @param filePath圖片保存路徑
	 * @param fileName圖片文件名
	 * @param formatName圖片格式
	 * @return: boolean
	 */
	public static boolean generateQRCodeToPath(BufferedImage bufferedImage, String filePath, String fileName,
			String formatName) {
		// 判斷路徑是否存在，不存在則創建
		File path = new File(filePath);
		if (!path.exists()) {
			path.mkdirs();
		}
		// 路徑後補充斜杠
		if (filePath.lastIndexOf("/") != filePath.length() - 1) {
			filePath = filePath + "/";
		}
		// 組合為圖片生成的全路徑
		String fileFullPath = filePath + fileName + "." + formatName;

		boolean result = false;
		try {
			// 輸出圖片文件到指定位置
			result = ImageIO.write(bufferedImage, formatName, new File(fileFullPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) {
		//測試範例
		//二維碼內容
		String contents = "A000002";
		int width = 200;// 二維碼寬度
		int height = 200;// 二維碼高度
		int margin = 0;// 二維碼邊距

		String logoPath = "C:/temp/pic/logo.png";// LOGO圖片路徑
		int logoSizeMultiple = 3;// 二維碼與LOGO的大小比例

		String filePath = "C:/temp/pic";// 指定生成圖片文件的保存路徑
		String fileName = "A000002";// 生成的圖片文件名
		String formatName = "png";// 生成的圖片格式，可自定義

		try {
			// 生成二維碼
			BufferedImage qrcode = QRCodeUtils.createQRCode(contents, width, height, margin);
			// 添加LOGO
			qrcode = QRCodeUtils.createQRCodeWithLogo(qrcode, width, height, logoPath, logoSizeMultiple);
			// 導出到指定路徑
			boolean result = QRCodeUtils.generateQRCodeToPath(qrcode, filePath, fileName, formatName);
			System.out.println("執行結果" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}