package gitlet;

// TODO: any imports you need here
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private ArrayList<File> parent;
    private File branch;

    Commit() {
        COMMIT_DIR.mkdir();
        this.message = "initial commit";
        parent = new ArrayList<>();
        try {
            MARSTER.createNewFile();
            HEAD.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        branch = MARSTER;
        timestamp = new Date(0);
        files = new HashMap<>();
        saveCommit();
    }

    Commit(File thisBranch, File otherBranch) {
        this.message = "Merged " + otherBranch.getName() + " into " + thisBranch.getName() +".";
        timestamp = new Date(0);
        files = new HashMap<>();
    }

    public void update(HashMap<File, Myfile> newfiles, File... parents) {
        files = newfiles;
        for (File p: parents) {
            this.parent.add(p);
        }
        saveCommit();
    }

    public void update(Status status, String message) {
        parent = new ArrayList<>();
        parent.add(readHead().currPoint());
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
        branch = status.getBranch();
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
        File commitName = join(COMMIT_DIR, sha1(this));
        try {
            commitName.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writeObject(commitName, this);
    }

    public String getTime() {
        SimpleDateFormat sf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy ", Locale.ENGLISH);
        return sf.format(timestamp) + "-0800";

    }

    public File getParent(int index) {
        if (parent.isEmpty()) {
            return null;
        }
        return parent.get(index);
    }

    public String getMessage() {
        return message;
    }

    public File getBranch() {
        return branch;
    }
    /* TODO: fill in the rest of this class. */
}
