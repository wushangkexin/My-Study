package com.ckx.gateway.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class RouteVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Data
    public static class RouteBean implements Serializable {
        private static final long serialVersionUID = 1L;

        private String routeId;

        private String uri;

        private Integer sortIndex;

        private List<PredicateDefinitionVo> predicates;

        private List<FilterDefinitionVo> filters;

        private String flag;
    }

    /**
     * 过滤器vo
     */
    @Data
    public static class FilterDefinitionVo implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private Map<String, String> args = new LinkedHashMap();
    }

    /**
     * 过滤器vo
     */
    @Data
    public static class PredicateDefinitionVo implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private Map<String, String> args = new LinkedHashMap();
    }

    @Data
    public static class RouteBeanList implements Serializable {
        private static final long serialVersionUID = 1L;

        private List<RouteBean> list;
    }

}
