package lt.lunar.platform.logger.key;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RequestMapping("/api/url/{urlId}/remote-key")
@RestController
class RemoteKeyRest {

    private final RemoteKeyService remoteKeyService;

    RemoteKeyRest(RemoteKeyService remoteKeyService) {
        this.remoteKeyService = remoteKeyService;
    }

    @GetMapping
    ResponseEntity<RemoteKeyResource> findOne(
        @PathVariable Long urlId,
        @RequestHeader("Crawler-Id") String crawlerId
    ) {
        return ok(RemoteKeyResource.toResource(remoteKeyService.findOne(crawlerId, urlId)));
    }

    @PostMapping
    ResponseEntity<Void> createKey(
        @PathVariable Long urlId,
        @RequestHeader("Crawler-Id") String crawlerId,
        @RequestBody @Valid RemoteKeyResource request
    ) {
        remoteKeyService.createKeyForUrl(crawlerId, urlId, request.getRemoteKey());
        return created(linkTo(methodOn(RemoteKeyRest.class).findOne(urlId, crawlerId)).toUri()).build();
    }

}
