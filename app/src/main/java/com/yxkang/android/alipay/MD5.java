package com.yxkang.android.alipay;

import java.security.MessageDigest;

/**
 * Created by fine on 2016/4/28.
 */
public class MD5 {

    public static String getMessageDigest(byte[] buffer) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(buffer);
            byte[] digest = messageDigest.digest();
            int j = digest.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : digest) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}
