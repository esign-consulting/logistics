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

import br.com.esign.logistics.core.RouteChooser;
import br.com.esign.logistics.core.Place;
import br.com.esign.logistics.core.RoutesMap;
import br.com.esign.logistics.core.Route;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author gustavomunizdocarmo
 */
public class RouteChooserImplTest {
    
    /**
     * Test of findRoutes method, of class RouteChooserImpl.
     */
    @Test
    public void testFindRoutes_String_String() {
        RoutesMap routesMap = new RoutesMap("SP");
        Route routeAB = routesMap.addRoute("A", "B", 10)[0];
        Route routeBD = routesMap.addRoute("B", "D", 15)[0];
        RouteChooserImpl routeChooser = new RouteChooserImpl(routesMap, new PathFinderImpl(), 10, 2.5);
        Route routeABD = new Route(Arrays.asList(new Route[] {routeAB, routeBD}));
        assertEquals(Arrays.asList(new Route[] {routeABD}), routeChooser.findRoutes("A", "D"));
    }

    /**
     * Test of findRoutes method, of class RouteChooserImpl.
     */
    @Test
    public void testFindRoutes_Place_Place() {
        RoutesMap routesMap = new RoutesMap("SP");
        Place placeA = new Place("A");
        Place placeB = new Place("B");
        Place placeD = new Place("D");
        Route routeAB = routesMap.addRoute(placeA, placeB, 10)[0];
        Route routeBD = routesMap.addRoute(placeB, placeD, 15)[0];
        RouteChooserImpl routeChooser = new RouteChooserImpl(routesMap, new PathFinderImpl(), 10, 2.5);
        Route routeABD = new Route(Arrays.asList(new Route[] {routeAB, routeBD}));
        assertEquals(Arrays.asList(new Route[] {routeABD}), routeChooser.findRoutes(placeA, placeD));
    }

    /**
     * Test of calculateCost method, of class RouteChooserImpl.
     */
    @Test
    public void testCalculateCost() {
        RouteChooserImpl routeChooser = new RouteChooserImpl(new RoutesMap("SP"), new PathFinderImpl(), 10, 2.5);
        assertEquals(25.0 / 10 * 2.5, routeChooser.calculateCost(25), 0);
    }

    /**
     * Test of chooseRoute method, of class RouteChooserImpl.
     */
    @Test
    public void testChooseRoute() {
        RoutesMap routesMap = new RoutesMap("SP");
        Route routeAB = routesMap.addRoute("A", "B", 10)[0];
        Route routeBD = routesMap.addRoute("B", "D", 15)[0];
        Route routeABD = new Route(Arrays.asList(new Route[] {routeAB, routeBD}));
        routesMap.addRoute("A", "C", 20);
        routesMap.addRoute("C", "D", 30);
        routesMap.addRoute("B", "E", 50);
        routesMap.addRoute("D", "E", 30);
        RouteChooser routeChooser = new RouteChooserImpl(routesMap, new PathFinderImpl(), 10, 2.5);
        ChosenRoute chosenRoute = (ChosenRoute) routeChooser.chooseRoute("A", "D");
        assertEquals(routeABD.getRoutes(), chosenRoute.getRoutes());
        assertEquals(6.25, chosenRoute.getCost(), 0);
    }
    
}
