package timingtest;
import edu.princeton.cs.algs4.Stopwatch;


/**
 * Created by hug.
 */
public class TimeSLList {

    static int M=10000;

    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        AList<Integer> cout=new AList<>(),opcount=new AList<>();
        AList<Double> Fulltime=new AList<>();
        for(int i=0,N=1000;i<8;i++){
            SLList<Integer> item=new SLList<>();
            for(int n=0;n<N;n++){
                item.addLast(n);
            }
            Stopwatch sw=new Stopwatch();
            for(int j=0;j<M;j++){
                item.getLast();
            }
            double time=sw.elapsedTime();
            cout.addLast(N);
            Fulltime.addLast(time);
            opcount.addLast(M);
            N*=2;
        }
        printTimingTable(cout,Fulltime,opcount);    }

}
