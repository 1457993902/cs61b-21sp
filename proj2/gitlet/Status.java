package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;

import static gitlet.Repository.STATUS;
import static gitlet.Utils.readObject;
import static gitlet.Utils.writeObject;

public class Status implements Serializable {
    public ArrayList<File> Branches;
    public ArrayList<Myfile> staging;
    public ArrayList<Myfile> removal;

    public Status() {
        staging = new ArrayList<>();
        removal = new ArrayList<>();
        Branches = new ArrayList<>();
        saveStatus();
    }

    public void staged(Myfile filename) {
        staging.add(filename);
    }

    public void remove(Myfile filename) {
        removal.add(filename);
    }

    public void saveStatus() {
        writeObject(STATUS, this);
    }

    public static Status readStatus() {
        return readObject(STATUS, Status.class);
    }
}
