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
package br.com.esign.logistics.ejb.impl;

import br.com.esign.logistics.core.PathFinder;
import br.com.esign.logistics.core.Place;
import br.com.esign.logistics.core.Route;
import br.com.esign.logistics.core.RouteChooser;
import br.com.esign.logistics.core.RoutesMap;
import br.com.esign.logistics.core.impl.RouteChooserImpl;
import br.com.esign.logistics.dao.RoutesMapDAO;
import br.com.esign.logistics.ejb.RoutesMapEJB;
import com.github.slugify.Slugify;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author gustavomunizdocarmo
 */
@Stateless
public class RoutesMapEJBImpl implements RoutesMapEJB {
    
    @EJB
    private RoutesMapDAO dao;
    
    @Inject
    private PathFinder pathFinder;
    
    @Override
    public List<RoutesMap> listRoutesMaps() {
        try {
            return dao.listRoutesMaps();
        } catch (Exception e) {
            throw new EJBException("Error on maps retrieving.", e);
        }
    }
    
    @Override
    public RoutesMap getRoutesMapBySlug(String slug) {
        if (slug == null || slug.isEmpty())
            throw new EJBException("The slug is required.");
        try {
            return dao.getRoutesMapBySlug(slug);
        } catch (Exception e) {
            throw new EJBException("Error on map retrieving.", e);
        }
    }

    @Override
    public RoutesMap createRoutesMap(RoutesMap routesMap) {
        String name = routesMap.getName();
        if (name == null || name.isEmpty())
            throw new EJBException("The name of the map is required.");
        if (dao.getRoutesMapByName(name) != null)
            throw new EJBException("The map already exists.");
        try {
            Slugify slugify = new Slugify();
            routesMap.setSlug(slugify.slugify(name));
        } catch (Exception e) {
            throw new EJBException("Error on map name slugifing.", e);
        }
        try {
            dao.saveRoutesMap(routesMap);
        } catch (Exception e) {
            throw new EJBException("Error on map creation.", e);
        }
        return routesMap;
    }
    
    @Override
    public void removeRoutesMap(String slug) {
        RoutesMap routesMap = getRoutesMapBySlug(slug);
        if (routesMap == null)
            throw new EJBException("The map was not found.");
        try {
            dao.removeRoutesMap(routesMap);
        } catch (Exception e) {
            throw new EJBException("Error on map removing.", e);
        }
    }

    @Override
    public Route[] addRouteToMap(String slug, Route route) {
        RoutesMap routesMap = getRoutesMapBySlug(slug);
        if (routesMap == null)
            throw new EJBException("The map was not found.");
        if (route == null || route.getOrigin() == null || route.getDestination() == null || route.getDistance() <= 0)
            throw new EJBException("The route must be properly defined.");
        if (routesMap.containsRoute(route))
            throw new EJBException("The route already exists.");
        
        Place origin = route.getOrigin();
        Place destination = route.getDestination();
        double distance = route.getDistance();
        
        addPlaceToMap(routesMap, origin);
        addPlaceToMap(routesMap, destination);
        
        route = addRouteToMap(routesMap, origin, destination, distance);
        Route oppositeRoute = addRouteToMap(routesMap, destination, origin, distance);
        
        return new Route[] {route, oppositeRoute};
    }
    
    private void addPlaceToMap(RoutesMap routesMap, Place place) {
        if (!routesMap.containsPlace(place)) {
            try {
                dao.addPlaceToMap(routesMap, place);
                routesMap.addPlace(place);
            } catch (Exception e) {
                throw new EJBException("Error on place adding.", e);
            }
        }
    }
    
    private Route addRouteToMap(RoutesMap routesMap, Place origin, Place destination, double distance) {
        Route route = routesMap.getRoute(origin, destination);
        if (route == null) {
            try {
                route = new Route(origin, destination, distance);
            } catch (Exception e) {
                throw new EJBException("Error on route creation.", e);
            }
            try {
                Slugify slugify = new Slugify();
                route.setSlug(slugify.slugify(route.getName()));
            } catch (Exception e) {
                throw new EJBException("Error on route name slugifing.", e);
            }
            try {
                dao.addRouteToMap(routesMap, route);
                routesMap.addRoute(route);
            } catch (Exception e) {
                throw new EJBException("Error on route adding.", e);
            }
        }
        return route;
    }
    
    @Override
    public void removeRouteFromMap(String slug, Route route) {
        RoutesMap routesMap = getRoutesMapBySlug(slug);
        if (routesMap == null)
            throw new EJBException("The map was not found.");
        if (route == null || route.getOrigin() == null || route.getDestination() == null || route.getName() == null)
            throw new EJBException("The route is not valid.");
        if (!routesMap.containsRoute(route))
            throw new EJBException("The route was not found.");
        
        removeRouteFromMap(routesMap, route);
        removeRouteFromMap(routesMap, route.opposite());
    }
    
    private void removeRouteFromMap(RoutesMap routesMap, Route route) {
        if (routesMap.containsRoute(route)) {
            try {
                dao.removeRouteFromMap(routesMap, route);
                routesMap.removeRoute(route);
            } catch (Exception e) {
                throw new EJBException("Error on route removing.", e);
            }
        }
    }

    @Override
    public Route getBestRoute(String slug, String originName, String destinationName, double autonomy, double gasPrice) {
        RoutesMap routesMap = getRoutesMapBySlug(slug);
        if (routesMap == null)
            throw new EJBException("The map was not found.");
        
        try {
            routesMap.linkPlaces();
            RouteChooser routeChooser = new RouteChooserImpl(routesMap, pathFinder, autonomy, gasPrice);
            return routeChooser.chooseRoute(originName, destinationName);
        } catch (Exception e) {
            throw new EJBException("Error on best route obtaining.", e);
        }
    }
    
}
