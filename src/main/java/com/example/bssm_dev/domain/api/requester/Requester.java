package com.example.bssm_dev.domain.api.requester;

public interface Requester {
    Object get(String endpoint);
    Object post(String endpoint, Object body);
    Object put(String endpoint, Object body);
    Object patch(String endpoint, Object body);
    Object delete(String endpoint);
}
