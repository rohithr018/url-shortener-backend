package com.project.url_shortener.scheduler;

import com.project.url_shortener.cache.UrlCache;
import com.project.url_shortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
public class CacheCleanupJob {
    private final UrlRepository urlRepository;
    private final UrlCache urlCache;

    @Scheduled(cron="0 0 0 */2 * ?")
    public void cleanup(){
        urlRepository.findAll().forEach(
                url -> {
                    if(url.getExpiresAt().isBefore(LocalDateTime.now())){
                        urlCache.remove(url.getShortCode());
                    }
                }
        );
    }


}
