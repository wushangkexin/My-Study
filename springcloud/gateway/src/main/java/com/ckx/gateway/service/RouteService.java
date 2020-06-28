package com.ckx.gateway.service;

import com.ckx.gateway.entity.Route;

import java.util.List;

public interface RouteService {
    List<Route> getRouteList() throws Exception;

    String insertRoute(Route route) throws Exception;

    String updateRoute(Route route) throws Exception;
}
