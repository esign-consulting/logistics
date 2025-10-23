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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import jakarta.ejb.EJB;
import jakarta.validation.constraints.DecimalMin;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

/**
 *
 * @author gustavomunizdocarmo
 */
@Path("maps")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Api(tags={"logistics"})
public class RoutesMapWebService {
    
    private static final Logger logger = Logger.getLogger(RoutesMapWebService.class.getName());
    
    private static final String MAP_NOT_FOUND_MSG_PATTERN = "The map identified by \"{0}\" was not found.";
    private static final String ROUTE_NOT_FOUND_MSG_PATTERN = "The route identified by \"{0}\" was not found.";
    
    @EJB
    private RoutesMapEJB ejb;
    
    @Context
    UriInfo uriInfo;
    
    @GET
    @ApiOperation(value="List all maps")
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
    @ApiOperation(value="Get a map")
    public Response getRoutesMap(@PathParam("slug") String slug) {
        try {
            RoutesMap map = ejb.getRoutesMapBySlug(slug);
            if (map == null) {
                return getResponseStatusNotOK(Response.Status.NOT_FOUND, MessageFormat.format(MAP_NOT_FOUND_MSG_PATTERN, slug));
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
    @ApiOperation(value="Create a map")
    public Response createRoutesMap(RoutesMap routesMap) {
        try {
            RoutesMap newRoutesMap = ejb.createRoutesMap(routesMap);
            return getResponseStatusOK(getRoutesMapResource(newRoutesMap, uriInfo.getAbsolutePathBuilder().path(newRoutesMap.getSlug())));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return getResponseStatusNotOK(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    @DELETE
    @Path("{slug}")
    @ApiOperation(value="Remove a map")
    public Response removeRoutesMap(@PathParam("slug") String slug) {
        try {
            ejb.removeRoutesMap(slug);
            return getResponseStatusOK(null);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return getResponseStatusNotOK(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DELETE
    @ApiOperation(value="Remove all maps")
    public Response removeAllMaps() {
        try {
            ejb.removeAllRoutesMaps();
            return getResponseStatusOK(null);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return getResponseStatusNotOK(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    @GET
    @Path("{slug}/routes")
    @ApiOperation(value="List all routes of a map")
    public Response listRoutesByMap(@PathParam("slug") String slug) {
        try {
            RoutesMap map = ejb.getRoutesMapBySlug(slug);
            if (map == null) {
                return getResponseStatusNotOK(Response.Status.NOT_FOUND, MessageFormat.format(MAP_NOT_FOUND_MSG_PATTERN, slug));
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
    @ApiOperation(value="Add a route into a map")
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
    @ApiOperation(value="Get a route of a map")
    public Response getRouteByMap(@PathParam("mapSlug") String mapSlug, @PathParam("routeSlug") String routeSlug) {
        try {
            RoutesMap map = ejb.getRoutesMapBySlug(mapSlug);
            if (map == null) {
                return getResponseStatusNotOK(Response.Status.NOT_FOUND, MessageFormat.format(MAP_NOT_FOUND_MSG_PATTERN, mapSlug));
            } else {
                Optional<Route> optional = map.getRoutes().stream().filter(route -> route.getSlug().equals(routeSlug)).findFirst();
                if (!optional.isPresent()) {
                    return getResponseStatusNotOK(Response.Status.NOT_FOUND, MessageFormat.format(ROUTE_NOT_FOUND_MSG_PATTERN, routeSlug));
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
    @ApiOperation(value="Remove a route from a map")
    public Response removeRouteFromMap(@PathParam("mapSlug") String mapSlug, @PathParam("routeSlug") String routeSlug) {
        try {
            RoutesMap map = ejb.getRoutesMapBySlug(mapSlug);
            if (map == null) {
                return getResponseStatusNotOK(Response.Status.NOT_FOUND, MessageFormat.format(MAP_NOT_FOUND_MSG_PATTERN, mapSlug));
            } else {
                Optional<Route> optional = map.getRoutes().stream().filter(route -> route.getSlug().equals(routeSlug)).findFirst();
                if (!optional.isPresent()) {
                    return getResponseStatusNotOK(Response.Status.NOT_FOUND, MessageFormat.format(ROUTE_NOT_FOUND_MSG_PATTERN, routeSlug));
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
    @ApiOperation(value="Get the best route")
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
