package com.taskmanager.util;

public final class Constants {

    private Constants() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final String API_V1_PREFIX = "/api/v1";
    public static final String AUTH_ENDPOINT = "/api/v1/auth/**";
    public static final String SWAGGER_ENDPOINTS = "/swagger-ui/**";
    public static final String V3_API_DOCS_ENDPOINTS = "/v3/api-docs/**";
    public static final String ACTUATOR_ENDPOINTS = "/actuator/**";
    public static final String H2_CONSOLE = "/h2-console/**";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String MAX_PAGE_SIZE = "100";
}
