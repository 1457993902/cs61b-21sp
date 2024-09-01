package gitlet;

import java.io.File;
import java.io.Serializable;


public class Myfile implements Serializable {

    private File filename;
    private File Version;


    Myfile(File filename, File version) {
        this.filename = filename;
        Version = null;
        update(version);
    }

    public void update(File newversion) {
        Version = newversion;
    }

    public File getname() {
        return filename;
    }


    public File Version() {
        return Version;
    }
}
