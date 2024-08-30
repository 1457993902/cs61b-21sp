package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.TreeMap;

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
    private TreeMap<String, Myfile> files;
    private String parent;

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
        files = new TreeMap<>();


        CommitPointer head = new CommitPointer(HEAD, this);
        CommitPointer master = new CommitPointer(MARSTER, this);
        saveCommit();
    }

    Commit(String message) {
        parent = readObject(HEAD, CommitPointer.class).currPoint();
        this.message = message;
        files = new TreeMap<>();

        CommitPointer head = new CommitPointer(HEAD, this);
        saveCommit();
    }

    private void saveCommit() {
        File commit = join(COMMIT_DIR, sha1(this));
        writeObject(commit, this);
    }

    public void update(Status status) {

    }
    /* TODO: fill in the rest of this class. */
}
