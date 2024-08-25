package deque;

import org.junit.Test;

import static org.junit.Assert.*;

public class ADTest {
    @Test
    public void tes1(){
        ArrayDeque<Integer> ad1=new ArrayDeque<>();
        for(int i=0;i<100;i++){
            ad1.addFirst(i);
        }
        for(int i=0;i<100;i++){
            ad1.removeFirst();
        }
        assertTrue(ad1.isEmpty());
        for(int i=0;i<100;i++){
            ad1.addLast(i);
        }
        assertEquals(56,(int)ad1.get(56));
        for(int i=0;i<100;i++){
            ad1.removeLast();
        }
        assertTrue(ad1.isEmpty());
    }
}
