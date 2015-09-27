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
public class PathTest {
    
    /**
     * Test of addPlace method, of class Path.
     */
    @Test
    public void testAddPlace() {
        Place place = new Place("A");
        Path path = new Path();
        path.addPlace(place);
        assertTrue(path.getPath().contains(place));
    }

    /**
     * Test of getPath method, of class Path.
     */
    @Test
    public void testGetPath() {
        Path path = new Path();
        assertNotNull(path.getPath());
    }

    /**
     * Test of addPath method, of class Path.
     */
    @Test
    public void testAddPath() {
        Path firstPath = new Path();
        firstPath.addPlace(new Place("A"));
        firstPath.addPlace(new Place("B"));
        Path secondPath = new Path();
        secondPath.addPath(firstPath);
        assertTrue(secondPath.getPath().containsAll(firstPath.getPath()));
    }

    /**
     * Test of copy method, of class Path.
     */
    @Test
    public void testCopy() {
        Path firstPath = new Path();
        firstPath.addPlace(new Place("A"));
        firstPath.addPlace(new Place("B"));
        Path secondPath = firstPath.copy();
        assertEquals(secondPath.getPath(), firstPath.getPath());
    }

    /**
     * Test of toString method, of class Path.
     */
    @Test
    public void testToString() {
        Path path = new Path();
        path.addPlace(new Place("A"));
        path.addPlace(new Place("B"));
        assertEquals("[A, B]", path.toString());
    }

    /**
     * Test of iterator method, of class Path.
     */
    @Test
    public void testIterator() {
        Path path = new Path();
        assertNull(path.iterator());
        path.addPlace(new Place("A"));
        assertNull(path.iterator());
        path.addPlace(new Place("B"));
        assertNotNull(path.iterator());
    }
    
}
