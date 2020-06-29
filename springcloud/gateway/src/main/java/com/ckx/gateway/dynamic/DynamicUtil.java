package com.ckx.gateway.dynamic;

import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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
