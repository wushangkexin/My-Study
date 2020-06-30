package com.ckx.gateway.service;

import com.ckx.gateway.entity.Route;

import java.util.List;

public interface RouteService {
    List<Route> getRouteList();

    String insertRoute(Route route);

    String updateRoute(Route route);

    String deleteRoute(String routeId);
}
