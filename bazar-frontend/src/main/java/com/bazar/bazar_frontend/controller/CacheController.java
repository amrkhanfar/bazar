package com.bazar.bazar_frontend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bazar.bazar_frontend.cache.InMemoryCache;

@RestController
@RequestMapping("/cache")
public class CacheController {

    @Autowired
    private InMemoryCache cache;

    @DeleteMapping("/invalidate/{id}")
    public void invalidate(@PathVariable int id) {
        cache.invalidate("book:" + id);
    }
}