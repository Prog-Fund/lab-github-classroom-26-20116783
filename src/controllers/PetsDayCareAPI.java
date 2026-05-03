package controllers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import models.*;
import utils.BirdUtility;
import utils.ISerializer;
import utils.Utilities;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Manages all pets at Urban Tails Daycare.
 * Handles CRUD, reporting, sorting, searching, and XML persistence.
 *
 * @author Student Name
 * @version 1.0
 */
public class PetsDayCareAPI implements ISerializer {

    private ArrayList<Pet> pets = new ArrayList<>();
    private String name         = "";
    private int maxNumberOfPets;
    private File file;

    public PetsDayCareAPI(String name, int maxNumberOfPets, File file) {
        initName(name);
        this.maxNumberOfPets = maxNumberOfPets;
        this.file = file;
    }

    // --- Getters / Setters ---

    public String getName()          { return name; }
    public int getMaxNumberOfPets()  { return maxNumberOfPets; }
    public ArrayList<Pet> getPetsArray() { return pets; }

    public void initName(String name)  { this.name = Utilities.truncateString(name, 20); }
    public void setName(String name)   { if (Utilities.validateStringLength(name, 20)) this.name = name; }
    public void setMaxNumberOfPets(int max) { this.maxNumberOfPets = max; }
    public void setPetsArray(ArrayList<Pet> pets) { this.pets = pets; }

    // --- CRUD ---

    /** Adds a pet to the list. Returns true if successful. */
    public boolean addPet(Pet pet) { return pets.add(pet); }

    /** Removes the pet at the given index. Returns the removed pet, or null if index invalid. */
    public Pet deletePetByIndex(int index) {
        return isValidPetIndex(index) ? pets.remove(index) : null;
    }

    /** Removes the pet with the given id. Returns the removed pet, or null if not found. */
    public Pet deletePetById(int id) {
        Pet found = getPetById(id);
        if (found != null) { pets.remove(found); return found; }
        return null;
    }

    /** Returns the pet at the given index, or null if index invalid. */
    public Pet getPet(int index) {
        return isValidPetIndex(index) ? pets.get(index) : null;
    }

    /** Returns the first pet with the given name (case-insensitive), or null if not found. */
    public Pet getPet(String name) {
        for (Pet p : pets)
            if (p.getName().equalsIgnoreCase(name)) return p;
        return null;
    }

    /** Returns the pet with the given id, or null if not found. */
    public Pet getPetById(int id) {
        for (Pet p : pets)
            if (p.getId() == id) return p;
        return null;
    }

    /** Updates the pet at the given index. Returns the updated pet, or null if index invalid. */
    public Pet updatePet(int index, Pet updated) {
        if (isValidPetIndex(index)) { pets.set(index, updated); return updated; }
        return null;
    }

    // --- Reporting ---

    /** Returns a numbered list of all pets, or "No Pets" if empty. */
    public String listAllPets() {
        if (pets.isEmpty()) return "No Pets";
        String str = "";
        for (int i = 0; i < pets.size(); i++) str += i + ": " + pets.get(i) + "\n";
        return str;
    }

    /** Returns a numbered list of all dogs, or "No Dogs" if none. */
    public String listAllDogs() {
        String str = "";
        for (int i = 0; i < pets.size(); i++)
            if (pets.get(i) instanceof Dog) str += i + ": " + pets.get(i) + "\n";
        return str.isEmpty() ? "No Dogs" : str;
    }

    /** Returns a numbered list of all cats, or "No Cats" if none. */
    public String listAllCats() {
        String str = "";
        for (int i = 0; i < pets.size(); i++)
            if (pets.get(i) instanceof Cat) str += i + ": " + pets.get(i) + "\n";
        return str.isEmpty() ? "No Cats" : str;
    }

    /** Returns a numbered list of all parrots, or "No Parrots" if none. */
    public String listAllParrots() {
        String str = "";
        for (int i = 0; i < pets.size(); i++)
            if (pets.get(i) instanceof Parrot) str += i + ": " + pets.get(i) + "\n";
        return str.isEmpty() ? "No Parrots" : str;
    }

    /** Returns a numbered list of dangerous dogs, or the appropriate message if none. */
    public String listAllDangerousDogs() {
        String str = "";
        for (int i = 0; i < pets.size(); i++)
            if (pets.get(i) instanceof Dog && ((Dog) pets.get(i)).isDangerousBreed())
                str += i + ": " + pets.get(i) + "\n";
        return str.isEmpty() ? "No Dangerous Dogs in the Kennels" : str;
    }

    /** Returns all pets belonging to the given owner name, or a message if none found. */
    public String listAllPetsByOwner(String ownerName) {
        String str = "";
        for (int i = 0; i < pets.size(); i++) {
            Pet p = pets.get(i);
            if (p.getOwner() != null && p.getOwner().getOwnerName().equalsIgnoreCase(ownerName))
                str += i + ": " + p + "\n";
        }
        return str.isEmpty() ? "No Pet with owner " + ownerName : str;
    }

    /** Returns all pets attending more than numDays days, or a message if none found. */
    public String listAllPetsThatStayMoreThanDays(int numDays) {
        String str = "";
        for (int i = 0; i < pets.size(); i++)
            if (pets.get(i).numOfDaysAttending() > numDays) str += i + ": " + pets.get(i) + "\n";
        return str.isEmpty() ? "No Pet stays longer than " + numDays : str;
    }

    /** Returns a list of unique owner names from the pets registered. */
    public String listOwners() {
        String str = "";
        ArrayList<String> seen = new ArrayList<>();
        for (Pet p : pets) {
            if (p.getOwner() != null && !seen.contains(p.getOwner().getOwnerName())) {
                seen.add(p.getOwner().getOwnerName());
                str += p.getOwner() + "\n";
            }
        }
        return str.isEmpty() ? "No owners found" : str;
    }

    // --- numberOfX ---

    /** Returns the total number of pets. */
    public int numberOfPets() { return pets.size(); }

    /** Returns the number of dogs in the system. */
    public int numberOfDogs() {
        int n = 0; for (Pet p : pets) if (p instanceof Dog) n++; return n;
    }

    /** Returns the number of cats in the system. */
    public int numberOfCats() {
        int n = 0; for (Pet p : pets) if (p instanceof Cat) n++; return n;
    }

    /** Returns the number of parrots in the system. */
    public int numberOfParrots() {
        int n = 0; for (Pet p : pets) if (p instanceof Parrot) n++; return n;
    }

    /** Returns the number of dangerous dogs in the system. */
    public int numberOfDangerousDogs() {
        int n = 0;
        for (Pet p : pets) if (p instanceof Dog && ((Dog) p).isDangerousBreed()) n++;
        return n;
    }

    /** Returns the number of indoor cats in the system. */
    public int numberOfIndoorCats() {
        int n = 0;
        for (Pet p : pets) if (p instanceof Cat && ((Cat) p).isIndoorCat()) n++;
        return n;
    }

    /** Returns the number of parrots whose vocabulary category matches the given word count. */
    public int numberOfParrotsByVocabularySize(int vocabSize) {
        String category = BirdUtility.convertToVocabSize(vocabSize);
        if (category == null) return 0;
        int n = 0;
        for (Pet p : pets)
            if (p instanceof Parrot && ((Parrot) p).getVocabularySize().equals(category)) n++;
        return n;
    }

    // --- Validation ---

    /** Returns true if the index exists in the pets list. */
    public boolean isValidPetIndex(int index) {
        return index >= 0 && index < pets.size();
    }

    // --- Calculations ---

    /** Returns the total weekly income from all pets currently attending. */
    public double getWeeklyIncome() {
        double total = 0;
        for (Pet p : pets) total += p.calculateWeeklyFee();
        return Utilities.toTwoDecimalPlaces(total);
    }

    /** Returns the average number of days pets attend per week. */
    public double getAverageNumDaysPerWeek() {
        if (pets.isEmpty()) return 0;
        double total = 0;
        for (Pet p : pets) total += p.numOfDaysAttending();
        return Utilities.toTwoDecimalPlaces(total / pets.size());
    }

    /** Finds a dog matching the given owner name, breed and age. Returns null if not found. */
    public Pet findDogByOwnerAndBreedAndAge(String ownerName, String breed, int age) {
        for (Pet p : pets) {
            if (p instanceof Dog) {
                Dog d = (Dog) p;
                if (d.getOwner() != null
                        && d.getOwner().getOwnerName().equalsIgnoreCase(ownerName)
                        && d.getBreed().equalsIgnoreCase(breed)
                        && d.getAge() == age)
                    return d;
            }
        }
        return null;
    }

    /** Returns all pets for the given owner name, or "No Pets for [name]" if none. */
    public String getPetsByOwnersName(String name) {
        String str = "";
        for (Pet p : pets)
            if (p.getOwner() != null && p.getOwner().getOwnerName().equalsIgnoreCase(name))
                str += p + "\n";
        return str.isEmpty() ? "No Pets for " + name : str;
    }

    // --- Sorting (manual selection sort, no library methods) ---

    /** Sorts pets by id descending (highest id first). */
    public void sortPetsById() {
        for (int i = pets.size() - 1; i >= 0; i--) {
            int lowest = 0;
            for (int j = 0; j <= i; j++)
                if (pets.get(j).getId() < pets.get(lowest).getId()) lowest = j;
            swapPets(i, lowest);
        }
    }

    /** Sorts pets by name ascending (A to Z). */
    public void sortPetsByName() {
        for (int i = pets.size() - 1; i >= 0; i--) {
            int highest = 0;
            for (int j = 0; j <= i; j++)
                if (pets.get(j).getName().compareToIgnoreCase(pets.get(highest).getName()) > 0)
                    highest = j;
            swapPets(i, highest);
        }
    }

    private void swapPets(int i, int j) {
        Pet tmp = pets.get(i); pets.set(i, pets.get(j)); pets.set(j, tmp);
    }

    private void swapPets(Pet a, Pet b) {
        int ia = pets.indexOf(a), ib = pets.indexOf(b);
        if (ia != -1 && ib != -1) swapPets(ia, ib);
    }

    // --- Persistence ---

    /** Saves all pets to the XML file. */
    @Override
    public void save() throws Exception {
        XStream xstream = new XStream(new DomDriver());
        ObjectOutputStream out = xstream.createObjectOutputStream(new FileWriter(file));
        out.writeObject(pets);
        out.close();
    }

    /** Loads all pets from the XML file. Updates nextId to avoid duplicate IDs. */
    @Override
    @SuppressWarnings("unchecked")
    public void load() throws Exception {
        Class<?>[] classes = {Pet.class, Mammal.class, Bird.class, Dog.class, Cat.class, Parrot.class, Owner.class};
        XStream xstream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(classes);
        ObjectInputStream in = xstream.createObjectInputStream(new FileReader(file));
        pets = (ArrayList<Pet>) in.readObject();
        in.close();
        // Ensure new pets get IDs higher than any loaded ID
        int maxId = 999;
        for (Pet p : pets) if (p.getId() > maxId) maxId = p.getId();
        Pet.setNextId(maxId + 1);
    }

    /** Returns the name of the XML file used for persistence. */
    @Override
    public String fileName() { return file.getName(); }
}
