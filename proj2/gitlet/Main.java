package gitlet;

import java.io.File;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author mu9
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        if (!firstArg.equals("init") && !(new File(".gitlet").exists())) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        switch(firstArg) {
            case "init":
                if (args.length > 1) {
                    opError();
                }
                Repository.init();
                break;
            case "add":
                if (args.length > 2) {
                    opError();
                }
                Repository.add(args[1]);
                break;
            case "commit":
                if (args.length < 2) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
                Repository.commit(args[1]);
                break;
            case "rm":
                if (args.length > 2) {
                    opError();
                }
                Repository.remove(args[1]);
                break;
            case "log":
                if (args.length > 1) {
                    opError();
                }
                Repository.log();
                break;
            case "global-log":
                if (args.length > 1) {
                    opError();
                }
                Repository.globaLlog();
                break;
            case "find":
                if (args.length > 2) {
                    opError();
                }
                Repository.find(args[1]);
                break;
            case "status":
                if (args.length > 1) {
                    opError();
                }
                Repository.status();
                break;
            case "checkout":
                switch(args.length) {
                    case 3:
                        if (!args[1].equals("--")) {
                            opError();
                        }
                        Repository.checkoutLatest(args[2]);
                        break;
                    case 4:
                        if (!args[2].equals("--")) {
                            opError();
                        }
                        Repository.checkout(args[1],args[3]);
                        break;
                    case 2:
                        Repository.checkoutBranch(args[1]);
                        break;
                    default:
                        opError();
                }
                break;
            case "branch":
                if (args.length > 2) {
                    opError();
                }
                Repository.createBranch(args[1]);
                break;
            case "rm-branch":
                if (args.length > 2) {
                    opError();
                }
                Repository.removeBranch(args[1]);
                break;
            case "reset":
                if (args.length > 2) {
                    opError();
                }
                Repository.reset(args[1]);
                break;
            case "merge":
                if (args.length > 2) {
                    opError();
                }
                Repository.merge(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
            // TODO: FILL THE REST IN
        }
    }

    public static void opError() {
        System.out.println("Incorrect operands.");
        System.exit(0);
    }
}
