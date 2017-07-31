package com.dzr.util;

/**
 * @author dingzr
 * @Description
 * @ClassName DataUtils
 * @since 2017/7/31 10:51
 */
public class DataUtils {

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

}
