package com.example.bssm_dev.domain.api.requester;

public interface Requester {
    Object get();
    Object post();
    Object put();
    Object patch();
    Object delete();
}
