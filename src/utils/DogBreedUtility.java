package utils;

public class DogBreedUtility {

    private static final String[] validBreeds = {
        "Labrador Retriever", "German Shepherd", "Golden Retriever",
        "Bulldog", "Beagle", "Rottweiler", "Pit Bull"
    };

    public static boolean isValidBreed(String breed) {
        for (String b : validBreeds)
            if (b.equalsIgnoreCase(breed)) return true;
        return false;
    }

    public static String getValidBreeds() {
        String str = "";
        for (int i = 0; i < validBreeds.length; i++)
            str += (i + 1) + ") " + validBreeds[i] + "\n";
        return str;
    }
}
