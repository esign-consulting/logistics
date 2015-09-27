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

import br.com.esign.logistics.core.Route;
import br.com.esign.logistics.core.Path;
import br.com.esign.logistics.core.PathFinder;
import br.com.esign.logistics.core.RoutesMap;
import br.com.esign.logistics.core.RouteChooser;
import br.com.esign.logistics.core.Place;
import java.util.List;

/**
 *
 * @author gustavomunizdocarmo
 */
public class RouteChooserImpl implements RouteChooser<ChosenRoute> {
    
    private final RoutesMap routesMap;
    private final PathFinder pathFinder;
    private final double autonomy;
    private final double gasPrice;

    public RouteChooserImpl(RoutesMap routesMap, PathFinder pathFinder, double autonomy, double gasPrice) {
        if (routesMap == null || pathFinder == null || autonomy <= 0 || gasPrice <= 0)
            throw new IllegalArgumentException();
        
        this.routesMap = routesMap;
        this.pathFinder = pathFinder;
        this.autonomy = autonomy;
        this.gasPrice = gasPrice;
    }
    
    public List<Route> findRoutes(String originName, String destinationName) {
        return findRoutes(routesMap.getPlace(originName), routesMap.getPlace(destinationName));
    }
    
    public List<Route> findRoutes(Place origin, Place destination) {
        if (origin == null || !routesMap.containsPlace(origin) ||
            destination == null || !routesMap.containsPlace(destination) ||
            origin.equals(destination))
            throw new IllegalArgumentException();
        
        List<Path> paths = pathFinder.findPaths(origin, destination);
        return routesMap.getRoutes(paths);
    }
    
    public double calculateCost(double distance) {
        return distance / autonomy * gasPrice;
    }
    
    @Override
    public ChosenRoute chooseRoute(String originName, String destinationName) {
        List<Route> routes = findRoutes(originName, destinationName);
        if (routes.isEmpty())
            return null;
        
        Route chosen = null;
        double lowestCost = 0;
        for (Route route : routes) {
            double cost = calculateCost(route.getDistance());
            if (chosen == null || cost < lowestCost) {
                chosen = route;
                lowestCost = cost;
            }
        }
        return new ChosenRoute(chosen.getRoutes(), lowestCost);
    }
    
}
