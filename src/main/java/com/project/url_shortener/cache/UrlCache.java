package com.project.url_shortener.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UrlCache {
    private final Map<String,String> cache=new ConcurrentHashMap<>();

    public String get(String shortCode){
        return cache.get(shortCode);
    }
    public void put(String shortCode,String originalUrl){
        cache.put(shortCode,originalUrl);
    }
    public void remove(String shortCode){
        cache.remove(shortCode);
    }
    public void clear(){
        cache.clear();
    }

}
