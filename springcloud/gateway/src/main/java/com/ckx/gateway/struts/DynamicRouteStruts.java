package com.ckx.gateway.struts;

import com.ckx.gateway.entity.GatewayDynamicRoute;
import com.ckx.gateway.entity.DynamicRouteVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;

import java.util.List;

@Mapper(uses = {StringToMapMapper.class})
public interface DynamicRouteStruts {
    DynamicRouteStruts INSTANCES = Mappers.getMapper(DynamicRouteStruts.class);

    FilterDefinition toFilterDefinition(DynamicRouteVo.FilterDefinitionVo filterDefinitionVo);

    List<FilterDefinition> toFilterDefinition(List<DynamicRouteVo.FilterDefinitionVo> filterDefinitionVo);

    PredicateDefinition toPredicateDefinition(DynamicRouteVo.PredicateDefinitionVo predicateDefinitionVo);

    List<PredicateDefinition> toPredicateDefinition(List<DynamicRouteVo.PredicateDefinitionVo> predicateDefinitionVos);

    @Mappings({
            @Mapping(source="routeId", target="routeId"),
            @Mapping(source="uri", target="uri"),
            @Mapping(source="sortIndex", target="sortIndex"),
            @Mapping(source="predicates", target="predicates"),
            @Mapping(source="filters", target="filters"),
            @Mapping(source="enable", target="enable")
    })
    GatewayDynamicRoute toGatewayDynamicRoute(DynamicRouteVo.RouteBean routeBean);

    List<GatewayDynamicRoute> toGatewayDynamicRoute(List<DynamicRouteVo.RouteBean> routeBean);
}
