package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

public class Myfile implements Serializable {

    private File filename;
    private File FILENAME_BLOB;
    private HashSet<File> versions;
    private File latestVersion;


    Myfile(String filename) {
        this.filename = join(CWD, new File(filename).getPath());
        if (!this.filename.exists()) {
            throw new GitletException("File does not exist.");
        }
        FILENAME_BLOB = join(BLOBS_DIR, filename);
        FILENAME_BLOB.mkdir();
        versions = new HashSet<>();
        latestVersion = null;
    }

    /**
     * called to switch Myfile to staged status, if there's no directory or file in blobs,
     * then create
     */
    public void stage(Status status) {
        String contents = readContentsAsString(filename);
        String hashContents = sha1(contents);
        File newVersion = join(FILENAME_BLOB, hashContents);
        if (newVersion.equals(latestVersion)) {
            return;
        }
        latestVersion = newVersion;
        if (!versions.contains(latestVersion)) {
            versions.add(latestVersion);
            try {
                newVersion.createNewFile();
                writeObject(newVersion, Myfile.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        status.staged(this);
    }

    public void remove(Status status) {
        status.remove(this);
    }


    public File getname() {
        return filename;
    }

    public Boolean sameName(Myfile otherfile) {
        return filename == otherfile.getname();
    }
}
