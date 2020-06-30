package com.ckx.gateway.controller;

import com.ckx.gateway.dynamic.DynamicRouteServiceImpl;
import com.ckx.gateway.dynamic.DynamicUtil;
import com.ckx.gateway.dynamic.GatewayRouteDefinition;
import com.ckx.gateway.entity.Route;
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

    /**
     * 重新加载路由配置
     * @return
     */
    @PostMapping("/loadMysql")
    public String load(){
        return dynamicRouteService.loadMysql();
    }

    /**
     * 新增路由配置
     * @param gatewayRouteDefinition
     * @return
     */
    @PostMapping("/insertRoute")
    public String insertRoute(@RequestBody GatewayRouteDefinition gatewayRouteDefinition) {
        dynamicRouteService.add(DynamicUtil.getRouteDefinition(gatewayRouteDefinition));
        return routeService.insertRoute(DynamicUtil.getRoute(gatewayRouteDefinition));
    }

    /**
     * 修改路由配置
     * @param gatewayRouteDefinition
     * @return
     */
    @PostMapping("/updateRoute")
    public String updateRoute(@RequestBody GatewayRouteDefinition gatewayRouteDefinition) {
        dynamicRouteService.update(DynamicUtil.getRouteDefinition(gatewayRouteDefinition));
        return routeService.updateRoute(DynamicUtil.getRoute(gatewayRouteDefinition));
    }

    /**
     * 删除路由配置
     * @param id
     * @return
     */
    @PostMapping("/deleteRoute/{id}")
    public String deleteRoute(@PathVariable String id) {
        dynamicRouteService.delete(id);
        return routeService.deleteRoute(id);
    }
}
