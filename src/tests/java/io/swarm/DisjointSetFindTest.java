package io.swarm;

import io.swarm.collections.DisjointSet;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DisjointSetFindTest {

    DisjointSet set;

    @Before
    public void setup() {
        set = new DisjointSet(4, 4);
        int[] array = new int[4 * 4];
        set.replaceSet(initialiseArray(array));
    }

    private int[] initialiseArray(int[] array) {
        for(int i = 0; i < array.length; i++) {
            array[i] = (i%4 == 0) ? set.getRootValue(4) : i - 1;
        }
        return array;
    }

    @Test
    public void elementThreeHasRootZero() {
        assertEquals(0, set.find(3));
    }

    @Test
    public void lastElementHasRootThree() {
        assertEquals(12, set.find(set.getSize() - 1));
    }

    @Test
    public void disjointSetHasFourClusters() {
        assertEquals(4, set.countSets());
    }


}
