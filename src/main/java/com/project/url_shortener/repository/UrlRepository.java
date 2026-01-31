package com.project.url_shortener.repository;

import com.project.url_shortener.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url,Long> {
    Optional<Url> findByShortCode(String shortcode);
    Optional<Url> findByOriginalUrl(String originalUrl);
    List<Url> findByExpiresAtBefore(LocalDateTime time);
}
