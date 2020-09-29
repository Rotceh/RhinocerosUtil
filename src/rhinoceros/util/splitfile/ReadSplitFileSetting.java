package rhinoceros.util.splitfile;

public class ReadSplitFileSetting {
	private String filePath; //檔案絕對路徑
	private String encode; //編碼方式(支援BIG5 UTF-8)兩種
	private String splitCode; //分隔符號(支援多字元分隔符號，例如 #|
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getEncode() {
		return encode;
	}
	public void setEncode(String encode) {
		this.encode = encode;
	}
	public String getSplitCode() {
		return splitCode;
	}
	public void setSplitCode(String splitCode) {
		this.splitCode = splitCode;
	}
}
