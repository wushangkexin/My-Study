package com.ckx.gateway.dynamic;

import com.ckx.gateway.entity.Route;
import com.ckx.gateway.mapper.RouteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class DynamicRouteServiceImpl implements ApplicationEventPublisherAware {
    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;
    @Autowired
    private RouteMapper routeMapper;

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     * 增加路由
     * @param routeDefinition
     * @return
     */
    public String add(RouteDefinition routeDefinition){
        routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
        this.doLoad();
        return "success";
    }

    /**
     * 更新路由
     */
    public String update(RouteDefinition definition) {
        try {
            this.routeDefinitionWriter.delete(Mono.just(definition.getId()));
        } catch (Exception e) {
            return "update fail,not find route  routeId: " + definition.getId();
        }
        try {
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            this.doLoad();
            return "success";
        } catch (Exception e) {
            return "update route  fail";
        }
    }


    /**
     * 删除路由
     *
     */
    public String delete(String id) {
        try {
            this.routeDefinitionWriter.delete(Mono.just(id)).subscribe();
            this.doLoad();
        } catch (Exception e) {
            e.printStackTrace();
            return "delete fail,not find route  routeId: " + id;
        }
        return "delete success";
    }

    /**
     * 从数据库中读取路由信息
     * @return
     */
    public String loadMysql(){
        List<Route> list = routeMapper.getRouteList();
        for (int i = 0;i<list.size();i++){
            routeDefinitionWriter.save(Mono.just(DynamicUtil.getRouteDefinition(list.get(i)))).subscribe();
        }
        this.doLoad();
        return "load success";
    }

    /**
     * 重新刷新 路由
     */
    public String doLoad() {
        try {
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
        }catch (Exception e){
            e.printStackTrace();
            return "load fail";
        }
        return "load success";
    }

}
