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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Indexed;

/**
 * Class that represents a map where there are places and routes between them.
 * @author gustavomunizdocarmo
 */
@Entity("routesMap")
public class RoutesMap implements Comparable<RoutesMap> {
    
    @Id
    private final String name;
    
    private final Set<Place> places = new HashSet<>();
    
    private final Set<Route> routes = new HashSet<>();
    
    @Indexed
    private String slug;
    
    private RoutesMap() {
        this.name = null;
    }
    
    public RoutesMap(String name) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException();
        
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public Set<Place> getPlaces() {
        return places;
    }
    
    public Set<Route> getRoutes() {
        return routes;
    }

    public Place getPlace(String name) {
        Optional<Place> optional = places.stream().filter(p -> p.getName().equals(name)).findFirst();
        return (optional.isPresent()) ? optional.get() : null;
    }

    public Route getRoute(String originName, String destinationName) {
        return getRoute(getPlace(originName), getPlace(destinationName));
    }

    public Route getRoute(Place origin, Place destination) {
        Optional<Route> optional = routes.stream().filter(r -> r.getOrigin().equals(origin) && r.getDestination().equals(destination)).findFirst();
        return (optional.isPresent()) ? optional.get() : null;
    }
    
    public Route getRoute(Path path) {
        List<Route> routesList = new ArrayList<>();
        for (Place[] placesArray : path) {
            Route route = getRoute(placesArray[0], placesArray[1]);
            if (route == null)
                throw new IllegalArgumentException();
            routesList.add(route);
        }
        return new Route(routesList);
    }
    
    public List<Route> getRoutes(List<Path> paths) {
        List<Route> routesList = new ArrayList<>();
        paths.forEach(p -> routesList.add(getRoute(p)));
        return routesList;
    }

    public Place addPlace(String name) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException();
        
        Place place = getPlace(name);
        if (place == null)
            place = addPlace(new Place(name));
        
        return place;
    }
    
    public Place addPlace(Place place) {
        if (place == null)
            throw new IllegalArgumentException();
        
        if (!places.contains(place))
            places.add(place);
        
        return place;
    }

    public Route[] addRoute(String originName, String destinationName, double distance) {
        return addRoute(addPlace(originName), addPlace(destinationName), distance);
    }

    public Route[] addRoute(Place origin, Place destination, double distance) {
        if (origin == null || destination == null || origin.equals(destination) || distance <= 0)
            throw new IllegalArgumentException();
        
        addPlace(origin);
        addPlace(destination);
        
        Route route1 = getRoute(origin, destination);
        if (route1 == null)
            route1 = addRoute(new Route(origin, destination, distance));
        
        Route route2 = getRoute(destination, origin);
        if (route2 == null)
            route2 = addRoute(route1.opposite());
        
        return new Route[] {route1, route2};
    }
    
    public Route addRoute(Route route) {
        if (route == null || route.getOrigin() == null || route.getDestination() == null || route.getName() == null)
            throw new IllegalArgumentException();
        
        if (!routes.contains(route))
            routes.add(route);
        
        return route;
    }
    
    public boolean containsPlace(Place place) {
        if (place == null || place.getName() == null)
            return false;
        
        return places.contains(place);
    }
    
    public boolean containsRoute(Route route) {
        if (route == null || route.getOrigin() == null || route.getDestination() == null)
            return false;
        
        return (getRoute(route.getOrigin(), route.getDestination()) != null);
    }
    
    public boolean removePlace(Place place) {
        return places.remove(place);
    }
    
    public boolean removeRoute(Route route) {
        return routes.remove(route);
    }
    
    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
    
    public void linkPlaces() {
        routes.forEach(r -> {
            r.linkPlaces(getPlace(r.getOrigin().getName()), getPlace(r.getDestination().getName()));
            
            Route opposite = r.opposite();
            if (!routes.contains(opposite)) {
                routes.add(opposite);
            }
        });
    }
    
    @Override
    public int compareTo(RoutesMap o) {
        return name.compareTo(o.getName());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.name);
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
        final RoutesMap other = (RoutesMap) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public String toString() {
            return name;
    }
	
}