package com.example.gateway.entity;
import java.util.Objects;

public class Endpoint {
    private  String url;
    private HttpMethod httpMethod;
    private UserType role;

    public Endpoint(String url, HttpMethod httpMethod, UserType role) {
        this.url = url;
        this.httpMethod = httpMethod;
        this.role = role;
    }

    public String getUrl() {
        return url;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public UserType getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Endpoint endpoint = (Endpoint) o;
        return Objects.equals(url, endpoint.url) &&
                httpMethod == endpoint.httpMethod &&
                Objects.equals(role, endpoint.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, httpMethod, role);
    }
}