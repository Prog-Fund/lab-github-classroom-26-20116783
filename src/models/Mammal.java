package models;

import utils.Utilities;

public abstract class Mammal extends Pet {

    private char    sex        = 'M';   // M, F, or U
    private boolean vaccinated = false;
    private boolean neutered   = false;
    private double  weight     = 2;     // 2 to 200 kg

    public Mammal(String name, int age, Owner owner, int id,
                  char sex, boolean vaccinated, double weight, boolean neutered) {
        super(name, age, owner, id);
        setSex(sex);
        this.vaccinated = vaccinated;
        if (Utilities.validRange(weight, 2, 200)) this.weight = weight;
        this.neutered = neutered;
    }

    public char    getSex()         { return sex; }
    public boolean isVaccinated()   { return vaccinated; }
    public boolean isNeutered()     { return neutered; }
    public double  getWeight()      { return weight; }

    public void setSex(char sex)            { if (sex == 'M' || sex == 'F' || sex == 'U') this.sex = sex; }
    public void setVaccinated(boolean v)    { this.vaccinated = v; }
    public void setNeutered(boolean n)      { this.neutered = n; }
    public void setWeight(double weight)    { if (Utilities.validRange(weight, 2, 200)) this.weight = weight; }

    @Override
    public String toString() {
        return super.toString() + " | Sex: " + sex + " | Vaccinated: " + vaccinated
                + " | Neutered: " + neutered + " | Weight: " + weight + "kg";
    }
}
