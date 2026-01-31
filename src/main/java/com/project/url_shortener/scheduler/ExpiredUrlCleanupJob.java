package com.project.url_shortener.scheduler;

import com.project.url_shortener.entity.Url;
import com.project.url_shortener.recycle.ExpiredCodeQueue;
import com.project.url_shortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExpiredUrlCleanupJob {
    private final UrlRepository urlRepository;
    private final ExpiredCodeQueue expiredCodeQueue;

    @Scheduled(fixedDelay = 3*60*60*1000)
    public void recycleExpiredUrls(){
        List<Url> expiredUrls=urlRepository.findByExpiresAtBefore(LocalDateTime.now());

        for(Url url:expiredUrls){
            expiredCodeQueue.add(url.getShortCode());
            urlRepository.delete(url);
        }
    }
}
