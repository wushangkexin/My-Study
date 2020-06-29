package com.ckx.gateway.dynamic;

import lombok.Data;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class GatewayFilterDefinition implements Serializable {
    private static final long serialVersionUID = 1116611165045260463L;
    /**
     * 过滤器对应的Name
     */
    private String name;

    /**
     * 对应的路由规则
     */
    private Map<String,String> args = new LinkedHashMap<>();
}
