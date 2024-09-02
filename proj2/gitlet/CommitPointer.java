package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Repository.*;
import static gitlet.Utils.*;


public class CommitPointer implements Serializable {

    private File commit;

    CommitPointer(File file, Commit commit) {
        this.commit = join(COMMIT_DIR, sha1(commit));
        writeObject(file, this);
    }

    public File currPoint() {
        return commit;
    }

    public static CommitPointer readHead() {
        return readObject(HEAD, CommitPointer.class);
    }

    public static CommitPointer readBranch(File pointerName) {
        return readObject(pointerName, CommitPointer.class);
    }

    public static void saveHead(Commit commit) {
        writeObject(HEAD, new CommitPointer(HEAD, commit));
    }

    public static void saveBranch(File pointerName, Commit commit) {
        writeObject(pointerName, new CommitPointer(pointerName, commit));
        saveHead(commit);
    }

}
