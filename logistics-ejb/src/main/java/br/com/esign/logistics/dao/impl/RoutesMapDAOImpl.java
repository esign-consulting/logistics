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
import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import dev.morphia.query.Query;
import dev.morphia.query.experimental.filters.Filters;
import dev.morphia.query.experimental.updates.UpdateOperators;

/**
 *
 * @author gustavomunizdocarmo
 */
public class RoutesMapDAOImpl implements RoutesMapDAO {
    
    private Datastore datastore;

    public RoutesMapDAOImpl(Datastore datastore) {
        this.datastore = datastore;
    }
    
    @Override
    public List<RoutesMap> listRoutesMaps() {
        Query<RoutesMap> query = datastore.find(RoutesMap.class);
        return query.iterator().toList();
    }
    
    @Override
    public RoutesMap getRoutesMapByName(String name) {
        Query<RoutesMap> query = datastore.find(RoutesMap.class)
            .filter(Filters.eq("name", name));
        return query.iterator().tryNext();
    }
    
    @Override
    public RoutesMap getRoutesMapBySlug(String slug) {
        Query<RoutesMap> query = datastore.find(RoutesMap.class)
            .filter(Filters.eq("slug", slug));
        return query.iterator().tryNext();
    }
    
    @Override
    public void saveRoutesMap(RoutesMap routesMap) {
        datastore.save(routesMap);
    }

    @Override
    public void removeRoutesMap(String name) {
        datastore.find(RoutesMap.class)
            .filter(Filters.eq("name", name))
            .delete();
    }

    @Override
    public void removeAllRoutesMaps() {
        datastore.find(RoutesMap.class)
            .delete(new DeleteOptions()
            .multi(true));
    }

    @Override
    public void addPlaceToMap(String name, Place place) {
        datastore.find(RoutesMap.class)
            .filter(Filters.eq("name", name))
            .update(UpdateOperators.addToSet("places", place));
    }
    
    @Override
    public void addRouteToMap(String name, Route route) {
        datastore.find(RoutesMap.class)
            .filter(Filters.eq("name", name))
            .update(UpdateOperators.addToSet("routes", route));
    }

    @Override
    public void removeRouteFromMap(String name, Route route) {
        datastore.find(RoutesMap.class)
            .filter(Filters.eq("name", name))
            .update(UpdateOperators.pull("routes", Filters.eq("name", route.getName())));
    }
    
}
