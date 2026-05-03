package models;

import utils.Utilities;

public abstract class Bird extends Pet {

    private double  wingspan = 3;    // 3 to 400 cm
    private boolean canFly   = false;

    public Bird(String name, int age, Owner owner, int id, double wingSpan, boolean canFly) {
        super(name, age, owner, id);
        if (Utilities.validRange(wingSpan, 3, 400)) this.wingspan = wingSpan;
        this.canFly = canFly;
    }

    public double  getWingSpan() { return wingspan; }
    public boolean isCanFly()    { return canFly; }

    public void setWingSpan(double w) { if (Utilities.validRange(w, 3, 400)) this.wingspan = w; }
    public void setCanFly(boolean c)  { this.canFly = c; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bird)) return false;
        Bird b = (Bird) o;
        return Double.compare(b.wingspan, wingspan) == 0 && canFly == b.canFly && super.equals(o);
    }

    @Override
    public String toString() {
        return super.toString() + " | Wingspan: " + wingspan + "cm | Can Fly: " + canFly;
    }
}
