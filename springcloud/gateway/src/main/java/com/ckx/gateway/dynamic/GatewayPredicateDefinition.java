package com.ckx.gateway.dynamic;

import lombok.Data;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class GatewayPredicateDefinition implements Serializable {
    private static final long serialVersionUID = 1116611165045260463L;
    /**
     * 断言对应的Name
     */
    private String name;

    /**
     * 配置的断言规则
     */
    private Map<String,String> args = new LinkedHashMap<>();
}
