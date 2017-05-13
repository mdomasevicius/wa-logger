package lt.lunar.platform.logger.url;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

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

    @GetMapping("/{id}")
    ResponseEntity<CrawlURLResource> findOne(@PathVariable Long id) {
        CrawlURLResource resource = CrawlURLResource.toResource(crawlURLService.findOne(id));
        return ok(resource);
    }

    @PostMapping
    ResponseEntity create(
        @RequestBody @Valid CrawlURLResource request,
        @RequestHeader("Crawler-Id") String crawlerId
    ) {
        CrawlURLDto dto = crawlURLService.create(crawlerId, request.toDto());
        URI uri = linkTo(methodOn(CrawlURLRest.class).findOne(dto.getId())).toUri();
        return created(uri).build();
    }

}
