package com.spring.jwt.FilterController;

import com.spring.jwt.SparePart.SpareFilterDto;
import com.spring.jwt.exception.PageNotFoundException;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/Filter")
public class FilterController {

    private final  FilterService filterService;

    public FilterController(FilterService filterService) {
        this.filterService = filterService;
    }

    @GetMapping("/searchBarFilter")
    public ResponseEntity<?> searchBarFilter(
            @RequestParam String searchBarInput,
            @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {
        try {
            long startRequestTime = System.currentTimeMillis();
            String cacheKey = searchBarInput.toLowerCase();
            String etag = String.format("W/\"%s\"", cacheKey.hashCode());
            if (ifNoneMatch != null && ifNoneMatch.equals(etag)) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                        .eTag(etag)
                        .build();
            }
            List<SpareFilterDto> sparePartDtos = filterService.searchBarFilter(searchBarInput);
            long requestDuration = System.currentTimeMillis() - startRequestTime;
            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(Duration.ofSeconds(60)).mustRevalidate().cachePublic())
                    .eTag(etag)
                    .header("X-Response-Time-Ms", String.valueOf(requestDuration))
                    .header("X-Total-Count", String.valueOf(sparePartDtos.size()))
                    .header("X-Compression", "enabled")
                    .header("Access-Control-Expose-Headers", "X-Total-Count, X-Response-Time-Ms, X-Compression")
                    .body(sparePartDtos);
        } catch (PageNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/search")
    public List<SpareFilterDto> searchSpareParts(@RequestParam String keyword) {
        return filterService.searchSpareParts(keyword, 40);
    }
}
