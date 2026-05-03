package controllers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import models.Owner;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class OwnerAPI {

    private ArrayList<Owner> owners = new ArrayList<>();
    private File file;

    public OwnerAPI(File file) { this.file = file; }

    public boolean addOwner(Owner owner)    { return owners.add(owner); }
    public int numberOfOwners()             { return owners.size(); }
    public boolean isValidIndex(int index)  { return index >= 0 && index < owners.size(); }

    public Owner deleteOwner(int index) {
        return isValidIndex(index) ? owners.remove(index) : null;
    }

    public Owner getOwner(int index) {
        return isValidIndex(index) ? owners.get(index) : null;
    }

    public Owner getOwnerByName(String name) {
        for (Owner o : owners)
            if (o.getOwnerName().equalsIgnoreCase(name)) return o;
        return null;
    }

    public Owner updateOwner(int index, Owner updated) {
        if (isValidIndex(index)) { owners.set(index, updated); return updated; }
        return null;
    }

    public String listOwners() {
        if (owners.isEmpty()) return "No Owners";
        String str = "";
        for (int i = 0; i < owners.size(); i++)
            str += i + ": " + owners.get(i) + "\n";
        return str;
    }

    public ArrayList<Owner> getOwners() { return owners; }

    public void save() throws Exception {
        XStream xstream = new XStream(new DomDriver());
        ObjectOutputStream out = xstream.createObjectOutputStream(new FileWriter(file));
        out.writeObject(owners);
        out.close();
    }

    @SuppressWarnings("unchecked")
    public void load() throws Exception {
        XStream xstream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(new Class[]{Owner.class});
        ObjectInputStream in = xstream.createObjectInputStream(new FileReader(file));
        owners = (ArrayList<Owner>) in.readObject();
        in.close();
    }

    public String fileName() { return file.getName(); }
}
