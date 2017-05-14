package lt.platform.lunar.logger.celebrities;

import lt.platform.lunar.logger.CollectionResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

@RequestMapping("/api/url/{urlId}/celebrities")
@RestController
class CelebrityRest {

    private final CelebrityService celebritiesService;

    CelebrityRest(CelebrityService celebritiesService) {
        this.celebritiesService = celebritiesService;
    }

    @GetMapping
    ResponseEntity<CollectionResource<CelebrityResource>> fetchCelebsByUrl(@PathVariable("urlId") Long urlId) {
        CollectionResource<CelebrityResource> collection = new CollectionResource<>(celebritiesService.findByUrl(urlId)
            .stream()
            .map(CelebrityResource::toResource)
            .collect(toList()));
        return ok(collection);
    }

    @PostMapping
    ResponseEntity<Void> create(
        @PathVariable("urlId") Long urlId,
        @Valid @RequestBody CelebrityResource request
    ) {
        celebritiesService.create(urlId, request.toDto());
        return ok().build();
    }

}
