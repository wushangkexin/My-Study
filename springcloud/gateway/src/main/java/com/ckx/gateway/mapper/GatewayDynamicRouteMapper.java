package com.ckx.gateway.mapper;

import com.ckx.gateway.entity.GatewayDynamicRoute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GatewayDynamicRouteMapper {
    //获取路由列表
    List<GatewayDynamicRoute> getListByEnable(String enable);
    //更新路由禁用状态
    int updateEnable(@Param("routeId")String routeId,@Param("enable")String enable);
    //更新路由
    int updateGatewayDynamicRoute(@Param("entity") GatewayDynamicRoute entity, @Param("routeId") String routeId);
    //新增路由
    int insertGatewayDynamicRoute(GatewayDynamicRoute entity);
}
