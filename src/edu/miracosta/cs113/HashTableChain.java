package edu.miracosta.cs113;
/**
 * HashTableChain.java : This class fully implements the java.util.Map interface to build hash table using chaining.
 * It creates table which is an array of linkedLists.
 *
 * @author : Danny Lee
 * @version : 1.0
 */


import java.util.*;

public class HashTableChain<K, V> implements Map<K,V> {

    //Data fields
    /**The table*/
    private LinkedList<Entry<K, V>>[] table;
    /**The number of keys*/
    private int numKeys;
    /**The capacity of the table[].*/
    private static final int CAPACITY = 101;
    /**The maximum load factor*/
    private static final double LOAD_THRESHOLD = 3.0;


    /**
     * Inner class Entry
     * Contains key-value pairs for a hash table.
     */
    private static class Entry<K, V> implements Map.Entry<K, V>{
        /** The key*/
        private K key;
        /** The value*/
        private V value;

        /**
         * Creates a new key-value pair
         * @param key The key
         * @param value The value
         */
        public Entry(K key, V value){
            this.key = key;
            this.value = value;
        }

        /**
         * Retrieves the key
         * @return
         */
        public K getKey(){
            return key;
        }

        /**
         * Retrieves the value
         * @return The value
         */
        public V getValue(){
            return value;
        }

        /**
         * Sets the value
         * @param val The new value
         * @return The old value
         */
        public V setValue(V val){
            V oldVal = value;
            value = val;
            return oldVal;
        }
    }//end of inner class Entry.

    /**
     * Inner class EntrySet
     */
    private class EntrySet extends AbstractSet<Map.Entry<K, V>>{

        /**
         * Return the size of the set.
         * @return the size of the set.
         */
        public int size() {
            return numKeys;
        }

        /**
         * Return an iterator over the Set.
         * @return an iterator.
         */
        public Iterator<Map.Entry<K, V>> iterator() {
            return new SetIterator();
        }
    }//end of inner class EntrySet.

    /**
     * Inner class SetIterator
     */
    private class SetIterator implements Iterator<Map.Entry<K, V>> {
        //Data field
        /**Index to keep track of the next value of the iterator. Initally 0*/
        int index = 0;
        /**Keeps track of the index of the last item returned by next(); this is used by the remove() method.*/
        Entry<K, V> lastItemReturned = null;
        /**Iterator to traverse the LikedList in this map*/
        Iterator<Entry<K, V>> setIterator = null;

        /**
         * Default constructor
         */
        public SetIterator(){
            index = 0;
            lastItemReturned = null;
            setIterator = null;
        }

        /**
         * Returns true if the iteration has more elements.
         * @return boolean value
         */
        @Override
        public boolean hasNext() {
            if(setIterator != null && setIterator.hasNext()){
                return true;
            }
            //when setIterator is null, set it to the appropriate table[index] linkedlist.
            while(table[index] == null){
                index++;
                if(index >= table.length){
                    return false; //index cannot be equal or greater than table.length!
                }
            }
            setIterator = table[index].iterator();
            return setIterator.hasNext();

        }

        /**
         * Returns the next element in the iteration.
         * @return
         */
        @Override
        public Map.Entry<K, V> next() {
            if(setIterator.hasNext()){
                lastItemReturned = setIterator.next();
                return lastItemReturned;
            }
            else{
                System.out.println("There is no next value! Returning null instead. ");
                return null;
            }
        }

        /**
         * Removes from the underlying collection the last element returned by the iterator.
         */
        @Override
        public void remove(){
            if(lastItemReturned == null){
                System.out.println("lastItemReturned was null!");
            }else{
                // System.out.println("lastItemReturned : " + lastItemReturned + " is removed.");
                setIterator.remove();
                lastItemReturned = null;
            }
        }
    }//end of inner class SetIterator

    /**
     * Constructor
     */
    public HashTableChain(){
        table = new LinkedList[CAPACITY];
    }

    /**
     * Removes all of the mappings from this map.
     */
    public void clear() {
        for(LinkedList<Entry<K, V>> list : table){
            list = null;
        }
        numKeys = 0;
    }

    /**
     * Returns true if this map contains mapping for the specified key.
     * @param key The key object trying to find
     * @return true if the map contains the key.
     */
    public boolean containsKey(Object key) {

        int index = key.hashCode() % table.length;
        if(index < 0){
            index += table.length;
        }
        if(table[index] == null){
            return false;
        }
        for(Entry<K, V> entry : table[index]){
            if(entry.getKey().equals(key)){
                return true;
            }
        }
        // the key was not found.
        return false;
    }

    /**
     * Returns true if this map maps one or more keys to the specified value.
     * @param value The value object trying to find
     * @return true if the the map contains the value.
     */
    public boolean containsValue(Object value) {
        for(LinkedList<Entry<K, V>> list : table){
            if(list != null){
                for(Entry<K, V> entry : list){
                    if(entry.getValue().equals(value)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns a Set view of the mappings contained in this map.
     * @return
     */
    public Set<Map.Entry<K, V>> entrySet() {
        return new EntrySet();
    }

    @Override
    /**
     * Compares the specified object with this map for equality. Returns true if the given object is also a map and two
     * maps represent the same mappings. More formally, two maps m1 and m2 represent the same mappings if m1.entrySet().equals(m2.entrySet()).
     * This ensures that the equals method works properly across different implementations of the Map interface.
     */
    public boolean equals(Object anotherMap){
        boolean isEqual = false;
        if(anotherMap == null || getClass() != anotherMap.getClass()){
            return false;
        }
        HashTableChain<K, V> otherHashMap = (HashTableChain<K, V>) anotherMap;
        return(this.hashCode() == otherHashMap.hashCode());
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
     * @param key
     * @return
     */
    public V get(Object key) {
        int index = key.hashCode() % table.length;
        if(index < 0){
            index += table.length;
        }
        if(table[index] == null){
            return null; // key is not in the table.
        }
        //Search the list at table[index] to find the key.
        for(Entry<K, V> nextItem : table[index]){
            if(nextItem.key.equals(key)){
                return nextItem.value;
            }
        }
        //assert : key is not in the table.
        return null;
    }

    @Override
    /**
     * Returns the hash code value for this map. The hash code of a map is defined to be the sum of the hash codes of each
     * entry in the map's entrySet() view.
     */
    public int hashCode(){
        EntrySet entrySet = new EntrySet();
        int hashCode = 0;
        for(LinkedList<Entry<K, V>> list : table) {
            if (list != null) {
                for (Entry<K, V> entry : list) {
                    hashCode = hashCode + entry.getValue().hashCode();
                }
            }
        }
        return hashCode;
    }

    /**
     * Returns true if this map contains no key-value mappings.
     * @return true if numKeys == 0.
     */
    public boolean isEmpty() {
        return numKeys == 0;
    }

    @Override
    /**
     * Returns a Set view of the keys contained in this map.
     */
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>(size());
        for(LinkedList<Entry<K, V>> list : table){
            if(list != null){
                for(Entry<K, V> entry : list){
                    if(entry != null){
                        keySet.add(entry.getKey());
                    }
                }
            }
        }
        return keySet;
    }

    /**
     * A
     * @param key
     * @param value
     * @return
     */
    public V put(K key, V value) {
        int index = key.hashCode() % table.length;
        if(index < 0){
            index += table.length;
        }
        if(table[index] == null){
            //Create a new linked list at table[index]
            table[index] = new LinkedList<Entry<K,V>>();
        }

        //Search the list at table[index] to find the key.
        for(Entry<K, V> nextItem : table[index]){
            //If the search is successful, replace the old value.
            if(nextItem.key.equals(key)){
                //Replace value for this key.
                V originalValue = nextItem.value;
                nextItem.setValue(value);
                return originalValue;
            }
        }
        //assert : key is not in the table, add new item.
        table[index].addFirst(new Entry<K, V>(key, value));
        numKeys++;
        if(numKeys > (LOAD_THRESHOLD * table.length)){
            rehash();
        }
        return null;
    }

    /**
     * Rehashes the table if the table reaches LOAD_THRESHOLD.
     */
    private void rehash() {
        LinkedList<Entry<K, V>>[] oldTable = table;
        numKeys = 0;
        table = new LinkedList[oldTable.length * 2 + 1];
        for(LinkedList<Entry<K, V>> list : oldTable){
            if(list != null){
                for(Entry<K, V> entry : list){
                    if(entry.getValue() != null){//if the value is null don't add it.
                        put(entry.getKey(), entry.getValue());
                        numKeys++;
                    }
                }
            }
        }
    }

    /**
     * Removes the mapping for a key from this map if it is present.
     * @param key
     * @return
     */
    public V remove(Object key) {
        int index = key.hashCode() % table.length;
        if(index < 0){
            index += table.length;
        }
        if(table[index] == null){
            return null;// The key is not in the table.
        }

        for(Entry<K, V> nextItem : table[index]){
            if(nextItem.getKey().equals(key)){
                V value = nextItem.getValue();
                table[index].remove(nextItem);
                numKeys--;
                if(table[index].isEmpty()){
                    table[index] = null;
                }
                return value;
            }
        }
        //The key is not in the table.
        return null;
    }

    /**
     * Returns the number of key-value mappings in thismap.
     * @return
     */
    public int size() {
        return numKeys;
    }

    //Stub out this method.
    public void putAll(Map<? extends K, ? extends V> m) {
    }


    //Stub out this method
    public Collection<V> values(){
        return null;
    }

}