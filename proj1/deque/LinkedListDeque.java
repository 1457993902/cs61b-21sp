package deque;
/**Create a linkedlistdeque*/
public class LinkedListDeque<T>{
    public static class LinkNode<T>{
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


    LinkNode<T> sentinel;
    int size;


    public LinkedListDeque(){
        sentinel=new LinkNode<>(null,null,null);
        sentinel.pre=sentinel;
        sentinel.next=sentinel;
        size=0;
    }

    /**return the size of the deque*/
    public int size(){
        return size;
    }

    /**add an element to the deque's begin*/
    public void addFirst(T item){
        sentinel.next.pre=new LinkNode<>(item,sentinel,sentinel.next);
        sentinel.next=sentinel.next.pre;
        size+=1;
    }

    /**add an element to the deque's end*/
    public void addLast(T item){
        sentinel.pre.next=new LinkNode<>(item,sentinel.pre,sentinel);
        sentinel.pre=sentinel.pre.next;
        size+=1;
    }

    /**return whether the deque is empty or not */
    public boolean isEmpty(){
        return size==0;
    }

    /**remove the first element in deque*/
    public T removeFirst(){
        if(isEmpty()){
            return null;
        }
        LinkNode<T> temp=sentinel.next;
        sentinel.next=sentinel.next.next;
        size--;
        return temp.item;
    }

    /**remove the last element in deque*/
    public T removeLast(){
        if(isEmpty()){
            return null;
        }
        LinkNode<T> temp=sentinel.pre;
        sentinel.pre=sentinel.pre.pre;
        size--;
        return temp.item;
    }

    /**Prints the items in the deque from first to last,
     separated by a space. Once all the items have been printed,
     print out a new line.*/
    public void printDeque(){
        for(LinkNode<T> curr=sentinel.next;curr!=sentinel&&curr!=null;curr=curr.next){
            System.out.print(curr.item+" ");
        }
        System.out.println();
    }

    /**Gets the item at the given index,
    where 0 is the front, 1 is the next item, and so forth.
     If no such item exists, returns null.*/
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

}