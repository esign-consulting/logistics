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
public class RouteTest {
    
    /**
     * Test of getName method, of class Route.
     */
    @Test
    public void testGetName() {
        Place placeA = new Place("A");
        Place placeB = new Place("B");
        Route routeAB = new Route(placeA, placeB, 10);
        assertEquals("A -> B", routeAB.getName());
        
        Place placeC = new Place("C");
        Route routeBC = new Route(placeB, placeC, 10);
        Route routeABC = new Route(Arrays.asList(new Route[] {routeAB, routeBC}));
        assertEquals("A -> B -> C", routeABC.getName());
    }

    /**
     * Test of getOrigin method, of class Route.
     */
    @Test
    public void testGetOrigin() {
        Place placeA = new Place("A");
        Place placeB = new Place("B");
        Route routeAB = new Route(placeA, placeB, 10);
        assertEquals(placeA, routeAB.getOrigin());
        
        Place placeC = new Place("C");
        Route routeBC = new Route(placeB, placeC, 10);
        Route routeABC = new Route(Arrays.asList(new Route[] {routeAB, routeBC}));
        assertEquals(placeA, routeABC.getOrigin());
    }

    /**
     * Test of getDestination method, of class Route.
     */
    @Test
    public void testGetDestination() {
        Place placeA = new Place("A");
        Place placeB = new Place("B");
        Route routeAB = new Route(placeA, placeB, 10);
        assertEquals(placeB, routeAB.getDestination());
        
        Place placeC = new Place("C");
        Route routeBC = new Route(placeB, placeC, 10);
        Route routeABC = new Route(Arrays.asList(new Route[] {routeAB, routeBC}));
        assertEquals(placeC, routeABC.getDestination());
    }

    /**
     * Test of getDistance method, of class Route.
     */
    @Test
    public void testGetDistance() {
        Place placeA = new Place("A");
        Place placeB = new Place("B");
        Route routeAB = new Route(placeA, placeB, 10);
        assertEquals(10, routeAB.getDistance(), 0);
        
        Place placeC = new Place("C");
        Route routeBC = new Route(placeB, placeC, 5);
        Route routeABC = new Route(Arrays.asList(new Route[] {routeAB, routeBC}));
        assertEquals(10 + 5, routeABC.getDistance(), 0);
    }

    /**
     * Test of getRoutes method, of class Route.
     */
    @Test
    public void testGetRoutes() {
        Place placeA = new Place("A");
        Place placeB = new Place("B");
        Route routeAB = new Route(placeA, placeB, 10);
        assertEquals(routeAB, routeAB.getRoutes().get(0));
        
        Place placeC = new Place("C");
        Route routeBC = new Route(placeB, placeC, 10);
        List<Route> routes = Arrays.asList(new Route[] {routeAB, routeBC});
        Route routeABC = new Route(routes);
        assertEquals(routes, routeABC.getRoutes());
    }

    /**
     * Test of hashCode method, of class Route.
     */
    @Test
    public void testHashCode() {
        Place placeA = new Place("A");
        Place placeB = new Place("B");
        Route routeAB1 = new Route(placeA, placeB, 10);
        Route routeAB2 = new Route(placeA, placeB, 10);
        assertEquals(routeAB1.hashCode(), routeAB2.hashCode());
        
        Place placeC = new Place("C");
        Route routeBC1 = new Route(placeB, placeC, 10);
        Route routeBC2 = new Route(placeB, placeC, 10);
        Route routeABC1 = new Route(Arrays.asList(new Route[] {routeAB1, routeBC1}));
        Route routeABC2 = new Route(Arrays.asList(new Route[] {routeAB2, routeBC2}));
        assertEquals(routeABC1.hashCode(), routeABC2.hashCode());
    }

    /**
     * Test of equals method, of class Route.
     */
    @Test
    public void testEquals() {
        Place placeA = new Place("A");
        Place placeB = new Place("B");
        Route routeAB1 = new Route(placeA, placeB, 10);
        Route routeAB2 = new Route(placeA, placeB, 10);
        assertTrue(routeAB1.equals(routeAB2));
        
        Place placeC = new Place("C");
        Route routeBC1 = new Route(placeB, placeC, 10);
        Route routeBC2 = new Route(placeB, placeC, 10);
        Route routeABC1 = new Route(Arrays.asList(new Route[] {routeAB1, routeBC1}));
        Route routeABC2 = new Route(Arrays.asList(new Route[] {routeAB2, routeBC2}));
        assertTrue(routeABC1.equals(routeABC2));
    }

    /**
     * Test of toString method, of class Route.
     */
    @Test
    public void testToString() {
        Place placeA = new Place("A");
        Place placeB = new Place("B");
        Route routeAB = new Route(placeA, placeB, 10);
        assertEquals("A -> B", routeAB.toString());
        
        Place placeC = new Place("C");
        Route routeBC = new Route(placeB, placeC, 10);
        Route routeABC = new Route(Arrays.asList(new Route[] {routeAB, routeBC}));
        assertEquals("A -> B -> C", routeABC.toString());
    }
    
}
