package com.ckx.gateway.route;

import com.ckx.gateway.struts.RouteDefinitionStruts;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class DynamicUtil {
    public static final String HTTP="http";

    public static RouteDefinition getRouteDefinition(GatewayRouteDefinition definition) {
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(definition.getId());
        routeDefinition.setOrder(definition.getOrder());
        try {
            routeDefinition.setUri(new URI(definition.getUri()));
        }catch (URISyntaxException e){
            throw new RuntimeException("route uri 转换失败");
        }

        List<FilterDefinition> fList= RouteDefinitionStruts.INSTANCES.toFilter(definition.getFilters());
        routeDefinition.setFilters(fList);
        List<PredicateDefinition> dList= RouteDefinitionStruts.INSTANCES.toPredicate(definition.getPredicates());
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
