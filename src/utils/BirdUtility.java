package utils;

public class BirdUtility {

    // Converts number of words to a vocabulary category. Returns null if invalid.
    public static String convertToVocabSize(int numWords) {
        if (numWords < 0)    return null;
        if (numWords <= 10)  return "Basic";
        if (numWords <= 50)  return "Intermediate";
        if (numWords <= 200) return "Advanced";
        return "Amazing";
    }

    public static boolean isValidVocabSize(String v) {
        return v.equals("Basic") || v.equals("Intermediate")
                || v.equals("Advanced") || v.equals("Amazing");
    }
}
