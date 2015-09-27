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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author gustavomunizdocarmo
 */
public class PlaceTest {

    /**
     * Test of getName method, of class Place.
     */
    @Test
    public void testGetName() {
        Place place = new Place("A");
        assertEquals("A", place.getName());
    }

    /**
     * Test of getDestinations method, of class Place.
     */
    @Test
    public void testGetDestinations() {
        Place place = new Place("A");
        assertNotNull(place.getDestinations());
    }

    /**
     * Test of addDestination method, of class Place.
     */
    @Test
    public void testAddDestination() {
        Place placeA = new Place("A");
        Place placeB = new Place("B");
        placeA.addDestination(placeB);
        assertTrue(placeA.getDestinations().contains(placeB));
    }

    /**
     * Test of hashCode method, of class Place.
     */
    @Test
    public void testHashCode() {
        Place place1 = new Place("A");
        Place place2 = new Place("A");
        assertEquals(place1.hashCode(), place2.hashCode());
    }

    /**
     * Test of equals method, of class Place.
     */
    @Test
    public void testEquals() {
        Place place1 = new Place("A");
        Place place2 = new Place("A");
        assertTrue(place1.equals(place2));
    }

    /**
     * Test of toString method, of class Place.
     */
    @Test
    public void testToString() {
        Place place = new Place("A");
        assertEquals("A", place.toString());
    }
    
}
