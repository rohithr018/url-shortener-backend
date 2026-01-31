package com.project.url_shortener.recycle;

import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class ExpiredCodeQueue {
    private final Queue<String> queue= new ConcurrentLinkedQueue<>();

    public void add(String shortCode){
        queue.offer(shortCode);
    }
    public String poll(){
        return queue.poll();
    }

    public int size(){
        return queue.size();
    }
}
