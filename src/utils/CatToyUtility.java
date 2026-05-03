package utils;

public class CatToyUtility {

    private static final String[] validToys = {
        "FEATHER WAND", "LASER POINTER", "CATNIP MOUSE",
        "BALL", "SCRATCHING POST", "TUNNEL", "TEASER WAND"
    };

    public static boolean isValidCatToy(String toy) {
        for (String t : validToys)
            if (t.equalsIgnoreCase(toy)) return true;
        return false;
    }

    public static String getValidToys() {
        String str = "";
        for (int i = 0; i < validToys.length; i++)
            str += (i + 1) + ") " + validToys[i] + "\n";
        return str;
    }
}
