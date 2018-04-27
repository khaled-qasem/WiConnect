package wiconnect.khaled.com.wiconnect.utils;

/**
 * Created by Khaled on 4/27/2018.
 * Assumptions
 * Descriptions
 */

public class StringUtil {
    public static String trimQuotes(String str) {
        if (!isEmpty(str)) {
            return str.replaceAll("^\"*", "").replaceAll("\"*$", "");
        }

        return str;
    }

    private static boolean isEmpty(CharSequence string) {
        return string == null || string.toString().isEmpty();
    }

}
