package deque;

import java.util.Iterator;

/**Create an Arraylistdeque*/
public class ArrayDeque<T> implements Deque<T>,Iterable<T>{

    private T[] items;
    private int size;
    private int indexFirst;
    private int max;


    public ArrayDeque(){
        items=(T[])new Object[8];
        max=8;
        size=0;
        indexFirst=0;
    }

    /*circularly return the true position of an index**/
    private int position(int index){
        return (max+index+indexFirst)%max;
    }


    /**return the size of the deque*/
    @Override
    public int size(){
        return size;
    }

    /*resize the deque bigger**/
    private void increasesize(){
        T[] increased=(T[])new Object[max*2];
        for(int i=0;i<size;i++){
            increased[i]=items[position(i)];
        }
        items=increased;
        indexFirst=0;
        max*=2;
    }

    /*resize the deque smaller**/
    private void decreasesize(){
        T[] decreased=(T[])new Object[max/2];
        for(int i=0;i<size;i++){
            decreased[i]=items[position(i)];
        }
        indexFirst=0;
        items=decreased;
        max/=2;
    }


    /**add an element to the deque's begin*/
    @Override
    public void addFirst(T item){
        if(isEmpty()){
            items[position(0)]=item;
            size+=1;
            return;
        }
        if(size()==max){
            increasesize();
        }
        items[position(-1)]=item;
        indexFirst=(indexFirst+max-1)%max;
        size+=1;
    }


    /**add an element to the deque's end*/
    @Override
    public void addLast(T item){
        if(size==max){
            increasesize();
        }
        items[position(size)]=item;
        size+=1;
    }


    /**remove the first element in deque*/
    @Override
    public T removeFirst(){
        if(isEmpty()){
            return null;
        }
        T temp=items[position(0)];
        items[position(0)]=null;
        indexFirst=(indexFirst+1)%max;
        size-=1;
        if(1.0*size/max<0.25&&size>16){
            decreasesize();
        }
        return temp;
    }


    /**remove the last element in deque*/
    @Override
    public T removeLast(){
        if(isEmpty()){
            return null;
        }
        T temp=items[position(size-1)];
        items[position(size-1)]=null;
        size-=1;
        if(1.0*size/max<0.25&&size>16){
            decreasesize();
        }
        return temp;
    }


    /**Prints the items in the deque from first to last,
     separated by a space. Once all the items have been printed,
     print out a new line.*/
    @Override
    public void printDeque(){
        for(int i=0;i<size;i++){
            System.out.print(items[position(i)]);
        }
        System.out.println();
    }

    /**Gets the item at the given index,
     where 0 is the front, 1 is the next item, and so forth.
     If no such item exists, returns null.*/
    @Override
    public T get(int index){
        if (index>size-1){
            return null;
        }
        return items[position(index)];
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator() ;
    }

    private class ArrayIterator implements Iterator<T>{

        private int position;

        public ArrayIterator(){
            position=0;
        }

        @Override
        public boolean hasNext() {
            if(position==size()){
                return false;
            }
            return true;
        }

        @Override
        public T next() {
            position+=1;
            return get(position-1);
        }

    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Deque){
            Deque ob;
            if(o instanceof LinkedListDeque<?>){
                ob=(LinkedListDeque<?>) o;
            }else{
                ob=(ArrayDeque<?>) o;
            }
            for(int i=0;i<Math.max(size(),ob.size());i++) {
                if (ob.get(i) != get(i)) {
                    return false;
                }
            }
            return true;
        }else{
            return false;
        }
    }


}