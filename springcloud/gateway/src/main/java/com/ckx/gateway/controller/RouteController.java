package com.ckx.gateway.controller;

import com.alibaba.fastjson.JSONArray;
import com.ckx.gateway.dynamic.DynamicRouteServiceImpl;
import com.ckx.gateway.entity.Route;
import com.ckx.gateway.entity.RouteVo;
import com.ckx.gateway.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/route")
@RestController
public class RouteController {
    @Autowired
    private RouteService routeService;
    @Autowired
    private DynamicRouteServiceImpl dynamicRouteService;

    @GetMapping("/getRouteList")
    public List<Route> getRouteList() throws Exception{
        return routeService.getRouteList();
    }

    @PostMapping("/insertRoute")
    public String insertRoute(@RequestBody RouteVo.RouteBean route) throws Exception{
        Route r = new Route();
        r.setRouteId(route.getRouteId());
        r.setUri(route.getUri());
        r.setSortIndex(route.getSortIndex());
        r.setPredicates(JSONArray.toJSON(route.getPredicates()).toString());
        r.setFilters(JSONArray.toJSON(route.getFilters()).toString());
        r.setFlag(route.getFlag());
        return routeService.insertRoute(r);
    }

    @PostMapping("/updateRoute")
    public String updateRoute(@RequestBody RouteVo.RouteBean route) throws Exception{
        Route r = new Route();
        r.setRouteId(route.getRouteId());
        r.setUri(route.getUri());
        r.setSortIndex(route.getSortIndex());
        r.setPredicates(JSONArray.toJSON(route.getPredicates()).toString());
        r.setFilters(JSONArray.toJSON(route.getFilters()).toString());
        r.setFlag(route.getFlag());
        return routeService.updateRoute(r);
    }
}
