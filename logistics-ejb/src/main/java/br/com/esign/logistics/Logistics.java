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
package br.com.esign.logistics;

import br.com.esign.logistics.core.impl.ChosenRoute;
import br.com.esign.logistics.core.impl.PathFinderImpl;
import br.com.esign.logistics.core.impl.RouteChooserImpl;
import br.com.esign.logistics.core.RoutesMap;
import br.com.esign.logistics.core.RouteChooser;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Logistics {

    private static final Logger LOGGER = Logger.getLogger(Logistics.class.getName());
    
    public static void main(String[] args) {
            RoutesMap routesMap = new RoutesMap("SP");
            routesMap.addRoute("A", "B", 10);
            routesMap.addRoute("B", "D", 15);
            routesMap.addRoute("A", "C", 20);
            routesMap.addRoute("C", "D", 30);
            routesMap.addRoute("B", "E", 50);
            routesMap.addRoute("D", "E", 30);

            RouteChooser<ChosenRoute> routeChooser = new RouteChooserImpl(routesMap, new PathFinderImpl(), 10, 2.5);
            ChosenRoute chosenRoute = routeChooser.chooseRoute("A", "D");
            LOGGER.log(Level.INFO, "{0}: {1}", new Object[] {chosenRoute, chosenRoute.getCost()});
    }

}
