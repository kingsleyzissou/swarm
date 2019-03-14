package io.swarm;

import io.swarm.collections.DisjointSet;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class DisjointSetUnionTest {

    DisjointSet set;

    @Before
    public void setup() {
        set = new DisjointSet(4, 4);
        int[] array = new int[4 * 4];
        int rootValue = set.getRootValue(1);
        Arrays.fill(array, rootValue);
        set.replaceSet(array);
    }

    @Test
    public void disjointSetWithNRootsHasCountN() {
        assertEquals(set.getSize(), set.countSets());
    }

    @Test
    public void mergingFirstTwoElementsReducesCountByOne() {
        set.quickUnion(0,1);
        assertEquals(set.getSize() - 1, set.countSets());
    }

    @Test
    public void mergingLastTwoElementsReducesCountByOne() {
        set.quickUnion(set.getSize() - 1,set.getSize() - 2);
        assertEquals(set.getSize() - 1, set.countSets());
    }

    @Test
    public void mergingAdjacentElementsHalvesCount() {
        for(int i = 0; i < set.getSize() - 1; i += 2) set.quickUnion(i, i + 1);
        assertEquals(set.getSize()/2, set.countSets());
    }

}
