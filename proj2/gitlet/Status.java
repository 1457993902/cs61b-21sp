package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import static gitlet.Commit.currCommit;
import static gitlet.CommitPointer.readBranch;
import static gitlet.CommitPointer.saveBranch;
import static gitlet.Repository.*;
import static gitlet.Utils.*;

public class Status implements Serializable {
    private ArrayList<File> branches;
    private ArrayList<File> staging;
    private ArrayList<File> removal;
    private File branch;

    public Status() {
        try {
            STATUS.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        staging = new ArrayList<>();
        removal = new ArrayList<>();
        branches = new ArrayList<>();
        branches.add(MARSTER);
        branch = MARSTER;
        saveStatus();
    }

    public void stage(File filename) {
        if (!filename.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        String contents = readContentsAsString(filename);
        String hashContents = sha1(contents);
        File newVersion = join(STAGE_DIR, filename.getPath());
        HashMap<File, Myfile> files = currCommit().files();
        if (files.containsKey(filename)
                && files.get(filename).Version().getName().equals(hashContents)) {
            removal.remove(filename);
            saveStatus();
        } else {
            try {
                newVersion.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            writeContents(newVersion, contents);
            if (!staging.contains(filename)) {
                staging.add(filename);
            }
            saveStatus();
        }
    }


    public void remove(File filename) {
        if (staging.contains(filename)) {
            staging.remove(filename);
            join(STAGE_DIR, filename.getPath()).delete();
            saveStatus();
        } else if (currCommit().files().containsKey(filename)) {
            if (!removal.contains(filename)) {
                removal.add(filename);
            }
            join(CWD, filename.getPath()).delete();
            saveStatus();
        } else {
            System.out.println("No reason to remove the file");
            System.exit(0);
        }
    }

    public void clear() {
        for (String remove: plainFilenamesIn(STAGE_DIR)) {
            join(STAGE_DIR, remove).delete();
        }
        staging = new ArrayList<>();
        removal = new ArrayList<>();
        saveStatus();
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

    public ArrayList<File> getBranches() {
        return branches;
    }

    public File getBranch() {
        return branch;
    }

    public void createBranch(String branchname) {
        File branchName = join(GITLET_DIR, branchname);
        if (branches.contains(branchName)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        try {
            branchName.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        saveBranch(branchName, currCommit());
        branches.add(branchName);
        saveStatus();
    }

    public void removeBranch(String branchname) {
        File branchName = join(GITLET_DIR, branchname);
        if (!branchName.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        } else if (branchName.equals(branch)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        branchName.delete();
        branches.remove(branchName);
        saveStatus();
    }

    public void checkout(File commitName) {
        if (!untrackedFile().isEmpty()) {
            System.out.println("There is an untracked file in the way; delete it,"
                    + " or add and commit it first.");
            System.exit(0);
        }
        Boolean find = true;
        for (String file: plainFilenamesIn(COMMIT_DIR)) {
            if (file.equals(commitName.getName()) || file.contains(commitName.getName())) {
                commitName = join(COMMIT_DIR, file);
                find = true;
                break;
            }
        }
        if (!find) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit commit = readObject(join(COMMIT_DIR, commitName.getName()), Commit.class);
        for (String all: plainFilenamesIn(CWD)) {
            new File(all).delete();
        }
        for (File file: commit.files().keySet()) {
            File fileBlob = commit.files().get(file).Version();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            writeContents(file, readContents(fileBlob));
        }
        if (!commit.getBranch().equals(branch)) {
            clear();
            branch = commit.getBranch();
        }
        clear();
        saveStatus();
    }

    public void switchBranch(File branchName) {
        if (branchName.equals(branch)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        } else if (!branches.contains(branchName)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        checkout(readBranch(branchName).currPoint());
        writeObject(HEAD, readBranch(branchName));
        branch = branchName;
        saveStatus();
    }

    public void sort() {
        branches.sort(new CompareFile());
        staging.sort(new CompareFile());
        removal.sort(new CompareFile());
    }

    private static class CompareFile implements Comparator<File> {
        @Override
        public int compare(File o1, File o2) {
            if (o1.getName().compareTo(o2.getName()) < 0) {
                return -1;
            } else if (o1.getName().compareTo(o2.getName()) > 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }

}
