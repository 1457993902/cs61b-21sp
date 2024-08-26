package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c){
        super();
        comparator=c;
    }

    public T max(){
        return max(comparator);
    }

    public T max(Comparator<T> c){
        if(isEmpty()){
            return null;
        }
        T maxinum=get(0);
        for(T i : this){
            maxinum=comparator.compare(i,maxinum)>0 ? i :maxinum;
        }
        return maxinum;
    }

}
