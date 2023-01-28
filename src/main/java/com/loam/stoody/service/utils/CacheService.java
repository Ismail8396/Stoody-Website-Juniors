package com.loam.stoody.service.utils;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.loam.stoody.global.constants.MiscConstants;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class CacheService {
    private static final LoadingCache cacheInstance;

    static {
        cacheInstance = CacheBuilder.newBuilder().
                expireAfterWrite(MiscConstants.verificationTimeoutMinute, TimeUnit.MINUTES)
                .build(new CacheLoader() {
                    @Override
                    public Object load(Object o) throws Exception {
                        return 0;
                    }
                });
    }

    public static void put(String key, Object value) {
        cacheInstance.put(key, value);
    }

    @SneakyThrows
    public static Object get(String key) {
        return cacheInstance.getIfPresent(key);
    }

    public static boolean isValid(String token, String username) {
        var _loadingCache = CacheService.get(username);
        if (_loadingCache == null) return false;

        String savedToken = _loadingCache.toString();
        if (savedToken == null) return false;

        return savedToken.equals(token);
    }

    public static Integer generateRandomOTP(){
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }
}
