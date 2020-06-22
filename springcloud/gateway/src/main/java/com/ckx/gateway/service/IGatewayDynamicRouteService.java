package com.ckx.gateway.service;

import com.ckx.gateway.entity.GatewayDynamicRoute;

import java.util.List;

public interface IGatewayDynamicRouteService{
    /**
     * 获取所有 启用的 路由配置信息
     * @param enable
     * @return
     */
    List<GatewayDynamicRoute> getListByEnable(String enable);

    /**
     * 禁用指定 路由Id
     * @param routeId
     * @return
     */
    String disabled(String routeId);

    /**
     * 启用指定路由Id
     * @param routeId
     * @return
     */
    String enable(String routeId);

    /**
     * 设置 指定路由Id的 状态
     * @param routeId
     * @param enable
     * @return
     */
    String updateEnable(String routeId,String enable);

    /**
     * 更新指定的路由配置
     * @param entity
     * @return
     */
    String update(GatewayDynamicRoute entity);

    /**
     * 新增路由配置
     * @param entity
     * @return
     */
    String saveOne(GatewayDynamicRoute entity);

}
