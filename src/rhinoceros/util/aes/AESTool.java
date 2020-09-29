/*
 * AES加解密工具
 * 使用第三方的Jar
 * sun.misc.BASE64Decoder.jar
 * Weber NoteBook 主機板序號 NBMZ111004524048CA7600
 * Jimmy MacBook 序號 C02SJ4TVFVH3
 * 
 * @author 黃郁授,吳彥儒
 * @date 2020/09/28
 * 
 * 
 */
package rhinoceros.util.aes;

import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class AESTool {
    /*
     * 加密
     * @param sSrc 明文
     * @param sKey 加解密的金鑰(一定要剛好16碼長)
     * @return 密文
     */
    public String encrypt(String sSrc, String sKey) throws Exception {
        // 註：sKey必須為16碼
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); // 演算法/模式/補碼方式
        // 使用CBC模式，需要一個向量值iv，可以增加加密演算法的強度
        IvParameterSpec iv = new IvParameterSpec("9192939495969798".getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());
        // 使用BASE64轉碼功能，有2次加密的作用
        return new BASE64Encoder().encode(encrypted);
    }

    /*
     * 解密
     * @param sSrc 密文
     * @param sKey 加解密的金鑰(一定要剛好16碼長)
     * @return 明文
     */
	public String decrypt(String sSrc, String sKey) throws Exception {
    	String originalString = "";
        byte[] raw = sKey.getBytes("ASCII");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec("9192939495969798".getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);// 先用base64解密
        try {
            byte[] original = cipher.doFinal(encrypted1);
            originalString = new String(original);
        } catch (Exception e) {
        	throw e;
        }        
    	return originalString;
    }

	/*
	 * 解密
	 * @param 金鑰
	 */
    public void decrypt(String sKey) throws Exception {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        String sSrc = null; // 密文
        System.out.println();
        System.out.println("==================================================================");
        System.out.println("  Decrypt tool (Java version)");
        System.out.println("  Please input the encryption string:");
        while (true) {
            sSrc = scanner.nextLine();
            if (sSrc.equalsIgnoreCase("back")) {
                System.out.println();
                break;
            }

            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("9192939495969798".getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);// 先用base64解密

            try {
                byte[] original = cipher.doFinal(encrypted1);
                System.out.println("  plaintext string:[" + new String(original) + "]");
            } catch (Exception e) {
                System.out.println(e.toString());
                System.out.println();
            }

            System.out.println();
            System.out.println("  Please input the encryption string:(Back to the pervious level, please input [back]):");
        }
    }

    /*
     * 開啟加解密工具Console
     * @param key 金鑰
     */
    public void decryptTool(String key) throws Exception {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        String plainText = null; // 原始明文
        String enString = null; // 加密後字串
        String deString = null; // 解密後字串
        System.out.println();
        System.out.println("==================================================================");
        System.out.println("  Sinopac encryption tool (Java version)");
        System.out.println("  Please input the plaintext:");
        while (true) {
            plainText = scanner.nextLine();
            if (plainText.equalsIgnoreCase("back")) {
                System.out.println();
                break;
            }
            enString = this.encrypt(plainText, key);
            deString = this.decrypt(enString, key);
            System.out.println("  encryption string:[" + enString + "]");
            System.out.println("  decryption string:[" + deString + "] << confirm it ");
            System.out.println();
            System.out.println("  Please input next plaintext(Back to the pervious level, please input [back]):");
        }
    }

    // 主程式
    public static void main(String[] args) throws Exception {
        String key = "Weber&Jimmy@2020"; // 金鑰
        Scanner scanner = new Scanner(System.in); // scanner reads block of input
        AESTool aesTool = new AESTool();
        String input = null;
        System.out.println();
        while (true) {
            System.out.println("==================================================================");
            System.out.println("  Choice the function(input [exit] to leave this program):");
            System.out.println("  1.Encrypt Tool");
            System.out.println("  2.Decrypt Tool");

            input = scanner.nextLine();
            if (input != null && (input.equalsIgnoreCase("1") || input.equalsIgnoreCase("2") || input.equalsIgnoreCase("exit"))) {
                if (input.equalsIgnoreCase("exit")) {
                    break;
                }

                if (input.equalsIgnoreCase("1")) {
                    aesTool.decryptTool(key);
                }

                if (input.equalsIgnoreCase("2")) {
                    aesTool.decrypt(key);
                }
            } else {
                System.out.println("re-enter the function code 1/2 or exit:");
            }
        }
        scanner.close();
    }
}