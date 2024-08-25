package randomizedtest;

import edu.princeton.cs.introcs.StdRandom;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class testThreeAddThreeRemove {

    @Test
    public void tes1(){
        AListNoResizing nosize=new AListNoResizing<>();
        BuggyAList buggy=new BuggyAList<>();
        for(int i=4;i<7;i++){
            nosize.addLast(i);
            buggy.addLast(i);
        }
        for(int i=0;i< buggy.size();i++){
            assertEquals(nosize.removeLast(),buggy.removeLast());
        }
    }

    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList buggy=new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                buggy.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
            } else if (operationNumber == 2) {
                if(L.size()>0){
                    L.getLast();
                    buggy.getLast();
                }
            } else if (operationNumber == 3) {
                if(L.size()>0){
                    L.removeLast();
                    buggy.removeLast();
                }
            }
        }
    }
}
