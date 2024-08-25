package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    Comparator<T> comparator;
    T a;

    public MaxArrayDeque(){
        super();
    }

    public MaxArrayDeque(Comparator<T> c){
        super();
        comparator=c;
    }

    public T max(){
        return max(comparator);
    }

    public T max(Comparator<T> c){
        return a;
    }
}
