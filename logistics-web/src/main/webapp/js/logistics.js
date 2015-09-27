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

angular.module('logistics', ['ui.bootstrap'])
    .controller('RoutesMapController', function($scope, $http, $modal) {
        
        $scope.alerts = [];
        $scope.maps = [];
        
        $http.get('api/maps')
            .then(function(response) {
                if (response.data.data.length > 0) {
                    $scope.alerts.push({type: 'success', msg: response.data.data.length + ' map(s) successfully retrieved.'});
                    $scope.maps = response.data.data;
                } else {
                    $scope.alerts.push({msg: 'There are no maps. Please add the first one.'});
                }
            }, error);
        
        $scope.closeAlert = function(index) {
            $scope.alerts.splice(index, 1);
        };
        
        $scope.openAddMapModal = function() {
            
            var modalInstance = $modal.open({
                animation: true,
                templateUrl: 'addMapModal.html',
                controller: 'AddMapModalController',
                size: 'sm'
            });
            
            modalInstance.result.then(function(mapName) {
                
                $scope.alerts = [];
                
                $http.post('api/maps', {name: mapName})
                    .then(function(response) {
                        $scope.alerts.push({type: 'success', msg: 'The map \'' + response.data.data.name + '\' was successfully created.'});
                        $scope.maps.push(response.data.data);
                    }, error);
                
            });
            
        };
        
        $scope.removeMap = function(map) {
            
            var modalInstance = $modal.open({
                animation: true,
                templateUrl: 'removeMapModal.html',
                controller: 'RemoveMapModalController',
                resolve: {
                    map: function() {
                        return map;
                    }
                }
            });
            
            modalInstance.result.then(function(map) {
                
                $scope.alerts = [];
                
                $http.delete('api/maps/' + map.slug)
                    .then(function(response) {
                        $scope.alerts.push({type: 'success', msg: 'The map \'' + map.name + '\' was successfully removed.'});
                        $scope.maps.splice($scope.maps.indexOf(map), 1);
                    }, error);
                
            });
            
        };
        
        $scope.openAddRouteModal = function(map) {
            
            var modalInstance = $modal.open({
                animation: true,
                templateUrl: 'addRouteModal.html',
                controller: 'AddRouteModalController',
                resolve: {
                    map: function() {
                        return map;
                    }
                }
            });
            
            modalInstance.result.then(function(result) {
                
                $scope.alerts = [];
                
                $http.post('api/maps/' + map.slug + '/routes', {origin: {name: result.originName}, destination: {name: result.destinationName}, distance: result.distance})
                    .then(function(response) {
                        $scope.alerts.push({type: 'success', msg: 'The routes \'' + response.data.data[0].name + '\' and \'' + response.data.data[1].name + '\' were successfully created.'});
                        map.routes.push(response.data.data[0]);
                        map.routes.push(response.data.data[1]);
                    }, error);
                
            });
            
        };
        
        $scope.removeRoute = function(map, route) {
            
            var modalInstance = $modal.open({
                animation: true,
                templateUrl: 'removeRouteModal.html',
                controller: 'RemoveRouteModalController',
                resolve: {
                    map: function() {
                        return map;
                    },
                    route: function() {
                        return route;
                    }
                }
            });
            
            modalInstance.result.then(function(result) {
                
                $scope.alerts = [];

                $http.delete('api/maps/' + result.map.slug + '/routes/' + result.route.slug)
                    .then(function(response) {

                        var oppositeRouteName = result.route.destination.name + " -> " + result.route.origin.name;
                        $scope.alerts.push({type: 'success', msg: 'The routes \'' + result.route.name + '\' and \'' + oppositeRouteName + '\' were successfully removed.'});
                        
                        var map = $scope.maps[$scope.maps.indexOf(result.map)];
                        map.routes.splice(map.routes.indexOf(result.route), 1);

                        var oppositeRouteIndex = -1;
                        for (i = map.routes.length - 1; i >= 0; i --) {
                            if (map.routes[i].name === oppositeRouteName) {
                                oppositeRouteIndex = i;
                                break;
                            }
                        }
                        if (oppositeRouteIndex >= 0)
                            map.routes.splice(oppositeRouteIndex, 1);

                    }, error);
                
            });
            
        };
        
        $scope.openBestRouteModal = function(map) {
            
            var modalInstance = $modal.open({
                animation: true,
                templateUrl: 'bestRouteModal.html',
                controller: 'BestRouteModalController',
                resolve: {
                    map: function() {
                        return map;
                    }
                }
            });
            
            modalInstance.result.then(function(result) {
                
                $scope.alerts = [];
                
                $http.get('api/maps/' + map.slug + '/bestRoute', {params: {originName: result.originName, destinationName: result.destinationName, autonomy: result.autonomy, gasPrice: result.gasPrice}})
                    .then(function(response) {
                        if (response.data.data) {
                            $scope.alerts.push({type: 'success', msg: 'The route \'' + response.data.data.name + '\' is the best, once the cost for delivering is ' + response.data.data.cost + '.'});
                        } else {
                            $scope.alerts.push({msg: 'No result.'});
                        }
                    }, error);
                
            });
            
        };
        
        function error(response) {
            if (response.data.message) {
                $scope.alerts.push({type: 'danger', msg: response.data.message});
            } else {
                $scope.alerts.push({type: 'danger', msg: response.statusText});
            }
        }
        
    });

angular.module('logistics')
    .controller('AddMapModalController', function ($scope, $modalInstance) {
        
        $scope.ok = function() {
            $modalInstance.close($scope.map.name);
        };
        
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
        
    });

angular.module('logistics')
    .controller('RemoveMapModalController', function ($scope, $modalInstance, map) {
        
        $scope.map = map;
        
        $scope.yes = function() {
            $modalInstance.close(map);
        };
        
        $scope.no = function() {
            $modalInstance.dismiss('cancel');
        };
        
    });

angular.module('logistics')
    .controller('AddRouteModalController', function ($scope, $modalInstance, map) {
        
        $scope.map = map;
        
        $scope.ok = function() {
            $modalInstance.close({originName: $scope.route.origin.name, destinationName: $scope.route.destination.name, distance: $scope.route.distance});
        };
        
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
        
    });

angular.module('logistics')
    .controller('RemoveRouteModalController', function ($scope, $modalInstance, map, route) {
        
        $scope.map = map;
        $scope.route = route;
        
        $scope.yes = function() {
            $modalInstance.close({map: map, route: route});
        };
        
        $scope.no = function() {
            $modalInstance.dismiss('cancel');
        };
        
    });

angular.module('logistics')
    .controller('BestRouteModalController', function ($scope, $modalInstance, map) {
        
        $scope.map = map;
        
        $scope.ok = function() {
            $modalInstance.close({originName: $scope.bestRoute.origin.name, destinationName: $scope.bestRoute.destination.name, autonomy: $scope.bestRoute.autonomy, gasPrice: $scope.bestRoute.gasPrice});
        };
        
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
        
    });

angular.module('utils.autofocus', [])
    .directive('autofocus', ['$timeout', function($timeout) {
        return {
            restrict: 'A',
            link: function($scope, $element) {
                $timeout(function() {
                    $element[0].focus();
                });
            }
        };
    }]);
