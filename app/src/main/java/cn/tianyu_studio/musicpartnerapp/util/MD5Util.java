package cn.tianyu_studio.musicpartnerapp.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.tianyu_studio.musicpartnerapp.constants.SysConsts;

public class MD5Util {
    public static String getMD5(String str) {
        StringBuilder sb = new StringBuilder();//定义可变的字符串
        char[] chars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F' };
        byte[] b = str.getBytes();//接受的字符串转化为字节数组
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("md5");//调用MD5算法
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
        byte[] md5 = md.digest(b);
        for (byte m : md5) {
            sb.append(chars[(m >> 4) & 0x0f]);
            sb.append(chars[m & 0x0f]);
        }
        return sb.toString();
    }

    public static String getSign(String token, String timeMillion) {
        return getMD5(token + timeMillion + SysConsts.SECRET);
    }

    public static void main(String[] args) {
        System.out.println(getMD5("bg_login"));
        System.out.println(getMD5(getMD5("bg_login")));
    }
}
