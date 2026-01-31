package com.project.url_shortener.service;

import com.project.url_shortener.cache.UrlCache;
import com.project.url_shortener.entity.Counter;
import com.project.url_shortener.entity.Url;
import com.project.url_shortener.exception.BadRequestException;
import com.project.url_shortener.exception.GoneException;
import com.project.url_shortener.exception.NotFoundException;
import com.project.url_shortener.recycle.ExpiredCodeQueue;
import com.project.url_shortener.repository.CounterRepository;
import com.project.url_shortener.repository.UrlRepository;
import com.project.url_shortener.util.Base62Encoder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UrlService {
    private final UrlRepository urlRepository;
    private final CounterRepository counterRepository;
    private final Base62Encoder encoder;
    private final UrlCache urlCache;
    private final ExpiredCodeQueue expiredCodeQueue;

    public Url shorten(String originalUrl , String alias){

        //no alias
        if (alias == null || alias.isBlank()) {

            Optional<Url> existing = urlRepository.findByOriginalUrl(originalUrl);

            if (existing.isPresent()) {
                Url url = existing.get();

                if (url.getExpiresAt().isAfter(LocalDateTime.now())) {
                    return url;
                }
            }
        }

        //alias provided
        if (alias != null && !alias.isBlank()) {

            String normalizedAlias = alias.toLowerCase();

            urlRepository.findByShortCode(normalizedAlias).ifPresent(u -> {
                if (u.getExpiresAt().isAfter(LocalDateTime.now())) {
                    throw new BadRequestException("Custom alias already in use");
                }
            });

            Url url = new Url();
            url.setOriginalUrl(originalUrl);
            url.setShortCode(normalizedAlias);
            url.setExpiresAt(LocalDateTime.now().plusDays(7));

            return urlRepository.save(url);
        }

        //generate new short code
        String shortCode=expiredCodeQueue.poll();
        if(shortCode!=null){
            if(urlRepository.findByShortCode(shortCode).isPresent()){
                shortCode=null;
            }
        }
        if(shortCode==null){
            Counter counter=counterRepository.findById(1L)
                    .orElseGet(()->{
                        Counter c = new Counter();
                        c.setCounterValue(100000L);
                        return counterRepository.save(c);
                    });
            long next=counter.getCounterValue()+1;
            counter.setCounterValue(next);

            shortCode=encoder.encode(next);
        }

        Url url=new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortCode(shortCode);
        url.setExpiresAt(LocalDateTime.now().plusDays(7));

        return urlRepository.save(url);
    }

    public String resolve(String shortCode){
        //cache hit
        String cached=urlCache.get(shortCode);
        if(cached!=null){
            return cached;
        }
        //db lookup
        Url url=urlRepository.findByShortCode(shortCode)
                .orElseThrow(()->new NotFoundException("Short URL not found"));

        if(url.getExpiresAt().isBefore(LocalDateTime.now())){
            urlCache.remove(shortCode);
            throw new GoneException("Short URL has expired");
        }
        System.out.println(url.getOriginalUrl());
        urlCache.put(shortCode,url.getOriginalUrl());

        return url.getOriginalUrl();
    }
}
