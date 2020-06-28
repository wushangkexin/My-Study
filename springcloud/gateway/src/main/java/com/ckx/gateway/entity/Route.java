package com.ckx.gateway.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Route implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 路由Id
     */
    private String routeId;

    /**
     * 路由规则转发的uri
     */
    private String uri;

    /**
     * 路由的执行顺序
     */
    private Integer sortIndex;

    /**
     * 路由断言集合配置json串
     */
    private String predicates;

    /**
     * 路由过滤器集合配置json串
     */
    private String filters;

    /**
     * 状态：0,"不可用")；1,"可用")
     */
    private String flag;
}
