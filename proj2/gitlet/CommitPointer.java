package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Repository.*;
import static gitlet.Utils.*;


public class CommitPointer implements Serializable {

    private File commit;
    private File masterOrHead;
    /**
     *
     * @param file head or master
     * @param commit
     */
     CommitPointer(File file, Commit commit) {
         masterOrHead = file;
         MovePointer(commit);
     }

    /**
     * update the CommitPointer pointer when commit check ...etc
     */
    public void MovePointer(Commit commit) {
        this.commit = join(COMMIT_DIR, sha1(commit));
        writeObject(masterOrHead, this);
        savePointer();
    }

    public File currPoint() {
        return commit;
    }

    public static CommitPointer readHead() {
        return readObject(HEAD, CommitPointer.class);
    }

    public static CommitPointer readMaster() {
        return readObject(MARSTER, CommitPointer.class);
    }

    public void savePointer() {
        writeObject(masterOrHead, this);
    }
}
