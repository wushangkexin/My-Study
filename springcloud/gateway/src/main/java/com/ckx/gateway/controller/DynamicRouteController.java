package com.ckx.gateway.controller;

import com.ckx.gateway.dynamic.DynamicRouteServiceImpl;
import com.ckx.gateway.dynamic.DynamicUtil;
import com.ckx.gateway.dynamic.GatewayRouteDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/dynamicRoute")
@RestController
public class DynamicRouteController {
    @Autowired
    private DynamicRouteServiceImpl dynamicRouteService;

    /**
     * 重新加载路由配置
     * @return
     */
    @PostMapping("/load")
    public String load(){
        return dynamicRouteService.doLoad();
    }

    /**
     * 增加路由
     * @param gwdDefinition
     * @return
     */
    @PostMapping("/add")
    public String add(@RequestBody GatewayRouteDefinition gwdDefinition){
        RouteDefinition definition = DynamicUtil.getRouteDefinition(gwdDefinition);
        return dynamicRouteService.add(definition);
    }

    /**
     * 更新路由
     * @param gwDefinition
     * @return
     */
    @PostMapping("/update")
    public String update(@RequestBody GatewayRouteDefinition gwDefinition){
        RouteDefinition definition = DynamicUtil.getRouteDefinition(gwDefinition);
        return dynamicRouteService.update(definition);
    }

    /**
     * 删除路由
     * @param id
     * @return
     */
    @PostMapping("/del/{id}")
    public String delete(@PathVariable String id){
        return dynamicRouteService.delete(id);
    }
}
