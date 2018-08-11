package com.mall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * guava缓存
 */
@Slf4j
public class TokenCache {
    public static final String TOKEN_PREFIX = "token_";

    /**
     * initialCapacity      缓存初始化容量
     * maximumSize          最大容量，超过时会采用LRU(最小使用)算法移除缓存项
     * expireAfterAccess    有效期
     */
    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder()
            .initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                //默认数据加载实现，调用get取值key没有对应值时调用此方法
                @Override
                public String load(String s) throws Exception {
                    //默认返回null，避免空指针异常返回字符串
                    return "null";
                }
            });
    public static void setKey(String key,String value){
        localCache.put(key,value);
    }
    public static String getKey(String key){
        String value = null;
        try {
            value = localCache.get(key);
            if ("null".equals(value)){
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            log.error("localCache get error",e);
            //e.printStackTrace();
        }
        return  null;
    }
}
