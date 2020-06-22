package com.ckx.gateway.route;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }
}
