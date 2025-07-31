package com.example.gateway.entity;
import com.example.auth.entity.UserRole;
import com.example.gateway.entity.HttpMethod;
import java.util.Objects;

public class Endpoint {
    private  String url;
    private HttpMethod httpMethod;
    private UserRole role;

    public Endpoint(String url, HttpMethod httpMethod, UserRole role) {
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

    public UserRole getRole() {
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