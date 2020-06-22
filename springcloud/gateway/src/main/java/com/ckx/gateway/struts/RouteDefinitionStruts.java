package com.ckx.gateway.struts;

import com.ckx.gateway.route.GatewayFilterDefinition;
import com.ckx.gateway.route.GatewayPredicateDefinition;
import org.mapstruct.factory.Mappers;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;

import java.util.List;

public interface RouteDefinitionStruts {
    RouteDefinitionStruts INSTANCES = Mappers.getMapper(RouteDefinitionStruts.class);

    PredicateDefinition toPredicate(GatewayPredicateDefinition definition);

    List<PredicateDefinition> toPredicate(List<GatewayPredicateDefinition> definition);

    FilterDefinition toFilter(GatewayFilterDefinition definition);

    List<FilterDefinition> toFilter(List<GatewayFilterDefinition> definition);
}
