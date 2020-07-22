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
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.List;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

/**
 *
 * @author gustavomunizdocarmo
 */
public class RoutesMapDAOImpl extends BasicDAO<RoutesMap, String> implements RoutesMapDAO {
    
    public RoutesMapDAOImpl(Datastore datastore) {
        super(datastore);
    }
    
    @Override
    public List<RoutesMap> listRoutesMaps() {
        return find().asList();
    }
    
    @Override
    public RoutesMap getRoutesMapByName(String name) {
        return get(name);
    }
    
    @Override
    public RoutesMap getRoutesMapBySlug(String slug) {
        return findOne("slug", slug);
    }
    
    @Override
    public void saveRoutesMap(RoutesMap routesMap) {
        save(routesMap);
    }

    @Override
    public void removeRoutesMap(RoutesMap routesMap) {
        delete(routesMap);
    }

    @Override
    public void removeAllRoutesMaps() {
        deleteByQuery(createQuery());
    }

    @Override
    public void addPlaceToMap(RoutesMap routesMap, Place place) {
        Query<RoutesMap> updateQuery = createQuery().field(Mapper.ID_KEY).equal(routesMap.getName());
        UpdateOperations<RoutesMap> ops = createUpdateOperations().addToSet("places", place);
        update(updateQuery, ops);
    }
    
    @Override
    public void addRouteToMap(RoutesMap routesMap, Route route) {
        DBObject origin = new BasicDBObject("name", route.getOrigin().getName());
        DBObject destination = new BasicDBObject("name", route.getDestination().getName());
        DBObject routeToAdd = new BasicDBObject("name", route.getName())
            .append("name", route.getName())
            .append("origin", origin)
            .append("destination", destination)
            .append("distance", route.getDistance())
            .append("slug", route.getSlug());
        DBObject routes = new BasicDBObject("routes", routeToAdd);
        DBObject routesMapToUpdate = new BasicDBObject(Mapper.ID_KEY, routesMap.getName());
        DBObject pushOperation = new BasicDBObject("$push", routes);
        getCollection().update(routesMapToUpdate, pushOperation);
    }

    @Override
    public void removeRouteFromMap(RoutesMap routesMap, Route route) {
        DBObject routeToRemove = new BasicDBObject("name", route.getName());
        DBObject routes = new BasicDBObject("routes", routeToRemove);
        DBObject routesMapToUpdate = new BasicDBObject(Mapper.ID_KEY, routesMap.getName());
        DBObject pullOperation = new BasicDBObject("$pull", routes);
        getCollection().update(routesMapToUpdate, pullOperation);
    }
    
}
