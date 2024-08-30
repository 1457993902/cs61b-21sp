package gitlet;

import java.io.File;
import java.io.IOException;

import static gitlet.Commit.currCommit;
import static gitlet.CommitPointer.readHead;
import static gitlet.CommitPointer.readMaster;
import static gitlet.Status.readStatus;
import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File HEAD = join(GITLET_DIR, "head");
    public static final File MARSTER = join(GITLET_DIR, "master");
    public static final File BLOBS_DIR = join(GITLET_DIR, "blobs");
    public static final File COMMIT_DIR = join(GITLET_DIR, "commits");
    public static final File STATUS = join(GITLET_DIR, "status");

    public static void init() {
        if(GITLET_DIR.exists()){
            throw new GitletException("A Gitlet version-control system already exists in the current directory.");
        }
        GITLET_DIR.mkdir();
        BLOBS_DIR.mkdir();
        try {
            STATUS.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new Status();
        Commit commit = new Commit();
        new CommitPointer(HEAD, commit);
        new CommitPointer(MARSTER, commit);
    }

    /**
     *receive a String, add
     * @param filename
     */
    public static void add(String filename) {
        Status status = readStatus();
        Myfile addedFile = new Myfile(filename);
        if (status.removal != null && status.removal.contains(addedFile)) {
            status.removal.remove(addedFile);
        }
        addedFile.stage(status);
        status.saveStatus();
    }

    /**
     *
     * @param message
     */
    public static void commit(String message) {
        Commit newcommit =currCommit();
        Status status = readStatus();
        newcommit.update(status, message);
        readHead().MovePointer(newcommit);
        readMaster().MovePointer(newcommit);
        writeObject(STATUS, new Status());
    }

    /**
     *
     * @param filename
     */
    public static void remove(String filename) {
        Myfile removedFile = new Myfile(filename);
        Status status = readStatus();
        if (status.staging.contains(removedFile)) {
            status.staging.remove(removedFile);
        }
        if (currCommit().files().contains(removedFile)) {
            removedFile.remove(status);
            restrictedDelete(filename);
        }

        status.saveStatus();
    }

    public static void log() {
    }
    
    public static void global_log() {
        
    }

    public static void find(String arg) {
    }

    public static void status() {
    }

    public static void checkoutLatest(String arg) {
    }

    public static void createBranch(String arg) {
    }

    public static void removeBranch(String arg) {
    }

    public static void reset(String arg) {
    }

    public static void merge(String arg) {
    }
}
