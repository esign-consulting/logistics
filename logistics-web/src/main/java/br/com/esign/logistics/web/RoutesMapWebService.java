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
package br.com.esign.logistics.web;

import br.com.esign.logistics.web.response.WebServiceBadResponse;
import br.com.esign.logistics.web.response.WebServiceResponse;
import br.com.esign.logistics.core.Route;
import br.com.esign.logistics.web.resource.RoutesMapResource;
import br.com.esign.logistics.web.resource.SelfLink;
import br.com.esign.logistics.core.RoutesMap;
import br.com.esign.logistics.ejb.RoutesMapEJB;
import br.com.esign.logistics.web.resource.RouteResource;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.validation.constraints.DecimalMin;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author gustavomunizdocarmo
 */
@Path("maps")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class RoutesMapWebService {
    
    private static final Logger logger = Logger.getLogger(RoutesMapWebService.class.getName());
    
    @EJB
    private RoutesMapEJB ejb;
    
    @Context
    UriInfo uriInfo;
    
    @GET
    public Response listRoutesMaps() {
        try {
            return getResponseStatusOK(ejb.listRoutesMaps().stream().map(map -> getRoutesMapResource(map, uriInfo.getAbsolutePathBuilder().path(map.getSlug()))).collect(Collectors.toList()));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return getResponseStatusNotOK(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    @GET
    @Path("{slug}")
    public Response getRoutesMap(@PathParam("slug") String slug) {
        try {
            RoutesMap map = ejb.getRoutesMapBySlug(slug);
            if (map == null) {
                return getResponseStatusNotOK(Response.Status.NOT_FOUND, "The map identified by '" + slug + "' was not found.");
            } else {
                return getResponseStatusOK(getRoutesMapResource(map, uriInfo.getAbsolutePathBuilder()));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return getResponseStatusNotOK(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createRoutesMap(RoutesMap routesMap) {
        try {
            routesMap = ejb.createRoutesMap(routesMap);
            return getResponseStatusOK(getRoutesMapResource(routesMap, uriInfo.getAbsolutePathBuilder().path(routesMap.getSlug())));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return getResponseStatusNotOK(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    @DELETE
    @Path("{slug}")
    public Response removeRoutesMap(@PathParam("slug") String slug) {
        try {
            ejb.removeRoutesMap(slug);
            return getResponseStatusOK(null);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return getResponseStatusNotOK(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    @GET
    @Path("{slug}/routes")
    public Response listRoutesByMap(@PathParam("slug") String slug) {
        try {
            RoutesMap map = ejb.getRoutesMapBySlug(slug);
            if (map == null) {
                return getResponseStatusNotOK(Response.Status.NOT_FOUND, "The map identified by '" + slug + "' was not found.");
            } else {
                return getResponseStatusOK(map.getRoutes().stream().map(route -> getRouteResource(route, uriInfo.getAbsolutePathBuilder().path(route.getSlug()))).collect(Collectors.toList()));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return getResponseStatusNotOK(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    @POST
    @Path("{slug}/routes")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRouteToMap(@PathParam("slug") String slug, Route route) {
        try {
            Route[] routes = ejb.addRouteToMap(slug, route);
            RouteResource[] routeResources = new RouteResource[] {
                getRouteResource(routes[0], uriInfo.getAbsolutePathBuilder().path(routes[0].getSlug())),
                getRouteResource(routes[1], uriInfo.getAbsolutePathBuilder().path(routes[1].getSlug()))
            };
            return getResponseStatusOK(Arrays.asList(routeResources));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return getResponseStatusNotOK(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    @GET
    @Path("{mapSlug}/routes/{routeSlug}")
    public Response getRouteByMap(@PathParam("mapSlug") String mapSlug, @PathParam("routeSlug") String routeSlug) {
        try {
            RoutesMap map = ejb.getRoutesMapBySlug(mapSlug);
            if (map == null) {
                return getResponseStatusNotOK(Response.Status.NOT_FOUND, "The map identified by '" + mapSlug + "' was not found.");
            } else {
                Optional<Route> optional = map.getRoutes().stream().filter(route -> route.getSlug().equals(routeSlug)).findFirst();
                if (!optional.isPresent()) {
                    return getResponseStatusNotOK(Response.Status.NOT_FOUND, "The route identified by '" + routeSlug + "' was not found.");
                } else {
                    return getResponseStatusOK(getRouteResource(optional.get(), uriInfo.getAbsolutePathBuilder()));
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return getResponseStatusNotOK(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    @DELETE
    @Path("{mapSlug}/routes/{routeSlug}")
    public Response removeRouteFromMap(@PathParam("mapSlug") String mapSlug, @PathParam("routeSlug") String routeSlug) {
        try {
            RoutesMap map = ejb.getRoutesMapBySlug(mapSlug);
            if (map == null) {
                return getResponseStatusNotOK(Response.Status.NOT_FOUND, "The map identified by '" + mapSlug + "' was not found.");
            } else {
                Optional<Route> optional = map.getRoutes().stream().filter(route -> route.getSlug().equals(routeSlug)).findFirst();
                if (!optional.isPresent()) {
                    return getResponseStatusNotOK(Response.Status.NOT_FOUND, "The route identified by '" + routeSlug + "' was not found.");
                } else {
                    ejb.removeRouteFromMap(mapSlug, optional.get());
                    return getResponseStatusOK(null);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return getResponseStatusNotOK(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    @GET
    @Path("{mapSlug}/bestRoute")
    public Response getBestRoute(@PathParam("mapSlug") String mapSlug,
            @QueryParam("originName") String originName,
            @QueryParam("destinationName") String destinationName,
            @DecimalMin("0.001") @QueryParam("autonomy") double autonomy,
            @DecimalMin("0.01") @QueryParam("gasPrice") double gasPrice) {
        try {
            return getResponseStatusOK(ejb.getBestRoute(mapSlug, originName, destinationName, autonomy, gasPrice));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return getResponseStatusNotOK(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    private RoutesMapResource getRoutesMapResource(RoutesMap map, UriBuilder uriBuilder) {
        return (map == null) ? null : new RoutesMapResource(map,
            new SelfLink(uriBuilder.build().toASCIIString()));
    }
    
    private RouteResource getRouteResource(Route route, UriBuilder uriBuilder) {
        return (route == null) ? null : new RouteResource(route,
            new SelfLink(uriBuilder.build().toASCIIString()));
    }
    
    private Response getResponseStatusOK(Object data) {
        Status status = Response.Status.OK;
        return Response
            .status(status)
            .entity(new WebServiceResponse(status.getStatusCode(), data))
            .build();
    }
    
    private Response getResponseStatusNotOK(Status status, String message) {
        return Response
            .status(status)
            .entity(new WebServiceBadResponse(status.getStatusCode(), message))
            .build();
    }
    
}
