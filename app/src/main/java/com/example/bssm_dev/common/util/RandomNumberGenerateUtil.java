package com.example.bssm_dev.common.util;

import java.util.UUID;

public class RandomNumberGenerateUtil {
    public static String generate(int length) {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, length);
    }
}
