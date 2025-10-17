package com.example.bssm_dev.domain.api.requester;

public interface Requester {
    Object get(String endpoint);
    Object post(String endpoint);
    Object put(String endpoint);
    Object patch(String endpoint);
    Object delete(String endpoint);
}
