package com.ckx.gateway.mapper;

import com.ckx.gateway.entity.Route;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RouteMapper {
    List<Route> getRouteList() throws Exception;

    int insertRoute(Route route) throws Exception;

    int updateRoute(String routeId ,Route route) throws Exception;
}
