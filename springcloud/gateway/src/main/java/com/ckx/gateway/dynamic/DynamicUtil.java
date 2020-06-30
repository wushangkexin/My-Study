package com.ckx.gateway.dynamic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ckx.gateway.entity.Route;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DynamicUtil {
    public static final String HTTP="http";

    /**
     * 将页面数据类（GatewayRouteDefinition）转为路由实体类RouteDefinition
     * @param definition
     * @return
     */
    public static RouteDefinition getRouteDefinition(GatewayRouteDefinition definition) {
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(definition.getRouteId());
        routeDefinition.setOrder(definition.getSortIndex());
        try {
            routeDefinition.setUri(new URI(definition.getUri()));
        }catch (URISyntaxException e){
            throw new RuntimeException("route uri 转换失败");
        }
        List<GatewayFilterDefinition> gList = definition.getFilters();
        List<FilterDefinition> fList= new ArrayList<>();
        for (int i = 0;i<gList.size();i++){
            GatewayFilterDefinition gFilterDefinition = gList.get(i);
            FilterDefinition filterDefinition = new FilterDefinition();
            filterDefinition.setName(gFilterDefinition.getName());
            filterDefinition.setArgs(gFilterDefinition.getArgs());
            fList.add(filterDefinition);
        }
        routeDefinition.setFilters(fList);
        List<GatewayPredicateDefinition> gpList = definition.getPredicates();
        List<PredicateDefinition> dList= new ArrayList<>();
        for (int i = 0;i<gpList.size();i++){
            GatewayPredicateDefinition gPredicateDefinition = gpList.get(i);
            PredicateDefinition predicateDefinition = new PredicateDefinition();
            predicateDefinition.setName(gPredicateDefinition.getName());
            predicateDefinition.setArgs(gPredicateDefinition.getArgs());
            dList.add(predicateDefinition);
        }
        routeDefinition.setPredicates(dList);
        return routeDefinition;
    }

    /**
     * 将数据库实体类Route 转为路由实体类RouteDefinition
     * @param route
     * @return
     */
    public static RouteDefinition getRouteDefinition(Route route) {
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
        r.setPredicates(predicateDefinitionList);
        JSONArray filter_ja = JSONArray.parseArray(route.getFilters());
        List<FilterDefinition> filterDefinitionList = new ArrayList<>();
        for (int i = 0;i<filter_ja.size();i++){
            JSONObject filter_jo = (JSONObject)filter_ja.get(i);
            FilterDefinition filterDefinition = new FilterDefinition();
            filterDefinition.setName((String) filter_jo.get("name"));
            filterDefinition.setArgs((Map<String, String>)filter_jo.get("args"));
            filterDefinitionList.add(filterDefinition);
        }
        r.setFilters(filterDefinitionList);
        return r;
    }

    /**
     * 将页面数据类（GatewayRouteDefinition）转为数据库实体类Route
     * @param definition
     * @return
     */
    public static Route getRoute(GatewayRouteDefinition definition) {
        Route route = new Route();
        route.setRouteId(definition.getRouteId());
        route.setUri(definition.getUri());
        route.setSortIndex(definition.getSortIndex());
        route.setPredicates(JSONArray.toJSON(definition.getPredicates()).toString());
        route.setFilters(JSONArray.toJSON(definition.getFilters()).toString());
        return route;
    }

    /**
     * 将页面数据类（GatewayRouteDefinition）转为数据库实体类RouteDefinition
     * @param definition
     * @return
     */
    public static Route getRouteByDefinition(RouteDefinition definition) {
        Route route = new Route();
        route.setRouteId(definition.getId());
        route.setUri(definition.getUri().toString());
        route.setSortIndex(definition.getOrder());
        route.setPredicates(JSONArray.toJSON(definition.getPredicates()).toString());
        route.setFilters(JSONArray.toJSON(definition.getFilters()).toString());
        return route;
    }


    public static URI getUri(String uriStr){
        URI uri;
        if(uriStr.startsWith(DynamicUtil.HTTP)){
            //http地址
            uri = UriComponentsBuilder.fromHttpUrl(uriStr).build().toUri();
        }else{
            //注册中心
            uri = UriComponentsBuilder.fromUriString(uriStr).build().toUri();
        }
        return uri;
    }
}
