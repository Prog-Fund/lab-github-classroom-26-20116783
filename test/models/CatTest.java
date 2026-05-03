package models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CatTest {

    private Owner owner;
    private Cat indoorCat, outdoorCat, invalidCat;

    @BeforeEach
    void setUp() {
        owner      = new Owner("Alice", "0871234567");
        indoorCat  = new Cat("Whiskers", 3, owner, 1000, 'F', true, 4.5, true, true, "FEATHER WAND");
        outdoorCat = new Cat("Mittens", 5, owner, 1001, 'M', false, 6.0, false, false, "BALL");
        invalidCat = new Cat("Felix", -1, owner, 1002, 'X', true, 1.0, false, true, "bad toy"); // invalid age, sex, weight, toy
    }

    @AfterEach
    void tearDown() { owner = null; indoorCat = outdoorCat = invalidCat = null; }

    // Constructor - valid values set correctly
    @Test void validConstructor() {
        assertEquals("Whiskers", indoorCat.getName());
        assertEquals(3, indoorCat.getAge());
        assertEquals('F', indoorCat.getSex());
        assertTrue(indoorCat.isVaccinated());
        assertEquals(4.5, indoorCat.getWeight());
        assertTrue(indoorCat.isNeutered());
        assertTrue(indoorCat.isIndoorCat());
        assertEquals("FEATHER WAND", indoorCat.getFavouriteToy());
    }

    // Constructor - invalid values use defaults
    @Test void invalidConstructorDefaults() {
        assertEquals(0, invalidCat.getAge());        // -1 invalid -> 0
        assertEquals('M', invalidCat.getSex());      // 'X' invalid -> 'M'
        assertEquals(2.0, invalidCat.getWeight());   // 1.0 below min -> 2
        assertEquals("not known", invalidCat.getFavouriteToy()); // bad toy -> "not known"
    }

    // Name truncated to 30 chars
    @Test void nameTruncated() {
        Cat c = new Cat("ABCDEFGHIJKLMNOPQRSTUVWXYZ12345", 1, owner, 1010, 'M', false, 3.0, false, false, "BALL");
        assertEquals(30, c.getName().length());
    }

    // Setters - valid updates accepted
    @Test void settersValidValues() {
        indoorCat.setName("Luna");       assertEquals("Luna", indoorCat.getName());
        indoorCat.setAge(6);             assertEquals(6, indoorCat.getAge());
        indoorCat.setSex('U');           assertEquals('U', indoorCat.getSex());
        indoorCat.setWeight(10.0);       assertEquals(10.0, indoorCat.getWeight());
        indoorCat.setIndoorCat(false);   assertFalse(indoorCat.isIndoorCat());
        indoorCat.setFavouriteToy("BALL"); assertEquals("BALL", indoorCat.getFavouriteToy());
        indoorCat.setVaccinated(false);  assertFalse(indoorCat.isVaccinated());
        indoorCat.setNeutered(false);    assertFalse(indoorCat.isNeutered());
    }

    // Setters - invalid values ignored (no update)
    @Test void settersInvalidValuesIgnored() {
        indoorCat.setAge(-1);            assertEquals(3, indoorCat.getAge());
        indoorCat.setSex('Z');           assertEquals('F', indoorCat.getSex());
        indoorCat.setWeight(1.0);        assertEquals(4.5, indoorCat.getWeight());   // below min
        indoorCat.setWeight(201.0);      assertEquals(4.5, indoorCat.getWeight());   // above max
        indoorCat.setFavouriteToy("xyz"); assertEquals("FEATHER WAND", indoorCat.getFavouriteToy());
        indoorCat.setName("A".repeat(31)); assertEquals("Whiskers", indoorCat.getName()); // too long
    }

    // Weight boundary values
    @Test void weightBoundaries() {
        indoorCat.setWeight(2.0);   assertEquals(2.0, indoorCat.getWeight());   // min valid
        indoorCat.setWeight(200.0); assertEquals(200.0, indoorCat.getWeight()); // max valid
        indoorCat.setWeight(1.9);   assertEquals(200.0, indoorCat.getWeight()); // just below min, no change
    }

    // Weekly fee - indoor: 25/day, outdoor: 20/day
    @Test void calculateWeeklyFeeIndoor() {
        indoorCat.checkIn(0); indoorCat.checkIn(1); // 2 days
        assertEquals(50.0, indoorCat.calculateWeeklyFee());
    }

    @Test void calculateWeeklyFeeOutdoor() {
        outdoorCat.checkIn(0); outdoorCat.checkIn(1); outdoorCat.checkIn(2); // 3 days
        assertEquals(60.0, outdoorCat.calculateWeeklyFee());
    }

    @Test void calculateWeeklyFeeZeroDays() {
        assertEquals(0.0, indoorCat.calculateWeeklyFee());
    }

    // Attendance
    @Test void checkInAndOut() {
        assertEquals(0, indoorCat.numOfDaysAttending());
        indoorCat.checkIn(0); indoorCat.checkIn(2);
        assertEquals(2, indoorCat.numOfDaysAttending());
        indoorCat.checkOut(0);
        assertEquals(1, indoorCat.numOfDaysAttending());
    }

    @Test void invalidDayIgnored() {
        indoorCat.checkIn(-1); indoorCat.checkIn(6);
        assertEquals(0, indoorCat.numOfDaysAttending());
    }

    // toString contains key info
    @Test void toStringContainsName() {
        assertTrue(indoorCat.toString().contains("Whiskers"));
        assertTrue(indoorCat.toString().contains("FEATHER WAND"));
    }
}
