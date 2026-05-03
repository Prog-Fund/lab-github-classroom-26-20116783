package models;

import utils.Utilities;

public class Owner {

    private String ownerName = "";   // max 20 chars
    private String phoneNumber = ""; // max 15 chars

    public Owner(String ownerName, String phoneNumber) {
        this.ownerName   = Utilities.truncateString(ownerName, 20);
        this.phoneNumber = Utilities.truncateString(phoneNumber, 15);
    }

    public String getOwnerName()   { return ownerName; }
    public String getPhoneNumber() { return phoneNumber; }

    public void setOwnerName(String ownerName) {
        if (Utilities.validateStringLength(ownerName, 20)) this.ownerName = ownerName;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (Utilities.validateStringLength(phoneNumber, 15)) this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Owner)) return false;
        Owner owner = (Owner) o;
        return ownerName.equals(owner.ownerName) && phoneNumber.equals(owner.phoneNumber);
    }

    @Override
    public String toString() {
        return "Owner: " + ownerName + " | Phone: " + phoneNumber;
    }
}
