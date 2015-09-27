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

import br.com.esign.logistics.core.Path;
import br.com.esign.logistics.core.PathFinder;
import br.com.esign.logistics.core.Place;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;

/**
 * 
 * @author gustavomunizdocarmo
 */
@ApplicationScoped
public class PathFinderImpl implements PathFinder {
	
    @Override
    public List<Path> findPaths(Place origin, Place destination) {
        if (origin == null || destination == null || origin.equals(destination))
            throw new IllegalArgumentException();
        
        return findPossiblePaths(origin, destination, new Path());
    }

    private List<Path> findPossiblePaths(Place origin, Place destination, Path path) {
        List<Path> paths = new ArrayList<>();
        Set<Place> places = origin.getDestinations();
        places.removeAll(path.getPath());
        if (!places.isEmpty()) {
            path.addPlace(origin);
            places.stream().forEach((place) -> {
                Path copy = path.copy();
                if (place.equals(destination)) {
                    copy.addPlace(destination);
                    paths.add(copy);
                } else {
                    List<Path> innerPaths = findPossiblePaths(place, destination, copy);
                    innerPaths.stream().forEach((p) -> {
                        paths.add(p);
                    });
                }
            });
        }
        return paths;
    }

}
