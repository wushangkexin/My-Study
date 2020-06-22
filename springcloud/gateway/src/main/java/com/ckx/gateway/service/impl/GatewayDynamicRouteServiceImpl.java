package com.ckx.gateway.service.impl;

import com.alibaba.fastjson.JSON;
import com.ckx.gateway.entity.GatewayDynamicRoute;
import com.ckx.gateway.mapper.GatewayDynamicRouteMapper;
import com.ckx.gateway.route.MysqlRouteDefinitionRepository;
import com.ckx.gateway.service.IGatewayDynamicRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class GatewayDynamicRouteServiceImpl implements IGatewayDynamicRouteService {
    @Autowired
    private GatewayDynamicRouteMapper gatewayDynamicRouteMapper;
    //@Autowired
    //private RedisService redisService;

    private final static long userId = 1L;

    /**
     * 获取所有 启用的 路由配置信息
     * @param enable
     * @return
     */
    @Override
    public List<GatewayDynamicRoute> getListByEnable(String enable) {
        return gatewayDynamicRouteMapper.getListByEnable(enable);
    }

    /**
     * 禁用指定 路由Id
     * @param routeId
     * @return
     */
    @Override
    public String disabled(String routeId) {
        int i = gatewayDynamicRouteMapper.updateEnable(routeId, "0");
        return "success";
    }

    /**
     * 启用指定路由Id
     * @param routeId
     * @return
     */
    @Override
    public String enable(String routeId) {
        int i = gatewayDynamicRouteMapper.updateEnable(routeId, "0");
        return "success";
    }

    /**
     * 设置 指定路由Id的 状态
     * @param routeId
     * @param enable
     * @return
     */
    @Override
    public String updateEnable(String routeId, String enable) {
        GatewayDynamicRoute entity = new GatewayDynamicRoute();
        entity.setEnable(enable);
        int i = gatewayDynamicRouteMapper.updateGatewayDynamicRoute(entity, routeId);
        return "success";
    }

    /**
     * 更新指定的路由配置
     * @param entity
     * @return
     */
    @Override
    public String update(GatewayDynamicRoute entity) {
        int i = gatewayDynamicRouteMapper.updateGatewayDynamicRoute(entity, entity.getRouteId());
        return "success";
    }

    /**
     * 新增路由配置
     * @param entity
     * @return
     */
    @Override
    public String saveOne(GatewayDynamicRoute entity) {
        System.out.println("entity--->"+entity.toString());
        entity.setEnable(Objects.isNull(entity.getEnable())?"1":entity.getEnable());
        int i = gatewayDynamicRouteMapper.insertGatewayDynamicRoute(entity);
        if (i > 0) {
            /**
             * 数据库新增成功后，新增到redis中
             */
            //this.saveOrUpdateToRedis(entity);
            return entity.getRouteId();
        }
        return null;
    }


    /**
     * 更新路由信息
     * @param entity
     * @param routeId
     * @return
     */
    private String updateT(GatewayDynamicRoute entity, String routeId) {
        entity.setEnable(Objects.isNull(entity.getEnable())?"1":entity.getEnable());
        int i = gatewayDynamicRouteMapper.updateGatewayDynamicRoute(entity, routeId);
        if (i > 0) {
            if ("1".equals(entity.getEnable())) {
                this.saveOrUpdateToRedis(entity);
            }else {
                this.delFromRedis(routeId);
            }
            return routeId;
        }
        return null;
    }

    /**
     * 新增或更新路由信息 至 redis 中
     * @param entity
     * @return
     */
    private boolean saveOrUpdateToRedis(GatewayDynamicRoute entity){
        try {
            RouteDefinition r = new MysqlRouteDefinitionRepository().setRouteDefinition(entity);
            System.out.println("添加或更新(saveOrUpdateToRedis)路由信息到redis,数据为{}"+JSON.toJSONString(r));
            //redisService.hmSet(MysqlRouteDefinitionRepository.GATEWAY_ROUTES,r.getId(),r);
            return true;
        }catch (Exception e){
            System.out.println("添加或更新(saveOrUpdateToRedis)路由信息到redis,发生错误"+e.getMessage());
            return false;
        }

    }

    /**
     * 从redis中删除 路由信息
     * @param routeId
     * @return
     */
    private boolean delFromRedis(String  routeId){
        try {
            //redisService.delhm(MysqlRouteDefinitionRepository.GATEWAY_ROUTES,routeId);
            System.out.println("删除(delFromRedis)路由信息到redis,routeId 数据为{}"+routeId);
            return true;
        }catch (Exception e){
            System.out.println("删除(delFromRedis)路由信息到redis,发生错误，信息为{}"+e.getMessage());
            return false;
        }
    }
}
