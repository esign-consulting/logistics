/* 
 * The MIT License
 *
 * Copyright 2015 Esign Consulting Ltda.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.com.esign.logistics.core;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author gustavomunizdocarmo
 */
public class RoutesMapTest {
    
    /**
     * Test of getName method, of class RoutesMap.
     */
    @Test
    public void testGetName() {
        RoutesMap routesMap = new RoutesMap("SP");
        assertEquals("SP", routesMap.getName());
    }

    /**
     * Test of getPlaces method, of class RoutesMap.
     */
    @Test
    public void testGetPlaces() {
        RoutesMap routesMap = new RoutesMap("SP");
        assertNotNull(routesMap.getPlaces());
    }

    /**
     * Test of getRoutes method, of class RoutesMap.
     */
    @Test
    public void testGetRoutes_0args() {
        RoutesMap routesMap = new RoutesMap("SP");
        assertNotNull(routesMap.getRoutes());
    }

    /**
     * Test of getPlace method, of class RoutesMap.
     */
    @Test
    public void testGetPlace() {
        RoutesMap routesMap = new RoutesMap("SP");
        Place placeA = routesMap.addPlace("A");
        assertEquals(placeA, routesMap.getPlace("A"));
    }

    /**
     * Test of getRoute method, of class RoutesMap.
     */
    @Test
    public void testGetRoute_String_String() {
        RoutesMap routesMap = new RoutesMap("SP");
        Route[] routes = routesMap.addRoute("A", "B", 10);
        assertEquals(routes[0], routesMap.getRoute("A", "B"));
        assertEquals(routes[1], routesMap.getRoute("B", "A"));
    }
    
    

    /**
     * Test of getRoute method, of class RoutesMap.
     */
    @Test
    public void testGetRoute_Place_Place() {
        RoutesMap routesMap = new RoutesMap("SP");
        Place placeA = new Place("A");
        Place placeB = new Place("B");
        Route[] routes = routesMap.addRoute(placeA, placeB, 10);
        assertEquals(routes[0], routesMap.getRoute(placeA, placeB));
        assertEquals(routes[1], routesMap.getRoute(placeB, placeA));
    }

    /**
     * Test of getRoute method, of class RoutesMap.
     */
    @Test
    public void testGetRoute_Path() {
        RoutesMap routesMap = new RoutesMap("SP");
        Place placeA = new Place("A");
        Place placeB = new Place("B");
        Route[] routes = routesMap.addRoute(placeA, placeB, 10);
        
        Path pathAB = new Path();
        pathAB.addPlace(placeA);
        pathAB.addPlace(placeB);
        assertEquals(routes[0], routesMap.getRoute(pathAB));
        
        Path pathBA = new Path();
        pathBA.addPlace(placeB);
        pathBA.addPlace(placeA);
        assertEquals(routes[1], routesMap.getRoute(pathBA));
    }

    /**
     * Test of getRoutes method, of class RoutesMap.
     */
    @Test
    public void testGetRoutes_List() {
        RoutesMap routesMap = new RoutesMap("SP");
        Place placeA = new Place("A");
        Place placeB = new Place("B");
        Place placeC = new Place("C");
        Route[] routesAB = routesMap.addRoute(placeA, placeB, 10);
        Route[] routesBC = routesMap.addRoute(placeB, placeC, 10);
        
        Path pathBA = new Path();
        pathBA.addPlace(placeB);
        pathBA.addPlace(placeA);
        Path pathCB = new Path();
        pathCB.addPlace(placeC);
        pathCB.addPlace(placeB);
        List<Path> paths2 = Arrays.asList(new Path[] {pathBA, pathCB});
        List<Route> routes2 = Arrays.asList(new Route[] {routesAB[1], routesBC[1]});
        assertEquals(routes2, routesMap.getRoutes(paths2));
        
        
        Path pathAB = new Path();
        pathAB.addPlace(placeA);
        pathAB.addPlace(placeB);
        Path pathBC = new Path();
        pathBC.addPlace(placeB);
        pathBC.addPlace(placeC);
        List<Path> paths1 = Arrays.asList(new Path[] {pathAB, pathBC});
        List<Route> routes1 = Arrays.asList(new Route[] {routesAB[0], routesBC[0]});
        assertEquals(routes1, routesMap.getRoutes(paths1));
    }

    /**
     * Test of addPlace method, of class RoutesMap.
     */
    @Test
    public void testAddPlace() {
        RoutesMap routesMap = new RoutesMap("SP");
        Place place = routesMap.addPlace("A");
        assertTrue(routesMap.getPlaces().contains(place));
    }
    
    /**
     * Test of addPlace method, of class RoutesMap.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddPlaceIllegalArgumentException() {
        RoutesMap routesMap = new RoutesMap("SP");
        routesMap.addPlace("");
    }

    /**
     * Test of addRoute method, of class RoutesMap.
     */
    @Test
    public void testAddRoute_3args_1() {
        RoutesMap routesMap = new RoutesMap("SP");
        Route[] routes = routesMap.addRoute("A", "B", 10);
        assertTrue(routesMap.getRoutes().contains(routes[0]));
        assertTrue(routesMap.getRoutes().contains(routes[1]));
    }
    
    /**
     * Test of addRoute method, of class RoutesMap.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddRoute_3args_1IllegalArgumentException() {
        RoutesMap routesMap = new RoutesMap("SP");
        routesMap.addRoute("", "B", 10);
    }

    /**
     * Test of addRoute method, of class RoutesMap.
     */
    @Test
    public void testAddRoute_3args_2() {
        RoutesMap routesMap = new RoutesMap("SP");
        Place placeA = new Place("A");
        Place placeB = new Place("B");
        Route[] routesAB = routesMap.addRoute(placeA, placeB, 10);
        assertTrue(routesMap.getRoutes().contains(routesAB[0]));
        assertTrue(routesMap.getRoutes().contains(routesAB[1]));
    }
    
    /**
     * Test of addRoute method, of class RoutesMap.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddRoute_3args_2IllegalArgumentException() {
        RoutesMap routesMap = new RoutesMap("SP");
        Place placeA = new Place("A");
        Place placeB = null;
        routesMap.addRoute(placeA, placeB, 10);
    }

    /**
     * Test of containsPlace method, of class RoutesMap.
     */
    @Test
    public void testContainsPlace() {
        RoutesMap routesMap = new RoutesMap("SP");
        Place place = routesMap.addPlace("A");
        assertTrue(routesMap.containsPlace(place));
    }

    /**
     * Test of hashCode method, of class RoutesMap.
     */
    @Test
    public void testHashCode() {
        RoutesMap routesMap1 = new RoutesMap("SP");
        RoutesMap routesMap2 = new RoutesMap("SP");
        assertEquals(routesMap1.hashCode(), routesMap2.hashCode());
    }

    /**
     * Test of equals method, of class RoutesMap.
     */
    @Test
    public void testEquals() {
        RoutesMap routesMap1 = new RoutesMap("SP");
        RoutesMap routesMap2 = new RoutesMap("SP");
        assertTrue(routesMap1.equals(routesMap2));
    }

    /**
     * Test of toString method, of class RoutesMap.
     */
    @Test
    public void testToString() {
        RoutesMap routesMap = new RoutesMap("SP");
        assertEquals("SP", routesMap.toString());
    }
    
}
