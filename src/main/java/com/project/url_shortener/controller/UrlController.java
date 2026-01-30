package com.project.url_shortener.controller;

import com.project.url_shortener.entity.Url;
import com.project.url_shortener.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;

    @PostMapping("/shorten")
    public Map<String,String> shorten(
            @RequestBody Map<String,String> body
    ){
        Url url=urlService.shorten(body.get("url"),body.get("alias"));
        return Map.of(
                "shortUrl",url.getShortCode()
        );
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code){
        String originalUrl=urlService.resolve(code);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }
}
