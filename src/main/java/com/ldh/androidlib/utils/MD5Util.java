
package com.ldh.androidlib.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    /**
     * MD5变换<br>
     * <br>
     * Modify the method by Norika in 2012.6.21 11:11<br>
     * <br>
     * 1. Change StringBuffer to StringBuilder<br>
     * The method doesn't use no static filed, so it not exists thread-safe
     * problem;<br>
     * 2. Change compute ways<br>
     * Bit computing instead of Math.<br>
     * 3. Modify this, run with more speed.<br>
     * 4. Consider of the string which contains some blank. <br>
     * 
     * @param str
     * @return
     */
    public static String Md5(String str) {
        if (str != null && !str.trim().equals("")) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                char[] HEX = {
                        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
                };
                byte[] md5Byte = md5.digest(str.getBytes("UTF8"));
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < md5Byte.length; i++) {
                    sb.append(HEX[(int) (md5Byte[i] & 0xf0) >>> 4]);
                    sb.append(HEX[(int) (md5Byte[i] & 0x0f)]);
                }
                str = sb.toString();
            } catch (NoSuchAlgorithmException e) {

            } catch (Exception e) {

            }
        }
        return str;
    }

    // MD5加密，32位
    public static String MD5For32(String str) {
        if (str == null) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
