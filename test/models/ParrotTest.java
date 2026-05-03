package models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParrotTest {

    private Owner owner;
    private Parrot basic, intermediate, advanced, amazing, invalid;

    @BeforeEach
    void setUp() {
        owner        = new Owner("Mary", "0861234567");
        basic        = new Parrot("Polly",   2, owner, 1000, 25.0, true,  5);    // 5 words -> Basic
        intermediate = new Parrot("Charlie", 4, owner, 1001, 30.0, true,  25);   // 25 words -> Intermediate
        advanced     = new Parrot("Rio",     6, owner, 1002, 40.0, false, 100);  // 100 words -> Advanced
        amazing      = new Parrot("Einstein",8, owner, 1003, 50.0, true,  300);  // 300 words -> Amazing
        invalid      = new Parrot("X",      -1, owner, 1004, 1.0,  false, -1);   // invalid age, wingspan, vocab
    }

    @AfterEach
    void tearDown() { owner = null; basic = intermediate = advanced = amazing = invalid = null; }

    // Constructor - valid values
    @Test void validConstructor() {
        assertEquals("Polly", basic.getName());
        assertEquals(2, basic.getAge());
        assertEquals(25.0, basic.getWingSpan());
        assertTrue(basic.isCanFly());
        assertEquals("Basic", basic.getVocabularySize());
    }

    // Constructor - invalid values use defaults
    @Test void invalidConstructorDefaults() {
        assertEquals(0, invalid.getAge());              // -1 -> 0
        assertEquals(3.0, invalid.getWingSpan());       // 1.0 below min -> 3
        assertEquals("Amazing", invalid.getVocabularySize()); // -1 words -> stays "Amazing"
    }

    // Vocabulary size category boundaries
    @Test void vocabBoundaries() {
        assertEquals("Basic",        new Parrot("a", 1, owner, 1010, 20.0, true, 0).getVocabularySize());
        assertEquals("Basic",        new Parrot("a", 1, owner, 1011, 20.0, true, 10).getVocabularySize());
        assertEquals("Intermediate", new Parrot("a", 1, owner, 1012, 20.0, true, 11).getVocabularySize());
        assertEquals("Intermediate", new Parrot("a", 1, owner, 1013, 20.0, true, 50).getVocabularySize());
        assertEquals("Advanced",     new Parrot("a", 1, owner, 1014, 20.0, true, 51).getVocabularySize());
        assertEquals("Advanced",     new Parrot("a", 1, owner, 1015, 20.0, true, 200).getVocabularySize());
        assertEquals("Amazing",      new Parrot("a", 1, owner, 1016, 20.0, true, 201).getVocabularySize());
    }

    // All four categories
    @Test void allVocabCategories() {
        assertEquals("Basic",        basic.getVocabularySize());
        assertEquals("Intermediate", intermediate.getVocabularySize());
        assertEquals("Advanced",     advanced.getVocabularySize());
        assertEquals("Amazing",      amazing.getVocabularySize());
    }

    // Getters
    @Test void getters() {
        assertEquals(25.0, basic.getWingSpan());
        assertTrue(basic.isCanFly());
        assertFalse(advanced.isCanFly());
        assertEquals(2, basic.getAge());
        assertEquals(owner, basic.getOwner());
    }

    // Setters - valid
    @Test void settersValidValues() {
        basic.setVocabularySize(25);   assertEquals("Intermediate", basic.getVocabularySize());
        basic.setVocabularySize(100);  assertEquals("Advanced", basic.getVocabularySize());
        basic.setVocabularySize(201);  assertEquals("Amazing", basic.getVocabularySize());
        basic.setWingSpan(100.0);      assertEquals(100.0, basic.getWingSpan());
        basic.setCanFly(false);        assertFalse(basic.isCanFly());
        basic.setAge(5);               assertEquals(5, basic.getAge());
    }

    // Setters - invalid ignored
    @Test void settersInvalidIgnored() {
        basic.setVocabularySize(-1); assertEquals("Basic", basic.getVocabularySize()); // unchanged
        basic.setWingSpan(2.9);      assertEquals(25.0, basic.getWingSpan());  // below min
        basic.setWingSpan(400.1);    assertEquals(25.0, basic.getWingSpan());  // above max
        basic.setAge(-1);            assertEquals(2, basic.getAge());
    }

    // Wingspan boundaries
    @Test void wingSpanBoundaries() {
        basic.setWingSpan(3.0);   assertEquals(3.0, basic.getWingSpan());
        basic.setWingSpan(400.0); assertEquals(400.0, basic.getWingSpan());
    }

    // Fee calculation: €10 per day
    @Test void weeklyFee() {
        assertEquals(0.0, basic.calculateWeeklyFee());
        basic.checkIn(0); basic.checkIn(1); basic.checkIn(2);
        assertEquals(30.0, basic.calculateWeeklyFee());
    }

    @Test void feeSameRegardlessOfVocab() {
        basic.checkIn(0); amazing.checkIn(0);
        assertEquals(basic.calculateWeeklyFee(), amazing.calculateWeeklyFee());
    }

    // Attendance
    @Test void attendance() {
        basic.checkIn(0); basic.checkIn(3);
        assertEquals(2, basic.numOfDaysAttending());
        basic.checkOut(0);
        assertEquals(1, basic.numOfDaysAttending());
        basic.checkIn(-1); basic.checkIn(6); // invalid, no change
        assertEquals(1, basic.numOfDaysAttending());
    }

    // toString
    @Test void toStringContainsKeyFields() {
        assertTrue(basic.toString().contains("Polly"));
        assertTrue(basic.toString().contains("Basic"));
    }
}
