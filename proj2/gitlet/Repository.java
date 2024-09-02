package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static gitlet.Commit.currCommit;
import static gitlet.CommitPointer.*;
import static gitlet.Status.readStatus;
import static gitlet.Utils.*;


/** Represents a gitlet repository.
 *  does at a high level.
 *
 *  @author mu9
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
    public static final File STAGE_DIR = join(GITLET_DIR, "stage");
    public static final File STATUS = join(GITLET_DIR, "status");

    public static void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already" +
                    " exists in the current directory.");
            System.exit(0);
        }
        GITLET_DIR.mkdir();
        BLOBS_DIR.mkdir();
        STAGE_DIR.mkdir();
        Commit commit = new Commit();
        new CommitPointer(HEAD, commit);
        new CommitPointer(MARSTER, commit);
        new Status();
    }

    /**
     *
     * @param filename
     */
    public static void add(String filename) {
        Status status = readStatus();
        File addedFile = new File(filename);
        status.stage(addedFile);
    }

    /**
     *
     * @param message
     */
    public static void commit(String message) {
        Commit newcommit = currCommit();
        Status status = readStatus();
        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
        if (status.removal().isEmpty() && status.staging().isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        newcommit.update(status, message);
        status.clear();
    }

    public static void remove(String filename) {
        File removedFile = new File(filename);
        Status status = readStatus();
        status.remove(removedFile);
    }

    public static void log() {
        File commitName = readHead().currPoint();
        Commit commit;
        do {
            commit = readObject(commitName, Commit.class);
            System.out.println("===\ncommit " + commitName.getName());
            if (commit.parents().size() > 1) {
                System.out.println("Merge: " + commit.getParent(0).getName().substring(0,7)
                        + " " + commit.getParent(1).getName().substring(0,7));
            }
            System.out.println("Date: " + commit.getTime());
            System.out.println(commit.getMessage());
            System.out.println();
            commitName = commit.getParent(0);
        } while (commitName != null);
    }
    
    public static void globaLlog() {
        for (String commitName: plainFilenamesIn(COMMIT_DIR)) {
            Commit commit = readObject(join(COMMIT_DIR, commitName), Commit.class);
            System.out.println("===\ncommit " + commitName);
            System.out.println("Date: " + commit.getTime());
            System.out.println(commit.getMessage());
            System.out.println();
        }
    }

    public static void find(String message) {
        Boolean exist = false;
        for (String commitName: plainFilenamesIn(COMMIT_DIR)) {
            Commit commit = readObject(join(COMMIT_DIR, commitName), Commit.class);
            if (commit.getMessage().equals(message)) {
                System.out.println(commitName);
                exist = true;
            }
        }
        if (!exist) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }
    }

    public static void status() {
        Status status = readStatus();
        status.sort();
        System.out.println("=== Branches ===");
        for (File branch: status.getBranches()) {
            if (branch.equals(status.getBranch())) {
                System.out.print("*");
            }
            System.out.println(branch.getName());
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        for (File staged: status.staging()) {
            System.out.println(staged.getName());
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        for (File removed: status.removal()) {
            System.out.println(removed.getName());
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        //TreeSet<File> modified = modifiedFile();
        //for (File file: modified) {
        //    System.out.println(file.getName());
        //}
        System.out.println();
        System.out.println("=== Untracked Files ===");
        //TreeSet<File> untrack = untrackedFile();
        //for (File file: untrack) {
        //    System.out.println(file.getName());
        //}
        System.out.println();
    }

    public static TreeSet<File> modifiedFile() {
        Status status = readStatus();
        TreeSet<File> files = new TreeSet<>();
        Commit commit = currCommit();
        for (String filename: plainFilenamesIn(CWD)) {
            File fileName = new File(filename);
            String contentSHA = sha1(readContents(fileName));
            if (commit.files().containsKey(fileName)) {
                if (commit.files().get(fileName).Version().getName() != contentSHA
                        && !status.staging().contains(fileName)) {
                    files.add(fileName);
                }
                if (!status.removal().contains(fileName)
                        && !plainFilenamesIn(CWD).contains(filename)) {
                    files.add(fileName);//TODO (delete)
                }
            }
            if (status.staging().contains(fileName)) {
                if (!readContentsAsString(new File(STAGE_DIR, filename)).equals(contentSHA)) {
                    files.add(fileName);
                }
                if (!plainFilenamesIn(CWD).contains(filename)) {
                    files.add(fileName);//TODO (delete)
                }
            }
            return files;
        }
        return files;
    }

    public static TreeSet<File> untrackedFile() {
        Status status = readStatus();
        TreeSet<File> files = new TreeSet<>();
        Commit commit = currCommit();
        for (String filename: plainFilenamesIn(CWD)) {
            File fileName = new File(filename);
            if (status.removal().contains(fileName)
                    || !status.staging().contains(fileName) && !commit.files().containsKey(fileName)) {
                files.add(fileName);
            }
        }
        return files;
    }

    private static void checkout(File commitName, File fileName) {
        Boolean find = false;
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
        Commit commit = readObject(commitName, Commit.class);
        for (File file: commit.files().keySet()) {
            if (file.equals(fileName)) {
                String contents = readContentsAsString(commit.files().get(file).Version());
                writeContents(fileName, contents);
                return;
            }
        }
        System.out.println("File does not exist in that commit.");
        System.exit(0);
    }

    public static void checkout(String commitname, String filename) {
        File commitName = join(COMMIT_DIR, commitname);
        File fileName = new File(filename);
        checkout(commitName, fileName);
    }

    public static void checkoutLatest(String filename) {
        checkout(readHead().currPoint(), new File(filename));
    }

    //TODO 缩写
    public static void checkoutBranch(String branchname) {
        Status status = readStatus();
        File branchName = join(GITLET_DIR, branchname);
        status.switchBranch(branchName);
    }

    public static void createBranch(String branchname) {
        Status status = readStatus();
        status.createBranch(branchname);
    }

    public static void removeBranch(String branchname) {
        Status status = readStatus();
        status.removeBranch(branchname);
    }

    public static void reset(String commitname) {
        Status status = readStatus();
        File commitName = new File(commitname);
        if (!plainFilenamesIn(COMMIT_DIR).contains(commitName.getName())) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        status.checkout(commitName);
    }

    public static Commit getSpilt(File otherBranch, File branch, Status status) {
        Commit otherCommit = readObject(readObject(otherBranch, CommitPointer.class).currPoint(), Commit.class);
        Commit commit = readObject(readObject(branch, CommitPointer.class).currPoint(), Commit.class);
        Commit spilt1 = otherCommit, spilt2 = commit;
        for (int i = 0; i < otherCommit.parents().size(); i++) {
            spilt1 = otherCommit;
            while (!spilt1.getMessage().equals("initial commit")) {
                if (spilt1.getBranch().equals(branch)) {
                    break;
                }
                spilt1 = readObject(spilt1.getParent(i), Commit.class);
            }
            if (!spilt1.getMessage().equals("initial commit")) {
                break;
            }
        }
        for (int i = 0; i < commit.parents().size(); i++) {
            spilt2 = commit;
            while (!spilt2.getMessage().equals("initial commit")) {
                if (spilt2.getBranch().equals(otherBranch)) {
                    break;
                }
                spilt2 = readObject(spilt2.getParent(i), Commit.class);
            }
            if (!spilt2.getMessage().equals("initial commit")) {
                break;
            }
        }
        Commit spilt = spilt1.getMessage().equals("initial commit") ? spilt2 : spilt1;
        if (spilt.equals(otherCommit)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }
        if (spilt.equals(commit)) {
            status.switchBranch(otherBranch);
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }
        return spilt;
    }

    public static void checkMerge(Status status, File branchName, Commit commit) {
        if (!status.removal().isEmpty() || !status.staging().isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        if (!status.getBranches().contains(branchName)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (status.getBranch().equals(branchName)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
        if (!untrackedFile().isEmpty()) {
            System.out.println("There is an untracked file in the way; delete it,"
                    + " or add and commit it first.");
            System.exit(0);
        }
    }

    public static void merge(String branchname) {
        Status status = readStatus();
        File branchName = join(GITLET_DIR, branchname);
        Commit commit = currCommit();
        checkMerge(status, branchName, commit);
        Commit otherCommit = readObject(readBranch(branchName).currPoint(), Commit.class);
        Commit spilt = getSpilt(branchName, status.getBranch(), status);
        HashMap<File, Myfile> thisFile = commit.files(), otherFile = otherCommit.files(), spiltFile = spilt.files();
        HashSet<File> allFile = new HashSet<>();
        allFile.addAll(thisFile.keySet());
        allFile.addAll(otherFile.keySet());
        allFile.addAll(spiltFile.keySet());
        Boolean conflict = false;
        for (File file: allFile) {
            if (thisFile.containsKey(file)) {
                File thisVersion = thisFile.get(file).Version();
                if (spiltFile.containsKey(file)) {
                    File spiltVersion = spiltFile.get(file).Version();
                    if (thisVersion.equals(spiltVersion)) {
                        if (otherFile.containsKey(file)) {
                            if (!otherFile.get(file).Version().equals(spiltVersion)) {
                                writeContents(join(CWD, file.getName()), readContentsAsString(otherFile.get(file).Version()));
                                status.stage(file);
                            }
                        } else {
                            join(CWD, file.getName()).delete();
                        }
                    } else {
                        if (otherFile.containsKey(file) && !otherFile.get(file).Version().equals(spiltVersion)) {
                            conflict = true;
                            String contents = "<<<<<<< HEAD\n";
                            contents = contents + readContentsAsString(thisVersion) + "=======\n"
                                    + readContentsAsString(otherFile.get(file).Version()) + ">>>>>>>\n";
                            writeContents(join(CWD, file.getName()), contents);
                            status.stage(file);
                        } else {
                            conflict = true;
                            String contents = "<<<<<<< HEAD\n";
                            contents = contents + readContentsAsString(thisVersion) + "=======\n" + ">>>>>>>\n";
                            writeContents(join(CWD, file.getName()), contents);
                            status.stage(file);
                        }
                    }
                } else {
                    if (otherFile.containsKey(file)) {
                        conflict = true;
                        String contents = "<<<<<<< HEAD\n";
                        contents = contents + readContentsAsString(thisVersion) + "=======\n"
                                + readContentsAsString(otherFile.get(file).Version()) + ">>>>>>>\n";
                        writeContents(join(CWD, file.getName()), contents);
                        status.stage(file);
                    }
                }
            } else {
                if (spiltFile.containsKey(file)) {
                    if (otherFile.containsKey(file)) {
                        if (!otherFile.get(file).Version().equals(spiltFile.get(file).Version())) {
                            conflict = true;
                            String contents = "<<<<<<< HEAD\n";
                            contents = contents + "=======\n" + readContentsAsString(otherFile.get(file).Version()) + ">>>>>>>\n";
                            writeContents(join(CWD, file.getName()), contents);
                            status.stage(file);
                        }
                    }
                } else {
                    if (otherFile.containsKey(file)) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        writeContents(file, readContentsAsString(otherFile.get(file).Version()));
                        status.stage(file);
                    }
                }
            }
        }
        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        }
        commit.update(commit.getBranch(), branchName, status);
        status.clear();
    }
}
