package com.ckx.gateway.dynamic;

import com.ckx.common.redis.util.RedisUtils;
import com.ckx.gateway.entity.Route;
import com.ckx.gateway.service.RouteService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class MysqlRouteDefinitionRepository implements RouteDefinitionRepository {
    public static final String  GATEWAY_ROUTES = "geteway_routes";
    @Autowired
    private RouteService routeService;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * Gateway启动的时候，会加载这个方法
     * @return
     */
    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        Object obj = redisUtils.hmget(GATEWAY_ROUTES);
        List<RouteDefinition> routeList = new ArrayList<>();
        if (Objects.isNull(obj) || MapUtils.isEmpty((Map<?, ?>) obj)) {
            System.out.println("从数据库获取路由信息");
            List<Route> list = routeService.getRouteList();
            if (CollectionUtils.isNotEmpty(list)){
                //转换成 RouteDefinition 集合后，返回
                routeList = this.toRouteList(redisUtils,list);
            }
        }else {
            System.out.println("从redis获取路由信息");
            // map转list
            List<Route> list = ((Map<String, Route>)obj).entrySet().parallelStream().map(map->map.getValue()).collect(Collectors.toList());
            for (int i = 0;i<list.size();i++){
                routeList.add(DynamicUtil.getRouteDefinition(list.get(i)));
            }
        }
        return Flux.fromIterable(routeList);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        System.out.println("调用save方法");
        return route.flatMap(routeDefinition -> {
            Route r = DynamicUtil.getRouteByDefinition(routeDefinition);
            redisUtils.hset(GATEWAY_ROUTES,routeDefinition.getId(),r);
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        System.out.println("调用delete方法");
        return routeId.flatMap(id -> {
            Route route = (Route)redisUtils.hget(GATEWAY_ROUTES, id);
            if (route != null) {
                redisUtils.hdel(GATEWAY_ROUTES, id);
                return Mono.empty();
            }
            return Mono.defer(() -> Mono.error(new NotFoundException("路由文件没有找到: " + routeId)));
        });
    }

    /**
     * 转换成 List<RouteDefinition>
     * @param redisUtils
     * @param list
     * @return
     */
    private List<RouteDefinition> toRouteList(RedisUtils redisUtils,List<Route> list){
        List<RouteDefinition> routeList = new ArrayList<>();
        /**
         * 循环转换：
         * 因为数据库中，Predicates 和 Filters 存储的 json字符串。所以，得先转换成 对应的 vo.
         * 然后在转换成 List<PredicateDefinition>和 List<FilterDefinition>
         */
        list.stream().forEach(route->{
            RouteDefinition r = DynamicUtil.getRouteDefinition(route);
            routeList.add(r);
            redisUtils.hset(GATEWAY_ROUTES,r.getId(),route);
        });
        return routeList;
    }

}
