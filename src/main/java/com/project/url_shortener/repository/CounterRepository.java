package com.project.url_shortener.repository;

import com.project.url_shortener.entity.Counter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterRepository extends JpaRepository <Counter,Long>{
}
