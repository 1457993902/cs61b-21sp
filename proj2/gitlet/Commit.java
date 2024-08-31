package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;
import java.util.HashSet;

import static gitlet.CommitPointer.*;
import static gitlet.Repository.*;
import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private Date timestamp;
    private HashMap<File, Myfile> files;
    private File parent;

    Commit() {
        COMMIT_DIR.mkdir();
        this.message = "initial commit";
        parent = null;
        try {
            MARSTER.createNewFile();
            HEAD.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        timestamp = new Date(0);
        files = new HashMap<>();
        saveCommit();
    }

    public void update(Status status, String message) {
        parent = readHead().currPoint();
        for (File add: status.staging()) {
            String newcontents = readContentsAsString(join(STAGE_DIR, add.getPath()));
            File newversion = join(BLOBS_DIR, sha1(newcontents));
            if (!newversion.exists()) {
                try {
                    newversion.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                writeContents(newversion, newcontents);
            }
            if (!files.containsKey(add)) {
                files.put(add, new Myfile(add, newversion));
            } else {
                Myfile pre = files.get(add);
                pre.update(newversion);
            }
        }
        for (File remove: status.removal()) {
            files.remove(remove);
        }
        timestamp = new Date();
        this.message = message;
        saveCommit();
    }

    public static Commit currCommit() {
        return readObject(readHead().currPoint(), Commit.class);
    }

    public HashMap<File, Myfile> files() {
        return files;
    }

    private void saveCommit() {
        File commit = join(COMMIT_DIR, sha1(this));
        writeObject(commit, this);
    }
    /* TODO: fill in the rest of this class. */
}
