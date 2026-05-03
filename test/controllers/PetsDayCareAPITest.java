package controllers;

import models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class PetsDayCareAPITest {

    private PetsDayCareAPI api, emptyApi;
    private Owner owner1, owner2, owner3;
    private Dog dog1, dog2, dangerousDog;
    private Cat indoorCat, outdoorCat;
    private Parrot parrot1, parrot2;

    @BeforeEach
    void setUp() {
        api      = new PetsDayCareAPI("Urban Tails", 50, new File("testpets.xml"));
        emptyApi = new PetsDayCareAPI("Empty", 10, new File("empty.xml"));

        owner1 = new Owner("Alice", "0871111111");
        owner2 = new Owner("Bob",   "0852222222");
        owner3 = new Owner("Carol", "0863333333");

        dog1         = new Dog("Buddy",   3, owner1, 1000, 'M', true,  25.0, true,  "Labrador Retriever", false);
        dog2         = new Dog("Max",     5, owner2, 1001, 'M', false, 30.0, false, "Beagle",             false);
        dangerousDog = new Dog("Spike",   4, owner3, 1002, 'M', false, 40.0, false, "Rottweiler",         true);
        indoorCat    = new Cat("Whiskers",2, owner1, 1003, 'F', true,  4.0,  true,  true,  "FEATHER WAND");
        outdoorCat   = new Cat("Mittens", 6, owner2, 1004, 'F', true,  5.0,  false, false, "BALL");
        parrot1      = new Parrot("Polly",   3, owner3, 1005, 25.0, true,  5);    // Basic
        parrot2      = new Parrot("Charlie", 7, owner1, 1006, 30.0, false, 300);  // Amazing

        // Attendance: dog1=2 days, dog2=1 day, indoorCat=3 days, parrot1=1 day
        dog1.checkIn(0); dog1.checkIn(1);
        dog2.checkIn(0);
        indoorCat.checkIn(0); indoorCat.checkIn(1); indoorCat.checkIn(2);
        parrot1.checkIn(0);

        api.addPet(dog1); api.addPet(dog2); api.addPet(dangerousDog);
        api.addPet(indoorCat); api.addPet(outdoorCat);
        api.addPet(parrot1); api.addPet(parrot2);
    }

    @AfterEach
    void tearDown() { api = emptyApi = null; owner1 = owner2 = owner3 = null; }

    // --- Getters / Setters ---

    @Test void getName()          { assertEquals("Urban Tails", api.getName()); }
    @Test void setNameValid()     { api.setName("New Name"); assertEquals("New Name", api.getName()); }
    @Test void setNameTooLong()   { api.setName("A".repeat(21)); assertEquals("Urban Tails", api.getName()); }
    @Test void getMaxPets()       { assertEquals(50, api.getMaxNumberOfPets()); }
    @Test void setMaxPets()       { api.setMaxNumberOfPets(100); assertEquals(100, api.getMaxNumberOfPets()); }
    @Test void getPetsArray()     { assertEquals(7, api.getPetsArray().size()); }

    // --- CRUD ---

    @Test void addPet() {
        assertTrue(api.addPet(new Dog("Rex", 1, owner1, 2000, 'M', true, 20.0, false, "Bulldog", false)));
        assertEquals(8, api.numberOfPets());
    }

    @Test void deletePetByIndexValid() {
        Pet d = api.deletePetByIndex(0);
        assertNotNull(d); assertEquals(dog1, d); assertEquals(6, api.numberOfPets());
    }

    @Test void deletePetByIndexInvalid() {
        assertNull(api.deletePetByIndex(-1));
        assertNull(api.deletePetByIndex(99));
    }

    @Test void deletePetByIdValid() {
        Pet d = api.deletePetById(1000);
        assertNotNull(d); assertEquals(dog1, d);
    }

    @Test void deletePetByIdInvalid() { assertNull(api.deletePetById(9999)); }

    @Test void getPetByIndex() {
        assertEquals(dog1, api.getPet(0));
        assertNull(api.getPet(-1));
        assertNull(api.getPet(99));
    }

    @Test void getPetByName() {
        assertEquals(dog1, api.getPet("Buddy"));
        assertEquals(dog1, api.getPet("BUDDY")); // case insensitive
        assertNull(api.getPet("NoSuchPet"));
    }

    @Test void getPetById() {
        assertEquals(dog1, api.getPetById(1000));
        assertNull(api.getPetById(9999));
    }

    @Test void updatePet() {
        Dog updated = new Dog("Buddy2", 4, owner1, 1000, 'M', true, 25.0, true, "Labrador Retriever", false);
        assertNotNull(api.updatePet(0, updated));
        assertEquals("Buddy2", api.getPet(0).getName());
        assertNull(api.updatePet(-1, updated));
    }

    // --- Counts ---

    @Test void numberOfPets()          { assertEquals(7, api.numberOfPets()); assertEquals(0, emptyApi.numberOfPets()); }
    @Test void numberOfDogs()          { assertEquals(3, api.numberOfDogs()); }
    @Test void numberOfCats()          { assertEquals(2, api.numberOfCats()); }
    @Test void numberOfParrots()       { assertEquals(2, api.numberOfParrots()); }
    @Test void numberOfDangerousDogs() { assertEquals(1, api.numberOfDangerousDogs()); }
    @Test void numberOfIndoorCats()    { assertEquals(1, api.numberOfIndoorCats()); }

    @Test void numberOfParrotsByVocab() {
        assertEquals(1, api.numberOfParrotsByVocabularySize(5));   // Basic
        assertEquals(1, api.numberOfParrotsByVocabularySize(300)); // Amazing
        assertEquals(0, api.numberOfParrotsByVocabularySize(-1));  // invalid
    }

    // --- Listing ---

    @Test void listAllPets() {
        assertTrue(api.listAllPets().contains("Buddy"));
        assertEquals("No Pets", emptyApi.listAllPets());
    }

    @Test void listAllDogs() {
        assertTrue(api.listAllDogs().contains("Buddy"));
        assertFalse(api.listAllDogs().contains("Whiskers")); // cat not in list
        assertEquals("No Dogs", emptyApi.listAllDogs());
    }

    @Test void listAllCats() {
        assertTrue(api.listAllCats().contains("Whiskers"));
        assertFalse(api.listAllCats().contains("Buddy"));
        assertEquals("No Cats", emptyApi.listAllCats());
    }

    @Test void listAllParrots() {
        assertTrue(api.listAllParrots().contains("Polly"));
        assertEquals("No Parrots", emptyApi.listAllParrots());
    }

    @Test void listAllDangerousDogs() {
        assertTrue(api.listAllDangerousDogs().contains("Spike"));
        assertFalse(api.listAllDangerousDogs().contains("Buddy"));
        assertEquals("No Dangerous Dogs in the Kennels", emptyApi.listAllDangerousDogs());
    }

    @Test void listPetsByOwner() {
        assertTrue(api.listAllPetsByOwner("Alice").contains("Buddy"));
        assertTrue(api.listAllPetsByOwner("Unknown").contains("No Pet with owner"));
    }

    @Test void listPetsByDays() {
        assertTrue(api.listAllPetsThatStayMoreThanDays(2).contains("Whiskers")); // 3 days > 2
        assertTrue(api.listAllPetsThatStayMoreThanDays(10).contains("No Pet stays longer than"));
    }

    // --- Validation ---

    @Test void isValidPetIndex() {
        assertTrue(api.isValidPetIndex(0));
        assertTrue(api.isValidPetIndex(6));
        assertFalse(api.isValidPetIndex(-1));
        assertFalse(api.isValidPetIndex(7));
        assertFalse(emptyApi.isValidPetIndex(0));
    }

    // --- Calculations ---

    @Test void getWeeklyIncome() {
        // dog1: 2*30=60, dog2: 1*30=30, indoorCat: 3*25=75, parrot1: 1*10=10 -> total=175
        assertEquals(175.0, api.getWeeklyIncome());
        assertEquals(0.0, emptyApi.getWeeklyIncome());
    }

    @Test void getAverageNumDaysPerWeek() {
        // total days: 2+1+0+3+0+1+0 = 7, pets = 7 -> avg = 1.0
        assertEquals(1.0, api.getAverageNumDaysPerWeek());
        assertEquals(0.0, emptyApi.getAverageNumDaysPerWeek());
    }

    @Test void findDogByOwnerBreedAge() {
        assertEquals(dog1, api.findDogByOwnerAndBreedAndAge("Alice", "Labrador Retriever", 3));
        assertNull(api.findDogByOwnerAndBreedAndAge("Nobody", "Poodle", 99));
    }

    @Test void getPetsByOwnersName() {
        assertTrue(api.getPetsByOwnersName("Alice").contains("Buddy"));
        assertTrue(api.getPetsByOwnersName("Unknown").contains("No Pets for"));
    }

    // --- Sorting ---

    @Test void sortPetsById() {
        api.sortPetsById();
        assertEquals(1006, api.getPet(0).getId());                             // highest first
        assertEquals(1000, api.getPet(api.numberOfPets() - 1).getId());        // lowest last
        assertEquals(7, api.numberOfPets());                                    // size unchanged
    }

    @Test void sortPetsByName() {
        api.sortPetsByName();
        assertEquals("Buddy", api.getPet(0).getName()); // B comes first alphabetically
        assertEquals(7, api.numberOfPets());
    }
}
