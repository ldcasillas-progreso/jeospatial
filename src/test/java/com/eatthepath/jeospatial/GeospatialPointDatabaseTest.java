package com.eatthepath.jeospatial;

import static org.junit.Assert.*;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import com.eatthepath.jeospatial.util.GeospatialDistanceComparator;
import com.eatthepath.jeospatial.util.SimpleGeospatialPoint;

public abstract class GeospatialPointDatabaseTest {
    protected static Hashtable<String, SimpleGeospatialPoint> cities;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        GeospatialPointDatabaseTest.cities = new Hashtable<String, SimpleGeospatialPoint>();
        
        GeospatialPointDatabaseTest.cities.put("Boston", new SimpleGeospatialPoint(42.338947, -70.919635));
        GeospatialPointDatabaseTest.cities.put("New York", new SimpleGeospatialPoint(40.780751, -73.977182));
        GeospatialPointDatabaseTest.cities.put("San Francisco", new SimpleGeospatialPoint(37.766529, -122.39577));
        GeospatialPointDatabaseTest.cities.put("Los Angeles", new SimpleGeospatialPoint(34.048411, -118.34015));
        GeospatialPointDatabaseTest.cities.put("Dallas", new SimpleGeospatialPoint(32.787629, -96.79941));
        GeospatialPointDatabaseTest.cities.put("Chicago", new SimpleGeospatialPoint(41.904667, -87.62504));
        GeospatialPointDatabaseTest.cities.put("Memphis", new SimpleGeospatialPoint(35.169255, -89.990415));
        GeospatialPointDatabaseTest.cities.put("Las Vegas", new SimpleGeospatialPoint(36.145303, -115.18358));
        GeospatialPointDatabaseTest.cities.put("Detroit", new SimpleGeospatialPoint(42.348937, -83.08994));
    }
    
    /**
     * Returns a new, empty database for testing.
     * 
     * @return a new, empty database
     */
    public abstract GeospatialPointDatabase<SimpleGeospatialPoint> createEmptyDatabase();
    
    @Test
    public void testGetNearestNeighborGeospatialPoint() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        database.addAll(GeospatialPointDatabaseTest.cities.values());
        
        SimpleGeospatialPoint somerville = new SimpleGeospatialPoint(42.387597, -71.099497);
        
        assertEquals(GeospatialPointDatabaseTest.cities.get("Boston"), database.getNearestNeighbor(somerville));
    }
    
    @Test
    public void testGetNearestNeighborGeospatialPointDouble() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        database.addAll(GeospatialPointDatabaseTest.cities.values());
        
        SimpleGeospatialPoint somerville = new SimpleGeospatialPoint(42.387597, -71.099497);
        
        assertEquals(GeospatialPointDatabaseTest.cities.get("Boston"), database.getNearestNeighbor(somerville, 1000 * 1000));
        assertNull(database.getNearestNeighbor(new SimpleGeospatialPoint(0, 0), 1000 * 1000));
    }
    
    @Test
    public void testGetNearestNeighborGeospatialPointSearchCriteria() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        database.addAll(GeospatialPointDatabaseTest.cities.values());
        
        SimpleGeospatialPoint somerville = new SimpleGeospatialPoint(42.387597, -71.099497);
        
        SearchCriteria<SimpleGeospatialPoint> criteria = new SearchCriteria<SimpleGeospatialPoint>() {
            @Override
            public boolean matches(SimpleGeospatialPoint p) {
                return p.getLatitude() < 40.0;
            }
        };
        
        assertEquals(GeospatialPointDatabaseTest.cities.get("Memphis"), database.getNearestNeighbor(somerville, criteria));
    }
    
    @Test
    public void testGetNearestNeighborGeospatialPointDoubleSearchCriteria() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        database.addAll(GeospatialPointDatabaseTest.cities.values());
        
        SimpleGeospatialPoint somerville = new SimpleGeospatialPoint(42.387597, -71.099497);
        
        SearchCriteria<SimpleGeospatialPoint> criteria = new SearchCriteria<SimpleGeospatialPoint>() {
            @Override
            public boolean matches(SimpleGeospatialPoint p) {
                return p.getLatitude() < 40.0;
            }
        };
        
        assertEquals(GeospatialPointDatabaseTest.cities.get("Memphis"), database.getNearestNeighbor(somerville, 2000 * 1000, criteria));
        assertNull(database.getNearestNeighbor(new SimpleGeospatialPoint(0, 0), 2000 * 1000, criteria));
    }
    
    @Test
    public void testGetNearestNeighborsGeospatialPointInt() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        database.addAll(GeospatialPointDatabaseTest.cities.values());
        
        SimpleGeospatialPoint somerville = new SimpleGeospatialPoint(42.387597, -71.099497);
        List<SimpleGeospatialPoint> nearestNeighbors = database.getNearestNeighbors(somerville, 4);
        
        ArrayList<SimpleGeospatialPoint> expectedResults =
                new ArrayList<SimpleGeospatialPoint>(GeospatialPointDatabaseTest.cities.values());
        
        java.util.Collections.sort(expectedResults,
                new GeospatialDistanceComparator<SimpleGeospatialPoint>(somerville));
        
        assertEquals(expectedResults.subList(0, 4), nearestNeighbors);
    }
    
    @Test
    public void testGetNearestNeighborsGeospatialPointIntSearchCriteriaOfE() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        database.addAll(GeospatialPointDatabaseTest.cities.values());
        
        SearchCriteria<SimpleGeospatialPoint> criteria = new SearchCriteria<SimpleGeospatialPoint>() {
            @Override
            public boolean matches(SimpleGeospatialPoint p) {
                return p.getLongitude() <= -90;
            }
        };
        
        SimpleGeospatialPoint somerville = new SimpleGeospatialPoint(42.387597, -71.099497);
        List<SimpleGeospatialPoint> nearestNeighbors = database.getNearestNeighbors(somerville, 4, criteria);
        
        ArrayList<SimpleGeospatialPoint> expectedResults = new ArrayList<SimpleGeospatialPoint>();
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("San Francisco"));
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("Los Angeles"));
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("Dallas"));
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("Las Vegas"));
        
        java.util.Collections.sort(expectedResults,
                new GeospatialDistanceComparator<SimpleGeospatialPoint>(somerville));
        
        assertEquals(expectedResults, nearestNeighbors);
    }
    
    @Test
    public void testGetNearestNeighborsGeospatialPointIntDouble() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        database.addAll(GeospatialPointDatabaseTest.cities.values());
        
        SimpleGeospatialPoint somerville = new SimpleGeospatialPoint(42.387597, -71.099497);
        List<SimpleGeospatialPoint> nearestNeighbors = database.getNearestNeighbors(somerville, 4, 1000 * 1000);
        
        ArrayList<SimpleGeospatialPoint> expectedResults = new ArrayList<SimpleGeospatialPoint>();
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("Boston"));
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("New York"));
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("Detroit"));
        
        java.util.Collections.sort(expectedResults,
                new GeospatialDistanceComparator<SimpleGeospatialPoint>(somerville));
        
        assertEquals(expectedResults, nearestNeighbors);
        
        for(SimpleGeospatialPoint p : nearestNeighbors) {
            assertTrue(somerville.getDistanceTo(p) < 1000 * 1000);
        }
    }
    
    @Test
    public void testGetNearestNeighborsGeospatialPointIntDoubleSearchCriteriaOfE() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        database.addAll(GeospatialPointDatabaseTest.cities.values());
        
        SearchCriteria<SimpleGeospatialPoint> criteria = new SearchCriteria<SimpleGeospatialPoint>() {
            @Override
            public boolean matches(SimpleGeospatialPoint p) {
                return p.getLatitude() > 42.0;
            }
        };
        
        SimpleGeospatialPoint somerville = new SimpleGeospatialPoint(42.387597, -71.099497);
        List<SimpleGeospatialPoint> nearestNeighbors = database.getNearestNeighbors(somerville, 4, 1000 * 1000, criteria);
        
        ArrayList<SimpleGeospatialPoint> expectedResults = new ArrayList<SimpleGeospatialPoint>();
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("Boston"));
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("Detroit"));
        
        java.util.Collections.sort(expectedResults,
                new GeospatialDistanceComparator<SimpleGeospatialPoint>(somerville));
        
        assertEquals(expectedResults, nearestNeighbors);
    }
    
    @Test
    public void testGetAllNeighborsWithinDistanceGeospatialPointDouble() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        database.addAll(GeospatialPointDatabaseTest.cities.values());
        
        SimpleGeospatialPoint somerville = new SimpleGeospatialPoint(42.387597, -71.099497);
        List<SimpleGeospatialPoint> nearestNeighbors = database.getAllNeighborsWithinDistance(somerville, 1000 * 1000);
        
        ArrayList<SimpleGeospatialPoint> expectedResults = new ArrayList<SimpleGeospatialPoint>();
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("Boston"));
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("New York"));
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("Detroit"));
        
        java.util.Collections.sort(expectedResults,
                new GeospatialDistanceComparator<SimpleGeospatialPoint>(somerville));
        
        assertEquals(expectedResults, nearestNeighbors);
        
        for(SimpleGeospatialPoint p : nearestNeighbors) {
            assertTrue(somerville.getDistanceTo(p) < 1000 * 1000);
        }
    }
    
    @Test
    public void testGetAllNeighborsWithinDistanceGeospatialPointDoubleSearchCriteriaOfE() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        database.addAll(GeospatialPointDatabaseTest.cities.values());
        
        SearchCriteria<SimpleGeospatialPoint> criteria = new SearchCriteria<SimpleGeospatialPoint>() {
            @Override
            public boolean matches(SimpleGeospatialPoint p) {
                return p.getLatitude() > 40.0;
            }
        };
        
        SimpleGeospatialPoint somerville = new SimpleGeospatialPoint(42.387597, -71.099497);
        List<SimpleGeospatialPoint> nearestNeighbors =
                database.getAllNeighborsWithinDistance(somerville, 10000 * 1000, criteria);
        
        ArrayList<SimpleGeospatialPoint> expectedResults = new ArrayList<SimpleGeospatialPoint>();
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("Boston"));
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("New York"));
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("Detroit"));
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("Chicago"));
        
        java.util.Collections.sort(expectedResults,
                new GeospatialDistanceComparator<SimpleGeospatialPoint>(somerville));
        
        assertEquals(expectedResults, nearestNeighbors);
    }
    
    @Test
    public void testGetAllPointsInBoundingBox() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        database.addAll(GeospatialPointDatabaseTest.cities.values());
        
        List<SimpleGeospatialPoint> pointsInBox = database.getAllPointsInBoundingBox(-90, -70, 43, 40);
        
        ArrayList<SimpleGeospatialPoint> expectedResults = new ArrayList<SimpleGeospatialPoint>();
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("Boston"));
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("New York"));
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("Chicago"));
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("Detroit"));
        
        assertEquals(expectedResults.size(), pointsInBox.size());
        assertTrue(pointsInBox.containsAll(expectedResults));
    }
    
    @Test
    public void testGetAllPointsInBoundingBoxOrderingPoint() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        database.addAll(GeospatialPointDatabaseTest.cities.values());
        
        SimpleGeospatialPoint somerville = new SimpleGeospatialPoint(42.387597, -71.099497);
        
        List<SimpleGeospatialPoint> pointsInBox = database.getAllPointsInBoundingBox(-90, -70, 43, 40, somerville);
        
        ArrayList<SimpleGeospatialPoint> expectedResults = new ArrayList<SimpleGeospatialPoint>();
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("Boston"));
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("New York"));
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("Chicago"));
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("Detroit"));
        
        java.util.Collections.sort(expectedResults,
                new GeospatialDistanceComparator<SimpleGeospatialPoint>(somerville));
        
        assertEquals(expectedResults, pointsInBox);
    }
    
    @Test
    public void testGetAllPointsInBoundingBoxSearchCriteria() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        database.addAll(GeospatialPointDatabaseTest.cities.values());
        
        SearchCriteria<SimpleGeospatialPoint> bostonOnlyCriteria = new SearchCriteria<SimpleGeospatialPoint>() {
            @Override
            public boolean matches(SimpleGeospatialPoint point) {
                return point.equals(GeospatialPointDatabaseTest.cities.get("Boston"));
            }
        };
        
        List<SimpleGeospatialPoint> pointsInBox = database.getAllPointsInBoundingBox(-90, -70, 43, 40, bostonOnlyCriteria);
        
        ArrayList<SimpleGeospatialPoint> expectedResults = new ArrayList<SimpleGeospatialPoint>();
        expectedResults.add(GeospatialPointDatabaseTest.cities.get("Boston"));
        
        assertEquals(expectedResults, pointsInBox);
    }
    
    @Test
    public void testGetAllPointsInBoundingBoxWideRatio() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        
        SimpleGeospatialPoint northeast = new SimpleGeospatialPoint(45, 100);
        SimpleGeospatialPoint southwest = new SimpleGeospatialPoint(-10, -100);
        
        database.add(northeast);
        database.add(southwest);
        
        List<SimpleGeospatialPoint> pointsInBox = database.getAllPointsInBoundingBox(-100, 100, 45, -10);
        
        assertEquals(2, pointsInBox.size());
        assertTrue(pointsInBox.contains(northeast));
        assertTrue(pointsInBox.contains(southwest));
    }
    
    @Test
    public void testIterator() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        assertTrue(database.isEmpty());
        
        Iterator<SimpleGeospatialPoint> i = database.iterator();
        assertFalse(i.hasNext());

        database.addAll(GeospatialPointDatabaseTest.cities.values());
        i = database.iterator();
        
        ArrayList<SimpleGeospatialPoint> iteratedPoints = new ArrayList<SimpleGeospatialPoint>();
        
        while(i.hasNext()) {
            iteratedPoints.add(i.next());
        }
        
        assertEquals(database.size(), iteratedPoints.size());
        
        for(SimpleGeospatialPoint p : iteratedPoints) {
            assertTrue(database.contains(p));
        }
        
        for(SimpleGeospatialPoint p : GeospatialPointDatabaseTest.cities.values()) {
            assertTrue(iteratedPoints.contains(p));
        }
    }
    
    @Test
    public void testSize() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        assertTrue(database.isEmpty());
        assertEquals(0, database.size());
        
        database.addAll(GeospatialPointDatabaseTest.cities.values());
        
        assertEquals(GeospatialPointDatabaseTest.cities.size(), database.size());
    }
    
    @Test
    public void testIsEmpty() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        
        SimpleGeospatialPoint boston = GeospatialPointDatabaseTest.cities.get("Boston");
        
        assertTrue(database.isEmpty());
        assertTrue(database.add(boston));
        assertFalse(database.isEmpty());
        assertTrue(database.remove(boston));
        assertTrue(database.isEmpty());
    }
    
    @Test
    public void testContains() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        
        SimpleGeospatialPoint boston = GeospatialPointDatabaseTest.cities.get("Boston");
        
        assertFalse(database.contains(boston));
        assertTrue(database.add(boston));
        assertTrue(database.contains(boston));
        assertTrue(database.remove(boston));
        assertFalse(database.contains(boston));
    }
    
    @Test
    public void testToArray() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        database.addAll(GeospatialPointDatabaseTest.cities.values());
        
        Object[] array = database.toArray();
        
        assertEquals(database.size(), array.length);
        
        for(Object o : array) {
            assertTrue(database.contains(o));
        }
    }
    
    @Test
    public void testToArrayTArray() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        database.addAll(GeospatialPointDatabaseTest.cities.values());
        
        SimpleGeospatialPoint[] points = database.toArray(new SimpleGeospatialPoint[0]);
        
        assertEquals(database.size(), points.length);
        
        for(SimpleGeospatialPoint p : points) {
            assertTrue(database.contains(p));
        }
        
        points = new SimpleGeospatialPoint[database.size() * 2];
        SimpleGeospatialPoint[] returnedPoints = database.toArray(points);
        
        assertSame(points, returnedPoints);
        
        for(int i = 0; i < database.size(); i++) {
            assertTrue(database.contains(points[i]));
        }
        
        assertNull(points[database.size()]);
    }
    
    @Test
    public void testAdd() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        
        assertTrue(database.isEmpty());
        assertFalse(database.contains(GeospatialPointDatabaseTest.cities.get("Boston")));
        assertTrue(database.add(GeospatialPointDatabaseTest.cities.get("Boston")));
        assertTrue(database.contains(GeospatialPointDatabaseTest.cities.get("Boston")));
        assertEquals(1, database.size());
    }
    
    @Test
    public void testRemove() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        database.addAll(GeospatialPointDatabaseTest.cities.values());
        
        assertTrue(database.remove(GeospatialPointDatabaseTest.cities.get("Boston")));
        assertFalse(database.remove(GeospatialPointDatabaseTest.cities.get("Boston")));
        
        assertEquals(GeospatialPointDatabaseTest.cities.size() - 1, database.size());
    }
    
    @Test
    public void testContainsAll() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        database.addAll(GeospatialPointDatabaseTest.cities.values());
        
        assertTrue(database.containsAll(GeospatialPointDatabaseTest.cities.values()));
        assertTrue(database.remove(GeospatialPointDatabaseTest.cities.get("Boston")));
        assertFalse(database.containsAll(GeospatialPointDatabaseTest.cities.values()));
    }
    
    @Test
    public void testAddAll() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        
        assertTrue(database.isEmpty());
        
        database.addAll(GeospatialPointDatabaseTest.cities.values());
        
        assertFalse(database.isEmpty());
        assertEquals(GeospatialPointDatabaseTest.cities.size(), database.size());
        
        for(SimpleGeospatialPoint p : GeospatialPointDatabaseTest.cities.values()) {
            assertTrue(database.contains(p));
        }
    }
    
    @Test
    public void testRemoveAll() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        database.addAll(GeospatialPointDatabaseTest.cities.values());
        database.add(GeospatialPointDatabaseTest.cities.get("Boston"));
        // Add Boston a second time; the behavioral contract for the Collection
        // interface states, "After this call returns, this collection will
        // contain no elements in common with the specified collection."
        
        ArrayList<SimpleGeospatialPoint> citiesToRemove = new ArrayList<SimpleGeospatialPoint>();
        
        citiesToRemove.add(GeospatialPointDatabaseTest.cities.get("Boston"));
        citiesToRemove.add(GeospatialPointDatabaseTest.cities.get("Las Vegas"));
        citiesToRemove.add(GeospatialPointDatabaseTest.cities.get("Detroit"));
        
        assertTrue(database.removeAll(citiesToRemove));
        assertFalse(database.removeAll(citiesToRemove));
        
        for(SimpleGeospatialPoint p : citiesToRemove) {
            assertFalse(database.contains(p));
        }
    }
    
    @Test
    public void testRetainAll() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        database.addAll(GeospatialPointDatabaseTest.cities.values());
        
        List<SimpleGeospatialPoint> citiesToRetain =
            new ArrayList<SimpleGeospatialPoint>(GeospatialPointDatabaseTest.cities.values());
        
        citiesToRetain = citiesToRetain.subList(0, 4);
        
        database.retainAll(citiesToRetain);
        
        assertEquals(citiesToRetain.size(), database.size());
        assertTrue(database.containsAll(citiesToRetain));
    }
    
    @Test
    public void testClear() {
        GeospatialPointDatabase<SimpleGeospatialPoint> database = this.createEmptyDatabase();
        database.addAll(GeospatialPointDatabaseTest.cities.values());
        
        assertFalse(database.isEmpty());
        
        database.clear();
        
        assertTrue(database.isEmpty());
    }
}
