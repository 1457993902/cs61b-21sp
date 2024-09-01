package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                Repository.init();
                break;
            case "add":
                Repository.add(args[1]);
                break;
            case "commit":
                if (args.length < 2) {
                    throw new GitletException("Please enter a commit message.");
                }
                Repository.commit(args[1]);
                break;
            case "rm":
                Repository.remove(args[1]);
                break;
            case "log":
                Repository.log();
                break;
            case "global-log":
                Repository.global_log();
                break;
            case "find":
                Repository.find(args[1]);
                break;
            case "status":
                Repository.status();
                break;
            case "checkout":
                switch(args.length) {
                    case 3:
                        Repository.checkoutLatest(args[2]);
                        break;
                    case 4:
                        Repository.checkout(args[2],args[3]);
                        break;
                    case 2:
                        Repository.checkoutBranch(args[1]);
                        break;
                }
                break;
            case "branch":
                Repository.createBranch(args[1]);
                break;
            case "rm-branch":
                Repository.removeBranch(args[1]);
                break;
            case "reset":
                Repository.reset(args[1]);
                break;
            case "merge":
                Repository.merge(args[1]);
                break;

            // TODO: FILL THE REST IN
        }
    }
}
