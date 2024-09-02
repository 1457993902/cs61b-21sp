package gitlet;

import java.io.File;
import java.io.Serializable;


public class Myfile implements Serializable {

    private File filename;
    private File version;


    Myfile(File filename, File version) {
        this.filename = filename;
        this.version = null;
        update(version);
    }

    public void update(File newversion) {
        version = newversion;
    }

    public File getname() {
        return filename;
    }


    public File Version() {
        return version;
    }
}
