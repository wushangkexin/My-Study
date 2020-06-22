package com.ckx.gateway.controller;

import com.ckx.gateway.entity.DynamicRouteVo;
import com.ckx.gateway.route.DynamicRouteServiceImpl;
import com.ckx.gateway.service.IGatewayDynamicRouteService;
import com.ckx.gateway.struts.DynamicRouteStruts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gateway-dynamic-route")
public class GatewayDynamicRouteController {
    @Autowired
    private IGatewayDynamicRouteService iGatewayDynamicRouteService;
    @Autowired
    private DynamicRouteServiceImpl dynamicRouteService;

    /**
     * 增加路由
     * @param routeBean
     * @return
     */
    @PostMapping("/add")
    public String add(@RequestBody DynamicRouteVo.RouteBean routeBean){
        iGatewayDynamicRouteService.saveOne(DynamicRouteStruts.INSTANCES.toGatewayDynamicRoute(routeBean));
        return this.dynamicRouteService.doLoad();
    }

    /**
     * 删除路由
     * @param id
     * @return
     */
    @PostMapping("/del/{id}")
    public String delete(@PathVariable String id){
        iGatewayDynamicRouteService.disabled(id);
        return this.dynamicRouteService.doLoad();
    }

    /**
     * 更新路由
     * @param routeBean
     * @return
     */
    @PostMapping("/update")
    public String update(@RequestBody DynamicRouteVo.RouteBean routeBean){
        iGatewayDynamicRouteService.update(DynamicRouteStruts.INSTANCES.toGatewayDynamicRoute(routeBean));
        return this.dynamicRouteService.doLoad();
    }
}
