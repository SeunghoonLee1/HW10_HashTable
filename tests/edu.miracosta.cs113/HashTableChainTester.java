package edu.miracosta.cs113;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

public class HashTableChainTester {

    private HashTableChain<Integer, Integer> hashTableChain;
    private HashTableChain<Integer, Integer> anotherHashTableChain;

    @Before
    public void setup(){
        hashTableChain = new HashTableChain();
    }

    @Test
    public void testPutWhereKeyValuePairDoesNotExist(){
        hashTableChain.put(1,1);
    }

    @Test
    public void testPutWhereKeyValueDoesExist(){
        int originalValue = 0;
        hashTableChain.put(1,1);
        originalValue = hashTableChain.put(1,2);//Collision happens here!
        originalValue = hashTableChain.put(1,3);//Collision happens here!
        assertTrue("Original value(1) should be changed to the new value(2). ", 2 == originalValue);
    }

    @Test
    public void testIsEmpty(){
        hashTableChain.put(1,1);
        assertFalse("hashTableChain should not be empty.", hashTableChain.isEmpty());
    }

    @Test
    public void testClear(){
        hashTableChain.put(1,1);
        hashTableChain.clear();
        assertTrue("hashTableChain should be empty", hashTableChain.isEmpty());
    }

    @Test
    public void testContainsKey(){
        hashTableChain.put(1,1);
        assertTrue("Key : 1 should be contained.", hashTableChain.containsKey(1));
        assertFalse("Key : 2 should not be contained.", hashTableChain.containsKey(2));
    }

    @Test
    public void testContainsValue(){
        hashTableChain.put(1,1);
        assertTrue("Value : 1 should be contained. ", hashTableChain.containsValue(1));
        assertFalse("Value : 2 should not be contained.", hashTableChain.containsValue(2));
    }

    @Test
    public void testEntrySet(){
        hashTableChain.put(1,1);
        hashTableChain.put(2,2);
        assertEquals("The size of the entrySet should be 2",2, hashTableChain.entrySet().size());
    }

    @Test
    public void testSetIteratorMethods(){
        Iterator setIterator = null;
        hashTableChain.put(1,1);
        hashTableChain.put(2,2);
        Map.Entry<Integer, Integer> nextEntry;

        //Testing hasNext method
        setIterator = hashTableChain.entrySet().iterator();
        assertTrue("setIterator should have next element", setIterator.hasNext());
        //Testing next method
        nextEntry = (Map.Entry<Integer, Integer>)setIterator.next();
        assertEquals("setIterator should return key 1 which was the next key", 1, (long)nextEntry.getKey());
        assertEquals("setIterator should return value 1 which was the next value", 1, (long)nextEntry.getValue());
        //Testing remove method
        setIterator.remove();
        assertTrue("setIterator should remove value 1 which was the next value", (!hashTableChain.containsValue(1) && !hashTableChain.containsValue(1)));
    }

    @Test
    public void testEquals(){
        anotherHashTableChain = new HashTableChain<>();
        hashTableChain.put(1,1);
        anotherHashTableChain.put(1,1);

        assertTrue("hashTableChain and anotherHashTableChain should be equal. ", hashTableChain.equals(anotherHashTableChain));
    }

    @Test
    public void testGet(){
        hashTableChain.put(1,7);
        assertTrue("Value for the key : 1 should be 7", hashTableChain.get(1).equals(7));
    }

    @Test
    public void testHashCode(){
        anotherHashTableChain = new HashTableChain<>();

        hashTableChain.put(1,1);
        hashTableChain.put(2,2);
        anotherHashTableChain.put(1,1);
        anotherHashTableChain.put(2,2);

        hashTableChain.hashCode();
        anotherHashTableChain.hashCode();

        assertEquals("hashTableChain and anotherHashTableChain should have equal hashCodes.", hashTableChain.hashCode(), anotherHashTableChain.hashCode());
    }

    @Test
    public void testKeySet(){
        Set<Integer> keySet;
        hashTableChain.put(1,1);
        hashTableChain.put(2,2);
        hashTableChain.put(3,3);
        keySet = hashTableChain.keySet();
        assertTrue("keySet should contain all 1, 2 and 3", keySet.contains(1)&& keySet.contains(2)&& keySet.contains(3));
        System.out.println("keySet : " + keySet);
    }

    @Test
    public void testRemove(){
        assertEquals("Removing from empty table should return null", null, hashTableChain.remove(1));
        hashTableChain.put(1,1);
        assertEquals("Original value 1 should be removed and returned.", 1, (long)hashTableChain.remove(1));
    }

    @Test
    public void testSize(){
        hashTableChain.put(1,1);
        assertEquals("The size of this mapping should be 1", 1, hashTableChain.size());
        hashTableChain.put(2,2);
        assertEquals("The size of this mapping should be 2", 2, hashTableChain.size());
        hashTableChain.put(2,3);
        assertEquals("The size of this mapping should be still 2", 2, hashTableChain.size());
    }

}
