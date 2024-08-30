package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import static gitlet.Repository.BLOBS_DIR;
import static gitlet.Repository.CWD;
import static gitlet.Utils.*;

public class Myfile {

    String filename;
    File FILENAME_DIR;
    HashSet<String> version;
    String latestVersion;;

    Myfile(String filename) {
        if (!join(CWD, filename).exists()) {
            throw new GitletException("File does not exist.");
        }
        FILENAME_DIR = join(BLOBS_DIR, filename);
        FILENAME_DIR.mkdir();
        this.filename = filename;
    }

    public void stage() {
        String contents = readContentsAsString(join(CWD, filename));
        String hashContents = sha1(contents);
        if (latestVersion == hashContents) {
            return;
        }
        latestVersion = hashContents;
        if (!version.contains(latestVersion)) {
        version.add(latestVersion);
            try {
                join(FILENAME_DIR, latestVersion).createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
