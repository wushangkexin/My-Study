package com.ckx.gateway.controller;

import com.ckx.gateway.route.DynamicRouteServiceImpl;
import com.ckx.gateway.route.DynamicUtil;
import com.ckx.gateway.route.GatewayRouteDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/route")
public class RouteController {
    @Autowired
    private DynamicRouteServiceImpl dynamicRouteService;

    /**
     * 重新加载路由配置
     * @param all
     * @return
     */
    @PostMapping("/load/{all}")
    public String load(@PathVariable("all") Boolean all){
        if (all){
            return this.dynamicRouteService.doLoad();
        }
        return "需要执行部分加载";
    }

    /**
     * 增加路由
     * @param gwdDefinition
     * @return
     */
    @PostMapping("/add")
    public String add(@RequestBody GatewayRouteDefinition gwdDefinition){
        RouteDefinition definition = DynamicUtil.getRouteDefinition(gwdDefinition);
        return this.dynamicRouteService.add(definition);
    }

    /**
     * 删除路由
     * @param id
     * @return
     */
    @PostMapping("/del/{id}")
    public String delete(@PathVariable String id){
        return this.dynamicRouteService.delete(id);
    }

    /**
     * 更新路由
     * @param gwDefinition
     * @return
     */
    @PostMapping("/update")
    public String update(@RequestBody GatewayRouteDefinition gwDefinition){
        RouteDefinition definition = DynamicUtil.getRouteDefinition(gwDefinition);
        return this.dynamicRouteService.update(definition);
    }

}
