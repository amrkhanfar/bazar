package com.bazar.bazar_frontend.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.bazar.bazar_frontend.Dto.BookInfoDto;
import com.bazar.bazar_frontend.Dto.BookSearchDto;
import com.bazar.bazar_frontend.cache.InMemoryCache;
import com.bazar.bazar_frontend.load_balancer.RoundRobinLoadBalancer;
import com.bazar.bazar_frontend.model.Book;

@RestController
public class FrontendController {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${catalog.service.url}")
	private String catalogServiceUrl;
	
	@Value("${order.service.url}")
	private String orderServiceUrl;
	
    @Autowired
    InMemoryCache cache;
    
    @Autowired
    RoundRobinLoadBalancer lb;
	
    // ----------- SEARCH BY TOPIC ---------------
    @GetMapping("/search/{topic}")
    public ResponseEntity<?> search(@PathVariable String topic) {

        /* cache key is *only* the topic string */
        return cache.get("topic:" + topic, List.class)
               .<ResponseEntity<?>>map(ResponseEntity::ok)
               .orElseGet(() -> {

           String url = lb.nextCatalog() + "/catalog/query/topic/" + topic;
           ResponseEntity<Book[]> resp;
           try { resp = restTemplate.getForEntity(url, Book[].class); }
           catch (RestClientException ex) {
               return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                       .body("Catalog replicas unreachable");
           }

           List<BookSearchDto> out = Arrays.stream(resp.getBody())
               .map(b -> BookSearchDto.builder().id(b.getId()).title(b.getName()).build())
               .collect(Collectors.toList());

           cache.put("topic:" + topic, out);
           return ResponseEntity.ok(out);
        });
    }

    // ----------- INFO BY ID ---------------
    @GetMapping("/info/{id}")
    public ResponseEntity<?> info(@PathVariable int id) {

        return cache.get("book:" + id, BookInfoDto.class)
               .<ResponseEntity<?>>map(ResponseEntity::ok)
               .orElseGet(() -> {

           String url = lb.nextCatalog() + "/catalog/query/id/" + id;
           Book b;
           try { b = restTemplate.getForObject(url, Book.class); }
           catch (HttpClientErrorException.NotFound ex) {
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No book with id " + id);
           } catch (RestClientException ex) {
               return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                       .body("Catalog replicas unreachable");
           }

           BookInfoDto dto = BookInfoDto.builder()
                                .title(b.getName())
                                .price(b.getPrice())
                                .quantity(b.getQuantity())
                                .build();
           cache.put("book:" + id, dto);
           return ResponseEntity.ok(dto);
        });
    }

    // -------------- PURCHASE ----------------
    @PostMapping("/purchase/{id}")
    public ResponseEntity<?> purchase(@PathVariable int id) {

        String url = lb.nextOrder() + "/order/purchase/id/" + id;
        String reply;
        try { reply = restTemplate.postForObject(url, null, String.class); }
        catch (HttpClientErrorException.NotFound ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No book with id " + id);
        } catch (RestClientException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Order service unreachable");
        }

        return ResponseEntity.ok(reply);
    }
}
