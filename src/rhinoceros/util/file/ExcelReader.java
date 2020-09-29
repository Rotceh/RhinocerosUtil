/*
 * 解析Excel文件的程式，支援.xls .xlsx
 * 目前發現若Excel的內文是日期時，解析後會變一串數字，暫時還不知如何轉回年月日時分秒，尤其是時分秒的部份
 * 使用的第三方JAR檔 
 * poi-4.1.2.jar
 * poi-ooxml-4.1.2.jar" 
 * poi-ooxml-schemas-4.1.2.jar
 * commons-compress-1.20.jar
 * commons-collections4-4.4.jar
 * xmlbeans-3.1.0.jar   
 *
 * @author 黃郁授,吳彥儒
 * @date 2020/09/25
 */

package rhinoceros.util.file;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {
	
	
	/*
	 * 取得Excel檔
	 * @param path 傳入Excel檔的絕對路徑
	 * @return Workbook物件
	 */
	public Workbook getWorkbook(String path) throws Exception {
		Workbook wb = null;
		if (path == null)
			return null;
		String extString = path.substring(path.lastIndexOf("."));
		InputStream is = new FileInputStream(path);
		if (".XLS".equalsIgnoreCase(extString)) {
			wb = new HSSFWorkbook(is);
		} else if (".XLSX".equalsIgnoreCase(extString)) {
			wb = new XSSFWorkbook(is);
		}
		return wb;
	}
	
	/*
	 * 取得Excel檔中的指定頁籤
	 * @param workbook 傳入Workbook物件
	 * @param sheetNo 傳入指定的頁籤序號(由0開始，0代表第1個頁籤)
	 * @return Sheet物件
	 */
	public Sheet getSheet(Workbook workbook, int sheetNo) {
		return workbook.getSheetAt(sheetNo);
	}	
	
	/*
	 * 判斷欄位的值，以便回傳正確的型態值
	 */
	private Object getCellFormatValue(Cell cell) {
		DecimalFormat df = new DecimalFormat("#.##");
		Object cellValue = null;
		if (cell != null) {
			switch (cell.getCellType()) {
			case NUMERIC:
				cellValue = df.format(cell.getNumericCellValue());
				break;
			case FORMULA:
				if (DateUtil.isCellDateFormatted(cell)) {
					cellValue = cell.getDateCellValue();
				} else {
					cellValue = String.valueOf(cell.getNumericCellValue());
				}
				break;
			case STRING:
				cellValue = cell.getRichStringCellValue().getString();
				break;
			default:
				cellValue = "";
			}
		} else {
			cellValue = "";
		}
		return cellValue;
	}	
	
	/*
	 * 取得Excel中每一列裡面的格式(Cell)
	 */
	private String readCell(Cell cell) {
		return (String) getCellFormatValue(cell);
	}
	
	/*
	 * 把Excel中的值回傳成一個TreeMap，如下面的
	 * @param workbook 傳入Workbook物件
	 * @param sheetNo 傳入要第幾個頁籤(由0開始為第1頁)
	 * @param firstRow 傳入第1列的位置(由0開始為第1列)
	 * @param firstCol 傳入第1行的位置(由0開始為第1行)
	 * 回傳的資料範例
	 * [1, ['A000001','2020/9/24 12:01:42 AM'] 註：換算為24小時制，為00:01:42
	 * [2, ['A000002','2020/9/24 12:59:42 PM'] 註：換算為24小時制，為12:59:42
	 * [3, ['A000002','2020/9/24 11:58:41 PM'] 註：換算為24小時制，為23:58:42 
	 */
	public TreeMap<String,ArrayList<String>> readFields(Workbook workbook, int sheetNo, int firstRow, int firstCol) throws Exception {
		Sheet sheet = workbook.getSheetAt(sheetNo);
		Row row = sheet.getRow(0);
		int rownum = sheet.getPhysicalNumberOfRows();
		int colnum = row.getPhysicalNumberOfCells();
		TreeMap<String, ArrayList<String>> tm = new TreeMap<String, ArrayList<String>>();
		ArrayList<String> rowData;
		for (int i = firstRow; i < rownum; i++) {
			row = sheet.getRow(i);
			rowData = new ArrayList<>();
			if (row != null) {
				for (int j = firstCol; j < colnum; j++) {
					rowData.add(readCell(row.getCell(j)));
				}				
				tm.put(String.valueOf(i), rowData);
			} else {
				break;
			}
		}
		return tm;
	}	
	
	/*
	 * 供測試ReadExcel使用
	 */
	public static void main(String[] args) {
		TreeMap<String, ArrayList<String>> data = new TreeMap<String, ArrayList<String>>();
		ExcelReader re = new ExcelReader();
		Workbook wb = null;
		//Sheet st = null;
		try {
			wb = re.getWorkbook("C:/ColorSportsClubMDM/點名資料.xlsx");
			//st = re.getSheet(wb, 0);
			data = re.readFields(wb, 0, 1, 0);
			Set<String> key = data.keySet();
			ArrayList<String> value = null;
			for(String index : key) {
				value = data.get(index);
				for(String s : value) {
					System.out.print(s+";");
				}
				System.out.println();				
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		System.out.println("程讀取結束！");
	}	
	
}
