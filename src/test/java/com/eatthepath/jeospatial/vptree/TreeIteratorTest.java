package com.eatthepath.jeospatial.vptree;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.eatthepath.jeospatial.util.SimpleGeospatialPoint;

/**
 * Test suite for the TreeIterator class.
 * 
 * @author <a href="mailto:jon.chambers@gmail.com">Jon Chambers</a>
 */
public class TreeIteratorTest {
    private static final int DEFAULT_BIN_SIZE = 2;
    
    private VPTree<SimpleGeospatialPoint>.VPNode<SimpleGeospatialPoint> testNode;
    private ArrayList<SimpleGeospatialPoint> cities;
    
    @Before
    public void setUp() throws Exception {
        this.cities = new ArrayList<SimpleGeospatialPoint>();
        
        this.cities.add(new SimpleGeospatialPoint(42.338947, -70.919635));
        this.cities.add(new SimpleGeospatialPoint(40.780751, -73.977182));
        this.cities.add(new SimpleGeospatialPoint(37.766529, -122.39577));
        this.cities.add(new SimpleGeospatialPoint(34.048411, -118.34015));
        this.cities.add(new SimpleGeospatialPoint(32.787629, -96.79941));
        this.cities.add(new SimpleGeospatialPoint(41.904667, -87.62504));
        this.cities.add(new SimpleGeospatialPoint(35.169255, -89.990415));
        this.cities.add(new SimpleGeospatialPoint(36.145303, -115.18358));
        this.cities.add(new SimpleGeospatialPoint(42.348937, -83.08994));
        
        VPTree<SimpleGeospatialPoint> tree = new VPTree<SimpleGeospatialPoint>();
        this.testNode = tree.new VPNode<SimpleGeospatialPoint>(DEFAULT_BIN_SIZE);
    }
    
    @Test
    public void testHasNext() {
        TreeIterator<SimpleGeospatialPoint> i = new TreeIterator<SimpleGeospatialPoint>(this.testNode);
        assertFalse(i.hasNext());
        
        this.testNode.addAll(this.cities);
        
        i = new TreeIterator<SimpleGeospatialPoint>(this.testNode);
        assertTrue(i.hasNext());
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testNextEmptyIterator() {
        TreeIterator<SimpleGeospatialPoint> i = new TreeIterator<SimpleGeospatialPoint>(this.testNode);
        
        assertFalse(i.hasNext());
        i.next();
    }
    
    @Test
    public void testNext() {
        this.testNode.addAll(this.cities);
        TreeIterator<SimpleGeospatialPoint> i = new TreeIterator<SimpleGeospatialPoint>(this.testNode);
        
        ArrayList<SimpleGeospatialPoint> returnedPoints = new ArrayList<SimpleGeospatialPoint>();
        
        while(i.hasNext()) {
            returnedPoints.add(i.next());
        }
        
        assertTrue(returnedPoints.containsAll(this.cities));
        assertEquals(this.cities.size(), returnedPoints.size());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        TreeIterator<SimpleGeospatialPoint> i = new TreeIterator<SimpleGeospatialPoint>(this.testNode);
        
        // We don't support removal from iterators, so this should always throw
        // an exception
        i.remove();
    }
}
