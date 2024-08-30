package gitlet;

import java.io.File;
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
    public static final File STAGE_DIR = join(GITLET_DIR, "stage");
    public static final File COMMIT_DIR = join(GITLET_DIR, "commits");

    public static void init() {
        if(GITLET_DIR.exists()){
            throw new GitletException("A Gitlet version-control system already exists in the current directory.");
        }
        GITLET_DIR.mkdir();
        BLOBS_DIR.mkdir();
        STAGE_DIR.mkdir();
        Commit InitCommit = new Commit();
    }

    /**
     *
     * @param filename
     */
    public static void add(String filename) {
        Myfile addedFile = new Myfile(filename);
        addedFile.stage();

    }

    /**
     *
     * @param message
     */
    public static void commit(String message) {
        Commit newcommit = readObject(join(COMMIT_DIR, readObject(HEAD, CommitPointer.class).currPoint()), Commit.class);
        
    }

    /**
     *
     * @param filename
     */
    public static void remove(String filename) {

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
