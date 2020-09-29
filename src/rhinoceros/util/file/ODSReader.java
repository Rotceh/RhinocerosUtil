/*
 * 由Google Sheet另存的ODS文件解析程式
 * 目前發現有個缺點，Google Sheet另存ODS後，會變成有很多空白列或欄，造成解析時解析出一堆不必要的東西。
 * 使用的第三方JAR檔 
 * jOpenDocument-1.3.jar 
 * 
 * @author 黃郁授,吳彥儒
 * @date 2020/09/25
 */

package rhinoceros.util.file;

import java.io.File;
import java.io.IOException;

import org.jopendocument.dom.spreadsheet.MutableCell;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

public class ODSReader {
	public void readODS(File file) {
		Sheet sheet;
		try {
			// Getting the 0th sheet for manipulation| pass sheet name as string
			sheet = SpreadSheet.createFromFile(file).getSheet(0);

			// Get row count and column count
			int nColCount = sheet.getColumnCount();
			int nRowCount = sheet.getRowCount();

			System.out.println("Rows :" + nRowCount);
			System.out.println("Cols :" + nColCount);
			// Iterating through each row of the selected sheet
			MutableCell cell = null;
			for (int nRowIndex = 0; nRowIndex < nRowCount; nRowIndex++) {
				// Iterating through each column
				int nColIndex = 0;
				for (; nColIndex < nColCount; nColIndex++) {
					cell = sheet.getCellAt(nColIndex, nRowIndex);
					System.out.print(cell.getValue() + " ");
				}
				System.out.println();
			}
			System.out.println("over");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Creating File object of .ods file
		File file = new File("D:/Chrome download/MyExcel.ods");
		ODSReader objODSReader = new ODSReader();
		objODSReader.readODS(file);
	}
}
