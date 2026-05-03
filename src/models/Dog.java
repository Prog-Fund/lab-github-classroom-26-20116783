package models;

import utils.DogBreedUtility;

public class Dog extends Mammal {

    public static final float DANGEROUS_DAILY_RATE    = 40;
    public static final float NONDANGEROUS_DAILY_RATE = 30;

    private String  breed          = "";
    private boolean dangerousBreed = false;

    public Dog(String name, int age, Owner owner, int id,
               char sex, boolean vaccinated, double weight, boolean neutered,
               String breed, boolean dangerousBreed) {
        super(name, age, owner, id, sex, vaccinated, weight, neutered);
        if (DogBreedUtility.isValidBreed(breed)) this.breed = breed;
        this.dangerousBreed = dangerousBreed;
    }

    public String  getBreed()          { return breed; }
    public boolean isDangerousBreed()  { return dangerousBreed; }

    public void setBreed(String breed)              { if (DogBreedUtility.isValidBreed(breed)) this.breed = breed; }
    public void setDangerousBreed(boolean dangerous) { this.dangerousBreed = dangerous; }

    @Override
    public double calculateWeeklyFee() {
        return (dangerousBreed ? DANGEROUS_DAILY_RATE : NONDANGEROUS_DAILY_RATE) * numOfDaysAttending();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dog)) return false;
        Dog d = (Dog) o;
        return dangerousBreed == d.dangerousBreed && breed.equals(d.breed) && super.equals(o);
    }

    @Override
    public String toString() {
        return super.toString() + " | Breed: " + breed + " | Dangerous: " + dangerousBreed;
    }
}
