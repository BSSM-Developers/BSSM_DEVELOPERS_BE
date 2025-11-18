package com.example.bssm_dev.domain.api.requester;

public interface Requester {
    Object get(String endpoint, java.util.Map<String, String> headers);
    Object post(String endpoint, Object body, java.util.Map<String, String> headers);
    Object put(String endpoint, Object body, java.util.Map<String, String> headers);
    Object patch(String endpoint, Object body, java.util.Map<String, String> headers);
    Object delete(String endpoint, java.util.Map<String, String> headers);
}
