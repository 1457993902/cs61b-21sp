package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    private int hash(K key) {
        int hash = key.hashCode();
        return Math.floorMod(hash, buckets.length);
    }

    @Override
    public void clear() {
        buckets = createTable(16);
        keys = new HashSet<>();
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return keys.contains(key);
    }

    @Override
    public V get(K key) {
        if(buckets[hash(key)] == null){
            return null;
        }
        for(Node n :buckets[hash(key)]) {
            if(key.equals(n.key)) {
                return n.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        Collection<Node> bucket = buckets[hash(key)];
        remove(key);
        bucket.add(createNode(key, value));
        if(!keys.contains(key)) {
            size += 1;
            keys.add(key);
        }
        if(size / buckets.length > maxLoad){
            resize(buckets.length * 2);
        }
    }

    @Override
    public Set<K> keySet() {
        return keys;
    }

    @Override
    public V remove(K key) {
        Node temp = null;
        Collection<Node> bucket = buckets[hash(key)];
        if(bucket != null) {
            for(Node n: bucket) {
                if(n.key.equals(key)){
                    temp = n;
                    bucket.remove(n);
                    keys.remove(key);
                    size -= 1;
                    return temp.value;
                }
            }
        }
        return null;
    }

    @Override
    public V remove(K key, V value) {
        Node temp = null;
        Collection<Node> bucket = buckets[hash(key)];
        if(bucket != null){
            for(Node n: bucket) {
                if(n.key.equals(key)&&n.value.equals(value)){
                    temp = n;
                    bucket.remove(n);
                    keys.remove(key);
                    size -= 1;
                    return temp.value;
                }
            }
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keys.iterator();
    }

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private HashSet<K> keys;
    private int size;
    private double maxLoad;

    /** Constructors */
    public MyHashMap() {
        this(16, 0.75);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, 0.75);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        size = 0;
        buckets = createTable(initialSize);
        for(int i = 0;i < initialSize;i++){
            buckets[i] = createBucket();
        }
        keys = new HashSet<>();
        this.maxLoad = maxLoad;
    }

    private void resize(int maxSize){
        Collection<Node>[] temp = buckets;
        buckets = createTable(maxSize);
        for(int i = 0;i < maxSize;i++){
            buckets[i] = createBucket();
        }
        for(Collection<Node> b: temp){
            for(Node n: b){
                put(n.key, n.value);
            }
        }
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    // Your code won't compile until you do so!

}
