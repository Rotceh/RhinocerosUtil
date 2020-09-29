/*
 * 解析有「分隔符號」檔案的內容
 * 支援單字元(例如「,」)或多字元(例如「#|」這些自訂義的分隔符號)
 * 使用的第三方JAR檔 
 * jboss-logging-3.3.0.Final.jar
 * jtstand-editor-1.5.13.jar 
 * 
 * @author 黃郁授,吳彥儒
 * @date 2020/09/25
 */

package rhinoceros.util.splitfile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import org.fife.io.UnicodeReader;

public class ReadSplitFile {

    private ReadSplitFileSetting setting; //自訂的「參數」類別

    // 回傳解析結果
    private TreeMap<Integer, TreeMap<Integer, String>> result = new TreeMap<Integer, TreeMap<Integer, String>>();

    // 設定解析檔案相關參數
    public boolean setConfig(ReadSplitFileSetting setting) {
        // 檢核是否有傳入設定參數
        if (setting == null) {
            return false;
        }

        // 要解析的檔案絕對路徑不可為null或長度為0
        if (setting.getFilePath() == null || setting.getFilePath().length() <= 0) {
            return false;
        }

        // 設定的編碼不可為null或長度為0
        if (setting.getEncode() == null || setting.getEncode().length() <= 0) {
            return false;
        }

        // 目前只支援UTF-8編碼
        if (setting.getEncode().equalsIgnoreCase("UTF-8")) {
        } else {
            return false;
        }

        // 分隔符號字元不可為null或長度為0
        if (setting.getSplitCode() == null || setting.getSplitCode().length() <= 0) {
            return false;
        }

        this.setting = setting;
        return true;
    }

 
    public TreeMap<Integer, TreeMap<Integer, String>> readFile() throws IOException {
        FileInputStream fis = new FileInputStream(setting.getFilePath());
        UnicodeReader ur = new UnicodeReader(fis, "utf-8");
        BufferedReader reader = new BufferedReader(ur);

        /*
         * 改用UnicodeReader去除文字檔BOM字元，故不用下面這兩列的方式了
         * BufferedReader reader;
         * reader = Files.newBufferedReader(Paths.get(setting.getFilePath()), StandardCharsets.UTF_8);
         */
        String line;
        Integer i = 1;
        while ((line = reader.readLine()) != null) {
            /*
             *  透過splitFile切割欄位，將該列各欄位轉成TreeMap<Integer, String>型態
             *  將fiels放入result這個TreeMap屬性中
             */
            result.put(i, splitFile(line));
            i++;
        }
        reader.close();
        return result;
    }

 

    private TreeMap<Integer, String> splitFile(String line) {
        TreeMap<Integer, String> fields = new TreeMap<Integer, String>();
        ArrayList<Integer> position = new ArrayList<Integer>();
        for (int k=0; k < line.length(); k++){
            /*
             * regionMatches()用法：
             * 參數1：true不區分大小寫
             * 參數2：字串中所有字元的位置編號
             * 參數3：要查的字串
             * 參數4：查詢開始位置
             * 參數5：要查的字串長度
             */

            if (line.regionMatches(true, k, setting.getSplitCode(), 0, setting.getSplitCode().length())) {
                position.add(k);
            }
        }

        //若是沒有發現任何分隔符號，代表只有1個欄位
        if (position.size()==0) {
            fields.put(1, line);
        } else {
            for(int i=0; i <= position.size(); i++) {
                //第一個欄位由0開始抓
                if (i==0) {                   
                    fields.put(i+1, line.substring(0,position.get(i)));
                //最後一個欄位由最後一個分隔符號最後字串結尾   
                } else if (i>=position.size()) {               
                    fields.put(i+1, line.substring(position.get(i-1)+2));
                //若兩個分隔符號緊密相臨，則代表該欄位無字元，給一個""值   
                } else if ((position.get(i-1)+2)==position.get(i)) {    
                    fields.put(i+1, "");
                } else { 
                    //其它情況，則由前1個分隔符號抓到下一個分隔符號之間
                    fields.put(i+1, line.substring(position.get(i-1)+2, position.get(i)));
                }
            }
        }       
        return fields;
    }

    public static void main(String[] args) throws IOException {
        // ReadSplitFile使用範例(測試吃#|分隔符號的檔)
        ReadSplitFileSetting rsft = new ReadSplitFileSetting();
        rsft.setEncode("UTF-8");
        rsft.setSplitCode("#|");
        rsft.setFilePath("C:/temp/NEXSYS_807_01_20200723161512.txt");
        ReadSplitFile rsf = new ReadSplitFile();
        rsf.setConfig(rsft);
        TreeMap<Integer, TreeMap<Integer, String>> result = rsf.readFile();

        // 取得每列的key值
        Set<Integer> rowSet = result.keySet();
        Set<Integer> columnSet = null;
        for (Integer i : rowSet) {
            // 取得每一列的值(一個MAP物件)
            columnSet = result.get(i).keySet();
            for (Integer j : columnSet) {
                System.out.print(result.get(i).get(j) + "#|");
            }
            System.out.println();
        }
    }
}
