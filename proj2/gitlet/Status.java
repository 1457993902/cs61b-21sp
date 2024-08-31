package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import static gitlet.Commit.currCommit;
import static gitlet.Repository.STAGE_DIR;
import static gitlet.Repository.STATUS;
import static gitlet.Utils.*;

public class Status implements Serializable {
    public ArrayList<File> Branches;
    private ArrayList<File> staging;
    private ArrayList<File> removal;

    public Status() {
        try {
            STATUS.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        staging = new ArrayList<>();
        removal = new ArrayList<>();
        Branches = new ArrayList<>();
        saveStatus();
    }

    public void stage(File filename) {
        String contents = readContentsAsString(filename);
        String hashContents = sha1(contents);
        File newVersion = join(STAGE_DIR, filename.getPath());
        HashMap<File, Myfile> files = currCommit().files();
        if (files.containsKey(filename) && files.get(filename).latestVersion().getName().equals(hashContents)) {
            remove(filename);
            saveStatus();
        } else {
            try {
                newVersion.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            writeContents(newVersion, contents);
            staging.add(filename);
            saveStatus();
        }
    }


    public void remove(File filename) {
        if (staging.contains(filename)) {
            staging.remove(filename);
            restrictedDelete(join(STAGE_DIR, filename.getPath()));
            saveStatus();
        } else if (currCommit().files().containsKey(filename)) {
            removal.add(filename);
            restrictedDelete(filename);
            saveStatus();
        } else {
            throw new GitletException("No reason to remove the file");
        }
    }

    public void clear() {
        for (String remove: plainFilenamesIn(STAGE_DIR)) {
            restrictedDelete(remove);
        }
        new Status().saveStatus();
    }

    public void saveStatus() {
        writeObject(STATUS, this);
    }

    public static Status readStatus() {
        return readObject(STATUS, Status.class);
    }

    public ArrayList<File> removal() {
        return removal;
    }

    public ArrayList<File> staging() {
        return staging;
    }
}
