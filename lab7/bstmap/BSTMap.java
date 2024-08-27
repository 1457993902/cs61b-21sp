package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{
    private class BSTNode {
        BSTNode left, right;
        K key;
        V value;

        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private BSTNode root;
    int size;

    public BSTMap(){
        root=null;
        size=0;
    }

    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException();
    }

    private BSTNode clear(BSTNode curr) {
        if(curr == null) {
            return null;
        }
        curr.right = clear(curr.right);
        curr.left = clear(curr.left);
        size = 0;
        return null;
    }

    @Override
    public void clear() {
        root = clear(root);
    }

    @Override
    public boolean containsKey(K key) {
        return in(key, root);
    }

    private boolean in(K key, BSTNode curr){
        if(curr == null){
            return false;
        }
        if(key.compareTo(curr.key) == 0){
            return true;
        } else if(key.compareTo(curr.key) < 0) {
            return in(key, curr.left);
        } else if(key.compareTo(curr.key) > 0) {
            return in(key, curr.right);
        }
        return false;
    }

    private V getin(K key, BSTNode curr){
        if(curr == null){
            return null;
        }
        if(key.compareTo(curr.key) == 0){
            return curr.value;
        } else if(key.compareTo(curr.key) < 0) {
            return getin(key, curr.left);
        } else if(key.compareTo(curr.key) > 0) {
            return getin(key, curr.right);
        }
        return null;
    }

    @Override
    public V get(K key) {
        return getin(key, root);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if(key == null){
            return;
        }
        root=putin(key, value, root);
    }

    private BSTNode putin(K key, V value, BSTNode curr) {
        if(curr == null) {
            size += 1;
            return new BSTNode(key, value);
        }
        if(key.compareTo(curr.key) < 0) {
            curr.left = putin(key, value, curr.left);
        } else if(key.compareTo(curr.key) > 0){
            curr.right = putin(key, value, curr.right);
        }
        return curr;
    }

    public void printInOrder(){

    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }
}
