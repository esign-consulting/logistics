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
package br.com.esign.logistics.dao.impl;

import br.com.esign.logistics.core.Place;
import br.com.esign.logistics.core.Route;
import br.com.esign.logistics.core.RoutesMap;
import br.com.esign.logistics.dao.RoutesMapDAO;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author gustavomunizdocarmo
 */
@Stateless
public class RoutesMapDAOImplAdapter implements RoutesMapDAO {
    
    @Inject
    private DatastoreAdapter datastoreAdapter;
    
    private RoutesMapDAO dao;
    
    @PostConstruct
    public void postConstruct() {
        dao = new RoutesMapDAOImpl(datastoreAdapter.getDatastore());
    }
    
    @Override
    public List<RoutesMap> listRoutesMaps() {
        return dao.listRoutesMaps();
    }

    @Override
    public RoutesMap getRoutesMapByName(String name) {
        return dao.getRoutesMapByName(name);
    }
    
    @Override
    public RoutesMap getRoutesMapBySlug(String slug) {
        return dao.getRoutesMapBySlug(slug);
    }

    @Override
    public void saveRoutesMap(RoutesMap routesMap) {
        dao.saveRoutesMap(routesMap);
    }
    
    @Override
    public void removeRoutesMap(RoutesMap routesMap) {
        dao.removeRoutesMap(routesMap);
    }

    @Override
    public void removeAllRoutesMaps() {
        dao.removeAllRoutesMaps();
    }
    
    @Override
    public void addPlaceToMap(RoutesMap routesMap, Place place) {
        dao.addPlaceToMap(routesMap, place);
    }

    @Override
    public void addRouteToMap(RoutesMap routesMap, Route route) {
        dao.addRouteToMap(routesMap, route);
    }
    
    @Override
    public void removeRouteFromMap(RoutesMap routesMap, Route route) {
        dao.removeRouteFromMap(routesMap, route);
    }
    
    public RoutesMapDAO getDao() {
        return dao;
    }
    
}