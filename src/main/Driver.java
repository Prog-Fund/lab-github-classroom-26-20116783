package main;

import controllers.OwnerAPI;
import controllers.PetsDayCareAPI;
import models.*;
import utils.CatToyUtility;
import utils.DogBreedUtility;
import utils.ScannerInput;

import java.io.File;

public class Driver {

    private PetsDayCareAPI dayCareAPI = new PetsDayCareAPI("Urban Tails", 50, new File("pets.xml"));
    private OwnerAPI ownerAPI = new OwnerAPI(new File("owners.xml"));

    public static void main(String[] args) { new Driver(); }

    public Driver() { runMainMenu(); }

    // ---- Main Menu ----

    private void runMainMenu() {
        int option = mainMenu();
        while (option != 0) {
            switch (option) {
                case 1 -> runPetsCRUDMenu();
                case 2 -> runReportsMenu();
                case 3 -> runOwnerMenu();
                case 4 -> sortMenu();
                case 10 -> save();
                case 11 -> load();
                default -> System.out.println("Invalid option.");
            }
            ScannerInput.readNextLine("\nPress enter to continue...");
            option = mainMenu();
        }
        System.out.println("Goodbye!");
        System.exit(0);
    }

    private int mainMenu() {
        return ScannerInput.readNextInt("""
                ------- Pet Day Care --------
                | 1) Pets CRUD Menu        |
                | 2) Reports Menu          |
                | 3) Owner Menu            |
                | 4) Sort Pets             |
                | 10) Save  | 11) Load     |
                | 0) Exit                  |
                -----------------------------
                ==>> """);
    }

    // ---- Pets CRUD ----

    private void runPetsCRUDMenu() {
        int option = ScannerInput.readNextInt("""
                ----- Pets CRUD Menu ------
                | 1) Add a Pet            |
                | 2) Delete a Pet         |
                | 3) List all Pets        |
                | 4) Update a Pet         |
                | 0) Back                 |
                ---------------------------
                ==>> """);
        while (option != 0) {
            switch (option) {
                case 1 -> addPet();
                case 2 -> deletePet();
                case 3 -> System.out.println(dayCareAPI.listAllPets());
                case 4 -> updatePet();
                default -> System.out.println("Invalid option.");
            }
            ScannerInput.readNextLine("\nPress enter to continue...");
            option = ScannerInput.readNextInt("""
                    ----- Pets CRUD Menu ------
                    | 1) Add  2) Delete        |
                    | 3) List 4) Update 0) Back|
                    ==>> """);
        }
    }

    private void addPet() {
        if (ownerAPI.numberOfOwners() == 0) {
            System.out.println("Add an owner first.");
            return;
        }
        int type = ScannerInput.readNextInt("1) Dog  2) Cat  3) Parrot ==>> ");
        switch (type) {
            case 1 -> addDog();
            case 2 -> addCat();
            case 3 -> addParrot();
            default -> System.out.println("Invalid type.");
        }
    }

    private void addDog() {
        String name = ScannerInput.readNextLine("Name: ");
        int age = ScannerInput.readNextInt("Age: ");
        Owner owner = selectOwner();
        if (owner == null) return;
        char sex = ScannerInput.readNextChar("Sex (M/F/U): ");
        boolean vax = ScannerInput.readNextLine("Vaccinated? (y/n): ").equalsIgnoreCase("y");
        double weight = ScannerInput.readNextDouble("Weight (kg): ");
        boolean neutered = ScannerInput.readNextLine("Neutered? (y/n): ").equalsIgnoreCase("y");
        System.out.println(DogBreedUtility.getValidBreeds());
        String breed = ScannerInput.readNextLine("Breed: ");
        boolean dangerous = ScannerInput.readNextLine("Dangerous breed? (y/n): ").equalsIgnoreCase("y");
        dayCareAPI.addPet(new Dog(name, age, owner, Pet.generateNextId(), sex, vax, weight, neutered, breed, dangerous));
        System.out.println("Dog added.");
    }

    private void addCat() {
        String name = ScannerInput.readNextLine("Name: ");
        int age = ScannerInput.readNextInt("Age: ");
        Owner owner = selectOwner();
        if (owner == null) return;
        char sex = ScannerInput.readNextChar("Sex (M/F/U): ");
        boolean vax = ScannerInput.readNextLine("Vaccinated? (y/n): ").equalsIgnoreCase("y");
        double weight = ScannerInput.readNextDouble("Weight (kg): ");
        boolean neutered = ScannerInput.readNextLine("Neutered? (y/n): ").equalsIgnoreCase("y");
        boolean indoor = ScannerInput.readNextLine("Indoor cat? (y/n): ").equalsIgnoreCase("y");
        System.out.println(CatToyUtility.getValidToys());
        String toy = ScannerInput.readNextLine("Favourite toy: ");
        dayCareAPI.addPet(new Cat(name, age, owner, Pet.generateNextId(), sex, vax, weight, neutered, indoor, toy));
        System.out.println("Cat added.");
    }

    private void addParrot() {
        String name = ScannerInput.readNextLine("Name: ");
        int age = ScannerInput.readNextInt("Age: ");
        Owner owner = selectOwner();
        if (owner == null) return;
        double wingspan = ScannerInput.readNextDouble("Wingspan (cm, 3-400): ");
        boolean canFly = ScannerInput.readNextLine("Can fly? (y/n): ").equalsIgnoreCase("y");
        int words = ScannerInput.readNextInt("Number of words known: ");
        dayCareAPI.addPet(new Parrot(name, age, owner, Pet.generateNextId(), wingspan, canFly, words));
        System.out.println("Parrot added.");
    }

    private Owner selectOwner() {
        System.out.println(ownerAPI.listOwners());
        Owner o = ownerAPI.getOwner(ScannerInput.readNextInt("Select owner index: "));
        if (o == null) System.out.println("Invalid owner.");
        return o;
    }

    private void deletePet() {
        System.out.println(dayCareAPI.listAllPets());
        if (dayCareAPI.numberOfPets() == 0) return;
        Pet deleted = dayCareAPI.deletePetByIndex(ScannerInput.readNextInt("Index to delete: "));
        System.out.println(deleted != null ? "Deleted: " + deleted.getName() : "Invalid index.");
    }

    private void updatePet() {
        System.out.println(dayCareAPI.listAllPets());
        if (dayCareAPI.numberOfPets() == 0) return;
        Pet pet = dayCareAPI.getPet(ScannerInput.readNextInt("Index to update: "));
        if (pet == null) { System.out.println("Invalid index."); return; }
        String name = ScannerInput.readNextLine("New name (Enter to keep): ");
        if (!name.isBlank()) pet.setName(name);
        int age = ScannerInput.readNextInt("New age (-1 to keep): ");
        if (age >= 0) pet.setAge(age);
        System.out.println("Updated: " + pet);
    }

    // ---- Reports ----

    private void runReportsMenu() {
        int option = ScannerInput.readNextInt("""
                ------- Reports Menu ----------
                | 1) All Pets               |
                | 2) All Dogs               |
                | 3) All Cats               |
                | 4) All Parrots            |
                | 5) Dangerous Dogs         |
                | 6) Pets by Owner          |
                | 7) Pets > N days          |
                | 8) Weekly Income          |
                | 9) Check In / Check Out   |
                | 0) Back                   |
                --------------------------------
                ==>> """);
        while (option != 0) {
            switch (option) {
                case 1 -> System.out.println(dayCareAPI.listAllPets());
                case 2 -> System.out.println(dayCareAPI.listAllDogs());
                case 3 -> System.out.println(dayCareAPI.listAllCats());
                case 4 -> System.out.println(dayCareAPI.listAllParrots());
                case 5 -> System.out.println(dayCareAPI.listAllDangerousDogs());
                case 6 -> System.out.println(dayCareAPI.listAllPetsByOwner(ScannerInput.readNextLine("Owner name: ")));
                case 7 -> System.out.println(dayCareAPI.listAllPetsThatStayMoreThanDays(ScannerInput.readNextInt("More than how many days? ")));
                case 8 -> System.out.println("Weekly Income: €" + dayCareAPI.getWeeklyIncome());
                case 9 -> checkInOut();
                default -> System.out.println("Invalid option.");
            }
            ScannerInput.readNextLine("\nPress enter to continue...");
            option = ScannerInput.readNextInt("Reports (1-9, 0=back): ");
        }
    }

    private void checkInOut() {
        System.out.println(dayCareAPI.listAllPets());
        if (dayCareAPI.numberOfPets() == 0) return;
        Pet pet = dayCareAPI.getPet(ScannerInput.readNextInt("Pet index: "));
        if (pet == null) { System.out.println("Invalid."); return; }
        int action = ScannerInput.readNextInt("1) Check In  2) Check Out ==>> ");
        int day = ScannerInput.readNextInt("Day (0=Mon, 1=Tue, 2=Wed, 3=Thu, 4=Fri, 5=Sat): ");
        if (action == 1) pet.checkIn(day);
        else if (action == 2) pet.checkOut(day);
        System.out.println(pet.getName() + " now attending " + pet.numOfDaysAttending() + " day(s).");
    }

    // ---- Sort ----

    private void sortMenu() {
        int option = ScannerInput.readNextInt("1) Sort by ID (desc)  2) Sort by Name (A-Z)  0) Back ==>> ");
        if (option == 1) { dayCareAPI.sortPetsById(); System.out.println(dayCareAPI.listAllPets()); }
        else if (option == 2) { dayCareAPI.sortPetsByName(); System.out.println(dayCareAPI.listAllPets()); }
    }

    // ---- Owners ----

    private void runOwnerMenu() {
        int option = ScannerInput.readNextInt("""
                ----- Owner Menu -----
                | 1) Add Owner      |
                | 2) Delete Owner   |
                | 3) List Owners    |
                | 0) Back           |
                ----------------------
                ==>> """);
        while (option != 0) {
            switch (option) {
                case 1 -> { ownerAPI.addOwner(new Owner(ScannerInput.readNextLine("Name: "), ScannerInput.readNextLine("Phone: "))); System.out.println("Owner added."); }
                case 2 -> { System.out.println(ownerAPI.listOwners()); Owner d = ownerAPI.deleteOwner(ScannerInput.readNextInt("Index to delete: ")); System.out.println(d != null ? "Deleted: " + d.getOwnerName() : "Invalid."); }
                case 3 -> System.out.println(ownerAPI.listOwners());
                default -> System.out.println("Invalid option.");
            }
            ScannerInput.readNextLine("\nPress enter to continue...");
            option = ScannerInput.readNextInt("Owner menu (1-3, 0=back): ");
        }
    }

    // ---- Save / Load ----

    private void save() {
        try { dayCareAPI.save(); ownerAPI.save(); System.out.println("Saved."); }
        catch (Exception e) { System.err.println("Save failed: " + e.getMessage()); }
    }

    private void load() {
        try { dayCareAPI.load(); ownerAPI.load(); System.out.println("Loaded."); }
        catch (Exception e) { System.err.println("Load failed: " + e.getMessage()); }
    }
}
