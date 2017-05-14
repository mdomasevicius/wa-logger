package lt.platform.lunar.logger.url;

import lt.platform.lunar.logger.celebrities.CelebrityMappers;
import lt.platform.lunar.logger.common.NotFoundException;
import lt.platform.lunar.logger.common.RecordAlreadyExistsException;
import lt.platform.lunar.logger.key.RemoteKey;
import lt.platform.lunar.logger.key.RemoteKeyDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Service
class DefaultCrawlURLService implements CrawlURLService {

    private final CrawlURLRepository crawlURLRepository;

    DefaultCrawlURLService(CrawlURLRepository crawlURLRepository) {
        this.crawlURLRepository = crawlURLRepository;
    }

    @Transactional
    @Override
    public CrawlURLDto create(CrawlURLDto crawlURL) {
        if (crawlURLRepository.findByUrl(crawlURL.getUrl()) != null) {
            throw new RecordAlreadyExistsException();
        }

        return toDto(crawlURLRepository.save(fromDto(crawlURL)));
    }

    @Transactional(readOnly = true)
    @Override
    public CrawlURLDto findOne(Long id) {
        return Optional.ofNullable(crawlURLRepository.findOne(id))
            .map(DefaultCrawlURLService::toDto)
            .orElseThrow(NotFoundException::new);
    }

    @Override
    public CrawlURLDto findOne(String url) {
        return Optional.ofNullable(crawlURLRepository.findByUrl(url))
            .map(DefaultCrawlURLService::toDto)
            .orElseThrow(NotFoundException::new);
    }

    @Transactional
    @Override
    public void update(CrawlURLDto crawlUrl) {
        Assert.notNull(crawlUrl.getId(), "id must not be null");

        crawlURLRepository.save(fromDto(crawlUrl));
    }

    @Override
    public boolean exists(String url) {
        return crawlURLRepository.existsByUrl(url);
    }

    @Override
    public boolean exists(Long id) {
        return crawlURLRepository.exists(id);
    }

    @Override
    public List<CrawlURLDto> findAll(boolean unfinishedOnly) {
        if (unfinishedOnly) {
            return crawlURLRepository.findAllUnfinished()
                .stream()
                .map(DefaultCrawlURLService::toDto)
                .collect(toList());
        } else  {
            Iterable<CrawlURL> allUrls = crawlURLRepository.findAll();
            return StreamSupport.stream(allUrls.spliterator(), false)
                .map(DefaultCrawlURLService::toDto)
                .collect(toList());
        }
    }

    private static CrawlURLDto toDto(CrawlURL crawlURL) {
        CrawlURLDto dto = new CrawlURLDto();
        dto.setId(crawlURL.getId());
        dto.setUrl(crawlURL.getUrl());

        RemoteKeyDto remoteKeyDto = Optional.ofNullable(crawlURL.getRemoteKey())
            .map(k -> new RemoteKeyDto(k.getKey()))
            .orElse(null);
        dto.setRemoteKey(remoteKeyDto);

        crawlURL.getCelebrities().forEach(c -> dto.addCelebrity(CelebrityMappers.toDto(c)));

        return dto;
    }

    private static CrawlURL fromDto(CrawlURLDto dto) {
        CrawlURL url = new CrawlURL();
        url.setId(dto.getId());
        url.setUrl(dto.getUrl());

        RemoteKey remoteKey = Optional.ofNullable(dto.getRemoteKey())
            .map(k -> {
                RemoteKey rk = new RemoteKey();
                rk.setKey(k.getKey());
                return rk;
            }).orElse(null);
        url.setRemoteKey(remoteKey);

        dto.getCelebrities().forEach(c -> url.addCelebrity(CelebrityMappers.fromDto(c)));

        return url;
    }
}
