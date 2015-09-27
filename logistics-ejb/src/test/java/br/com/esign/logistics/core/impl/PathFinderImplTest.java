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
package br.com.esign.logistics.core.impl;

import br.com.esign.logistics.core.PathFinder;
import br.com.esign.logistics.core.Place;
import br.com.esign.logistics.core.Path;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author gustavomunizdocarmo
 */
public class PathFinderImplTest {
    
    /**
     * Test of findPaths method, of class PathFinderImpl.
     */
    @Test
    public void testFindPaths() {
        Place placeA = new Place("A");
        Place placeB = new Place("B");
        Place placeD = new Place("D");
        placeA.addDestination(placeB);
        placeB.addDestination(placeA);
        placeB.addDestination(placeD);
        placeD.addDestination(placeB);
        Path path = new Path();
        path.addPlace(placeA);
        path.addPlace(placeB);
        path.addPlace(placeD);
        PathFinder pathFinder = new PathFinderImpl();
        assertEquals(Arrays.asList(new Path[] {path}), pathFinder.findPaths(placeA, placeD));
    }
    
}
