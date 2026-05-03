package utils;

public class Utilities {

    public static String truncateString(String str, int len) {
        return str.length() <= len ? str : str.substring(0, len);
    }

    public static boolean validateStringLength(String str, int maxLen) {
        return str.length() <= maxLen;
    }

    public static boolean validRange(double num, double min, double max) {
        return num >= min && num <= max;
    }

    public static double toTwoDecimalPlaces(double num) {
        return (int)(num * 100) / 100.0;
    }
}
