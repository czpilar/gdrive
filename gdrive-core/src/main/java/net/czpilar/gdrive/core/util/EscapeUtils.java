package net.czpilar.gdrive.core.util;

/**
 * Strings escaping utility class.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class EscapeUtils {

    /**
     * Escapes only single quotes in given string.
     *
     * @param value
     * @return
     */
    public static String escapeSingleQuote(String value) {
        return value == null ? null : escapeSingleQuoteInternal(value);
    }

    private static String escapeSingleQuoteInternal(String value) {
        StringBuilder sb = new StringBuilder();
        int cnt = 0;
        for (char c : value.toCharArray()) {
            switch (c) {
                case '\\':
                    cnt++;
                    break;
                case '\'':
                    if (cnt % 2 == 0) {
                        sb.append('\\');
                    }
                default:
                    cnt = 0;
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
