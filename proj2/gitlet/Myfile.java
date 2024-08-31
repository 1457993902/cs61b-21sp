package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

public class Myfile implements Serializable {

    private File filename;
    private HashSet<File> versions;
    private File latestVersion;


    Myfile(File filename, File version) {
        this.filename = filename;
        versions = new HashSet<>();
        latestVersion = null;
    }

    public void update(File newversion) {
        latestVersion = newversion;
        versions.add(newversion);
    }

    public File getname() {
        return filename;
    }

    public Boolean contains(File version) {
        return versions.contains(version);
    }

    public File latestVersion() {
        return latestVersion;
    }
}
