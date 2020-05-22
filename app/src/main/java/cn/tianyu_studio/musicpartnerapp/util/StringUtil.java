package cn.tianyu_studio.musicpartnerapp.util;

import java.text.DecimalFormat;

public class StringUtil {

    /**
     * 将转义后的html解码为正常的html
     *
     * @param str
     * @return
     */
    public static String decodeToHtml(String str) {
        return str.replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&amp;", "&")
                .replace("&quot;", "\"")
                .replace("&#39;", "'");

    }

    public static String formatToSepara(long number) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(number);
    }
}
