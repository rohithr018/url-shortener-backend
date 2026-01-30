package com.project.url_shortener.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name="urls",
        uniqueConstraints = @UniqueConstraint(columnNames = "shortCode")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 2048)
    private String originalUrl;

    @Column(nullable = false)
    private String shortCode;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime createdAt = LocalDateTime.now();
}
