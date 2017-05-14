package lt.platform.lunar.logger.celebrities;

import lt.platform.lunar.logger.common.NotFoundException;
import lt.platform.lunar.logger.url.CrawlURLService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
class CelebritiesService {

    private final CelebrityRepository celebrityRepository;
    private final CrawlURLService crawlURLService;

    CelebritiesService(CelebrityRepository celebrityRepository, CrawlURLService crawlURLService) {
        this.celebrityRepository = celebrityRepository;
        this.crawlURLService = crawlURLService;
    }

    @Transactional(readOnly = true)
    CelebrityDto findOne(Long id) {
        return toDto(celebrityRepository.findOne(id));
    }

    @Transactional
    CelebrityDto create(CelebrityDto celebrity) {
        if (!crawlURLService.exists(celebrity.getSourceUrl())) {
            throw new NotFoundException();
        }
        return toDto(celebrityRepository.save(fromDto(celebrity)));
    }

    @Transactional(readOnly = true)
    List<CelebrityDto> findByUrl(String url) {
        return celebrityRepository.findAllBySourceUrl(url)
            .stream()
            .map(CelebritiesService::toDto)
            .collect(Collectors.toList());
    }

    private static CelebrityDto toDto(Celebrity celebrity) {
        CelebrityDto dto = new CelebrityDto();
        dto.setId(celebrity.getId());
        dto.setSourceUrl(celebrity.getSourceUrl());
        dto.setFirstName(celebrity.getFirstName());
        dto.setLastName(celebrity.getLastName());
        dto.setAddress(celebrity.getAddress());
        return dto;
    }

    private static Celebrity fromDto(CelebrityDto dto) {
        Celebrity celebrity = new Celebrity();
        celebrity.setId(dto.getId());
        celebrity.setSourceUrl(dto.getSourceUrl());
        celebrity.setFirstName(dto.getFirstName());
        celebrity.setLastName(dto.getLastName());
        celebrity.setAddress(dto.getAddress());
        return celebrity;
    }

}
