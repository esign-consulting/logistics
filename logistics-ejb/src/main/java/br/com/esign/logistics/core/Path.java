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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * 
 * @author gustavomunizdocarmo
 */
public class Path implements Iterable<Place[]> {
	
    private final List<Place> placesList = new ArrayList<>();

    public void addPlace(Place place) {
        if (placesList.contains(place))
            throw new IllegalArgumentException();
        placesList.add(place);
    }

    public List<Place> getPath() {
        return placesList;
    }

    public void addPath(Path path) {
        if (path == null)
            throw new IllegalArgumentException();
        path.getPath().forEach(this::addPlace);
    }

    public Path copy() {
        Path copy = new Path();
        copy.addPath(this);
        return copy;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.placesList);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Path other = (Path) obj;
        return Objects.equals(this.placesList, other.placesList);
    }

    @Override
    public String toString() {
        return placesList.toString();
    }

    @Override
    public Iterator<Place[]> iterator() {
        return (placesList.size() > 1) ? new PathIterator() : null;
    }
    
    private class PathIterator implements Iterator<Place[]> {
        
        private int i = 0;
        private int j = 1;
        private final int size;

        private PathIterator() {
            this.size = placesList.size();
        }

        @Override
        public boolean hasNext() {
            return j < size;
        }

        @Override
        public Place[] next() {
            if (j == size)
                throw new NoSuchElementException();
            return new Place[] {placesList.get(i ++), placesList.get(j ++)};
        }
        
    }
	
}