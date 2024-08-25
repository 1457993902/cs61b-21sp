package deque;

import java.util.Iterator;

/**Create a linkedlistdeque*/
public class LinkedListDeque<T> implements Deque<T>,Iterable<T>{
    private static class LinkNode<T>{
        T item;
        LinkNode<T> next,pre;

        public LinkNode(T item,LinkNode<T> pre,LinkNode<T> next){
            this.item=item;
            this.next=next;
            this.pre=pre;
        }

        public T Getrecursive(int index){
            if(index==0){
                return this.item;
            }
            return next.Getrecursive(index-1);
        }
    }


    private LinkNode<T> sentinel;
    private int size;


    public LinkedListDeque(){
        sentinel=new LinkNode<>(null,null,null);
        sentinel.pre=sentinel;
        sentinel.next=sentinel;
        size=0;
    }

    /**return the size of the deque*/
    @Override
    public int size(){
        return size;
    }

    /**add an element to the deque's begin*/
    @Override
    public void addFirst(T item){
        sentinel.next.pre=new LinkNode<>(item,sentinel,sentinel.next);
        sentinel.next=sentinel.next.pre;
        size+=1;
    }


    /**add an element to the deque's end*/
    @Override
    public void addLast(T item){
        sentinel.pre.next=new LinkNode<>(item,sentinel.pre,sentinel);
        sentinel.pre=sentinel.pre.next;
        size+=1;
    }


    /**remove the first element in deque*/
    @Override
    public T removeFirst(){
        if(isEmpty()){
            return null;
        }
        LinkNode<T> temp=sentinel.next;
        sentinel.next=sentinel.next.next;
        sentinel.next.pre=sentinel;
        size--;
        return temp.item;
    }

    /**remove the last element in deque*/
    @Override
    public T removeLast(){
        if(isEmpty()){
            return null;
        }
        LinkNode<T> temp=sentinel.pre;
        sentinel.pre=sentinel.pre.pre;
        sentinel.pre.next=sentinel;
        size--;
        return temp.item;
    }

    /**Prints the items in the deque from first to last,
     separated by a space. Once all the items have been printed,
     print out a new line.*/
    @Override
    public void printDeque(){
        for(LinkNode<T> curr=sentinel.next;curr!=sentinel&&curr!=null;curr=curr.next){
            System.out.print(curr.item+" ");
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
        LinkNode<T> curr=sentinel.next;
        for(int i=0;i<index;curr=curr.next,i++);
        return curr.item;
    }

    /*recursively get the item**/
    public T getRecursive(int index){
        if(index>size-1){
            return null;
        }
        return sentinel.next.Getrecursive(index);
    }

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

    @Override
    public Iterator<T> iterator() {
        return new LinkedIterator() ;
    }

    private class LinkedIterator implements Iterator<T> {

        private int position;

        public LinkedIterator() {
            position = 0;
        }

        @Override
        public boolean hasNext() {
            if (position == size()) {
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

}