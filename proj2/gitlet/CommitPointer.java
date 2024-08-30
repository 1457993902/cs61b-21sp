package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import static gitlet.Utils.*;


public class CommitPointer implements Serializable {

    private String commit;


     CommitPointer(File file, Commit commit) {
        MovePointer(commit);
        writeObject(file, this);
     }

    /**
     * update the CommitPointer pointer when commit check ...etc
     */
    public void MovePointer(Commit commit) {
        this.commit=sha1(commit);
    }

    public String currPoint() {
        return commit;
    }
}
