package lt.platform.lunar.logger.celebrities;

import lt.platform.lunar.logger.common.NotFoundException;
import lt.platform.lunar.logger.url.CrawlURL;
import lt.platform.lunar.logger.url.CrawlURLRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
class DefaultCelebritiesService implements CelebrityService {

    private final CelebrityRepository celebrityRepository;
    private final CrawlURLRepository crawlURLRepository;

    public DefaultCelebritiesService(CelebrityRepository celebrityRepository, CrawlURLRepository crawlURLRepository) {
        this.celebrityRepository = celebrityRepository;
        this.crawlURLRepository = crawlURLRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public CelebrityDto findOne(Long id) {
        return CelebrityMappers.toDto(celebrityRepository.findOne(id));
    }

    @Transactional
    @Override
    public void create(Long urlId, CelebrityDto celebrity) {
        if (!crawlURLRepository.exists(urlId)) {
            throw new NotFoundException();
        }

        Celebrity savedCelebrity = celebrityRepository.save(CelebrityMappers.fromDto(celebrity));

        CrawlURL url = crawlURLRepository.findOne(urlId);
        url.addCelebrity(savedCelebrity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CelebrityDto> findByUrl(Long urlId) {
        return crawlURLRepository.findOne(urlId)
            .getCelebrities()
            .stream()
            .map(CelebrityMappers::toDto)
            .collect(toList());
    }

}
