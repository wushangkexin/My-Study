package com.ckx.gateway.route;

import com.alibaba.fastjson.JSONArray;
import com.ckx.gateway.entity.DynamicRouteVo;
import com.ckx.gateway.entity.GatewayDynamicRoute;
import com.ckx.gateway.service.IGatewayDynamicRouteService;
import com.ckx.gateway.struts.DynamicRouteStruts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * RouteDefinitionRepository 继承了RouteDefinitionWriter，是 Spring Cloud Gateway官方预留的接口。
 * 从而可以通过下面两种方式来实现集群下的动态路由修改：RouteDefinitionWriter 接口和 RouteDefinitionRepository 接口。
 * 在这里推荐实现RouteDefinitionRepository 这个接口，从数据库或者从配置中心获取路由进行动态配置。
 */
public class MysqlRouteDefinitionRepository implements RouteDefinitionRepository {
    public static final String  GATEWAY_ROUTES = "geteway_routes";
    @Autowired
    private IGatewayDynamicRouteService routeService;
    /*@Autowired
    private RedisService redisService;*/

    /**
     * Gateway启动的时候，会加载这个方法
     * @return
     */
    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        List<RouteDefinition> routeList = null;
        //获取redis是否存在路由信息
        //Object obj = redisService.hmGetAll(GATEWAY_ROUTES);
        Object obj = null;
        if (Objects.isNull(obj)) {
            //从数据库获取路由信息
            List<GatewayDynamicRoute> listByEnable = routeService.getListByEnable("1");
            if (listByEnable != null && listByEnable.size() > 0){
                //转换成 RouteDefinition 集合后，返回
                routeList = this.toRouteList(/*redisService,*/listByEnable);
            }
        }

        return Flux.fromIterable(routeList);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }

    /**
     * 转换成 List<RouteDefinition>
     * @param redisService
     * @param listByEnable
     * @return
     */
    private List<RouteDefinition> toRouteList(/*RedisService redisService,*/List<GatewayDynamicRoute> listByEnable){
        List<RouteDefinition> routeList = new ArrayList<>();
        /**
         * 循环转换：
         * 因为数据库中，Predicates 和 Filters 存储的 json字符串。所以，得先转换成 对应的 vo.
         * 然后在转换成 List<PredicateDefinition>和 List<FilterDefinition>
         */

        listByEnable.stream().forEach(gw->{
            RouteDefinition r = this.setRouteDefinition(gw);
            routeList.add(r);
            //redisService.hmSet(GATEWAY_ROUTES,r.getId(),r);
        });
        return routeList;
    }

    public RouteDefinition setRouteDefinition(GatewayDynamicRoute gw){
        RouteDefinition r = new RouteDefinition();
        r.setUri(DynamicUtil.getUri(gw.getUri()));
        r.setOrder(gw.getSortIndex());
        r.setId(gw.getRouteId());
        r.setPredicates(DynamicRouteStruts.INSTANCES.toPredicateDefinition(JSONArray.parseArray(gw.getPredicates(), DynamicRouteVo.PredicateDefinitionVo.class)));
        r.setFilters(DynamicRouteStruts.INSTANCES.toFilterDefinition(JSONArray.parseArray(gw.getFilters(),DynamicRouteVo.FilterDefinitionVo.class)));
        return r;
    }
}
