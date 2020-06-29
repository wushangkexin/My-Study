package com.ckx.gateway.dynamic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ckx.common.redis.util.RedisUtils;
import com.ckx.gateway.entity.Route;
import com.ckx.gateway.service.RouteService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
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
        System.out.println("自动加载路由信息");
        Object obj = redisUtils.get(GATEWAY_ROUTES);
        List<RouteDefinition> routeList = null;
        if (Objects.isNull(obj) || MapUtils.isEmpty((Map<?, ?>) obj)) {
            System.out.println("从数据库获取路由信息");
            List<Route> list = routeService.getRouteList();
            redisUtils.set(GATEWAY_ROUTES,list);
            if (CollectionUtils.isNotEmpty(list)){
                //转换成 RouteDefinition 集合后，返回
                routeList = this.toRouteList(redisUtils,list);
            }
        }else {
            System.out.println("从redis获取路由信息");
            // map转list
            routeList = ((Map<String, RouteDefinition>)obj).entrySet().parallelStream().map(map->map.getValue()).collect(Collectors.toList());
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
            RouteDefinition r = this.setRouteDefinition(route);
            routeList.add(r);
            redisUtils.set(r.getId(),r);
        });
        return routeList;
    }

    public RouteDefinition setRouteDefinition(Route route){
        RouteDefinition r = new RouteDefinition();
        r.setId(route.getRouteId());
        r.setUri(DynamicUtil.getUri(route.getUri()));
        r.setOrder(route.getSortIndex());
        JSONArray predicate_ja = JSONArray.parseArray(route.getPredicates());
        List<PredicateDefinition> predicateDefinitionList = new ArrayList<>();
        for (int i = 0;i<predicate_ja.size();i++){
            JSONObject predicate_jo = (JSONObject)predicate_ja.get(i);
            PredicateDefinition predicateDefinition = new PredicateDefinition();
            predicateDefinition.setName((String) predicate_jo.get("name"));
            predicateDefinition.setArgs((Map<String, String>)predicate_jo.get("args"));
            predicateDefinitionList.add(predicateDefinition);
        }
        JSONArray filter_ja = JSONArray.parseArray(route.getFilters());
        List<FilterDefinition> FilterDefinitionList = new ArrayList<>();
        for (int i = 0;i<filter_ja.size();i++){
            JSONObject filter_jo = (JSONObject)filter_ja.get(i);
            FilterDefinition filterDefinition = new FilterDefinition();
            filterDefinition.setName((String) filter_jo.get("name"));
            filterDefinition.setArgs((Map<String, String>)filter_jo.get("args"));
            FilterDefinitionList.add(filterDefinition);
        }
        return r;
    }
}
