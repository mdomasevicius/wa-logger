package lt.platform.lunar.logger.url;

import lt.platform.lunar.logger.CollectionResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RequestMapping("/api/url")
@RestController
class CrawlURLRest {

    private final CrawlURLService crawlURLService;

    CrawlURLRest(CrawlURLService crawlURLService) {
        this.crawlURLService = crawlURLService;
    }

    @GetMapping
    ResponseEntity<CollectionResource<CrawlURLResource>> fetchCrawlUrlList(
        @RequestParam(required = false, defaultValue = "false") boolean unfinishedOnly
    ) {
        List<CrawlURLResource> entries = crawlURLService.findAll(unfinishedOnly)
            .stream()
            .map(CrawlURLResource::toResource)
            .collect(toList());
        return ok(new CollectionResource<>(entries));
    }

    @GetMapping("/{id}")
    ResponseEntity<CrawlURLResource> findOne(@PathVariable Long id) {
        CrawlURLResource resource = CrawlURLResource.toResource(crawlURLService.findOne(id));
        return ok(resource);
    }

    @PostMapping
    ResponseEntity<Void> create(@RequestBody @Valid CrawlURLResource request) {
        CrawlURLDto dto = crawlURLService.create(request.toDto());
        URI uri = linkTo(methodOn(CrawlURLRest.class).findOne(dto.getId())).toUri();
        return created(uri).build();
    }

}
