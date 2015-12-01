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

import br.com.esign.logistics.core.Path;
import br.com.esign.logistics.core.PathFinder;
import br.com.esign.logistics.core.Place;
import br.com.esign.logistics.core.Route;
import br.com.esign.logistics.core.RoutesMap;
import br.com.esign.logistics.core.impl.ChosenRoute;
import br.com.esign.logistics.dao.RoutesMapDAO;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author gustavomunizdocarmo
 */
@RunWith(MockitoJUnitRunner.class)
public class RoutesMapEJBImplTest {
    
    private final String MAP_NAME = "São Paulo";
    private final String MAP_SLUG = "sao-paulo";
    private final Place PLACEA = new Place("A");
    private final Place PLACEB = new Place("B");
    private final Place PLACEC = new Place("C");
    private final Route ROUTE1 = new Route(PLACEA, PLACEB, 10);
    private final Route ROUTE2 = new Route(PLACEB, PLACEC, 15);
    
    @InjectMocks
    private RoutesMapEJBImpl ejb;
    
    @Mock
    private RoutesMapDAO dao;
    
    @Mock
    private PathFinder pathFinder;
    
    /**
     * Test of getRoutesMapBySlug method, of class RoutesMapEJBImpl.
     */
    @Test
    public void testGetRoutesMapBySlug() {
        RoutesMap routesMap = new RoutesMap(MAP_NAME);
        Mockito.when(dao.getRoutesMapBySlug(MAP_SLUG)).thenReturn(routesMap);
        assertNotNull(routesMap);
    }
    
    /**
     * Test of createRoutesMap method, of class RoutesMapEJBImpl.
     */
    @Test
    public void testCreateRoutesMap() {
        RoutesMap inputRoutesMap = new RoutesMap(MAP_NAME);
        Mockito.doNothing().when(dao).saveRoutesMap(inputRoutesMap);
        RoutesMap outputRoutesMap = ejb.createRoutesMap(inputRoutesMap);
        assertNotNull(outputRoutesMap);
        assertNotNull(outputRoutesMap.getSlug());
        assertEquals(inputRoutesMap, outputRoutesMap);
    }

    /**
     * Test of removeRoutesMap method, of class RoutesMapEJBImpl.
     */
    @Test
    public void testRemoveRoutesMap() {
        RoutesMap routesMap = new RoutesMap(MAP_NAME);
        Mockito.when(dao.getRoutesMapBySlug(MAP_SLUG)).thenReturn(routesMap);
        Mockito.doNothing().when(dao).removeRoutesMap(routesMap);
        ejb.removeRoutesMap(MAP_SLUG);
    }

    /**
     * Test of addRouteToMap method, of class RoutesMapEJBImpl.
     */
    @Test
    public void testAddRouteToMap() {
        RoutesMap routesMap = new RoutesMap(MAP_NAME);
        Mockito.when(dao.getRoutesMapBySlug(MAP_SLUG)).thenReturn(routesMap);
        Mockito.doNothing().when(dao).addPlaceToMap(routesMap, PLACEA);
        Mockito.doNothing().when(dao).addPlaceToMap(routesMap, PLACEB);
        Mockito.doNothing().when(dao).addRouteToMap(routesMap, ROUTE1);
        Route[] routes = ejb.addRouteToMap(MAP_SLUG, ROUTE1);
        assertNotNull(routes);
        assertEquals(2, routes.length);
        assertArrayEquals(new Route[] {ROUTE1, ROUTE1.opposite()}, routes);
    }

    /**
     * Test of removeRouteFromMap method, of class RoutesMapEJBImpl.
     */
    @Test
    public void testRemoveRouteFromMap() {
        RoutesMap routesMap = new RoutesMap(MAP_NAME);
        routesMap.addRoute(ROUTE1);
        Mockito.when(dao.getRoutesMapBySlug(MAP_SLUG)).thenReturn(routesMap);
        Mockito.doNothing().when(dao).removeRouteFromMap(routesMap, ROUTE1);
        ejb.removeRouteFromMap(MAP_SLUG, ROUTE1);
        assertFalse(routesMap.containsRoute(ROUTE1));
        assertFalse(routesMap.containsRoute(ROUTE1.opposite()));
    }

    /**
     * Test of getBestRoute method, of class RoutesMapEJBImpl.
     */
    @Test
    public void testGetBestRoute() {
        RoutesMap routesMap = new RoutesMap(MAP_NAME);
        routesMap.addPlace(PLACEA);
        routesMap.addPlace(PLACEB);
        routesMap.addPlace(PLACEC);
        routesMap.addRoute(ROUTE1);
        routesMap.addRoute(ROUTE1.opposite());
        routesMap.addRoute(ROUTE2);
        routesMap.addRoute(ROUTE2.opposite());
        Mockito.when(dao.getRoutesMapBySlug(MAP_SLUG)).thenReturn(routesMap);
        Path path = new Path();
        path.addPlace(PLACEA);
        path.addPlace(PLACEB);
        path.addPlace(PLACEC);
        Mockito.when(pathFinder.findPaths(PLACEA, PLACEC)).thenReturn(Arrays.asList(new Path[] {path}));
        ChosenRoute chosenRoute = (ChosenRoute) ejb.getBestRoute(MAP_SLUG, PLACEA.getName(), PLACEC.getName(), 10, 2.5);
        assertEquals(Arrays.asList(new Route[] {ROUTE1, ROUTE2}), chosenRoute.getRoutes());
        assertEquals(6.25, chosenRoute.getCost(), 0);
    }
    
}
