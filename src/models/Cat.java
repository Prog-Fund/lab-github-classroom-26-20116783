package models;

import utils.CatToyUtility;

public class Cat extends Mammal {

    private boolean indoorCat    = true;
    private String  favouriteToy = "not known";

    public Cat(String name, int age, Owner owner, int id,
               char sex, boolean vaccinated, double weight, boolean neutered,
               boolean indoorCat, String favouriteToy) {
        super(name, age, owner, id, sex, vaccinated, weight, neutered);
        this.indoorCat = indoorCat;
        if (CatToyUtility.isValidCatToy(favouriteToy)) this.favouriteToy = favouriteToy.toUpperCase();
    }

    public boolean isIndoorCat()     { return indoorCat; }
    public String  getFavouriteToy() { return favouriteToy; }

    public void setIndoorCat(boolean indoorCat) { this.indoorCat = indoorCat; }

    public void setFavouriteToy(String toy) {
        if (CatToyUtility.isValidCatToy(toy)) this.favouriteToy = toy.toUpperCase();
    }

    @Override
    public double calculateWeeklyFee() {
        return (indoorCat ? 25 : 20) * numOfDaysAttending();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cat)) return false;
        Cat c = (Cat) o;
        return indoorCat == c.indoorCat && favouriteToy.equals(c.favouriteToy) && super.equals(o);
    }

    @Override
    public String toString() {
        return super.toString() + " | Indoor: " + indoorCat + " | Favourite Toy: " + favouriteToy;
    }
}
