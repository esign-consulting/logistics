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

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Transient;

/**
 * 
 * @author gustavomunizdocarmo
 */
@Embedded
public class Route implements Comparable<Route> {
    
    private String name;
    
    private Place origin;
    
    private Place destination;
    
    private double distance;
    
    @Transient @JsonIgnore
    private List<Route> routes;
    
    private String slug;
    
    public Route(Place origin, Place destination, double distance) {
        if (origin == null || destination == null || origin.equals(destination) || distance <= 0)
            throw new IllegalArgumentException();
        
        linkPlaces(origin, destination);
        this.distance = distance;
        this.routes = Arrays.asList(this);
    }
    
    public final void linkPlaces(Place origin, Place destination) {
        if (origin == null || destination == null || origin.equals(destination))
            throw new IllegalArgumentException();
        
        this.name = origin + " -> " + destination;
        this.origin = origin;
        this.destination = destination;
        origin.addDestination(destination);
    }

    public Route(List<Route> routes) {
        if (routes == null)
            throw new IllegalArgumentException();
        Iterator<Route> iterator = routes.iterator();
        if (!iterator.hasNext())
            throw new IllegalArgumentException();
        
        Place firstPlace = null;
        Place lastPlace = null;
        StringBuilder nameBuilder = new StringBuilder();
        double distanceSum = 0;
        do {
            Route route = iterator.next();
            if (firstPlace == null) {
                firstPlace = route.getOrigin();
                nameBuilder.append(firstPlace);
            }
            nameBuilder.append(" -> ");
            if (lastPlace != null && !lastPlace.equals(route.getOrigin()))
                throw new IllegalArgumentException();
            lastPlace = route.getDestination();
            nameBuilder.append(lastPlace);
            distanceSum += route.getDistance();
        } while (iterator.hasNext());
        
        this.origin = firstPlace;
        this.destination = lastPlace;
        this.name = nameBuilder.toString();
        this.distance = distanceSum;
        this.routes = routes;
    }
    
    public Route opposite() {
        if (routes == null || routes.isEmpty())
            routes = Arrays.asList(this);
        
        int size = routes.size();
        if (size == 1) {
            return new Route(getDestination(), getOrigin(), getDistance());
            
        } else {
            List<Route> oppositeRoutes = new ArrayList<>(size);
            ListIterator<Route> iterator = routes.listIterator(size);
            while (iterator.hasPrevious()) {
                oppositeRoutes.add(iterator.previous().opposite());
            }
            return new Route(oppositeRoutes);
        }
    }
    
    public String getName() {
        return name;
    }

    public Place getOrigin() {
        return origin;
    }

    public Place getDestination() {
        return destination;
    }

    public double getDistance() {
        return distance;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
    
    @Override
    public int compareTo(Route o) {
        return name.compareTo(o.getName());
    }
    
    public Iterator<Route> iterator() {
        return routes.iterator();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.name);
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
        final Route other = (Route) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return name;
    }
	
}