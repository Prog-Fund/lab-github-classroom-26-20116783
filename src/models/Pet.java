package models;

import utils.Utilities;

public abstract class Pet {

    private static int nextId = 1000;

    private int id;
    private String name = "";
    private int age = 0;
    private Owner owner = null;
    private boolean[] daysAttending = new boolean[7]; // Mon=0 ... Sat=5

    public Pet(String name, int age, Owner owner, int id) {
        this.name  = Utilities.truncateString(name, 30);
        if (age >= 0) this.age = age;
        this.owner = owner;
        this.id    = id;
    }

    // --- Static ID helpers ---
    public static int generateNextId() { return nextId++; }
    public static int getNextId()      { return nextId; }
    public static void setNextId(int id) { nextId = id; }

    // --- Getters ---
    public int     getId()            { return id; }
    public String  getName()          { return name; }
    public int     getAge()           { return age; }
    public Owner   getOwner()         { return owner; }
    public boolean[] getDaysAttending() { return daysAttending; }

    // --- Setters ---
    public void setId(int id)           { if (id >= 1000) this.id = id; }
    public void initName(String name)   { this.name = Utilities.truncateString(name, 30); }
    public void setName(String name)    { if (Utilities.validateStringLength(name, 30)) this.name = name; }
    public void setAge(int age)         { if (age >= 0) this.age = age; }
    public void setOwner(Owner owner)   { if (owner != null) this.owner = owner; }
    public void setDaysAttending(boolean[] days) { this.daysAttending = days; }

    // --- Attendance ---
    public void checkIn(int day)  { if (day >= 0 && day <= 5) daysAttending[day] = true; }
    public void checkOut(int day) { if (day >= 0 && day <= 5) daysAttending[day] = false; }

    public int numOfDaysAttending() {
        int count = 0;
        for (boolean day : daysAttending) if (day) count++;
        return count;
    }

    public abstract double calculateWeeklyFee();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pet)) return false;
        Pet pet = (Pet) o;
        return id == pet.id && age == pet.age && name.equals(pet.name);
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Age: " + age
                + " | Owner: " + (owner != null ? owner.getOwnerName() : "None")
                + " | Days: " + numOfDaysAttending();
    }
}
