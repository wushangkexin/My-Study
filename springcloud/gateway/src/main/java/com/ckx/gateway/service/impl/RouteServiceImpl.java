package com.ckx.gateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ckx.gateway.entity.Route;
import com.ckx.gateway.mapper.RouteMapper;
import com.ckx.gateway.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteServiceImpl implements RouteService {
    @Autowired
    private RouteMapper routeMapper;

    public List<Route> getRouteList(){
        return routeMapper.getRouteList();
    }

    public String insertRoute(Route route){
        JSONObject obj = new JSONObject();
        int i = routeMapper.insertRoute(route);
        if(i>0){
            obj = setResult(true,"操作成功！");
        }else {
            obj = setResult(false,"系统繁忙！！");
        }
        return obj.toString();
    }

    public String updateRoute(Route route){
        JSONObject obj = new JSONObject();
        int i = routeMapper.updateRoute(route.getRouteId(),route);
        if(i>0){
            obj = setResult(true,"操作成功！");
        }else {
            obj = setResult(false,"系统繁忙！！");
        }
        return obj.toString();
    }

    public String deleteRoute(String routeId){
        JSONObject obj = new JSONObject();
        int i = routeMapper.deleteRoute(routeId);
        if(i>0){
            obj = setResult(true,"操作成功！");
        }else {
            obj = setResult(false,"系统繁忙！！");
        }
        return obj.toString();
    }

    private JSONObject setResult(boolean flag,String msg){
        JSONObject obj = new JSONObject();
        obj.put("flag",flag);
        obj.put("msg",msg);
        return obj;
    }

}
